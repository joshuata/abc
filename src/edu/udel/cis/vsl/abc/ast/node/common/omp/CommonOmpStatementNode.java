package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

@SuppressWarnings("unchecked")
public abstract class CommonOmpStatementNode extends CommonOmpNode implements
		OmpStatementNode {

	protected OmpStatementNodeKind ompStatementKind;

	protected boolean nowait;

	/**
	 * 
	 * Children:
	 * <ul>
	 * <li>
	 * Child 0: IdentifierNode "omp", name of the pragma</li>
	 * <li>Child 1: SequenceNode&lt;IdentifierExpressionNode&gt; "sharedList",
	 * the list of identifiers declared by <code>shared</code></li>
	 * <li>Child 2: SequenceNode&lt;IdentifierExpressionNode&gt; "privateList",
	 * the list of identifiers declared by <code>private</code></li>
	 * <li>Child 3: SequenceNode&lt;IdentifierExpressionNode&gt;
	 * "firstprivateList", the list of identifiers declared by
	 * <code>firstprivate</code></li>
	 * <li>Child 4: SequenceNode&lt;IdentifierExpressionNode&gt;
	 * "lastprivateList", the list of identifiers declared by
	 * <code>lastprivate</code></li>
	 * <li>Child 5: SequenceNode&lt;IdentifierExpressionNode&gt; "copyinList",
	 * the list of identifiers declared by <code>copyin</code></li>
	 * <li>Child 6: SequenceNode&lt;IdentifierExpressionNode&gt;
	 * "copyprivateList", the list of identifiers declared by
	 * <code>copyprivate</code></li>
	 * <li>Child 7: SequenceNode&lt;OperatorNode&gt; "reductionList", the list
	 * of operators and identifiers declared by <code>reduction</code></li>
	 * <li>Child 8: StatementNode, the statement node affected by this pragma</li>
	 * </ul>
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 */
	public CommonOmpStatementNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken);
		nowait = false;
		this.addChild(null);// child 1
		this.addChild(null);// child 2
		this.addChild(null);// child 3
		this.addChild(null);// child 4
		this.addChild(null);// child 5
		this.addChild(null);// child 6
		this.addChild(null);// child 7
		this.addChild(null);// child 8
	}

	@Override
	public boolean completed() {
		StatementNode statementNode = (StatementNode) this.child(8);

		if (statementNode != null) {
			if (statementNode instanceof OmpStatementNode)
				return ((OmpStatementNode) statementNode).completed();
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
		return (StatementNode) this.child(8);
	}

	@Override
	public void setStatementNode(StatementNode stmtNode) {
		this.setChild(8, stmtNode);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> sharedList() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(1);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> privateList() {
		return (SequenceNode<IdentifierExpressionNode>) child(2);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> firstprivateList() {
		return (SequenceNode<IdentifierExpressionNode>) child(3);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> lastprivateList() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(4);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> copyinList() {
		return (SequenceNode<IdentifierExpressionNode>) child(5);
	}

	@Override
	public SequenceNode<IdentifierExpressionNode> copyprivateList() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(6);
	}

	@Override
	public SequenceNode<OmpReductionNode> reductionList() {
		return (SequenceNode<OmpReductionNode>) this.child(7);
	}

	@Override
	public void setSharedList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(1, list);
	}

	@Override
	public void setPrivateList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(2, list);
	}

	@Override
	public void setFirstprivateList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(3, list);
	}

	@Override
	public void setLastprivateList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(4, list);
	}

	@Override
	public void setCopyinList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(5, list);
	}

	@Override
	public void setCopyprivateList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(6, list);
	}

	@Override
	public void setReductionList(SequenceNode<OmpReductionNode> list) {
		this.setChild(7, list);
	}

	// @Override
	// protected void printExtras(String prefix, PrintStream out) {
	// int count;
	// SequenceNode<IdentifierExpressionNode> list;
	// SequenceNode<OmpReductionNode> reductionList;
	//
	// if (this.nowait) {
	// out.println();
	// out.print(prefix + "nowait");
	// }
	// // sharedList
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(1);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "shared(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// // privateList
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(2);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "private(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// // firstprivateList
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(3);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "firstprivate(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// // lastprivateList
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(4);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "lastprivate(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// // copyin
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(5);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "copyin(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// // copyprivate
	// list = (SequenceNode<IdentifierExpressionNode>) this.child(6);
	// if (list != null) {
	// count = list.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "copyprivate(");
	// for (int i = 0; i < count; i++) {
	// out.print(list.getSequenceChild(i).getIdentifier().name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// reductionList = (SequenceNode<OmpReductionNode>) this.child(7);
	// if (reductionList != null) {
	// out.println();
	// out.print(prefix + "reduction(");
	//
	// if (reductionList != null) {
	// count = reductionList.numChildren();
	//
	// for (int i = 0; i < count; i++) {
	// OperatorNode current = reductionList.getSequenceChild(i);
	// Operator operator = current.getOperator();
	// IdentifierExpressionNode arg = (IdentifierExpressionNode) current
	// .getArgument(0);
	//
	// switch (operator) {
	// case PLUS:
	// out.print("+");
	// break;
	// case TIMES:
	// out.print("*");
	// break;
	// case MINUS:
	// out.print("-");
	// break;
	// case BITAND:
	// out.print("&");
	// break;
	// case BITXOR:
	// out.print("^");
	// break;
	// case BITOR:
	// out.print("|");
	// break;
	// case LAND:
	// out.print("&&");
	// break;
	// default:
	// out.print("||");
	// break;
	// }
	// out.print(":");
	// out.print(arg.getIdentifier().name());
	// out.print(",");
	// }
	// }
	// out.print(")");
	// }
	//
	// }

}
