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

	OmpStatementNodeKind ompStatementNodeKind();

	boolean nowait();

	void setNowait(boolean value);

	StatementNode statementNode();

	void setStatementNode(StatementNode statementNode);

	List<IdentifierNode> sharedList();

	void setSharedList(ArrayList<IdentifierNode> list);

	List<IdentifierNode> privateList();

	void setPrivateList(ArrayList<IdentifierNode> list);

	List<IdentifierNode> firstprivateList();

	void setFirstprivateList(ArrayList<IdentifierNode> list);

	List<IdentifierNode> lastprivateList();

	void setLastprivateList(ArrayList<IdentifierNode> list);

	List<IdentifierNode> copyinList();

	void setCopyinList(ArrayList<IdentifierNode> list);

	List<IdentifierNode> copyprivateList();

	void setCopyprivateList(ArrayList<IdentifierNode> list);

	Pair<Operator, ArrayList<IdentifierNode>> reductionList();

	void setReductionList(Pair<Operator, ArrayList<IdentifierNode>> list);
}
