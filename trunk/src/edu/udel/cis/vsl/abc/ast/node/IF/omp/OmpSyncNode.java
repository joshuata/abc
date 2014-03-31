package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;

/**
 * This interface stands for synchronization constructs of OpenMP, including:
 * <ol>
 * <li>master</li>
 * <li>critical</li>
 * <li>barrier</li>
 * <li>flush</li>
 * </ol>
 * 
 * @author Manchun Zheng
 * 
 */
public interface OmpSyncNode extends OmpStatementNode {
	public enum OmpSyncNodeKind {
		MASTER, CRITICAL, BARRIER, FLUSH, ORDERED
	}

	OmpSyncNodeKind ompSyncNodeKind();

	void setCriticalName(IdentifierNode name);

	/**
	 * The identifier node representing the name of the critical section, only
	 * valid for CRITICAL kind.
	 * 
	 * @return
	 */
	IdentifierNode criticalName();

	/**
	 * The list of variables in the flush construct. NULL for other kinds.
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> flushedList();

	void setFlushedList(SequenceNode<IdentifierNode> list);
}
