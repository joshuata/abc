package edu.udel.cis.vsl.abc.antlr2ast.IF;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface ASTBuilderWorker {

	SequenceNode<ExternalDefinitionNode> translateRoot() throws SyntaxException;

	ExpressionNode translateExpression(CommonTree expressionTree,
			SimpleScope scope) throws SyntaxException;

	/**
	 * Translates an ANTLR {@link CommonTree} node of type BLOCK_ITEM. The
	 * result is a list of {@link BlockItemNode} because it is possible for a
	 * single ANTLR block item node to yield several AST {@link BlockItemNode}s.
	 * This can happen for example, if the block item is a declaration such as
	 * <code>int x,y</code> which is translated to two AST nodes, roughly
	 * corresponding to <code>int x</code> and <code>int y</code>.
	 * 
	 * @param blockItemTree
	 * @param scope
	 * @return
	 * @throws SyntaxException
	 */
	List<BlockItemNode> translateBlockItem(CommonTree blockItemTree,
			SimpleScope scope) throws SyntaxException;

}
