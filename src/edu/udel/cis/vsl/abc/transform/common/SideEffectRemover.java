package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
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
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode.ExpressionKind;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
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
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
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
 * scope.
 * 
 * There are 3 places where function calls occur: (1) an expression statement in
 * which the expression is a function call. In this case the return value is not
 * used and you just need to create temps for the arguments if they have side
 * effects. (2) an expression statement in which the expression is an assignment
 * and the right-hand side is a function call. (3) somewhere else inside an
 * expression.
 * 
 * @author zirkel
 *
 */
public class SideEffectRemover extends BaseTransformer {

	/* Static Fields */

	public final static String CODE = "sef";

	public final static String LONG_NAME = "SideEffectRemover";

	public final static String SHORT_DESCRIPTION = "transforms program to side-effect-free form";

	private final static String tempVariablePrefix = "$" + CODE + "$";

	/* Instance Fields */

	private int tempVariableCounter = 0;

	/* Constructors */

	public SideEffectRemover(ASTFactory astFactory) {
		super(CODE, LONG_NAME, SHORT_DESCRIPTION, astFactory);
	}

	/* Private methods */

	/**
	 * Given a {@link Type}, creates a new type node tree that will generate
	 * that type.
	 * 
	 * @param type
	 *            An AST type.
	 * @return An AST type node corresponding to the type.
	 */
	private TypeNode typeNode(Source source, Type type) {
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
				String dimensionString = Integer.toString(domainType
						.getDimension());
				IntegerConstantNode dimensionNode;

				try {
					dimensionNode = nodeFactory.newIntegerConstantNode(source,
							dimensionString);
				} catch (SyntaxException e) {
					throw new ABCRuntimeException(
							"error creating integer constant node for "
									+ dimensionString);
				}
				return nodeFactory.newDomainTypeNode(source, dimensionNode);
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
		case OTHER_INTEGER: {
			// for now, just using "int" for all the "other integer types"
			return nodeFactory.newBasicTypeNode(source, BasicTypeKind.INT);
		}
		case PROCESS: {
			return nodeFactory.newTypedefNameNode(
					nodeFactory.newIdentifierNode(source, "$proc"), null);
		}
		case FUNCTION:
		case HEAP:
		case QUALIFIED:
		default:
			throw new ABCUnsupportedException("converting type " + type
					+ " to a type node.", source.getSummary(false));
		}
	}

	/**
	 * Is the given expression a call to one of the functions "malloc" or
	 * "$malloc"?
	 * 
	 * @param node
	 *            any expression node
	 * @return <code>true</code> iff the node is a function call node for a
	 *         function named "malloc" or "$malloc"
	 */
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
	 * <p>
	 * Returns a new triple resulting from the processing of a function call
	 * node. The expression in the new triple will be either a new temporary
	 * variable holding the result of the function call, or it will be the
	 * function call itself (with arguments possibly modified), depending on the
	 * value of parameter <code>storeResult</code>.
	 * </p>
	 * 
	 * <p>
	 * In the end, the only places where a function call node may occur are (1)
	 * as an expression statement, i.e., the function call is the complete
	 * statement, or (2) as the expression which is the complete right-hand side
	 * of an assignment. Furthermore, in the end, the only place an assignment
	 * can occur is as an expression statement. Therefore, when this method is
	 * invoked from an expression statement or when processing the right-hand
	 * side of an assignment expression, it should be invoked with
	 * <code>storeResult</code> <code>false</code>. In all other cases it should
	 * be called with <code>storeResult</code> <code>true</code>.
	 * </p>
	 * 
	 * @param callNode
	 *            a function call node
	 * @param storeResult
	 *            if <code>true</code>, a new temporary variable will be created
	 *            and the result of the function call will be assigned to the
	 *            temp variable in the "before" section; the temp variable
	 *            itself will be the "expression" component of the returned
	 *            triple. If <code>false</code>, this temp variable will not be
	 *            created and the function call expression itself (suitably
	 *            modified) will be the "expression" component.
	 * @return a new triple with side-effects processed and stored in the before
	 *         and after sections
	 */
	private SideEffectFreeTriple processFunctionCall(FunctionCallNode callNode,
			boolean storeResult) {
		Source source = callNode.getSource();
		ExpressionNode functionNode = callNode.getFunction();
		int numArgs = callNode.getNumberOfArguments();
		List<ExpressionNode> arguments = new LinkedList<>();
		int numContextArgs = callNode.getNumberOfContextArguments();
		List<ExpressionNode> contextArgs = new LinkedList<>();
		boolean change = false;
		List<BlockItemNode> before = new LinkedList<>(), after = new LinkedList<>();
		SideEffectFreeTriple result;

		if (!functionNode.isSideEffectFree(false)) {
			SideEffectFreeTriple functionTriple = processExpression(functionNode);

			before.addAll(functionTriple.getBefore());
			after.addAll(functionTriple.getAfter());
			functionNode = functionTriple.getExpression();
			change = true;
		}
		for (int i = 0; i < numArgs; i++) {
			ExpressionNode oldArg = callNode.getArgument(i);

			if (oldArg.isSideEffectFree(false)) {
				arguments.add(oldArg);
			} else {
				SideEffectFreeTriple argTriple = processExpression(oldArg);

				before.addAll(argTriple.getBefore());
				after.addAll(argTriple.getAfter());
				arguments.add(argTriple.getExpression());
				change = true;
			}
		}
		for (int i = 0; i < numContextArgs; i++) {
			ExpressionNode oldArg = callNode.getContextArgument(i);

			if (oldArg.isSideEffectFree(false)) {
				contextArgs.add(oldArg);
			} else {
				SideEffectFreeTriple argTriple = processExpression(oldArg);

				before.addAll(argTriple.getBefore());
				after.addAll(argTriple.getAfter());
				contextArgs.add(argTriple.getExpression());
				change = true;
			}
		}
		if (change) {
			functionNode.remove();
			for (ExpressionNode arg : arguments)
				arg.remove();

			if (numContextArgs == 0) {
				callNode = nodeFactory.newFunctionCallNode(source,
						functionNode, arguments, null);
			} else {
				for (ExpressionNode contextArg : contextArgs)
					contextArg.remove();
				callNode = nodeFactory.newFunctionCallNode(source,
						functionNode, contextArgs, arguments, null);
			}
		}
		if (storeResult) {
			// T tmp = f(...);
			// resulting expression: tmp
			String tmpId = tempVariablePrefix + tempVariableCounter;

			tempVariableCounter++;
			callNode.remove();

			TypeNode returnTypeNode = typeNode(source,
					((FunctionType) functionNode.getType()).getReturnType());
			VariableDeclarationNode decl = nodeFactory
					.newVariableDeclarationNode(source,
							nodeFactory.newIdentifierNode(source, tmpId),
							returnTypeNode, callNode);
			IdentifierExpressionNode tmpVarExpr = nodeFactory
					.newIdentifierExpressionNode(source,
							nodeFactory.newIdentifierNode(source, tmpId));

			before.add(decl);
			result = new SideEffectFreeTriple(before, tmpVarExpr, after);
		} else {
			result = new SideEffectFreeTriple(before, callNode, after);
		}
		return result;
	}

	/**
	 * Removes side effects from a spawn statement. The spawn statement has one
	 * argument, which is a function call. This processes the function call.
	 * 
	 * @param spawn
	 *            a spawn statement node
	 * @param storeResult
	 *            should the result of the spawn be stored in a temporary
	 *            variable which replaces the expression?
	 * @return a new side-effect-free triple equivalent to the original spawn
	 */
	private SideEffectFreeTriple processSpawn(SpawnNode spawn,
			boolean storeResult) {
		FunctionCallNode call = spawn.getCall();
		SideEffectFreeTriple result = processFunctionCall(call, false);
		Source source = spawn.getSource();

		if (result.isTrivial()) {
			result.setExpression(spawn);
		} else {
			result.setExpression(nodeFactory.newSpawnNode(spawn.getSource(),
					(FunctionCallNode) result.getExpression()));
		}
		if (storeResult) {
			String tmpId = tempVariablePrefix + tempVariableCounter;

			tempVariableCounter++;

			VariableDeclarationNode decl = nodeFactory
					.newVariableDeclarationNode(source,
							nodeFactory.newIdentifierNode(source, tmpId),
							typeNode(source, spawn.getType()),
							result.getExpression());

			result.getBefore().add(decl);
			result.setExpression(nodeFactory.newIdentifierExpressionNode(
					source, nodeFactory.newIdentifierNode(source, tmpId)));
		}
		return result;
	}

	/**
	 * <p>
	 * Processes the side-effect expressions in an assignment expression. The
	 * result is a side-effect-free triple in which all components consist of
	 * side-effect-free statements or simple assignments (where neither the
	 * right-hand-side nor left-hand-side have side-effects).
	 * </p>
	 * 
	 * <p>
	 * The structure of the triple depends on the value of
	 * <code>storeResult</code>. If <code>true</code>, the assignment will take
	 * place in the "before" component of the triple and the expression
	 * component will repeat the left-hand-side of the assignment after removal
	 * of side-effects. If <code>false</code>, the expression component contains
	 * the assignment.
	 * </p>
	 * 
	 * <p>
	 * If the result is being used to form an expression statement, the value of
	 * the assignment is not needed and this method should be called with
	 * <code>storeResult</code> <code>false</code>. Otherwise, the result is
	 * part of some larger expression, and this method should be called with
	 * <code>storeResult</code> <code>true</code>.
	 * </p>
	 * 
	 * @param assign
	 *            an operator node in which the operator is ASSIGN
	 * @param storeResult
	 *            will the value of this expression need to be stored in a
	 *            temporary variable so that is can be used again?
	 * @return a new triple as described above
	 */
	private SideEffectFreeTriple processAssignment(OperatorNode assign,
			boolean storeResult) {
		ExpressionNode lhs = assign.getArgument(0), rhs = assign.getArgument(1);
		ExpressionKind rhsKind = rhs.expressionKind();
		List<BlockItemNode> before = new LinkedList<>(), after = new LinkedList<>();
		SideEffectFreeTriple result;
		SideEffectFreeTriple lhsTriple = processExpression(
				assign.getArgument(0), false);
		boolean storeRhsResult = !(rhsKind == ExpressionKind.FUNCTION_CALL
				|| rhsKind == ExpressionKind.SPAWN || rhs
				.isSideEffectFree(false));
		SideEffectFreeTriple rhsTriple = processExpression(
				assign.getArgument(1), storeRhsResult);

		if (!lhsTriple.isTrivial() || !rhsTriple.isTrivial()) {
			before.addAll(rhsTriple.getBefore());
			before.addAll(lhsTriple.getBefore());
			after.addAll(rhsTriple.getAfter());
			after.addAll(lhsTriple.getAfter());
			lhs = lhsTriple.getExpression();
			rhs = rhsTriple.getExpression();
			lhs.remove();
			rhs.remove();
			assign = nodeFactory.newOperatorNode(assign.getSource(),
					assign.getOperator(), Arrays.asList(lhs, rhs));
		}
		if (storeResult) {
			assign.remove();
			before.add(nodeFactory.newExpressionStatementNode(assign));
			result = new SideEffectFreeTriple(before, lhs.copy(), after);
		} else {
			result = new SideEffectFreeTriple(before, assign, after);
		}
		return result;
	}

	private SideEffectFreeTriple processUnaryExpression(OperatorNode expression) {
		ExpressionNode operand = expression.getArgument(0);

		if (operand.isSideEffectFree(false)) {
			return new SideEffectFreeTriple(expression);
		} else {
			SideEffectFreeTriple triple = processExpression(operand);
			ExpressionNode newOperand = triple.getExpression();

			triple.setExpression(nodeFactory.newOperatorNode(
					expression.getSource(), expression.getOperator(),
					Arrays.asList(newOperand)));
			return triple;
		}
	}

	private SideEffectFreeTriple processGenericBinaryOperator(
			OperatorNode expression) {
		ExpressionNode left = expression.getArgument(0);
		ExpressionNode right = expression.getArgument(1);
		SideEffectFreeTriple leftTriple = processExpression(left);
		SideEffectFreeTriple rightTriple = processExpression(right);
		ExpressionNode sideEffectFreeExpression = nodeFactory.newOperatorNode(
				expression.getSource(), expression.getOperator(), Arrays
						.asList(leftTriple.getExpression().copy(), rightTriple
								.getExpression().copy()));
		List<BlockItemNode> before = new LinkedList<>(), after = new LinkedList<>();
		SideEffectFreeTriple result;

		before.addAll(leftTriple.getBefore());
		before.addAll(rightTriple.getBefore());
		after.addAll(leftTriple.getAfter());
		after.addAll(rightTriple.getAfter());
		result = new SideEffectFreeTriple(before, sideEffectFreeExpression,
				after);
		return result;
	}

	private SideEffectFreeTriple processAssignmentOperator(
			OperatorNode expression) {
		ExpressionNode left = expression.getArgument(0);
		ExpressionNode right = expression.getArgument(1);
		SideEffectFreeTriple leftTriple = processExpression(left);
		SideEffectFreeTriple rightTriple = processExpression(right);
		StatementNode assignment = opEqStatement(expression.getSource(),
				expression.getOperator(), leftTriple.getExpression(),
				rightTriple.getExpression());
		List<BlockItemNode> before = new LinkedList<>(), after = new LinkedList<>();
		SideEffectFreeTriple result;

		before.addAll(leftTriple.getBefore());
		before.addAll(rightTriple.getBefore());
		before.add(assignment);
		after.addAll(leftTriple.getAfter());
		after.addAll(rightTriple.getAfter());
		result = new SideEffectFreeTriple(before, left.copy(), after);
		return result;
	}

	private SideEffectFreeTriple processComma(OperatorNode expression) {
		ExpressionNode left = expression.getArgument(0);
		ExpressionNode right = expression.getArgument(1);
		SideEffectFreeTriple result;

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
			List<BlockItemNode> before = new LinkedList<>();
			SideEffectFreeTriple leftTriple = processExpression(left);

			before.addAll(leftTriple.getBefore());
			// In case there is a possible error side effect, add an
			// expression statement for the LHS. Normally this will
			// essentially be a noop.
			// if (!leftTriple.getExpression().isSideEffectFree(true)) {
			// before.add(nodeFactory
			// .newExpressionStatementNode(leftTriple
			// .getExpression().copy()));
			// }
			// All side effects from the left operand must be evaluated
			// before the right operand since there is a sequence point
			// between evaluations of left and right operands (per C11
			// standard Sec. 6.5.17)
			before.addAll(leftTriple.getAfter());
			if (right.isSideEffectFree(false)) {
				result = new SideEffectFreeTriple(before, right,
						new ArrayList<BlockItemNode>());
			} else {
				SideEffectFreeTriple rightTriple = processExpression(right);

				before.addAll(rightTriple.getBefore());
				result = new SideEffectFreeTriple(before,
						rightTriple.getExpression(), rightTriple.getAfter());
			}
		}
		return result;
	}

	private SideEffectFreeTriple processConditional(OperatorNode expression) {
		// x = cond ? a : b;
		// ==>
		// cond_before;
		// if(cond_expr) a_before; else b_before;
		// x = cond_expr? a_expr : b_expr;
		// cond_after;
		// if(cond_expr) a_after; else b_after;
		ExpressionNode condition = expression.getArgument(0), trueExpr = expression
				.getArgument(1), falseExpr = expression.getArgument(2);
		SideEffectFreeTriple condTriple, trueTriple, falseTriple;
		StatementNode trueOrFalseBefore, trueOrFalseAfter, trueBlock, falseBlock;
		NodeFactory nodeFactory = this.astFactory.getNodeFactory();
		List<BlockItemNode> trueList, falseList;
		List<BlockItemNode> before = new LinkedList<>(), after = new LinkedList<>();

		condTriple = processExpression(condition);
		trueTriple = processExpression(trueExpr);
		falseTriple = processExpression(falseExpr);

		ExpressionNode sideEffectFreeExpression = nodeFactory.newOperatorNode(
				expression.getSource(), expression.getOperator(), Arrays
						.asList(condTriple.getExpression().copy(), trueTriple
								.getExpression().copy(), falseTriple
								.getExpression().copy()));

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
				trueOrFalseBefore = nodeFactory.newIfNode(
						condition.getSource(), nodeFactory.newOperatorNode(
								condition.getSource(), Operator.NOT,
								Arrays.asList(condition.copy())), falseBlock);
			else if (falseBlock == null)
				trueOrFalseBefore = nodeFactory.newIfNode(
						condition.getSource(), condition.copy(), trueBlock);
			else
				trueOrFalseBefore = nodeFactory.newIfNode(
						condition.getSource(), condition.copy(), trueBlock,
						falseBlock);
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
				trueOrFalseAfter = nodeFactory.newIfNode(condition.getSource(),
						nodeFactory.newOperatorNode(condition.getSource(),
								Operator.NOT, Arrays.asList(condition.copy())),
						falseBlock);
			else if (falseBlock == null)
				trueOrFalseAfter = nodeFactory.newIfNode(condition.getSource(),
						condition.copy(), trueBlock);
			else
				trueOrFalseAfter = nodeFactory.newIfNode(condition.getSource(),
						condition.copy(), trueBlock, falseBlock);
			after.add(trueOrFalseAfter);
		}
		return new SideEffectFreeTriple(before, sideEffectFreeExpression, after);
	}

	private SideEffectFreeTriple processOperatorExpression(
			OperatorNode expression, boolean storeResult) {
		SideEffectFreeTriple result;

		switch (expression.getOperator()) {
		case ASSIGN:
			result = processAssignment(expression, storeResult);
			break;
		case ADDRESSOF:
		case DEREFERENCE:
		case NOT:
		case UNARYMINUS:
		case UNARYPLUS:
			result = processUnaryExpression(expression);
			break;
		case PREINCREMENT:
		case PREDECREMENT:
		case POSTINCREMENT:
		case POSTDECREMENT:
			result = processIncrementOrDecrement(expression);
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
			result = processGenericBinaryOperator(expression);
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
			result = processAssignmentOperator(expression);
			break;
		}
		case COMMA:
			result = processComma(expression);
			break;
		case CONDITIONAL: {
			result = processConditional(expression);
			break;
		}
		default:// BIG_O, CONDITIONAL,
			throw new ABCUnsupportedException(
					"removing side effects from an expression with the operator "
							+ expression.getOperator() + ": " + expression,
					expression.getSource().getSummary(false));
		}
		return result;
	}

	private SideEffectFreeTriple processExpression(ExpressionNode expression,
			boolean storeResult) {
		SideEffectFreeTriple result;

		if (expression instanceof OperatorNode) {
			result = processOperatorExpression((OperatorNode) expression,
					storeResult);
		} else if (expression instanceof FunctionCallNode) {
			result = processFunctionCall((FunctionCallNode) expression,
					storeResult);
		} else if (expression instanceof SpawnNode) {
			result = processSpawn((SpawnNode) expression, storeResult);
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
	 * Remove side effects from an expression, and do store the resulting value
	 * in a temporary variable for assignments, functions calls, or spawns.
	 * 
	 * @param expression
	 *            an expression node
	 * @return a side-effect-free triple equivalent to the original expression
	 * @throws SyntaxException
	 *             if a syntax error is discovered in the process
	 */
	private SideEffectFreeTriple processExpression(ExpressionNode expression) {
		return processExpression(expression, true);
	}

	private StatementNode expressionStatement(ExpressionStatementNode statement)
			throws SyntaxException {
		StatementNode result;
		ExpressionNode expression = statement.getExpression();

		if (expression == null)
			return statement;
		statement.remove();
		if (expression.isSideEffectFree(false)) {
			result = statement;
		} else if (expression instanceof FunctionCallNode) {
			SideEffectFreeTriple triple = processFunctionCall(
					(FunctionCallNode) expression, false);

			if (triple.isTrivial()) {
				result = statement;
			} else {
				List<BlockItemNode> blockItems = new LinkedList<>();

				blockItems.addAll(triple.getBefore());
				blockItems.add(nodeFactory.newExpressionStatementNode(triple
						.getExpression()));
				blockItems.addAll(triple.getAfter());
				result = nodeFactory.newCompoundStatementNode(
						statement.getSource(), blockItems);
			}
		} else if (expression instanceof SpawnNode) {
			SideEffectFreeTriple triple = processSpawn((SpawnNode) expression,
					false);

			if (triple.isTrivial()) {
				result = statement;
			} else {
				List<BlockItemNode> blockItems = new LinkedList<>();

				blockItems.addAll(triple.getBefore());
				blockItems.add(nodeFactory.newExpressionStatementNode(triple
						.getExpression()));
				blockItems.addAll(triple.getAfter());
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
				subtract = nodeFactory.newOperatorNode(expression.getSource(),
						Operator.MINUS, subtractArguments);
				decrementArguments.add(((OperatorNode) expression).getArgument(
						0).copy());
				decrementArguments.add(subtract);
				result = nodeFactory.newExpressionStatementNode(nodeFactory
						.newOperatorNode(expression.getSource(),
								Operator.ASSIGN, decrementArguments));
				break;
			case ASSIGN: {
				SideEffectFreeTriple triple = processAssignment(
						(OperatorNode) expression, false);

				if (triple.isTrivial()) {
					result = statement;
				} else {
					List<BlockItemNode> blockItems = new LinkedList<>();

					blockItems.addAll(triple.getBefore());
					blockItems
							.add(nodeFactory.newExpressionStatementNode(triple
									.getExpression()));
					blockItems.addAll(triple.getAfter());
					result = nodeFactory.newCompoundStatementNode(
							statement.getSource(), blockItems);
				}
				break;
			}
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
						leftTriple.getExpression(), rightTriple.getExpression());
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
					commaStatements
							.add(nodeFactory.newExpressionStatementNode(triple
									.getExpression()));
				} else
					commaStatements.add(nodeFactory
							.newExpressionStatementNode(expr.copy()));
				commaStatements.addAll(triple.getAfter());
				result = nodeFactory.newCompoundStatementNode(
						expression.getSource(), commaStatements);
				break;
			default:
				throw new ABCUnsupportedException("this operation: "
						+ statement, statement.getSource().getSummary(false));
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
							+ statement, statement.getSource()
							.getSummary(false));
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

	private StatementNode returnStatement(ReturnNode statement) {
		ExpressionNode arg = statement.getExpression();

		if (arg == null)
			return statement;

		SideEffectFreeTriple triple = processExpression(arg, true);

		if (triple.isTrivial())
			return statement;

		List<BlockItemNode> newStatements = new LinkedList<>();
		Source source = statement.getSource();

		newStatements.addAll(triple.getBefore());
		newStatements.addAll(triple.getAfter());
		newStatements.add(nodeFactory.newReturnNode(source,
				triple.getExpression()));
		return nodeFactory.newCompoundStatementNode(source, newStatements);
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

	private SideEffectFreeTriple processIncrementOrDecrement(
			OperatorNode operator) {
		List<BlockItemNode> before = new ArrayList<>();
		List<BlockItemNode> after = new ArrayList<>();
		ExpressionNode base = operator.getArgument(0).copy();

		OperatorNode.Operator operation = operator.getOperator();
		ExpressionNode math, assignment;
		List<ExpressionNode> assignArguments = new ArrayList<>();
		List<ExpressionNode> mathArguments = new ArrayList<>();

		// base.parent().removeChild(base.childIndex());
		mathArguments.add(base.copy());
		try {
			mathArguments.add(nodeFactory.newIntegerConstantNode(
					operator.getSource(), "1"));
		} catch (SyntaxException e) {
			throw new ABCRuntimeException(
					"unreachable: no problem creating integer constant node for 1: "
							+ e);
		}
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

	private void removeSideEffects(FunctionDefinitionNode function)
			throws SyntaxException {
		function.setBody(processCompoundStatement(function.getBody()));
	}

	/* Public Methods */

	@Override
	public AST transform(AST ast) throws SyntaxException {
		SequenceNode<ExternalDefinitionNode> rootNode = ast.getRootNode();

		assert this.astFactory == ast.getASTFactory();
		assert this.nodeFactory == astFactory.getNodeFactory();
		ast.release();
		for (int i = 0; i < rootNode.numChildren(); i++) {
			ASTNode node = rootNode.child(i);

			// For now, assume initializers for global variables are side effect
			// free (SEF).
			if (node instanceof VariableDeclarationNode) {

			} else if (node instanceof FunctionDefinitionNode) {
				removeSideEffects((FunctionDefinitionNode) node);
			}
		}
		return astFactory.newAST(rootNode, ast.getSourceFiles());
	}

}
