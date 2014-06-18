package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.Iterator;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * An object which translates a collection of C tokens of CIVL pragma into an
 * ABC AST representing CIVL nodes.
 * 
 * @author Manchun Zheng (zmanchun)
 * 
 */
public interface CIVLPragmaBuilder {

	/**
	 * Translates a collection of C tokens (representing a CIVL pragma) into a
	 * list of block item nodes.
	 * 
	 * @param ctokens
	 *            The collection of C tokens parsed from the CIVL pragma.
	 * @return The list of block item nodes, representing the token collection,
	 *         which is parsed from a certain CIVL pragma.
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	List<BlockItemNode> getBlockItem(Iterator<CToken> ctokens)
			throws ParseException, SyntaxException;

}