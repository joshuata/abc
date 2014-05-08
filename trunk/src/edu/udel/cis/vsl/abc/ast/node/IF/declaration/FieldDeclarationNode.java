package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

/**
 * Represents a declaration of a field (member) in a struct or union type.
 * 
 * @author siegel
 * 
 */
public interface FieldDeclarationNode extends DeclarationNode {

	/**
	 * Returns the type of the field being declared. This may be null.
	 * 
	 * @return the type node child of this node
	 */
	TypeNode getTypeNode();

	/**
	 * Sets the type node child of this node. This is the type of field.
	 * 
	 * @param type
	 *            the type of the field being declared
	 */
	void setTypeNode(TypeNode type);

	/**
	 * Returns the bit field width. This is a constant expression. It is
	 * optional. If there is no identifier, the bit width must be there, but it
	 * can also be there with an identifier. If absent, this method returns
	 * <code>null</code>. This is a child node of this node.
	 * 
	 * @return the optional bit field width expression (<code>null</code> if
	 *         absent)
	 */
	ExpressionNode getBitFieldWidth();

	/**
	 * Sets the bit field width child node of this node to the given node.
	 * 
	 * @param width
	 *            the expression to be the bit field width of this node
	 */
	void setBitFieldWidth(ExpressionNode width);

	@Override
	Field getEntity();

	@Override
	FieldDeclarationNode copy();

}
