package edu.udel.cis.vsl.abc.preproc.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.runtime.ANTLRStringStream;

/**
 * An ANTLR stream which reads from a file and removes any two consecutive
 * characters that are backslash followed by newline. This is part of the C
 * translation process.
 * 
 * @author siegel
 *
 */
public class FilteredANTLRInputStream extends ANTLRStringStream {

	/**
	 * The number of characters to read at one time.
	 */
	public final static int CHUNK_SIZE = 8192;

	/**
	 * Name of this stream, like file name, for example.
	 */
	private String name;

	public FilteredANTLRInputStream(String name, InputStream stream,
			String encoding) throws IOException {
		this.name = name;
		load(stream, encoding);
	}

	public FilteredANTLRInputStream(String name, InputStream stream)
			throws IOException {
		this(name, stream, null);
	}

	public FilteredANTLRInputStream(String name, String string)
			throws IOException {
		this(name, new ByteArrayInputStream(string.getBytes()));
	}

	private void load(InputStreamReader isr) throws IOException {
		BufferedReader br = new BufferedReader(isr, CHUNK_SIZE);
		StringBuilder filteredChars = new StringBuilder(CHUNK_SIZE);
		boolean priorBackslash = false;
		char[] cbuf = new char[CHUNK_SIZE];
		int numFilteredChars;

		while (true) {
			int numChars = br.read(cbuf);

			if (numChars <= 0) {
				if (priorBackslash)
					filteredChars.append('\\');
				break;
			}

			int i;

			if (priorBackslash && cbuf[0] == '\n')
				i = 1;
			else
				i = 0;
			priorBackslash = false;
			while (i < numChars) {
				char c = cbuf[i];

				i++;
				if (c == '\\') {
					if (i < numChars) {
						if (cbuf[i] == '\n') {
							i++;
							continue;
						}
					} else {
						priorBackslash = true;
						break;
					}
				}
				filteredChars.append(c);
			}
		}
		numFilteredChars = filteredChars.length();
		super.n = numFilteredChars;
		super.data = new char[numFilteredChars];
		filteredChars.getChars(0, numFilteredChars, data, 0);
	}

	public void load(InputStream stream, String encoding) throws IOException {
		InputStreamReader isr;

		if (stream == null) {
			return;
		}
		if (encoding != null) {
			isr = new InputStreamReader(stream, encoding);
		} else {
			isr = new InputStreamReader(stream);
		}
		try {
			load(isr);
		} finally {
			isr.close();
		}
	}

	@Override
	public String getSourceName() {
		return name;
	}
}
