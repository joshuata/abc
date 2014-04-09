package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpSyncNode extends CommonOmpStatementNode implements
		OmpSyncNode {
	private OmpSyncNodeKind ompSyncNodeKind;

	// private IdentifierNode criticalName;

	// private SequenceNode<IdentifierNode> flushedList;

	/**
	 * Children
	 * <ul>
	 * <li>Children 0-7: same as {@link CommonOmpStatementNode};</li>
	 * <li>Child 8: SequenceNode&lt;IdentifierNode&gt; "flushedList", the list
	 * of identifiers declared by <code>flushed()</code> ;</li>
	 * <li>Child 9: IdentifierNode, optional "name" of critical construct.</li>
	 * </ul>
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 * @param kind
	 */
	public CommonOmpSyncNode(Source source, OmpSyncNodeKind kind) {
		super(source);
		this.ompSyncNodeKind = kind;
		this.ompStatementKind = OmpStatementNodeKind.SYNCHRONIZATION;
		this.addChild(null);// child 8
		this.addChild(null);// child 9
	}

	@Override
	public boolean completed() {
		switch (this.ompSyncNodeKind) {
		case BARRIER:
		case FLUSH:
			return true;
		default:
			return super.completed();
		}
	}

	@Override
	public OmpSyncNodeKind ompSyncNodeKind() {
		return this.ompSyncNodeKind;
	}

	@Override
	public IdentifierNode criticalName() {
		return (IdentifierNode) this.child(9);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<IdentifierExpressionNode> flushedList() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(8);
	}

	// MASTER, CRITICAL, BARRIER, FLUSH
	@Override
	protected void printBody(PrintStream out) {
		switch (this.ompSyncNodeKind) {
		case MASTER:
			out.print("OmpMaster");
			break;
		case CRITICAL:
			out.print("OmpCritical");
			break;
		case BARRIER:
			out.print("OmpBarrier");
			break;
		case FLUSH:
			out.print("OmpFlush");
			break;
		case ORDERED:
			out.print("OmpOrdered");
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	@Override
	public void setCriticalName(IdentifierNode name) {
		assert this.ompSyncNodeKind == OmpSyncNodeKind.CRITICAL;
		this.setChild(9, name);
	}

	@Override
	public void setFlushedList(SequenceNode<IdentifierExpressionNode> list) {
		assert this.ompSyncNodeKind == OmpSyncNodeKind.FLUSH;
		this.setChild(8, list);
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// protected void printExtras(String prefix, PrintStream out) {
	// int count;
	// SequenceNode<IdentifierExpressionNode> flushedList =
	// (SequenceNode<IdentifierExpressionNode>) this
	// .child(8);
	//
	// if (flushedList != null) {
	// count = flushedList.numChildren();
	// if (count > 0) {
	// out.println();
	// out.print(prefix + "flush(");
	// for (int i = 0; i < count; i++) {
	// out.print(flushedList.getSequenceChild(i).getIdentifier()
	// .name());
	// if (i < count - 1)
	// out.print(",");
	// }
	// out.print(")");
	// }
	// }
	// }

	@Override
	public OmpSyncNode copy() {
		OmpSyncNode newSyncNode = new CommonOmpSyncNode(this.getSource(),
				this.ompSyncNodeKind);
		int numChildren = this.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ASTNode child = this.child(i);

			if (child != null) {
				newSyncNode.setChild(i, child.copy());
			}
		}
		return newSyncNode;
	}
}
