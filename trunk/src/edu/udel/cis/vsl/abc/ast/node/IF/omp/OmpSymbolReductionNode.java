package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;

public interface OmpSymbolReductionNode extends
		OmpReductionNode {
	Operator operator();
}
