package edu.udel.cis.vsl.abc.ast.IF;

import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeVariable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public interface ASTFactory {

	/**
	 * Creates a new AST with the given root node. The root node is typically a
	 * translation unit, but it may also be the root of an AST formed by merging
	 * multiple translation units. This method also checks conformance with many
	 * facets of the C11 Standard, resolves references and adds this information
	 * to the AST, marks all the AST nodes reachable from root as "owned" by the
	 * new AST, and numbers the nodes from 0 to n-1, where n is the number of
	 * reachable nodes.
	 * 
	 * After this method returns, the nodes belonging to the new AST will be
	 * essentially immutable (with exceptions for certain "harmless" fields that
	 * cannot effect the correctness of the AST). If you want to modify the AST,
	 * you have to invoke its "release" method, which disolves the AST but
	 * leaves the nodes untouched and free (so mutable again), and then create a
	 * new AST once the modifications are complete. You may also invoke the
	 * AST's clone() method to create a new AST equivalent to the original, with
	 * all new nodes, if you want to keep the original AST.
	 * 
	 * Some of the interpretation that takes place:
	 * 
	 * <ul>
	 * <li>when creating the abstract type of a formal function parameter, the
	 * type "qualified array of T" is changed to "qualified pointer to T".</li>
	 * 
	 * <li>"f(void)" means 0 parameters. "f()" means unknown parameters.</li>
	 * 
	 * <li>determines and sets the cases in the switch statements</li>
	 * 
	 * 
	 * <ul>
	 * 
	 * @param root
	 *            the root node of the new AST
	 * @return the new AST
	 */
	AST newTranslationUnit(ASTNode root) throws SyntaxException;

	NodeFactory getNodeFactory();

	TokenFactory getTokenFactory();

	TypeFactory getTypeFactory();

	/**
	 * Returns the type obtained by replacing scopes in pointer modifiers as
	 * specified by the map.
	 * 
	 * @param type
	 *            any ABC Type
	 * @param map
	 *            map from scope variables to scope values
	 * @return type after substitutions performed
	 */
	Type substituteScopes(Type type, Map<ScopeVariable, ScopeValue> map);

}
