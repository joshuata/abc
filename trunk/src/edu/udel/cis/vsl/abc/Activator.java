package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.analysis.Analysis;
import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST;
import edu.udel.cis.vsl.abc.antlr2ast.impl.ASTBuilder;
import edu.udel.cis.vsl.abc.ast.ASTs;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.conversion.Conversions;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.entity.Entities;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.node.Nodes;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.Types;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.Values;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.parse.Parse;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.CTokenSource;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.program.Programs;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.reason.IF.Reasoner;
import edu.udel.cis.vsl.abc.reason.common.CommonReasoner;
import edu.udel.cis.vsl.abc.token.Tokens;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;
import edu.udel.cis.vsl.abc.util.ANTLRUtils;
import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

/**
 * Marshalls together the various components of the ABC tool chain to perform
 * one or more compilation tasks.
 * 
 * <ul>
 * <li>Source file(s) --preprocess--> TokenStream</li>
 * <li>TokenStream --parse--> Antlr Tree</li>
 * <li>Antlr Tree --build--> TranslationUnit</li>
 * <li>TranslationUnit --analyze--> TranslationUnit</li>
 * <li>TranslationUnit --transform--> TranslationUnit</li>
 * </ul>
 * 
 * Side-effect-removal is one common transformation and a method is provided to
 * do it.
 * 
 * @author siegel
 * 
 */
public class Activator {

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

	private Analyzer standardAnalyzer = Analysis.newStandardAnalyzer(
			astFactory, entityFactory, nodeFactory, sourceFactory,
			conversionFactory);

	private Transformer sideEffectRemover = new SideEffectRemover();

	private Transformer pruner = new Pruner(astFactory);

	private ProgramFactory programFactory = Programs.newProgramFactory(
			astFactory, standardAnalyzer, pruner, sideEffectRemover);

	private Preprocessor preprocessor;

	private File file;

	private SymbolicUniverse universe = SARL.newStandardUniverse();

	private Reasoner reasoner;

	/**
	 * Creates a new Activator instance with the given file and include paths.
	 * No action is taken: the file is not opened.
	 * 
	 * @param file
	 *            an input file
	 * @param systemIncludes
	 *            list of system include paths
	 * @param userIncludes
	 *            list of user include paths
	 */
	public Activator(File file, File[] systemIncludes, File[] userIncludes) {
		String filename = file.getName();

		this.file = file;
		this.reasoner = new CommonReasoner(this.universe);
		ABC.setLanguageFromName(filename);
		preprocessor = preprocessorFactory.newPreprocessor(systemIncludes,
				userIncludes);
	}

	/**
	 * Creates a new Activator instance with empty system and user include
	 * paths.
	 * 
	 * @param file
	 *            an input file
	 */
	public Activator(File file) {
		this(file, new File[0], new File[0]);
	}

	/**
	 * Returns the factory responsible for making Types.
	 * 
	 * @return the type factory
	 */
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	/**
	 * Returns the factory responsible for making Values.
	 * 
	 * @return the value factory
	 */
	public ValueFactory getValueFactory() {
		return valueFactory;
	}

	/**
	 * Returns the factory responsible for making ASTNodes.
	 * 
	 * @return the node factory
	 */
	public NodeFactory getNodeFactory() {
		return nodeFactory;
	}

	/**
	 * Returns the factory responsible for making tokens.
	 * 
	 * @return the token factory
	 */
	public TokenFactory getTokeFactory() {
		return sourceFactory;
	}

	/**
	 * Returns the factory responsible for making ASTs.
	 * 
	 * @return the AST factory
	 */
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	/**
	 * Returns the factory responsible for making preprocessors.
	 * 
	 * @return the preprocessor factory
	 */
	public PreprocessorFactory getPreprocessorFactory() {
		return preprocessorFactory;
	}

	/**
	 * Returns the program factory responsible for making programs form ASTs.
	 */
	public ProgramFactory getProgramFactory() {
		return programFactory;
	}

	/**
	 * Returns the preprocessor used by this activator on the file.
	 * 
	 * @return the preprocessor
	 */
	public Preprocessor getPreprocessor() {
		return preprocessor;
	}

	/**
	 * Returns the output token source from the preprocessor: this is the stream
	 * of tokens that result after preprocessing the file.
	 * 
	 * @return the preprocesses token source
	 * 
	 * @throws PreprocessorException
	 *             if the file contains a preprocessing error
	 */
	public CTokenSource getPreprocessedSource() throws PreprocessorException {
		return preprocessor.outputTokenSource(file);
	}

	/**
	 * Preprocesses the file and prints the output to the given stream.
	 * 
	 * @param out
	 *            a PrintStream (e.g., System.out)
	 * @throws PreprocessorException
	 *             if the file contains a preprocessing error
	 */
	public void preprocess(PrintStream out) throws PreprocessorException {
		preprocessor.printOutput(out, file);
	}

	/**
	 * Returns the ANTLR tree that results from the ANTRL parser after parsing
	 * the preprocessed token stream.
	 * 
	 * @return the ANTLR tree
	 * @throws PreprocessorException
	 *             if file contains a preprocessing error
	 * @throws ParseException
	 *             if file violates the (expanded) C grammar
	 */
	public CommonTree getAntlrTree() throws PreprocessorException,
			ParseException {
		CParser parser = Parse.newCParser(preprocessor, file);
		CommonTree tree = parser.getTree();

		return tree;
	}

