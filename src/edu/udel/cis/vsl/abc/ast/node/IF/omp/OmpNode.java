package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;

public interface OmpNode extends ASTNode {
public enum OmpNodeKind{
	DECLARATIVE, STATEMENT
}

OmpNodeKind ompNodeKind();
}
