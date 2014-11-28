package edu.udel.cis.vsl.abc.preproc.IF;


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
	 * @return a new Preprocessor
	 */
	Preprocessor newPreprocessor();

	// CharStream newFilteredCharStreamFromFile(File file) throws IOException;
	//
	// CharStream newFilteredCharStreamFromResource(String name, String
	// resource)
	// throws IOException;

}
