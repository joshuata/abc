package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DependsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonDependsNode extends CommonASTNode implements DependsNode {

	public CommonDependsNode(Source source, ExpressionNode expression) {
		super(source, expression);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Depends");
	}

	@Override
	public DependsNode copy() {
		return new CommonDependsNode(getSource(), duplicate(getExpression()));
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.CONTRACT;
	}

	@Override
	public ContractKind contractKind() {
		return ContractKind.DEPENDS;
	}

	@Override
	public ExpressionNode getExpression() {
		return (ExpressionNode) child(0);
	}

}
