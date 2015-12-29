package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;
import edu.udel.cis.vsl.abc.err.IF.ABCException;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.front.c.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Checks a number of simple C programs to make sure they pass on the call graph
 * construction analyzer.
 * 
 * @author siegel
 * 
 */
public class CallGraphTest {

	/**
	 * Turn on a lot of output for debugging? Set this to true only in your
	 * local copy. Be sure to set it back to false before committing!
	 */
	private static boolean debug = false;

	private static File root = new File(new File("examples"), "c");

	FrontEnd fe = new FrontEnd();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private AST getAST(File file) throws ParseException, SyntaxException, PreprocessorException {
		FrontEnd fe = new FrontEnd();
		AST ast = fe.compile(file, Language.C, new File[0], new File[0],
				new HashMap<String, Macro>());
		return ast;
	}

	private void check(String filenameRoot) throws ABCException, IOException {
		File file = new File(root, filenameRoot + ".c");
		AST root = getAST(file);

		if (debug) {
			edu.udel.cis.vsl.abc.analysis.common.CallAnalyzer.printCallGraph(root);
		} 
	}

	@Test
	public void dagcall() throws ABCException, IOException {
		check("dagcall");
	}

	@Test
	public void indirectcall() throws ABCException, IOException {
		check("indirectcall");
	}
	
	@Test
	public void recursivecall() throws ABCException, IOException {
		check("recursivecall");
	}
}
