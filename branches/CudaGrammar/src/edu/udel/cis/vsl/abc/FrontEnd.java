package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import edu.udel.cis.vsl.abc.analysis.IF.Analysis;
import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.Antlr2AST;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.IF.ASTs;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.conversion.IF.Conversions;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entities;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.Nodes;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.Types;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.Values;
import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;
import edu.udel.cis.vsl.abc.config.IF.Configurations;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.Parse;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.program.IF.Programs;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.Tokens;
import edu.udel.cis.vsl.abc.transform.IF.Transform;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

/**
 * <p>
 * A FrontEnd provides a simple, high-level interface for accessing all of the
 * main functionality of ABC. It provides two different families of methods: (1)
 * methods to get or create individual components of the ABC tool chain, such as
 * factories, {@link Preprocessor}s, {@link CParser}s, etc., and (2)
 * higher-level methods which marshall together these different components in
 * order to carry out a complete translation task, such as compiling a
 * translation unit, or linking several translation units to form a complete
 * {@link Program}.
 * </p>
 * 
 * @author siegel
 * 
 */
public class FrontEnd {

	private final static String bar = "===================";

	private TokenFactory sourceFactory = Tokens.newTokenFactory();

	private PreprocessorFactory preprocessorFactory = Preprocess
			.newPreprocessorFactory();

	private CParser parser = Parse.newCParser();

	private TypeFactory typeFactory = Types.newTypeFactory();

	private ValueFactory valueFactory = Values.newValueFactory(typeFactory);

