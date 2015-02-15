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
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.AlignOfNode;
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
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
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
		Source source = lhs.getSource();
		ExprTriple result;

		switch (kind) {
		case ARROW: {
			// p->f = (*p).f
			ArrowNode arrow = (ArrowNode) lhs;
			ExpressionNode arg = arrow.getStructurePointer();

			result = translate(arg);
			if (purify(result)) { // it changed
				result.setNode(nodeFactory.newArrowNode(source,
						result.getNode(), arrow.getFieldName()));
			} else {
				result = new ExprTriple(lhs);
			}
			break;
		}
		case DOT: {
			// e.f
			DotNode dotNode = (DotNode) lhs;
			ExpressionNode arg = dotNode.getStructure();

			result = translate(arg);
			if (purify(result)) { // it changed
				result.setNode(nodeFactory.newDotNode(source, result.getNode(),
						dotNode.getFieldName()));
			} else {
				result = new ExprTriple(lhs);
			}
			break;
		}
		case IDENTIFIER_EXPRESSION:
			result = new ExprTriple(lhs);
			break;
		case OPERATOR: {
			OperatorNode opNode = (OperatorNode) lhs;
			Operator op = opNode.getOperator();
			switch (op) {
			case DEREFERENCE: {
				// *p
				ExpressionNode arg = opNode.getArgument(0);

				result = translate(arg);
				if (purify(result)) { // it changed
					result.setNode(nodeFactory.newOperatorNode(source, op,
							Arrays.asList(result.getNode())));
				} else {
					result = new ExprTriple(lhs);
				}
				break;
			}
			case SUBSCRIPT: {
				// expr[i].
				// expr can be a LHSExpression of array type (like a[j][k])
				// expr can be an expression of pointer type
				ExpressionNode left = opNode.getArgument(0);
				ExpressionNode right = opNode.getArgument(1);
				ExprTriple t1 = translate(left), t2 = translate(right);

				if (purify(t1) || purify(t2)) {
					t1.getBefore().addAll(t2.getBefore());
					t1.setNode(nodeFactory.newOperatorNode(source, op,
							Arrays.asList(t1.getNode(), t2.getNode())));
					result = t1;
				} else {
					result = new ExprTriple(lhs);
				}
				break;
			}
			default:
				throw new ABCRuntimeException(
						"Unreachable: unknown LHS operator: " + op);
			}
			break;
		}
		default:
			throw new ABCRuntimeException(
					"Unreachable: unknown LHS expression kind: " + kind);
		}
		return result;
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
				.newExpressionStatementNode(nodeFactory.newOperatorNode(source,
						Operator.ASSIGN, Arrays.asList(newArg.copy(),
								nodeFactory.newOperatorNode(source, unaryOp,
										Arrays.asList(newArg.copy(),
												newOneNode(source))))));

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
		ExprTriple result = lhsTranslate(lhs);
		ExprTriple rightTriple = translate(rhs);

		emptyAfter(rightTriple);

		ExpressionNode newLhs = result.getNode();
		ExpressionNode newRhs = rightTriple.getNode();
		Source source = assign.getSource();
		StatementNode assignment = nodeFactory
				.newExpressionStatementNode(nodeFactory.newOperatorNode(source,
						Operator.ASSIGN, Arrays.asList(newLhs.copy(), newRhs)));

		result.addAllBefore(rightTriple.getBefore());
		result.addBefore(assignment);
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

		assert operator == Operator.DEREFERENCE;

		Source source = dereference.getSource();
		ExpressionNode expr = dereference.getArgument(0);
		ExprTriple result = translate(expr);

		makesef(result);
		result.setNode(nodeFactory.newOperatorNode(source, operator,
				Arrays.asList(result.getNode())));
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
		Operator operator = opNode.getOperator();
		ExpressionNode left = opNode.getArgument(0);
		ExpressionNode right = opNode.getArgument(1);
		ExprTriple leftTriple = translate(left);
		ExprTriple rightTriple = translate(right);
		Source source = opNode.getSource();

		makesef(leftTriple);
		makesef(rightTriple);
		leftTriple.addAllBefore(rightTriple.getBefore());
		leftTriple.addAllAfter(rightTriple.getAfter());
		leftTriple.setNode(nodeFactory.newOperatorNode(source, operator,
				Arrays.asList(leftTriple.getNode(), rightTriple.getNode())));

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
		Operator operator = opNode.getOperator();
		ExpressionNode arg = opNode.getArgument(0);
		Source source = opNode.getSource();
		ExprTriple result = translate(arg);

		makesef(result);
		result.setNode(nodeFactory.newOperatorNode(source, operator,
				Arrays.asList(result.getNode())));
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
		Source source = callNode.getSource();
		ExprTriple functionTriple = translate(callNode.getFunction());
		int numArgs = callNode.getNumberOfArguments();
		List<ExpressionNode> arguments = new LinkedList<>();
		int numContextArgs = callNode.getNumberOfContextArguments();
		List<ExpressionNode> contextArgs = new LinkedList<>();
		List<BlockItemNode> before = new LinkedList<>();

		purify(functionTriple);
		before.addAll(functionTriple.getBefore());
		for (int i = 0; i < numContextArgs; i++) {
			ExprTriple triple = translate(callNode.getContextArgument(i));

			purify(triple);
			before.addAll(triple.getBefore());
			contextArgs.add(triple.getNode());
		}
		for (int i = 0; i < numArgs; i++) {
			ExprTriple triple = translate(callNode.getArgument(i));

			purify(triple);
			before.addAll(triple.getBefore());
			arguments.add(triple.getNode());
		}

		ExprTriple result = new ExprTriple(
				before,
				nodeFactory.newFunctionCallNode(source,
						functionTriple.getNode(), contextArgs, arguments, null),
				new LinkedList<BlockItemNode>());

		return result;
	}

	private ExprTriple translateSpawn(SpawnNode spawn) {
		ExprTriple result = translate(spawn.getCall());

		result.setNode(nodeFactory.newSpawnNode(spawn.getSource(),
				(FunctionCallNode) result.getNode()));
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
				.newExpressionStatementNode(nodeFactory.newOperatorNode(source,
						Operator.ASSIGN, Arrays.asList(newLhs.copy(),
								nodeFactory.newOperatorNode(source, binaryOp,
										Arrays.asList(newLhs.copy(), newRhs)))));

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
			result = new ExprTriple(nodeFactory.newOperatorNode(source,
					operator, Arrays.asList(e0, e1, e2)));
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
											Arrays.asList(tmpNode.copy(), e1))));
					stmtlist.addAll(a1);
					stmt1 = nodeFactory.newCompoundStatementNode(source,
							stmtlist);
				}
				{
					List<BlockItemNode> stmtlist = new LinkedList<>(b2);

					stmtlist.add(nodeFactory
							.newExpressionStatementNode(nodeFactory
									.newOperatorNode(source, Operator.ASSIGN,
											Arrays.asList(tmpNode.copy(), e2))));
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

	// need type node triple?
	private TypeNode translateTypeNode(TypeNode typeNode) {
		TypeNodeKind kind = typeNode.kind();

		// need to define isSideEffectFree(boolean) on type nodes
		// just look for presence of expression
		// need ExprTriple and TypeTriple

		switch (kind) {
		case ARRAY:
			break;
		case ATOMIC:
			break;
		case BASIC:
			break;
		case DOMAIN:
			break;
		case ENUMERATION:
			break;
		case FUNCTION:
			break;
		case POINTER:
			break;
		case RANGE:
			break;
		case SCOPE:
			break;
		case STRUCTURE_OR_UNION:
			break;
		case TYPEDEF_NAME:
			break;
		case VOID:
			break;
		default:
			break;

		}
		// TODO: finish this
		return typeNode;
	}

	private ExprTriple translateAlignOf(AlignOfNode align) {
		TypeNode typeNode = align.getArgument();

		// TODO: need to get the side effects from the type node
		return new ExprTriple(align);
	}

	private ExprTriple translateSizeof(SizeofNode expression) {
		SizeableNode arg = expression.getArgument();
		ExprTriple triple;

		if (arg instanceof ExpressionNode) {
			triple = translate((ExpressionNode) arg);
			makesef(triple);
			triple.setNode(nodeFactory.newSizeofNode(expression.getSource(),
					triple.getNode()));
		} else if (arg instanceof TypeNode) {
			// TODO: need to process type nodes
			triple = new ExprTriple(expression);
		} else
			throw new ABCRuntimeException("Unexpected kind of SizeableNode: "
					+ arg);
		return triple;
	}

	private ExprTriple translateScopeOf(ScopeOfNode expression) {
		ExpressionNode arg = expression.expression();
		ExprTriple result = translate(arg);

		makesef(result);
		result.setNode(nodeFactory.newScopeOfNode(expression.getSource(),
				result.getNode()));
		return result;
	}

	private ExprTriple translateRemoteReference(RemoteExpressionNode expression) {
		IdentifierExpressionNode id = expression.getIdentifierNode();
		ExprTriple result = translate(expression.getProcessExpression());

		makesef(result);
		result.setNode(nodeFactory.newRemoteExpressionNode(
				expression.getSource(), result.getNode(), id));
		return result;
	}

	private ExprTriple translateRegularRange(RegularRangeNode expression) {
		ExprTriple low = translate(expression.getLow()), hi = translate(expression
				.getHigh()), step = translate(expression.getStep());

		makesef(low);
		makesef(hi);
		makesef(step);

		ExprTriple result = new ExprTriple(nodeFactory.newRegularRangeNode(
				expression.getSource(), low.getNode(), hi.getNode(),
				step.getNode()));

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
		result.setNode(nodeFactory.newDotNode(expression.getSource(),
				result.getNode(), expression.getFieldName()));
		return result;
	}

	// ExpressionNode in middle
	// would like a triple with an InitializerNode in middle
	// would like a triple with CompoundInitializerNode in middle
	// would like a triple with TypeNode in the middle
	// would like many different kinds of triples with different nodes in middle

	private ExprTriple translateCompoundLiteral(CompoundLiteralNode expression) {
		CompoundInitializerNode ciNode = expression.getInitializerList();

		for (PairNode<DesignationNode, InitializerNode> pair : ciNode) {
			DesignationNode designationNode = pair.getLeft();
			InitializerNode initNode = pair.getRight();

			for (DesignatorNode designator : designationNode) {

				if (designator instanceof FieldDesignatorNode) {
					// no side effects possible

				} else if (designator instanceof ArrayDesignatorNode) {
					ExpressionNode indexNode = ((ArrayDesignatorNode) designator)
							.getIndex();

				} else {
					throw new ABCRuntimeException(
							"Unexpected kind of designator node: " + designator);
				}
			}
		}

		// TODO Auto-generated method stub
		return null;
	}

	private ExprTriple translateCollective(CollectiveExpressionNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	private ExprTriple translateCast(CastNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	private ExprTriple translateArrow(ArrowNode expression) {
		// TODO Auto-generated method stub
		return null;
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
		Source source = expression.getSource();

		switch (kind) {
		case ALIGNOF:
			return translateAlignOf((AlignOfNode) expression);
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

	// TODO: make these methods return a list of statements.
	// no need to keep creating new scopes.

	private void removeSideEffects(FunctionDefinitionNode function)
			throws SyntaxException {
		// function.setBody(processCompoundStatement(function.getBody()));
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
