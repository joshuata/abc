package edu.udel.cis.vsl.abc;

import java.io.File;

import org.antlr.runtime.tree.CommonTree;

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
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.Nodes;
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

/**
 * A FrontEnd provides a simple, high-level interface for accessing
 * all of the main functionality of ABC.  
 * @author siegel
 *
 */
public class FrontEnd {

	private static String bar = "===================";

	private TokenFactory sourceFactory = Tokens.newTokenFactory();

	private PreprocessorFactory preprocessorFactory = Preprocess
			.newPreprocessorFactory();

	private TypeFactory typeFactory = Types.newTypeFactory();

	private ValueFactory valueFactory = Values.newValueFactory(typeFactory);

	private NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
			valueFactory);

	private ASTFactory astFactory = ASTs.newASTFactory(nodeFactory,
			sourceFactory, typeFactory);

	private EntityFactory entityFactory = Entities.newEntityFactory();

	private ConversionFactory conversionFactory = Conversions
			.newConversionFactory(typeFactory);

	private Configuration c_config;

	private Configuration civl_config;

	public FrontEnd() {
		c_config = Configurations.newMinimalConfiguration();
		c_config.setLanguage(Language.C);
		civl_config = Configurations.newMinimalConfiguration();
		civl_config.setLanguage(Language.CIVL_C);
	}

	public Preprocessor getPreprocessor(File[] systemIncludePaths,
			File[] userIncludePaths) {
		return preprocessorFactory.newPreprocessor(systemIncludePaths,
				userIncludePaths);
	}

	public CParser getParser(CTokenSource tokens) {
		return Parse.newCParser(tokens);
	}

	public ASTBuilder getASTBuilder(CParser parser, CommonTree tree)
			throws SyntaxException {
		return Antlr2AST.newASTBuilder(parser, astFactory, tree);
	}

	public Analyzer getStandardAnalyzer(Language language) {
		return Analysis.newStandardAnalyzer(language == Language.C ? c_config
				: civl_config, astFactory, entityFactory, conversionFactory);
	}

	public Transformer getTransformer(String code) throws SyntaxException {
		return Transform.newTransformer(code, astFactory);
	}

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
		CParser parser = getParser(tokens);
		ASTBuilder builder = getASTBuilder(parser, parser.getTree());
		AST result = builder.getTranslationUnit();

		return result;
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
			CParser parser = getParser(tokens);
			ASTBuilder builder = getASTBuilder(parser, parser.getTree());

			asts[i] = builder.getTranslationUnit();
		}
		result = programFactory.newProgram(asts);
		return result;
	}

	public void showTranslation() {

	}

}
