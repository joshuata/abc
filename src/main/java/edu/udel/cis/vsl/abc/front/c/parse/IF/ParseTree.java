package edu.udel.cis.vsl.abc.front.c.parse.IF;

import java.util.Collection;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.front.c.parse.IF.Parse.RuleKind;
import edu.udel.cis.vsl.abc.token.IF.CTokenSequence;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SourceFile;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * A ParseTree is the result of parsing a translation unit. It comprises an
 * ANTLR tree (represented using ANTLR's {@link CommonTree} class) together with
 * "source" information specifying the origins of every node in the tree.
 * 
 * @author siegel
 * 
 */
public interface ParseTree {

	/**
	 * What kind of parse tree is this?
	 */
	RuleKind getKind();

	/**
	 * Gets the root of the tree.
	 * 
	 * @return the root
	 */
	CommonTree getRoot();

	/**
	 * Given a node in the parse tree, returns a source object for it.
	 * 
	 * @param tree
	 *            node in tree returned by method getTree()
	 * @return a source object describing the origin of that node in the source
	 *         code
	 */
	Source source(CommonTree tree);

	/**
	 * The source files from which this parse tree was derived.
	 * 
	 * @return the set of source files
	 */
	Collection<SourceFile> getSourceFiles();

	/**
	 * Creates a new syntax exception.
	 * 
	 * @param message
	 *            the message to print out when this exception is reported to
	 *            the user
	 * @param tree
	 *            the node in the ANTLR tree that led to this exception
	 * @return the new syntax exception
	 */
	SyntaxException newSyntaxException(String message, CommonTree tree);

	/**
	 * Returns a token source consisting of the tokens from the children of the
	 * given node in the tree. This is useful, for example, to obtain the token
	 * source from the body of a pragma node. Child number 1 of the pragma node
	 * would be passed as the argument to this method.
	 * 
	 * @param tokenListNode
	 *            a node in the ANTLR tree whose children are all leaf nodes
	 * @return a token source comprising the sequence of tokens obtained from
	 *         the children of the given node
	 */
	CTokenSequence getTokenSourceProducer(CommonTree tokenListNode);

}
