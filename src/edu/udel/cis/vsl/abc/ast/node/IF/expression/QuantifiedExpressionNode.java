/**
 * 
 */
package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;

/**
 * A CIVL-C quantified expression, e.g.,
 * 
 * <pre>
 * forall {int x | x > 0} x > -1
 * </pre>
 * 
 * which means "for all ints x for which x&gt;0, x&gt;-1". As can be seen in
 * this example, the CIVL-C syntax allows one to restrict the domain of x to a
 * subset of the domain of its type. It is equivalent to "for all ints x, x&gt;0
 * implies x&gt;-1" but can be more convenient and also enable optimizations.
 * 
 * @author zirkel
 * 
 */
public interface QuantifiedExpressionNode extends ExpressionNode {

	/**
	 * An enumerated type for the different quantifiers.
	 * 
	 * @author siegel
	 * 
	 */
	enum Quantifier {
		/**
		 * The universal quantifier "for all".
		 */
		FORALL,
		/**
		 * The existential quantifier "there exists".
		 */
		EXISTS,
		/**
		 * A special case of the universal quantifier for expression uniform
		 * continuity.
		 */
		UNIFORM;
	}

	/**
	 * Returns the quantifier.
	 * 
	 * @return The quantifier used by this quantifier expression.
	 */
	Quantifier quantifier();

	/**
	 * Returns the declaration of the bound variable.
	 * 
	 * @return The bound variable declaration
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
