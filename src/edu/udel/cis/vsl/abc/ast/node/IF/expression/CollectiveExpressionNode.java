package edu.udel.cis.vsl.abc.ast.node.IF.expression;

public interface CollectiveExpressionNode extends ExpressionNode {

	ExpressionNode getProcessPointerExpression();

	ExpressionNode getLengthExpression();

	ExpressionNode getBody();

	@Override
	CollectiveExpressionNode copy();
}
