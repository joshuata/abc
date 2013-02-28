package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

public interface TypedefDeclarationNode extends DeclarationNode,
		ExternalDefinitionNode, BlockItemNode {

	@Override
	Typedef getEntity();

	/**
	 * Returns the AST node for the type that is being associated to the typedef
	 * name.
	 * 
	 * @return the type assigned to this typedef name
	 */
	TypeNode getTypeNode();

	void setTypeNode(TypeNode type);
}
