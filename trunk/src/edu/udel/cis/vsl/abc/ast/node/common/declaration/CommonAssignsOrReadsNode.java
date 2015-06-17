package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AssignsOrReadsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonAssignsOrReadsNode extends CommonASTNode implements
		AssignsOrReadsNode {

	private boolean isAssigns;

	public CommonAssignsOrReadsNode(Source source, boolean isAssigns,
			ExpressionNode condition, SequenceNode<ExpressionNode> child) {
		super(source, condition, (ASTNode) child);
		this.isAssigns = isAssigns;
	}

	@Override
	public ContractKind contractKind() {
		return ContractKind.ASSIGNS_READS;
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.CONTRACT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ExpressionNode> getMemoryList() {
		return (SequenceNode<ExpressionNode>) this.child(1);
	}

	@Override
	public AssignsOrReadsNode copy() {
		return new CommonAssignsOrReadsNode(this.getSource(), this.isAssigns,
				duplicate(getCondition()), duplicate(getMemoryList()));
	}

	@Override
	protected void printBody(PrintStream out) {
		if (this.isAssigns)
			out.print("$assigns");
		else
			out.print("$reads");
	}

	@Override
	public ExpressionNode getCondition() {
		return (ExpressionNode) this.child(0);
	}

	@Override
	public boolean isAssigns() {
		return this.isAssigns;
	}

	@Override
	public boolean isReads() {
		return !this.isAssigns;
	}

}
