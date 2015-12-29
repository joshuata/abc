package edu.udel.cis.vsl.abc.front.c.preproc.common;

import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.front.c.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.front.c.preproc.IF.PreprocessorFactory;

public class CommonPreprocessorFactory implements PreprocessorFactory {

	@Override
	public Preprocessor newPreprocessor(Configuration config) {
		return new CommonPreprocessor(config);
	}

}
