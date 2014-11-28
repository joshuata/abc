package edu.udel.cis.vsl.abc.preproc.common;

import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;

public class CommonPreprocessorFactory implements PreprocessorFactory {

	@Override
	public Preprocessor newPreprocessor() {
		return new CommonPreprocessor();
	}

}
