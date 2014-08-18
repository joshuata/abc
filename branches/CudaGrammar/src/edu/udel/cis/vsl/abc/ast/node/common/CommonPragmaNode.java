package edu.udel.cis.vsl.abc.ast.node.common;

import java.io.PrintStream;
import java.util.Arrays;

import edu.udel.cis.vsl.abc.ast.IF.DifferenceObject;
import edu.udel.cis.vsl.abc.ast.IF.DifferenceObject.DiffKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.parse.common.CivlCParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.CTokenSourceProducer;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonPragmaNode extends CommonASTNode implements PragmaNode {

	protected CToken[] body;

	protected CToken eofToken;

	protected CTokenSourceProducer bodyTokenSourceProducer;

	public CommonPragmaNode(Source source, IdentifierNode identifier,
			CTokenSourceProducer bodyTokenSourceProducer, CToken eofToken) {
		super(source, identifier);
		this.bodyTokenSourceProducer = bodyTokenSourceProducer;
		this.eofToken = eofToken;
		assert eofToken.getType() == CivlCParser.EOF;
		body = bodyTokenSourceProducer.getTokens();
	}

	@Override
	protected void printBody(PrintStream out) {
		int numTokens = body.length;

		out.print("Pragma[");
		for (int i = 0; i < numTokens; i++) {
			CToken token = body[i];

			if (i > 0)
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
		return body.length;
	}

	@Override
	public CToken getToken(int index) {
		return body[index];
	}

	@Override
	public Iterable<CToken> getTokens() {
		return Arrays.asList(body);
	}

	@Override
	public PragmaNode copy() {
		IdentifierNode identifier = getPragmaIdentifier();
		IdentifierNode identifierCopy = identifier == null ? null : identifier
				.copy();

		return new CommonPragmaNode(getSource(), identifierCopy,
				bodyTokenSourceProducer, eofToken);
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.PRAGMA;
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.PRAGMA;
	}

	@Override
	public BlockItemKind blockItemKind() {
		return BlockItemKind.PRAGMA;
	}

	@Override
	protected DifferenceObject diffWork(ASTNode that) {
		if (that instanceof PragmaNode) {
			PragmaNode thatPragma = (PragmaNode) that;
			int numTokens = this.getNumTokens();

			if (numTokens != thatPragma.getNumTokens())
				return new DifferenceObject(this, that,
						DiffKind.PRAGMA_NUM_TOKENS);
			for (int i = 0; i < numTokens; i++) {
				String thisToken = this.getToken(i).getText(), thatToken = thatPragma
						.getToken(i).getText();

				if (!thisToken.equals(thatToken))
					return new DifferenceObject(this, that, DiffKind.OTHER,
							"the " + i + " token is different: " + thisToken
									+ " vs " + thatToken);
			}
			return null;
		}
		return new DifferenceObject(this, that);
	}

	@Override
	public CTokenSource newTokenSource() {
		return bodyTokenSourceProducer.newSource();
	}
}
