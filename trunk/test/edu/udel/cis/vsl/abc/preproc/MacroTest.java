package edu.udel.cis.vsl.abc.preproc;

import org.junit.Test;

import edu.udel.cis.vsl.abc.ABC;

public class MacroTest {

	private void run(String... args) {
		ABC.main(args);
	}

	//-D<name>
	@Test
	public void adder1() {
		run("-DCIVL_PROG", "-p", "examples/macro/adder.c");
	}
	
	//-D<name>=<object>
	@Test
	public void adder2() {
		run("-DNVALUE=50", "-p", "examples/macro/adder.c");
	}

	@Test
	public void intro() {
		run("-DNAME=Joseph", "-E", "-v", "examples/macro/intro.txt");
	}
}
