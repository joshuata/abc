package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.EntityKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
//import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CivlForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.DeclarationListNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.AtomicType;
import edu.udel.cis.vsl.abc.ast.type.IF.DomainType;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.BaseTransformer;

/**
 * A transformer which modifies an AST so that no expressions other than
 * assignments have side effects.
 * 
 * TODO: search for race conditions. Need a way to represent the set read-set
 * and write-set of a statement or expression. This is a subset of the variables
 * in scope.
 * 
 * i++: equivalent to i=i+1, this is a read and write of i.
 * 
 * a[i]: reads of a, i
 * 
 * function call: reads arguments, then go to function.
 * 
 * If function pointer, get type, go over all functions with that type.
 * 
 * *p : reads p, and also (in absence of pointer analysis) anything visible with
 * same type as *p.
 * 
 * *p=...; reads p and writes to *p
 * 
 * system functions: in absence of contracts, can read or write anything in
 * scope
 * 
 * @author zirkel
 *
 */
public class SideEffectRemover extends BaseTransformer {

	public final static String CODE = "sef";

	public final static String LONG_NAME = "SideEffectRemover";

	public final static String SHORT_DESCRIPTION = "transforms program to side-effect-free form";

	private String tempVariablePrefix = "_TEMP_";

	private int tempVariableCounter = 0;

	public SideEffectRemover(ASTFactory astFactory) {
		super(CODE, LONG_NAME, SHORT_DESCRIPTION, astFactory);
	}

	@Override
	public AST transform(AST unit) throws SyntaxException {
		SequenceNode<ExternalDefinitionNode> rootNode = unit.getRootNode();

		assert this.astFactory == unit.getASTFactory();
		assert this.nodeFactory == astFactory.getNodeFactory();
		unit.release();
		for (int i = 0; i < rootNode.numChildren(); i++) {
			ASTNode node = rootNode.child(i);

			// For now, assume initializers for global variables are side effect
			// free (SEF).
			if (node instanceof VariableDeclarationNode) {

			} else if (node instanceof FunctionDefinitionNode) {
				removeSideEffects((FunctionDefinitionNode) node);
			}
		}
		return astFactory.newAST(rootNode, unit.getSourceFiles());
	}

	private void removeSideEffects(FunctionDefinitionNode function)
			throws SyntaxException {
		function.setBody(processCompoundStatement(function.getBody()));
	}

	private CompoundStatementNode processCompoundStatement(
			CompoundStatementNode statement) throws SyntaxException {
		CompoundStatementNode result;
		List<BlockItemNode> items = new ArrayList<>();

		for (int i = 0; i < statement.numChildren(); i++) {
			BlockItemNode item = statement.getSequenceChild(i);

			if (item != null && item.parent() != null) {
				item.parent().removeChild(item.childIndex());
			}
			if (item instanceof FunctionDefinitionNode) {
				removeSideEffects((FunctionDefinitionNode) item);
				items.add(item);
			} else if (item instanceof StatementNode) {
				items.add(processStatement((StatementNode) item));
			} else if (item instanceof VariableDeclarationNode) {
				VariableDeclarationNode vdecl = (VariableDeclarationNode) item;
				InitializerNode initNode = vdecl.getInitializer();

				if (initNode == null) {
					items.add(item);
				} else {
					if (initNode instanceof CompoundInitializerNode) {
						if (initNode.isSideEffectFree(false)) {
							items.add(item);
						} else {
							throw new ABCUnsupportedException(
									"removing side effects from compound initializers:  "
											+ initNode, initNode.getSource()
											.getSummary(false));
						}
					} else if (initNode.isSideEffectFree(false)) {
						// Only modify things if we need to.
						items.add(item);
					} else {
						ExpressionNode initializer = (ExpressionNode) initNode;
						List<ExpressionNode> assignmentArguments = new ArrayList<>();
						IdentifierNode variable = vdecl.getIdentifier();
						TypeNode type = vdecl.getTypeNode().copy();
						ExpressionNode initializerAssignment;
						VariableDeclarationNode newDeclaration;

						variable.parent().removeChild(variable.childIndex());
						vdecl.setInitializer(null);
						newDeclaration = nodeFactory
								.newVariableDeclarationNode(
										item.getSource(),
										nodeFactory.newIdentifierNode(
												variable.getSource(),
												variable.name()), type);
						items.add(newDeclaration);
						assignmentArguments.add(nodeFactory
								.newIdentifierExpressionNode(
										variable.getSource(), variable));
						assignmentArguments.add(initializer);
						initializerAssignment = nodeFactory.newOperatorNode(
								initializer.getSource(), Operator.ASSIGN,
								assignmentArguments);
						items.add(processStatement(nodeFactory
								.newExpressionStatementNode(initializerAssignment)));
					}
				}
			} else {
				// Other types of block items should be SEF.
				items.add(item);
			}
		}
		result = nodeFactory.newCompoundStatementNode(statement.getSource(),
				items);
		return result;
	}

	/**
	 * Remove side effects if the statement contains them. Note that
	 * <code>goto</code>, <code>jump</code>, and <code>null</code> statements
	 * cannot have side effects, and so will never be modified.
	 * 
	 * @param statement
	 * @return
	 * @throws SyntaxException
	 */
	private StatementNode processStatement(StatementNode statement)
			throws SyntaxException {
		StatementNode result = null;

		// TODO: switch to switch:

		if (statement != null && statement.parent() != null) {
			statement.parent().removeChild(statement.childIndex());
		}
		if (statement instanceof AssertNode) {
			result = assertStatement((AssertNode) statement);
		} else if (statement instanceof AssumeNode) {
			result = assumeStatement((AssumeNode) statement);
		} else if (statement instanceof ChooseStatementNode) {
			result = chooseStatement((ChooseStatementNode) statement);
		} else if (statement instanceof CompoundStatementNode) {
			result = processCompoundStatement((CompoundStatementNode) statement);
		} else if (statement instanceof ExpressionStatementNode) {
			result = expressionStatement((ExpressionStatementNode) statement);
		} else if (statement instanceof ForLoopNode) {
			result = loopStatement((ForLoopNode) statement);
		} else if (statement instanceof IfNode) {
			result = ifStatement((IfNode) statement);
		} else if (statement instanceof LabeledStatementNode) {
			result = labeledStatement((LabeledStatementNode) statement);
		} else if (statement instanceof LoopNode) {
			result = loopStatement((LoopNode) statement);
		} else if (statement instanceof ReturnNode) {
			result = returnStatement((ReturnNode) statement);
		} else if (statement instanceof SwitchNode) {
			result = switchStatement((SwitchNode) statement);
		} else if (statement instanceof WhenNode) {
			result = whenStatement((WhenNode) statement);
		} else if (statement instanceof AtomicNode) {
			result = atomicStatement((AtomicNode) statement);
		} else if (statement instanceof CivlForNode) {
			result = civlForStatement((CivlForNode) statement);
		} else {
			result = statement;
		}
		return result;
	}

