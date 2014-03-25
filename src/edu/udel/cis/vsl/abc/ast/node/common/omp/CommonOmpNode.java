package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonPragmaNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public abstract class CommonOmpNode extends CommonPragmaNode implements OmpNode {

	public CommonOmpNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken);
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.OMP_NODE;
	}

}
