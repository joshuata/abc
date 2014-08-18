package edu.udel.cis.vsl.abc.parse.common;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.parse.IF.Parse.RuleKind;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.CTokenSourceProducer;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class CommonParseTree implements ParseTree {

	private CTokenSource tokenSource;

	private TokenFactory tokenFactory;

	private CommonTree root;

	private RuleKind kind;

	public CommonParseTree(RuleKind kind, CTokenSource tokenSource,
			CommonTree root) {
		this.tokenSource = tokenSource;
		this.tokenFactory = tokenSource.getTokenFactory();
		this.root = root;
		this.kind = kind;
	}

	@Override
	public CommonTree getRoot() {
		return root;
	}

	@Override
	public Source source(CommonTree tree) {
		CToken firstToken = null, lastToken = null;
		int start = tree.getTokenStartIndex();
		int stop = tree.getTokenStopIndex();

		if (start >= 0)
			firstToken = tokenSource.getToken(start);
		if (stop >= 0)
			lastToken = tokenSource.getToken(stop);
		if (firstToken == null)
			if (lastToken == null)
				throw new IllegalArgumentException(
						"No tokens associated to tree node " + tree);
			else
				firstToken = lastToken;
		else if (lastToken == null)
			lastToken = firstToken;
		return tokenFactory.newSource(firstToken, lastToken);
	}

	@Override
	public SyntaxException newSyntaxException(String message, CommonTree tree) {
		return tokenFactory.newSyntaxException(message, source(tree));
	}

	@Override
	public CTokenSourceProducer getTokenSourceProducer(CommonTree tokenListNode) {
		int numChildren = tokenListNode.getChildCount();

		if (numChildren == 0) {
			return tokenFactory.emptySubTokenSourceProducer(tokenSource);
		} else {
			CToken startToken = (CToken) ((CommonTree) tokenListNode
					.getChild(0)).getToken();
			CToken stopToken = (CToken) ((CommonTree) tokenListNode
					.getChild(numChildren - 1)).getToken();

			return tokenFactory.subTokenSourceProducer(tokenSource, startToken,
					stopToken);
		}
	}

	@Override
	public RuleKind getKind() {
		return kind;
	}

}
