package edu.udel.cis.vsl.abc.ast.node.common.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonForLoopNode extends CommonLoopNode implements ForLoopNode {

	public CommonForLoopNode(Source source, ExpressionNode condition,
			StatementNode statement, ForLoopInitializerNode initializer,
			ExpressionNode incrementer, ExpressionNode invariant) {
		super(source, LoopKind.FOR, condition, statement, invariant);
		addChild(initializer);
		addChild(incrementer);
	}

	@Override
	public ForLoopInitializerNode getInitializer() {
		return (ForLoopInitializerNode) child(3);
	}

	@Override
	public ExpressionNode getIncrementer() {
		return (ExpressionNode) child(4);
	}

	@Override
	public ForLoopNode copy() {
		return new CommonForLoopNode(getSource(), duplicate(getCondition()),
				duplicate(getBody()), duplicate(getInitializer()),
				duplicate(getIncrementer()), duplicate(getInvariant()));
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.STATEMENT;
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.FOR;
	}

}
