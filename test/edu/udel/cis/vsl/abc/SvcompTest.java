package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;

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
	// @SuppressWarnings("unused")
	private static boolean debug = false;

	private static File root = new File(new File("examples"), "svcomp");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private File file(String name) {
		return new File(root, name);
	}

	private void check(File file) throws ABCException, IOException {
		TranslationTask task = new TranslationTask();
		FrontEnd frontEnd = new FrontEnd();

		task.setVerbose(debug);
		task.setSilent(true);
		task.setUnkownFunc(true);
		task.setSvcomp(true);
		task.addTransformCode("sef");
		task.addTransformCode("prune");
		task.setFiles(new File[] { file });
		frontEnd.showTranslation(task);
	}

	// this test checks for the GNU C feature __attribute__(...)
	@Test
	public void queue_ok_longest_true() throws ABCException, IOException {
		check(this.file("queue_ok_longest_true-unreach-call.c"));
	}

	// this test checks for the GNU C feature that allows zero parameter for a
	// function prototype, non-return-type function, implicit functions
	@Ignore
	@Test
	public void function() throws ABCException, IOException {
		check(this.file("function.c"));
	}

	@Ignore
	@Test
	public void array() throws ABCException, IOException {
		check(this.file("array.c"));
	}

	@Test
	public void svcompHeader() throws ABCException, IOException {
		check(this.file("svcompHeader.c"));
	}

	@Ignore
	@Test
	public void integerpromotion_false() throws ABCException, IOException {
		check(this.file("integerpromotion_false-unreach-call.i"));
	}

	@Test
	public void pointerIntConversions() throws ABCException, IOException {
		check(this.file("pointerIntConversions.c"));
	}

	@Ignore
	@Test
	public void sssc12_variant() throws ABCException, IOException {
		check(this.file("sssc12_variant_true-unreach-call.i"));
	}

	@Ignore
	@Test
	public void simple_loop5() throws ABCException, IOException {
		check(this.file("31_simple_loop5_vs_true-unreach-call.i"));
	}

	// implicit functions
	@Ignore
	@Test
	public void cdaudio_simpl1() throws ABCException, IOException {
		check(this
				.file("cdaudio_simpl1_false-unreach-call_true-termination.cil.c"));
	}

	@Test
	public void emptyStruct() throws ABCException, IOException {
		check(this.file("emptyStruct.c"));
	}

	@Ignore
	@Test
	public void sll_to_dll_rev() throws ABCException, IOException {
		check(this.file("sll_to_dll_rev_false-unreach-call.i"));
	}

	@Ignore
	@Test
	public void parport() throws ABCException, IOException {
		check(this.file("parport_true-unreach-call.i.cil.c"));
	}

	@Ignore
	@Test
	public void cs_fib() throws ABCException, IOException {
		check(this.file("cs_fib_false-unreach-call.i"));
	}

	@Ignore
	@Test
	public void fpointer() throws ABCException, IOException {
		check(this.file("fpointer.c"));
	}
}
