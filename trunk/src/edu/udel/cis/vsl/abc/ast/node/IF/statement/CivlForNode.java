package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * <p>
 * Represents a CIVL <code>$for</code> or <code>$parfor</code> statement. The
 * syntax for <code>$for</code> is
 * 
 * <pre>
 * $for (i1, i2, ... : expr) stmt
 * </pre>
 * 
 * where <code>i1</code>, <code>i2</code>, etc., are the loop variables,
 * <code>expr</code> is an expression of type <code>$domain(n)</code>, where
 * <code>n</code> is the number of loop variables, and <code>stmt</code> is a
 * statement. The <code>int</code> type name may be inserted before the first
 * variable, in which case all of the variables are being declared:
 * 
 * <pre>
 * $for (int i1, i2, ... : expr) stmt
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * The syntax for <code>$parfor</code> is exactly the same with
 * <code>$parfor</code> replacing <code>$for</code>.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface CivlForNode extends StatementNode {

	/**
	 * Is this a parallel loop, i.e., <code>$parfor</code> (as opposed to
	 * <code>$for</code>)?
	 * 
	 * @return <code>true</code> iff this is a parellel loop
	 */
	boolean isParallel();

	/**
	 * Returns the iteration domain expression, which is the expression
	 * following the colon.
	 * 
	 * @return the iteration domain expression
	 */
	ExpressionNode getDomain();

	/**
	 * The loop body.
	 * 
	 * @return the loop body
	 */
	StatementNode getBody();

	/**
	 * Optional loop invariant expression.
	 * 
	 * @return loop invariant or null
	 */
	ExpressionNode getInvariant();

	/**
	 * Returns the sequence of loop variables. Each element in the sequence is
	 * either a declaration of an integer variable, or an identifier expression
	 * node representing a previously-declared integer variable.
	 * 
	 * @return the sequence of loop variables
	 */
	SequenceNode<ForLoopInitializerNode> getVariables();

	@Override
	CivlForNode copy();

}
