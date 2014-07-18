package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.err.IF.ABCException;

/**
 * Checks a number of simple C programs to make sure they pass on the parsing
 * and analysis stages, while also applying the prune and side-effect-free
 * transformations.
 * 
 * @author siegel
 * 
 */
public class CTranslationTest {

	/**
	 * Turn on a lot of output for debugging? Set this to true only in your
	 * local copy. Be sure to set it back to false before committing!
	 */
	private static boolean debug = false;

	private static File[] systemIncludes = new File[0];

	private static File[] userIncludes = new File[0];

	private static PrintStream out = System.out;

	private static File root = new File("examples");

	private static List<String> codes = Arrays.asList("prune", "sef");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws ABCException, IOException {
		Activator a = ABC.activator(new File(root, filenameRoot + ".c"),
				systemIncludes, userIncludes);

		if (debug) {
			a.showTranslation(out, codes);
		} else {
			a.getProgram().applyTransformers(codes);
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

	@Test
	public void enum2() throws ABCException, IOException {
		check("enum2");
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

	@Test
	public void completeStruct() throws ABCException, IOException {
		check("completeStruct");
	}

	@Test
	public void forcomma() throws ABCException, IOException {
		check("forcomma");
	}

	@Test
	public void funcPointer() throws ABCException, IOException {
		check("funcPointer");
	}
}
