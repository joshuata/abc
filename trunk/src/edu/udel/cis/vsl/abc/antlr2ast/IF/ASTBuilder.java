package edu.udel.cis.vsl.abc.antlr2ast.IF;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
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
	AST getTranslationUnit() throws SyntaxException;

	/**
	 * Translates a single expression in an ANTLR tree.
	 * 
	 * @param expressionTree
	 *            an ANTLR tree node corresponding to an expression
	 * @param scope
	 *            the simple scope of the expression being translated
	 * @return the root of the ABC tree for the translated expression
	 * @throws SyntaxException
	 *             if there is a syntax error in the expression
	 */
	ExpressionNode translateExpression(CommonTree expressionTree,
			SimpleScope scope) throws SyntaxException;

}
