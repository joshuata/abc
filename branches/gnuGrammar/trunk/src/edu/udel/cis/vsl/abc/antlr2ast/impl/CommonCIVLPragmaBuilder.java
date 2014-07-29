package edu.udel.cis.vsl.abc.antlr2ast.impl;

import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.CIVLPragmaBuilder;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.Parse;
import edu.udel.cis.vsl.abc.parse.IF.Parse.RuleKind;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class CommonCIVLPragmaBuilder implements CIVLPragmaBuilder {

	/**
	 * The AST builder to be used to create expressions.
	 */
	private ASTBuilder astBuilder;

	/**
	 * Constructs a new CIVL pragma builder for the given ANTLR tree of CIVL
	 * pragmas.
	 * 
	 * @param astBuilder
	 *            the AST builder to be reused.
	 * 
	 */
	public CommonCIVLPragmaBuilder(ASTBuilder astBuilder) {
		this.astBuilder = astBuilder;
	}

	@Override
	public List<BlockItemNode> getBlockItem(Iterator<CToken> ctokens)
			throws ParseException, SyntaxException {
		CommonTree tree = this.getTree(ctokens, RuleKind.BLOCK_ITEM);

		return astBuilder.translateBlockItemNode(tree, null);
	}

	private CommonTree getTree(Iterator<CToken> ctokens, RuleKind type)
			throws ParseException {
		CTokenSource tokenSource = astBuilder.getCParser().getTokenSource();
		CParser cparser;
		int start = -1, end = -1;

		while (ctokens.hasNext()) {
			CToken current = ctokens.next();

			if (start == -1)
				start = current.getIndex();
			end = current.getIndex();
		}
		cparser = Parse.newCParser(type, tokenSource, start, end);
		return cparser.getTree();
	}

}
