package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;

/**
 * A derivative expression is a function call to the partial derivative of an
 * abstract function.
 * 
 * @author zirkel
 * 
 */
public interface DerivativeExpressionNode extends ExpressionNode {

	/**
	 * The abstract function whose derivative is being taken.
	 * 
	 * @return The abstract function.
	 */
	ExpressionNode getFunction();

	/**
	 * Returns the number of actual arguments occurring in this derivative
	 * expression.
	 * 
	 * @return The number of actual arguments.
	 */
	int getNumberOfArguments();

	/**
	 * Returns the index-th argument, indexed from 0.
	 */
	ExpressionNode getArgument(int index);

	/**
	 * @return The number of parameters which have at least one partial
	 *         derivative taken. Note that each parameter may have a partial
	 *         taken more than once (e.g. $D[rho,{x,3},{y2})], so this is not
	 *         the same thing as the total times a partial derivative is taken.
	 *         That is, for $D[rho,{x,3},{y2})], this returns 2, not 5.
	 */
	int getNumberOfPartials();

	/**
	 * Returns the {identifier, int} pair at the specified index in the list of
	 * partial derivatives.
	 */
	PairNode<IdentifierExpressionNode, IntegerConstantNode> getPartial(int index);

	@Override
	DerivativeExpressionNode copy();
}