	private StatementNode assertStatement(AssertNode statement)
			throws SyntaxException {
		StatementNode result;
		ExpressionNode condition = statement.getCondition();
		SequenceNode<ExpressionNode> explanation = statement.getExplanation();
		boolean sideEffectFree = condition.isSideEffectFree(false);

		if (explanation != null)
			for (int i = 0; i < explanation.numChildren(); i++) {
				sideEffectFree = sideEffectFree
						&& explanation.getSequenceChild(i).isSideEffectFree(
								false);
			}
		if (sideEffectFree) {
			result = statement;
		} else {
			List<BlockItemNode> newStatements = new ArrayList<BlockItemNode>();
			SideEffectFreeTriple triple;
			List<BlockItemNode> before = new ArrayList<>(), after = new ArrayList<>();
			ExpressionNode newCondition;
			List<ExpressionNode> newExplanationArgs = new ArrayList<>();
			SequenceNode<ExpressionNode> newExplanation;

			triple = processExpression(condition);
			newCondition = triple.getExpression();
			before.addAll(triple.getBefore());
			after.addAll(triple.getAfter());
			if (explanation != null)
				for (int i = 0; i < explanation.numChildren(); i++) {
					triple = processExpression(explanation.getSequenceChild(i));
					newExplanationArgs.add(triple.getExpression());
					before.addAll(triple.getBefore());
					after.addAll(triple.getAfter());
				}
			newExplanation = explanation != null ? nodeFactory.newSequenceNode(
					explanation.getSource(), "Explanation", newExplanationArgs)
					: null;
			newStatements.addAll(before);
			newStatements.add(nodeFactory.newAssertNode(statement.getSource(),
					newCondition, newExplanation));
			newStatements.addAll(after);
			result = nodeFactory.newCompoundStatementNode(
					statement.getSource(), newStatements);
		}
		return result;
	}

	private StatementNode civlForStatement(CivlForNode statement)
			throws SyntaxException {
		StatementNode bodyNode = processStatement(statement.getBody());

		return nodeFactory.newCivlForNode(statement.getSource(), statement
				.isParallel(), statement.getVariables().copy(), statement
				.getDomain().copy(), bodyNode,
				statement.getInvariant() == null ? null : statement
						.getInvariant().copy());
	}

	private StatementNode atomicStatement(AtomicNode statement)
			throws SyntaxException {
		StatementNode bodyNode = processStatement(statement.getBody());

		return nodeFactory.newAtomicStatementNode(statement.getSource(),
				statement.isAtom(), bodyNode);
	}

	private StatementNode chooseStatement(ChooseStatementNode statement)
			throws SyntaxException {
		ChooseStatementNode result;
		StatementNode defaultCase = statement.getDefaultCase();
		List<StatementNode> statements = new ArrayList<>();

		for (StatementNode child : statement) {
			statements.add(processStatement(child));
		}
		result = nodeFactory.newChooseStatementNode(statement.getSource(),
				statements);
		if (defaultCase != null) {
			defaultCase = processStatement(defaultCase);
			assert defaultCase instanceof LabeledStatementNode;
			result.setDefaultCase((LabeledStatementNode) defaultCase);
		}
		return result;
	}

	private StatementNode switchStatement(SwitchNode statement)
			throws SyntaxException {
		StatementNode result;
		StatementNode body;

		if (!statement.getCondition().isSideEffectFree(false)) {
			throw new ABCUnsupportedException(
					"side effects in a switch condition. "
							+ statement.getCondition(), statement
							.getCondition().getSource().getSummary(false));
		}
		body = processStatement(statement.getBody().copy());
		result = nodeFactory.newSwitchNode(statement.getSource(), statement
				.getCondition().copy(), body);
		return result;
	}

	private StatementNode returnStatement(ReturnNode statement)
			throws SyntaxException {
		StatementNode result;

		if (statement.getExpression() == null
				|| statement.getExpression().isSideEffectFree(false)) {
			result = statement;
		} else {
			List<BlockItemNode> newStatements = new ArrayList<>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());
			TypeNode expressionType = typeNode(statement.getExpression()
					.getSource(), statement.getExpression().getType());
			IdentifierNode tempVariableID = nodeFactory.newIdentifierNode(
					statement.getExpression().getSource(), tempVariablePrefix
							+ tempVariableCounter++);
			VariableDeclarationNode tempVariableDeclaration = nodeFactory
					.newVariableDeclarationNode(statement.getExpression()
							.getSource(), tempVariableID, expressionType,
							triple.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(tempVariableDeclaration);
			newStatements.addAll(triple.getAfter());
			newStatements
					.add(nodeFactory.newReturnNode(statement.getSource(),
							nodeFactory.newIdentifierExpressionNode(statement
									.getExpression().getSource(),
									tempVariableID.copy())));
			result = nodeFactory.newCompoundStatementNode(
					statement.getSource(), newStatements);
		}
		return result;
	}

	/**
	 * 
	 * @param type
	 *            An AST type.
	 * @return An AST type node corresponding to the type.
	 * @throws SyntaxException
	 */
	private TypeNode typeNode(Source source, Type type) throws SyntaxException {

		switch (type.kind()) {
		case ARRAY:
			ArrayType arrayType = (ArrayType) type;

			return nodeFactory.newArrayTypeNode(source,
					typeNode(source, arrayType.getElementType()), null);
		case ATOMIC:
			AtomicType atomicType = (AtomicType) type;

			return nodeFactory.newAtomicTypeNode(source,
					typeNode(source, atomicType.getBaseType()));
		case BASIC:
			StandardBasicType basicType = (StandardBasicType) type;

			return nodeFactory.newBasicTypeNode(source,
					basicType.getBasicTypeKind());
		case DOMAIN: {
			DomainType domainType = (DomainType) type;

			if (domainType.hasDimension()) {
				nodeFactory.newDomainTypeNode(
						source,
						nodeFactory.newIntegerConstantNode(source,
								Integer.toString(domainType.getDimension())));
			} else
				return nodeFactory.newDomainTypeNode(source);
		}
		case POINTER: {
			PointerType pointerType = (PointerType) type;

			return nodeFactory.newPointerTypeNode(source,
					typeNode(source, pointerType.referencedType()));
		}
		case VOID:
			return nodeFactory.newVoidTypeNode(source);
		case ENUMERATION: {
			// if original type is anonymous enum, need to spell out
			// the type again.
			// if original type has tag, and is visible, can leave out
			// the enumerators
			EnumerationType enumType = (EnumerationType) type;
			String tag = enumType.getTag();

			if (tag != null) {
				IdentifierNode tagNode = nodeFactory.newIdentifierNode(source,
						tag);
				TypeNode result = nodeFactory.newEnumerationTypeNode(source,
						tagNode, null);

				return result;
			} else {
				throw new ABCUnsupportedException(
						"converting anonymous enumeration type  " + type,
						source.getSummary(false));
			}
		}
		case STRUCTURE_OR_UNION: {
			StructureOrUnionType structOrUnionType = (StructureOrUnionType) type;

			return nodeFactory.newStructOrUnionTypeNode(
					source,
					structOrUnionType.isStruct(),
					nodeFactory.newIdentifierNode(source,
							structOrUnionType.getName()), null);
		}
		case SCOPE:
			return nodeFactory.newScopeTypeNode(source);
		case FUNCTION:
		case HEAP:
		case OTHER_INTEGER:
		case PROCESS:
		case QUALIFIED:
		default:
			throw new ABCUnsupportedException("converting type " + type
					+ " to a type node.", source.getSummary(false));
		}
	}

