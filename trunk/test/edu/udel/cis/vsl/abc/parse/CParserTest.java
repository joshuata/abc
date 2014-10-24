package edu.udel.cis.vsl.abc.parse;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;
import org.junit.Test;

import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.Parse;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

public class CParserTest {

	private static boolean debug = false;

	private static Preprocessor preprocessor = Preprocess
			.newPreprocessorFactory().newPreprocessor();

	private static PrintStream out = System.out;

	private static File root = new File(new File("examples"), "parse");

	private static File[] systemIncludes = new File[] {};

	private static File[] userIncludes = new File[] { new File(
			System.getProperty("user.dir")) };

	private static Map<String, Macro> implicitMacros = new HashMap<>();

	private void check(String filenameRoot) throws PreprocessorException,
			ParseException {
		File file = new File(root, filenameRoot + ".c");
		CParser parser = Parse.newCParser();
		ParseTree parseTree = parser.parse(preprocessor.outputTokenSource(
				systemIncludes, userIncludes, implicitMacros, file));
		CommonTree tree = parseTree.getRoot();

		if (debug)
			ANTLRUtils.printTree(out, tree);
	}

	@Test
	public void if1() throws PreprocessorException, ParseException {
		check("if1");
	}
}
