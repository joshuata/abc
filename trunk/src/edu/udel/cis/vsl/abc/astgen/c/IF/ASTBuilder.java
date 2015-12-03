package edu.udel.cis.vsl.abc.astgen.c.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * An object which translates an ANTLR tree to an ABC AST.
 * 
 * @author siegel
 * 
 */
public interface ASTBuilder {

	/**
	 * Builds the AST specified by a {@link ParseTree} which represents a
	 * translation unit.
	 * 
	 * @param config
	 *            the configuration of the translation task, e.g., if svcomp is
	 *            enabled
	 * @return the AST
	 * @throws SyntaxException
	 *             if something is wrong with the object being translated into
	 *             an ABC
	 */
	AST getTranslationUnit(Configuration config, ParseTree tree)
			throws SyntaxException;

	/**
	 * Creates a worker which can be used to perform more specific translation
	 * tasks related to a single {@link ParseTree}. The worker can be used to
	 * translate a single expression, a single block item, etc.
	 * 
	 * @param tree
	 *            a {@link ParseTree}
	 * @return a worker for performing specific translation tasks related to
	 *         that parse tree
	 */
	ASTBuilderWorker getWorker(ParseTree tree);

	/**
	 * Gets the {@link ASTFactory} used by this builder to create new
	 * {@link ASTNode}s and other {@link AST} components.
	 * 
	 * @return the {@link ASTFactory} used by this builder
	 */
	ASTFactory getASTFactory();

	/**
	 * Gets the {@link PragmaFactory} used by this builder to translate pragmas
	 * that occur in the parse tree.
	 * 
	 * @return the pragma factory used by this builder
	 */
	PragmaFactory getPragmaFactory();

}
