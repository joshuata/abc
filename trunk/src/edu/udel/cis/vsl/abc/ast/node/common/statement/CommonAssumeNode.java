package edu.udel.cis.vsl.abc.ast.node.common.statement;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonAssumeNode extends CommonStatementNode implements AssumeNode {

	public CommonAssumeNode(Source source, ExpressionNode expression) {
		super(source, expression);
	}

	@Override
	public ExpressionNode getExpression() {
		return (ExpressionNode) child(0);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Assume");
	}

	@Override
	public AssumeNode copy() {
		return new CommonAssumeNode(getSource(), duplicate(getExpression()));
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.ASSUME;
	}

	@Override
	public void setExpression(ExpressionNode expr) {
		setChild(0, expr);
	}
}
