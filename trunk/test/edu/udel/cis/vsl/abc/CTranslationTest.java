package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	private void check(String filenameRoot) throws ABCException, IOException {
		Activator a;

		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes);
		a.showTranslation(out);
	}

	@Test
	public void constants() throws ABCException, IOException {
		check("constants");
	}
	
	@Test
	public void adder_seq() throws ABCException, IOException {
		check("adder_seq");
	}

	@Test
	public void useNull() throws ABCException, IOException {
		check("parse/useNull");
	}

	@Test
	public void pointer1() throws ABCException, IOException {
		check("parse/pointer1");
	}

	@Test
	public void pointer2() throws ABCException, IOException {
		check("parse/pointer2");
	}
}
