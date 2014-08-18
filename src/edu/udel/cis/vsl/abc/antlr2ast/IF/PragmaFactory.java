package edu.udel.cis.vsl.abc.antlr2ast.IF;

import edu.udel.cis.vsl.abc.parse.IF.ParseTree;

public interface PragmaFactory {

	ASTBuilder getASTBuilder();

	PragmaHandler newHandler(String code, ParseTree parseTree);

}
