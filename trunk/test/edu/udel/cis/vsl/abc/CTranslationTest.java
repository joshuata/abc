package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class CTranslationTest {

	private File[] systemIncludes, userIncludes;

	// private static boolean debug = true;

	private PrintStream out = System.out;

	private File root = new File("examples");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws PreprocessorException,
			ParseException, SyntaxException, IOException {
		Activator a;

		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes);
		a.showTranslation(out);
	}

	@Test
	public void adder_seq() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("adder_seq");
	}

	@Test
	public void useNull() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/useNull");
	}

	@Test
	public void pointer1() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/pointer1");
	}

	@Test
	public void pointer2() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/pointer2");
	}
}
