package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

/**
 * <p>
 * A declaration of a variable or function via a C "declarator". In addition to
 * the identifier (common to all declarations), this also specifies a type and
 * storage information.
 * </p>
 * 
 * <p>
 * Note that this is not used to declare members of structures or unions
 * ("fields"). A {@link FieldDeclarationNode} is used for that.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface OrdinaryDeclarationNode extends BlockItemNode,
		DeclarationNode, ExternalDefinitionNode {

	public enum OrdinaryDeclarationKind {
		VARIABLE_DECLARATION,
		FUNCTION_DECLARATION,
		FUNCTION_DEFINITION,
		ABSTRACT_FUNCTION_DEFINITION
	}

	OrdinaryDeclarationKind ordinaryDeclarationKind();

	/**
	 * The type of the thing being declared. This may be null: e.g., in a
	 * function declaration, the parameter types do not necessarily have to be
	 * declared.
	 */
	TypeNode getTypeNode();

	void setTypeNode(TypeNode type);

	/**
	 * Does the declaration include the "extern" storage class specifier?
	 * 
	 * For functions and objects.
	 * 
	 * @return true if declaration contains "extern"
	 */
	boolean hasExternStorage();

	void setExternStorage(boolean value);

	/**
	 * Does the declaration include the "static" storage class specifier?
	 * 
	 * For functions and objects.
	 * 
	 * @return true if declaration contains "static"
	 */
	boolean hasStaticStorage();

	void setStaticStorage(boolean value);

	@Override
	OrdinaryDeclarationNode copy();

}
