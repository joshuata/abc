package edu.udel.cis.vsl.abc.ast.node.IF.type;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface PointerTypeNode extends TypeNode {

	TypeNode referencedType();

	/**
	 * Returns the CIVL-C scope modifier expression associated to the pointer.
	 * May be null, if no modifier is present (equivalent to using the root
	 * scope).
	 * 
	 * The type of this exprssion will be $scope, the scope type.
	 * 
	 * The expression may be an identifier expression, in which case the
	 * identifier is the name of a scope variable.
	 * 
	 * The expression may be a ScopeOfNode, which is an expression that takes a
	 * variable and returns the scope in which the variable is declared.
	 * 
	 * @return scope modifier or null if absent
	 */
	ExpressionNode scopeModifier();

	@Override
	PointerTypeNode copy();
}
