package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.analysis.Analysis;
import edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.parse.Parse;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.CTokenSource;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
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
 * TranslationUnit+ --link--> Program
 * 
 * Actions: preprocess, parse, build, analyze, removeSideEffects,
 * trim(Collection of Function)
 * 
 * getTokenStream(), getTree(), getTranslationUnit()
 * 
 * printTokenStream(), printTree(), printAST(), printTranslationUnit()
 * 
 * @author siegel
 * 
 */
public class Activator {

	private PreprocessorFactory preprocessorFactory;

	private Preprocessor preprocessor;

	private File file;

	private String bar = "===================";

	public Activator(File file, File[] systemIncludes, File[] userIncludes) {
		this.file = file;
		preprocessorFactory = Preprocess.newPreprocessorFactory();
		preprocessor = preprocessorFactory.newPreprocessor(systemIncludes,
				userIncludes);
	}

	public Activator(File file) {
		this(file, new File[0], new File[0]);
	}

	public CTokenSource getPreprocessedSource() throws PreprocessorException {
		return preprocessor.outputTokenSource(file);
	}

	public void preprocess(PrintStream out) throws PreprocessorException {
		preprocessor.printOutput(out, file);
	}

	public CommonTree getAntlrTree() throws PreprocessorException,
			ParseException {
		CParser parser = Parse.newCParser(preprocessor, file);
		CommonTree tree = parser.getTree();

		return tree;
	}

	public AST getRawTranslationUnit() throws ParseException, SyntaxException,
			PreprocessorException {
		CParser parser = Parse.newCParser(preprocessor, file);
		AST unit = Antlr2AST.buildAST(parser, null);

		return unit;
	}

	public AST getTranslationUnit() throws ParseException, SyntaxException,
			PreprocessorException {
		AST unit = getRawTranslationUnit();

		Analysis.performStandardAnalysis(unit);
		return unit;
	}

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
	 * @throws PreprocessorException
	 * @throws ParseException
	 * @throws SyntaxException
	 * @throws IOException
	 */
	public void showTranslation(PrintStream out) throws PreprocessorException,
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
	}

}
