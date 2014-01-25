package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

/**
 * Compound literals are used to represent literal array, structure, and union
 * values. See C11 Sec. 6.5.2.5 and Sec. 6.7.9.
 * 
 * This is almost the same as a compound initializer.  No need
 * to have both.  It need not be fully bracketed.  I just want
 * it in a standard form.  It is a form which can be unfolded
 * if all indexes involved are concrete.  That's why it
 * says getInitializerList.
 * 
 * @author siegel
 * 
 */
public interface CompoundLiteralNode extends ExpressionNode {

	TypeNode getTypeNode();

	CompoundInitializerNode getInitializerList();

	@Override
	CompoundLiteralNode copy();

}
