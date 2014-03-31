package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode.OmpSyncNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode.OmpWorkshareNodeKind;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public interface OmpNodeFactory {
	OmpParallelNode newParallelNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken);

	OmpForNode newForNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken);

	OmpSyncNode newSyncNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpSyncNodeKind kind);

	OmpWorkshareNode newWorkshareNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken, OmpWorkshareNodeKind kind);

	OmpDeclarativeNode newDeclarativeNode(Source source,
			IdentifierNode identifier, List<CToken> body, CToken eofToken,
			SequenceNode<IdentifierNode> variables);
}
