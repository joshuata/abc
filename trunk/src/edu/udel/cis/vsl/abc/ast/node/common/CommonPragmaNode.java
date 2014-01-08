package edu.udel.cis.vsl.abc.ast.node.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.parse.common.CivlCParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.TokenUtils;

public class CommonPragmaNode extends CommonASTNode implements PragmaNode {

	private ArrayList<CToken> body;

	private CToken eofToken;

	public CommonPragmaNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier);
		this.body = new ArrayList<CToken>(body);
		this.eofToken = eofToken;
		assert eofToken.getType() == CivlCParser.EOF;
	}

	@Override
	protected void printBody(PrintStream out) {
		boolean first = true;

		out.print("Pragma[");
		for (CToken token : body) {
			if (first)
				first = false;
			else
				out.print(" ");
			out.print(token.getText());
		}
		out.print("]");
	}

	@Override
	public IdentifierNode getPragmaIdentifier() {
		return (IdentifierNode) child(0);
	}

	@Override
	public int getNumTokens() {
		return body.size();
	}

	@Override
	public CToken getToken(int index) {
		return body.get(index);
	}

	@Override
	public Iterator<CToken> getTokens() {
		return body.iterator();
	}

	@Override
	public TokenSource getTokenSource() {
		TokenSource tokenSource = new GenericTokenSource(
				TokenUtils.getShortFilename(getSource().getFirstToken(), false),
				body, eofToken);

		return tokenSource;
	}

	@Override
	public PragmaNode copy() {
		@SuppressWarnings("unchecked")
		ArrayList<CToken> bodyCopy = body == null ? null
				: (ArrayList<CToken>) body.clone();
		IdentifierNode identifier = getPragmaIdentifier();
		IdentifierNode identifierCopy = identifier == null ? null : identifier
				.copy();

		return new CommonPragmaNode(getSource(), identifierCopy, bodyCopy,
				eofToken);
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.PRAGMA;
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.PRAGMA;
	}
}

class GenericTokenSource implements TokenSource {
	private Iterator<CToken> tokenIterator;

	private CToken nextToken;

	private CToken eofToken;

	private String sourceName;

	public GenericTokenSource(String sourceName, Iterable<CToken> tokens,
			CToken eofToken) {
		this.tokenIterator = tokens.iterator();
		this.eofToken = eofToken;
		this.sourceName = sourceName;
		if (tokenIterator.hasNext())
			nextToken = tokenIterator.next();
		else
			nextToken = eofToken;
	}

	@Override
	public Token nextToken() {
		CToken result = nextToken;

		if (tokenIterator.hasNext())
			nextToken = tokenIterator.next();
		else
			nextToken = eofToken;
		return result;
	}

	@Override
	public String getSourceName() {
		return sourceName;
	}

}
