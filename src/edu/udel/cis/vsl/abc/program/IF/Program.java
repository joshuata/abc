package edu.udel.cis.vsl.abc.program.IF;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Program {

	AST getAST();

	void removeSideEffects() throws SyntaxException;

	void prune() throws SyntaxException;

	void print(PrintStream out);

	void printSymbolTable(PrintStream out);

}
