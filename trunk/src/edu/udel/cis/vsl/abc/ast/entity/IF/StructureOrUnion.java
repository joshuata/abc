package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;

/**
 * A structure or union is an entity corresponding to a structure or union type.
 * It simply wraps the type.
 * 
 * @author siegel
 * 
 */
public interface StructureOrUnion extends TaggedEntity {

	@Override
	StructureOrUnionType getType();

}
