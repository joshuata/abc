package edu.udel.cis.vsl.abc.antlr2ast.IF;

import edu.udel.cis.vsl.abc.parse.IF.ParseTree;

public interface PragmaFactory {

	ASTBuilder getASTBuilder();

	// may need more info?
	PragmaHandler newHandler(String code, ParseTree parseTree);

}
