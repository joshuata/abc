package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface WaitNode extends StatementNode {

	/**
	 * The argument to "wait": an expression of proc type indicating a process
	 * upon which to wait for termination.
	 * 
	 * @return argument to wait
	 */
	ExpressionNode getExpression();

	@Override
	WaitNode copy();

}
