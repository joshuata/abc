package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.StructureOrUnion;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;

public class CommonStructureOrUnion extends CommonTaggedEntity implements
		StructureOrUnion {

	public CommonStructureOrUnion(StructureOrUnionType type) {
		super(EntityKind.STRUCTURE_OR_UNION, type.getTag());
		setType(type);
	}

	@Override
	public StructureOrUnionType getType() {
		return (StructureOrUnionType) super.getType();
	}

	@Override
	public boolean isComplete() {
		return getType().isComplete();
	}

}
