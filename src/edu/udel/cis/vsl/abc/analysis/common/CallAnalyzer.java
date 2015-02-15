package edu.udel.cis.vsl.abc.analysis.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CivlForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Given an AST, determines caller/callee relationships among functions.
 * 
 * Calls through a function pointer are approximated by the set of functions whose type
 * matches the function pointer type.
 * 
 * This could be improved by various forms of points-to analysis.
 * 
 * @author dwyer
 * 
 */
public class CallAnalyzer implements Analyzer {
	/*
	 * This is work in progress.  Ultimately, these caller/callee relationships should
	 * be embedded into the AST node.  Still thinking about which node to put it in, e.g.,
	 * FunctionDefinitionNode or FunctionDeclarationNode or both?
	 */
	private Map<Function, Set<Entity>> callees;
	private Map<Function, Set<Entity>> callers;
	
	/*
	 * Traverse program
	 *    - collect function declarations
	 * 
	 * Traverse function bodies
	 *    - let f be the function
	 *    - for each call, c, do
	 *       - add f to calls(c)
	 *       - add c to calledFrom(f)
	 *       
	 * What is the unique defining entity for a function
	 *     - extract it from a call node
	 *     - extract a set that matches the type
	 *    
	 */
	
	// Handle FunctionTypeNode as well

	private void processFunctionDefinitionNode(FunctionDefinitionNode funcNode) {
		// children: identifier, type, contract (optional), body
		FunctionTypeNode typeNode = funcNode.getTypeNode();
		SequenceNode<ContractNode> contract = funcNode.getContract();
		CompoundStatementNode body = funcNode.getBody();
		SequenceNode<VariableDeclarationNode> paramsNode = typeNode
				.getParameters();
		Function fEntity = funcNode.getEntity();
		
		// For now just collect up the expressions in FunctionCallNodes
		Set<Entity> myCallees;
		if (callees.keySet().contains(fEntity)) {
			myCallees = callees.get(fEntity);
		} else {
			myCallees = new HashSet<Entity>();
			callees.put(fEntity, myCallees);
		}
		
		System.out.println("Processing definition of function "+fEntity);

		processFunctionBody(body, myCallees);
	}

	private void processFunctionDeclarationNode(FunctionDeclarationNode funcNode) {
		// children: ident, type, contract.
		FunctionTypeNode typeNode = funcNode.getTypeNode();
		SequenceNode<ContractNode> contract = funcNode.getContract();
		SequenceNode<VariableDeclarationNode> paramsNode = typeNode
				.getParameters();

		// TBD	
	}
	
	private void processFunctionTypeNode(FunctionTypeNode funcTypeNode) {
		// children: ???

		// TBD	
	}
	
	private void processFunctionBody(ASTNode node, Set<Entity> callees) {
		if (node instanceof FunctionCallNode) {
			FunctionCallNode fcn = (FunctionCallNode)node;
			
			
			if (fcn.getFunction() instanceof IdentifierExpressionNode) {
				IdentifierNode calledFunId = ((IdentifierExpressionNode)fcn.getFunction()).getIdentifier();
				callees.add(calledFunId.getEntity());
			} else {
				System.out.println("Found indirect call to : "+fcn.getFunction());
			}
			
			
			// Check arguments for nested calls
			Iterable<ExpressionNode> args = fcn.getArguments();
			for (ExpressionNode arg : args) {
				processFunctionBody(arg, callees);
			}
		} else if (node != null) {
			Iterable<ASTNode> children = node.children();
			for (ASTNode child : children) {
				processFunctionBody(child, callees);
			}
		}
	}

	private void processProgram(ASTNode node) {
		if (node instanceof FunctionDefinitionNode) {
			processFunctionDefinitionNode((FunctionDefinitionNode)node);
		} else if (node != null) {
			Iterable<ASTNode> children = node.children();
			for (ASTNode child : children) {
				processProgram(child);
			}
		}
	}


	// Exported methods...

	@Override
	public void clear(AST unit) {
		// TBD
	}

	@Override
	public void analyze(AST unit) throws SyntaxException {
		ASTNode root = unit.getRootNode();
		callees = new HashMap<Function, Set<Entity>>();
		processProgram(root);
		for (Function caller : callees.keySet()) {
			if (!callees.get(caller).isEmpty()) {
				System.out.println("Function "+caller+" calls:");
				System.out.println("   "+callees.get(caller));
			}
		}

	}

}
