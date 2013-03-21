package edu.udel.cis.vsl.abc.antlr2ast;

import java.io.File;
import java.io.PrintStream;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.impl.ASTBuilder;
import edu.udel.cis.vsl.abc.ast.node.Nodes;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.Types;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.unit.Units;
import edu.udel.cis.vsl.abc.ast.unit.IF.TranslationUnit;
import edu.udel.cis.vsl.abc.ast.unit.IF.UnitFactory;
import edu.udel.cis.vsl.abc.ast.value.Values;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.parse.Parse;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.token.Tokens;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.util.ANTLRUtils;

public class Antlr2AST {

	public static TranslationUnit translate(CParser parser, CommonTree rootTree)
			throws SyntaxException {
		TypeFactory typeFactory = Types.newTypeFactory();
		ValueFactory valueFactory = Values.newValueFactory(typeFactory);
		NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
				valueFactory);
		TokenFactory sourceFactory = Tokens.newTokenFactory();
		UnitFactory unitFactory = Units.newUnitFactory(nodeFactory,
				sourceFactory, typeFactory);
		ASTBuilder builder = new ASTBuilder(parser, unitFactory, rootTree);

		return builder.getTranslationUnit();
	}

	public static TranslationUnit build(CParser parser,
			UnitFactory unitFactory, PrintStream out) throws ParseException,
			SyntaxException {
		CommonTree rootTree = parser.getTree();
		ASTBuilder builder;

		if (out != null) {
			ANTLRUtils.printTree(out, rootTree);
			out.println();
			out.flush();
		}
		builder = new ASTBuilder(parser, unitFactory, rootTree);
		return builder.getTranslationUnit();
	}

	public static TranslationUnit buildAST(CParser parser, PrintStream out)
			throws ParseException, SyntaxException {
		TypeFactory typeFactory = Types.newTypeFactory();
		ValueFactory valueFactory = Values.newValueFactory(typeFactory);
		NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
				valueFactory);
		TokenFactory sourceFactory = Tokens.newTokenFactory();
		UnitFactory unitFactory = Units.newUnitFactory(nodeFactory,
				sourceFactory, typeFactory);

		return build(parser, unitFactory, out);
	}

	public static void buildAndPrintAST(PrintStream out, CParser parser)
			throws ParseException, SyntaxException {
		TranslationUnit tu = buildAST(parser, out);

		tu.print(out);
	}

	public static void main(String[] args) throws PreprocessorException,
			ParseException, SyntaxException {
		String filename = args[0];
		File file = new File(filename);
		Preprocessor preprocessor = Preprocess.newPreprocessorFactory()
				.newPreprocessor();
		CParser parser = Parse.newCParser(preprocessor, file);

		buildAndPrintAST(System.out, parser);
	}

}
