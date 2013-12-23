package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonSizeofNode extends CommonExpressionNode implements
		SizeofNode {

	public CommonSizeofNode(Source source, SizeableNode argument) {
		super(source, argument);
	}

	@Override
	public SizeableNode getArgument() {
		return (SizeableNode) child(0);
	}

	@Override
	public void setArgument(SizeableNode argument) {
		setChild(0, argument);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("SizeOf");
	}

	@Override
	public boolean isConstantExpression() {
		return !getArgument().getType().isVariablyModified();
	}

	@Override
	public SizeofNode copy() {
		return new CommonSizeofNode(getSource(), duplicate(getArgument()));
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.SIZEOF;
	}
}
