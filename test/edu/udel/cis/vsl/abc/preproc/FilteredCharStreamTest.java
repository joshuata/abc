package edu.udel.cis.vsl.abc.preproc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.CharStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.abc.preproc.common.FilteredANTLRFileStream;
import edu.udel.cis.vsl.abc.preproc.common.FilteredANTLRInputStream;

public class FilteredCharStreamTest {

	private static boolean debug = false;

	private static PrintStream out = System.out;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String filter(String original, boolean useFileStream)
			throws IOException {
		if (debug)
			out.print("Input---->" + original + "<--");

		CharStream output = useFileStream ? new FilteredANTLRFileStream(
				original) : new FilteredANTLRInputStream("test", original);
		String result = "";
		int index0 = 0;

		if (debug)
			out.println(" (size=" + original.length() + ")");

		while (index0 == output.index()) {
			int number = output.LA(1);
			char c = (char) number;

			if (number == CharStream.EOF)
				break;
			result += c;
			output.consume();
			index0++;
		}
		if (debug) {
			out.print("Output--->" + result + "<--");
			out.println(" (size=" + output.size() + ")");
			out.println();
		}
		return result;
	}

	private String filter1(String original) throws IOException {
		return filter(original, true);
	}

	private String filter2(String original) throws IOException {
		return filter(original, false);
	}

	@Test
	public void testNone() throws IOException {
		assertEquals("abc", filter1("abc"));
		assertEquals("abc", filter2("abc"));
	}

	@Test
	public void testMiddle() throws IOException {
		assertEquals("ab", filter1("a\\\nb"));
		assertEquals("ab", filter2("a\\\nb"));
	}

	@Test
	public void testBegin() throws IOException {
		assertEquals("ab", filter1("\\\nab"));
		assertEquals("ab", filter2("\\\nab"));
	}

	@Test
	public void testEnd() throws IOException {
		assertEquals("ab", filter1("ab\\\n"));
		assertEquals("ab", filter2("ab\\\n"));
	}

	@Test
	public void testEmpty() throws IOException {
		assertEquals("", filter1("\\\n"));
		assertEquals("", filter2("\\\n"));
	}

	@Test
	public void testDouble() throws IOException {
		assertEquals("ab", filter1("a\\\n\\\nb"));
		assertEquals("ab", filter2("a\\\n\\\nb"));
	}

	@Test
	public void testBackslashOnly() throws IOException {
		assertEquals("a\\b", filter1("a\\b"));
		assertEquals("a\\b", filter2("a\\b"));
	}

	@Test
	public void testNewlineOnly() throws IOException {
		assertEquals("a\nb", filter1("a\nb"));
		assertEquals("a\nb", filter2("a\nb"));
	}

	@Test
	public void testDoubleBegin() throws IOException {
		assertEquals("x", filter1("\\\n\\\nx"));
		assertEquals("x", filter2("\\\n\\\nx"));
	}
}
