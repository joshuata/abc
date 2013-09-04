package edu.udel.cis.vsl.abc.ast.node.IF.type;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface PointerTypeNode extends TypeNode {

	TypeNode referencedType();

	/**
	 * Returns the name of the CIVL-C scope used in the scope modifier
	 * associated to this pointer. For example <code>double *<s> p;</code>
	 * indicates that p is a pointer-to-double-in-s, i.e., the scope modifier is
	 * s.
	 * 
	 * @return scope modifier or null if absent
	 */
	ExpressionNode scopeModifier();

	@Override
	PointerTypeNode copy();
}
