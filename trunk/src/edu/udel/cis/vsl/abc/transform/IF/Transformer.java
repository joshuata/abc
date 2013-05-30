package edu.udel.cis.vsl.abc.transform.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Transformer {

	/**
	 * Apply a transformation to a translation unit.
	 * 
	 * @param unit
	 *            The translation unit being modified.
	 * @throws SyntaxException
	 *             If it encounters an error in the translation unit or an
	 *             unhandled case.
	 */
	public AST transform(AST unit) throws SyntaxException;

}
