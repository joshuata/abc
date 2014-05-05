package edu.udel.cis.vsl.abc.ast.node.IF;

import java.io.PrintStream;
import java.util.NoSuchElementException;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.token.IF.Source;

/**
 * Root of the AST node type hierarchy. All AST nodes implement this interface.
 */
public interface ASTNode {

	/**
	 * The different kind of AST nodes. Every AST node falls into one of the
	 * following categories.
	 * 
	 * @author siegel
	 * 
	 */
	public enum NodeKind {
		ARRAY_DESIGNATOR,
		COLLECTIVE,
		DECLARATION_LIST,
		DESIGNATION,
		ENSURES,
		ENUMERATOR_DECLARATION,
		EXPRESSION,
		FIELD_DECLARATION,
		FIELD_DESIGNATOR,
		FUNCTION_DECLARATION,
		FUNCTION_DEFINITION,
		GENERIC_SELECTION,
		IDENTIFIER,
		IDENTIFIER_EXPRESSION,
		INVARIANT,
		OMP_NODE,
		OMP_REDUCTION_OPERATOR,
		ORDINARY_LABEL,
		PAIR,
		PRAGMA,
		REQUIRES,
		RESULT,
		SCOPE_PARAMETERIZED_DECLARATION,
		SELF,
		SEQUENCE,
		STATEMENT,
		STATIC_ASSERTION,
		SWITCH_LABEL,
		TYPE,
		TYPEDEF,
		TYPEDEF_NAME,
		VARIABLE_DECLARATION
	};

	/**
	 * Returns the index-th child node of this AST node.
	 * 
	 * @param index
	 * @throws NoSuchElementException
	 *             if index is less than 0 or greater than or equal to the
	 *             number of children
	 */
	ASTNode child(int index) throws NoSuchElementException;

	/** The index of this node among the children of its parent. */
	int childIndex();

	/**
	 * Returns the sequence of children of this node as an iterable object. Do
	 * not attempt to cast the iterable to another type and/or modify it; if you
	 * do, all bets are off. Use it only to iterate over the children.
	 */
	Iterable<ASTNode> children();

	/**
	 * Returns a deep copy of this AST node. The node and all of its descendants
	 * will be cloned. The cloning does not copy analysis or attribute
	 * information.
	 * 
	 * @return deep copy of this node
	 */
	ASTNode copy();

	/**
	 * Returns the attribute value associated to the given key, or null if no
	 * value has been set for that key. Note that attribute keys are generated
	 * in the ASTFactory.
	 */
	Object getAttribute(AttributeKey key);

	/**
	 * Returns the "owner" of this AST, i.e., the AST to which this node
	 * belongs. This can be <code>null</code>, in which case we say this node is
	 * "free".
	 * 
	 * @return the owner of this node or <code>null</code>
	 */
	AST getOwner();

	/**
	 * Gets the scope in which this syntactic element occurs.
	 * 
	 * @return the scope
	 */
	Scope getScope();

	/**
	 * Returns the source object that locates the origin of this program
	 * construct in the original source code. This is used for reporting
	 * friendly messages to the user.
	 * 
	 * @return source object for this node
	 */
	Source getSource();

	/** ID number unique within the AST to which this node belongs. */
	int id();

	/**
	 * Removes all children that do not satisfy the predicate and applies this
	 * method recursively to the remaining children.
	 * 
	 * Removing a node is interpreted as follows. If u is an instance of
	 * {@link SequenceNode}, and a child of u does not satisfy the predicate,
	 * then the child is removed and all subsequent elements of the sequence are
	 * shifted down to remove the gap. If u is not an instance of
	 * {@link SequenceNode} and the child does not satisfy the predicate then
	 * the child is replaced by null.
	 * 
	 * @param keep
	 *            a node predicate
	 */
	void keepOnly(NodePredicate keep);

	/**
	 * Distinguish expression, statement, type, declaration, function
	 * declaration, function definition and other nodes from each other. But
	 * does not distinguish from their sub-types, e.g, both a JumpNode and a
	 * ForLoopNode's node kind is NodeKind.STATEMENT.
	 * 
	 * @return The node kind defined as an enum element
	 */
	NodeKind nodeKind();

	/** Returns the number of children nodes of this AST node. */
	int numChildren();

	/** The parent of this node, or null if this node has no parent. */
	ASTNode parent();

	/** Prints a textual representation of this node. */
	void print(String prefix, PrintStream out, boolean includeSource);

	/**
	 * Removes the child at given index from the node. The index must be in the
	 * range [0,numChildren-1]. If there is no child at the given index (i.e.,
	 * child is null), this is a no-op.
	 * 
	 * @param index
	 *            nonnegative integer
	 */
	void removeChild(int index);

	/**
	 * Sets the attribute value associated to the given key. This method also
	 * checks that the value belongs to the correct class. Note that attribute
	 * keys are generated in the ASTFactory.
	 */
	void setAttribute(AttributeKey key, Object value);

	/**
	 * Sets the child node at the given index. This ASTNode must be either null
	 * or free (not owned by an AST) if this is called. A non-null child must
	 * have a null parent, i.e., not be the child of another node. The caller is
	 * responsible for ensuring that the child is of the appropriate kind and
	 * type. The index can be any nonnegative integer. The list of children will
	 * be expanded as necessary with null values in order to incorporate the
	 * index.
	 * 
	 * @param index
	 *            nonnegative integer
	 * @param child
	 *            a node (or null) to be made the index-th child of this node
	 */
	void setChild(int index, ASTNode child);

	void setId(int id);

	/**
	 * Sets the owner of this node to the given AST.
	 * 
	 * @param owner
	 *            the AST to make the owner of this node
	 */
	void setOwner(AST owner);

	/**
	 * Sets the scope of this syntactic element.
	 * 
	 * @param scope
	 *            the scope
	 */
	void setScope(Scope scope);

	/** Returns a textual representation of this node only. */
	String toString();
}
