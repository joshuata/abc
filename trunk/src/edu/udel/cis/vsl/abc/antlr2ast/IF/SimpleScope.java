package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

/**
 * A very simple notion of lexical scope used only in the process of translating
 * from an ANTLR tree to an AST.
 * 
 * @author siegel
 * 
 */
public class SimpleScope {

	private SimpleScope parent;

	private boolean isFunctionScope;

	private Map<String, TypeNode> typedefMap = new HashMap<String, TypeNode>();

	public SimpleScope(SimpleScope parent, boolean isFunctionScope) {
		this.parent = parent;
		this.isFunctionScope = isFunctionScope;
	}

	public SimpleScope(SimpleScope parent) {
		this(parent, false);
	}

	public void putMapping(String name, TypeNode node) {
		typedefMap.put(name, node);
	}

	public TypeNode getReferencedType(String name) {
		return typedefMap.get(name);
	}

	public SimpleScope getParent() {
		return parent;
	}

	public boolean isFunctionScope() {
		return isFunctionScope;
	}
}
