package edu.udel.cis.vsl.abc.civlc.preproc;

import edu.udel.cis.vsl.abc.civlc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.civlc.preproc.common.CommonPreprocessorFactory;

public class Preprocess {

	public static PreprocessorFactory newPreprocessorFactory() {
		return new CommonPreprocessorFactory();
	}

}
