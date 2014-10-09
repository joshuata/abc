package edu.udel.cis.vsl.abc.ast.node.common.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonForLoopNode extends CommonLoopNode implements ForLoopNode {

	private ForLoopInitializerNode initializer;
	private ExpressionNode condition;
	private ExpressionNode incrementer;

	public CommonForLoopNode(Source source, ExpressionNode condition,
			StatementNode statement, ForLoopInitializerNode initializer,
			ExpressionNode incrementer, ExpressionNode invariant) {
		super(source, LoopKind.FOR, condition, statement, invariant);
		addChild(initializer);
		addChild(incrementer);
		this.initializer = initializer;
		this.condition = condition;
		this.incrementer = incrementer;

	}

	// TODO: currently just assuming for loop initializer has only 2 forms: i =
	// 0 or int i = 0.
	@Override
	public ForLoopInitializerNode getInitializer() {
		return this.initializer;
	}

	@Override
	public ExpressionNode getIncrementer() {
		return (ExpressionNode) this.incrementer;
	}

	@Override
	public ExpressionNode getCondition() {
		return (ExpressionNode) this.condition;
	}

	@Override
	public ForLoopNode copy() {
		return new CommonForLoopNode(getSource(), duplicate(getCondition()),
				duplicate(getBody()), duplicate(getInitializer()),
				duplicate(getIncrementer()), duplicate(getInvariant()));
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.LOOP;
	}
}
