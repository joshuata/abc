package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

/**
 * A "requires" clause in a function contract.
 * 
 * @author siegel
 * 
 */
public interface RequiresNode extends ContractNode {

	ExpressionNode getExpression();

	@Override
	RequiresNode copy();

}
