package edu.udel.cis.vsl.abc.ast.node.IF;


/**
 * A node in which all children have type T.
 */
public interface SequenceNode<T extends ASTNode> extends ASTNode {

	/** Add a child element at index numChildren() */
	void addSequenceChild(T child);

	/** Returns the child at position i */
	T getSequenceChild(int i);

	/** Sets the child at index i to the T child */
	void setSequenceChild(int i, T child);

	/** Returns an iterable object of the children of this node. */
	Iterable<T> childIterable();

	@Override
	SequenceNode<T> copy();
}
