package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * <p>
 * Represents a CIVL-C <code>$assert</code> statement. This statement is used to
 * declare an assertion at some point during an execution. If the assertion is
 * not satisfied an error will be reported. In verification mode, this means
 * that the search will backtrack immediately; in run mode, the execution will
 * be terminated immediately.
 * </p>
 * 
 * <p>
 * The syntax is <code>$assert cond;</code>, or
 * <code>$assert cond : expr0, expr1, ...;</code>, where <code>cond</code> is a
 * boolean expression, <code>expr0</code>, <code>expr1</code>, ... are
 * explanations of the assertion following the format of printf's arguments.
 * </p>
 * 
 * @author Manchun Zheng (zmanchun)
 * 
 */
public interface AssertNode extends StatementNode {

	/**
	 * Gets the condition to the assert statement.
	 * 
	 * @return the asserted expression
	 */
	ExpressionNode getCondition();

	/**
	 * Sets the condition to the assert statement.
	 * 
	 * @param expr
	 *            a Boolean-valued expression
	 */
	void setCondition(ExpressionNode expr);

	/**
	 * Gets the explanation of the assert statement. Could be null if absent.
	 * 
	 * @return the explanation of the assert statement, or null if absent.
	 */
	SequenceNode<ExpressionNode> getExplanation();

	@Override
	AssertNode copy();
}
