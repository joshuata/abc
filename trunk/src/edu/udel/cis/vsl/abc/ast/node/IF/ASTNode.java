package edu.udel.cis.vsl.abc.ast.node.IF;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.token.IF.Source;

/**
 * Root of the AST node type hierarchy. All AST nodes implement this interface.
 */
public interface ASTNode {

	/**
	 * The different kind of nodes. Not yet using this, but could if it turns
	 * out to be useful.
	 * 
	 * @author siegel
	 * 
	 */
	//useful in CIVL's model builder worker, to get rid of long branches of if-else
	public enum NodeKind {
		PAIR, SEQUENCE, IDENTIFIER, PRAGMA, STATIC_ASSERTION,
		// COMPOUND_INITIALIZER,
		DESIGNATION, ARRAY_DESIGNATOR, FIELD_DESIGNATOR, ENUMERATOR_DECLARATION, FIELD_DECLARATION, 
		VARIABLE_DECLARATION, FUNCTION_DECLARATION, FUNCTION_DEFINITION, TYPEDEF,
		// ALIGNOF,
		// ARROW,
		// CAST,
		// CHARACTER_CONSTANT,
		// COMPOUND_LITERAL,
		// DOT,
		// ENUMERATION_CONSTANT,
		// FLOATING_CONSTANT,
		// FUNCTION_CALL,
		GENERIC_SELECTION, IDENTIFIER_EXPRESSION,
		// INTEGER_CONSTANT,
		// OPERATOR,
		// SIZEOF,
		// STRING_LITERAL,
		ORDINARY_LABEL, SWITCH_LABEL,
		// COMPOUND_STATEMENT,
		DECLARATION_LIST,
		// EXPRESSION_STATEMENT,
		// FOR,
		// WHILE,
		// DO_WHILE,
		// GOTO,
		// IF,
		// CONTINUE,
		// BREAK,
		// RETURN,
		// LABELED_STATEMENT,
		// SWITCH,
		ARRAY_TYPE, ATOMIC_TYPE, BASIC_TYPE, ENUMERATION_TYPE, FUNCTION_TYPE, POINTER_TYPE, STRUCT_OR_UNION_TYPE, TYPEDEF_NAME, PROCESS_TYPE, SELF,
		// SPAWN,
		// WAIT,
		// ASSERT,
		// ASSUME,
		// WHEN,
		// CHOOSE,
		INVARIANT, ENSURES, REQUIRES, COLLECTIVE, RESULT,
		// REMOTE_REFERENCE,
		SCOPE_PARAMETERIZED_DECLARATION,
		// CONSTANT,
		TYPE, EXPRESSION, STATEMENT, OMP_NODE
	};

	/** ID number unique within the AST to which this node belongs. */
	int id();

	void setId(int id);

	/** The parent of this node, or null if this node has no parent. */
	ASTNode parent();

	/** The index of this node among the children of its parent. */
	int childIndex();

	/** Returns the number of children nodes of this AST node. */
	int numChildren();

	/**
	 * Returns the index-th child node of this AST node.
	 * 
	 * @param index
	 * @throws NoSuchElementException
	 *             if index is less than 0 or greater than or equal to the
	 *             number of children
	 */
	ASTNode child(int index) throws NoSuchElementException;

	/** Returns an iterator over the set of children */
	Iterator<ASTNode> children();

	/** Returns a textual representation of this node only. */
	String toString();

	/** Prints a textual representation of this node. */
	void print(String prefix, PrintStream out, boolean includeSource);

	/**
	 * Returns the attribute value associated to the given key, or null if no
	 * value has been set for that key. Note that attribute keys are generated
	 * in the ASTFactory.
	 */
	Object getAttribute(AttributeKey key);

	/**
	 * Sets the attribute value associated to the given key. This method also
	 * checks that the value belongs to the correct class. Note that attribute
	 * keys are generated in the ASTFactory.
	 */
	void setAttribute(AttributeKey key, Object value);

	/**
	 * Returns the source object that locates the origin of this program
	 * construct in the original source code. This is used for reporting
	 * friendly messages to the user.
	 * 
	 * @return source object for this node
	 */
	Source getSource();

	/**
	 * Gets the scope in which this syntactic element occurs.
	 * 
	 * @return the scope
	 */
	Scope getScope();

	/**
	 * Sets the scope of this syntactic element.
	 * 
	 * @param scope
	 *            the scope
	 */
	void setScope(Scope scope);

	void setOwner(AST owner);

	AST getOwner();

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
	 * Returns a deep copy of this AST node. The node and all of its descendants
	 * will be cloned. The cloning does not copy analysis or attribute
	 * information.
	 * 
	 * @return deep copy of this node
	 */
	ASTNode copy();

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
}
