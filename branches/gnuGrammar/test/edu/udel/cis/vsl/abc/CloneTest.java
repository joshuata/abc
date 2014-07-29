package edu.udel.cis.vsl.abc;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Tests the "copy" method in nodes, which performs deep cloning.
 * 
 * @author siegel
 * 
 */
public class CloneTest {

	private static File[] systemIncludes = new File[0];

	private static File[] userIncludes = new File[0];

	private static File root = new File("examples");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(String filenameRoot) throws PreprocessorException,
			ParseException, SyntaxException, IOException {
		Activator a;
		AST ast1, ast2;
		ASTNode root1, root2;

		a = ABC.activator(new File(root, filenameRoot + ".c"), systemIncludes,
				userIncludes);
		ast1 = a.getTranslationUnit();
		root1 = ast1.getRootNode();
		root2 = root1.copy();
		ast2 = a.newTranslationUnit(root2);
		assertEquals(ast1.getNumberOfNodes(), ast2.getNumberOfNodes());
	}

	@Test
	public void adder_seq() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("adder_seq");
	}

	@Test
	public void useNull() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/useNull");
	}

	@Test
	public void pointer1() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/pointer1");
	}

	@Test
	public void pointer2() throws PreprocessorException, ParseException,
			SyntaxException, IOException {
		check("parse/pointer2");
	}
}
