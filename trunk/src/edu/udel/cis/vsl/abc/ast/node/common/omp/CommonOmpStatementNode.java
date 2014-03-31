package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.util.Pair;

public abstract class CommonOmpStatementNode extends CommonOmpNode implements
		OmpStatementNode {

	protected OmpStatementNodeKind ompStatementKind;

	protected boolean nowait;

	protected StatementNode statementNode = null;

	protected SequenceNode<IdentifierNode> sharedList = null;

	protected SequenceNode<IdentifierNode> privateList = null;

	protected SequenceNode<IdentifierNode> firstprivateList = null;

	protected SequenceNode<IdentifierNode> lastprivateList = null;

	protected SequenceNode<IdentifierNode> copyinList = null;

	protected SequenceNode<IdentifierNode> copyprivateList = null;

	protected Pair<Operator, SequenceNode<IdentifierNode>> reductionList = null;

	public CommonOmpStatementNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken);
		nowait = false;
	}

	@Override
	public boolean completed() {
		if (this.statementNode != null) {
			if (this.statementNode instanceof OmpStatementNode)
				return ((OmpStatementNode) this.statementNode).completed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean nowait() {
		return this.nowait;
	}

	@Override
	public void setNowait(boolean value) {
		this.nowait = value;
	}

	@Override
	public OmpNodeKind ompNodeKind() {
		return OmpNodeKind.STATEMENT;
	}

	@Override
	public StatementKind statementKind() {
		return StatementKind.OMP_STATEMENT;
	}

	@Override
	public OmpStatementNodeKind ompStatementNodeKind() {
		return this.ompStatementKind;
	}

	@Override
	public StatementNode statementNode() {
		return this.statementNode;
	}

	@Override
	public void setStatementNode(StatementNode stmtNode) {
		this.statementNode = stmtNode;
		this.addChild(stmtNode);
	}

	@Override
	public SequenceNode<IdentifierNode> sharedList() {
		return this.sharedList;
	}

	@Override
	public SequenceNode<IdentifierNode> privateList() {
		return this.privateList;
	}

	@Override
	public SequenceNode<IdentifierNode> firstprivateList() {
		return this.firstprivateList;
	}

	@Override
	public SequenceNode<IdentifierNode> lastprivateList() {
		return this.lastprivateList;
	}

	@Override
	public SequenceNode<IdentifierNode> copyinList() {
		return this.copyinList;
	}

	@Override
	public SequenceNode<IdentifierNode> copyprivateList() {
		return this.copyprivateList;
	}

	@Override
	public Pair<Operator, SequenceNode<IdentifierNode>> reductionList() {
		return this.reductionList;
	}

	@Override
	public void setSharedList(SequenceNode<IdentifierNode> list) {
		this.sharedList = list;
	}

	@Override
	public void setPrivateList(SequenceNode<IdentifierNode> list) {
		this.privateList = list;
	}

	@Override
	public void setFirstprivateList(SequenceNode<IdentifierNode> list) {
		this.firstprivateList = list;
	}

	@Override
	public void setLastprivateList(SequenceNode<IdentifierNode> list) {
		this.lastprivateList = list;
	}

	@Override
	public void setCopyinList(SequenceNode<IdentifierNode> list) {
		this.copyinList = list;
	}

	@Override
	public void setCopyprivateList(SequenceNode<IdentifierNode> list) {
		this.copyprivateList = list;
	}

	@Override
	public void setReductionList(
			Pair<Operator, SequenceNode<IdentifierNode>> list) {
		this.reductionList = list;
	}

	@Override
	protected void printExtras(String prefix, PrintStream out) {
		int count;

		if (this.nowait) {
			out.println();
			out.print(prefix + "nowait");
		}
		if (sharedList != null) {
			count = sharedList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "shared(");
				for (int i = 0; i < count; i++) {
					out.print(sharedList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (privateList != null) {
			count = privateList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "private(");
				for (int i = 0; i < count; i++) {
					out.print(privateList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (firstprivateList != null) {
			count = firstprivateList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "firstprivate(");
				for (int i = 0; i < count; i++) {
					out.print(firstprivateList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (lastprivateList != null) {
			count = lastprivateList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "lastprivate(");
				for (int i = 0; i < count; i++) {
					out.print(lastprivateList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (copyinList != null) {
			count = copyinList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "copyin(");
				for (int i = 0; i < count; i++) {
					out.print(copyinList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (copyprivateList != null) {
			count = copyprivateList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "copyprivate(");
				for (int i = 0; i < count; i++) {
					out.print(copyprivateList.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (reductionList != null) {
			out.println();
			out.print(prefix + "reduction(");
			switch (reductionList.left) {
			case PLUS:
				out.print("+");
				break;
			case TIMES:
				out.print("*");
				break;
			case MINUS:
				out.print("-");
				break;
			case BITAND:
				out.print("&");
				break;
			case BITXOR:
				out.print("^");
				break;
			case BITOR:
				out.print("|");
				break;
			case LAND:
				out.print("&&");
				break;
			default:
				out.print("||");
				break;
			}
			out.print(":");
			count = reductionList.right.numChildren();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					out.print(reductionList.right.getSequenceChild(i).name());
					if (i < count - 1)
						out.print(",");
				}
			}
			out.print(")");
		}

	}

}
