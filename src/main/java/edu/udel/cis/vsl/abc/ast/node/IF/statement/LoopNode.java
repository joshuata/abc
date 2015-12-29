package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * Root of type hierarchy for every kind of loop statement. Every such statement
 * has at least a condition specifying when to stay in the loop, and a body.
 * 
 * @author siegel
 * 
 */
public interface LoopNode extends StatementNode {

	public static enum LoopKind {
		WHILE, DO_WHILE, FOR
	};

	/**
	 * The condition which controls when to stay in or exit the loop. For WHILE
	 * and FOR loops, evaluated before entering body; for DO_WHILE, evaluated
	 * after each execution of body.
	 * 
	 * @return the loop condition
	 */
	ExpressionNode getCondition();

	/**
	 * Sets the loop condition for this loop node.
	 * 
	 * @param condition
	 *            the loop condition
	 */
	void setCondition(ExpressionNode condition);

	/**
	 * The loop body.
	 * 
	 * @return the loop body
	 */
	StatementNode getBody();

	/**
	 * Sets the loop body.
	 * 
	 * @param body
	 *            the body
	 */
	void setBody(StatementNode body);

	/**
	 * Gets the invariant expression associated to this loop node.
	 * 
	 * @return the invariant
	 */
	ExpressionNode getInvariant();

	/**
	 * Sets the loop invariant associated to this loop node.
	 * 
	 * @param invariant
	 *            the loop invariant expression
	 */
	void setInvariant(ExpressionNode invariant);

	/**
	 * What kind of loop is this?
	 * 
	 * @return the loop kind
	 */
	LoopKind getKind();

	@Override
	LoopNode copy();

}
