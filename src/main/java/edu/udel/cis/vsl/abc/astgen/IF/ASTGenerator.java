package edu.udel.cis.vsl.abc.astgen.IF;

import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.astgen.c.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.astgen.c.common.CommonASTBuilder;
import edu.udel.cis.vsl.abc.front.c.parse.IF.CParser;

/**
 * A factory class for producing new instances of {@link ASTBuilder} and
 * {@link OmpBuilder} and for using those classes in some common scenarios.
 * 
 * @author siegel
 * 
 */
public class ASTGenerator {

	public static ASTBuilder newASTBuilder(ASTFactory astFactory, CParser parser) {
		return new CommonASTBuilder(astFactory, parser);
	}

}
