package edu.udel.cis.vsl.abc.ast.node.common.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonReturnNode extends CommonJumpNode implements ReturnNode {

	public CommonReturnNode(Source source, ExpressionNode expression) {
		super(source, JumpKind.RETURN);
		addChild(expression);
	}

	@Override
	public ExpressionNode getExpression() {
		return (ExpressionNode) child(0);
	}

}
