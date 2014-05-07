package edu.udel.cis.vsl.abc.transform.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Combiner {

	/**
	 * Combine two translation units into a single unit.
	 * 
	 * @param unit0
	 *            The first translation unit being combined.
	 * @param unit1
	 *            The second translation unit being combined.
	 * @return The translation unit representing the combination of unit0 and
	 *         unit1.
	 * @throws SyntaxException
	 *             If it encounters an error in the translation units or an
	 *             unhandled case.
	 */
	 AST combine(AST unit0, AST unit1) throws SyntaxException;

}