	private StatementNode labeledStatement(LabeledStatementNode statement)
			throws SyntaxException {
		StatementNode target = processStatement(statement.getStatement());
		LabelNode label = statement.getLabel();

		if (label != null && label.parent() != null) {
			label.parent().removeChild(label.childIndex());
		}
		return nodeFactory.newLabeledStatementNode(statement.getSource(),
				label, target);
	}

	private StatementNode ifStatement(IfNode statement) throws SyntaxException {
		StatementNode trueBranch = processStatement(statement.getTrueBranch());
		StatementNode falseBranch = null;

		if (statement.getFalseBranch() != null) {
			falseBranch = processStatement(statement.getFalseBranch());
		}
		if (!statement.getCondition().isSideEffectFree(false)) {
			ExpressionNode condition = statement.getCondition();
			SideEffectFreeTriple sefCondition = processExpression(condition);
			IdentifierNode tempVariableID = nodeFactory.newIdentifierNode(
					condition.getSource(), tempVariablePrefix
							+ tempVariableCounter++);
			List<BlockItemNode> items = new ArrayList<BlockItemNode>();

			items.addAll(sefCondition.getBefore());
			items.add(nodeFactory.newVariableDeclarationNode(condition
					.getSource(), tempVariableID, nodeFactory.newBasicTypeNode(
					condition.getSource(), BasicTypeKind.BOOL), sefCondition
					.getExpression()));
			items.addAll(sefCondition.getAfter());
			items.add(nodeFactory.newIfNode(
					statement.getSource(),
					nodeFactory.newIdentifierExpressionNode(
							condition.getSource(), tempVariableID.copy()),
					trueBranch, falseBranch));
			return nodeFactory.newCompoundStatementNode(statement.getSource(),
					items);
		}
		return nodeFactory.newIfNode(statement.getSource(), statement
				.getCondition().copy(), trueBranch, falseBranch);
	}

	private StatementNode whenStatement(WhenNode statement)
			throws SyntaxException {
		StatementNode result;
		ExpressionNode guard = statement.getGuard().copy();

		if (!guard.isSideEffectFree(false))
			throw new SyntaxException(
					"Possible side-effect in $when statement guard: " + guard,
					guard.getSource());
		result = nodeFactory.newWhenNode(statement.getSource(), guard,
				processStatement(statement.getBody()));
		return result;
	}

	private StatementNode assumeStatement(AssumeNode statement)
			throws SyntaxException {
		StatementNode result;

		if (statement.getExpression().isSideEffectFree(false)) {
			result = statement;
		} else {
			List<BlockItemNode> newStatements = new ArrayList<BlockItemNode>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(nodeFactory.newAssumeNode(statement.getSource(),
					triple.getExpression()));
			newStatements.addAll(triple.getAfter());
			result = nodeFactory.newCompoundStatementNode(
					statement.getSource(), newStatements);
		}
		return result;
	}

