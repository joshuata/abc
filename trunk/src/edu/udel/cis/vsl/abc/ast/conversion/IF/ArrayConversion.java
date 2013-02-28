package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;

public interface ArrayConversion extends Conversion {

	@Override
	ArrayType getOldType();

	@Override
	PointerType getNewType();

}
