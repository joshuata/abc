package edu.udel.cis.vsl.abc.ast.node.IF.expression;

public interface RemoteExpressionNode extends ExpressionNode {

	ExpressionNode getProcessExpression();

	IdentifierExpressionNode getIdentifierNode();

}
