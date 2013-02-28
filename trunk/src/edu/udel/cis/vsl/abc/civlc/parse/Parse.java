package edu.udel.cis.vsl.abc.civlc.parse;

import java.io.File;

import edu.udel.cis.vsl.abc.civlc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.civlc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.civlc.preproc.IF.CTokenSource;
import edu.udel.cis.vsl.abc.civlc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.civlc.preproc.IF.PreprocessorException;

public class Parse {

	public static CParser newCParser(Preprocessor preprocessor,
			CTokenSource source) {
		return new CommonCParser(preprocessor, source);
	}

	public static CParser newCParser(Preprocessor preprocessor, File file)
			throws PreprocessorException {
		return new CommonCParser(preprocessor, file);
	}

}
