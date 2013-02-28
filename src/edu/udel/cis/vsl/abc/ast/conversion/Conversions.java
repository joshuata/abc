package edu.udel.cis.vsl.abc.ast.conversion;

import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.conversion.common.CommonConversionFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;

public class Conversions {

	public static ConversionFactory newConversionFactory(TypeFactory typeFactory) {
		return new CommonConversionFactory(typeFactory);
	}

}
