package edu.udel.cis.vsl.abc.preproc.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.common.PreprocessorParser.file_return;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.token.IF.SourceFile;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

/**
 * The class provides easy access to all services exported by the preproc module
 * (a la Facade Pattern). It includes a main method which preprocesses the file
 * and sends result to stdout.
 * 
 * TODO: support -D, i.e., object macros defined at command line TODO: reduce
 * the state of this class
 * 
 * @author Stephen F. Siegel, University of Delaware
 * 
 */
public class CommonPreprocessor implements Preprocessor {

	public final static String SHORT_FILE_NAME_PREFIX = "f";

	public final static boolean debug = true;

	private Map<File, SourceFile> sourceFileMap = new LinkedHashMap<>();

	private ArrayList<SourceFile> sourceFiles = new ArrayList<>();

	// /**
	// * Read these files to get their macros. Store the macros and use them as
	// * the starting point when parsing any subsequent file.
	// *
	// * @param implicitIncludes
	// * @throws PreprocessorException
	// */
	// @Override
	// public void setImplicitIncludes(File[] implicitIncludes)
	// throws PreprocessorException {
	// this.implicitMacros = new HashMap<String, Macro>();
	// for (File file : implicitIncludes) {
	// PreprocessorTokenSource tokenSource = outputTokenSource(file,
	// implicitMacros, tokenFactory);
	// Token token;
	//
	// do {
	// token = tokenSource.nextToken();
	// } while (token.getType() != PreprocessorLexer.EOF);
	// }
	// }

	@Override
	public Map<String, Macro> getMacros(File file) throws PreprocessorException {
		PreprocessorWorker worker = new PreprocessorWorker(
				PreprocessorWorker.defaultSystemIncludes,
				PreprocessorWorker.defaultSystemIncludes,
				new HashMap<String, Macro>());
		PreprocessorTokenSource tokenSource = worker.outputTokenSource(file,
				this, true);
		Token token;

		do {
			token = tokenSource.nextToken();
		} while (token.getType() != PreprocessorLexer.EOF);
		return tokenSource.macroMap;
	}

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
	@Override
	public PreprocessorLexer lexer(File file) throws PreprocessorException {
		try {
			CharStream charStream = new FilteredCharStream(new ANTLRFileStream(
					file.getAbsolutePath()));

			return new PreprocessorLexer(charStream);
		} catch (IOException e) {
			throw new PreprocessorException(
					"I/O error occurred while scanning " + file + ":\n" + e);
		}
	}

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
	@Override
	public void lex(PrintStream out, File file) throws PreprocessorException {
		out.println("Lexical analysis of " + file + ":");
		try {
			PreprocessorLexer lexer = lexer(file);
			int numErrors;

			PreprocessorUtils.printTokenSource(out, lexer);
			numErrors = lexer.getNumberOfSyntaxErrors();

			if (numErrors != 0)
				throw new PreprocessorException(numErrors
						+ " syntax errors occurred while scanning " + file);
		} catch (RuntimeException e) {
			throw new PreprocessorException(e.getMessage());
		}
	}

	/**
	 * Returns a parser for the given preprocessor source file.
	 * 
	 * @param file
	 *            a preprocessor source file
	 * @return a parser for that file
	 * @throws PreprocessorException
	 *             if an I/O error occurs in attempting to open the file
	 */
	@Override
	public PreprocessorParser parser(File file) throws PreprocessorException {
		PreprocessorLexer lexer = lexer(file);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);

