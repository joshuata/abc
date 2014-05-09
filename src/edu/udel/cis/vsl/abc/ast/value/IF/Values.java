package edu.udel.cis.vsl.abc.ast.value.IF;

import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.common.CommonValueFactory;

public class Values {

	public static ValueFactory newValueFactory(TypeFactory typeFactory) {
		return new CommonValueFactory(typeFactory);
	}

}
