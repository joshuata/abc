package edu.udel.cis.vsl.abc.astgen.c.common;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.astgen.c.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.astgen.c.IF.ASTBuilderWorker;
import edu.udel.cis.vsl.abc.astgen.c.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.astgen.c.IF.SimpleScope;
import edu.udel.cis.vsl.abc.front.c.parse.IF.CParser;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.front.c.parse.IF.Parse.RuleKind;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class CIVLPragmaHandler implements PragmaHandler {

	private NodeFactory nodeFactory;

	private CParser parser;

	private ParseTree parseTree;

	ASTBuilderWorker worker;

	public CIVLPragmaHandler(ASTBuilder builder, CParser parser,
			ParseTree parseTree) {
		this.parser = parser;
		this.nodeFactory = builder.getASTFactory().getNodeFactory();
		this.parseTree = parseTree;
		this.worker = builder.getWorker(parseTree);
	}

	@Override
	public EntityKind getEntityKind() {
		return EntityKind.PRAGMA_HANDLER;
	}

	@Override
	public String getName() {
		return "CIVL";
	}

	@Override
	public ASTNode processPragmaNode(PragmaNode pragmaNode, SimpleScope scope)
			throws SyntaxException {
		CTokenSource tokens = pragmaNode.newTokenSource();
		Source source = pragmaNode.getSource();

		try {
			ParseTree pragmaTree = parser.parse(RuleKind.BLOCK_ITEM, tokens,
					scope.getScopeSymbolStack());
			List<BlockItemNode> blockList = worker.translateBlockItem(
					pragmaTree.getRoot(), scope);

			return blockList.size() == 1 ? blockList.get(0) : nodeFactory
					.newCompoundStatementNode(source, blockList);
		} catch (ParseException e) {
			throw new SyntaxException(e.getMessage(), source);
		}
	}

	@Override
	public ParseTree getParseTree() {
		return parseTree;
	}

}
