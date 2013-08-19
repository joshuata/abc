package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface AssumeNode extends StatementNode, ExternalDefinitionNode {

	/**
	 * The argument to the assume statement.
	 * 
	 * @return the assumed expression
	 */
	ExpressionNode getExpression();

	@Override
	AssumeNode copy();
}
