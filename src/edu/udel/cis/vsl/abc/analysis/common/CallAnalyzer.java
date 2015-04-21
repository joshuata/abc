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
	 * Traverse program
	 *    - collect function declarations
	 * 
	 * Traverse function bodies
	 *    - let f be the function
	 *    - for each call, c, do
	 *       - add f to calls(c)
	 *       - add c to calledFrom(f)
	 */
	
	// Handle FunctionTypeNode as well

	private void processFunctionDefinitionNode(FunctionDefinitionNode funcNode) {
		// children: identifier, type, contract (optional), body
		CompoundStatementNode body = funcNode.getBody();
		Function fEntity = funcNode.getEntity();
		
		processFunctionBody(body, fEntity);
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
	
	private void processFunctionBody(ASTNode node, Function caller) {
		if (node instanceof FunctionCallNode) {
			FunctionCallNode fcn = (FunctionCallNode)node;
			
			if (fcn.getFunction() instanceof IdentifierExpressionNode) {
				IdentifierNode calledFunId = ((IdentifierExpressionNode)fcn.getFunction()).getIdentifier();
				if (calledFunId.getEntity() instanceof Function) {
					Function callee = (Function)calledFunId.getEntity();
					caller.getCallees().add(callee);
					callee.getCallers().add(caller);
				} else {
					assert false : "CallAnalyzer expected id in function call node to be a function";
				}
			} else {
				System.out.println("Found indirect call to : "+fcn.getFunction());
			}
			
			
			// Check arguments for nested calls
			Iterable<ExpressionNode> args = fcn.getArguments();
			for (ExpressionNode arg : args) {
				processFunctionBody(arg, caller);
			}
		} else if (node != null) {
			Iterable<ASTNode> children = node.children();
			for (ASTNode child : children) {
				processFunctionBody(child, caller);
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
	
	private void printCallGraph(ASTNode node) {
		if (node instanceof FunctionDefinitionNode) {
			Function f = ((FunctionDefinitionNode)node).getEntity();
			System.out.println("Function "+f+" is called by:");
			for (Function caller : f.getCallers()) {
				System.out.println("   "+caller);
			}
			if (f.getCallers().isEmpty()) 
				System.out.println("   nothing");
			System.out.println("and calls:");
			for (Function callee : f.getCallees()) {
				System.out.println("   "+callee);
			}
			if (f.getCallees().isEmpty()) 
				System.out.println("   nothing");
		} else if (node != null) {
			Iterable<ASTNode> children = node.children();
			for (ASTNode child : children) {
				printCallGraph(child);
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
		processProgram(root);
		printCallGraph(root);

	}

}
