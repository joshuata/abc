package edu.udel.cis.vsl.abc.transform.common;

import java.util.Map;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class ScopeParameterRemover implements Transformer {

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
	// anywhere there is a scope identiifer, replace according to the
	// map. For an internal scope parameterized decl, iterate over
	// Results and add their maps to the current map and
	// make recursive call.

	private ASTNode substitute(Map<ScopeValue, Scope> map, ASTNode node) {
		// TODO

		return null;
	}

	@Override
	public AST transform(AST unit) throws SyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

}
