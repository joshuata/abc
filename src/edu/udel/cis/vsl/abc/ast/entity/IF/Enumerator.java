package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;

public interface Enumerator extends OrdinaryEntity {

	@Override
	EnumeratorDeclarationNode getDefinition();

	@Override
	EnumerationType getType();

	/**
	 * Returns the optional constant integer value assigned to this enumerator,
	 * or null if this is missing.
	 * 
	 * @return
	 */
	Value getValue();
}
