package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * <p>
 * Represents a CIVL-C <code>$assume</code> statement. This statement is used to
 * declare an assumption at some point during an execution. If the assumption is
 * not satisfied the execution is not considered to be a real execution. In
 * verification mode, this means that the search will backtrack immediately; in
 * run mode, the execution will be terminated immediately.
 * </p>
 * 
 * <p>
 * The syntax is <code>$assume expr;</code>, where <code>expr</code> is a
 * boolean expression.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface AssumeNode extends StatementNode {

	/**
	 * Gets the argument to the assume statement.
	 * 
	 * @return the assumed expression
	 */
	ExpressionNode getExpression();

	/**
	 * Sets the argument to the assume statement.
	 * 
	 * @param expr
	 *            the argument, a boolean-valued expression
	 */
	void setExpression(ExpressionNode expr);

	@Override
	AssumeNode copy();
}
