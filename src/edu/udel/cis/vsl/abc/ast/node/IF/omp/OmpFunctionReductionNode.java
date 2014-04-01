package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;

public interface OmpFunctionReductionNode extends OmpReductionNode {
	IdentifierExpressionNode function();
}
