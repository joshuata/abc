package edu.udel.cis.vsl.abc.antlr2ast.IF;

import edu.udel.cis.vsl.abc.antlr2ast.common.CommonASTBuilder;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.parse.IF.CParser;

/**
 * A factory class for producing new instances of {@link ASTBuilder} and
 * {@link OmpBuilder} and for using those classes in some common scenarios.
 * 
 * @author siegel
 * 
 */
public class Antlr2AST {

	public static ASTBuilder newASTBuilder(ASTFactory astFactory, CParser parser) {
		return new CommonASTBuilder(astFactory, parser);
	}

}
