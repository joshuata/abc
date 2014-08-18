package edu.udel.cis.vsl.abc.token.IF;

public interface CTokenSourceProducer {

	CTokenSource newSource();

	CToken[] getTokens();
}
