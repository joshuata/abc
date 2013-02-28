package edu.udel.cis.vsl.abc.ast.type;

import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.common.CommonTypeFactory;

public class Types {

	public static TypeFactory newTypeFactory() {
		return new CommonTypeFactory();
	}

}
