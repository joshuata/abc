package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

/**
 * A quantified expression consists of a quantifier, a variable bound by the
 * quantifier, an expression restricting the values of the quantified variable,
 * and a quantified expression. e.g. forall {int x | x > 1} x > 0;
 * 
 * @author zirkel
 * 
 */
public class CommonQuantifiedExpressionNode extends CommonExpressionNode
		implements QuantifiedExpressionNode {

	private Quantifier quantifier;
	private VariableDeclarationNode variable;
	private ExpressionNode restriction;
	private boolean isRange;
	private ExpressionNode lower;
	private ExpressionNode upper;
	private ExpressionNode expression;

	/**
	 * @param source
	 *            The source code information for this expression.
	 * @param quantifier
	 *            The quantifier for this expression. One of {FORALL, EXISTS,
	 *            UNIFORM}.
	 * @param variable
	 *            The quantified variable.
	 * @param restriction
	 *            Boolean-valued expression
	 */
	public CommonQuantifiedExpressionNode(Source source, Quantifier quantifier,
			VariableDeclarationNode variable, ExpressionNode restriction,
			ExpressionNode expression) {
		super(source, variable, restriction, expression);
		this.quantifier = quantifier;
		this.variable = variable;
		this.restriction = restriction;
		this.expression = expression;
		this.lower = null;
		this.upper = null;
		isRange = false;
	}

	/**
	 * @param source
	 *            The source code information for this expression.
	 * @param quantifier
	 *            The quantifier for this expression. One of {FORALL, EXISTS,
	 *            UNIFORM}.
	 * @param variable
	 *            The quantified variable.
	 * @param lower
	 *            Integer-valued expression for the lower bound on the
	 *            quantified variable.
	 * @param upper
	 *            Integer-valued expression for the upper bound on the
	 *            quantified variable.
	 */
	public CommonQuantifiedExpressionNode(Source source, Quantifier quantifier,
			VariableDeclarationNode variable, ExpressionNode lower,
			ExpressionNode upper, ExpressionNode expression) {
		super(source, variable, upper, expression);
		this.quantifier = quantifier;
		this.variable = variable;
		this.lower = lower;
		this.upper = upper;
		this.expression = expression;
		this.restriction = null;
		isRange = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode#
	 * isConstantExpression()
	 */
	@Override
	public boolean isConstantExpression() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode#copy()
	 */
	@Override
	public ExpressionNode copy() {
		if (isRange()) {
			return new CommonQuantifiedExpressionNode(this.getSource(),
					quantifier, variable.copy(), lower.copy(), upper.copy(),
					expression.copy());
		}
		return new CommonQuantifiedExpressionNode(this.getSource(), quantifier,
				variable.copy(), restriction.copy(), expression.copy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode#
	 * quantifier()
	 */
	@Override
	public Quantifier quantifier() {
		return quantifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode#
	 * variable()
	 */
	@Override
	public VariableDeclarationNode variable() {
		return variable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode#
	 * restriction()
	 */
	@Override
	public ExpressionNode restriction() {
		return restriction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode#
	 * expression()
	 */
	@Override
	public ExpressionNode expression() {
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode#printBody(java.io.
	 * PrintStream)
	 */
	@Override
	protected void printBody(PrintStream out) {
		String output = "";

		switch (quantifier) {
		case FORALL:
			output = "forall";
			break;
		case EXISTS:
			output = "exists";
			break;
		case UNIFORM:
			output = "uniform";
			break;
		}
		out.print(output);
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.QUANTIFIED_EXPRESSION;
	}

	@Override
	public boolean isRange() {
		return isRange;
	}

	@Override
	public ExpressionNode lower() {
		return lower;
	}

	@Override
	public ExpressionNode upper() {
		return upper;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		boolean result = expression().isSideEffectFree(errorsAreSideEffects);

		if (restriction == null) {
			result = result && lower.isSideEffectFree(errorsAreSideEffects)
					&& upper.isSideEffectFree(errorsAreSideEffects);
		} else {
			result = result
					&& restriction.isSideEffectFree(errorsAreSideEffects);
		}
		return result;
	}
	
	
}
