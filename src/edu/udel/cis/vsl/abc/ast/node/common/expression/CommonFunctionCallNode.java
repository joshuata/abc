package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonFunctionCallNode extends CommonExpressionNode implements
		FunctionCallNode {

	public CommonFunctionCallNode(Source source, ExpressionNode function,
			SequenceNode<ExpressionNode> arguments) {
		super(source, function, arguments);
	}

	@Override
	public ExpressionNode getFunction() {
		return (ExpressionNode) child(0);
	}

	@Override
	public void setFunction(ExpressionNode function) {
		setChild(0, function);
	}

	@Override
	public int getNumberOfArguments() {
		return child(1).numChildren();
	}

	@Override
	public ExpressionNode getArgument(int index) {
		return (ExpressionNode) child(1).child(index);
	}

	@Override
	public void setArgument(int index, ExpressionNode value) {
		((CommonASTNode) child(1)).setChild(index, value);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("FunctionCall");
	}

	@Override
	public boolean isConstantExpression() {
		return false;
	}

	@Override
	public FunctionCallNode copy() {
		@SuppressWarnings("unchecked")
		SequenceNode<ExpressionNode> arguments = (SequenceNode<ExpressionNode>) child(1);

		return new CommonFunctionCallNode(getSource(),
				duplicate(getFunction()), duplicate(arguments));
	}

}
