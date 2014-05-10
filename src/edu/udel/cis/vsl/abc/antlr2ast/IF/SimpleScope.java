package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

/**
 * A very simple notion of lexical scope used only in the process of translating
 * from an ANTLR tree to an AST. The scopes form a rooted tree.
 * 
 * @author siegel
 * 
 */
public class SimpleScope {

	private SimpleScope parent;

	private boolean isFunctionScope;

	/**
	 * Mapping from typedef names to corresponding type node in typedef for all
	 * typedefs in this scope.
	 */
	private Map<String, TypeNode> typedefMap = new HashMap<String, TypeNode>();

	/**
	 * Constructs new scope with specified parent scope.
	 * 
	 * @param parent
	 *            the parent scope, i.e., the scope immediately containing this
	 *            scope, or <code>null</code> if this is the root scope
	 * @param isFunctionScope
	 *            is this a function scope, i.e., the outermost scope of the
	 *            function body
	 */
	public SimpleScope(SimpleScope parent, boolean isFunctionScope) {
		this.parent = parent;
		this.isFunctionScope = isFunctionScope;
	}

	/**
	 * Constructs a new non-function scope with specified parent scope.
	 * 
	 * @param parent
	 *            the parent scope of the new scope
	 */
	public SimpleScope(SimpleScope parent) {
		this(parent, false);
	}

	/**
	 * Declares that there is a typdef in this scope with given name and type
	 * node.
	 * 
	 * @param name
	 *            the typedef name
	 * @param node
	 *            the node representing the type in the typdef
	 */
	public void putMapping(String name, TypeNode node) {
		typedefMap.put(name, node);
	}

	/**
	 * Returns the type node in the typedef in this scope with the given name,
	 * or <code>null</code> if there is no typedef in this scope with that name
	 * 
	 * @param name
	 *            the typedef name
	 * @return the type node in the typedef with that name
	 */
	public TypeNode getReferencedType(String name) {
		return typedefMap.get(name);
	}

	/**
	 * Returns the parent scope of this scope, i.e., the scope immediately
	 * containing this scope, or <code>null</code> if this is the root scope
	 * 
	 * @return the parent scope or <code>null</code>
	 */
	public SimpleScope getParent() {
		return parent;
	}

	/**
	 * Is this a function scope, i.e., the outermost scope of a function body?
	 * 
	 * @return <code>true</code> iff this is a function scope, else
	 *         <code>false</code>
	 */
	public boolean isFunctionScope() {
		return isFunctionScope;
	}
}
