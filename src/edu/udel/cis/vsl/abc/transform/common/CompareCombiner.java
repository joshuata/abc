package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode.NodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Combiner;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

/**
 * Combine two ASTs to form a new AST to be used for comparison. The
 * CompareCombiner treats the first argument to combine() as the specification
 * program, and the second argument as the implementation.
 * 
 * @author zirkel
 * 
 */
public class CompareCombiner implements Combiner {

	private NodeFactory factory;

	private ASTFactory astFactory;

	private Source specSource;

	private Source implSource;

	@Override
	public AST combine(AST unit0, AST unit1) throws SyntaxException {
		SequenceNode<ExternalDefinitionNode> specRoot = unit0.getRootNode();
		SequenceNode<ExternalDefinitionNode> implRoot = unit1.getRootNode();
		SequenceNode<ExternalDefinitionNode> newRoot;
		Map<String, VariableDeclarationNode> inputVariables;
		Map<String, VariableDeclarationNode> specOutputs;
		Map<String, VariableDeclarationNode> implOutputs;
		FunctionDefinitionNode specSystem;
		FunctionDefinitionNode implSystem;
		FunctionDefinitionNode newMain;
		List<BlockItemNode> newMainBodyNodes = new ArrayList<BlockItemNode>();
		List<ExternalDefinitionNode> nodes = new ArrayList<ExternalDefinitionNode>();
		Transformer nameTransformer;
		TypedefDeclarationNode fileTypeDef = null;
		FunctionDeclarationNode equalsFunc = null;

		astFactory = unit0.getASTFactory();
		unit0.release();
		unit1.release();
		fileTypeDef = this.getAndRemoveFileTypeNode(specRoot);
		if (fileTypeDef != null)
			this.removeFileTypeNode(implRoot);
		else
			fileTypeDef = this.getAndRemoveFileTypeNode(implRoot);
		if (fileTypeDef != null)
			nodes.add(fileTypeDef);
		equalsFunc = this.getAndRemoveEqualsFuncNode(specRoot);
		nodes.add(equalsFunc);
		unit0 = astFactory.newAST(specRoot);
		unit1 = astFactory.newAST(implRoot);
		specSource = this.getMainSource(specRoot);
		implSource = this.getMainSource(implRoot);
		factory = astFactory.getNodeFactory();
		inputVariables = combineInputs(specRoot, implRoot);
		nodes.add(equalsFunctionNode(specSource));
		nodes.addAll(inputVariables.values());
		specOutputs = outputs(specRoot);
		implOutputs = outputs(implRoot);
		checkOutputs(specOutputs, implOutputs);
		// systemFunctions = combineSystemFunctions(specRoot, implRoot);
		// nodes.addAll(systemFunctions.values());
		nameTransformer = new NameTransformer(renameVariables(
				specOutputs.values(), "_spec"), astFactory);
		unit0 = nameTransformer.transform(unit0);
		// TODO: Check consistency of assumptions
		specSystem = wrapperFunction(specSource, specRoot, "system_spec");
		implSystem = wrapperFunction(implSource, implRoot, "system_impl");
		for (VariableDeclarationNode v : specOutputs.values()) {
			v.getTypeNode().setOutputQualified(false);
			nodes.add(v.copy());
		}
		for (VariableDeclarationNode v : implOutputs.values()) {
			v.getTypeNode().setOutputQualified(false);
			nodes.add(v.copy());
		}
		nodes.add(specSystem);
		nodes.add(implSystem);

		newMainBodyNodes.add(factory.newVariableDeclarationNode(specSource,
				factory.newIdentifierNode(specSource, "_isEqual"),
				factory.newBasicTypeNode(specSource, BasicTypeKind.BOOL)));

		newMainBodyNodes.add(factory.newExpressionStatementNode(factory
				.newFunctionCallNode(specSource, factory
						.newIdentifierExpressionNode(specSource, factory
								.newIdentifierNode(specSource, "system_spec")),
						new ArrayList<ExpressionNode>(), null)));
		newMainBodyNodes.add(factory.newExpressionStatementNode(factory
				.newFunctionCallNode(implSource, factory
						.newIdentifierExpressionNode(implSource, factory
								.newIdentifierNode(implSource, "system_impl")),
						new ArrayList<ExpressionNode>(), null)));
		// TODO: spawn and join (but calls ok until joint assertions
		// implemented)
		newMainBodyNodes.addAll(outputAssertions(specOutputs, implOutputs));
		newMain = factory
				.newFunctionDefinitionNode(
						specSource,
						factory.newIdentifierNode(specSource, "main"),
						factory.newFunctionTypeNode(
								specSource,
								factory.newVoidTypeNode(specSource),
								factory.newSequenceNode(
										specSource,
										"Formals",
										new ArrayList<VariableDeclarationNode>()),
								false), factory.newSequenceNode(specSource,
								"Contract", new ArrayList<ContractNode>()),
						factory.newCompoundStatementNode(specSource,
								newMainBodyNodes));
		nodes.add(newMain);
		newRoot = factory.newSequenceNode(
				astFactory.getTokenFactory().join(specSource, implSource),
				"Composite System", nodes);
		return astFactory.newAST(newRoot);
	}

