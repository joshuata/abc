package edu.udel.cis.vsl.abc.parse.IF;

import edu.udel.cis.vsl.abc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.preproc.IF.CTokenSource;

public class Parse {
	
	public static CParser newCParser(CTokenSource source) {
		return new CommonCParser(source);
	}

	// public static CParser newCParser(Preprocessor preprocessor,
	// CTokenSource source) {
	// return new CommonCParser(preprocessor, source);
	// }
	//
	// public static CParser newCParser(Preprocessor preprocessor, File file)
	// throws PreprocessorException {
	// return new CommonCParser(preprocessor, file);
	// }
}
