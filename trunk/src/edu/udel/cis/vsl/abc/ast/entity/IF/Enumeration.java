package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;

public interface Enumeration extends TaggedEntity {

	@Override
	EnumerationType getType();

}
