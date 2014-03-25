package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpSyncNode extends CommonOmpStatementNode implements
		OmpSyncNode {
	private OmpSyncNodeKind ompSyncNodeKind;
	private IdentifierNode criticalName;
	private ArrayList<IdentifierNode> flushedList;

	public CommonOmpSyncNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpSyncNodeKind kind) {
		super(source, identifier, body, eofToken);
		this.ompSyncNodeKind = kind;
		this.ompStatementKind = OmpStatementNodeKind.SYNCHRONIZATION;
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

	@Override
	public ArrayList<IdentifierNode> flushedList() {
		return this.flushedList;
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
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	@Override
	public void setCriticalName(IdentifierNode name) {
		assert this.ompSyncNodeKind == OmpSyncNodeKind.CRITICAL;
		this.criticalName = name;

	}
}
