package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;

public interface OmpDeclarativeNode extends OmpNode {
	public enum OmpDeclarativeNodeKind {
		THREADPRIVATE
	}

	OmpDeclarativeNodeKind ompDeclarativeNodeKind();

	void setList(SequenceNode<IdentifierExpressionNode> list);

	/**
	 * Returns
	 * <ul>
	 * <li><code>threadprivate(x, y, z, ...)</code>: a non-empty sequence node</li>
	 * <li><code>threadprivate()</code>: an empty sequence node</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierExpressionNode> variables();
}
