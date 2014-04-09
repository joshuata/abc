package edu.udel.cis.vsl.abc.ast.node.IF.omp;

/**
 * This represents an OpenMP worksharing construct, either a loop,
 * sections/section, or single construct.
 * 
 * @author Manchun Zheng
 */
public interface OmpWorksharingNode extends OmpStatementNode {
	public enum OmpWorksharingNodeKind {
		FOR, SECTIONS, SINGLE, SECTION
	}

	OmpWorksharingNodeKind ompWorkshareNodeKind();
}
