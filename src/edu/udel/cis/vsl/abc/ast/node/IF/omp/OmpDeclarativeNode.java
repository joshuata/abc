package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;

/**
 * This represents the OpenMP declarative pragma. Currently, only threadprivate
 * is supported.
 * 
 * @author Manchun Zheng
 * 
 */
public interface OmpDeclarativeNode extends OmpNode, ExternalDefinitionNode {
	public enum OmpDeclarativeNodeKind {
		THREADPRIVATE
	}

	/**
	 * The kind of this OpenMP declarative node.
	 * 
	 * @return
	 */
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
