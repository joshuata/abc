package edu.udel.cis.vsl.abc.antlr2ast.IF;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * An object which translates an ANTLR tree to an ABC AST.
 * 
 * @author siegel
 * 
 */
public interface ASTBuilder {

	/**
	 * Builds the AST.
	 * 
	 * @return the AST
	 * @throws SyntaxException
	 *             if something is wrong with the object being translated into
	 *             an ABC
	 */
	AST getTranslationUnit(ParseTree tree) throws SyntaxException;

	ASTBuilderWorker getWorker(ParseTree tree);

	/**
	 * Gets the {@link ASTFactory} used by this builder to create new
	 * {@link ASTNode}s and other {@link AST} components.
	 * 
	 * @return the {@link ASTFactory} used by this builder
	 */
	ASTFactory getASTFactory();

	PragmaFactory getPragmaFactory();

}
