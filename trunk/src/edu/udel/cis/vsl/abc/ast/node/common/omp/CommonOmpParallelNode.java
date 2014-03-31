package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpParallelNode extends CommonOmpStatementNode implements
		OmpParallelNode {
	private ExpressionNode numThreads = null;
	private ExpressionNode ifClause = null;
	private boolean isDefaultShared = true;

	/**
	 * Children
	 * <ul>
	 * <li>Children 0-8: same as {@link CommonOmpStatementNode};</li>
	 * <li>Child 9: ExpressionNode, the expression of <code>num_threads()</code>
	 * ;</li>
	 * <li>Child 10: ExpressionNode, the expression of <code>if()</code>.</li>
	 * </ul>
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 */
	public CommonOmpParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
		this.addChild(null);// child 9
		this.addChild(null);// child 10
	}

	public CommonOmpParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, ExpressionNode numThreads,
			ExpressionNode ifClause, StatementNode statementNode,
			boolean isDefaultShared) {
		super(source, identifier, body, eofToken);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
		this.setChild(8, statementNode);
		this.setChild(9, numThreads);
		this.setChild(10, ifClause);
		this.isDefaultShared = isDefaultShared;
	}

	/*
	 * protected List<IdentifierNode> sharedList = null;
	 * 
	 * protected List<IdentifierNode> privateList = null;
	 * 
	 * protected List<IdentifierNode> firstprivateList = null;
	 * 
	 * protected List<IdentifierNode> lastprivateList = null;
	 * 
	 * protected List<IdentifierNode> copyinList = null;
	 * 
	 * protected List<IdentifierNode> copyprivateList = null;
	 * 
	 * protected Pair<Operator, List<IdentifierNode>> reductionList = null;
	 */

	// @Override
	// @SuppressWarnings({ "unchecked" })
	// public CommonOmpParallelNode copy() {
	// ArrayList<CToken> bodyCopy = this.body == null ? null
	// : (ArrayList<CToken>) this.body.clone();
	// IdentifierNode identifier = getPragmaIdentifier();
	// IdentifierNode identifierCopy = identifier == null ? null : identifier
	// .copy();
	// CommonOmpParallelNode newNode = new CommonOmpParallelNode(
	// this.getSource(), identifierCopy, bodyCopy, this.eofToken,
	// duplicate(this.numThreads), duplicate(ifClause),
	// duplicate(statementNode), isDefaultShared);
	// SequenceNode<IdentifierNode> listCopy = this.sharedList == null ? null
	// : this.sharedList.copy();
	// Pair<Operator, SequenceNode<IdentifierNode>> reductionCopy;
	//
	// newNode.setSharedList(listCopy);
	// listCopy = this.privateList == null ? null : this.privateList.copy();
	// newNode.setPrivateList(listCopy);
	// listCopy = this.firstprivateList == null ? null : this.firstprivateList
	// .copy();
	// newNode.setFirstprivateList(listCopy);
	// listCopy = this.lastprivateList == null ? null : this.lastprivateList
	// .copy();
	// newNode.setLastprivateList(listCopy);
	// listCopy = this.copyinList == null ? null : this.copyinList.copy();
	// newNode.setCopyinList(listCopy);
	// listCopy = this.copyprivateList == null ? null : this.copyprivateList
	// .copy();
	// newNode.setCopyprivateList(listCopy);
	// if (this.reductionList == null)
	// reductionCopy = null;
	// else {
	// listCopy = this.reductionList.right == null ? null
	// : this.reductionList.right.copy();
	// reductionCopy = new Pair<>(this.reductionList.left, listCopy);
	// }
	// newNode.setReductionList(reductionCopy);
	// return newNode;
	// }

	@Override
	public void setStatementNode(StatementNode statement) {
		StatementNode statementNode = (StatementNode) this.child(8);

		if (statementNode != null) {
			((OmpStatementNode) statementNode).setStatementNode(statement);
		} else {
			super.setStatementNode(statement);
		}

	}

	@Override
	public ExpressionNode numThreads() {
		return this.numThreads;
	}

	@Override
	public ExpressionNode ifClause() {
		return this.ifClause;
	}

	@Override
	public boolean isDefaultShared() {
		return this.isDefaultShared;
	}

	@Override
	public void setDefault(boolean shared) {
		this.isDefaultShared = shared;
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("OmpParallel");
	}

	@Override
	protected void printExtras(String prefix, PrintStream out) {
		super.printExtras(prefix, out);
		if (numThreads != null) {
			out.println();
			out.print(prefix + "num_threads(");
			out.print(this.numThreads.toString());
			out.print(")");
		}
		if (ifClause != null) {
			out.println();
			out.print(prefix + "if(");
			out.print(this.ifClause.toString());
			out.print(")");
		}
		if (this.isDefaultShared) {
			out.println();
			out.print(prefix + "default(shared)");
		} else {
			out.println();
			out.print(prefix + "default(none)");
		}

	}

	@Override
	public void setNumThreads(ExpressionNode value) {
		this.numThreads = value;
	}

	@Override
	public void setIfClause(ExpressionNode value) {
		this.ifClause = value;
	}
}