	private StatementNode expressionStatement(ExpressionStatementNode statement)
			throws SyntaxException {
		StatementNode result = null;
		ExpressionNode expression = statement.getExpression();

		if (expression == null)
			return statement;
		if (statement.parent() != null) {
			statement.parent().removeChild(statement.childIndex());
		}
		if (expression.isSideEffectFree(false)) {
			result = statement;
		} else {
			if (expression instanceof FunctionCallNode) {
				boolean sideEffectFree = true;

				for (int i = 0; i < ((FunctionCallNode) expression)
						.getNumberOfArguments(); i++) {
					sideEffectFree = sideEffectFree
							&& ((FunctionCallNode) expression).getArgument(i)
									.isSideEffectFree(false);
				}
				if (sideEffectFree) {
					result = statement;
				} else {
					throw new ABCRuntimeException(
							"arguments with possible side-effect: prohibited to avoid undefined behavior",
							expression.getSource().getSummary(false));
				}
			} else if (expression instanceof SpawnNode) {
				FunctionCallNode call = ((SpawnNode) expression).getCall();
				boolean sideEffectFree = true;

				for (int i = 0; i < call.getNumberOfArguments(); i++) {
					sideEffectFree = sideEffectFree
							&& call.getArgument(i).isSideEffectFree(false);
				}
				if (sideEffectFree) {
					result = statement;
				} else {
					List<BlockItemNode> before = new ArrayList<>();
					List<BlockItemNode> after = new ArrayList<>();
					List<ExpressionNode> arguments = new ArrayList<>();
					List<BlockItemNode> blockItems = new ArrayList<>();

					for (int i = 0; i < call.getNumberOfArguments(); i++) {
						ExpressionNode originalArgument = call.getArgument(i);

						if (originalArgument.isSideEffectFree(false)) {
							arguments.add(originalArgument);
						} else {
							SideEffectFreeTriple triple = processExpression(originalArgument);

							before.addAll(triple.getBefore());
							arguments.add(triple.getExpression());
							after.addAll(triple.getAfter());
						}
					}
					blockItems.addAll(before);
					blockItems.add(nodeFactory
							.newExpressionStatementNode(nodeFactory
									.newSpawnNode(statement.getSource(),
											nodeFactory.newFunctionCallNode(
													statement.getSource(),
													call.getFunction(),
													arguments,
													call.getScopeList()))));
					blockItems.addAll(after);
					result = nodeFactory.newCompoundStatementNode(
							statement.getSource(), blockItems);
				}
			} else if (expression instanceof OperatorNode) {
				OperatorNode.Operator operator = ((OperatorNode) expression)
						.getOperator();

				switch (operator) {
				case PREINCREMENT:
				case POSTINCREMENT:
					ExpressionNode add;
					List<ExpressionNode> addArguments = new ArrayList<>();
					List<ExpressionNode> incrementArguments = new ArrayList<>();
					ExpressionNode variableExpression = ((OperatorNode) expression)
							.getArgument(0).copy();

					// variableExpression.parent().removeChild(
					// variableExpression.childIndex());
					addArguments.add(variableExpression);
					addArguments.add(nodeFactory.newIntegerConstantNode(
							expression.getSource(), "1"));
					add = nodeFactory.newOperatorNode(expression.getSource(),
							Operator.PLUS, addArguments);
					incrementArguments.add(variableExpression.copy());
					incrementArguments.add(add);
					result = nodeFactory.newExpressionStatementNode(nodeFactory
							.newOperatorNode(expression.getSource(),
									Operator.ASSIGN, incrementArguments));
					break;
				case PREDECREMENT:
				case POSTDECREMENT:
					ExpressionNode subtract;
					List<ExpressionNode> subtractArguments = new ArrayList<>();
					List<ExpressionNode> decrementArguments = new ArrayList<>();

					subtractArguments.add(((OperatorNode) expression)
							.getArgument(0).copy());
					subtractArguments.add(nodeFactory.newIntegerConstantNode(
							expression.getSource(), "1"));
					subtract = nodeFactory.newOperatorNode(
							expression.getSource(), Operator.MINUS,
							subtractArguments);
					decrementArguments.add(((OperatorNode) expression)
							.getArgument(0).copy());
					decrementArguments.add(subtract);
					result = nodeFactory.newExpressionStatementNode(nodeFactory
							.newOperatorNode(expression.getSource(),
									Operator.ASSIGN, decrementArguments));
					break;
				case ASSIGN:
					ExpressionNode lhs = ((OperatorNode) expression)
							.getArgument(0);
					ExpressionNode rhs = ((OperatorNode) expression)
							.getArgument(1);

					if (lhs.isSideEffectFree(false)
							&& rhs.isSideEffectFree(false)) {
						result = statement;
					} else if (lhs.isSideEffectFree(false)) {
						if (isCompleteMallocExpression(rhs)) {
							result = statement;
						} else {
							SideEffectFreeTriple triple;
							List<BlockItemNode> blockItems = new ArrayList<>();

							if (rhs instanceof FunctionCallNode) {
								for (int i = 0; i < ((FunctionCallNode) rhs)
										.getNumberOfArguments(); i++) {
									if (!((FunctionCallNode) rhs)
											.getArgument(i).isSideEffectFree(
													false))
										throw new ABCRuntimeException(
												"arguments with possible side-effect: prohibited to avoid undefined behavior",
												rhs.getSource().getSummary(
														false));
								}
								result = statement;
							} else if (rhs instanceof SpawnNode) {
								for (int i = 0; i < ((SpawnNode) rhs).getCall()
										.getNumberOfArguments(); i++) {
									if (!((SpawnNode) rhs).getCall()
											.getArgument(i)
											.isSideEffectFree(false))
										throw new ABCRuntimeException(
												"arguments with possible side-effect: prohibited to avoid undefined behavior",
												rhs.getSource().getSummary(
														false));
								}
								result = statement;
							} else {
								List<ExpressionNode> assignArguments = new ArrayList<>();
								ExpressionNode assignRhs;

								assignArguments.add(lhs.copy());
								triple = processExpression(rhs);
								blockItems.addAll(triple.getBefore());
								assignRhs = triple.getExpression().copy();
								assignArguments.add(assignRhs);
								blockItems.add(nodeFactory
										.newExpressionStatementNode(nodeFactory
												.newOperatorNode(
														expression.getSource(),
														Operator.ASSIGN,
														assignArguments)));
								blockItems.addAll(triple.getAfter());
								result = nodeFactory.newCompoundStatementNode(
										statement.getSource(), blockItems);
							}
						}
					} else {
						// lhs is not SEF.
						SideEffectFreeTriple lhsTriple = processExpression(lhs);
						List<BlockItemNode> blockItems = new ArrayList<>();
						List<ExpressionNode> assignArguments = new ArrayList<>();

						assignArguments.add(lhsTriple.getExpression());
						blockItems.addAll(lhsTriple.getBefore());
						if (rhs.isSideEffectFree(false)
								|| rhs instanceof FunctionCallNode
								|| rhs instanceof SpawnNode) {

							assignArguments.add(rhs.copy());
							blockItems.add(nodeFactory
									.newExpressionStatementNode(nodeFactory
											.newOperatorNode(
													expression.getSource(),
													Operator.ASSIGN,
													assignArguments)));
						} else {
							SideEffectFreeTriple rhsTriple = processExpression(rhs);

							blockItems.addAll(rhsTriple.getBefore());
							assignArguments.add(rhsTriple.getExpression());
							blockItems.add(nodeFactory
									.newExpressionStatementNode(nodeFactory
											.newOperatorNode(
													expression.getSource(),
													Operator.ASSIGN,
													assignArguments)));
							blockItems.addAll(rhsTriple.getAfter());
						}
						blockItems.addAll(lhsTriple.getAfter());
						result = nodeFactory.newCompoundStatementNode(
								statement.getSource(), blockItems);
					}
					break;
				case PLUS:
				case MINUS:
				case DIV:
				case TIMES:
					// TODO: Need to process these for precedent, or is this
					// taken care of?
					ExpressionNode left = ((OperatorNode) expression)
							.getArgument(0);
					ExpressionNode right = ((OperatorNode) expression)
							.getArgument(1);
					SideEffectFreeTriple leftTriple = processExpression(left);
					SideEffectFreeTriple rightTriple = processExpression(right);
					List<ExpressionNode> operands = new ArrayList<>();
					StatementNode sideEffectFreeStatement;
					List<BlockItemNode> blockItems = new ArrayList<>();

					operands.add(leftTriple.getExpression());
					operands.add(rightTriple.getExpression());
					sideEffectFreeStatement = nodeFactory
							.newExpressionStatementNode(nodeFactory
									.newOperatorNode(statement.getSource(),
											((OperatorNode) expression)
													.getOperator(), operands));
					blockItems.addAll(leftTriple.getBefore());
					blockItems.addAll(rightTriple.getBefore());
					blockItems.add(sideEffectFreeStatement);
					blockItems.addAll(rightTriple.getAfter());
					blockItems.addAll(leftTriple.getAfter());
					result = nodeFactory.newCompoundStatementNode(
							statement.getSource(), blockItems);
					break;
				case PLUSEQ:
				case TIMESEQ:
				case MINUSEQ:
				case MODEQ:
				case DIVEQ:
					StatementNode assignment;
					List<BlockItemNode> statements = new ArrayList<BlockItemNode>();

					left = ((OperatorNode) expression).getArgument(0);
					right = ((OperatorNode) expression).getArgument(1);
					leftTriple = processExpression(left);
					rightTriple = processExpression(right);
					assignment = opEqStatement(expression.getSource(),
							((OperatorNode) expression).getOperator(),
							leftTriple.getExpression(),
							rightTriple.getExpression());
					statements.addAll(leftTriple.getBefore());
					statements.addAll(rightTriple.getBefore());
					statements.add(assignment);
					statements.addAll(leftTriple.getAfter());
					statements.addAll(rightTriple.getAfter());
					if (statements.size() == 1) {
						assert statements.get(0) instanceof StatementNode;
						result = (StatementNode) statements.get(0);
					} else {
						result = nodeFactory.newCompoundStatementNode(
								expression.getSource(), statements);
					}
					break;
				case COMMA:
					SideEffectFreeTriple triple = processExpression(expression);
					ExpressionNode expr = triple.getExpression();
					List<BlockItemNode> commaStatements = new ArrayList<BlockItemNode>();

					commaStatements.addAll(triple.getBefore());
					if (!expr.isSideEffectFree(true)) {
						ASTNode parent = expr.parent();

						if (parent != null) {
							parent.removeChild(expr.childIndex());
						}
						commaStatements.add(nodeFactory
								.newExpressionStatementNode(triple
										.getExpression()));
					}else
						commaStatements.add(nodeFactory
								.newExpressionStatementNode(expr.copy()));
					commaStatements.addAll(triple.getAfter());
					result = nodeFactory.newCompoundStatementNode(
							expression.getSource(), commaStatements);
					break;
				default:
					throw new ABCUnsupportedException("this operation: "
							+ statement, statement.getSource()
							.getSummary(false));
				}
			} else if (expression instanceof CastNode) {
				// It's not clear why this would be used, since the result of
				// the cast is discarded, but we've seen it in examples.
				// Solution: formulate a new expression statement without the
				// cast and do a recursive call.
				result = expressionStatement(nodeFactory
						.newExpressionStatementNode(((CastNode) expression)
								.getArgument().copy()));
			} else {
				throw new ABCUnsupportedException(
						"removing side effects from this expression statement: "
								+ statement, statement.getSource().getSummary(
								false));
			}
		}
		return result;
	}

