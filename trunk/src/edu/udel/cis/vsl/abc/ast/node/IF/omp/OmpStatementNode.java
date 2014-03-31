package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.util.Pair;

public interface OmpStatementNode extends OmpNode, StatementNode {
	public enum OmpStatementNodeKind {
		PARALLEL, SYNCHRONIZATION, WORKSHARE
	}

	boolean completed();

	/**
	 * Returns the OpenMP statement kind of this node, i.e., either it is a
	 * parallel, workshare or synchronization node.
	 * 
	 * @return
	 */
	OmpStatementNodeKind ompStatementNodeKind();

	/**
	 * Returns true iff nowait is applied.
	 * 
	 * @return
	 */
	boolean nowait();

	void setNowait(boolean value);

	/**
	 * Returns the statement node affected by this OpenMP pragma. e.g., the
	 * following code is represented as an OpenMP parallel node with the
	 * following compound statements as its statement node.<code>
	 * #prama omp parallel
	 * {
	 *   ...//statements
	 * }
	 * </code>
	 * 
	 * @return
	 */
	StatementNode statementNode();

	void setStatementNode(StatementNode statementNode);

	/**
	 * Returns the list of identifier nodes declared by <code>shared</code>
	 * clause. There are several possibilities:
	 * <ul>
	 * <li><code>shared(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>shared()</code>: an empty sequence node;</li>
	 * <li>no <code>shared</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> sharedList();

	void setSharedList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>private</code>
	 * clause. There are several possibilities:
	 * <ul>
	 * <li><code>private(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>private()</code>: an empty sequence node;</li>
	 * <li>no <code>private</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> privateList();

	void setPrivateList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by
	 * <code>firstprivate</code> clause. There are several possibilities:
	 * <ul>
	 * <li><code>firstprivate(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>firstprivate()</code>: an empty sequence node;</li>
	 * <li>no <code>firstprivate</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> firstprivateList();

	void setFirstprivateList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>lastprivate</code>
	 * clause. There are several possibilities:
	 * <ul>
	 * <li><code>lastprivate(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>lastprivate()</code>: an empty sequence node;</li>
	 * <li>no <code>lastprivate</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> lastprivateList();

	void setLastprivateList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>copyin</code>
	 * clause. There are several possibilities:
	 * <ul>
	 * <li><code>copyin(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>copyin()</code>: an empty sequence node;</li>
	 * <li>no <code>copyin</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> copyinList();

	void setCopyinList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>copyprivate</code>
	 * clause. There are several possibilities:
	 * <ul>
	 * <li><code>copyprivate(x, y, z, ...)</code>: a non-empty sequence node;</li>
	 * <li><code>copyprivate()</code>: an empty sequence node;</li>
	 * <li>no <code>copyprivate</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	SequenceNode<IdentifierNode> copyprivateList();

	void setCopyprivateList(SequenceNode<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>reduction</code>
	 * clause.
	 * 
	 * There are several possibilities:
	 * <ul>
	 * <li><code>reduction(operator: x, y, z, ...)</code>: the operator and a
	 * non-empty sequence node;</li>
	 * <li>no <code>reduction</code> clause: null.</li>
	 * </ul>
	 * 
	 * @return
	 */
	Pair<Operator, SequenceNode<IdentifierNode>> reductionList();

	void setReductionList(Pair<Operator, SequenceNode<IdentifierNode>> list);
}
