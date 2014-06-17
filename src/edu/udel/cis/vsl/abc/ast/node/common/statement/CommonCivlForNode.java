package edu.udel.cis.vsl.abc.ast.node.common.statement;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CivlForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonCivlForNode extends CommonStatementNode implements
		CivlForNode {

	private boolean isParallel;

	public CommonCivlForNode(Source source, boolean isParallel,
			SequenceNode<ForLoopInitializerNode> variables,
			ExpressionNode domain, StatementNode body, ExpressionNode invariant) {
		super(source, variables, domain, body);
		addChild(invariant);
		this.isParallel = isParallel;
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.CIVL_FOR;
	}

	@Override
	public boolean isParallel() {
		return isParallel;
	}

	@Override
	public ExpressionNode getDomain() {
		return (ExpressionNode) child(1);
	}

	@Override
	public StatementNode getBody() {
		return (StatementNode) child(2);
	}

	@Override
	public ExpressionNode getInvariant() {
		return (ExpressionNode) child(3);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ForLoopInitializerNode> getVariables() {
		return (SequenceNode<ForLoopInitializerNode>) child(0);
	}

	@Override
	public CivlForNode copy() {
		return new CommonCivlForNode(getSource(), isParallel,
				duplicate(getVariables()), duplicate(getDomain()),
				duplicate(getBody()), duplicate(getInvariant()));
	}

	@Override
	protected void printBody(PrintStream out) {
		if (isParallel)
			out.print("$parfor");
		else
			out.print("$for");
	}

}
