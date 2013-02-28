package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;

public interface StructureOrUnion extends TaggedEntity {

	@Override
	StructureOrUnionType getType();

}
