package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.CollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonCollectiveExpressionNode extends CommonExpressionNode
		implements CollectiveExpressionNode {

	public CommonCollectiveExpressionNode(Source source,
			ExpressionNode processesGroupExpression, ExpressionNode bodyNode) {
		super(source, processesGroupExpression, bodyNode);
	}

	@Override
	public boolean isConstantExpression() {
		return false;
	}

	@Override
	public ExpressionNode getProcessesGroupExpression() {
		return (ExpressionNode) child(0);
	}

	@Override
	public ExpressionNode getBody() {
		return (ExpressionNode) child(1);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("CollectiveExpressionNode");
	}

	@Override
	public CollectiveExpressionNode copy() {
		return new CommonCollectiveExpressionNode(getSource(),
				duplicate((ExpressionNode) child(0)),
				duplicate((ExpressionNode) child(1)));
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.COLLECTIVE;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		return getProcessesGroupExpression().isSideEffectFree(
				errorsAreSideEffects);
	}

	@Override
	public void setProcessesGroupExpression(ExpressionNode arg) {
		setChild(0, arg);
	}

	@Override
	public void setBody(ExpressionNode arg) {
		setChild(1, arg);
	}
}
