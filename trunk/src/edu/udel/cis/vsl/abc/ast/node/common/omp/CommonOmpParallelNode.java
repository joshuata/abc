package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpParallelNode extends CommonOmpStatementNode implements
		OmpParallelNode {
	// private ExpressionNode numThreads = null;
	// private ExpressionNode ifClause = null;
	private boolean isDefaultShared = true;

	/**
	 * Children
	 * <ul>
	 * <li>Children 0-7: same as {@link CommonOmpStatementNode};</li>
	 * <li>Child 8: ExpressionNode, the expression of <code>num_threads()</code>
	 * ;</li>
	 * <li>Child 9: ExpressionNode, the expression of <code>if()</code>.</li>
	 * </ul>
	 * 
	 * @param source
	 */
	public CommonOmpParallelNode(Source source) {
		super(source);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
		this.addChild(null);// child 8
		this.addChild(null);// child 9
	}

	public CommonOmpParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, ExpressionNode numThreads,
			ExpressionNode ifClause, StatementNode statementNode,
			boolean isDefaultShared) {
		super(source);
		this.ompStatementKind = OmpStatementNodeKind.PARALLEL;
		this.setChild(7, statementNode);
		this.setChild(8, numThreads);
		this.setChild(9, ifClause);
		this.isDefaultShared = isDefaultShared;
	}

	@Override
	public void setStatementNode(StatementNode statement) {
		StatementNode statementNode = (StatementNode) this.child(7);

		if (statementNode != null) {
			((OmpStatementNode) statementNode).setStatementNode(statement);
		} else {
			super.setStatementNode(statement);
		}

	}

	@Override
	public ExpressionNode numThreads() {
		return (ExpressionNode) this.child(8);
	}

	@Override
	public ExpressionNode ifClause() {
		return (ExpressionNode) this.child(9);
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
		this.setChild(8, value);
	}

	@Override
	public void setIfClause(ExpressionNode value) {
		this.setChild(9, value);
	}

	@Override
	public OmpParallelNode copy() {
		OmpParallelNode newParallelNode = new CommonOmpParallelNode(
				this.getSource());
		int numChildren = this.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ASTNode child = this.child(i);

			if (child != null) {
				newParallelNode.setChild(i, child.copy());
			}
		}
		return newParallelNode;
	}
}
