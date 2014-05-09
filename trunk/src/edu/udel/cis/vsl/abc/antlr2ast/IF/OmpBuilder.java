package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.Iterator;

import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface OmpBuilder {

	OmpNode getOmpNode(Source source, Iterator<CToken> ctokens)
			throws SyntaxException;

}
