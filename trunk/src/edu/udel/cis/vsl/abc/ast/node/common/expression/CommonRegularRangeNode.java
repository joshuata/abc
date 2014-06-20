package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RegularRangeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonRegularRangeNode extends CommonExpressionNode implements
		RegularRangeNode {

	public CommonRegularRangeNode(Source source, ExpressionNode low,
			ExpressionNode high) {
		super(source, low, high);
	}

	public CommonRegularRangeNode(Source source, ExpressionNode low,
			ExpressionNode high, ExpressionNode step) {
		super(source, low, high, step);
	}

	@Override
	public ExpressionNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.REGULAR_RANGE;
	}

	@Override
	public boolean isConstantExpression() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ExpressionNode getLow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionNode getHigh() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionNode getStep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void printBody(PrintStream out) {
		// TODO Auto-generated method stub

	}

}
