package edu.udel.cis.vsl.abc.analysis.common;

import java.util.Iterator;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ScopeParameterizedTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Given an AST, determines and sets scope of every node.
 * 
 * Scope structure of a function definition with scope parameters:
 * 
 * <pre>
 * { ...current parent scope...
 *   identifier
 *   { block scope:
 *     type node - formals (including scopeParams, return type)
 *     { function scope:
 *       function defn node
 *       labels
 *       { block scope: formals
 *         { block scope: contract if contract!=null }
 *         body (will be block scope because is compound stmt)
 *       }
 *     }
 *   }
 *   ...current parent scope...
 * }
 * </pre>
 * 
 * In the above: the identifier is the name of the function. The scopeParams is
 * the sequence node consisting of variable declaration nodes for the scope
 * parameters. In "type node - formals" the "-" means "minus", i.e., we want the
 * type node and all its descendants other than the formals in that scope; this
 * includes the type node for the return type of the function and the scope
 * parameters.
 * 
 * Scope structure of a function definition without scope parameters: very
 * similar but without the extra block scope:
 * 
 * <pre>
 * { ...current parent scope...
 *   identifier
 *   type node - formals (including return type)
 *   { function scope:
 *     labels
 *     { block scope: formals
 *       { block scope: contract if contract!=null }
 *       body (will be block scope because is compound stmt)
 *     }
 *   }
 *   ...current parent scope...
 * }
 * </pre>
 * 
 * Scope structure of a function declaration with scope parameters:
 * 
 * <pre>
 * { ...current parent scope...
 *   identifier
 *   { block scope:
 *     typeNode - formals (including scopeParams, return type)
 *     { function prototype scope: formals
 *       { block scope: contract }
 *     }
 *   }
 *   ...current parent scope...
 * }
 * </pre>
 * 
 * Scope structure for function declaration without scope parameters:
 * 
 * <pre>
 * { ...current parent scope...
 *   identifier
 *   typeNode - formals
 *   { function prototype scope: formals
 *     { block scope: contract }
 *   }
 *   ...current parent scope...
 * }
 * </pre>
 * 
 * Scope structure of a parameterized scope type node used any place other than
 * the situations above:
 * 
 * <pre>
 * { ...current parent scope...
 *   { block scope: scopeParams
 *     body type
 *   }
 *   ...current parent scope...
 * }
 * </pre>
 * 
 * 
 * @author siegel
 * 
 */
public class ScopeAnalyzer implements Analyzer {

	public static void setScopes(AST ast, EntityFactory scopeFactory)
			throws SyntaxException {
		(new ScopeAnalyzer(scopeFactory)).analyze(ast);
	}

	private EntityFactory scopeFactory;

	private int currentId = 0;

	public ScopeAnalyzer(EntityFactory scopeFactory) {
		this.scopeFactory = scopeFactory;
	}

