package edu.udel.cis.vsl.abc.transform.common;

import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.MPITransformer;

public class CommonMPITransformer implements MPITransformer {

	private ASTFactory astFactory;

	// private NodeFactory nodeFactory;

	@Override
	public AST transform(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		Function main = (Function) root.getScope().getOrdinaryEntity("main");

		if (main == null)
			return ast;
		if (main.getDefinition() == null)
			throw new ASTException("Main function missing definition");
		else {
			// MPITransformWorker worker;
			AST newAst = astFactory.newTranslationUnit(this.mpiMainNode(root));

			/*
			 * List<ASTNode> reachableNodes;
			 * 
			 * ast.release(); worker = new MPITransformWorker(root);
			 * reachableNodes = worker.getReachableNodes();
			 * close(reachableNodes); // TODO: for variable declarations: if the
			 * initializer is reachable // but not the declaration, need to
			 * replace that node with an // expression statement node.
			 * root.keepOnly(reachable); newAst =
			 * astFactory.newTranslationUnit(root);
			 */
			return newAst;
		}
	}

	private ASTNode mpiMainNode(ASTNode root) {
		return root;

	}

}
