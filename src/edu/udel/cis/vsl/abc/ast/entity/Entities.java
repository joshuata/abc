package edu.udel.cis.vsl.abc.ast.entity;

import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.common.CommonEntityFactory;

public class Entities {

	public static EntityFactory newEntityFactory() {
		return new CommonEntityFactory();
	}

}
