package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOperatorNode extends CommonExpressionNode implements
		OperatorNode {

	private Operator operator;

	public CommonOperatorNode(Source source, Operator operator,
			List<ExpressionNode> arguments) {
		super(source, arguments);
		this.operator = operator;
	}

	@Override
	public Operator getOperator() {
		return operator;
	}

	@Override
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public int getNumberOfArguments() {
		return this.numChildren();
	}

	@Override
	public ExpressionNode getArgument(int index) {
		return (ExpressionNode) child(index);
	}

	@Override
	public void setArgument(int index, ExpressionNode value) {
		setChild(index, value);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("OperatorNode[operator=" + operator + "]");
	}

	private boolean hasConstantOperator() {
		switch (operator) {
		case ASSIGN:
		case BITANDEQ:
		case BITOREQ:
		case BITXOREQ:
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
			return false;
		default:
			return true;
		}
	}

	@Override
	public boolean isConstantExpression() {
		int numArgs = getNumberOfArguments();

		if (!hasConstantOperator())
			return false;
		for (int i = 0; i < numArgs; i++) {
			if (!getArgument(i).isConstantExpression())
				return false;
		}
		return true;
	}

	@Override
	public OperatorNode copy() {
		List<ExpressionNode> arguments = new LinkedList<ExpressionNode>();
		int numArgs = getNumberOfArguments();

		for (int i = 0; i < numArgs; i++)
			arguments.add(duplicate(getArgument(i)));
		return new CommonOperatorNode(getSource(), getOperator(), arguments);
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.OPERATOR;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		boolean result = true;
		switch (getOperator()) {
		case ASSIGN:
		case BITANDEQ:
		case BITOREQ:
		case BITXOREQ:
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
		case DIV:
		case MOD:
		case DEREFERENCE:
		case SUBSCRIPT:
			if (errorsAreSideEffects) {
				result = false;
				break;
			}
		default:
			for (int i = 0; i < getNumberOfArguments(); i++) {
				result = result
						&& getArgument(i)
								.isSideEffectFree(errorsAreSideEffects);
			}
		}
		return result;
	}
}
