package edu.udel.cis.vsl.abc.ast.unit;

import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.unit.IF.UnitFactory;
import edu.udel.cis.vsl.abc.ast.unit.common.CommonUnitFactory;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class Units {

	public static UnitFactory newUnitFactory(NodeFactory nodeFactory,
			TokenFactory tokenFactory, TypeFactory typeFactory) {
		return new CommonUnitFactory(nodeFactory, tokenFactory, typeFactory);
	}

}