	private FunctionDeclarationNode equalsFunctionNode(Source specSource) {
		IdentifierNode name = factory.newIdentifierNode(specSource, "$equals");
		FunctionTypeNode funcType = factory
				.newFunctionTypeNode(
						specSource,
						factory.newBasicTypeNode(specSource, BasicTypeKind.BOOL),
						factory.newSequenceNode(
								specSource,
								"Formals",
								Arrays.asList(
										factory.newVariableDeclarationNode(
												specSource,
												factory.newIdentifierNode(
														specSource, "first"),
												factory.newPointerTypeNode(
														specSource,
														factory.newVoidTypeNode(specSource))),
										factory.newVariableDeclarationNode(
												specSource,
												factory.newIdentifierNode(
														specSource, "second"),
												factory.newPointerTypeNode(
														specSource,
														factory.newVoidTypeNode(specSource)))))

						, false);

		return factory.newFunctionDeclarationNode(specSource, name, funcType,
				null);
	}

	private TypedefDeclarationNode getAndRemoveFileTypeNode(
			SequenceNode<ExternalDefinitionNode> root) {
		int numChildren = root.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ExternalDefinitionNode def = root.getSequenceChild(i);

			if (def != null && def instanceof TypedefDeclarationNode) {
				TypedefDeclarationNode typeDefNode = (TypedefDeclarationNode) def;
				String sourceFile = typeDefNode.getSource().getFirstToken()
						.getSourceFile().getName();

				if (sourceFile.equals("stdio.cvl")
						&& typeDefNode.getName().equals("$file")) {
					typeDefNode.parent().removeChild(typeDefNode.childIndex());
					return typeDefNode;
				}
			}
		}
		return null;
	}

	private FunctionDeclarationNode getAndRemoveEqualsFuncNode(
			SequenceNode<ExternalDefinitionNode> root) {
		int numChildren = root.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ExternalDefinitionNode def = root.getSequenceChild(i);

			if (def != null && def instanceof FunctionDeclarationNode) {
				FunctionDeclarationNode funcDec = (FunctionDeclarationNode) def;
				String sourceFile = funcDec.getSource().getFirstToken()
						.getSourceFile().getName();

				if (sourceFile.equals("pointer.cvh")
						&& funcDec.getName().equals("$equals")) {
					funcDec.parent().removeChild(funcDec.childIndex());
					return funcDec;
				}
			}
		}
		return null;
	}

	private void removeFileTypeNode(SequenceNode<ExternalDefinitionNode> root) {
		int numChildren = root.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ExternalDefinitionNode def = root.getSequenceChild(i);

			if (def != null && def instanceof TypedefDeclarationNode) {
				TypedefDeclarationNode typeDefNode = (TypedefDeclarationNode) def;
				String sourceFile = typeDefNode.getSource().getFirstToken()
						.getSourceFile().getName();

				if (sourceFile.equals("stdio.cvl")
						&& typeDefNode.getName().equals("$file")) {
					typeDefNode.parent().removeChild(typeDefNode.childIndex());
					return;
				}
			}
		}
	}

	private Map<String, VariableDeclarationNode> combineInputs(
			ASTNode specRoot, ASTNode implRoot) {
		Map<String, VariableDeclarationNode> inputs = new LinkedHashMap<String, VariableDeclarationNode>();

		// Put all input variables from the implementation into the map.
		for (int i = 0; i < implRoot.numChildren(); i++) {
			ASTNode child = implRoot.child(i);

			if (child != null
					&& child.nodeKind() == NodeKind.VARIABLE_DECLARATION) {
				VariableDeclarationNode var = (VariableDeclarationNode) child;

				if (var.getTypeNode().isInputQualified()) {
					inputs.put(var.getName(), var.copy());
				}
			}
		}
		// Check that all input variables from the spec were also in the impl.
		for (int i = 0; i < specRoot.numChildren(); i++) {
			ASTNode child = specRoot.child(i);

			if (child != null
					&& child.nodeKind() == NodeKind.VARIABLE_DECLARATION) {
				VariableDeclarationNode var = (VariableDeclarationNode) child;

				if (var.getTypeNode().isInputQualified()) {
					if (!inputs.containsKey(var.getName())) {
						throw new ASTException(
								"Implementation program does not contain specification input variable "
										+ var.getName() + ".");
					}
				}
			}
		}
		return inputs;
	}

	private Map<String, VariableDeclarationNode> outputs(ASTNode root) {
		Map<String, VariableDeclarationNode> outputs = new LinkedHashMap<String, VariableDeclarationNode>();

		// Put all output variables into the map.
		for (int i = 0; i < root.numChildren(); i++) {
			ASTNode child = root.child(i);

			if (child != null
					&& child.nodeKind() == NodeKind.VARIABLE_DECLARATION) {
				VariableDeclarationNode var = (VariableDeclarationNode) child;

				if (var.getTypeNode().isOutputQualified()) {
					outputs.put(var.getName(), var);
				}
			}
		}
		return outputs;
	}

	private void checkOutputs(Map<String, VariableDeclarationNode> specOutputs,
			Map<String, VariableDeclarationNode> implOutputs) {
		if (specOutputs.size() != implOutputs.size()) {
			throw new ASTException(
					"Specification and implementation must have the same output variables.");
		}
		for (String name : specOutputs.keySet()) {
			if (!implOutputs.containsKey(name)) {
				throw new ASTException(
						"No implementation output variable matching specification outputs variable "
								+ name + ".");
			}
		}
	}

	/**
	 * Given an AST, remove input and output variables and create a new function
	 * wrapping the remaining file scope items.
	 * 
	 * @param root
	 *            The root node of the AST being wrapped in a new function.
	 * @param name
	 *            The name of the new function.
	 * @return A function definition node with the appropriate name, void return
	 *         type, and no parameters.
	 */
	private FunctionDefinitionNode wrapperFunction(Source source, ASTNode root,
			String name) {
		FunctionTypeNode functionType = factory.newFunctionTypeNode(source,
				factory.newBasicTypeNode(source, BasicTypeKind.INT), factory
						.newSequenceNode(source, "Formals",
								new ArrayList<VariableDeclarationNode>()),
				false);
		List<BlockItemNode> items = new ArrayList<BlockItemNode>();
		CompoundStatementNode body;
		FunctionDefinitionNode result;

		for (int i = 0; i < root.numChildren(); i++) {
			ASTNode child = root.child(i);

			if (child == null)
				continue;
			if (child.nodeKind() == NodeKind.VARIABLE_DECLARATION) {
				VariableDeclarationNode var = (VariableDeclarationNode) child;

				if (!var.getTypeNode().isInputQualified()
						&& !var.getTypeNode().isOutputQualified()) {
					items.add(var.copy());
				}
			} else if (child.nodeKind() == NodeKind.FUNCTION_DECLARATION) {
				FunctionDeclarationNode function = (FunctionDeclarationNode) child;

				// if (function.getEntity().getDefinition() != null) {
				items.add(function.copy());
				// }
			} else if (child.nodeKind() == NodeKind.FUNCTION_DEFINITION) {
				FunctionDefinitionNode function = (FunctionDefinitionNode) child;
				if (function.getName() != null
						&& function.getName().equals("main")) {
					for (int j = 0; j < ((FunctionDefinitionNode) function)
							.getBody().numChildren(); j++) {
						items.add(((FunctionDefinitionNode) function).getBody()
								.getSequenceChild(j).copy());
					}
				} else {
					items.add(function.copy());
				}
			} else {
				assert child instanceof BlockItemNode;
				items.add((BlockItemNode) child.copy());
			}
		}
		body = factory.newCompoundStatementNode(source, items);
		result = factory.newFunctionDefinitionNode(source, factory
				.newIdentifierNode(root.getSource(), name), functionType,
				factory.newSequenceNode(source, "Contract",
						new ArrayList<ContractNode>()), body);
		return result;
	}

	/**
	 * Generate the set of assertions comparing output variables. We assume that
	 * checkOutputs has been called, so the sets are of equal cardinality and
	 * the names correspond. Furthermore, we assume that name substitutions have
	 * happened, but that the String key values in the argument maps are the old
	 * names.
	 * 
	 * @param specOutputs
	 *            Mapping from original output variable name to the
	 *            corresponding variable declaration from the spec program.
	 * @param implOutputs
	 *            Mapping from original output variable name to the
	 *            corresponding variable declaration from the impl program.
	 * @return A list of assertion statements checking equivalence of
	 *         corresponding output variables.
	 */
	private List<StatementNode> outputAssertions(
			Map<String, VariableDeclarationNode> specOutputs,
			Map<String, VariableDeclarationNode> implOutputs) {
		List<StatementNode> result = new ArrayList<StatementNode>();
		// TODO: do something better for source
		ExpressionNode equalFunction = factory.newIdentifierExpressionNode(
				specSource, factory.newIdentifierNode(specSource, "$equals"));

		for (String outputName : specOutputs.keySet()) {
			Source source = specOutputs.get(outputName).getSource();
			List<ExpressionNode> args = new ArrayList<ExpressionNode>();
			StatementNode assertion;
			FunctionCallNode equalCall;
			OperatorNode assign;
			VariableDeclarationNode specOutput = specOutputs.get(outputName);
			VariableDeclarationNode implOutput = implOutputs.get(outputName);

			args.add(factory.newOperatorNode(specOutput.getSource(),
					Operator.ADDRESSOF, Arrays.asList((ExpressionNode) factory
							.newIdentifierExpressionNode(
									specOutput.getSource(), specOutput
											.getIdentifier().copy()))));
			args.add(factory.newOperatorNode(implOutput.getSource(),
					Operator.ADDRESSOF, Arrays.asList((ExpressionNode) factory
							.newIdentifierExpressionNode(
									implOutput.getSource(), implOutput
											.getIdentifier().copy()))));
			equalCall = factory.newFunctionCallNode(source,
					equalFunction.copy(), args, null);
			assign = factory.newOperatorNode(source, Operator.ASSIGN, Arrays
					.asList(factory.newIdentifierExpressionNode(source,
							factory.newIdentifierNode(source, "_isEqual")),
							equalCall));
			result.add(factory.newExpressionStatementNode(assign));
			assertion = factory.newAssertNode(
					source,
					factory.newIdentifierExpressionNode(source,
							factory.newIdentifierNode(source, "_isEqual")),
					null);
			result.add(assertion);
		}
		return result;
	}

	/**
	 * Create a mapping from Entity to String where the entities are variables
	 * and the strings are those variable names with a suffix added.
	 * 
	 * @param variables
	 *            A set of variable declarations.
	 * @param suffix
	 *            The suffix to be added to each variable name.
	 * @return The mapping from entities to their new names.
	 */
	private Map<Entity, String> renameVariables(
			Collection<VariableDeclarationNode> variables, String suffix) {
		Map<Entity, String> result = new LinkedHashMap<Entity, String>();

		for (VariableDeclarationNode var : variables) {
			result.put(var.getEntity(), var.getName() + suffix);
		}
		return result;
	}

	private Source getMainSource(ASTNode node) {
		if (node.nodeKind() == NodeKind.FUNCTION_DEFINITION) {
			FunctionDefinitionNode functionNode = (FunctionDefinitionNode) node;
			IdentifierNode functionName = (IdentifierNode) functionNode
					.child(0);

			if (functionName.name().equals("main")) {
				return node.getSource();
			}
		}
		for (ASTNode child : node.children()) {
			if (child == null)
				continue;
			else {
				Source childResult = getMainSource(child);

				if (childResult != null)
					return childResult;
			}
		}
		return null;
	}
}