	private void processFunctionDefinitionNode(FunctionDefinitionNode funcNode,
			Scope parentScope) throws SyntaxException {
		// children: identifier, type, contract (optional), body
		IdentifierNode identifier = funcNode.getIdentifier();
		TypeNode typeNode = funcNode.getTypeNode();
		SequenceNode<ContractNode> contract = funcNode.getContract();
		CompoundStatementNode body = funcNode.getBody();
		FunctionTypeNode funcTypeNode;
		SequenceNode<VariableDeclarationNode> paramsNode;
		Scope typeScope, parameterScope, newFunctionScope;

		if (typeNode.kind() == TypeNodeKind.SCOPE_PARAMETERIZED) {
			ScopeParameterizedTypeNode scopedNode = (ScopeParameterizedTypeNode) typeNode;

			typeScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					typeNode);
			funcTypeNode = (FunctionTypeNode) scopedNode.getBody();
		} else {
			typeScope = parentScope;
			funcTypeNode = (FunctionTypeNode) typeNode;
		}
		newFunctionScope = scopeFactory.newScope(ScopeKind.FUNCTION, typeScope,
				funcNode);
		paramsNode = funcTypeNode.getParameters();
		parameterScope = scopeFactory.newScope(ScopeKind.BLOCK,
				newFunctionScope, paramsNode);
		if (paramsNode != null)
			processRecursive(paramsNode, parameterScope, null);
		if (contract != null) {
			Scope contractScope = scopeFactory.newScope(ScopeKind.CONTRACT,
					parameterScope, contract);

			processRecursive(contract, contractScope, null);
		}
		processRecursive(typeNode, typeScope, null);
		processNode(body, parameterScope, newFunctionScope);
		processRecursive(funcNode, parentScope, null);
	}

	private void processFunctionDeclarationNode(
			FunctionDeclarationNode funcNode, Scope parentScope)
			throws SyntaxException {
		// children: ident, type, contract.
		IdentifierNode identifier = funcNode.getIdentifier();
		TypeNode typeNode = funcNode.getTypeNode();
		SequenceNode<ContractNode> contract = funcNode.getContract();
		FunctionTypeNode funcTypeNode;
		SequenceNode<VariableDeclarationNode> paramsNode;
		Scope typeScope, parameterScope;

		if (typeNode.kind() == TypeNodeKind.SCOPE_PARAMETERIZED) {
			ScopeParameterizedTypeNode scopedNode = (ScopeParameterizedTypeNode) typeNode;

			typeScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					typeNode);
			funcTypeNode = (FunctionTypeNode) scopedNode.getBody();
		} else {
			typeScope = parentScope;
			funcTypeNode = (FunctionTypeNode) typeNode;
		}
		paramsNode = funcTypeNode.getParameters();
		parameterScope = scopeFactory.newScope(ScopeKind.BLOCK, typeScope,
				paramsNode);
		if (paramsNode != null)
			processRecursive(paramsNode, parameterScope, null);
		if (contract != null) {
			Scope contractScope = scopeFactory.newScope(ScopeKind.CONTRACT,
					parameterScope, contract);

			processRecursive(contract, contractScope, null);
		}
		processRecursive(typeNode, typeScope, null);
		processRecursive(funcNode, parentScope, null);
	}

	/**
	 * Performs scope analysis on a node and all its decendants, but back tracks
	 * if it encounters a node that already has a non-null scope. I.e., if a
	 * node has a non-null scope, it and all of its descendants are left alone.
	 * 
	 * @param node
	 *            an AST node
	 * @param parentScope
	 *            the current scope we are in when the given node is reached;
	 *            may be null if node is the root node, i.e., the first node in
	 *            the AST
	 * @param functionScope
	 *            the function scope for the current function we are "in" when
	 *            we reach this node; this is used only for LabelNodes as these
	 *            must go into the first containing function scope; may be null
	 *            if the node and all its descendants could not possibly have a
	 *            label
	 * @throws SyntaxException
	 *             if AST is malformed in some way
	 */
	private void processNode(ASTNode node, Scope parentScope,
			Scope functionScope) throws SyntaxException {

		if (node.getScope() != null)
			return;
		if (parentScope == null) {
			parentScope = scopeFactory.newScope(ScopeKind.FILE, null, node);
		} else if (node instanceof FunctionDefinitionNode) {
			processFunctionDefinitionNode((FunctionDefinitionNode) node,
					parentScope);
			return;
		} else if (node instanceof CompoundStatementNode) {
			parentScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					node);
		} else if (node instanceof SwitchNode) {
			ASTNode body = ((SwitchNode) node).getBody();
			Scope bodyScope;

			parentScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					node);
			bodyScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					body);
			processRecursive(body, bodyScope, functionScope);
		} else if (node instanceof IfNode) {
			ASTNode trueBranch = ((IfNode) node).getTrueBranch();
			ASTNode falseBranch = ((IfNode) node).getFalseBranch();
			Scope trueBranchScope;

			parentScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					node);
			trueBranchScope = scopeFactory.newScope(ScopeKind.BLOCK,
					parentScope, trueBranch);
			processRecursive(trueBranch, trueBranchScope, functionScope);
			if (falseBranch != null) {
				Scope falseBranchScope = scopeFactory.newScope(ScopeKind.BLOCK,
						parentScope, falseBranch);

				processRecursive(falseBranch, falseBranchScope, functionScope);
			}
		} else if (node instanceof LoopNode) {
			ASTNode body = ((LoopNode) node).getBody();
			Scope bodyScope;

			parentScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					node);
			bodyScope = scopeFactory.newScope(ScopeKind.BLOCK, parentScope,
					body);
			processRecursive(body, bodyScope, functionScope);
		} else if (node instanceof FunctionDeclarationNode) {
			processFunctionDeclarationNode((FunctionDeclarationNode) node,
					parentScope);
			return;
		} else if (node instanceof FunctionTypeNode) {
			// a function type node may occur outside of a function
			// declaration/definition, e.g., as a type name...
			ASTNode parameters = ((FunctionTypeNode) node).getParameters();

			if (parameters != null && parameters.getScope() == null) {
				Scope prototypeScope = scopeFactory.newScope(
						ScopeKind.FUNCTION_PROTOTYPE, parentScope, parameters);

				processRecursive(parameters, prototypeScope, functionScope);
			}
		} else if (node instanceof OrdinaryLabelNode) {
			parentScope = functionScope;
		}
		processRecursive(node, parentScope, functionScope);
	}

	private void setIds(Scope scope) {
		if (scope.getId() >= 0) {
			return;
		} else {
			Iterator<Scope> children = scope.getChildrenScopes();

			scope.setId(currentId);
			currentId++;
			while (children.hasNext())
				setIds(children.next());
		}
	}

	/**
	 * Assigns the given scope to the given node and then invokes method
	 * {@link #processNode} to all the children.
	 * 
	 * @param node
	 *            an ASTNode which does not yet have a Scope
	 * @param scope
	 *            the scope that will be assigned to the given node
	 * @param functionScope
	 *            the function scope we are currently in
	 * @throws SyntaxException
	 *             if problem in AST
	 */
	private void processRecursive(ASTNode node, Scope scope, Scope functionScope)
			throws SyntaxException {
		Iterator<ASTNode> children = node.children();

		assert scope != null;
		node.setScope(scope);
		while (children.hasNext()) {
			ASTNode child = children.next();

			if (child != null)
				processNode(child, scope, functionScope);
		}
	}

	private void clearNode(ASTNode node) {
		if (node != null) {
			Iterator<ASTNode> children = node.children();

			node.setScope(null);
			while (children.hasNext())
				clearNode(children.next());
		}
	}

	// Exported methods...

	@Override
	public void clear(AST unit) {
		clearNode(unit.getRootNode());
	}

	@Override
	public void analyze(AST unit) throws SyntaxException {
		ASTNode root = unit.getRootNode();

		processNode(root, null, null);
		setIds(root.getScope());
	}

}
