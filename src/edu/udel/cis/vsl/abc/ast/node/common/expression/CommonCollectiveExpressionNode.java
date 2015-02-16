package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.CollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonCollectiveExpressionNode extends CommonExpressionNode
		implements CollectiveExpressionNode {

	public CommonCollectiveExpressionNode(Source source,
			ExpressionNode processPointerExpression,
			ExpressionNode lengthExpression, ExpressionNode body) {
		super(source, processPointerExpression, lengthExpression, body);
	}

	@Override
	public boolean isConstantExpression() {
		return false;
	}

	@Override
	public ExpressionNode getProcessPointerExpression() {
		return (ExpressionNode) child(0);
	}

	@Override
	public ExpressionNode getLengthExpression() {
		return (ExpressionNode) child(1);
	}

	@Override
	public ExpressionNode getBody() {
		return (ExpressionNode) child(2);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("CollectiveExpressionNode");
	}

	@Override
	public CollectiveExpressionNode copy() {
		return new CommonCollectiveExpressionNode(getSource(),
				duplicate(getProcessPointerExpression()),
				duplicate(getLengthExpression()), duplicate(getBody()));
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.COLLECTIVE;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		return getBody().isSideEffectFree(errorsAreSideEffects)
				&& getLengthExpression().isSideEffectFree(errorsAreSideEffects)
				&& getProcessPointerExpression().isSideEffectFree(
						errorsAreSideEffects);
	}

	@Override
	public void setProcessPointerExpression(ExpressionNode arg) {
		setChild(0, arg);
	}

	@Override
	public void setLengthExpression(ExpressionNode arg) {
		setChild(1, arg);
	}

	@Override
	public void setBody(ExpressionNode arg) {
		setChild(2, arg);
	}
}
