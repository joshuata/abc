package edu.udel.cis.vsl.abc.ast.node.IF.expression;

/**
 * See C11 Sec. 6.5.1.1.
 * 
 * @author siegel
 * 
 */
public interface GenericSelectionNode extends ExpressionNode {
	// TODO: requires some work

	@Override
	FunctionCallNode copy();
}
