package edu.udel.cis.vsl.abc.ast.node.common.statement;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

/**
 * An "atomic" statement has the form <code>$atomic body</code> or
 * <code>$datomic body</code>, where body is a list of statements.
 * 
 * @author zheng
 * 
 */

public class CommonAtomicNode extends CommonASTNode implements AtomicNode {

	/**
	 * True iff the atomic node is deterministic, i.e., starting with
	 * <code>$datomic</code>; otherwise, it is general atomic node starting with
	 * <code>$atomic</code>.
	 */
	private boolean isDeterministic = false;

	/**
	 * Constructor
	 * 
	 * @param source
	 * @param body
	 */
	public CommonAtomicNode(Source source, boolean deterministic,
			StatementNode body) {
		super(source, body);
		this.isDeterministic = deterministic;
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

		return new CommonAtomicNode(getSource(), this.isDeterministic, bodyCopy);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("atomic");
	}

	@Override
	public boolean isDeterministic() {
		return this.isDeterministic;
	}
}
