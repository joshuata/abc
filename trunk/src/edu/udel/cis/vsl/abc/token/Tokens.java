package edu.udel.cis.vsl.abc.token;

import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.common.CommonTokenFactory;

public class Tokens {

	public static TokenFactory newTokenFactory() {
		return new CommonTokenFactory();
	}

}