	/**
	 * Returns the unanalyzed AST.
	 * 
	 * @return the unanalyzed AST
	 * @throws ParseException
	 *             if file violates the grammar
	 * @throws SyntaxException
	 *             if file has some syntactic error beyond that specified by
	 *             grammar
	 * @throws PreprocessorException
	 *             if file has preprocessor error
	 */
	public AST getRawTranslationUnit() throws ParseException, SyntaxException,
			PreprocessorException {
		CParser parser = Parse.newCParser(preprocessor, file);
		AST ast = Antlr2AST.build(parser, astFactory, null);

		return ast;
	}

	/**
	 * Returns the analyzed AST. This AST contains scope, type, and
	 * "symbol table" information.
	 * 
	 * @throws ParseException
	 *             if file violates the grammar
	 * @throws SyntaxException
	 *             if file has some syntactic error beyond that specified by
	 *             grammar
	 * @throws PreprocessorException
	 *             if file has preprocessor error
	 */
	public AST getTranslationUnit() throws ParseException, SyntaxException,
			PreprocessorException {
		AST unit = getRawTranslationUnit();

		Analysis.performStandardAnalysis(unit);
		return unit;
	}

	/**
	 * Returns the Program object containing the analyzed AST obtained from
	 * parsing the file. The program will not have any transformations performed
	 * upon it, but these can be accomplished by calling methods in the program
	 * itself.
	 * 
	 * @return the Program
	 * @throws ParseException
	 *             if a parsing error occurs
	 * @throws SyntaxException
	 *             if a syntax error is found in the parsed file
	 * @throws PreprocessorException
	 *             if an error occurs in preprocessing the file
	 */
	public Program getProgram() throws ParseException, SyntaxException,
			PreprocessorException {
		AST ast = getRawTranslationUnit();
		Program program = programFactory.newProgram(ast);

		return program;
	}

	/**
	 * Creates a new analyzed AST from the given root node. This method is
	 * useful for those doing AST transformations. The typical flow is to create
	 * the AST using one of the methods above, then release the AST, then modify
	 * the nodes, cloning, etc., then invoke this method to create a new AST.
	 * 
	 * @param root
	 *            a root AST node.
	 * @return new analyzed AST with that root
	 * @throws SyntaxException
	 *             if AST has a syntax error
	 */
	public AST newTranslationUnit(ASTNode root) throws SyntaxException {
		AST ast = astFactory.newTranslationUnit(root);

		Analysis.performStandardAnalysis(ast);
		return ast;
	}

	/**
	 * Returns the analyzed AST with all side-effects expressions removed.
	 * 
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 * @throws PreprocessorException
	 */
	public AST getSideEffectFreeTranslationUnit() throws SyntaxException,
			ParseException, PreprocessorException {
		AST unit = getTranslationUnit();

		unit = sideEffectRemover.transform(unit);
		Analysis.performStandardAnalysis(unit);
		return unit;
	}

	public AST getSideEffectFreePrunedTranslationUnit() throws SyntaxException,
			ParseException, PreprocessorException {
		AST ast = getTranslationUnit();

		ast = pruner.transform(ast);
		Analysis.performStandardAnalysis(ast);
		ast = sideEffectRemover.transform(ast);
		Analysis.performStandardAnalysis(ast);
		return ast;
	}

	/**
	 * Show every stage of translation. This is a lot of output and is only
	 * recommended for small examples. Applies pruner and side-effect-remover.
	 * 
	 * @param out
	 * @return the AST
	 * @throws PreprocessorException
	 *             if preprocessing fails
	 * @throws ParseException
	 *             if parsing the preprocessed file fails
	 * @throws SyntaxException
	 *             if analysis of syntax fails
	 * @throws IOException
	 *             if file cannot be read
	 */
	public Program showTranslation(PrintStream out) throws ABCException,
			IOException {
		AST ast;
		CParser parser;
		CommonTree tree;
		ASTBuilder builder;
		Program program;

		// print the original source file...
		ANTLRUtils.source(out, file);
		out.println();
		// print the result of preprocessing...
		out.println(bar + " Preprocessor output " + bar);
		preprocessor.printOutputDebug(out, file);
		out.println();
		// print the ANTLR Tree...
		out.println(bar + " ANTLR Parse Tree " + bar);
		parser = Parse.newCParser(preprocessor, file);
		tree = parser.getTree();
		ANTLRUtils.printTree(out, tree);
		out.println();
		out.flush();
		ast = null;
		try {
			builder = new ASTBuilder(parser, astFactory, tree);
			ast = builder.getTranslationUnit(); // creates ast
			program = programFactory.newProgram(ast); // analyzes ast
			ast = program.getAST();
		} catch (Exception e) {
			out.println("\n\n" + bar + " Translation Unit " + bar + "\n");
			if (ast == null)
				out.println("null");
			else
				ast.print(out);
			out.println();
			out.flush();
			throw e;
		}
		out.println("\n\n" + bar + " Program " + bar + "\n");
		program.print(out);
		out.println("\n\n" + bar + " Symbol Table " + bar + "\n");
		program.printSymbolTable(out);
		out.println("\n\n" + bar + " Types " + bar + "\n");
		typeFactory.printTypes(out);
		out.println();
		out.flush();
		// print the result of pruning unreachable decls...
		out.println("\n\n" + bar + " Pruned Program " + bar);
		program.prune();
		program.print(out);
		out.println();
		out.flush();
		// print the results of removing side-effects...
		out.println("\n\n" + bar + " Side-effect-free Pruned Program " + bar);
		program.removeSideEffects();
		program.print(out);
		out.println();
		out.flush();
		out.println("\n\n" + bar + " Types " + bar + "\n");
		typeFactory.printTypes(out);
		out.println();
		out.flush();

		return program;
	}
}
