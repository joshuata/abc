package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;

/** Implicit conversion of function to pointer to function. */
public interface FunctionConversion extends Conversion {

	@Override
	FunctionType getOldType();

	@Override
	PointerType getNewType();

}
