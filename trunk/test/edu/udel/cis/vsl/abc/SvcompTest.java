package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.abc.err.IF.ABCException;

public class SvcompTest {
	/**
	 * Turn on a lot of output for debugging? Set this to true only in your
	 * local copy. Be sure to set it back to false before committing!
	 */
	@SuppressWarnings("unused")
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
		List<String> newArgs = new ArrayList<>();
		String[] finalArgs;

		newArgs.add("-svcomp");
		newArgs.add("-sef");
		newArgs.add("-prune");
		for (int i = 0; i < args.length; i++)
			newArgs.add(args[i]);
		finalArgs = new String[newArgs.size()];
		newArgs.toArray(finalArgs);
		ABC.main(finalArgs);
	}

	// this test checks for the GNU C feature __attribute__(...)
	@Test
	public void queue_ok_longest_true() throws ABCException, IOException {
		check(this.filename("queue_ok_longest_true-unreach-call.c"));
	}

	// this test checks for the GNU C feature that allows zero parameter for a
	// function prototype, non-return-type function
	@Ignore
	@Test
	public void function() throws ABCException, IOException {
		check(this.filename("function.c"));
	}

	@Test
	public void array() throws ABCException, IOException {
		check(this.filename("array.c"));
	}

	@Test
	public void svcompHeader() throws ABCException, IOException {
		check(this.filename("svcompHeader.c"));
	}

	@Test
	public void integerpromotion_false() throws ABCException, IOException {
		check(this.filename("integerpromotion_false-unreach-call.i"));
	}

	@Test
	public void pointerIntConversions() throws ABCException, IOException {
		check(this.filename("pointerIntConversions.c"));
	}

	@Test
	public void sssc12_variant() throws ABCException, IOException {
		check(this.filename("sssc12_variant_true-unreach-call.i"));
	}

	@Test
	public void simple_loop5() throws ABCException, IOException {
		check(this.filename("31_simple_loop5_vs_true-unreach-call.i"));
	}

	// implicit functions
	@Ignore
	@Test
	public void cdaudio_simpl1() throws ABCException, IOException {
		check(this
				.filename("cdaudio_simpl1_false-unreach-call_true-termination.cil.c"));
	}

	@Test
	public void emptyStruct() throws ABCException, IOException {
		check(this.filename("emptyStruct.c"));
	}

	@Test
	public void sll_to_dll_rev() throws ABCException, IOException {
		check(this.filename("sll_to_dll_rev_false-unreach-call.i"));
	}

	@Test
	public void parport() throws ABCException, IOException {
		check(this.filename("parport_true-unreach-call.i.cil.c"));
	}

	@Ignore
	@Test
	public void cs_fib() throws ABCException, IOException {
		check(this.filename("cs_fib_false-unreach-call.i"));
	}

	@Ignore
	@Test
	public void fpointer() throws ABCException, IOException {
		check(this.filename("fpointer.c"));
	}
}
