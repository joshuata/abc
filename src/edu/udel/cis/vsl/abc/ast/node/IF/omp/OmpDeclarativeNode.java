package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import java.util.ArrayList;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;

public interface OmpDeclarativeNode extends OmpNode {
	public enum OmpDeclarativeNodeKind {
		THREADPRIVATE
	}

	OmpDeclarativeNodeKind ompDeclarativeNodeKind();

	void setList(ArrayList<IdentifierNode> list);

	ArrayList<IdentifierNode> variables();
}