	private StatementNode loopStatement(LoopNode statement)
			throws SyntaxException {
		ExpressionNode condition = statement.getCondition();
		StatementNode newBody = processStatement(statement.getBody());
		ExpressionNode invariant = statement.getInvariant();
		StatementNode result;

		if (invariant != null) {
			invariant.parent().removeChild(invariant.childIndex());
		}
		if (condition != null && !condition.isSideEffectFree(false)) {
			// If a side effect exists in a condition, convert from
			// while (e) {S;} to while (true) {int tmp_X = e; if (!e) break; S;}
			SideEffectFreeTriple sefCondition = processExpression(condition);
			List<BlockItemNode> bodyItems = new ArrayList<BlockItemNode>();
			List<ExpressionNode> ifArgument = new ArrayList<ExpressionNode>();
			IdentifierNode tempVariableID = nodeFactory.newIdentifierNode(
					condition.getSource(), tempVariablePrefix
							+ tempVariableCounter++);

			bodyItems.addAll(sefCondition.getBefore());
			bodyItems.add(nodeFactory.newVariableDeclarationNode(condition
					.getSource(), tempVariableID, nodeFactory.newBasicTypeNode(
					condition.getSource(), BasicTypeKind.BOOL), sefCondition
					.getExpression().copy()));
			bodyItems.addAll(sefCondition.getAfter());
			ifArgument.add(nodeFactory.newIdentifierExpressionNode(
					condition.getSource(), tempVariableID.copy()));
			bodyItems.add(nodeFactory.newIfNode(condition.getSource(),
					nodeFactory.newOperatorNode(condition.getSource(),
							Operator.NOT, ifArgument), nodeFactory
							.newBreakNode(condition.getSource())));
			bodyItems.add(newBody.copy());
			newBody = nodeFactory.newCompoundStatementNode(newBody.getSource(),
					bodyItems);
			condition = nodeFactory.newBooleanConstantNode(
					condition.getSource(), true);
		}
		switch (statement.getKind()) {
		case FOR:
			ForLoopInitializerNode initializer = ((ForLoopNode) statement)
					.getInitializer();
			ExpressionNode incrementer = ((ForLoopNode) statement)
					.getIncrementer();
			// We wrap the incrementer as an expression statement, then remove
			// side effects.
			// If it's a "simple" incrementer, the result will also be an
			// expression statement, otherwise the result will be some kind of
			// compound statement.
			// This removes ++, +=, etc. from the AST and also provides the
			// opportunity to modify this for loop into a while loop if
			// necessary for a complex incrementer.
			StatementNode modifiedIncrementer = null;

			if (incrementer != null) {
				incrementer.parent().removeChild(incrementer.childIndex());
				modifiedIncrementer = expressionStatement(nodeFactory
						.newExpressionStatementNode(incrementer));
			}
			// If initializer is not null, work on a copy to maintain tree
			// structure.
			if (initializer != null) {
				initializer = initializer.copy();
			}
			if (modifiedIncrementer instanceof ExpressionStatementNode) {
				if (initializer instanceof ExpressionNode
						&& !((ExpressionNode) initializer)
								.isSideEffectFree(false)) {
					StatementNode newForLoop = nodeFactory.newForLoopNode(
							statement.getSource(), null, condition.copy(),
							((ExpressionStatementNode) modifiedIncrementer)
									.getExpression().copy(), newBody.copy(),
							invariant);

					SideEffectFreeTriple initTriple = processExpression((ExpressionNode) initializer);
					List<BlockItemNode> allItems = new ArrayList<BlockItemNode>();

					allItems.addAll(initTriple.getBefore());
					allItems.addAll(initTriple.getAfter());
					allItems.add(newForLoop);
					result = nodeFactory.newCompoundStatementNode(
							statement.getSource(), allItems);
				} else
					result = nodeFactory.newForLoopNode(statement.getSource(),
							initializer, condition.copy(),
							((ExpressionStatementNode) modifiedIncrementer)
									.getExpression().copy(), newBody.copy(),
							invariant);
			} else {
				List<BlockItemNode> bodyItems = new ArrayList<BlockItemNode>();
				List<BlockItemNode> allItems = new ArrayList<BlockItemNode>();
				StatementNode loop;

				bodyItems.add(newBody);
				if (modifiedIncrementer != null)
					bodyItems.add(modifiedIncrementer);
				loop = nodeFactory.newWhileLoopNode(
						statement.getSource(),
						condition != null ? condition.copy() : null,
						nodeFactory.newCompoundStatementNode(
								newBody.getSource(), bodyItems), invariant);
				if (initializer != null) {
					if (initializer instanceof ExpressionNode) {
						if (((ExpressionNode) initializer)
								.isSideEffectFree(false)) {
							allItems.add(nodeFactory
									.newExpressionStatementNode((ExpressionNode) initializer));
						} else {
							SideEffectFreeTriple initTriple = processExpression((ExpressionNode) initializer);

							allItems.addAll(initTriple.getBefore());
							allItems.addAll(initTriple.getAfter());
						}
					} else if (initializer instanceof DeclarationListNode) {
						DeclarationListNode declarationList = (DeclarationListNode) initializer;

						for (VariableDeclarationNode child : declarationList) {
							child.parent().removeChild(child.childIndex());
							allItems.add(child);
						}
					} else {
						throw new ABCUnsupportedException(
								"converting initializer declaration to statement list.",
								initializer.getSource().getSummary(false));
					}
				}
				allItems.add(loop);
				result = nodeFactory.newCompoundStatementNode(
						statement.getSource(), allItems);
			}
			break;
		case DO_WHILE:
			result = nodeFactory.newDoLoopNode(statement.getSource(),
					condition.copy(), newBody, invariant);
			break;
		default:
			result = nodeFactory.newWhileLoopNode(statement.getSource(),
					condition.copy(), newBody, invariant);
		}
		return result;
	}

