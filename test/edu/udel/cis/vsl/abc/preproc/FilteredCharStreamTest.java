package edu.udel.cis.vsl.abc.preproc;

import java.io.PrintStream;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.preproc.common.FilteredCharStream;

import static org.junit.Assert.*;

public class FilteredCharStreamTest {

	private static PrintStream out = System.out;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String filter(String original) {
		out.print("Input---->" + original + "<--");

		CharStream input = new ANTLRStringStream(original);
		CharStream output = new FilteredCharStream(input);
		String result = "";
		int index0 = 0;

		out.println(" (size=" + input.size() + ")");

		while (index0 == output.index()) {
			int number = output.LA(1);
			char c = (char) number;

			// out.println("LA(1): " + number + "(" + c + ")");
			if (number == CharStream.EOF)
				break;
			result += c;
			output.consume();
			index0++;
			// out.println("output.index(): "+output.index());
		}
		out.print("Output--->" + result + "<--");
		out.println(" (size="+output.size()+")");
		out.println();
		return result;
	}

	@Test
	public void testNone() {
		assertEquals("abc", filter("abc"));
	}

	@Test
	public void testMiddle() {
		assertEquals("ab", filter("a\\\nb"));
	}

	@Test
	public void testBegin() {
		assertEquals("ab", filter("\\\nab"));
	}

	@Test
	public void testEnd() {
		assertEquals("ab", filter("ab\\\n"));
	}

	@Test
	public void testEmpty() {
		assertEquals("", filter("\\\n"));
	}

	@Test
	public void testDouble() {
		assertEquals("ab", filter("a\\\n\\\nb"));
	}

	@Test
	public void testBackslashOnly() {
		assertEquals("a\\b", filter("a\\b"));
	}

	@Test
	public void testNewlineOnly() {
		assertEquals("a\nb", filter("a\nb"));
	}

	@Test
	public void testDoubleBegin() {
		assertEquals("x", filter("\\\n\\\nx"));
	}
}
