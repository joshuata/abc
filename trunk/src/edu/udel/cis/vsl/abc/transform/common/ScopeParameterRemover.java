package edu.udel.cis.vsl.abc.transform.common;

import java.util.Map;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeVariable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ScopeOfNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class ScopeParameterRemover implements Transformer {

	// THIS WON'T WORK.  IF THERE IS A CYCLE, YOU GET INFINITE
	// NUMBER OF INSTANCES OF FUNCTIONS.
	
	/**
	 * Statically evaluates an expression of scope type to get a concrete scope.
	 * Requires a mapping from ScopeVariable to Scope for any variable of scope
	 * type that may occur in that expression.
	 * 
	 * The expressions of scope type are as follows:
	 * <ul>
	 * <li>{@link ScopeOfNode}: this take as argument a variable and returns the
	 * scope in which that variable is declared.</li>
	 * <li>{@link IdentifierExpressionNode}: this will be a scope variable. It
	 * has an Entity which is a ScopeVariable: look it up in the map.</li>
	 * <li>others are coming (join expressions, etc.).</li>
	 * </ul>
	 * 
	 * @param map
	 *            for each variable of scope type which may occur in the
	 *            expression, a concrete scope
	 * @param scopeExpr
	 *            an expression of scope type to be evaluated
	 * @return the result of evaluating the scope expression
	 */
	Scope evaluate(Map<ScopeVariable, Scope> map, ExpressionNode scopeExpr) {
		// TODO
		return null;
	}

	/**
	 * Given an ASTNode node, returns a new node obtained from the original
	 * except that it and all descendants have scope values replaced with the
	 * concrete scopes specified by the map.
	 *  
	 * 
	 * TypedefNameNode: has sequence of expression nodes, each of which
	 * has scope type.  Each expression is replaced with an
	 * IdentifierExpressionNode for which the identifier is a ...
	 * can you do this.  Point is, we know the scope.
	 * 
	 * So it turns out you can use scopes (e.g., *<s> is a use of s)
	 * even in locations where s is not visible:
	 * 
	 * HOW ABOUT A RULE. Given parameterized declaration <x> D in Scope S,
	 * D can only be instantiated with scopes that are descendants of S
	 * but not descendants of D.
	 * <pre>
	 * 
	 * $scope s1;
	 * int x;
	 * <t1> f(int *<t1> p) {
	 *   { $scope s2; int y;
	 *     g() {
	 *       f<s2>(&y);
	 *     }
	 *   }
	 * }
	 * main() {
	 *   f<s1>(&x);
	 * }
	 * 
	 * translates to
	 * 
	 * $scope s1;
	 * int x;
	 * f$1(int *<s1> p) {
	 *   { $scope s2$1; int y;
	 *     g() {
	 *       f$2(&y);
	 *     }
	 *   }
	 * }
	 * f$2(int *<s2$1> p) { // which s2? it doesn't exist yet.
	 *   { $scope s2$2; int y;
	 *     g() {
	 *       f$2(&y);
	 *     }
	 *   }
	 * }
	 * main() {
	 *   f$1();
	 * }
	 * 
	 * </pre>
	 * 
	 * Is there an alternative to this scope removal?
	 * Make it dynamic?
	 * 
	 * FunctionCallNode: has sequence of expression nodes, each of which
	 * evaluates to a scope value
	 * 
	 * PointerTypeNode: has scope modifier expression node, which evaluates to a
	 * scope value
	 * 
	 * need to do this for structures and unions too.
	 * 
	 * Need a method to evaluate an expression of scope type and return a Scope.
	 * What are the different expression of scope type: ScopeOfNode,
	 * IdentifierExpressionNode.
	 * 
	 * 
	 * 
	 * @param map
	 * @param node
	 * @return
	 */
	private ASTNode substitute(Map<ScopeVariable, Scope> map, ASTNode node) {
		// TODO

		return null;
	}

	
	// start with all declarations in outermost scope that
	// are not parameterized. Look at parameterized instantiations
	// (functions calls, types) in them. Each yields an
	// assignment of concrete scopes to parameters.
	// Let f be the function being called, s1,...,sn
	// the concrete scope actuals, t1,...,tn the corresponding
	// formals. Add to worklist: <f, (t1->s1,...,tn->sn)>

	// Let Result be the empty set.

	// Remove an item from the worklist <f, (t1->s1,...,tn->sn)>.
	// Add the item to Result. If the item was already there,
	// do nothing. Else, proceed as follows:

	// Look at all parameterized instantiations in f.
	// Say one is g with actuals u_i and corresponding formals v_i.
	// You must be able to evaluate u_i to a concrete scope u'_i:
	// either it is concrete or it is a key in the f map, i.e.,
	// it is one of the t_j, and u'_i=s_j. Add to the worklist
	// <g, (t1->s1,...,tn->sn,v1->u'_1,...)>.

	// Continue until worklist is empty. (Termination: only a finite
	// number of concrete scopes, declarations, parameters)

	// Transformation: recursively: iterate over outermost decls.
	// If it is parameterized, find all elements in Result
	// for that decl. Iterate over them. Create a new decl node
	// in the AST with name modified to include the concrete
	// scope names, e.g., f$s1$s2. Define substitute method
	// which takes an AST node and a map from scope values to
	// scopes and produces a new AST node (recursively). Think
	// of cases where a scope can appear: in a pointer type node
	// (*<s>) or function call or type instantiation. Basically,
	// anywhere there is a scope identifier, replace according to the
	// map. For an internal scope parameterized decl, iterate over
	// Results and add their maps to the current map and
	// make recursive call.


	@Override
	public AST transform(AST unit) throws SyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

}
