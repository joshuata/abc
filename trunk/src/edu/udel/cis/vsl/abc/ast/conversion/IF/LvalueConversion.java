package edu.udel.cis.vsl.abc.ast.conversion.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;

public interface LvalueConversion extends Conversion {

	@Override
	ObjectType getOldType();

	@Override
	UnqualifiedObjectType getNewType();

}
