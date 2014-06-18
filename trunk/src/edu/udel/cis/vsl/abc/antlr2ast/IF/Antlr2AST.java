package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.impl.CommonASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.impl.CommonCIVLPragmaBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.impl.CommonOmpBuilder;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

/**
 * A factory class for producing new instances of {@link ASTBuilder} and
 * {@link OmpBuilder} and for using those classes in some common scenarios.
 * 
 * @author siegel
 * 
 */
public class Antlr2AST {

	/**
	 * Returns a new ASTBuilder for the given ANTLR tree.
	 * 
	 * @param parser
	 *            the CParser used to create the ANTLR tree
	 * @param astFactory
	 *            the AST factory to use to construct the new AST components
	 * @param rootTree
	 *            the root of the ANTLR tree which is being translated
	 * @return the new ASTBuilder
	 */
	public static ASTBuilder newASTBuilder(CParser parser,
			ASTFactory astFactory, CommonTree rootTree) {
		return new CommonASTBuilder(parser, astFactory, rootTree);
	}

	/**
	 * Returns a new OmpBuilder.
	 * 
	 * @param astFactory
	 *            the AST factory to use to construct the new AST components.
	 * @param astBuilder
	 *            the AST builder to be reused.
	 * @return the new OmpBuilder
	 */
	public static OmpBuilder newOmpBuilder(ASTFactory astFactory,
			ASTBuilder astBuilder) {
		return new CommonOmpBuilder(astFactory, astBuilder);
	}

	/**
	 * Returns a new CIVLPragmaBuilder.
	 * 
	 * @param astBuilder
	 *            the AST builder to be reused.
	 * @return the new OmpBuilder
	 */
	public static CIVLPragmaBuilder newCIVLPragmaBuilder(ASTBuilder astBuilder) {
		return new CommonCIVLPragmaBuilder(astBuilder);
	}

	/**
	 * Parses a program to produce an ANTLR tree and then translates that tree
	 * to an AST. This method constructs an {@link ASTBuilder} and uses it to do
	 * the translation. This is mainly intended to help with debugging or
	 * demonstrating how to use ABC.
	 * 
	 * @param parser
	 *            the parser used to parse a token stream; the token stream
	 *            should already be set in the parser
	 * @param astFactory
	 *            the AST factory used to construct new AST components
	 * @param out
	 *            a stream to which to print the ANTLR tree; if
	 *            <code>null</code> it will not be printed
	 * @return the new AST
	 * @throws ParseException
	 *             if the program cannot be parsed because of a grammatical
	 *             violation
	 * @throws SyntaxException
	 *             if the program contains a static error
	 */
	public static AST build(CParser parser, ASTFactory astFactory,
			PrintStream out) throws ParseException, SyntaxException {
		CommonTree rootTree = parser.getTree();
		CommonASTBuilder builder;

		if (out != null) {
			ANTLRUtils.printTree(out, rootTree);
			out.println();
			out.flush();
		}
		builder = new CommonASTBuilder(parser, astFactory, rootTree);
		return builder.getTranslationUnit();
	}
}
