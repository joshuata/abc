package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.err.IF.ABCException;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;

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
		checkDebug(filenameRoot, false);
	}

	private void checkDebug(String filenameRoot, boolean debug)
			throws ABCException, IOException {
		Activator a;
		List<String> codes = new LinkedList<String>();

		codes.add(Pruner.CODE);
		codes.add(SideEffectRemover.CODE);
		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes);
		if (debug) {
			a.showTranslation(out);
			a.printProgram(out, a.getProgram());
		} else {
			Program program = a.getProgram();

			a.printProgram(out, program);
		}
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

	@Test
	public void varargs() throws ABCException, IOException {
		check("varargs");
	}

	@Test
	public void printf() throws ABCException, IOException {
		check("printf");
	}

	@Test
	public void compound() throws ABCException, IOException {
		check("compound");
	}

	@Test
	public void compound2() throws ABCException, IOException {
		check("compound2");
	}

	@Test
	public void enum1() throws ABCException, IOException {
		check("enum1");
	}

	@Test(expected = ABCException.class)
	public void tagBad1() throws ABCException, IOException {
		check("tagBad1");
	}

	@Test
	public void tagGood1() throws ABCException, IOException {
		check("tagGood1");
	}

	@Test
	public void a2d() throws ABCException, IOException {
		check("a2d");
	}
	
	@Test
	public void labels() throws ABCException, IOException {
		check("labels");
	}
}