	private SideEffectFreeTriple processExpression(ExpressionNode expression)
			throws SyntaxException {
		SideEffectFreeTriple result;

		if (expression instanceof OperatorNode) {
			List<ExpressionNode> operands = new ArrayList<ExpressionNode>();
			ExpressionNode left = ((OperatorNode) expression).getArgument(0);
			ExpressionNode right;
			SideEffectFreeTriple leftTriple;
			SideEffectFreeTriple rightTriple;
			ExpressionNode sideEffectFreeExpression;
			List<BlockItemNode> before = new ArrayList<>();
			List<BlockItemNode> after = new ArrayList<>();

			switch (((OperatorNode) expression).getOperator()) {
			case ASSIGN:
				result = assign((OperatorNode) expression);
				break;
			case ADDRESSOF:
			case DEREFERENCE:
			case NOT:
			case UNARYMINUS:
			case UNARYPLUS:
				if (left.isSideEffectFree(false)) {
					result = new SideEffectFreeTriple(
							new ArrayList<BlockItemNode>(), expression,
							new ArrayList<BlockItemNode>());
				} else {
					leftTriple = processExpression(left);
					operands.add(leftTriple.getExpression());
					result = new SideEffectFreeTriple(leftTriple.getBefore(),
							nodeFactory.newOperatorNode(expression.getSource(),
									((OperatorNode) expression).getOperator(),
									operands), leftTriple.getAfter());
				}
				break;
			case PREINCREMENT:
			case PREDECREMENT:
			case POSTINCREMENT:
			case POSTDECREMENT:
				result = incrementOrDecrement((OperatorNode) expression);
				break;
			case BITAND:
			case BITCOMPLEMENT:
			case BITOR:
			case BITXOR:
			case PLUS:
			case MINUS:
			case DIV:
			case TIMES:
			case SUBSCRIPT:
			case LAND:
			case LOR:
			case EQUALS:
			case NEQ:
			case LT:
			case GT:
			case LTE:
			case GTE:
			case IMPLIES:
			case MOD:
			case SHIFTLEFT:
			case SHIFTRIGHT:
				left = ((OperatorNode) expression).getArgument(0);
				right = ((OperatorNode) expression).getArgument(1);
				leftTriple = processExpression(left);
				rightTriple = processExpression(right);
				operands.add(leftTriple.getExpression().copy());
				operands.add(rightTriple.getExpression().copy());
				sideEffectFreeExpression = nodeFactory.newOperatorNode(
						expression.getSource(),
						((OperatorNode) expression).getOperator(), operands);
				before.addAll(leftTriple.getBefore());
				before.addAll(rightTriple.getBefore());
				after.addAll(leftTriple.getAfter());
				after.addAll(rightTriple.getAfter());
				result = new SideEffectFreeTriple(before,
						sideEffectFreeExpression, after);
				break;
			case BITANDEQ:
			case BITOREQ:
			case BITXOREQ:
			case PLUSEQ:
			case MINUSEQ:
			case TIMESEQ:
			case DIVEQ:
			case MODEQ:
			case SHIFTLEFTEQ:
			case SHIFTRIGHTEQ: {
				StatementNode assignment;

				left = ((OperatorNode) expression).getArgument(0);
				right = ((OperatorNode) expression).getArgument(1);
				leftTriple = processExpression(left);
				rightTriple = processExpression(right);
				assignment = opEqStatement(expression.getSource(),
						((OperatorNode) expression).getOperator(),
						leftTriple.getExpression(), rightTriple.getExpression());
				before.addAll(leftTriple.getBefore());
				before.addAll(rightTriple.getBefore());
				before.add(assignment);
				after.addAll(leftTriple.getAfter());
				after.addAll(rightTriple.getAfter());
				result = new SideEffectFreeTriple(before, left.copy(), after);
				break;
			}
			case COMMA:
				right = ((OperatorNode) expression).getArgument(1);
				// Note that we consider errors as side effects for a comma
				// operation left hand side, because if there are no possible
				// side effects we'll just remove the left hand argument.
				if (left.isSideEffectFree(true)) {
					if (right.isSideEffectFree(false)) {
						// If neither operand had possible side effects, just
						// use the right hand operand.
						result = new SideEffectFreeTriple(
								new ArrayList<BlockItemNode>(), right,
								new ArrayList<BlockItemNode>());
					} else {
						// Right hand operand might have side effects, so
						// process it.
						result = processExpression(right);
					}
				} else {
					leftTriple = processExpression(left);
					before.addAll(leftTriple.getBefore());
					// In case there is a possible error side effect, add an
					// expression statement for the LHS. Normally this will
					// essentially be a noop.
//					if (!leftTriple.getExpression().isSideEffectFree(true)) {
//						before.add(nodeFactory
//								.newExpressionStatementNode(leftTriple
//										.getExpression().copy()));
//					}
					// All side effects from the left operand must be evaluated
					// before the right operand since there is a sequence point
					// between evaluations of left and right operands (per C11
					// standard Sec. 6.5.17)
					before.addAll(leftTriple.getAfter());
					if (right.isSideEffectFree(false)) {
						result = new SideEffectFreeTriple(before, right,
								new ArrayList<BlockItemNode>());
					} else {
						rightTriple = processExpression(right);
						before.addAll(rightTriple.getBefore());
						result = new SideEffectFreeTriple(before,
								rightTriple.getExpression(),
								rightTriple.getAfter());
					}
				}
				break;
			case CONDITIONAL: {
				// x = cond ? a : b;
				// ==>
				// cond_before;
				// if(cond_expr) a_before; else b_before;
				// x = cond_expr? a_expr : b_expr;
				// cond_after;
				// if(cond_expr) a_after; else b_after;
				ExpressionNode condition = ((OperatorNode) expression)
						.getArgument(0), trueExpr = ((OperatorNode) expression)
						.getArgument(1), falseExpr = ((OperatorNode) expression)
						.getArgument(2);
				SideEffectFreeTriple condTriple, trueTriple, falseTriple;
				StatementNode trueOrFalseBefore, trueOrFalseAfter, trueBlock, falseBlock;
				NodeFactory nodeFactory = this.astFactory.getNodeFactory();
				List<BlockItemNode> trueList, falseList;

				// if (!trueExpr.isSideEffectFree(false)
				// || !falseExpr.isSideEffectFree(false))
				// throw new SyntaxException(
				// "Side effect is not allowed in the second/third argument of a conditional expression.",
				// expression.getSource());
				condTriple = processExpression(condition);
				trueTriple = processExpression(trueExpr);
				falseTriple = processExpression(falseExpr);
				operands.add(condTriple.getExpression().copy());
				operands.add(trueTriple.getExpression().copy());
				operands.add(falseTriple.getExpression().copy());
				sideEffectFreeExpression = nodeFactory.newOperatorNode(
						expression.getSource(),
						((OperatorNode) expression).getOperator(), operands);
				before.addAll(condTriple.getBefore());
				trueList = trueTriple.getBefore();
				falseList = falseTriple.getBefore();
				if (trueList.size() > 0)
					trueBlock = nodeFactory.newCompoundStatementNode(
							trueExpr.getSource(), trueList);
				else
					trueBlock = null;
				if (falseList.size() > 0)
					falseBlock = nodeFactory.newCompoundStatementNode(
							falseExpr.getSource(), falseList);
				else
					falseBlock = null;
				if (trueBlock != null || falseBlock != null) {
					if (trueBlock == null)
						trueOrFalseBefore = nodeFactory.newIfNode(condition
								.getSource(), nodeFactory.newOperatorNode(
								condition.getSource(), Operator.NOT,
								Arrays.asList(condition.copy())), falseBlock);
					else if (falseBlock == null)
						trueOrFalseBefore = nodeFactory.newIfNode(
								condition.getSource(), condition.copy(),
								trueBlock);
					else
						trueOrFalseBefore = nodeFactory.newIfNode(
								condition.getSource(), condition.copy(),
								trueBlock, falseBlock);
					before.add(trueOrFalseBefore);
				}
				after.addAll(condTriple.getAfter());
				trueList = trueTriple.getAfter();
				falseList = falseTriple.getAfter();
				if (trueList.size() > 0)
					trueBlock = nodeFactory.newCompoundStatementNode(
							trueExpr.getSource(), trueList);
				else
					trueBlock = null;
				if (falseList.size() > 0)
					falseBlock = nodeFactory.newCompoundStatementNode(
							falseExpr.getSource(), falseList);
				else
					falseBlock = null;
				if (trueBlock != null || falseBlock != null) {
					if (trueBlock == null)
						trueOrFalseAfter = nodeFactory.newIfNode(condition
								.getSource(), nodeFactory.newOperatorNode(
								condition.getSource(), Operator.NOT,
								Arrays.asList(condition.copy())), falseBlock);
					else if (falseBlock == null)
						trueOrFalseAfter = nodeFactory.newIfNode(
								condition.getSource(), condition.copy(),
								trueBlock);
					else
						trueOrFalseAfter = nodeFactory.newIfNode(
								condition.getSource(), condition.copy(),
								trueBlock, falseBlock);
					after.add(trueOrFalseAfter);
				}
				result = new SideEffectFreeTriple(before,
						sideEffectFreeExpression, after);
				break;
			}
			default:// BIG_O, CONDITIONAL,
				throw new ABCUnsupportedException(
						"removing side effects from an expression with the operator "
								+ ((OperatorNode) expression).getOperator()
								+ ": " + expression, expression.getSource()
								.getSummary(false));
			}
		} else if (expression instanceof FunctionCallNode) {
			List<BlockItemNode> before = new ArrayList<>();
			ExpressionNode functionExpression = ((FunctionCallNode) expression)
					.getFunction();
			Function functionEntity;
			TypeNode returnTypeNode;
			VariableDeclarationNode tmpVariable;
			List<ExpressionNode> arguments = new ArrayList<>();

			assert functionExpression instanceof IdentifierExpressionNode;
			functionEntity = (Function) ((IdentifierExpressionNode) functionExpression)
					.getIdentifier().getEntity();
			assert functionEntity.getEntityKind() == EntityKind.FUNCTION;
			// siegel: another possible way is to clone, but
			// may not have as much info. as type:
			for (int i = 0; i < ((FunctionCallNode) expression)
					.getNumberOfArguments(); i++) {
				if (!((FunctionCallNode) expression).getArgument(i)
						.isSideEffectFree(false))
					throw new ABCRuntimeException(
							"Arguments with possible side-effects may not "
									+ "be used, since they might lead to "
									+ "unspecified behavior.  To correct this, "
									+ "evaluate the argument first and store "
									+ "the result in a variable, then use the "
									+ "variable in the function call.",
							((FunctionCallNode) expression).getArgument(i)
									.getSource().getSummary(false));
			}
			returnTypeNode = typeNode(functionEntity.getFirstDeclaration()
					.getSource(), ((Function) functionEntity).getType()
					.getReturnType());
			tmpVariable = nodeFactory.newVariableDeclarationNode(expression
					.getSource(), nodeFactory.newIdentifierNode(
					expression.getSource(), tempVariablePrefix
							+ tempVariableCounter++), returnTypeNode);
			before.add(tmpVariable);
			arguments
					.add(nodeFactory.newIdentifierExpressionNode(expression
							.getSource(), tmpVariable.getIdentifier().copy()));
			arguments.add(expression.copy());
			before.add(nodeFactory.newExpressionStatementNode(nodeFactory
					.newOperatorNode(expression.getSource(), Operator.ASSIGN,
							arguments)));
			result = new SideEffectFreeTriple(before,
					nodeFactory.newIdentifierExpressionNode(expression
							.getSource(), tmpVariable.getIdentifier().copy()),
					new ArrayList<BlockItemNode>());
		} else if (expression instanceof CastNode) {
			ExpressionNode argument = ((CastNode) expression).getArgument();

			if (argument.isSideEffectFree(false) || isMallocCall(argument)) {
				result = new SideEffectFreeTriple(
						new ArrayList<BlockItemNode>(), expression.copy(),
						new ArrayList<BlockItemNode>());
			} else {
				SideEffectFreeTriple sefArgument = processExpression(argument);

				result = new SideEffectFreeTriple(sefArgument.getBefore(),
						nodeFactory.newCastNode(expression.getSource(),
								((CastNode) expression).getCastType().copy(),
								sefArgument.getExpression()),
						sefArgument.getAfter());
			}

		} else if (expression.isSideEffectFree(false)) {
			// expression.parent().removeChild(expression.childIndex());
			result = new SideEffectFreeTriple(new ArrayList<BlockItemNode>(),
					expression.copy(), new ArrayList<BlockItemNode>());
		} else {
			throw new ABCUnsupportedException("removing side effects from:  "
					+ expression, expression.getSource().getSummary(false));
		}
		return result;
	}

