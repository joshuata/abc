package edu.udel.cis.vsl.abc.token.common;

import org.antlr.runtime.Token;

import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.CTokenSequence;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class CTokenSubSequence implements CTokenSequence {

	int startTokenIndex;

	int lastTokenIndex;

	CTokenSource rootSource;

	CToken eofToken;

	/**
	 * A helper class which represents a subsequence of sequence of tokens
	 * through the CTokenSource abstraction.
	 * 
	 * @author siegel
	 * 
	 */
	class SubTokenSource implements CTokenSource {

		int currentTokenIndex = startTokenIndex;

		@Override
		public Token nextToken() {
			if (currentTokenIndex > lastTokenIndex) {
				return eofToken;
			} else {
				Token result = rootSource.getToken(currentTokenIndex);

				currentTokenIndex++;
				return result;
			}
		}

		@Override
		public String getSourceName() {
			return rootSource.getSourceName();
		}

		@Override
		public int getNumTokens() {
			return currentTokenIndex - startTokenIndex;
		}

		@Override
		public CToken getToken(int index) {
			return rootSource.getToken(index);
		}

		@Override
		public TokenFactory getTokenFactory() {
			return rootSource.getTokenFactory();
		}
	}

	public CTokenSubSequence(CTokenSource rootSource, int startTokenIndex,
			int lastTokenIndex) {
		this.rootSource = rootSource;
		this.startTokenIndex = startTokenIndex;
		this.lastTokenIndex = lastTokenIndex;
		this.eofToken = rootSource.getTokenFactory().newCToken(CParser.EOF,
				"EOF", null);
	}

	@Override
	public CTokenSource newSource() {
		return new SubTokenSource();
	}

	@Override
	public CToken[] getTokens() {
		int numTokens = size();
		CToken[] result = new CToken[numTokens];

		for (int i = 0; i < numTokens; i++) {
			result[i] = rootSource.getToken(startTokenIndex + i);
		}
		return result;
	}

	@Override
	public int size() {
		return lastTokenIndex - startTokenIndex + 1;
	}

	@Override
	public CToken getEOFToken() {
		return this.eofToken;
	}
}
