package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.GuardNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonGuardNode extends CommonASTNode implements GuardNode {

	public CommonGuardNode(Source source, ExpressionNode expression) {
		super(source, expression);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Guard");
	}

	@Override
	public GuardNode copy() {
		return new CommonGuardNode(getSource(), duplicate(getExpression()));
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.CONTRACT;
	}

	@Override
	public ContractKind contractKind() {
		return ContractKind.GUARD;
	}

	@Override
	public ExpressionNode getExpression() {
		return (ExpressionNode) child(0);
	}

}
