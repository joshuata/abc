package edu.udel.cis.vsl.abc.program.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

public interface ProgramFactory {

	ASTFactory getASTFactory();

	Program newProgram(AST ast, SymbolicUniverse universe)
			throws SyntaxException;

}
