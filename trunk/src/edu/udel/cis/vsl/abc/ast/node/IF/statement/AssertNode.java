package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface AssertNode extends StatementNode {

	/**
	 * The argument to the assert statement.
	 * 
	 * @return the asserted expression
	 */
	ExpressionNode getExpression();

	@Override
	AssertNode copy();

}
