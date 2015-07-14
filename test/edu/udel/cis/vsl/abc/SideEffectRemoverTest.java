package edu.udel.cis.vsl.abc;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;
import edu.udel.cis.vsl.abc.err.IF.ABCException;
import edu.udel.cis.vsl.abc.program.IF.Program;

public class SideEffectRemoverTest {

	// private static boolean debug = false;

	private static List<String> codes = Arrays.asList("prune", "sef");

	private File root = new File(new File("examples"), "side-effects");

	private void check(String filename) throws ABCException, IOException {
		FrontEnd f = new FrontEnd();
		File file = new File(root, filename);
		File outputFile = new File(root, "out_" + filename);
		Program program = f
				.compileAndLink(new File[] { file }, Language.CIVL_C);

		program.applyTransformers(codes);
		program.prettyPrint(System.out);

		Program outputProgram = f.compileAndLink(new File[] { outputFile },
				Language.CIVL_C);

		assertTrue(program.getAST().equiv(outputProgram.getAST()));
	}

	@Test
	public void assign1() throws ABCException, IOException {
		check("assign1.c");
	}

	@Test
	public void assign2() throws ABCException, IOException {
		check("assign2.c");
	}

	@Test
	public void assign3() throws ABCException, IOException {
		check("assign3.c");
	}

	@Test
	public void comma() throws ABCException, IOException {
		check("comma.c");
	}

	@Test
	public void enums() throws ABCException, IOException {
		check("enums.c");
	}

	@Test
	public void for_se() throws ABCException, IOException {
		check("for-se.c");
	}

	@Test
	public void inc() throws ABCException, IOException {
		check("inc.c");
	}

	@Test
	public void recurse() throws ABCException, IOException {
		check("recurse.c");
	}

	@Test
	public void returns() throws ABCException, IOException {
		check("returns.c");
	}

	@Test
	public void types() throws ABCException, IOException {
		check("types.c");
	}

	@Test
	public void doWhile() throws ABCException, IOException {
		check("doWhile.c");
	}
}
