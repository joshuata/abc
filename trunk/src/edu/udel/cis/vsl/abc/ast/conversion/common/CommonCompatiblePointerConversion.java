package edu.udel.cis.vsl.abc.ast.conversion.common;

import edu.udel.cis.vsl.abc.analysis.common.CompatiblePointerConversion;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;

public class CommonCompatiblePointerConversion extends CommonConversion
		implements CompatiblePointerConversion {

	public CommonCompatiblePointerConversion(PointerType type1,
			PointerType type2) {
		super(type1, type2);
	}

	@Override
	public PointerType getOldType() {
		return (PointerType) super.getOldType();
	}

	@Override
	public PointerType getNewType() {
		return (PointerType) super.getNewType();
	}
}
