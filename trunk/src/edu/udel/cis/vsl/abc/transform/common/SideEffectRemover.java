package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import edu.udel.cis.vsl.abc.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.EntityKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
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
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
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
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WaitNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.AtomicType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class SideEffectRemover implements Transformer {

	private ASTFactory unitFactory;
	private NodeFactory factory;

	private String tempVariablePrefix = "_TEMP_";
	private int tempVariableCounter = 0;

	public AST transform(AST unit) throws SyntaxException {
		ASTNode rootNode = unit.getRootNode();

		unitFactory = unit.getASTFactory();
		factory = unitFactory.getNodeFactory();
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
		return unitFactory.newTranslationUnit(rootNode);
	}

	private void removeSideEffects(FunctionDefinitionNode function)
			throws SyntaxException {
		function.setBody(processCompoundStatement(function.getBody()));
	}

	private CompoundStatementNode processCompoundStatement(
			CompoundStatementNode statement) throws SyntaxException {
		CompoundStatementNode result;
		List<BlockItemNode> items = new Vector<BlockItemNode>();

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
				if (((VariableDeclarationNode) item).getInitializer() == null) {
					items.add(item);
				} else {
					if (isSEF((ExpressionNode) ((VariableDeclarationNode) item)
							.getInitializer())) {
						// Only modify things if we need to.
						items.add(item);
					} else {
						ExpressionNode initializer;
						ExpressionNode initializerAssignment;
						List<ExpressionNode> assignmentArguments = new Vector<ExpressionNode>();
						IdentifierNode variable = ((VariableDeclarationNode) item)
								.getIdentifier();
						TypeNode type = ((VariableDeclarationNode) item)
								.getTypeNode().copy();
						VariableDeclarationNode newDeclaration;

						assert (((VariableDeclarationNode) item)
								.getInitializer() instanceof ExpressionNode);
						variable.parent().removeChild(variable.childIndex());
						initializer = (ExpressionNode) ((VariableDeclarationNode) item)
								.getInitializer();
						((VariableDeclarationNode) item).setInitializer(null);
						// type.parent().removeChild(type.childIndex());
						newDeclaration = factory.newVariableDeclarationNode(
								item.getSource(), factory.newIdentifierNode(
										variable.getSource(), variable.name()),
								type);
						items.add(newDeclaration);
						assignmentArguments.add(factory
								.newIdentifierExpressionNode(
										variable.getSource(), variable));
						assignmentArguments.add(initializer);
						initializerAssignment = factory.newOperatorNode(
								initializer.getSource(), Operator.ASSIGN,
								assignmentArguments);
						items.add(processStatement(factory
								.newExpressionStatementNode(initializerAssignment)));
					}
				}
			} else {
				// Other types of block items should be SEF.
				items.add(item);
			}
		}
		result = factory.newCompoundStatementNode(statement.getSource(), items);
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
		} else if (statement instanceof WaitNode) {
			result = waitStatement((WaitNode) statement);
		} else if (statement instanceof WhenNode) {
			result = whenStatement((WhenNode) statement);
		} else {
			result = statement;
		}
		return result;
	}

	private StatementNode chooseStatement(ChooseStatementNode statement)
			throws SyntaxException {
		ChooseStatementNode result;
		StatementNode defaultCase = statement.getDefaultCase();
		Vector<StatementNode> statements = new Vector<StatementNode>();
		Iterator<StatementNode> iterator = statement.childIterator();

		while (iterator.hasNext()) {
			statements.add(processStatement(iterator.next()));
		}
		result = factory.newChooseStatementNode(statement.getSource(),
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

		if (!isSEF(statement.getCondition())) {
			throw new ABCUnsupportedException(
					"side effects in a switch condition. "
							+ statement.getCondition(), statement
							.getCondition().getSource().getSummary());
		}
		body = processStatement(statement.getBody().copy());
		result = factory.newSwitchNode(statement.getSource(), statement
				.getCondition().copy(), body);
		return result;
	}

	private StatementNode returnStatement(ReturnNode statement)
			throws SyntaxException {
		StatementNode result;

		if (isSEF(statement.getExpression())) {
			result = statement;
		} else {
			Vector<BlockItemNode> newStatements = new Vector<BlockItemNode>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());
			TypeNode expressionType = typeNode(statement.getExpression()
					.getSource(), statement.getExpression().getType());
			IdentifierNode tempVariableID = factory.newIdentifierNode(statement
					.getExpression().getSource(), tempVariablePrefix
					+ tempVariableCounter++);
			VariableDeclarationNode tempVariableDeclaration = factory
					.newVariableDeclarationNode(statement.getExpression()
							.getSource(), tempVariableID, expressionType,
							triple.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(tempVariableDeclaration);
			newStatements.addAll(triple.getAfter());
			newStatements
					.add(factory.newReturnNode(statement.getSource(), factory
							.newIdentifierExpressionNode(statement
									.getExpression().getSource(),
									tempVariableID.copy())));
			result = factory.newCompoundStatementNode(statement.getSource(),
					newStatements);
		}
		return result;
	}

	/**
	 * 
	 * @param type
	 *            An AST type.
	 * @return An AST type node corresponding to the type.
	 */
	private TypeNode typeNode(Source source, Type type) {

		switch (type.kind()) {
		case ARRAY:
			ArrayType arrayType = (ArrayType) type;
			return factory.newArrayTypeNode(source,
					typeNode(source, arrayType.getElementType()), null);
		case ATOMIC:
			AtomicType atomicType = (AtomicType) type;
			return factory.newAtomicTypeNode(source,
					typeNode(source, atomicType.getBaseType()));
		case BASIC:
			StandardBasicType basicType = (StandardBasicType) type;
			return factory.newBasicTypeNode(source,
					basicType.getBasicTypeKind());
		case POINTER:
			PointerType pointerType = (PointerType) type;
			return factory.newPointerTypeNode(source,
					typeNode(source, pointerType.referencedType()));
		case VOID:
			return factory.newVoidTypeNode(source);
		case ENUMERATION:
		case FUNCTION:
		case HEAP:
		case OTHER_INTEGER:
		case PROCESS:
		case QUALIFIED:
		case STRUCTURE_OR_UNION:
		default:
			throw new ABCUnsupportedException("converting type " + type
					+ " to a type node.", source.getSummary());
		}
	}

	private StatementNode waitStatement(WaitNode statement)
			throws SyntaxException {
		StatementNode result;

		if (isSEF(statement.getExpression())) {
			result = statement;
		} else {
			Vector<BlockItemNode> newStatements = new Vector<BlockItemNode>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(factory.newWaitNode(statement.getSource(),
					triple.getExpression()));
			newStatements.addAll(triple.getAfter());
			result = factory.newCompoundStatementNode(statement.getSource(),
					newStatements);
		}
		return result;
	}

	private StatementNode labeledStatement(LabeledStatementNode statement)
			throws SyntaxException {
		StatementNode target = processStatement(statement.getStatement());
		LabelNode label = statement.getLabel();

		if (label != null && label.parent() != null) {
			label.parent().removeChild(label.childIndex());
		}
		return factory.newLabeledStatementNode(statement.getSource(), label,
				target);
	}

	private StatementNode ifStatement(IfNode statement) throws SyntaxException {
		StatementNode trueBranch = processStatement(statement.getTrueBranch());
		StatementNode falseBranch = null;

		if (statement.getFalseBranch() != null) {
			falseBranch = processStatement(statement.getFalseBranch());
		}
		if (!isSEF(statement.getCondition())) {
			ExpressionNode condition = statement.getCondition();
			SideEffectFreeTriple sefCondition = processExpression(condition);
			IdentifierNode tempVariableID = factory.newIdentifierNode(
					condition.getSource(), tempVariablePrefix
							+ tempVariableCounter++);
			List<BlockItemNode> items = new ArrayList<BlockItemNode>();

			items.addAll(sefCondition.getBefore());
			items.add(factory.newVariableDeclarationNode(condition.getSource(),
					tempVariableID, factory.newBasicTypeNode(
							condition.getSource(), BasicTypeKind.BOOL),
					sefCondition.getExpression()));
			items.addAll(sefCondition.getAfter());
			items.add(factory.newIfNode(statement.getSource(), factory
					.newIdentifierExpressionNode(condition.getSource(),
							tempVariableID.copy()), trueBranch, falseBranch));
			return factory.newCompoundStatementNode(statement.getSource(),
					items);
		}
		return factory.newIfNode(statement.getSource(), statement
				.getCondition().copy(), trueBranch, falseBranch);
	}

	private StatementNode whenStatement(WhenNode statement)
			throws SyntaxException {
		StatementNode result;
		ExpressionNode guard = statement.getGuard().copy();

		// guard.parent().removeChild(guard.childIndex());
		result = factory.newWhenNode(statement.getSource(), guard,
				processStatement(statement.getBody()));
		return result;
	}

	private StatementNode assertStatement(AssertNode statement)
			throws SyntaxException {
		StatementNode result;

		if (isSEF(statement.getExpression())) {
			result = statement;
		} else {
			Vector<BlockItemNode> newStatements = new Vector<BlockItemNode>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(factory.newAssertNode(statement.getSource(),
					triple.getExpression()));
			newStatements.addAll(triple.getAfter());
			result = factory.newCompoundStatementNode(statement.getSource(),
					newStatements);
		}
		return result;
	}

	private StatementNode assumeStatement(AssumeNode statement)
			throws SyntaxException {
		StatementNode result;

		if (isSEF(statement.getExpression())) {
			result = statement;
		} else {
			Vector<BlockItemNode> newStatements = new Vector<BlockItemNode>();
			SideEffectFreeTriple triple = processExpression(statement
					.getExpression());

			newStatements.addAll(triple.getBefore());
			newStatements.add(factory.newAssumeNode(statement.getSource(),
					triple.getExpression()));
			newStatements.addAll(triple.getAfter());
			result = factory.newCompoundStatementNode(statement.getSource(),
					newStatements);
		}
		return result;
	}

	private StatementNode expressionStatement(ExpressionStatementNode statement)
			throws SyntaxException {
		StatementNode result = null;
		ExpressionNode expression = statement.getExpression();

		// if (statement.parent() != null) {
		// statement.parent().removeChild(statement.childIndex());
		// }
		if (isSEF(expression)) {
			result = statement;
		} else {
			// expression.parent().removeChild(expression.childIndex());
			if (expression instanceof FunctionCallNode) {
				boolean sideEffectFree = true;

				for (int i = 0; i < ((FunctionCallNode) expression)
						.getNumberOfArguments(); i++) {
					sideEffectFree = sideEffectFree
							&& isSEF(((FunctionCallNode) expression)
									.getArgument(i));
				}
				if (sideEffectFree) {
					result = statement;
				} else {
					Vector<BlockItemNode> before = new Vector<BlockItemNode>();
					Vector<BlockItemNode> after = new Vector<BlockItemNode>();
					Vector<ExpressionNode> arguments = new Vector<ExpressionNode>();
					Vector<BlockItemNode> blockItems = new Vector<BlockItemNode>();

					for (int i = 0; i < ((FunctionCallNode) expression)
							.getNumberOfArguments(); i++) {
						ExpressionNode originalArgument = ((FunctionCallNode) expression)
								.getArgument(i);

						if (isSEF(originalArgument)) {
							arguments.add(originalArgument);
						} else {
							SideEffectFreeTriple triple = processExpression(originalArgument);

							before.addAll(triple.getBefore());
							arguments.add(triple.getExpression());
							after.addAll(triple.getAfter());
						}
					}
					blockItems.addAll(before);
					blockItems.add(factory.newExpressionStatementNode(factory
							.newFunctionCallNode(statement.getSource(),
									((FunctionCallNode) expression)
											.getFunction(), arguments)));
					blockItems.addAll(after);
					result = factory.newCompoundStatementNode(
							statement.getSource(), blockItems);
				}
			} else if (expression instanceof SpawnNode) {
				FunctionCallNode call = ((SpawnNode) expression).getCall();
				boolean sideEffectFree = true;

				for (int i = 0; i < call.getNumberOfArguments(); i++) {
					sideEffectFree = sideEffectFree
							&& isSEF(call.getArgument(i));
				}
				if (sideEffectFree) {
					result = statement;
				} else {
					Vector<BlockItemNode> before = new Vector<BlockItemNode>();
					Vector<BlockItemNode> after = new Vector<BlockItemNode>();
					Vector<ExpressionNode> arguments = new Vector<ExpressionNode>();
					Vector<BlockItemNode> blockItems = new Vector<BlockItemNode>();

					for (int i = 0; i < call.getNumberOfArguments(); i++) {
						ExpressionNode originalArgument = call.getArgument(i);

						if (isSEF(originalArgument)) {
							arguments.add(originalArgument);
						} else {
							SideEffectFreeTriple triple = processExpression(originalArgument);

							before.addAll(triple.getBefore());
							arguments.add(triple.getExpression());
							after.addAll(triple.getAfter());
						}
					}
					blockItems.addAll(before);
					blockItems.add(factory.newExpressionStatementNode(factory
							.newSpawnNode(
									statement.getSource(),
									factory.newFunctionCallNode(
											statement.getSource(),
											call.getFunction(), arguments))));
					blockItems.addAll(after);
					result = factory.newCompoundStatementNode(
							statement.getSource(), blockItems);
				}
			} else if (expression instanceof OperatorNode) {
				OperatorNode.Operator operator = ((OperatorNode) expression)
						.getOperator();

				switch (operator) {
				case PREINCREMENT:
				case POSTINCREMENT:
					ExpressionNode add;
					Vector<ExpressionNode> addArguments = new Vector<ExpressionNode>();
					Vector<ExpressionNode> incrementArguments = new Vector<ExpressionNode>();
					ExpressionNode variableExpression = ((OperatorNode) expression)
							.getArgument(0).copy();

					// variableExpression.parent().removeChild(
					// variableExpression.childIndex());
					addArguments.add(variableExpression);
					addArguments.add(factory.newIntegerConstantNode(
							expression.getSource(), "1"));
					add = factory.newOperatorNode(expression.getSource(),
							Operator.PLUS, addArguments);
					incrementArguments.add(variableExpression.copy());
					incrementArguments.add(add);
					result = factory.newExpressionStatementNode(factory
							.newOperatorNode(expression.getSource(),
									Operator.ASSIGN, incrementArguments));
					break;
				case PREDECREMENT:
				case POSTDECREMENT:
					ExpressionNode subtract;
					Vector<ExpressionNode> subtractArguments = new Vector<ExpressionNode>();
					Vector<ExpressionNode> decrementArguments = new Vector<ExpressionNode>();

					subtractArguments.add(((OperatorNode) expression)
							.getArgument(0).copy());
					subtractArguments.add(factory.newIntegerConstantNode(
							expression.getSource(), "1"));
					subtract = factory.newOperatorNode(expression.getSource(),
							Operator.MINUS, subtractArguments);
					decrementArguments.add(((OperatorNode) expression)
							.getArgument(0).copy());
					decrementArguments.add(subtract);
					result = factory.newExpressionStatementNode(factory
							.newOperatorNode(expression.getSource(),
									Operator.ASSIGN, decrementArguments));
					break;
				case ASSIGN:
					ExpressionNode rhs = ((OperatorNode) expression)
							.getArgument(1);

					if (isSEF(rhs)) {
						result = statement;
					} else if (isCompleteMallocExpression(rhs)) {
						result = statement;
					} else {
						SideEffectFreeTriple triple;
						Vector<BlockItemNode> blockItems = new Vector<BlockItemNode>();

						if (rhs instanceof FunctionCallNode) {
							// TODO check that arguments are SEF
							result = statement;
						} else if (rhs instanceof SpawnNode) {
							// TODO check that arguments are SEF
							result = statement;
						} else {
							Vector<ExpressionNode> assignArguments = new Vector<ExpressionNode>();
							ExpressionNode lhs = ((OperatorNode) expression)
									.getArgument(0).copy();
							ExpressionNode assignRhs;

							// lhs.parent().removeChild(lhs.childIndex());
							assignArguments.add(lhs);
							triple = processExpression(rhs);
							blockItems.addAll(triple.getBefore());
							assignRhs = triple.getExpression().copy();
							// if (assignRhs.parent() != null) {
							// assignRhs.parent().removeChild(
							// assignRhs.childIndex());
							// }
							assignArguments.add(assignRhs);
							blockItems.add(factory
									.newExpressionStatementNode(factory
											.newOperatorNode(
													expression.getSource(),
													Operator.ASSIGN,
													assignArguments)));
							blockItems.addAll(triple.getAfter());
							result = factory.newCompoundStatementNode(
									statement.getSource(), blockItems);
						}
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
					Vector<ExpressionNode> operands = new Vector<ExpressionNode>();
					StatementNode sideEffectFreeStatement;
					Vector<BlockItemNode> blockItems = new Vector<BlockItemNode>();

					operands.add(leftTriple.getExpression());
					operands.add(rightTriple.getExpression());
					sideEffectFreeStatement = factory
							.newExpressionStatementNode(factory
									.newOperatorNode(statement.getSource(),
											((OperatorNode) expression)
													.getOperator(), operands));
					blockItems.addAll(leftTriple.getBefore());
					blockItems.addAll(rightTriple.getBefore());
					blockItems.add(sideEffectFreeStatement);
					blockItems.addAll(rightTriple.getAfter());
					blockItems.addAll(leftTriple.getAfter());
					result = factory.newCompoundStatementNode(
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
						result = factory.newCompoundStatementNode(
								expression.getSource(), statements);
					}
					break;
				default:
					throw new ABCUnsupportedException("this operation: "
							+ statement, statement.getSource().getSummary());
				}
			} else {
				throw new ABCUnsupportedException(
						"removing side effects from this expression statement: "
								+ statement, statement.getSource().getSummary());
			}
		}
		return result;
	}

	private StatementNode loopStatement(LoopNode statement)
			throws SyntaxException {
		ExpressionNode condition = statement.getCondition().copy();
		StatementNode newBody = processStatement(statement.getBody());
		ExpressionNode invariant = statement.getInvariant();
		StatementNode result;

		// condition.parent().removeChild(condition.childIndex());
		if (invariant != null) {
			invariant.parent().removeChild(invariant.childIndex());
		}
		if (!isSEF(condition)) {
			// If a side effect exists in a condition, convert from
			// while (e) {S;} to while (true) {int tmp_X = e; if (!e) break; S;}
			SideEffectFreeTriple sefCondition = processExpression(condition
					.copy());
			List<BlockItemNode> bodyItems = new ArrayList<BlockItemNode>();
			List<ExpressionNode> ifArgument = new ArrayList<ExpressionNode>();
			IdentifierNode tempVariableID = factory.newIdentifierNode(
					condition.getSource(), tempVariablePrefix
							+ tempVariableCounter++);

			bodyItems.addAll(sefCondition.getBefore());
			bodyItems.add(factory.newVariableDeclarationNode(condition
					.getSource(), tempVariableID, factory.newBasicTypeNode(
					condition.getSource(), BasicTypeKind.BOOL), sefCondition
					.getExpression().copy()));
			bodyItems.addAll(sefCondition.getAfter());
			ifArgument.add(factory.newIdentifierExpressionNode(
					condition.getSource(), tempVariableID.copy()));
			bodyItems.add(factory.newIfNode(condition.getSource(), factory
					.newOperatorNode(condition.getSource(), Operator.NOT,
							ifArgument), factory.newBreakNode(condition
					.getSource())));
			bodyItems.add(newBody.copy());
			newBody = factory.newCompoundStatementNode(newBody.getSource(),
					bodyItems);
			condition = factory.newBooleanConstantNode(condition.getSource(),
					true);
		}
		switch (statement.getKind()) {
		case FOR:
			ForLoopInitializerNode initializer = ((ForLoopNode) statement)
					.getInitializer();
			ExpressionNode incrementer = ((ForLoopNode) statement)
					.getIncrementer().copy();
			// We wrap the incrementer as an expression statement, then remove
			// side effects.
			// If it's a "simple" incrementer, the result will also be an
			// expression statement,
			// otherwise the result will be some kind of compound statement.
			// This
			// removes ++, +=, etc. from the AST and also provides the
			// opportunity to modify
			// this for loop into a while loop if necessary for a complex
			// incrementer.
			StatementNode modifiedIncrementer = expressionStatement(factory
					.newExpressionStatementNode(incrementer));

			// If initializer is not null, work on a copy to maintain tree
			// structure.
			if (initializer != null) {
				initializer = initializer.copy();
			}
			if (modifiedIncrementer instanceof ExpressionStatementNode) {
				result = factory.newForLoopNode(statement.getSource(),
						initializer, condition.copy(),
						((ExpressionStatementNode) modifiedIncrementer)
								.getExpression().copy(), newBody.copy(),
						invariant);
			} else {
				List<BlockItemNode> bodyItems = new ArrayList<BlockItemNode>();
				List<BlockItemNode> allItems = new ArrayList<BlockItemNode>();
				StatementNode loop;

				bodyItems.add(newBody);
				bodyItems.add(modifiedIncrementer);
				loop = factory.newWhileLoopNode(statement.getSource(),
						condition, factory.newCompoundStatementNode(
								newBody.getSource(), bodyItems), invariant
								.copy());
				if (initializer instanceof ExpressionNode) {
					allItems.add(factory
							.newExpressionStatementNode((ExpressionNode) initializer));
				} else if (initializer instanceof DeclarationListNode) {
					Iterator<VariableDeclarationNode> iter = ((DeclarationListNode) initializer)
							.childIterator();
					while (iter.hasNext()) {
						allItems.add(iter.next());
					}
				} else {
					throw new ABCUnsupportedException(
							"converting initializer declaration to statement list.",
							initializer.getSource().getSummary());
				}
				allItems.add(loop);
				result = factory.newCompoundStatementNode(
						statement.getSource(), allItems);
			}
			break;
		case DO_WHILE:
			result = factory.newDoLoopNode(statement.getSource(), condition,
					newBody, invariant);
			break;
		default:
			result = factory.newWhileLoopNode(statement.getSource(), condition,
					newBody, invariant);
		}
		return result;
	}

	private boolean isSEF(ExpressionNode expression) {
		boolean result = true;

		if (expression instanceof FunctionCallNode) {
			result = false;
		} else if (expression instanceof OperatorNode) {
			switch (((OperatorNode) expression).getOperator()) {
			case ASSIGN:
			case DIVEQ:
			case MINUSEQ:
			case MODEQ:
			case PLUSEQ:
			case POSTDECREMENT:
			case POSTINCREMENT:
			case PREDECREMENT:
			case PREINCREMENT:
			case SHIFTLEFTEQ:
			case SHIFTRIGHTEQ:
			case TIMESEQ:
				result = false;
				break;
			default:
				for (int i = 0; i < ((OperatorNode) expression)
						.getNumberOfArguments(); i++) {
					result = result
							&& isSEF(((OperatorNode) expression).getArgument(i));
				}
			}
		} else if (expression instanceof SpawnNode) {
			result = false;
		} else if (expression instanceof CastNode) {
			result = isSEF(((CastNode) expression).getArgument());
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
			Vector<BlockItemNode> before = new Vector<BlockItemNode>();
			Vector<BlockItemNode> after = new Vector<BlockItemNode>();

			switch (((OperatorNode) expression).getOperator()) {
			case ASSIGN:
				result = assign((OperatorNode) expression);
				break;
			case ADDRESSOF:
			case DEREFERENCE:
				if (isSEF(left)) {
					result = new SideEffectFreeTriple(
							new Vector<BlockItemNode>(), expression,
							new Vector<BlockItemNode>());
				} else {
					leftTriple = processExpression(left);
					operands.add(leftTriple.getExpression());
					result = new SideEffectFreeTriple(leftTriple.getBefore(),
							factory.newOperatorNode(expression.getSource(),
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
				left = ((OperatorNode) expression).getArgument(0);
				right = ((OperatorNode) expression).getArgument(1);
				leftTriple = processExpression(left);
				rightTriple = processExpression(right);
				operands.add(leftTriple.getExpression());
				operands.add(rightTriple.getExpression());
				sideEffectFreeExpression = factory.newOperatorNode(
						expression.getSource(),
						((OperatorNode) expression).getOperator(), operands);
				before.addAll(leftTriple.getBefore());
				before.addAll(rightTriple.getBefore());
				after.addAll(leftTriple.getAfter());
				after.addAll(rightTriple.getAfter());
				result = new SideEffectFreeTriple(before,
						sideEffectFreeExpression, after);
				break;
			case PLUSEQ:
			case MINUSEQ:
			case TIMESEQ:
			case DIVEQ:
			case MODEQ:
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
			default:
				throw new ABCUnsupportedException(
						"removing side effects from: " + expression, expression
								.getSource().getSummary());
			}
		} else if (expression instanceof FunctionCallNode) {
			Vector<BlockItemNode> before = new Vector<BlockItemNode>();
			ExpressionNode functionExpression = ((FunctionCallNode) expression)
					.getFunction();
			Entity functionEntity;
			TypeNode returnTypeNode;
			VariableDeclarationNode tmpVariable;
			Vector<ExpressionNode> arguments = new Vector<ExpressionNode>();

			assert functionExpression instanceof IdentifierExpressionNode;
			functionEntity = ((IdentifierExpressionNode) functionExpression)
					.getIdentifier().getEntity();
			assert functionEntity.getEntityKind() == EntityKind.FUNCTION;
			returnTypeNode = typeNode(functionEntity.getFirstDeclaration()
					.getSource(), ((Function) functionEntity).getType().getReturnType());
			tmpVariable = factory.newVariableDeclarationNode(expression
					.getSource(), factory.newIdentifierNode(
					expression.getSource(), tempVariablePrefix
							+ tempVariableCounter++), returnTypeNode);
			before.add(tmpVariable);
			arguments.add(factory.newIdentifierExpressionNode(
					expression.getSource(), tmpVariable.getIdentifier().copy()));
			arguments.add(expression.copy());
			before.add(factory.newExpressionStatementNode(factory
					.newOperatorNode(expression.getSource(), Operator.ASSIGN,
							arguments)));
			result = new SideEffectFreeTriple(before,
					factory.newIdentifierExpressionNode(expression.getSource(),
							tmpVariable.getIdentifier().copy()),
					new Vector<BlockItemNode>());
		} else if (expression instanceof CastNode) {
			ExpressionNode argument = ((CastNode) expression).getArgument();

			if (isSEF(argument)) {
				result = new SideEffectFreeTriple(new Vector<BlockItemNode>(),
						expression.copy(), new Vector<BlockItemNode>());
			} else {
				SideEffectFreeTriple sefArgument = processExpression(argument);

				result = new SideEffectFreeTriple(sefArgument.getBefore(),
						factory.newCastNode(expression.getSource(),
								((CastNode) expression).getCastType().copy(),
								sefArgument.getExpression()),
						sefArgument.getAfter());
			}

		} else if (isSEF(expression)) {
			// expression.parent().removeChild(expression.childIndex());
			result = new SideEffectFreeTriple(new Vector<BlockItemNode>(),
					expression.copy(), new Vector<BlockItemNode>());
		} else {
			throw new ABCUnsupportedException("removing side effects from:  "
					+ expression, expression.getSource().getSummary());
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
		operation = factory.newOperatorNode(source, newOperator, arguments);
		assignArguments.add(operation);
		return factory.newExpressionStatementNode(factory.newOperatorNode(
				source, Operator.ASSIGN, assignArguments));
	}

	private SideEffectFreeTriple assign(OperatorNode assign)
			throws SyntaxException {
		Vector<BlockItemNode> before = new Vector<BlockItemNode>();
		Vector<BlockItemNode> after = new Vector<BlockItemNode>();
		ExpressionNode lhs = assign.getArgument(0).copy();

		assert assign.getOperator() == Operator.ASSIGN;
		// Assume left hand side is side effect free.
		if (isSEF(assign.getArgument(1))) {
			// assign.parent().removeChild(assign.childIndex());
			before.add(factory.newExpressionStatementNode(assign.copy()));
		} else {
			SideEffectFreeTriple rhs = processExpression(assign.getArgument(1)
					.copy());
			Vector<ExpressionNode> arguments = new Vector<ExpressionNode>();
			ExpressionNode newRhs = rhs.getExpression().copy();

			// lhs.parent().removeChild(lhs.childIndex());
			// newRhs.parent().removeChild(newRhs.childIndex());
			before.addAll(rhs.getBefore());
			arguments.add(lhs);
			arguments.add(newRhs);
			before.add(factory.newExpressionStatementNode(factory
					.newOperatorNode(assign.getSource(), Operator.ASSIGN,
							arguments)));
			after.addAll(rhs.getAfter());
		}
		return new SideEffectFreeTriple(before, lhs, after);
	}

	private SideEffectFreeTriple incrementOrDecrement(OperatorNode operator)
			throws SyntaxException {
		Vector<BlockItemNode> before = new Vector<BlockItemNode>();
		Vector<BlockItemNode> after = new Vector<BlockItemNode>();
		ExpressionNode base = operator.getArgument(0).copy();

		OperatorNode.Operator operation = operator.getOperator();
		ExpressionNode math, assignment;
		Vector<ExpressionNode> assignArguments = new Vector<ExpressionNode>();
		Vector<ExpressionNode> mathArguments = new Vector<ExpressionNode>();

		// base.parent().removeChild(base.childIndex());
		mathArguments.add(base.copy());
		mathArguments.add(factory.newIntegerConstantNode(operator.getSource(),
				"1"));
		switch (operation) {
		case PREINCREMENT:
		case POSTINCREMENT:
			math = factory.newOperatorNode(operator.getSource(), Operator.PLUS,
					mathArguments);
			break;
		default:
			math = factory.newOperatorNode(operator.getSource(),
					Operator.MINUS, mathArguments);
		}
		assignArguments.add(base.copy());
		assignArguments.add(math.copy());
		assignment = factory.newOperatorNode(operator.getSource(),
				Operator.ASSIGN, assignArguments);
		switch (operation) {
		case PREINCREMENT:
		case PREDECREMENT:
			before.add(factory.newExpressionStatementNode(assignment));
			break;
		default:
			after.add(factory.newExpressionStatementNode(assignment));
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
