package edu.udel.cis.vsl.abc.ast.node.common.expression;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;

public interface ProcnullNode extends ConstantNode {
	@Override
	ProcnullNode copy();
}
