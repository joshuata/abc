package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.err.IF.ABCException;

public class SvcompTest {
	/**
	 * Turn on a lot of output for debugging? Set this to true only in your
	 * local copy. Be sure to set it back to false before committing!
	 */
	private static boolean debug = false;

	private static File root = new File(new File("examples"), "svcomp");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String filename(String name) {
		return new File(root, name).getPath();
	}

	private void check(String... args) throws ABCException, IOException {
		ABC.main(args);
	}

	@Test
	public void queue_ok_longest_true() throws ABCException, IOException {
		check(this.filename("queue_ok_longest_true-unreach-call.c"));
	}
}
