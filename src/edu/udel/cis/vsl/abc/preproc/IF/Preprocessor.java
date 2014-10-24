package edu.udel.cis.vsl.abc.preproc.IF;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;

import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.token.IF.SourceFile;

/**
 * A Preprocessor is used to preprocess source files. A single Preprocessor
 * object can be used to preprocess multiple files.
 * 
 * @author siegel
 * 
 */
public interface Preprocessor {

	// /**
	// * Read these files to get their macros. Store the macros and use them as
	// * the starting point when parsing any subsequent file.
	// *
	// * @param implicitIncludes
	// * @throws PreprocessorException
	// */
	// void setImplicitIncludes(File[] implicitIncludes)
	// throws PreprocessorException;

	/**
	 * Returns a lexer for the given preprocessor source file. The lexer removes
	 * all occurrences of backslash-newline, scans and tokenizes the input to
	 * produce a sequence of tokens in the preprocessor grammar. It does not
	 * execute the preprocessor directives.
	 * 
	 * @param file
	 *            a preprocessor source file
	 * @return a lexer for the given file
	 * @throws IOException
	 *             if an I/O error occurs while reading the file
	 */
	Lexer lexer(File file) throws PreprocessorException;

	/**
	 * Prints the results of lexical analysis of the source file. Mainly useful
	 * for debugging.
	 * 
	 * @param out
	 *            a PrintStream to which the output should be sent
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if any kind of exception comes from ANTLR's lexer, including
	 *             a file which does not conform lexically to the preprocessor
	 *             grammar
	 */
	void lex(PrintStream out, File file) throws PreprocessorException;

	/**
	 * Returns a parser for the given preprocessor source file.
	 * 
	 * @param file
	 *            a preprocessor source file
	 * @return a parser for that file
	 * @throws PreprocessorException
	 *             if an I/O error occurs in attempting to open the file
	 */
	Parser parser(File file) throws PreprocessorException;

	/**
	 * Scans and parses the given preprocessor source file, sending a textual
	 * description of the resulting tree to out. This does not execute any
	 * preprocessor directives. It is useful mainly for debugging.
	 * 
	 * @param out
	 *            print stream to which the tree representation of the file will
	 *            be sent
	 * @param file
	 *            a preprocessor source file.
	 * @throws PreprocessorException
	 *             if the file does not conform to the preprocessor grammar, or
	 *             an I/O error occurs in reading the file
	 */
	void parse(PrintStream out, File file) throws PreprocessorException;

	Map<String, Macro> getMacros(File file) throws PreprocessorException;

	/**
	 * Given a preprocessor source file, this returns a Token Source that emits
	 * the tokens resulting from preprocessing the file.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param implicitMacros
	 *            the predefined macros, including those specified in command
	 *            line
	 * @param file
	 *            the file to be preprocessed
	 * @return a token source for the token resulting from preprocessing the
	 *         file
	 * @throws PreprocessorException
	 *             if an I/O error occurs
	 */
	CTokenSource outputTokenSource(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			File file) throws PreprocessorException;

	/**
	 * Given the name of a preprocessor source file, this returns a Token Source
	 * that emits the tokens resulting from preprocessing the file.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param implicitMacros
	 *            the predefined macros, including those specified in command
	 *            line
	 * @param filename
	 *            The name of the file to be preprocessed.
	 * @return a token source for the token resulting from preprocessing the
	 *         file
	 * @throws PreprocessorException
	 *             if an I/O error occurs
	 * @throws IOException
	 */
	CTokenSource outputTokenSource(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			String filename) throws PreprocessorException, IOException;

	/**
	 * Prints the list of tokens that result from preprocessing the file. One
	 * token is printed per line, along with information on the origin of that
	 * token. Useful mainly for debugging.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param implicitMacros
	 *            the predefined macros, including those specified in command
	 *            line
	 * @param out
	 *            where to send output list
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	void printOutputTokens(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException;

	/**
	 * Prints the result of preprocessing the file.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param implicitMacros
	 *            the predefined macros, including those specified in command
	 *            line
	 * @param out
	 *            where to send the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	void printOutput(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException;

	/**
	 * Prints the result of preprocessing the file, but surrounding the output
	 * with some lines to clearly delineate the beginning and ending of the
	 * output, and specifying the file name.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param implicitMacros
	 *            the predefined macros, including those specified in command
	 *            line
	 * @param out
	 *            where to send the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	void printOutputDebug(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException;

	/**
	 * Show the processing of the file in stages. Useful for debugging.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @param out
	 *            where to print the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if there is an I/O error the source file does not conform to
	 *             the preprocessor syntax
	 */
	void debug(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException;

	/**
	 * Returns the set of all source files processed by this preprocessor since
	 * creation, including those that have been included through preprocessor
	 * directives, recursively. Each file is assigned an index, numbered from 0,
	 * and unique to this preprocessor instance.
	 * 
	 * @return the set of source files seen by this preprocessor
	 */
	Collection<SourceFile> getSourceFiles();

	/**
	 * Returns the instance of {@link SourceFile} held by this preprocessor for
	 * which the {@link File} field equals the given argument, or
	 * <code>null</code> if no such file has been encountered by this
	 * preprocessor.
	 * 
	 * @param file
	 *            a file
	 * @return the corresponding {@link SourceFile} or <code>null</code>
	 */
	SourceFile getSourceFile(File file);

	/**
	 * Returns the number of source files encountered by this preprocessor since
	 * creation.
	 * 
	 * @return the number of source files encountered
	 */
	int getNumSourceFiles();

	/**
	 * Returns the index-th {@link SourceFile} object held by this preprocessor.
	 * 
	 * @param index
	 *            an int in range [0, n-1], where n is the int returned by
	 *            method {@link #getNumSourceFiles()}
	 * @return the index-th {@link SourceFile}
	 */
	SourceFile getSourceFile(int index);

	/**
	 * Prints the source file objects for the files encountered by this
	 * preprocessor since it was created. These are printed in a tabular form
	 * showing the index and full path of each file.
	 * 
	 * @param out
	 *            stream to which to print
	 */
	void printSourceFiles(PrintStream out);
}
