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
	 * @return An expression involving the quantified variable which is expected
	 *         to be true.
	 */
	ExpressionNode restriction();

	/**
	 * @return The quantified expression.
	 */
	ExpressionNode expression();

}