	private NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
			valueFactory);

	private ASTFactory astFactory = ASTs.newASTFactory(nodeFactory,
			sourceFactory, typeFactory);

	private EntityFactory entityFactory = Entities.newEntityFactory();

	private ConversionFactory conversionFactory = Conversions
			.newConversionFactory(typeFactory);

	private ASTBuilder builder = Antlr2AST.newASTBuilder(astFactory, parser);

	private Configuration c_config;

	private Configuration civl_config;

	private Analyzer analyzer_c;

	private Analyzer analyzer_civl;

	/**
	 * Constructs a new front end. The front end can be used repeatedly to
	 * perform different translation tasks. The factories used by this front end
	 * will persist throughout its lifetime, i.e., new factories are not created
	 * for each task.
	 * 
	 */
	public FrontEnd() {
		c_config = Configurations.newMinimalConfiguration();
		c_config.setLanguage(Language.C);
		civl_config = Configurations.newMinimalConfiguration();
		civl_config.setLanguage(Language.CIVL_C);
		analyzer_c = Analysis.newStandardAnalyzer(c_config, astFactory,
				entityFactory, conversionFactory);
		analyzer_civl = Analysis.newStandardAnalyzer(civl_config, astFactory,
				entityFactory, conversionFactory);
	}

	/**
	 * Creates a {@link Preprocessor} based on the specified system and include
	 * path lists. The new {@link Preprocessor} can be used to preprocess source
	 * files repeatedly. The method {@link Preprocessor#outputTokenSource(File)}
	 * is used to obtain the stream of tokens emanating from the preprocessor.
	 * 
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @return the new Preprocessor
	 */
	public Preprocessor getPreprocessor(File[] systemIncludePaths,
			File[] userIncludePaths) {
		return preprocessorFactory.newPreprocessor(systemIncludePaths,
				userIncludePaths);
	}

	/**
	 * Returns the parser used by this front end. The parser is used to parse a
	 * token stream and produce a {@link ParseTree}. The parser can be used
	 * repeatedly.
	 * 
	 * @return the parser
	 */
	public CParser getParser() {
		return parser;
	}

	/**
	 * Returns the {@link ASTFactory} used by this front end. This factory (or
	 * its subfactories) are used to create all components of an AST, including
	 * new {@link ASTNode}s.
	 * 
	 * @return the AST factory
	 */
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	/**
	 * Returns the {@link ASTBuilder} used by this front end. The builder is
	 * used convert a {@link ParseTree} to an {@link AST}. The builder can be
	 * used repeatedly.
	 * 
	 * @return the builder used to translate parse trees to ASTs
	 */
	public ASTBuilder getASTBuilder() {
		return builder;
	}

	/**
	 * Returns a standard {@link Analyzer}, which is used to analyze an AST,
	 * leaving behind information such as (1) the {@link Scope} of every node,
	 * (2) the {@link Type} of every expression, (3) the {@link Entity}
	 * asssociated to every identifier.
	 * 
	 * @param language
	 *            the language of the AST being analyzed
	 * @return a standard Analyzer for that language
	 */
	public Analyzer getStandardAnalyzer(Language language) {
		return language == Language.C ? analyzer_c : analyzer_civl;
	}

	/**
	 * Creates a new {@link Transformer} specified by the given transformer
	 * code.
	 * 
	 * @param code
	 *            a string code which specifies a transformer
	 * @return the new transformer
	 */
	public Transformer getTransformer(String code) {
		return Transform.newTransformer(code, astFactory);
	}

	/**
	 * Returns a program factory based on the given analyzer. The factory will
	 * apply that analyzer every time it instantiates a new {@link Program}.
	 * 
	 * @param analyzer
	 *            an analyzer that will be applied to any program created by the
	 *            factory
	 * @return the new program factory based on the analyzer
	 */
	public ProgramFactory getProgramFactory(Analyzer analyzer) {
		return Programs.newProgramFactory(astFactory, analyzer);
	}

	/**
	 * Preprocesses and parses the specified file, returning an AST
	 * representation. The AST will not be analyzed, and so will not have any
	 * information on types, identifiers, entities, and so on. This result is
	 * known as a "raw" translation unit.
	 * 
	 * @param file
	 *            the file to parse
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @return the raw translation unit obtained by parsing the file
	 * @throws PreprocessorException
	 *             if the file contains a preprocessor error
	 * @throws ParseException
	 *             if the token stream emanating from the preprocessor does not
	 *             satisfy the grammar of the language
	 * @throws SyntaxException
	 *             if the file violates some aspect of the syntax of the
	 *             language
	 * */
	public AST parse(File file, File[] systemIncludePaths,
			File[] userIncludePaths) throws PreprocessorException,
			SyntaxException, ParseException {
		Preprocessor preprocessor = getPreprocessor(systemIncludePaths,
				userIncludePaths);
		CTokenSource tokens = preprocessor.outputTokenSource(file);
		ParseTree parseTree = parser.parse(tokens);
		AST ast = builder.getTranslationUnit(parseTree);

		return ast;
	}

	/**
	 * Compiles the given file, producing an AST representation with full
	 * analysis results. The AST will contain type information, symbol table
	 * information mapping every identifier to an {@link Entity}, scope
	 * information, and so on. It is an "analyzed translation unit".
	 * 
	 * @param file
	 *            the file to compile
	 * @param language
	 *            the language in which the file is written
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * 
	 * @return the analyzed AST representing the translation unit
	 * @throws PreprocessorException
	 *             if the file contains a preprocessor error
	 * @throws ParseException
	 *             if the token stream emanating from the preprocessor does not
	 *             satisfy the grammar of the language
	 * @throws SyntaxException
	 *             if the file violates some aspect of the syntax of the
	 *             language
	 */
	public AST compile(File file, Language language, File[] systemIncludePaths,
			File[] userIncludePaths) throws PreprocessorException,
			SyntaxException, ParseException {
		AST result = parse(file, systemIncludePaths, userIncludePaths);
		Analyzer analyzer = getStandardAnalyzer(language);

		analyzer.analyze(result);
		return result;
	}

	/**
	 * Links the given translation units to form a whole program. The
	 * translation units may be "raw" (containing no analysis information) or
	 * not---it makes no difference since any analysis information will be
	 * erased and replaced with a fresh analysis. The translation units will be
	 * merged to form a single large AST; some entities may have to be renamed
	 * in this process, to avoid naming conflicts.
	 * 
	 * @param translationUnits
	 *            ASTs representing individual translation units
	 * @param language
	 *            the language to use when analyzing and linking
	 * @return the program formed by linking the translation units
	 * @throws SyntaxException
	 *             if any translation unit contains some statically detectable
	 *             error or the units cannot be linked for some reason
	 */
	public Program link(AST[] translationUnits, Language language)
			throws SyntaxException {
		Analyzer analyzer = getStandardAnalyzer(language);
		ProgramFactory programFactory = getProgramFactory(analyzer);
		Program result = programFactory.newProgram(translationUnits);

		return result;
	}

	/**
	 * Compiles the given files and links the resulting translation units to
	 * form a complete program. This is the method that "does everything".
	 * 
	 * @param files
	 *            the source files to compile
	 * @param language
	 *            the language to use when compiling the source files
	 * @param systemIncludePaths
	 *            the system include paths to search for included system headers
	 * @param userIncludePaths
	 *            the user include paths to search for included user headers
	 * @return the Program that results from compiling and linking
	 * @throws PreprocessorException
	 *             if any file contains a preprocessor error
	 * @throws ParseException
	 *             if the token stream emanating from the preprocessing of a
	 *             file does not satisfy the grammar of the language
	 * @throws SyntaxException
	 *             if any file violates some aspect of the syntax of the
	 *             language or the translation units cannot be linked for some
	 *             reason
	 */
	public Program compileAndLink(File[] files, Language language,
			File[] systemIncludePaths, File[] userIncludePaths)
			throws PreprocessorException, SyntaxException, ParseException {
		Preprocessor preprocessor = getPreprocessor(systemIncludePaths,
				userIncludePaths);
		Analyzer analyzer = getStandardAnalyzer(language);
		ProgramFactory programFactory = getProgramFactory(analyzer);
		int n = files.length;
		AST[] asts = new AST[n];
		Program result;

		for (int i = 0; i < n; i++) {
			CTokenSource tokens = preprocessor.outputTokenSource(files[i]);
			ParseTree parseTree = parser.parse(tokens);

			asts[i] = builder.getTranslationUnit(parseTree);
		}
		result = programFactory.newProgram(asts);
		return result;
	}

	/**
	 * Prints the program, symbol table, and type information to the given
	 * output stream in a plain-text, human-readable format.
	 * 
	 * @param out
	 *            the output stream
	 * @param program
	 *            the program
	 * @param pretty
	 *            if true, print AST in the original language, else print in
	 *            hierarchical form
	 * @param showTables
	 *            if true, print the symbol and type tables in addition to the
	 *            AST
	 */
	public void printProgram(PrintStream out, Program program, boolean pretty,
			boolean showTables) {
		if (pretty)
			program.prettyPrint(out);
		else
			program.print(out);
		if (showTables) {
			out.println("\n\nSymbol Table:\n");
			program.printSymbolTable(out);
			out.println("\n\nTypes:\n");
			typeFactory.printTypes(out);
		}
		out.println();
		out.flush();
	}

	/**
	 * Shows stages of translation. Useful mainly for debugging and
	 * experimentation.
	 * 
	 * @param task
	 *            configuration object specifying options and files
	 * @throws IOException
	 *             if file(s) cannot be opened
	 * @throws PreprocessorException
	 *             if any file contains a preprocessor error
	 * @throws ParseException
	 *             if the token stream emanating from the preprocessing of a
	 *             file does not satisfy the grammar of the language
	 * @throws SyntaxException
	 *             if any file violates some aspect of the syntax of the
	 *             language or the translation units cannot be linked for some
	 *             reason
	 */
	public void showTranslation(TranslationTask task) throws IOException,
			PreprocessorException, ParseException, SyntaxException {
		PrintStream out = task.getOut();
		boolean verbose = task.isVerbose();
		boolean pretty = task.doPrettyPrint();
		boolean tables = task.doShowTables();
		int nfiles = task.getFiles().length;
		FrontEnd frontEnd = new FrontEnd();
		Preprocessor preprocessor = frontEnd.getPreprocessor(
				task.getSystemIncludes(), task.getUserIncludes());
		AST[] asts = new AST[nfiles];

		for (int i = 0; i < nfiles; i++) {
			File file = task.getFiles()[i];
			String filename = file.getName();
			CTokenSource tokens;

			if (verbose) {
				out.println(bar + " File " + filename + " " + bar);
				ANTLRUtils.source(out, file);
				out.println();
				out.flush();
			}
			tokens = preprocessor.outputTokenSource(file);
			if (verbose) {
				out.println(bar + " Preprocessor output for " + filename + " "
						+ bar);
				preprocessor.printOutputDebug(out, file);
				out.println();
				out.flush();
			}
			if (!task.isPreprocOnly()) {
				ParseTree parseTree = parser.parse(tokens);

				if (verbose) {
					out.println(bar + " ANTLR Tree for " + filename + " " + bar);
					ANTLRUtils.printTree(out, parseTree.getRoot());
					out.println();
					out.flush();
				}
				asts[i] = builder.getTranslationUnit(parseTree);
				if (verbose) {
					out.println(bar + " Raw Translation Unit for " + filename
							+ " " + bar);
					if (pretty)
						asts[i].prettyPrint(out, false);
					else
						asts[i].print(out);
					out.println();
					out.flush();
				}
			}
		}
		if (!task.isPreprocOnly()) {
			Program program = frontEnd.link(asts, task.getLanguage());

			if (verbose)
				out.println(bar + " Program " + bar);
			for (String code : task.getTransformCodes()) {
				Transformer transformer = frontEnd.getTransformer(code);

				if (verbose) {
					frontEnd.printProgram(out, program, pretty, tables);
					out.println();
					out.println(bar + " Program after " + transformer + " "
							+ bar);
					out.flush();
				}
				program.apply(transformer);
			}
			frontEnd.printProgram(out, program, pretty, tables);
		}
	}
}
