package edu.udel.cis.vsl.abc.ast.node.common.statement;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonAssertNode extends CommonStatementNode implements AssertNode {

	public CommonAssertNode(Source source, ExpressionNode condition,
			SequenceNode<ExpressionNode> explanation) {
		super(source, condition, explanation);
	}

	@Override
	public ExpressionNode getCondition() {
		return (ExpressionNode) child(0);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Assert");
	}

	@Override
	public AssertNode copy() {
		return new CommonAssertNode(getSource(), duplicate(getCondition()),
				duplicate(getExplanation()));
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.ASSERT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ExpressionNode> getExplanation() {
		return (SequenceNode<ExpressionNode>) child(1);
	}

	@Override
	public void setCondition(ExpressionNode expr) {
		setChild(0, expr);
	}
}
