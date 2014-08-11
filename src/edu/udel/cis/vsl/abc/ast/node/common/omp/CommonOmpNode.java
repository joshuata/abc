package edu.udel.cis.vsl.abc.ast.node.common.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public abstract class CommonOmpNode extends CommonASTNode implements OmpNode {

	public CommonOmpNode(Source source) {
		super(source);
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.OMP_NODE;
	}

	@Override
	protected boolean equivWork(ASTNode that) {
		if (that instanceof OmpNode)
			return this.ompNodeKind() == ((OmpNode) that).ompNodeKind();
		return false;
	}
}
