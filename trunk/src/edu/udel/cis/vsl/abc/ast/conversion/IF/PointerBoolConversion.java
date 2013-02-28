package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardUnsignedIntegerType;

public interface PointerBoolConversion extends Conversion {

	/**
	 * Any pointer type.
	 */
	@Override
	PointerType getOldType();

	/**
	 * The boolean type _Bool.
	 */
	@Override
	StandardUnsignedIntegerType getNewType();

}
