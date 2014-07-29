package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.Iterator;

import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * An object which translates a collection of C tokens of OpenMP pragma into an
 * ABC AST representing OpenMP nodes.
 * 
 * @author Manchun Zheng (zmanchun)
 * 
 */
public interface OmpBuilder {

	/**
	 * Translates a collection of C tokens (representing an OpenMP pragma) into
	 * an OpenMP AST node in ABC.
	 * 
	 * @param source
	 *            The source code information of the OpenMP pragma.
	 * @param ctokens
	 *            The collection of C tokens parsed from the OpenMP pragma.
	 * @return The OpenMP AST node representing the token collection which is
	 *         parsed from a certain OpenM pragma.
	 * @throws SyntaxException
	 */
	OmpNode getOmpNode(Source source, Iterator<CToken> ctokens)
			throws SyntaxException;

}
