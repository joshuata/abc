package edu.udel.cis.vsl.abc.astgen.c.common;

import edu.udel.cis.vsl.abc.astgen.c.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.astgen.c.IF.PragmaFactory;
import edu.udel.cis.vsl.abc.astgen.c.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.front.c.parse.IF.CParser;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseTree;

public class CommonPragmaFactory implements PragmaFactory {

	private ASTBuilder astBuilder;

	private CParser parser;

	public CommonPragmaFactory(ASTBuilder astBuilder, CParser parser) {
		this.astBuilder = astBuilder;
		this.parser = parser;
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
