package edu.udel.cis.vsl.abc.transform.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.ArrayDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.FieldDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ArrowNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DotNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode.ExpressionKind;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.GenericSelectionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RegularRangeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RemoteExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ScopeOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode.BlockItemKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.DeclarationListNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
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

//import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;

/**
 * <p>
 * A transformer which modifies an AST so that no expressions other than a very
 * few prescribed ones have side effects.
 * </p>
 * 
 * <p>
 * An expression is <strong>side-effect-free</strong> if it does not contain a
 * function call or any subexpression which can modify the state (such as an
 * assignment).
 * </p>
 * 
 * <p>
 * A expression e is in <code>normal form</code> if it has one of the following
 * forms:
 * <ul>
 * 
 * <li>e is an assignment expression e1=e2, for side-effect-free expressions e1
 * and e2, and e1 is a lhs expression</li>
 * 
 * <li>e is of the form e1=f(arg0,...), for side-effect-free expressions e1, f,
 * arg0, .... (function call with left-hand-side)</li>
 * 
 * <li>e is of the form f(arg0,...), for side-effect-free expressions f, arg0,
 * ... (function call with no left-hand-side)</li>
 * 
 * <li>ditto last two with $spawn inserted before the call</li>
 * </ul>
 * </p>
 * 
 * <p>
 * A statement is in normal form if: - if it is an expression statement wrapping
 * e, then e is in normal form - for other kinds of statements: all its member
 * expressions are side-effect-free and all its member statements are in normal
 * form.
 * </p>
 * 
 * @author Timothy K. Zirkel
 * @author Stephen F. Siegel
 */
public class NewSideEffectRemover extends BaseTransformer {

	/* Static Fields */

	public final static String CODE = "sef";

	public final static String LONG_NAME = "SideEffectRemover";

	public final static String SHORT_DESCRIPTION = "transforms program to side-effect-free form";

	private final static String tempVariablePrefix = "$" + CODE + "$";

	/* Instance Fields */

	private int tempVariableCounter = 0;

	/* Constructors */

