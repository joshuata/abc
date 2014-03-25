package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.util.Pair;

public class CommonOmpParallelNode extends CommonOmpStatementNode implements
		OmpParallelNode {
	private ExpressionNode numThreads = null;
	private ExpressionNode ifClause = null;
	private boolean isDefaultShared = true;

	public CommonOmpParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
	}

	public CommonOmpParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, ExpressionNode numThreads,
			ExpressionNode ifClause, StatementNode statementNode,
			boolean isDefaultShared) {
		super(source, identifier, body, eofToken);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
		this.numThreads = numThreads;
		this.ifClause = ifClause;
		this.isDefaultShared = isDefaultShared;
		this.statementNode = statementNode;
		if (ifClause != null)
			this.addChild(ifClause);
		if (numThreads != null)
			this.addChild(numThreads);
		this.addChild(statementNode);
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

	@Override
	@SuppressWarnings({ "unchecked" })
	public CommonOmpParallelNode copy() {
		ArrayList<CToken> bodyCopy = this.body == null ? null
				: (ArrayList<CToken>) this.body.clone();
		IdentifierNode identifier = getPragmaIdentifier();
		IdentifierNode identifierCopy = identifier == null ? null : identifier
				.copy();
		CommonOmpParallelNode newNode = new CommonOmpParallelNode(
				this.getSource(), identifierCopy, bodyCopy, this.eofToken,
				duplicate(this.numThreads), duplicate(ifClause),
				duplicate(statementNode), isDefaultShared);
		ArrayList<IdentifierNode> listCopy = this.sharedList == null ? null
				: (ArrayList<IdentifierNode>) this.sharedList.clone();
		Pair<Operator, ArrayList<IdentifierNode>> reductionCopy;

		newNode.setSharedList(listCopy);
		listCopy = this.privateList == null ? null
				: (ArrayList<IdentifierNode>) this.privateList.clone();
		newNode.setPrivateList(listCopy);
		listCopy = this.firstprivateList == null ? null
				: (ArrayList<IdentifierNode>) this.firstprivateList.clone();
		newNode.setFirstprivateList(listCopy);
		listCopy = this.lastprivateList == null ? null
				: (ArrayList<IdentifierNode>) this.lastprivateList.clone();
		newNode.setLastprivateList(listCopy);
		listCopy = this.copyinList == null ? null
				: (ArrayList<IdentifierNode>) this.copyinList.clone();
		newNode.setCopyinList(listCopy);
		listCopy = this.copyprivateList == null ? null
				: (ArrayList<IdentifierNode>) this.copyprivateList.clone();
		newNode.setCopyprivateList(listCopy);
		if (this.reductionList == null)
			reductionCopy = null;
		else {
			listCopy = this.reductionList.right == null ? null
					: (ArrayList<IdentifierNode>) this.reductionList.right
							.clone();
			reductionCopy = new Pair<>(this.reductionList.left, listCopy);
		}
		newNode.setReductionList(reductionCopy);
		return newNode;
	}

	@Override
	public void setStatementNode(StatementNode statement) {
		if (this.statementNode != null) {
			((OmpStatementNode) this.statementNode).setStatementNode(statement);
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
