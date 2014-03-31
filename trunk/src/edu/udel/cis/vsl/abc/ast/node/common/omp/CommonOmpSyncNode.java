package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpSyncNode extends CommonOmpStatementNode implements
		OmpSyncNode {
	private OmpSyncNodeKind ompSyncNodeKind;
	private IdentifierNode criticalName;

	// private SequenceNode<IdentifierNode> flushedList;

	/**
	 * Children
	 * <ul>
	 * <li>Children 0-8: same as {@link CommonOmpStatementNode};</li>
	 * <li>Child 9: SequenceNode&lt;IdentifierNode&gt; "flushedList", the list
	 * of identifiers declared by <code>flushed()</code> ;</li>
	 * </ul>
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 * @param kind
	 */
	public CommonOmpSyncNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpSyncNodeKind kind) {
		super(source, identifier, body, eofToken);
		this.ompSyncNodeKind = kind;
		this.ompStatementKind = OmpStatementNodeKind.SYNCHRONIZATION;
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
		return this.criticalName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<IdentifierExpressionNode> flushedList() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(9);
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
		this.criticalName = name;

	}

	@Override
	public void setFlushedList(SequenceNode<IdentifierExpressionNode> list) {
		assert this.ompSyncNodeKind == OmpSyncNodeKind.FLUSH;
		this.setChild(9, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void printExtras(String prefix, PrintStream out) {
		int count;
		SequenceNode<IdentifierExpressionNode> flushedList = (SequenceNode<IdentifierExpressionNode>) this
				.child(9);

		if (flushedList != null) {
			count = flushedList.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "flush(");
				for (int i = 0; i < count; i++) {
					out.print(flushedList.getSequenceChild(i).getIdentifier()
							.name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
	}
}
