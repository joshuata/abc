package edu.udel.cis.vsl.abc.parse.IF;

import edu.udel.cis.vsl.abc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.preproc.IF.CTokenSource;

public class Parse {

	public static CParser newCParser(CTokenSource source) {
		return new CommonCParser(source);
	}
}
