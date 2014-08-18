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

	// public static OmpBuilder newOmpBuilder(ASTBuilder astBuilder) {
	// return new CommonOmpBuilder(astBuilder);
	// }

	// /**
	// * Parses a program to produce an ANTLR tree and then translates that tree
	// * to an AST. This method constructs an {@link ASTBuilder} and uses it to
	// do
	// * the translation. This is mainly intended to help with debugging or
	// * demonstrating how to use ABC.
	// *
	// * @param parser
	// * the parser used to parse a token stream; the token stream
	// * should already be set in the parser
	// * @param astFactory
	// * the AST factory used to construct new AST components
	// * @param out
	// * a stream to which to print the ANTLR tree; if
	// * <code>null</code> it will not be printed
	// * @return the new AST
	// * @throws ParseException
	// * if the program cannot be parsed because of a grammatical
	// * violation
	// * @throws SyntaxException
	// * if the program contains a static error
	// */
	// public static AST build(CParser parser, ASTFactory astFactory,
	// PrintStream out) throws ParseException, SyntaxException {
	// CommonTree rootTree = parser.getTree();
	// CommonASTBuilder builder;
	//
	// if (out != null) {
	// ANTLRUtils.printTree(out, rootTree);
	// out.println();
	// out.flush();
	// }
	// builder = new CommonASTBuilder(parser, astFactory, rootTree);
	// return builder.getTranslationUnit();
	// }
}
