package edu.udel.cis.vsl.abc.preproc;

import org.junit.Test;

import edu.udel.cis.vsl.abc.ABC;

public class MacroTest {

	private void run(String... args) {
		ABC.main(args);
	}

	@Test
	public void testMacro() {
		run("-D", "MYCIVL", "-p", "examples/macro/adder.c");
	}
}
