package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;

public interface FunctionCallNode extends ExpressionNode {

	/**
	 * The function being called.
	 * 
	 * @return function name
	 */
	ExpressionNode getFunction();

	void setFunction(ExpressionNode function);

	/**
	 * Returns the number of actual arguments occurring in this function call.
	 * 
	 * @return the number of actual arguments
	 */
	int getNumberOfArguments();

	/**
	 * Returns the index-th argument, indexed from 0. Beware: the argument index
	 * and the child index are off by one! So, argument 0 is child 1. That is
	 * because child 0 is the function name.
	 */
	ExpressionNode getArgument(int index);

	void setArgument(int index, ExpressionNode value);

	@Override
	FunctionCallNode copy();

	/**
	 * Returns the actual scope parameter names used when instantiating a
	 * scope-generic functions, as in
	 * 
	 * <code>
	 * f<s1,s2>(...)
	 * </code>
	 * 
	 * @return
	 */
	SequenceNode<ExpressionNode> getScopeList();

}
