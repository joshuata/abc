package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;

/**
 * An enumeration corresponds to an enumerated type, which in turn corresponds
 * to an <code>enum</code> definition in a program. Hence an {@link Enumeration}
 * basically just wraps an {@link EnumerationType}.
 * 
 * @author siegel
 * 
 */
public interface Enumeration extends TaggedEntity {

	/**
	 * Returns the enumeration type wrapped by this Enumeration.
	 * 
	 * @return the enumeration type
	 */
	@Override
	EnumerationType getType();

}
