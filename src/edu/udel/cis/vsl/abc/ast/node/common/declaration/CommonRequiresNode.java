package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.RequiresNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonRequiresNode extends CommonASTNode implements RequiresNode {

	public CommonRequiresNode(Source source, ExpressionNode expression) {
		super(source, expression);
	}

	@Override
	public ExpressionNode getExpression() {
		return (ExpressionNode) child(0);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Requires");
	}

	@Override
	public RequiresNode copy() {
		return new CommonRequiresNode(getSource(), duplicate(getExpression()));
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.REQUIRES;
	}

}
