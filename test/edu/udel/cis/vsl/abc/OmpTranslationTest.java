package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.transform.common.OpenMPTransformer;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;

public class OmpTranslationTest {

	private File[] systemIncludes, userIncludes;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "omp");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws ABCException, IOException {
		Activator a;
		List<String> codes = new LinkedList<String>();

		codes.add(Pruner.CODE);
		codes.add(SideEffectRemover.CODE);
		codes.add(OpenMPTransformer.CODE);
		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes);
		a.showTranslation(out, codes);
	}

	@Test
	public void dotProduct_critical() throws ABCException, IOException {
		check("dotProduct_critical");
	}

	@Test
	public void dotProduct_orphan() throws ABCException, IOException {
		check("dotProduct_orphan");
	}

	@Test
	public void dotProduct1() throws ABCException, IOException {
		check("dotProduct1");
	}

	@Test
	public void matProduct1() throws ABCException, IOException {
		check("matProduct1");
	}

	@Test
	public void matProduct2() throws ABCException, IOException {
		check("matProduct2");
	}

	@Test
	public void raceCond1() throws ABCException, IOException {
		check("raceCond1");
	}

	@Test
	public void raceCond2() throws ABCException, IOException {
		check("raceCond2");
	}

	@Test
	public void vecAdd_deadlock() throws ABCException, IOException {
		check("vecAdd_deadlock");
	}

	@Test
	public void vecAdd_fix() throws ABCException, IOException {
		check("vecAdd_fix");
	}

	@Test
	public void fig310_mxv_omp() throws ABCException, IOException {
		check("fig3.10-mxv-omp");
	}

	@Test
	public void fig498_threadprivate() throws ABCException, IOException {
		check("fig4.98-threadprivate");
	}

	@Test
	public void parallelfor() throws ABCException, IOException {
		check("parallelfor");
	}
}
