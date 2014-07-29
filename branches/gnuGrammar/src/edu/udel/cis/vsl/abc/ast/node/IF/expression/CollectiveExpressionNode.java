package edu.udel.cis.vsl.abc.ast.node.IF.expression;

/**
 * <p>
 * A collective expression is a CIVL-C construct used in a collective assertion.
 * This is an assertion that involves a set of processes. Such an expression has
 * the form
 * 
 * <pre>
 * $collective(nprocs, procs) expr
 * </pre>
 * 
 * where <code>nprocs</code> is an expression of integer type which evaluates to
 * the number of processes involved in the collective operation, and
 * <code>procs</code> is an expression of type pointer-to-<code>$proc</code>
 * which points to the first in a sequence of <code>nprocs</code> processes, the
 * ordered sequence of processes involved in the collective operation.
 * </p>
 * 
 * <p>
 * Both the "body" (<code>expr</code>) and the entire collective expression have
 * boolean type.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface CollectiveExpressionNode extends ExpressionNode {

	/**
	 * Gets the expression of type pointer-to-<code>$proc</code> (
	 * <code>procs</code>). This points to the first element in the sequence of
	 * processes involved in the collective operation.
	 * 
	 * @return the pointer to the first proc in the sequence of procs involved
	 *         in the collective operation
	 */
	ExpressionNode getProcessPointerExpression();

	/**
	 * Gets the expression of integer type which evaluates to the number of
	 * processes involved in the collective operation (<code>nprocs</code>).
	 * 
	 * @return the expression yielding the number of processes involved in the
	 *         collective operation
	 */
	ExpressionNode getLengthExpression();

	/**
	 * Returns the "body" of the collective expression (<code>expr</code>).
	 * 
	 * @return the body of the collective expression
	 */
	ExpressionNode getBody();

	@Override
	CollectiveExpressionNode copy();
}
