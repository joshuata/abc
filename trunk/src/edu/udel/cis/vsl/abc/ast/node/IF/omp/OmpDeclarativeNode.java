package edu.udel.cis.vsl.abc.ast.node.IF.omp;


public interface OmpDeclarativeNode extends OmpNode {
	public enum OmpDeclarativeNodeKind {
		THREADPRIVATE
	}

	OmpDeclarativeNodeKind ompDeclarativeNodeKind();
}
