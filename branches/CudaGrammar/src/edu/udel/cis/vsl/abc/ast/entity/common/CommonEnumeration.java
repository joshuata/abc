package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.Enumeration;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;

public class CommonEnumeration extends CommonTaggedEntity implements
		Enumeration {

	public CommonEnumeration(EnumerationType type) {
		super(EntityKind.ENUMERATION, type.getTag());
		setType(type);
	}

	@Override
	public EnumerationType getType() {
		return (EnumerationType) super.getType();
	}

	@Override
	public boolean isComplete() {
		return getType().isComplete();
	}

}
