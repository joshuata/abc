package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;

public interface OmpNode extends PragmaNode {
public enum OmpNodeKind{
	DECLARATIVE, STATEMENT
}

OmpNodeKind ompNodeKind();
}