	/**
	 * Take the arguments to an opEq expression (e.g. +=, *=, etc.) and return a
	 * statement of the form <code>left = left op right</code>.
	 * 
	 * @param source
	 *            The source for the original expression.
	 * @param left
	 *            The left operand.
	 * @param right
	 *            The right operand.
	 * @param operator
	 *            The operator.
	 * @return An assignment statement representing the operation.
	 */
	private StatementNode opEqStatement(Source source,
			OperatorNode.Operator operator, ExpressionNode left,
			ExpressionNode right) {
		ExpressionNode operation;
		List<ExpressionNode> arguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> assignArguments = new ArrayList<ExpressionNode>();
		Operator newOperator;

		switch (operator) {
		case PLUSEQ:
			newOperator = Operator.PLUS;
			break;
		case MINUSEQ:
			newOperator = Operator.MINUS;
			break;
		case TIMESEQ:
			newOperator = Operator.TIMES;
			break;
		case DIVEQ:
			newOperator = Operator.DIV;
			break;
		case MODEQ:
			newOperator = Operator.MOD;
			break;
		default:
			newOperator = operator;

		}
		arguments.add(left.copy());
		arguments.add(right.copy());
		assignArguments.add(left.copy());
		operation = nodeFactory.newOperatorNode(source, newOperator, arguments);
		assignArguments.add(operation);
		return nodeFactory.newExpressionStatementNode(nodeFactory
				.newOperatorNode(source, Operator.ASSIGN, assignArguments));
	}

