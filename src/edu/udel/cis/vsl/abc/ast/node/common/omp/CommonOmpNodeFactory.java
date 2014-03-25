package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode.OmpSyncNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode.OmpWorkshareNodeKind;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpNodeFactory implements OmpNodeFactory {

	// private LiteralInterpreter literalInterpreter;

	private ValueFactory valueFactory;

	public CommonOmpNodeFactory(ValueFactory valueFactory) {
		this.valueFactory = valueFactory;
	}

	public ValueFactory valueFactory() {
		return this.valueFactory;
	}

	@Override
	public OmpParallelNode newParallelNode(Source source,
			IdentifierNode identifier, List<CToken> body, CToken eofToken) {
		return new CommonOmpParallelNode(source, identifier, body, eofToken);
	}

	@Override
	public OmpForNode newForNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		return new CommonOmpForNode(source, identifier, body, eofToken);
	}

	@Override
	public OmpSyncNode newSyncNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpSyncNodeKind kind) {
		return new CommonOmpSyncNode(source, identifier, body, eofToken, kind);
	}

	@Override
	public OmpWorkshareNode newWorkshareNode(Source source,
			IdentifierNode identifier, List<CToken> body, CToken eofToken,
			OmpWorkshareNodeKind kind) {
		return new CommonOmpWorkshareNode(source, identifier, body, eofToken,
				kind);
	}
}
