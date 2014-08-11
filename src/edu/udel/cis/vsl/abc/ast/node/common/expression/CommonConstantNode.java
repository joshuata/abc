package edu.udel.cis.vsl.abc.ast.node.common.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.token.IF.Source;

public abstract class CommonConstantNode extends CommonExpressionNode implements
		ConstantNode {

	private String representation;

	public CommonConstantNode(Source source, String representation) {
		super(source);
		this.representation = representation;
	}

	public CommonConstantNode(Source source, String representation, Type type) {
		super(source);
		this.representation = representation;
		setInitialType(type);
	}

	@Override
	public String getStringRepresentation() {
		return representation;
	}

	@Override
	public void setStringRepresentation(String representation) {
		this.representation = representation;
	}

	@Override
	public boolean isConstantExpression() {
		return true;
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.CONSTANT;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		return true;
	}

	@Override
	protected boolean equivWork(ASTNode that) {
		if (that instanceof ConstantNode) {
			ConstantNode thatConst = (ConstantNode) that;
			Value thisValue = this.getConstantValue(), thatValue = ((ConstantNode) that)
					.getConstantValue();

			if (thatConst.constantKind() != this.constantKind())
				return false;
			if (thisValue != null)
				return thisValue.equals(thatValue);
			else if (thatValue != null)
				return false;
			return true;
		}
		return false;
	}
}
