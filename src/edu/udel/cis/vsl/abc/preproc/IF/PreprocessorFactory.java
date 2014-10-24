package edu.udel.cis.vsl.abc.preproc.IF;


/**
 * Factory for producing a Preprocessor and CTokens.
 * 
 * @author siegel
 * 
 */
public interface PreprocessorFactory {

	// /**
	// * Produces a new Preprocessor with the given list of paths for the system
	// * and user includes. The Preprocessor can be used to preprocess multiple
	// * source files.
	// *
	// * The protocol for searching for included files is a little complicated.
	// *
	// *
	// * @param systemIncludePaths
	// * the sequence of directories to search for angle bracket
	// * includes
	// * @param userIncludePaths
	// * the sequence of directories to search for quote includes
	// * @return a new Preprocessor
	// */
	// Preprocessor newPreprocessor(File[] systemIncludePaths,
	// File[] userIncludePaths);

	/**
	 * Returns a new Preprocessor using the default include paths.
	 * 
	 * @return a new Preprocessor
	 */
	Preprocessor newPreprocessor();

}
