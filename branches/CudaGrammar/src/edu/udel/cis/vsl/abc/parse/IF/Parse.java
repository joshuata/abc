package edu.udel.cis.vsl.abc.parse.IF;

import edu.udel.cis.vsl.abc.parse.common.CommonCParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;

/**
 * The entry point for the parse module, this class provides static method(s)
 * only to produce new instances of {@link CParser}. This is a simple
 * implementation of the Factory Pattern.
 * 
 * @author siegel
 * 
 */
public class Parse {

	public static enum RuleKind {
		TRANSLATION_UNIT, EXTERNAL_DEFINITION, BLOCK_ITEM
	}

	/**
	 * Creates a new instance of a {@link CParser} using the given source of
	 * tokens. It is unspecified whether the parsing process will begin
	 * immediately with the creation of the new parser, or whether it will begin
	 * only when some other method in the parser is invoked.
	 * 
	 * @param source
	 *            the token source, an abstraction specifying the sequence of
	 *            {@link CToken}s that are to be parsed
	 * @return the new {@link CParser}
	 */
	public static CParser newCParser() {
		return new CommonCParser();
	}

	// public static CParser newCParser(RuleKind rule, CTokenSource source,
	// int startTokenIndex, int lastTokenIndex) {
	// return new CommonCParser(rule, source, startTokenIndex, lastTokenIndex);
	// }
}
