package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.impl.CommonASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.impl.CommonOmpBuilder;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.IF.ASTs;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.Nodes;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.Types;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.Values;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.Tokens;
import edu.udel.cis.vsl.abc.util.IF.ANTLRUtils;

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

	public static AST translate(CParser parser, CommonTree rootTree)
			throws SyntaxException {
		TypeFactory typeFactory = Types.newTypeFactory();
		ValueFactory valueFactory = Values.newValueFactory(typeFactory);
		NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
				valueFactory);
		TokenFactory sourceFactory = Tokens.newTokenFactory();
		ASTFactory astFactory = ASTs.newASTFactory(nodeFactory, sourceFactory,
				typeFactory);
		CommonASTBuilder builder = new CommonASTBuilder(parser, astFactory,
				rootTree);

		return builder.getTranslationUnit();
	}

	public static AST build(CParser parser, ASTFactory unitFactory,
			PrintStream out) throws ParseException, SyntaxException {
		CommonTree rootTree = parser.getTree();
		CommonASTBuilder builder;

		if (out != null) {
			ANTLRUtils.printTree(out, rootTree);
			out.println();
			out.flush();
		}
		builder = new CommonASTBuilder(parser, unitFactory, rootTree);
		return builder.getTranslationUnit();
	}

	public static AST buildAST(CParser parser, PrintStream out)
			throws ParseException, SyntaxException {
		TypeFactory typeFactory = Types.newTypeFactory();
		ValueFactory valueFactory = Values.newValueFactory(typeFactory);
		NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
				valueFactory);
		TokenFactory sourceFactory = Tokens.newTokenFactory();
		ASTFactory unitFactory = ASTs.newASTFactory(nodeFactory, sourceFactory,
				typeFactory);

		return build(parser, unitFactory, out);
	}

	public static void buildAndPrintAST(PrintStream out, CParser parser)
			throws ParseException, SyntaxException {
		AST tu = buildAST(parser, out);

		tu.print(out);
	}
}
