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
import edu.udel.cis.vsl.abc.transform.common.IOTransformer;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;

public class IOTransformerTest {
	private File[] systemIncludes, userIncludes;

	private PrintStream out = System.out;

	private File root = new File(new File("examples"), "io");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws ABCException, IOException {
		Activator a;
		List<String> codes = new LinkedList<>();

		codes.add(IOTransformer.CODE);
		codes.add(Pruner.CODE);
		codes.add(SideEffectRemover.CODE);
		this.systemIncludes = new File[0];
		this.userIncludes = new File[0];
		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes, Language.CIVL_C);
		a.showTranslation(out, codes);
	}

	@Test
	public void io() throws ABCException, IOException {
		check("io");
	}
}
