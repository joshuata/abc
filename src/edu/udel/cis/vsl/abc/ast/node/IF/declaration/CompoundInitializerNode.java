package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.CompoundValue;

/**
 * A compound initializer (written with curly braces in C) is used to initialize
 * an array, struct, or union. It is specified as a sequence of
 * designation-initializer pairs.
 * 
 * It is also used to represent a compound literal.
 * 
 * @see {@link CompoundLiteralNode}
 * 
 * @author siegel
 * 
 */
public interface CompoundInitializerNode extends InitializerNode,
		SequenceNode<PairNode<DesignationNode, InitializerNode>> {

	@Override
	CompoundInitializerNode copy();

	/**
	 * Returns the type of this initializer. The type is determined from the
	 * type of the expression of which this initializer is part, or the declared
	 * type of the variable which it is being used to initialize.
	 * 
	 * @return the type of this initializer
	 */
	Type getType();

	/**
	 * Returns an array consisting of the members of this compound literal, or
	 * null if this compound literal cannot be concretized.
	 * 
	 * If this literal has array type, and the result is non-null, this will be
	 * an array of length n, for some n>=0, and the type will be
	 * array-of-T-of-length-n for some type T. The elements of this array will
	 * be expression nodes of type T. There will be no gaps, because elements
	 * not specified by the designations will be initialized to default values
	 * (typically 0s) as specified by the C Standard.
	 * 
	 * If this literal has structure type, the result returned is exactly like
	 * that for arrays, with n being the number of members (fields) of the
	 * struct, the elements appearing in the same order as the fields were
	 * declared.
	 * 
	 * If this literal has union type, the result will have length 1, consiting
	 * of the member of the union.
	 * 
	 * This compound literal may not be conretizeable if symbolic expressions
	 * are used as designators, rather than concrete inegers.
	 * 
	 * @return the members of this compound literal or null
	 */
	// THIS WON't work either---components can be expressions, not constants.
	// how about a new class:
	//    GeneralExpression
	//    SimpleExpression extends GeneralExpression: ExpressionNode getExpressionNode();
	//    CompoundExpression extends GeneralExpression: get length, set, etc.
	CompoundValue getMembers();

}
