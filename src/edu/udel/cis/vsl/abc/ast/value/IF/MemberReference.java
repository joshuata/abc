package edu.udel.cis.vsl.abc.ast.value.IF;

import edu.udel.cis.vsl.abc.ast.entity.IF.Field;

public interface MemberReference extends AddressValue {

	Field getField();

	AddressValue getStructureOrUnionReference();

}
