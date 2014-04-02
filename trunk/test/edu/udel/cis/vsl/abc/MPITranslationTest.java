package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.ABC.Language;
import edu.udel.cis.vsl.abc.transform.common.MPITransformer;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;

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
		List<String> codes = new LinkedList<>();

		codes.add(MPITransformer.CODE);
		codes.add(Pruner.CODE);
		codes.add(SideEffectRemover.CODE);
		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes, Language.CIVL_C);
		a.showTranslation(out, codes);
	}

	@Test
	public void ring() throws ABCException, IOException {
		check("ring");
	}

//	@Test
//	public void sumArray() throws ABCException, IOException {
//		check("sum_array");
//	}

	@Test
	public void mpithreads_mpi() throws ABCException, IOException {
		check("mpithreads_mpi");
	}

	@Test
	public void adder_par() throws ABCException, IOException {
		check("adder_par");
	}
}
