package edu.udel.cis.vsl.abc.preproc;

import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.preproc.common.CommonPreprocessorFactory;

public class Preprocess {

	public static PreprocessorFactory newPreprocessorFactory() {
		return new CommonPreprocessorFactory();
	}

}
