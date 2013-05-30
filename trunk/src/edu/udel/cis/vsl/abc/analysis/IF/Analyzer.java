package edu.udel.cis.vsl.abc.analysis.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Analyzer {

	void analyze(AST unit) throws SyntaxException;

	/**
	 * Removes all analysis artifacts added to the translation unit by the
	 * analyze method.
	 * 
	 * @param unit
	 *            a translation unit
	 */
	void clear(AST unit);

}
