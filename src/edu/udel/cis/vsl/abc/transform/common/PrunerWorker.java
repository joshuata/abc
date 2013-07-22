package edu.udel.cis.vsl.abc.transform.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Variable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.AttributeKey;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.OrdinaryDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.type.IF.QualifiedObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.common.Pruner.Reachability;

/**
 * Finds all reachable nodes in the AST and marks them REACHABLE. Assumes that
 * the AST has already had the standard analyses performed, so that every
 * identifier has been resolved to refer to some Entity.
 * 
 * @author siegel
 * 
 */
public class PrunerWorker {

	private ASTNode root;

	private AttributeKey reachedKey;

	List<ASTNode> reachableNodes = new LinkedList<ASTNode>();

	public PrunerWorker(AttributeKey reachedKey, ASTNode root)
			throws SyntaxException {
		this.reachedKey = reachedKey;
		this.root = root;
		search();
	}

	/**
	 * Marks a node and its descendants reachable.
	 * 
	 * If <code>node</code> is already marked reachable, returns immediately.
	 * Otherwise marks the node as reachable and recurses over the node's
	 * children. For each child, if the child is an identifier node, the entity
	 * to which that identifier refers is explored. In addition, if the child is
	 * anything other than an ordinary declaration (i.e., a function or variable
	 * declaration) or a typedef declaration, then <code>markReachable</code> is
	 * invoked recursively on the child.
	 * 
	 * @param node
	 */
	private void markReachable(ASTNode node) {
		if (node.getAttribute(reachedKey) == Reachability.REACHABLE)
			return;
		else {
			Iterator<ASTNode> iter = node.children();

			node.setAttribute(reachedKey, Reachability.REACHABLE);
			reachableNodes.add(node);
			while (iter.hasNext()) {
				ASTNode child = iter.next();

				if (child != null) {
					if (child instanceof IdentifierNode) {
						Entity entity = ((IdentifierNode) child).getEntity();

						if (entity == null)
							throw new ASTException("Identifier not resolved: "
									+ child.getSource().getSummary());
						explore(entity);
					}
					if (child instanceof OrdinaryDeclarationNode
							|| child instanceof TypedefDeclarationNode) {
						// do nothing: we want to see if we can reach these
					} else
						markReachable(child);
				}
			}
		}
	}

	/**
	 * Invokes {@link #markReachable(ASTNode)} on the definition node for the
	 * entity. If the entity does not have a definition node, the first
	 * declaration is used. If it has no declaration it is ignored as it is a
	 * system entity.
	 * 
	 * @param entity
	 *            an Entity occurring in the AST
	 */
	private void explore(Entity entity) {
		DeclarationNode defn = entity.getDefinition();

		if (defn == null) {
			if (entity.getNumDeclarations() != 0)
				markReachable(entity.getFirstDeclaration());
		} else
			markReachable(defn);
	}

	/**
	 * Searches AST, marking reachable nodes as REACHABLE.
	 * 
	 * @return list of reachable nodes
	 * @throws SyntaxException
	 */
	private void search() throws SyntaxException {
		Scope rootScope = root.getScope();
		Function main = (Function) rootScope.getOrdinaryEntity("main");
		Iterator<Variable> iter = rootScope.getVariables();

		if (main == null) // no main method, can't prune anything.
			return;
		while (iter.hasNext()) {
			Variable variable = iter.next();
			Type type = variable.getType();

			if (type instanceof QualifiedObjectType) {
				QualifiedObjectType qotype = (QualifiedObjectType) type;

				if (qotype.isInputQualified() || qotype.isOutputQualified()) {
					VariableDeclarationNode decl = variable.getDefinition();

					if (decl == null)
						throw new ASTException(
								"No definition for input or output variable: "
										+ variable);
					markReachable(decl);
				}
			}
		}
		// if (main == null)
		// throw new ASTException("Program does not contain a main function");
		if (main.getDefinition() == null)
			throw new ASTException("Main function missing definition");
		explore(main);
	}

	public List<ASTNode> getReachableNodes() {
		return reachableNodes;
	}

}
