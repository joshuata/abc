package edu.udel.cis.vsl.abc.ast.node;

import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.common.CommonNodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;

public class Nodes {

	public static NodeFactory newNodeFactory(TypeFactory typeFactory,
			ValueFactory valueFactory) {
		return new CommonNodeFactory(typeFactory, valueFactory);
	}

}
