package edu.udel.cis.vsl.abc.program.IF;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

/**
 * An abstract representation of a program. This is the highest level
 * representation and the one typically used first in most cases.
 * 
 * @author siegel
 * 
 */
public interface Program {

	/**
	 * Returns the abstract syntax tree of this program. Note that this can
	 * return different values as the program is modified. I.e., a whole new AST
	 * can replace the current one.
	 * 
	 * @return the current AST of the program
	 */
	AST getAST();

	/**
	 * Returns the token factory used for the producing and manipulation of
	 * tokens associated to this Program.
	 * 
	 * @return the token factory
	 */
	TokenFactory getTokenFactory();

	/**
	 * Modifies this program by factoring out expressions with side-effects into
	 * separate statements. The AST may be replaced with a new one.
	 * 
	 * @throws SyntaxException
	 */
	void removeSideEffects() throws SyntaxException;

	/**
	 * If this Program contains a "main" function, this method will remove any
	 * elements which cannot be reached starting from that function. Otherwise,
	 * it does nothing.
	 * 
	 * @throws SyntaxException
	 */
	void prune() throws SyntaxException;

	/**
	 * Prints a human readable abstract representation of the program to the
	 * given output stream. (Not in source code format.)
	 * 
	 * @param out
	 *            a print stream
	 */
	void print(PrintStream out);

	void printSymbolTable(PrintStream out);

}
