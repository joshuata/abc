/**
 * 
 */
package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;

/**
 * A quantified expression (e.g. forall {int x | x > 0} x > -1;).
 * 
 * @author zirkel
 * 
 */
public interface QuantifiedExpressionNode extends ExpressionNode {

	enum Quantifier {
		FORALL, EXISTS, UNIFORM;
	}

	/**
	 * @return The quantifier used by this quantifier expression.
	 */
	Quantifier quantifier();

	/**
	 * 
	 * @return The quantified variable.
	 */
	VariableDeclarationNode variable();

	/**
	 * 
	 * @return True iff the quantified variable in this expression is specified
	 *         to have a range (e.g. i=0..n).
	 */
	boolean isRange();

	/**
	 * 
	 * @return An expression involving the quantified variable which is expected
	 *         to be true. Null iff isRange()==true.
	 */
	ExpressionNode restriction();

	/**
	 * 
	 * @return If the quantified variable in this expression is specified to
	 *         have a range, the lower end of the range (e.g. 0 in i=0..n). Null
	 *         iff isRange() == false.
	 */
	ExpressionNode lower();

	/**
	 * 
	 * @return If the quantified variable in this expression is specified to
	 *         have a range, the upper end of the range (e.g. n in i=0..n). Null
	 *         iff isRange() == false.
	 */
	ExpressionNode upper();

	/**
	 * @return The quantified expression.
	 */
	ExpressionNode expression();

}
