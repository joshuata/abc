package edu.udel.cis.vsl.abc.antlr2ast.common;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilderWorker;
import edu.udel.cis.vsl.abc.antlr2ast.IF.PragmaFactory;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class CommonASTBuilder implements ASTBuilder {

	private ASTFactory astFactory;

	private PragmaFactory pragmaFactory;

	public CommonASTBuilder(ASTFactory astFactory, CParser parser) {
		this.astFactory = astFactory;
		pragmaFactory = new CommonPragmaFactory(this, parser);
	}

	@Override
	public AST getTranslationUnit(ParseTree tree) throws SyntaxException {
		ASTBuilderWorker worker = getWorker(tree);
		SequenceNode<BlockItemNode> rootNode = worker.translateRoot();
		AST ast = astFactory.newAST(rootNode, tree.getSourceFiles());

		return ast;
	}

	@Override
	public ASTBuilderWorker getWorker(ParseTree tree) {
		return new CommonASTBuilderWorker(tree, astFactory, pragmaFactory);
	}

	@Override
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	@Override
	public PragmaFactory getPragmaFactory() {
		return pragmaFactory;
	}
}
