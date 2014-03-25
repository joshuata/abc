package edu.udel.cis.vsl.abc.ast.node.IF.omp;

public interface OmpWorkshareNode extends OmpStatementNode {
	public enum OmpWorkshareNodeKind {
		FOR, SECTIONS, SINGLE, SECTION
	}
	OmpWorkshareNodeKind ompWorkshareNodeKind();
}