	private SideEffectFreeTriple assign(OperatorNode assign)
			throws SyntaxException {

		// here and possibly in other cases one must check
		// for race conditions between the two arguments.

		List<BlockItemNode> before = new ArrayList<>();
		List<BlockItemNode> after = new ArrayList<>();
		ExpressionNode lhs;

		assert assign.getOperator() == Operator.ASSIGN;
		// Remove side effects from left hand side if necessary.
		if (assign.getArgument(0).isSideEffectFree(false)) {
			lhs = assign.getArgument(0).copy();
		} else {
			SideEffectFreeTriple lhsTriple = processExpression(assign
					.getArgument(0).copy());
			before.addAll(lhsTriple.getBefore());
			lhs = lhsTriple.getExpression();
			after.addAll(lhsTriple.getAfter());
		}
		// Remove side effects from right hand side if necessary.
		if (assign.getArgument(1).isSideEffectFree(false)) {
			// assign.parent().removeChild(assign.childIndex());
			before.add(nodeFactory.newExpressionStatementNode(assign.copy()));
		} else {
			SideEffectFreeTriple rhs = processExpression(assign.getArgument(1));
			List<ExpressionNode> arguments = new ArrayList<>();
			ExpressionNode newRhs = rhs.getExpression().copy();

			// lhs.parent().removeChild(lhs.childIndex());
			// newRhs.parent().removeChild(newRhs.childIndex());
			before.addAll(rhs.getBefore());
			arguments.add(lhs);
			arguments.add(newRhs);
			before.add(nodeFactory.newExpressionStatementNode(nodeFactory
					.newOperatorNode(assign.getSource(), Operator.ASSIGN,
							arguments)));
			after.addAll(rhs.getAfter());
		}
		return new SideEffectFreeTriple(before, lhs, after);
	}

	private SideEffectFreeTriple incrementOrDecrement(OperatorNode operator)
			throws SyntaxException {
		List<BlockItemNode> before = new ArrayList<>();
		List<BlockItemNode> after = new ArrayList<>();
		ExpressionNode base = operator.getArgument(0).copy();

		OperatorNode.Operator operation = operator.getOperator();
		ExpressionNode math, assignment;
		List<ExpressionNode> assignArguments = new ArrayList<>();
		List<ExpressionNode> mathArguments = new ArrayList<>();

		// base.parent().removeChild(base.childIndex());
		mathArguments.add(base.copy());
		mathArguments.add(nodeFactory.newIntegerConstantNode(
				operator.getSource(), "1"));
		switch (operation) {
		case PREINCREMENT:
		case POSTINCREMENT:
			math = nodeFactory.newOperatorNode(operator.getSource(),
					Operator.PLUS, mathArguments);
			break;
		default:
			math = nodeFactory.newOperatorNode(operator.getSource(),
					Operator.MINUS, mathArguments);
		}
		assignArguments.add(base.copy());
		assignArguments.add(math.copy());
		assignment = nodeFactory.newOperatorNode(operator.getSource(),
				Operator.ASSIGN, assignArguments);
		switch (operation) {
		case PREINCREMENT:
		case PREDECREMENT:
			before.add(nodeFactory.newExpressionStatementNode(assignment));
			break;
		default:
			after.add(nodeFactory.newExpressionStatementNode(assignment));
		}
		return new SideEffectFreeTriple(before, base, after);
	}

	private boolean isMallocCall(ExpressionNode node) {
		if (node instanceof FunctionCallNode) {
			ExpressionNode functionNode = ((FunctionCallNode) node)
					.getFunction();

			if (functionNode instanceof IdentifierExpressionNode) {
				String functionName = ((IdentifierExpressionNode) functionNode)
						.getIdentifier().name();

				if ("$malloc".equals(functionName))
					return true;
				if ("malloc".equals(functionName))
					return true;
			}
		}
		return false;
	}

	/**
	 * Is the ABC expression node an expression of the form
	 * <code>(t)$malloc(...)</code>? I.e., a cast expression for which the
	 * argument is a malloc call?
	 * 
	 * @param node
	 *            an expression node
	 * @return true iff this is a cast of a malloc call
	 */
	private boolean isCompleteMallocExpression(ExpressionNode node) {
		if (node instanceof CastNode) {
			ExpressionNode argumentNode = ((CastNode) node).getArgument();

			return isMallocCall(argumentNode);
		}
		return false;
	}
}
