package edu.udel.cis.vsl.abc.preproc.IF;

import edu.udel.cis.vsl.abc.config.IF.Configuration;

/**
 * Factory for producing a Preprocessor and CTokens.
 * 
 * @author siegel
 * 
 */
public interface PreprocessorFactory {

	/**
	 * Returns a new Preprocessor using the default include paths.
	 * 
	 * @param config
	 *            the configuration of the translation task (e.g., is svcomp
	 *            enabled?)
	 * @return a new Preprocessor
	 */
	Preprocessor newPreprocessor(Configuration config);

	// CharStream newFilteredCharStreamFromFile(File file) throws IOException;
	//
	// CharStream newFilteredCharStreamFromResource(String name, String
	// resource)
	// throws IOException;

}
