package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;

public interface CompatiblePointerConversion extends Conversion {

	@Override
	PointerType getOldType();

	@Override
	PointerType getNewType();
}
