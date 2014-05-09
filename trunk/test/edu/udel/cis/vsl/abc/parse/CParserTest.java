package edu.udel.cis.vsl.abc.parse;

import java.io.File;
import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.Parse;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

public class CParserTest {

	private static Preprocessor preprocessor = Preprocess
			.newPreprocessorFactory().newPreprocessor();

	static boolean debug = true;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "parse");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws PreprocessorException,
			ParseException {
		File file = new File(root, filenameRoot + ".c");
		CParser parser = Parse.newCParser(preprocessor.outputTokenSource(file));
		CommonTree tree = parser.getTree();

		ANTLRUtils.printTree(out, tree);
	}

	@Test
	public void if1() throws PreprocessorException, ParseException {
		check("if1");
	}
}