	public NewSideEffectRemover(ASTFactory astFactory) {
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
	 * Modifies the triple by introducing a new temporary variable t whose type
	 * is same as the type of the expression, appending a declaration for t to
	 * the before clause, moving all the after clauses to the end of the before
	 * clause, and replacing the expression with t. The result is a triple with
	 * a side-effect-free expression and an empty after clause.
	 * 
	 * @param triple
	 *            any triple
	 */
	private void shift(ExprTriple triple) {
		ExpressionNode expression = triple.getNode();
		Source source = expression.getSource();
		String tmpId = tempVariablePrefix + tempVariableCounter;

		tempVariableCounter++;
		expression.remove();

		VariableDeclarationNode decl = nodeFactory.newVariableDeclarationNode(
				source, nodeFactory.newIdentifierNode(source, tmpId),
				typeNode(source, expression.getType()), expression);

		triple.getBefore().add(decl);
		triple.getBefore().addAll(triple.getAfter());
		triple.setNode(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, tmpId)));
		triple.setAfter(new LinkedList<BlockItemNode>());
	}

	/**
	 * Makes the triple after component empty. If the after component is already
	 * empty, does nothing and returns <code>false</code>. Otherwise, invokes
	 * {@link #shift(ExprTriple)} and returns <code>true</code>.
	 * 
	 * @param triple
	 *            any triple
	 * @return <code>true</code> iff the triple changed
	 */
	private boolean emptyAfter(ExprTriple triple) {
		if (triple.getAfter().isEmpty()) {
			return false;
		} else {
			shift(triple);
			return true;
		}
	}

	/**
	 * Makes the triple expression side-effect-free and the "after" clauses
	 * empty. If the triple already satisfies those properties, this does
	 * nothing and return <code>false</code>. Otherwise, it performs a
	 * {@link #shift(ExprTriple)} and returns <code>true</code>.
	 * 
	 * @param triple
	 *            any triple
	 * @return <code>true</code> iff the triple changed
	 */
	private boolean purify(ExprTriple triple) {
		if (triple.getAfter().isEmpty()
				&& triple.getNode().isSideEffectFree(false)) {
			return false;
		} else {
			shift(triple);
			return true;
		}
	}

	/**
	 * Makes the triple expression side-effect-free. Transforms the triple into
	 * an equivalent form in which the expression is side-effect-free. If the
	 * expression is already side-effect-free, this does nothing and returns
	 * false. Otherwise, it applies {@link #shift(ExprTriple)} and returns true.
	 * 
	 * @param triple
	 *            any triple
	 * @return <code>true</code> iff the triple changed
	 */
	private boolean makesef(ExprTriple triple) {
		if (triple.getNode().isSideEffectFree(false)) {
			return false;
		} else {
			shift(triple);
			return true;
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

	private ExprTriple lhsTranslate(ExpressionNode lhs) {
		ExpressionKind kind = lhs.expressionKind();

		switch (kind) {
		case ARROW: {
			// p->f = (*p).f
			ArrowNode arrow = (ArrowNode) lhs;
			ExprTriple result = translate(arrow.getStructurePointer());

			purify(result);
			arrow.setStructurePointer(result.getNode());
			result.setNode(arrow);
			return result;
		}
		case DOT: {
			// e.f
			DotNode dotNode = (DotNode) lhs;
			ExprTriple result = translate(dotNode.getStructure());

			purify(result);
			dotNode.setStructure(result.getNode());
			result.setNode(dotNode);
			return result;
		}
		case IDENTIFIER_EXPRESSION:
			return new ExprTriple(lhs);
		case OPERATOR: {
			OperatorNode opNode = (OperatorNode) lhs;
			Operator op = opNode.getOperator();

			switch (op) {
			case DEREFERENCE: { // *p
				ExprTriple result = translate(opNode.getArgument(0));

				purify(result);
				opNode.setArgument(0, result.getNode());
				result.setNode(opNode);
				return result;
			}
			case SUBSCRIPT: {
				// expr[i].
				// expr can be a LHSExpression of array type (like a[j][k])
				// expr can be an expression of pointer type
				ExprTriple t1 = translate(opNode.getArgument(0)), t2 = translate(opNode
						.getArgument(1));

				purify(t1);
				purify(t2);
				opNode.setArgument(0, t1.getNode());
				opNode.setArgument(1, t2.getNode());
				t1.addAllBefore(t2.getBefore());
				t1.setNode(opNode);
				return t1;
			}
			default:
				throw new ABCRuntimeException(
						"Unreachable: unknown LHS operator: " + op);
			}
		}
		default:
			throw new ABCRuntimeException(
					"Unreachable: unknown LHS expression kind: " + kind);
		}
	}

	private IntegerConstantNode newOneNode(Source source) {
		try {
			return nodeFactory.newIntegerConstantNode(source, "1");
		} catch (SyntaxException e) {
			throw new ABCRuntimeException("unreachable");
		}
	}

	/**
	 * <pre>
	 * lhs++:
	 * Let lhstranslate(lhs)=[b|e|].
	 * translate(lhs++)=[b|e|e=e+1]
	 * 
	 * ++lhs:
	 * Let lhstranslate(lhs)=[b|e|].
	 * translate(++lhs)=[b,e=e+1|e|]
	 * </pre>
	 * 
	 * @param opNode
	 * @return
	 */
	private ExprTriple translateIncrementOrDecrement(OperatorNode opNode) {
		Source source = opNode.getSource();
		Operator op = opNode.getOperator();
		Operator unaryOp;
		boolean pre;

		switch (op) {
		case PREINCREMENT:
			unaryOp = Operator.PLUS;
			pre = true;
			break;
		case POSTINCREMENT:
			unaryOp = Operator.PLUS;
			pre = false;
			break;
		case PREDECREMENT:
			unaryOp = Operator.MINUS;
			pre = true;
			break;
		case POSTDECREMENT:
			unaryOp = Operator.MINUS;
			pre = false;
			break;
		default:
			throw new ABCRuntimeException("Unreachable: unexpected operator: "
					+ op);
		}

		ExpressionNode arg = opNode.getArgument(0);
		ExprTriple result = lhsTranslate(arg);
		ExpressionNode newArg = result.getNode();
		StatementNode assignment = nodeFactory
				.newExpressionStatementNode(nodeFactory.newOperatorNode(
						source,
						Operator.ASSIGN,
						newArg.copy(),
						nodeFactory.newOperatorNode(source, unaryOp,
								newArg.copy(), newOneNode(source))));

		if (pre)
			result.addBefore(assignment);
		else
			result.addAfter(assignment);
		return result;
	}

	/**
	 * <p>
	 * C11 6.15.16: "The side effect of updating the stored value of the left
	 * operand is sequenced after the value computations of the left and right
	 * operands. The evaluations of the operands are unsequenced."
	 * </p>
	 *
	 * <pre>
	 * lhs=rhs:
	 * Let lhstranslate(lhs)=[b1|e1|], emptyAfter(translate(rhs))=[b2|e2|].
	 * translate(lhs=rhs) = [b1,b2,e1=e2|e1|]
	 * </pre>
	 * 
	 * @param assign
	 *            an assignment node (operator node for which the operator is
	 *            {@link Operator.ASSIGN}
	 * @return SEFTriple equivalent to the given expression
	 */
	private ExprTriple translateAssign(OperatorNode assign) {
		assert assign.getOperator() == Operator.ASSIGN;

		ExpressionNode lhs = assign.getArgument(0);
		ExpressionNode rhs = assign.getArgument(1);
		ExprTriple leftTriple = lhsTranslate(lhs);
		ExprTriple rightTriple = translate(rhs);

		emptyAfter(rightTriple);

		ExpressionNode newLhs = leftTriple.getNode();
		ExpressionNode newRhs = rightTriple.getNode();

		assign.setArgument(0, newLhs);
		assign.setArgument(1, newRhs);

		ExprTriple result = new ExprTriple(newLhs.copy());

		result.addAllBefore(leftTriple.getBefore());
		result.addAllBefore(rightTriple.getBefore());
		result.addBefore(nodeFactory.newExpressionStatementNode(assign));
		return result;
	}

	/**
	 * <pre>
	 * Pointer dereference *(expr):
	 * Let purify(translate(expr))=[b|e|].
	 * translate(*(expr))=[b|*e|].
	 * </pre>
	 * 
	 * @param dereference
	 * @return
	 */
	private ExprTriple translateDereference(OperatorNode dereference) {
		Operator operator = dereference.getOperator();
		ExprTriple result = translate(dereference.getArgument(0));

		assert operator == Operator.DEREFERENCE;
		makesef(result);
		dereference.setArgument(0, result.getNode());
		result.setNode(dereference);
		return result;
	}

	/**
	 * <pre>
	 * expr1+expr2:
	 * Let makesef(translate(expr1))=[b1|e1|a1],
	 * makesef(translate(expr2))=[b2|e2|a2].
	 * translate(expr1+expr2)=[b1,b2|e1+e2|a1,a2].
	 * Replace + with any side-effect-free binary operator.
	 * </pre>
	 * 
	 * @param opNode
	 * @return
	 */
	private ExprTriple translateGenericBinaryOperator(OperatorNode opNode) {
		ExprTriple leftTriple = translate(opNode.getArgument(0));
		ExprTriple rightTriple = translate(opNode.getArgument(1));

		makesef(leftTriple);
		makesef(rightTriple);
		opNode.setArgument(0, leftTriple.getNode());
		opNode.setArgument(1, rightTriple.getNode());
		leftTriple.addAllBefore(rightTriple.getBefore());
		leftTriple.addAllAfter(rightTriple.getAfter());
		leftTriple.setNode(opNode);
		return leftTriple;
	}

	/**
	 * <pre>
	 * -expr:
	 * Let makesef(translate(expr))=[b1|e1|a1].
	 * translate(-expr)=[b1|-e1|a1].
	 * Replace - with any side-effect-free unary operator.
	 * </pre>
	 * 
	 * @param opNode
	 * @return
	 */
	private ExprTriple translateGenericUnaryOperator(OperatorNode opNode) {
		ExprTriple result = translate(opNode.getArgument(0));

		makesef(result);
		opNode.setArgument(0, result.getNode());
		result.setNode(opNode);
		return result;
	}

	/**
	 * <p>
	 * C11 6.5.2.2: "There is a sequence point after the evaluations of the
	 * function designator and the actual arguments but before the actual call.
	 * Every evaluation in the calling function (including other function calls)
	 * that is not otherwise specifically sequenced before or after the
	 * execution of the body of the called function is indeterminately sequenced
	 * with respect to the execution of the called function."
	 * </p>
	 * 
	 * <p>
	 * As stated above, all side-effects must complete before the function call
	 * occurs. Hence all side-effects will take place in the "before" component
	 * of the returned triple, and the "after" component will be empty.
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
	 * <pre>
	 * func(arg1, arg2, ...):
	 * Let purify(func)=[b0|f|].
	 * Let purify(arg1)=[b1|e1|], ...
	 * translate(func(arg1, ...)) = [b1,b2,...|f(e1,e2,...)|].
	 * </pre>
	 */
	private ExprTriple translateFunctionCall(FunctionCallNode callNode) {
		ExprTriple functionTriple = translate(callNode.getFunction());
		int numContextArgs = callNode.getNumberOfContextArguments();
		int numArgs = callNode.getNumberOfArguments();
		ExprTriple result = new ExprTriple(callNode);

		purify(functionTriple);
		callNode.setFunction(functionTriple.getNode());
		result.addAllBefore(functionTriple.getBefore());
		for (int i = 0; i < numContextArgs; i++) {
			ExprTriple triple = translate(callNode.getContextArgument(i));

			purify(triple);
			result.addAllBefore(triple.getBefore());
			callNode.setContextArgument(i, triple.getNode());
		}
		for (int i = 0; i < numArgs; i++) {
			ExprTriple triple = translate(callNode.getArgument(i));

			purify(triple);
			result.addAllBefore(triple.getBefore());
			callNode.setArgument(i, triple.getNode());
		}
		return result;
	}

	private ExprTriple translateSpawn(SpawnNode spawn) {
		ExprTriple result = translate(spawn.getCall());

		spawn.setCall((FunctionCallNode) result.getNode());
		result.setNode(spawn);
		return result;
	}

	private ExprTriple translateGeneralAssignment(OperatorNode opNode) {
		Operator assignmentOp = opNode.getOperator();
		Operator binaryOp;

		switch (assignmentOp) {
		case PLUSEQ:
			binaryOp = Operator.PLUS;
			break;
		case MINUSEQ:
			binaryOp = Operator.MINUS;
			break;
		case BITANDEQ:
			binaryOp = Operator.BITAND;
			break;
		case BITOREQ:
			binaryOp = Operator.BITOR;
			break;
		case BITXOREQ:
			binaryOp = Operator.BITXOR;
			break;
		case DIVEQ:
			binaryOp = Operator.DIV;
			break;
		case MODEQ:
			binaryOp = Operator.MOD;
			break;
		case SHIFTLEFTEQ:
			binaryOp = Operator.SHIFTLEFT;
			break;
		case SHIFTRIGHTEQ:
			binaryOp = Operator.SHIFTRIGHT;
			break;
		case TIMESEQ:
			binaryOp = Operator.TIMES;
			break;
		default:
			throw new ABCRuntimeException("Unexpected assignment operator: "
					+ assignmentOp);
		}

		ExpressionNode lhs = opNode.getArgument(0);
		ExpressionNode rhs = opNode.getArgument(1);
		ExprTriple result = lhsTranslate(lhs);
		ExprTriple rightTriple = translate(rhs);

		purify(rightTriple);

		ExpressionNode newLhs = result.getNode();
		ExpressionNode newRhs = rightTriple.getNode();
		Source source = opNode.getSource();
		StatementNode assignment = nodeFactory
				.newExpressionStatementNode(nodeFactory.newOperatorNode(
						source,
						Operator.ASSIGN,
						newLhs.copy(),
						nodeFactory.newOperatorNode(source, binaryOp,
								newLhs.copy(), newRhs)));

		result.addAllBefore(rightTriple.getBefore());
		result.addBefore(assignment);
		return result;
	}

	/**
	 * Translates a comma expression into side-effect-free triple form. There is
	 * a sequence point at the comma. So all side effects from the first
	 * argument must complete before the second argument is evaluated.
	 * 
	 * <pre>
	 * expr1,expr2:
	 * let translate(expr1)   = [b1|e1|a1].
	 * let translate(expr2)   = [b2|e2|a2].
	 * translate(expr1,expr2) = [b1,e1^,a1,b2|e2|a2].
	 * Here e1^ means: omit this if e1 is s.e.f., else make it the expression
	 * statement e1;.
	 * </pre>
	 * 
	 * @param expression
	 *            a comma expression
	 * @return result of converting expression to side-effect-free triple
	 */
	private ExprTriple translateComma(OperatorNode expression) {
		ExprTriple leftTriple = translate(expression.getArgument(0));
		ExprTriple rightTriple = translate(expression.getArgument(1));
		ExprTriple result = new ExprTriple(rightTriple.getNode());
		ExpressionNode e1 = leftTriple.getNode();

		result.addAllBefore(leftTriple.getBefore());
		if (e1.isSideEffectFree(true)) {
			// Note that we consider errors as side effects for a comma
			// operation left hand side, because if there are no possible
			// side effects we'll just remove the left hand argument.
			result.addBefore(nodeFactory.newExpressionStatementNode(e1));
		}
		result.addAllBefore(leftTriple.getAfter());
		result.addAllBefore(rightTriple.getBefore());
		result.addAllAfter(rightTriple.getAfter());
		return result;
	}

	/**
	 * Translates a conditional expression <code>x?y:z</code> to a triple. There
	 * is a sequence point at the <code>?</code>.
	 * 
	 * @param conditional
	 *            the conditional expression
	 * @return result of translation
	 */
	private ExprTriple translateConditional(OperatorNode conditional) {
		Source source = conditional.getSource();
		Operator operator = conditional.getOperator();
		ExprTriple condTriple = translate(conditional.getArgument(0));
		ExprTriple triple1 = translate(conditional.getArgument(1));
		ExprTriple triple2 = translate(conditional.getArgument(2));
		ExprTriple result;

		assert operator == Operator.CONDITIONAL;
		purify(condTriple);
		makesef(triple1);
		makesef(triple2);

		List<BlockItemNode> b0 = condTriple.getBefore();
		ExpressionNode e0 = condTriple.getNode();
		List<BlockItemNode> b1 = triple1.getBefore(), b2 = triple2.getBefore();
		List<BlockItemNode> a1 = triple1.getAfter(), a2 = triple2.getAfter();
		ExpressionNode e1 = triple1.getNode(), e2 = triple2.getNode();

		if (b1.isEmpty() && b2.isEmpty() && a1.isEmpty() && a2.isEmpty()) {
			conditional.setChild(0, e0);
			conditional.setChild(1, e1);
			conditional.setChild(2, e2);
			result = new ExprTriple(conditional);
			result.addAllBefore(b0);
		} else {
			String tmpId = tempVariablePrefix + (tempVariableCounter++);
			VariableDeclarationNode decl = nodeFactory
					.newVariableDeclarationNode(source,
							nodeFactory.newIdentifierNode(source, tmpId),
							typeNode(source, conditional.getType()));
			ExpressionNode tmpNode = nodeFactory.newIdentifierExpressionNode(
					source, nodeFactory.newIdentifierNode(source, tmpId));
			StatementNode ifNode;

			{
				CompoundStatementNode stmt1, stmt2;

				{
					List<BlockItemNode> stmtlist = new LinkedList<>(b1);

					stmtlist.add(nodeFactory
							.newExpressionStatementNode(nodeFactory
									.newOperatorNode(source, Operator.ASSIGN,
											tmpNode.copy(), e1)));
					stmtlist.addAll(a1);
					stmt1 = nodeFactory.newCompoundStatementNode(source,
							stmtlist);
				}
				{
					List<BlockItemNode> stmtlist = new LinkedList<>(b2);

					stmtlist.add(nodeFactory
							.newExpressionStatementNode(nodeFactory
									.newOperatorNode(source, Operator.ASSIGN,
											tmpNode.copy(), e2)));
					stmtlist.addAll(a2);
					stmt2 = nodeFactory.newCompoundStatementNode(source,
							stmtlist);
				}
				ifNode = nodeFactory.newIfNode(source, e0, stmt1, stmt2);
			}
			result = new ExprTriple(tmpNode);
			result.addAllBefore(b0);
			result.addBefore(decl);
			result.addBefore(ifNode);
		}
		return result;
	}

	private ExprTriple translateOperatorExpression(OperatorNode expression) {
		ExprTriple result;

		switch (expression.getOperator()) {
		case ASSIGN:
			result = translateAssign(expression);
			break;
		case DEREFERENCE:
			result = translateDereference(expression);
			break;
		case ADDRESSOF:
		case NOT:
		case UNARYMINUS:
		case UNARYPLUS:
		case BIG_O:
			result = translateGenericUnaryOperator(expression);
			break;
		case PREINCREMENT:
		case PREDECREMENT:
		case POSTINCREMENT:
		case POSTDECREMENT:
			result = translateIncrementOrDecrement(expression);
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
			result = translateGenericBinaryOperator(expression);
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
		case SHIFTRIGHTEQ:
			result = translateGeneralAssignment(expression);
			break;
		case COMMA:
			result = translateComma(expression);
			break;
		case CONDITIONAL:
			result = translateConditional(expression);
			break;
		default:
			throw new ABCRuntimeException("Unexpected operator: "
					+ expression.getOperator() + ": " + expression, expression
					.getSource().getSummary(false));
		}
		return result;
	}

	private ExprTriple translateSizeof(SizeofNode expression) {
		SizeableNode arg = expression.getArgument();
		ExprTriple triple;

		if (arg instanceof ExpressionNode) {
			triple = translate((ExpressionNode) arg);
			makesef(triple);
			expression.setArgument(triple.getNode());
			triple.setNode(expression);
		} else if (arg instanceof TypeNode) {
			SETriple typeTriple = translateGenericNode(arg);

			expression.setArgument((TypeNode) typeTriple.getNode());
			triple = new ExprTriple(typeTriple.getBefore(), expression,
					new LinkedList<BlockItemNode>());
		} else
			throw new ABCRuntimeException("Unexpected kind of SizeableNode: "
					+ arg);
		return triple;
	}

	private ExprTriple translateScopeOf(ScopeOfNode expression) {
		ExprTriple result = translate(expression.expression());

		makesef(result);
		expression.setExpression(result.getNode());
		result.setNode(expression);
		return result;
	}

	private ExprTriple translateRemoteReference(RemoteExpressionNode expression) {
		ExprTriple result = translate(expression.getProcessExpression());

		makesef(result);
		expression.setProcessExpression(result.getNode());
		result.setNode(expression);
		return result;
	}

	private ExprTriple translateRegularRange(RegularRangeNode expression) {
		ExprTriple low = translate(expression.getLow()), hi = translate(expression
				.getHigh()), step = translate(expression.getStep());

		makesef(low);
		makesef(hi);
		makesef(step);

		expression.setLow(low.getNode());
		expression.setHigh(hi.getNode());
		expression.setStep(step.getNode());

		ExprTriple result = new ExprTriple(expression);

		result.addAllBefore(low.getBefore());
		result.addAllBefore(hi.getBefore());
		result.addAllBefore(step.getBefore());
		result.addAllAfter(low.getAfter());
		result.addAllAfter(hi.getAfter());
		result.addAllAfter(step.getAfter());
		return result;
	}

	private ExprTriple translateQuantifiedExpression(
			QuantifiedExpressionNode expression) {
		// should never have side-effects: check it in Analyzer
		return new ExprTriple(expression);
	}

	private ExprTriple translateGenericSelection(GenericSelectionNode expression) {
		throw new ABCUnsupportedException(
				"generic selections not yet implemented: " + expression);
	}

	private ExprTriple translateDot(DotNode expression) {
		ExprTriple result = translate(expression.getStructure());

		makesef(result);
		expression.setStructure(result.getNode());
		result.setNode(expression);
		return result;
	}

	/**
	 * Translates an initializer node.
	 * 
	 * @param node
	 *            an initializer node
	 * @param emptyAfter
	 *            must the resulting triple have an empty after clause?
	 * @return result of translation
	 */
	private SETriple translateInitializer(InitializerNode node,
			boolean emptyAfter) {
		if (node instanceof ExpressionNode) {
			ExprTriple triple = translate((ExpressionNode) node);

			emptyAfter(triple);
			return triple;
		} else if (node instanceof CompoundInitializerNode) {
			return translateCompoundInitializer((CompoundInitializerNode) node,
					emptyAfter);
		} else
			throw new ABCRuntimeException(
					"Unexpected kind of initializer node: " + node);
	}

	/**
	 * Translates a compound initializer. There are no sequence points.
	 * 
	 * @param node
	 *            a compound initializer node, possibly containing side-effects
	 * @param emptyAfter
	 *            should the triple returned have an empty after clause?
	 * @return triple corresponding to given node
	 */
	private SETriple translateCompoundInitializer(CompoundInitializerNode node,
			boolean emptyAfter) {
		SETriple result = new SETriple(node);

		for (PairNode<DesignationNode, InitializerNode> pair : node) {
			DesignationNode designationNode = pair.getLeft();

			for (DesignatorNode designator : designationNode) {
				if (designator instanceof FieldDesignatorNode) {
					// no side effects possible
				} else if (designator instanceof ArrayDesignatorNode) {
					ExpressionNode indexNode = ((ArrayDesignatorNode) designator)
							.getIndex();
					ExprTriple triple = translate(indexNode);

					if (emptyAfter) {
						purify(triple);
						result.addAllBefore(triple.getBefore());
					} else {
						makesef(triple);
						result.addAllBefore(triple.getBefore());
						result.addAllAfter(triple.getAfter());
					}
					((ArrayDesignatorNode) designator).setIndex(triple
							.getNode());
				} else {
					throw new ABCRuntimeException(
							"Unexpected kind of designator node: " + designator);
				}
			}

			SETriple initTriple = translateInitializer(pair.getRight(),
					emptyAfter);

			result.addAllBefore(initTriple.getBefore());
			result.addAllAfter(initTriple.getAfter());
			pair.setRight((InitializerNode) initTriple.getNode());
		}
		return result;
	}

	/**
	 * Translates a compound literal. There are no sequence points.
	 * 
	 * @param expression
	 *            a compound literal expression
	 * @return result of translation
	 */
	private ExprTriple translateCompoundLiteral(CompoundLiteralNode expression) {
		CompoundInitializerNode ciNode = expression.getInitializerList();
		SETriple triple = translateCompoundInitializer(ciNode, false);
		ExprTriple result = new ExprTriple(expression);

		expression.setInitializerList((CompoundInitializerNode) triple
				.getNode());
		result.setBefore(triple.getBefore());
		result.setAfter(triple.getAfter());
		return result;
	}

	private ExprTriple translateCollective(CollectiveExpressionNode expression) {
		ExprTriple result = new ExprTriple(expression);
		ExprTriple e0 = translate(expression.getProcessPointerExpression());
		ExprTriple e1 = translate(expression.getLengthExpression());
		ExprTriple e2 = translate(expression.getBody());

		makesef(e0);
		makesef(e1);
		makesef(e2);
		expression.setProcessPointerExpression(e0.getNode());
		expression.setLengthExpression(e1.getNode());
		expression.setBody(e2.getNode());
		result.addAllBefore(e0.getBefore());
		result.addAllBefore(e1.getBefore());
		result.addAllBefore(e2.getBefore());
		result.addAllAfter(e0.getAfter());
		result.addAllAfter(e1.getAfter());
		result.addAllAfter(e2.getAfter());
		return result;
	}

	private ExprTriple translateCast(CastNode expression) {
		// mallocs need to keep their casts, i.e., no
		// tmp=malloc | (int*)tmp | ...
		ExpressionNode arg = expression.getArgument();
		ExprTriple triple = translate(arg);
		ExpressionNode newArg = triple.getNode();

		// if arg started off as a function call, will newArg
		// still be a function call? Yes! See translateFunctionCall.
		if (isMallocCall(newArg)) {
			expression.setArgument(newArg);
		} else {
			makesef(triple);
			expression.setArgument(newArg);
		}
		triple.setNode(expression);
		return triple;
	}

	private ExprTriple translateArrow(ArrowNode expression) {
		ExprTriple result = translate(expression.getStructurePointer());

		makesef(result);
		expression.setStructurePointer(result.getNode());
		result.setNode(expression);
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
	private ExprTriple translate(ExpressionNode expression) {
		ExpressionKind kind = expression.expressionKind();

		switch (kind) {
		case ALIGNOF:
			return new ExprTriple(expression);
		case ARROW:
			return translateArrow((ArrowNode) expression);
		case CAST:
			return translateCast((CastNode) expression);
		case COLLECTIVE:
			return translateCollective((CollectiveExpressionNode) expression);
		case COMPOUND_LITERAL:
			return translateCompoundLiteral((CompoundLiteralNode) expression);
		case CONSTANT:
			return new ExprTriple(expression);
		case DERIVATIVE_EXPRESSION:
			return new ExprTriple(expression);
		case DOT:
			return translateDot((DotNode) expression);
		case FUNCTION_CALL:
			return translateFunctionCall((FunctionCallNode) expression);
		case GENERIC_SELECTION:
			return translateGenericSelection((GenericSelectionNode) expression);
		case IDENTIFIER_EXPRESSION:
			return new ExprTriple(expression);
		case OPERATOR:
			return translateOperatorExpression((OperatorNode) expression);
		case QUANTIFIED_EXPRESSION:
			return translateQuantifiedExpression((QuantifiedExpressionNode) expression);
		case REGULAR_RANGE:
			return translateRegularRange((RegularRangeNode) expression);
		case REMOTE_REFERENCE:
			return translateRemoteReference((RemoteExpressionNode) expression);
		case RESULT:
			return new ExprTriple(expression);
		case SCOPEOF:
			return translateScopeOf((ScopeOfNode) expression);
		case SIZEOF:
			return translateSizeof((SizeofNode) expression);
		case SPAWN:
			return translateSpawn((SpawnNode) expression);
		}
		throw new ABCRuntimeException("Unexpected kind of expression (" + kind
				+ ") in " + expression);
	}

	// Declarations...

	/**
	 * <p>
	 * Translates any AST node into a pure side-effect-free triple. Pure means
	 * the after clause will be empty and all expressions occurring within the
	 * resulting node will be side-effect-free. The kind of node returned in the
	 * triple will be the same kind given: e.g., if node is an instance of
	 * {@link DeclarationNode}, then the node component of the triple returned
	 * will also be an instance of {@link DeclarationNode}.
	 * </p>
	 * 
	 * <p>
	 * Specifically, what this method does: it explores the tree rooted at the
	 * given node in DFS order. Whenever it encounters an expression (so an
	 * expression that is not a sub-expression of another expression) it
	 * translates and purifies that expression. The before side-effects from the
	 * expression are appended to the before clause for the final result. The
	 * (sef) node component of the result replaces the original expression.
	 * </p>
	 * 
	 * @param node
	 *            any ASTNode
	 * @return a pure side-effect-free triple resulting from the translation of
	 *         the node
	 */
	private SETriple translateGenericNode(ASTNode node) {
		if (node instanceof ExpressionNode) {
			ExprTriple result = translate((ExpressionNode) node);

			purify(result);
			return result;
		} else {
			int numChildren = node.numChildren();
			SETriple result = new SETriple(node);

			for (int i = 0; i < numChildren; i++) {
				SETriple childTriple = translateGenericNode(node.child(i));

				result.addAllBefore(childTriple.getBefore());
				node.setChild(i, childTriple.getNode());
			}
			return result;
		}
	}

	/**
	 * Returns a triple in which the after clause is empty and the node is the
	 * variable declaration node, because we want the side-effects to complete
	 * before the initialization takes place.
	 * 
	 * @param decl
	 *            a variable declaration
	 * @return equivalent triple with empty after
	 */
	private List<BlockItemNode> normalizeVariableDeclaration(
			VariableDeclarationNode decl) {
		TypeNode typeNode = decl.getTypeNode();
		InitializerNode initNode = decl.getInitializer();
		SETriple typeTriple = translateGenericNode(typeNode);
		List<BlockItemNode> result = new LinkedList<>();

		result.addAll(typeTriple.getBefore());
		decl.setTypeNode((TypeNode) typeTriple.getNode());
		if (initNode != null) {
			SETriple initTriple;

			if (initNode instanceof ExpressionNode) {
				initTriple = translate((ExpressionNode) initNode);
				emptyAfter((ExprTriple) initTriple);
			} else {
				initTriple = translateCompoundInitializer(
						(CompoundInitializerNode) initNode, true);
				// true, since need side-effects to complete before
				// initialization happens
			}
			result.addAll(initTriple.getBefore());
			decl.setInitializer((InitializerNode) initTriple.getNode());
		}
		return result;
	}

	// statements

	private List<BlockItemNode> normalizeExpressionAsStatement(
			ExpressionNode expr) {
		ExprTriple triple = translate(expr);
		List<BlockItemNode> result;

		// expr part of triple may contain function call/spawn
		makesef(triple);
		result = triple.getBefore();
		result.addAll(triple.getAfter());
		return result;
	}

	private List<BlockItemNode> normalizeExpressionStatement(
			ExpressionStatementNode exprStmt) {
		return normalizeExpressionAsStatement(exprStmt.getExpression());
	}

	private CompoundStatementNode makeCompound(StatementNode stmt) {
		if (stmt instanceof CompoundStatementNode)
			return (CompoundStatementNode) stmt;
		else
			return nodeFactory.newCompoundStatementNode(stmt.getSource(),
					Arrays.asList((BlockItemNode) stmt));
	}

	private void normalizeLoopBody(LoopNode loop) {
		StatementNode body = loop.getBody();
		List<BlockItemNode> bodyList = normalizeStatement(body);

		if (bodyList.size() == 1)
			loop.setBody((StatementNode) bodyList.get(0));
		else
			loop.setBody(nodeFactory.newCompoundStatementNode(body.getSource(),
					bodyList));
	}

	/**
	 * Normalizes the initializer node of for loop by placing it in normal form
	 * and moving before the for loop if necessary. This may modify the for
	 * loop.
	 * 
	 * @param forLoop
	 *            a for loop node
	 * @return the sequence of statements to insert before the for loop
	 *         (possibly empty)
	 */
	private List<BlockItemNode> normalizeForLoopInitializer(ForLoopNode forLoop) {
		ForLoopInitializerNode init = forLoop.getInitializer();

		if (init instanceof ExpressionNode) {
			List<BlockItemNode> initItems = normalizeExpressionAsStatement((ExpressionNode) init);

			// if initItems consists of one expression statement, keep it in for
			if (initItems.size() == 1) {
				BlockItemNode item = initItems.get(0);

				if (item instanceof ExpressionStatementNode) {
					ExpressionNode expr = ((ExpressionStatementNode) item)
							.getExpression();

					forLoop.setInitializer(expr);
					return new LinkedList<BlockItemNode>();
				}
			}
			forLoop.setInitializer(null);
			return initItems;
		} else if (init instanceof DeclarationListNode) {
			// make all declarations normal. if there are any side
			// effects, move them to an outer scope?
			DeclarationListNode declList = (DeclarationListNode) init;
			int numDecls = declList.numChildren();
			List<BlockItemNode> result = new LinkedList<>();

			for (int i = 0; i < numDecls; i++) {
				VariableDeclarationNode decl = declList.getSequenceChild(i);

				result.addAll(normalizeVariableDeclaration(decl));
			}
			return result;
		} else
			throw new ABCRuntimeException(
					"Unexpected kind of for loop initializer: " + init);
	}

	private void normalizeForLoopIncrementer(ForLoopNode forLoop) {
		// incrementer: if normal statement, leave alone, otherwise:
		// for (...; ...; ;) { ... incrementer }
		ExpressionNode incrementer = forLoop.getIncrementer();
		List<BlockItemNode> incItems = normalizeExpressionAsStatement(incrementer);

		if (incItems.size() == 1
				& incItems.get(0) instanceof ExpressionStatementNode) {
			// nothing to do
		} else {
			CompoundStatementNode body = makeCompound(forLoop.getBody());

			forLoop.setBody(body);
			body.insertChildren(body.numChildren(), incItems);
			forLoop.setIncrementer(null);
		}
	}

	private void normalizeLoopCondition(LoopNode loop) {
		// cond: purify. if before is non-trivial then transform to
		// while (1) { befores; if (!expr) break; body}
		ExpressionNode cond = loop.getCondition();
		ExprTriple condTriple = translate(cond);

		purify(condTriple);

		List<BlockItemNode> condItems = condTriple.getBefore();

		if (!condItems.isEmpty()) {
			Source condSource = cond.getSource();
			CompoundStatementNode body = makeCompound(loop.getBody());

			loop.setBody(body);
			condItems.add(nodeFactory.newIfNode(condSource, nodeFactory
					.newOperatorNode(condSource, Operator.NOT,
							condTriple.getNode()), nodeFactory
					.newBreakNode(condSource)));
			body.insertChildren(0, condItems);
			loop.setCondition(newOneNode(condSource));
		}
	}

	private List<BlockItemNode> normalizeForLoop(ForLoopNode forLoop) {
		normalizeLoopBody(forLoop);

		List<BlockItemNode> result = normalizeForLoopInitializer(forLoop);

		result.add(forLoop);
		normalizeLoopCondition(forLoop);
		normalizeForLoopIncrementer(forLoop);
		return result;
	}

	private List<BlockItemNode> normalizeWhileLoop(LoopNode whileLoop) {
		normalizeLoopBody(whileLoop);
		normalizeLoopCondition(whileLoop);

		List<BlockItemNode> result = new LinkedList<>();

		result.add(whileLoop);
		return result;
	}

	private List<BlockItemNode> normalizeDoLoop(LoopNode doLoop) {
		normalizeLoopBody(doLoop);

		// do {... befores} while (e);
		ExprTriple condTriple = translate(doLoop.getCondition());

		purify(condTriple);

		List<BlockItemNode> condItems = condTriple.getBefore();

		if (condItems.isEmpty()) {
			// nothing to do
		} else {
			CompoundStatementNode body = makeCompound(doLoop.getBody());

			body.insertChildren(body.numChildren(), condItems);
			doLoop.setCondition(condTriple.getNode());
		}
		return Arrays.asList((BlockItemNode) doLoop);
	}

	private List<BlockItemNode> normalizeLoop(LoopNode loop) {
		switch (loop.getKind()) {
		case DO_WHILE:
			return normalizeDoLoop(loop);
		case FOR:
			return normalizeForLoop((ForLoopNode) loop);
		case WHILE:
			return normalizeWhileLoop(loop);
		default:
			throw new ABCRuntimeException("Unknown kind of loop: "
					+ loop.getKind());
		}
	}

	private List<BlockItemNode> normalizeAssert(AssertNode statement) {
		ExprTriple triple = translate(statement.getCondition());

		purify(triple);

		List<BlockItemNode> result = triple.getBefore();

		statement.setCondition(triple.getNode());
		result.add(statement);
		return result;
	}

	private List<BlockItemNode> normalizeAssume(AssumeNode statement) {
		ExprTriple triple = translate(statement.getExpression());

		purify(triple);

		List<BlockItemNode> result = triple.getBefore();

		statement.setExpression(triple.getNode());
		result.add(statement);
		return result;
	}

	private List<BlockItemNode> normalizeAtomic(AtomicNode statement) {
		StatementNode body = statement.getBody();
		List<BlockItemNode> bodyItems = normalizeStatement(body);
		List<BlockItemNode> result = new LinkedList<>();

		result.add(statement);
		if (bodyItems.size() == 1) {
			BlockItemNode item = bodyItems.get(0);

			if (item instanceof StatementNode) {
				statement.setBody((StatementNode) item);
				return result;
			}
		}
		statement.setBody(nodeFactory.newCompoundStatementNode(
				body.getSource(), bodyItems));
		return result;
	}

	private List<BlockItemNode> normalizeBlockItem(BlockItemNode item) {
		BlockItemKind kind = item.blockItemKind();

		// TODO
		switch (kind) {
		case ENUMERATOR:
			break;
		case ORDINARY_DECLARATION:
			break;
		case PRAGMA:
			break;
		case SCOPED_DECLARATION:
			break;
		case STATEMENT:
			break;
		case STATIC_ASSERTION:
			break;
		case STRUCT_OR_UNION:
			break;
		case TYPEDEF:
			break;
		default:
			break;

		}

		return null;
	}

	private List<BlockItemNode> normalizeCompound(CompoundStatementNode compound) {
		List<BlockItemNode> result = new LinkedList<>();

		for (BlockItemNode item : compound) {
			result.addAll(normalizeBlockItem(item));
		}
		return result;
	}

	private CompoundStatementNode removeSideEffectsCompound(
			CompoundStatementNode compound) {
		List<BlockItemNode> items = normalizeCompound(compound);

		// TODO
		return null;

	}

	// TODO: continue here for all statement

	List<BlockItemNode> normalizeStatement(StatementNode statement) {
		switch (statement.statementKind()) {
		case ASSERT:
			return normalizeAssert((AssertNode) statement);
		case ASSUME:
			return normalizeAssume((AssumeNode) statement);
		case ATOMIC:
			return normalizeAtomic((AtomicNode) statement);
		case CHOOSE:
			break;
		case CIVL_FOR:
			break;
		case COMPOUND:
			break;
		case EXPRESSION:
			return normalizeExpressionStatement((ExpressionStatementNode) statement);
		case IF:
			break;
		case JUMP:
			break;
		case LABELED:
			break;
		case LOOP:
			return normalizeLoop((LoopNode) statement);
		case NULL:
			break;
		case OMP_STATEMENT:
			break;
		case PRAGMA:
			break;
		case SWITCH:
			break;
		case WHEN:
			break;
		default:
			break;

		}
		return null;
	}

	// function definitions...

	private void removeSideEffects(FunctionDefinitionNode function)
			throws SyntaxException {
		function.setBody(removeSideEffectsCompound(function.getBody()));
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
