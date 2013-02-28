package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;

public interface CompatibleStructureOrUnionConversion extends Conversion {

	@Override
	StructureOrUnionType getOldType();

	@Override
	StructureOrUnionType getNewType();

}
