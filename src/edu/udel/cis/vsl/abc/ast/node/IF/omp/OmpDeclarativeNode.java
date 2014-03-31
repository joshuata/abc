package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;

public interface OmpDeclarativeNode extends OmpNode {
	public enum OmpDeclarativeNodeKind {
		THREADPRIVATE
	}

	OmpDeclarativeNodeKind ompDeclarativeNodeKind();

	void setList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns
	 * <ul>
	 * <li><code>threadprivate(x, y, z, ...)</code>: a non-empty sequence node</li>
	 * <li><code>threadprivate()</code>: an empty sequence node</li>
	 * </ul>
	 * @return
	 */
	SequenceNode<IdentifierNode> variables();
}
