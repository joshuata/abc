package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DependsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonDependsNode extends CommonASTNode implements DependsNode {

	public CommonDependsNode(Source source, ExpressionNode condition,
			SequenceNode<ExpressionNode> eventList) {
		super(source, condition, eventList);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("Depends");
	}

	@Override
	public DependsNode copy() {
		return new CommonDependsNode(getSource(), duplicate(getCondition()),
				duplicate(getEventList()));
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.CONTRACT;
	}

	@Override
	public ContractKind contractKind() {
		return ContractKind.DEPENDS;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ExpressionNode> getEventList() {
		return (SequenceNode<ExpressionNode>) child(1);
	}

	@Override
	public ExpressionNode getCondition() {
		return (ExpressionNode) child(0);
	}

}
