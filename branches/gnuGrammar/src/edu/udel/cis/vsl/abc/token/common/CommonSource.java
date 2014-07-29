package edu.udel.cis.vsl.abc.token.common;

import java.io.PrintStream;
import java.util.Iterator;

import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.TokenUtils;

public class CommonSource implements Source {

	private CToken firstToken;

	private CToken lastToken;

	public CommonSource(CToken firstToken, CToken lastToken) {
		if (firstToken == null)
			throw new IllegalArgumentException("Null firstToken");
		if (lastToken == null)
			throw new IllegalArgumentException("Null lastToken");

		int first = firstToken.getIndex();
		int last = lastToken.getIndex();

		if (last < first)
			throw new IllegalArgumentException("Last token precedes first:\n"
					+ firstToken + "\n" + lastToken);
		this.firstToken = firstToken;
		this.lastToken = lastToken;
	}

	@Override
	public CToken getFirstToken() {
		return firstToken;
	}

	@Override
	public CToken getLastToken() {
		return lastToken;
	}

	@Override
	public Iterator<CToken> tokens() {
		return new SourceTokenIterator(firstToken, lastToken);
	}

	@Override
	public int getNumTokens() {
		return lastToken.getIndex() - firstToken.getIndex() + 1;
	}

	@Override
	public String toString() {
		return getSummary(false);
	}

	@Override
	public void print(PrintStream out) {
		out.print(getSummary(false));
	}

	@Override
	public String getLocation(boolean abbreviated) {
		return TokenUtils.summarizeRangeLocation(firstToken, lastToken,
				abbreviated);
	}

	@Override
	public String getSummary(boolean abbreviated) {
		return TokenUtils.summarizeRange(firstToken, lastToken, abbreviated);
	}
}

class SourceTokenIterator implements Iterator<CToken> {
	CToken nextToken;
	CToken last;

	SourceTokenIterator(CToken firstToken, CToken lastToken) {
		nextToken = firstToken;
		last = lastToken;
	}

	public CToken next() {
		CToken result = nextToken;

		if (nextToken == last)
			nextToken = null;
		else
			nextToken = nextToken.getNext();
		return result;
	}

	public boolean hasNext() {
		return nextToken != null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
