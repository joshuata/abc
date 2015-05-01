package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AssignsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonAssignsNode extends CommonASTNode implements AssignsNode {

	public CommonAssignsNode(Source source, SequenceNode<ExpressionNode> child) {
		super(source, (ASTNode) child);
	}

	@Override
	public ContractKind contractKind() {
		return ContractKind.ASSIGNS;
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.CONTRACT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ExpressionNode> getMemoryList() {
		return (SequenceNode<ExpressionNode>) this.child(0);
	}

	@Override
	public AssignsNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void printBody(PrintStream out) {
		// TODO Auto-generated method stub

	}

}
