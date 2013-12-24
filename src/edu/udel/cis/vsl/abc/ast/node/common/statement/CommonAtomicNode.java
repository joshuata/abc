package edu.udel.cis.vsl.abc.ast.node.common.statement;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

/**
 * An "atomic" statement has the form "$atomic body", where body is a list of
 * statements.
 * 
 * @author zheng
 * 
 */

public class CommonAtomicNode extends CommonASTNode implements AtomicNode {

	/**
	 * Constructor
	 * 
	 * @param source
	 * @param body
	 */
	public CommonAtomicNode(Source source, StatementNode body) {
		super(source, body);
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.ATOMIC;
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.STATEMENT;
	}

	@Override
	public StatementNode getBody() {
		return (StatementNode) child(0);
	}

	@Override
	public AtomicNode copy() {
		StatementNode body = getBody();
		StatementNode bodyCopy = body == null ? null : body.copy();

		return new CommonAtomicNode(getSource(), bodyCopy);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Atomic");
	}
}