		return new PreprocessorParser(tokenStream);
	}

	// private void addFileName(String fileName) {
	// if (!fileNameMap.containsKey(fileName)) {
	// int index = fileNameMap.size();
	//
	// fileNameMap.put(fileName, index);
	// }
	// }

	// @Override
	// public String shortFileName(String fileName) {
	// if (fileNameMap.containsKey(fileName)) {
	// return SHORT_FILE_NAME_PREFIX + fileNameMap.get(fileName);
	// } else {
	// int index = fileNameMap.size();
	//
	// fileNameMap.put(fileName, index);
	// return SHORT_FILE_NAME_PREFIX + index;
	// }
	// }

	// /**
	// * Print the list of shorter file names and the corresponding original
	// file
	// * names
	// *
	// * @param out
	// * The output stream to be used.
	// */
	// @Override
	// public void printShorterFileNameMap(PrintStream out) {
	// if (fileNameMap.size() > 0) {
	// out.println();
	// out.println("File name list:");
	// for (String fileName : fileNameMap.keySet()) {
	// out.println(SHORT_FILE_NAME_PREFIX + fileNameMap.get(fileName)
	// + "\t: " + fileName);
	// }
	// }
	// }

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
	@Override
	public void parse(PrintStream out, File file) throws PreprocessorException {
		try {
			PreprocessorParser parser = parser(file);
			file_return fileReturn = parser.file();
			int numErrors = parser.getNumberOfSyntaxErrors();
			Tree tree;

			if (numErrors != 0)
				throw new PreprocessorException(numErrors
						+ " syntax errors occurred while scanning " + file);
			out.println("AST for " + file + ":");
			out.flush();
			tree = (Tree) fileReturn.getTree();
			ANTLRUtils.printTree(out, tree);
		} catch (RecognitionException e) {
			throw new PreprocessorException(
					"Recognition error while preprocessing:\n" + e);
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			throw new PreprocessorException(e.toString());
		}
	}

	/**
	 * Given a preprocessor source file, this returns a Token Source that emits
	 * the tokens resulting from preprocessing the file.
	 * 
	 * @param file
	 * @return a token source for the token resulting from preprocessing the
	 *         file
	 * @throws PreprocessorException
	 *             if an I/O error occurs
	 */
	@Override
	public PreprocessorTokenSource outputTokenSource(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			File file) throws PreprocessorException {
		PreprocessorWorker worker = new PreprocessorWorker(systemIncludePaths,
				userIncludePaths, implicitMacros);

		return worker.outputTokenSource(file, this, false);
	}

	@Override
	public CTokenSource outputTokenSource(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			String filename) throws PreprocessorException, IOException {
		PreprocessorWorker worker = new PreprocessorWorker(systemIncludePaths,
				userIncludePaths, implicitMacros);

		return worker.outputTokenSource(filename, this);
	}

	/**
	 * Prints the list of tokens that result from preprocessing the file. One
	 * token is printed per line, along with information on the origin of that
	 * token. Useful mainly for debugging.
	 * 
	 * @param out
	 *            where to send output list
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	@Override
	public void printOutputTokens(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			PrintStream out, File file) throws PreprocessorException {
		PreprocessorWorker worker = new PreprocessorWorker(systemIncludePaths,
				userIncludePaths, implicitMacros);
		PreprocessorTokenSource source = worker.outputTokenSource(file, this,
				false);

		out.println("Post-preprocessing token stream for " + file + ":\n");
		PreprocessorUtils.printTokenSource(out, source);
		out.flush();
	}

	/**
	 * Prints the result of preprocessing the file.
	 * 
	 * @param out
	 *            where to send the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	@Override
	public void printOutput(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException {
		PreprocessorWorker worker = new PreprocessorWorker(systemIncludePaths,
				userIncludePaths, implicitMacros);
		PreprocessorTokenSource source = worker.outputTokenSource(file, this,
				false);

		PreprocessorUtils.sourceTokenSource(out, source);
		out.flush();
	}

	/**
	 * Prints the result of preprocessing the file, but surrounding the output
	 * with some lines to clearly delineate the beginning and ending of the
	 * output, and specifying the file name.
	 * 
	 * @param out
	 *            where to send the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if the file fails to adhere to the preprocessor grammar, or
	 *             an I/O occurs
	 */
	@Override
	public void printOutputDebug(File[] systemIncludePaths,
			File[] userIncludePaths, Map<String, Macro> implicitMacros,
			PrintStream out, File file) throws PreprocessorException {
		out.println("Post-preprocessing output for " + file + ":\n");
		out.println("----------------------------------->");
		printOutput(systemIncludePaths, userIncludePaths, implicitMacros, out,
				file);
		out.println("<-----------------------------------");
		out.flush();
	}

	/**
	 * Show the processing of the file in stages. Useful for debugging.
	 * 
	 * @param out
	 *            where to print the output
	 * @param file
	 *            a preprocessor source file
	 * @throws PreprocessorException
	 *             if there is an I/O error the source file does not conform to
	 *             the preprocessor syntax
	 */
	@Override
	public void debug(File[] systemIncludePaths, File[] userIncludePaths,
			Map<String, Macro> implicitMacros, PrintStream out, File file)
			throws PreprocessorException {
		PreprocessorUtils.source(out, file);
		out.println();
		lex(out, file);
		out.println();
		parse(out, file);
		out.println();
		printOutputTokens(systemIncludePaths, userIncludePaths, implicitMacros,
				out, file);
		out.println();
		printOutputDebug(systemIncludePaths, userIncludePaths, implicitMacros,
				out, file);
		out.println();
	}

	@Override
	public Collection<SourceFile> getSourceFiles() {
		return sourceFiles;
	}

	@Override
	public SourceFile getSourceFile(File file) {
		return sourceFileMap.get(file);
	}

	@Override
	public int getNumSourceFiles() {
		return sourceFiles.size();
	}

	@Override
	public SourceFile getSourceFile(int index) {
		return sourceFiles.get(index);
	}

	@Override
	public void printSourceFiles(PrintStream out) {
		out.println("Source files:");
		for (SourceFile sourceFile : sourceFiles) {
			out.println(sourceFile.getIndexName() + "\t: "
					+ sourceFile.getPath());
		}
		out.println();
		out.flush();
	}

	/**
	 * Looks to see if a {@link SourceFile} object has already been created for
	 * the given {@link File}. If so, returns that one. Else creates a new one,
	 * assigns it the next index, and stores it.
	 * 
	 * @param file
	 *            a file that is being read to produce this token source
	 * @return the {@link SourceFile} corresponding to the given file
	 */
	SourceFile getOrMakeSourceFile(File file, boolean tmpFile) {
		SourceFile result = sourceFileMap.get(file);

		if (result == null) {
			result = new SourceFile(file, sourceFiles.size());
			if (!tmpFile) {
				// don't keep track of temp files created by parsing command
				// line macros
				sourceFiles.add(result);
				sourceFileMap.put(file, result);
			}
		}
		return result;
	}

	/**
	 * This main method is just here for simple tests. The real main method is
	 * in the main class, ABC.java.
	 */
	public final static void main(String[] args) throws PreprocessorException {
		String filename = args[0];
		CommonPreprocessor p = new CommonPreprocessor();
		File file = new File(filename);

		if (debug)
			p.debug(PreprocessorWorker.defaultSystemIncludes,
					PreprocessorWorker.defaultUserIncludes,
					PreprocessorWorker.defaultImplicitMacros, System.out, file);
		else
			p.printOutput(PreprocessorWorker.defaultSystemIncludes,
					PreprocessorWorker.defaultUserIncludes,
					PreprocessorWorker.defaultImplicitMacros, System.out, file);
	}

}
