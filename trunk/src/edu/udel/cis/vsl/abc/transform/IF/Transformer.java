package edu.udel.cis.vsl.abc.transform.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Transformer {

	/**
	 * Returns the short name of this transformer, e.g., "mpi", or "prune".
	 * 
	 * @return the short name ("code") of this transformer
	 */
	String getCode();

	/**
	 * A brief (one line) description of what this transformer does, suitable
	 * for appearing in a help message.
	 * 
	 * @return a short description of what this transformer does
	 */
	String getShortDescription();

	/**
	 * Returns the long name of this transformer, e.g., "MPI Transformer" or
	 * "Pruner". The name is suitable for human consumption.
	 * 
	 * @return long name of this transformer
	 */
	@Override
	String toString();

	/**
	 * Apply a transformation to a translation unit.
	 * 
	 * @param ast
	 *            The abstract syntax tree being modified.
	 * @throws SyntaxException
	 *             If it encounters an error in the AST or an some case which it
	 *             cannot handle
	 */
	AST transform(AST ast) throws SyntaxException;

}
