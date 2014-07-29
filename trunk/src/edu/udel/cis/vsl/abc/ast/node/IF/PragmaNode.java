package edu.udel.cis.vsl.abc.ast.node.IF;

import java.util.Iterator;

import org.antlr.runtime.TokenSource;

import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;

/**
 * A pragma may be included in the AST wherever a statement or an external
 * definition may occur. The pragma is represented as an identifier (the first
 * token to follow the # pragma), followed by some sequence of tokens of length
 * (say) n. We will refer to that sequence as the "pragma body".
 * 
 * @author siegel
 * 
 */
public interface PragmaNode extends ExternalDefinitionNode, StatementNode {

	/**
	 * Returns the pragma identifier, i.e., the identifier immediately following
	 * "# pragma". For example, in "# pragma omp ...", the identifier "omp"
	 * would be returned.
	 * 
	 * @return the pragma identifier
	 */
	IdentifierNode getPragmaIdentifier();

	/**
	 * Returns n, the number of tokens in the pragma body, i.e., the number of
	 * tokens following # pragma IDENTIFIER.
	 * 
	 * @return number of tokens in the pragma body
	 */
	int getNumTokens();

	/**
	 * Returns the index-th token in the pragma body. The tokens of body are
	 * indexed beginning with 0.
	 * 
	 * @param index
	 *            an integer in the range [0,n-1], where n is the value returned
	 *            by {@link #getNumTokens}
	 * 
	 * @return the index-th token in the pragma body
	 */
	CToken getToken(int index);

	/**
	 * Returns an iterator over the tokens in the pragma body.
	 * 
	 * @return iterator over the tokens of pragma body
	 */
	Iterator<CToken> getTokens();

	/**
	 * Returns the tokens of the pragma body as an ANTLR TokenSource, which can
	 * then be fed into an ANTLR parser for syntactic analysis.
	 * 
	 * @return a token source for the tokens comprising this pragma
	 */
	TokenSource getTokenSource();

	@Override
	PragmaNode copy();
}
