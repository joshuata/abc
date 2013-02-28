package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;

public interface Typedef extends OrdinaryEntity {

	@Override
	TypedefDeclarationNode getDefinition();

}
