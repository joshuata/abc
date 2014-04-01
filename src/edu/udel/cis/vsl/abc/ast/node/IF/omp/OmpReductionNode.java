package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;

public interface OmpReductionNode extends ASTNode {
	public enum OmpReductionNodeKind {
		SYMBOL, FUNCTION
	}

	OmpReductionNodeKind ompReductionOperatorNodeKind();

	SequenceNode<IdentifierExpressionNode> variables();
}
