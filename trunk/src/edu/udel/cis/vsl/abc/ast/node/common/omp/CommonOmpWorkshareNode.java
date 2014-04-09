package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpWorkshareNode extends CommonOmpStatementNode implements
		OmpWorkshareNode {

	protected OmpWorkshareNodeKind ompWorkshareKind;

	public CommonOmpWorkshareNode(Source source, OmpWorkshareNodeKind kind) {
		super(source);
		this.ompStatementKind = OmpStatementNodeKind.WORKSHARE;
		this.ompWorkshareKind = kind;
	}

	@Override
	public OmpWorkshareNode copy() {
		OmpWorkshareNode newWorkshareNode = new CommonOmpWorkshareNode(
				this.getSource(), this.ompWorkshareKind);
		int numChildren = this.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ASTNode child = this.child(i);

			if (child != null) {
				newWorkshareNode.setChild(i, child.copy());
			}
		}
		return newWorkshareNode;
	}

	@Override
	public OmpWorkshareNodeKind ompWorkshareNodeKind() {
		return this.ompWorkshareKind;
	}

	@Override
	protected void printBody(PrintStream out) {
		switch (this.ompWorkshareKind) {
		case SECTIONS:
			out.print("OmpSections");
			break;
		case SECTION:
			out.print("OmpSection");
			break;
		case SINGLE:
			out.print("OmpSingle");
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

}
