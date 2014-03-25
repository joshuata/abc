package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
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

	protected ArrayList<IdentifierNode> sharedList = null;

	protected ArrayList<IdentifierNode> privateList = null;

	protected ArrayList<IdentifierNode> firstprivateList = null;

	protected ArrayList<IdentifierNode> lastprivateList = null;

	protected ArrayList<IdentifierNode> copyinList = null;

	protected ArrayList<IdentifierNode> copyprivateList = null;

	protected Pair<Operator, ArrayList<IdentifierNode>> reductionList = null;

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
	public List<IdentifierNode> sharedList() {
		return this.sharedList;
	}

	@Override
	public List<IdentifierNode> privateList() {
		return this.privateList;
	}

	@Override
	public List<IdentifierNode> firstprivateList() {
		return this.firstprivateList;
	}

	@Override
	public List<IdentifierNode> lastprivateList() {
		return this.lastprivateList;
	}

	@Override
	public List<IdentifierNode> copyinList() {
		return this.copyinList;
	}

	@Override
	public List<IdentifierNode> copyprivateList() {
		return this.copyprivateList;
	}

	@Override
	public Pair<Operator, ArrayList<IdentifierNode>> reductionList() {
		return this.reductionList;
	}

	@Override
	public void setSharedList(ArrayList<IdentifierNode> list) {
		this.sharedList = list;
	}

	@Override
	public void setPrivateList(ArrayList<IdentifierNode> list) {
		this.privateList = list;
	}

	@Override
	public void setFirstprivateList(ArrayList<IdentifierNode> list) {
		this.firstprivateList = list;
	}

	@Override
	public void setLastprivateList(ArrayList<IdentifierNode> list) {
		this.lastprivateList = list;
	}

	@Override
	public void setCopyinList(ArrayList<IdentifierNode> list) {
		this.copyinList = list;
	}

	@Override
	public void setCopyprivateList(ArrayList<IdentifierNode> list) {
		this.copyprivateList = list;
	}

	@Override
	public void setReductionList(Pair<Operator, ArrayList<IdentifierNode>> list) {
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
			count = sharedList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "shared(");
				for (int i = 0; i < count; i++) {
					out.print(sharedList.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (privateList != null) {
			count = privateList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "private(");
				for (int i = 0; i < count; i++) {
					out.print(privateList.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (firstprivateList != null) {
			count = firstprivateList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "firstprivate(");
				for (int i = 0; i < count; i++) {
					out.print(firstprivateList.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (lastprivateList != null) {
			count = lastprivateList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "lastprivate(");
				for (int i = 0; i < count; i++) {
					out.print(lastprivateList.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (copyinList != null) {
			count = copyinList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "copyin(");
				for (int i = 0; i < count; i++) {
					out.print(copyinList.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
		if (copyprivateList != null) {
			count = copyprivateList.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "copyprivate(");
				for (int i = 0; i < count; i++) {
					out.print(copyprivateList.get(i).name());
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
			count = reductionList.right.size();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					out.print(reductionList.right.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
			}
			out.print(")");
		}

	}

}
