package edu.udel.cis.vsl.abc.transform.common;

import java.util.Iterator;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.AttributeKey;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.NodePredicate;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

/**
 * Prunes unreachable objects from an AST. Starting from the "main" function,
 * this transformer performs a search of the nodes that can be reached by
 * following children other than ordinary declarations and typedef declarations.
 * When an identifier is encountered, the definition or declaration of the
 * entity to which it refers is also searched. Hence only those
 * declarations/definitions that are actually used will be enountered in the
 * search.
 * 
 * Once the reachable nodes have been determined, the set of reachable nodes is
 * "closed" by marking all ancestors of reachable nodes reachable.
 * 
 * This transformer assumes the given AST is a closed program. It also assumes
 * that the standard analysis has been performed, so that identifiers have
 * entities associted to them.
 * 
 * The AST nodes are modified and re-used. If you want to keep the original AST
 * intact, you should clone it before performing this transformation.
 * 
 * The AST returned will be pruned, but will have not the standard analyses
 * encoded in it. If you want them, they should be invoked on the new AST.
 * 
 * @author siegel
 * 
 */
public class Pruner implements Transformer {

	public enum Reachability {
		/**
		 * Indicates this node is unreachable and can therefore be pruned from
		 * the AST.
		 */
		UNREACHABLE,
		/**
		 * Indicates this node is reachable and must therefore be kept in the
		 * AST.
		 */
		REACHABLE,
		/**
		 * Indicates that not only is this node reachable, but all of its
		 * ancestors have also been marked reachable (in fact, all ancestors
		 * have been marked reachable and closed).
		 */
		KEEP
	};

	private ASTFactory astFactory;

	private NodeFactory nodeFactory;

	private AttributeKey reachedKey;

	private NodePredicate reachable;

	public Pruner(ASTFactory astFactory) {
		this.astFactory = astFactory;
		this.nodeFactory = astFactory.getNodeFactory();
		reachedKey = nodeFactory.newAttribute("reached", Reachability.class);
		reachable = new NodePredicate() {

			@Override
			public boolean holds(ASTNode node) {
				return node.getAttribute(reachedKey) == Reachability.KEEP;
			}

		};
	}

	private void markAllUnreachable(ASTNode node) {
		if (node == null)
			return;
		else {
			Iterator<ASTNode> children = node.children();

			node.setAttribute(reachedKey, Reachability.UNREACHABLE);
			while (children.hasNext())
				markAllUnreachable(children.next());
		}
	}

	/**
	 * Change status of all reachable nodes and their ancestors to KEEP.
	 * 
	 * @param ast
	 *            the AST which has already been analyzed by the worker for
	 *            REACHABLE nodes
	 */
	private void close(List<ASTNode> reachableNodes) {
		for (ASTNode node : reachableNodes) {
			while (node != null
					&& node.getAttribute(reachedKey) != Reachability.KEEP) {
				node.setAttribute(reachedKey, Reachability.KEEP);
				node = node.parent();
			}
		}
	}
	
	// /**
	// * If any variable decl nodes have reachable initializer but unreachable
	// * identifier and type nodes, replace those nodes with a simple
	// * expression statement node wrapping the initializer.
	// *
	// * @param root
	// */
	// private void changeDecls(ASTNode node) {
	// int numChildren = node.numChildren();
	//
	// for (int i=0; i<numChildren; i++) {
	// ASTNode child = node.child(i);
	//
	// if (child instanceof VariableDeclarationNode) {
	// VariableDeclarationNode decl = (VariableDeclarationNode)child;
	// InitializerNode init = decl.getInitializer();
	//
	// if (init != null && init.getAttribute(reachedKey) == Reachability.KEEP) {
	// // if all other children of decl will not be kept, do transform...
	// boolean modify = true;
	// Iterator<ASTNode> iter = decl.children();
	//
	// while (iter.hasNext()) {
	// ASTNode grandchild = iter.next();
	//
	// if (grandchild != null && grandchild != init &&
	// grandchild.getAttribute(reachedKey)==Reachability.KEEP) {
	// modify = false;
	// break;
	// }
	// }
	// if (modify) {
	// // replace child with new ExpressionStatement
	//
	// }
	// }
	//
	// } else {
	// changeDecls(child);
	// }
	// }
	// }

	@Override
	public AST transform(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		Function main = (Function) root.getScope().getOrdinaryEntity("main");

		if (main == null)
			return ast;
		if (main.getDefinition() == null)
			throw new ASTException("Main function missing definition");
		else {
			PrunerWorker worker;
			AST newAst;
			List<ASTNode> reachableNodes;

			ast.release();
			markAllUnreachable(root);
			worker = new PrunerWorker(reachedKey, root);
			reachableNodes = worker.getReachableNodes();
			close(reachableNodes);
			// TODO: for variable declarations: if the initializer is reachable
			// but not the declaration, need to replace that node with an
			// expression statement node.
			root.keepOnly(reachable);
			newAst = astFactory.newTranslationUnit(root);
			return newAst;
		}
	}

}
