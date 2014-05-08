package edu.udel.cis.vsl.abc.program.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface ProgramFactory {

	ASTFactory getASTFactory();

	Program newProgram(AST ast) throws SyntaxException;

}
