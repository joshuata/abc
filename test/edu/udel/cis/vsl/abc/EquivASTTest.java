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

public class EquivASTTest {

	private static boolean debug = true;

	private static File[] systemIncludes = new File[0];

	private static File[] userIncludes = new File[0];

	private static List<String> codes = Arrays.asList("prune", "sef");

	private File root = new File(new File("examples"), "equiv");

	private boolean isEquiv(String filenameRoot1, String filenameRoot2)
			throws ABCException, IOException {
		FrontEnd f = new FrontEnd();
		File file1 = new File(root, filenameRoot1 + ".c");
		File file2 = new File(root, filenameRoot2 + ".c");
		Program program1, program2;

		program1 = f.compileAndLink(new File[] { file1 }, Language.C,
				systemIncludes, userIncludes);
		program1.applyTransformers(codes);

		program2 = f.compileAndLink(new File[] { file2 }, Language.C,
				systemIncludes, userIncludes);
		program2.applyTransformers(codes);
		if (debug) {
			System.out.println("First program is: ");
			program1.prettyPrint(System.out);
			System.out.println("Second program is: ");
			program2.prettyPrint(System.out);
		}
		return program1.getAST().getRootNode()
				.equiv(program2.getAST().getRootNode());
	}

	@Test
	public void test() throws ABCException, IOException {
		assertTrue(isEquiv("test1", "test2"));
	}
}
