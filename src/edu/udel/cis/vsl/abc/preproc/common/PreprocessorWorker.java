package edu.udel.cis.vsl.abc.preproc.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;

import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.Tokens;

/**
 * This is the worker of the preprocessor, which is instantiated each time when
 * a preprocessing is requested. The purpose is to make the preprocessor
 * stateless and re-usable.
 * 
 * @author Manchun Zheng
 * 
 */
public class PreprocessorWorker {

	/* ******************* Package-private static fields ******************* */

	/**
	 * The system include paths to search for included system headers
	 */
	private File[] systemIncludePaths;

	/**
	 * The user include paths to search for included user headers
	 */
	private File[] userIncludePaths;

	/**
	 * The predefined macros, including those specified by command line
	 * specification.
	 */
	private Map<String, Macro> implicitMacros;

	/**
	 * The token factory.
	 */
	private TokenFactory tokenFactory = Tokens.newTokenFactory();

	/* ******************* Package-private static fields ******************* */

	/**
	 * Default value for implicit macros, which is just an empty map.
	 */
	static Map<String, Macro> defaultImplicitMacros = new HashMap<>();

	/**
	 * Default value for system include path list.
	 */
	static File[] defaultSystemIncludes = new File[] {};

	/**
	 * Default value for user include path list. Currently, it consists of one
	 * directory, namely, the working directory.
	 */
	static File[] defaultUserIncludes = new File[] { new File(
			System.getProperty("user.dir")) };

	/* *************************** Constructors **************************** */

	/**
	 * Creates a new instance of preprocessor worker.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param macros
	 *            the predefined macros, including those specified in command
	 *            line
	 */
	public PreprocessorWorker(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> macros) {
		if (systemIncludePaths == null || systemIncludePaths.length == 0)
			this.systemIncludePaths = defaultSystemIncludes;
		else
			this.systemIncludePaths = systemIncludePaths;
		if (userIncludePaths == null || userIncludePaths.length == 0)
			this.userIncludePaths = defaultUserIncludes;
		else
			this.userIncludePaths = userIncludePaths;
		if (macros == null || macros.size() == 0)
			this.implicitMacros = defaultImplicitMacros;
		else
			this.implicitMacros = new HashMap<>(macros);
	}

	/* *************************** Constructors **************************** */

	/**
	 * Given a preprocessor source file, this returns a Token Source that emits
	 * the tokens resulting from preprocessing the file.
	 * 
	 * @param file
	 *            the file to be preprocessed
	 * @param preprocessor
	 * @return a token source for the token resulting from preprocessing the
	 *         file
	 * @throws PreprocessorException
	 *             if an I/O error occurs
	 */
	PreprocessorTokenSource outputTokenSource(File file,
			CommonPreprocessor preprocessor, boolean tmpFile)
			throws PreprocessorException {
		Map<String, Macro> macroMap = new HashMap<String, Macro>();

		if (implicitMacros != null)
			macroMap.putAll(implicitMacros);
		return outputTokenSource(file, macroMap, tokenFactory, preprocessor,
				tmpFile);
	}

	/**
	 * Given a preprocessor source file, this returns a Token Source that emits
	 * the tokens resulting from preprocessing the file.
	 * 
	 * @param filename
	 *            the name of the file to be preprocessed
	 * @param preprocessor
	 * @return a token source for the token resulting from preprocessing the
	 *         file
	 * @throws PreprocessorException
	 *             if an I/O error occurs
	 */
	CTokenSource outputTokenSource(String filename,
			CommonPreprocessor preprocessor) throws PreprocessorException,
			IOException {
		Map<String, Macro> macroMap = new HashMap<String, Macro>();

		if (implicitMacros != null)
			macroMap.putAll(implicitMacros);

		File file = null;
		CharStream charStream;
		PreprocessorParser parser;
		PreprocessorLexer lexer;
		int numErrors;

		file = findFile(userIncludePaths, filename);
		if (file == null)
			file = findFile(systemIncludePaths, filename);
		if (file == null) {
			InputStream inputStream = this.getClass().getResourceAsStream(
					"/" + filename);

			if (inputStream == null)
				return null;
			charStream = new FilteredCharStream(new ANTLRInputStream(
					inputStream));
			file = new File(filename);
		} else {
			charStream = new FilteredCharStream(new ANTLRFileStream(
					file.getAbsolutePath()));
		}
		lexer = new PreprocessorLexer(charStream);
		parser = new PreprocessorParser(new CommonTokenStream(lexer));
		numErrors = parser.getNumberOfSyntaxErrors();
		if (numErrors != 0)
			throw new PreprocessorException(numErrors
					+ " syntax errors occurred while scanning included file "
					+ file);

		PreprocessorTokenSource tokenSource = new PreprocessorTokenSource(file,
				parser, systemIncludePaths, userIncludePaths, macroMap,
				tokenFactory, preprocessor, false);

		return tokenSource;
	}

	/**
	 * Given a preprocessor source file, this returns a Token Source that emits
	 * the tokens resulting from preprocessing the file.
	 * 
	 * @param file
	 * @param macroMap
	 * @param tokenFactory
	 * @param preprocessor
	 * @return
	 * @throws PreprocessorException
	 */
	private PreprocessorTokenSource outputTokenSource(File file,
			Map<String, Macro> macroMap, TokenFactory tokenFactory,
			CommonPreprocessor preprocessor, boolean tmpFile)
			throws PreprocessorException {
		PreprocessorParser parser = preprocessor.parser(file);
		PreprocessorTokenSource tokenSource = new PreprocessorTokenSource(file,
				parser, systemIncludePaths, userIncludePaths, macroMap,
				tokenFactory, preprocessor, tmpFile);

		return tokenSource;
	}

	/**
	 * Find the file with the given name by looking through the directories in
	 * the given list. Go through list from first to last. Returns first
	 * instance found.
	 * 
	 * Note: the filename may itself containing directory structure, e.g.,
	 * "sys/stdio.h".
	 * 
	 * @param paths
	 *            list of directories to search
	 * @param filename
	 *            name of file
	 * @return file named filename, or null if not found
	 */
	private File findFile(File[] paths, String filename) {
		for (File path : paths) {
			File result = new File(path, filename);

			if (result.isFile())
				return result;
		}
		return null;
	}
}
