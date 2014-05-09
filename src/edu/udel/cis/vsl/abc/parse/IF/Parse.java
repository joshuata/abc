package edu.udel.cis.vsl.abc.parse.IF;

import java.io.File;

import edu.udel.cis.vsl.abc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.preproc.IF.CTokenSource;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;

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
