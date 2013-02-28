package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;

public interface ArrowNode extends ExpressionNode {

	ExpressionNode getStructurePointer();

	void setStructurePointer(ExpressionNode structure);

	IdentifierNode getFieldName();

	void setFieldName(IdentifierNode field);

}
