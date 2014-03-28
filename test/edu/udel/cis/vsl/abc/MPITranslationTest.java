package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.ABC.Language;

public class MPITranslationTest {

	private File[] systemIncludes, userIncludes;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "mpi");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * private void translate(String filenameRoot) throws ABCException,
	 * IOException { Activator a;
	 * 
	 * this.systemIncludes = new File[1]; a = ABC.activator(new File(root,
	 * filenameRoot + ".c"), systemIncludes, userIncludes, Language.CIVL_C);
	 * a.showMpiTransformation(out); }
	 */

	private void check(String filenameRoot) throws ABCException, IOException {
		Activator a;

		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes, Language.CIVL_C);
		a.showTranslation(out);
	}

	@Test
	public void test() throws ABCException, IOException {
		check("test");
	}

	@Test
	public void ring() throws ABCException, IOException {
		check("ring");
	}
}
