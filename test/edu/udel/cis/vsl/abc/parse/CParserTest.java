package edu.udel.cis.vsl.abc.parse;

import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.common.CommonPreprocessor;

public class CParserTest {

	private static CommonPreprocessor preprocessor = new CommonPreprocessor();

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
		CommonCParser.printTree(out, preprocessor, new File(root, filenameRoot
				+ ".c"));
	}

	@Test
	public void if1() throws PreprocessorException, ParseException {
		check("if1");
	}
}
