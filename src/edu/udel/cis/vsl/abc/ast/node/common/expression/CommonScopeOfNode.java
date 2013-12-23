package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ScopeOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonScopeOfNode extends CommonExpressionNode implements
		ScopeOfNode {

	public CommonScopeOfNode(Source source, SizeableNode argument) {
		super(source, argument);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("ScopeOf");
	}

	@Override
	public boolean isConstantExpression() {
		return true;
	}

	@Override
	public ScopeOfNode copy() {
		return new CommonScopeOfNode(getSource(),
				duplicate(variableExpression()));
	}

	@Override
	public IdentifierExpressionNode variableExpression() {
		return (IdentifierExpressionNode) child(0);
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.SCOPEOF;
	}

}
