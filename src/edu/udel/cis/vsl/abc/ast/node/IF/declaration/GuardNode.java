package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * This represents a <code>$guard</code> clause that specifies a guard for a
 * function. The function may be a system or ordinary function. It has the
 * syntax <code>$guard <bool-expr> ;</code>.
 * 
 * @author Manchun Zheng
 *
 */
public interface GuardNode extends ContractNode {
	/**
	 * Gets the boolean expression of this guard.
	 * 
	 * @return
	 */
	ExpressionNode getExpression();

	@Override
	GuardNode copy();
}
