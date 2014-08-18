package edu.udel.cis.vsl.abc.antlr2ast.common;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.PragmaFactory;
import edu.udel.cis.vsl.abc.antlr2ast.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;

public class CommonPragmaFactory implements PragmaFactory {

	private ASTBuilder astBuilder;

	private CParser parser;

	public CommonPragmaFactory(ASTBuilder astBuilder, CParser parser) {
		this.astBuilder = astBuilder;
	}

	@Override
	public PragmaHandler newHandler(String code, ParseTree parseTree) {
		switch (code) {
		case "CIVL":
			return new CIVLPragmaHandler(astBuilder, parser, parseTree);
		case "omp":
			return new OMPPragmaHandler(astBuilder, parseTree);
		default:
			return new TrivialPragmaHandler(code, parseTree);
		}
	}

	@Override
	public ASTBuilder getASTBuilder() {
		return astBuilder;
	}

}
