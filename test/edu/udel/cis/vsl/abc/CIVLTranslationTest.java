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

public class CIVLTranslationTest {

	private File[] systemIncludes, userIncludes;

	// private static boolean debug = true;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "civl");

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
		a = ABC.activator(new File(root, filenameRoot + ".cvl"),
				systemIncludes, userIncludes);
		a.showTranslation(out);
	}

	@Test
	public void spawn() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("spawn");
	}

	@Test
	public void choose() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("choose");
	}
	
	@Test
	public void sideEffects() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("sideEffects");
	}

}
