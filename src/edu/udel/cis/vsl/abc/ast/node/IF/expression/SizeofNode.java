package edu.udel.cis.vsl.abc.ast.node.IF.expression;

public interface SizeofNode extends ExpressionNode {

	/**
	 * The object that we taking the size of. It is either a type or an
	 * expression.
	 */
	SizeableNode getArgument();

	void setArgument(SizeableNode argument);

	@Override
	SizeofNode copy();
}
