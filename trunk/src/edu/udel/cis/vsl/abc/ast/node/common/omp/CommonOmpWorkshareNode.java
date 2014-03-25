package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpWorkshareNode extends CommonOmpStatementNode implements
		OmpWorkshareNode {

	protected OmpWorkshareNodeKind ompWorkshareKind;

	public CommonOmpWorkshareNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpWorkshareNodeKind kind) {
		super(source, identifier, body, eofToken);
		this.ompStatementKind = OmpStatementNodeKind.WORKSHARE;
		this.ompWorkshareKind = kind;
	}

	@Override
	public CommonOmpWorkshareNode copy() {
		// TODO Auto-generated method stub
		return null;
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
