package edu.udel.cis.vsl.abc.ast.node.IF;

import edu.udel.cis.vsl.abc.ast.node.common.CommonNodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;

/**
 * A factory class providing a static method to produce a new
 * {@link NodeFactory}.
 * 
 * @author siegel
 * 
 */
public class Nodes {

	/**
	 * Create a new node factory that uses the given type factory and the given
	 * value factory.
	 * 
	 * @param typeFactory
	 *            the factory for producing types
	 * @param valueFactory
	 *            the factory for producing values (constants)
	 * @return the new node factory
	 */
	public static NodeFactory newNodeFactory(TypeFactory typeFactory,
			ValueFactory valueFactory) {
		return new CommonNodeFactory(typeFactory, valueFactory);
	}

}
