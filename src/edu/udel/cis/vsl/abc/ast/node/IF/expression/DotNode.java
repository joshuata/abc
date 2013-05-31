package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;

public interface DotNode extends ExpressionNode {

	ExpressionNode getStructure();

	void setStructure(ExpressionNode structure);

	IdentifierNode getFieldName();

	void setFieldName(IdentifierNode field);

	@Override
	DotNode copy();

}
