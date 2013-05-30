package edu.udel.cis.vsl.abc.transform.common;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

/**
 * Prunes unreachable objects from a TranslationUnit.
 * 
 * Strategy: objects include: ASTNodes, Types, Entities, Scopes
 * 
 * Assume closed program.  Start from main method: parameters and
 * statement body (are marked reachable).  Do DFS on nodes from there.
 * Is it possible something reaches the node going through a non-node?
 * Get rid of the unreachable nodes.  Yes: example: decl node has type
 * node that names a typedef.  follow the type, you get to the typedef.
 * so need to include other objects in the DFS.
 * 
 * Certain nodes: you need all their children.
 * Others: not necessarily.  For example don't keep any decl nodes
 * unless they are referenced from somewhere else.
 * 
 * Eliminate any ordinary declaration that is never used?
 * 
 * statements, expressions, typesrequire all children.
 * 
 * Do DFS, but skip any OrdinaryDeclarationNodes -- mark them only
 * if throw an entity.  When it comes to nodes: hit all children
 * and additional object; types: go immediately to decls; entities:
 * 
 * types: need to find the type nodes with that type can keep the node.
 * 
 * so: first pass, mark the reachable types  second pass: mark
 * the reachable type nodes.
 * 
 * Conclusions: add reachable bits to nodes.
 * set all to false.
 * to dfs starting from main body.
 * 
 * iterate over all nodes in mark them unreachable.
 * then dfs(main.body)
 * 
 * dfs (node)
 *   - if node is already marked reachable, return
 *   - mark node reachable
 *   - if IdentifierNode, recurse on Entity object's decl node(s),
 *     then set entity to null.
 *   - dfs on all children that are not decl nodes
 *   - dfs on parent
 *   - return
 * 
 * remove unreachables: dfs on node.  let newChildren be a new list.
 * iterate over (old) children.  if child is reachable, add to list.
 * make new children the children of this node.  iterate over
 * (new) children and call dfs on each.
 * 
 * make new TranslationUnit,
 *   perform standard analyses to rebuild Types, Entities, Scopes 
 * 
 * @author siegel
 *
 */
public class Pruner implements Transformer {

	@Override
	public AST transform(AST unit)
			throws SyntaxException {
		
		// TODO Auto-generated method stub
		return null;
	}

}
