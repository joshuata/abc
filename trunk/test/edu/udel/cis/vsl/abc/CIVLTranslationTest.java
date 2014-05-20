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

public class CIVLTranslationTest {

	private File[] systemIncludes, userIncludes;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "civl");

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
		List<String> codes = new LinkedList<>();

		codes.add(Pruner.CODE);
		codes.add(SideEffectRemover.CODE);
		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".cvl"),
				systemIncludes, userIncludes);
		if (debug)
			a.showTranslation(out, codes);
		else {
			Program program = a.getProgram();

			a.printProgram(out, program);
		}
	}

	@Test
	public void spawn() throws ABCException, IOException {
		check("spawn");
	}

	@Test
	public void choose() throws ABCException, IOException {
		check("choose");
	}

	@Test
	public void duffs() throws ABCException, IOException {
		check("duffs");
	}

	@Test
	public void sideEffects() throws ABCException, IOException {
		check("sideEffects");
	}

	@Test
	public void malloc() throws ABCException, IOException {
		check("malloc");
	}

	@Test
	public void pointerScopes() throws ABCException, IOException {
		check("pointerScopes");
	}

	@Test
	public void atomicBlock() throws ABCException, IOException {
		check("atomicStatement");
	}

	@Test
	public void potentialBug() throws ABCException, IOException {
		check("potentialBug");
	}
}
