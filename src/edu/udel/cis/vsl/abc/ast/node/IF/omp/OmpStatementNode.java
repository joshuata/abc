package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
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
	 * clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> sharedList();

	void setSharedList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>private</code>
	 * clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> privateList();

	void setPrivateList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by
	 * <code>firstprivate</code> clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> firstprivateList();

	void setFirstprivateList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>lastprivate</code>
	 * clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> lastprivateList();

	void setLastprivateList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>copyin</code>
	 * clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> copyinList();

	void setCopyinList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>copyprivate</code>
	 * clause.
	 * 
	 * @return
	 */
	List<IdentifierNode> copyprivateList();

	void setCopyprivateList(ArrayList<IdentifierNode> list);

	/**
	 * Returns the list of identifier nodes declared by <code>reduction</code>
	 * clause.
	 * 
	 * @return
	 */
	Pair<Operator, ArrayList<IdentifierNode>> reductionList();

	void setReductionList(Pair<Operator, ArrayList<IdentifierNode>> list);
}
