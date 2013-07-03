package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.analysis.Analysis;
import edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST;
import edu.udel.cis.vsl.abc.ast.ASTs;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
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
import edu.udel.cis.vsl.abc.token.Tokens;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;
import edu.udel.cis.vsl.abc.util.ANTLRUtils;

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

	private TypeFactory typeFactory = Types.newTypeFactory();

	private ValueFactory valueFactory = Values.newValueFactory(typeFactory);

	private NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
			valueFactory);

	private TokenFactory sourceFactory = Tokens.newTokenFactory();

	private ASTFactory astFactory = ASTs.newUnitFactory(nodeFactory,
			sourceFactory, typeFactory);

	private PreprocessorFactory preprocessorFactory = Preprocess
			.newPreprocessorFactory();

	private Preprocessor preprocessor;

	private File file;

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
		this.file = file;
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
		SideEffectRemover sideEffectRemover = new SideEffectRemover();

		unit = sideEffectRemover.transform(unit);

		Analysis.performStandardAnalysis(unit);
		return unit;
	}

	/**
	 * Show every stage of translation. This is a lot of output and is only
	 * recommended for small examples.
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
	public AST showTranslation(PrintStream out) throws PreprocessorException,
			ParseException, SyntaxException, IOException {
		AST unit;
		CParser parser;
		CommonTree tree;
		SideEffectRemover sideEffectRemover;

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
		// print the raw TranslationUnit...
		out.println("\n\n" + bar + " Raw Translation Unit " + bar);
		unit = Antlr2AST.translate(parser, tree);
		unit.print(out);
		out.println();
		// perform analysis and print results...
		out.println("\n\n" + bar + " Analyzed Translation Unit " + bar + "\n");
		Analysis.performStandardAnalysis(unit);
		unit.print(out);
		out.println("\n\n" + bar + " Symbol Table " + bar + "\n");
		unit.getRootNode().getScope().print(out);
		out.println("\n\n" + bar + " Types " + bar + "\n");
		unit.getUnitFactory().getTypeFactory().printTypes(out);
		out.println();
		out.flush();
		// print the results of removing side-effects...
		out.println("\n\n" + bar + " Side-effect-free Translation Unit " + bar);
		sideEffectRemover = new SideEffectRemover();
		unit = sideEffectRemover.transform(unit);
		unit.print(out);
		out.println();
		// perform analysis and print results...
		out.println("\n\n" + bar
				+ " Analyzed Side-effect-free Translation Unit " + bar + "\n");
		Analysis.performStandardAnalysis(unit);
		unit.print(out);
		out.println("\n\n" + bar + " Symbol Table " + bar + "\n");
		unit.getRootNode().getScope().print(out);
		out.println("\n\n" + bar + " Types " + bar + "\n");
		unit.getUnitFactory().getTypeFactory().printTypes(out);
		out.println();
		out.flush();
		return unit;
	}

}
