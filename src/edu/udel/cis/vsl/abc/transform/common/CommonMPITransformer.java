package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonBasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonFunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonVoidTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.MPITransformer;

public class CommonMPITransformer implements MPITransformer {

	private ASTFactory astFactory;

	private NodeFactory nodeFactory;

	public CommonMPITransformer(ASTFactory astFactory, NodeFactory nodeFactory) {
		this.astFactory = astFactory;
		this.nodeFactory = nodeFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AST transform(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		Function main = (Function) root.getScope().getOrdinaryEntity("main");

		if (main == null)
			return ast;
		if (main.getDefinition() == null)
			throw new ASTException("Main function missing definition");
		else {
			AST newAst;
			ExpressionStatementNode callMain = nodeFactory
					.newExpressionStatementNode(nodeFactory
							.newFunctionCallNode(null, nodeFactory
									.newIdentifierExpressionNode(null,
											nodeFactory.newIdentifierNode(null,
													"main")),
									new ArrayList<ExpressionNode>(), null));

			ast.release();
			// add call main: main();
			((SequenceNode<ASTNode>) root).addSequenceChild(callMain);

			// build MPI_Process() function:
			// void MPI_Process(int _rank)
			// {
			// original program; (function/variable declaration/definition)
			// main();
			// }
			List<BlockItemNode> items = new LinkedList<BlockItemNode>();
			int number = root.numChildren();
			for (int i = 0; i < number; i++) {
				ASTNode child = root.child(i);
				root.removeChild(i);
				items.add((BlockItemNode) child);
			}
			CompoundStatementNode mpiMainBody = nodeFactory
					.newCompoundStatementNode(root.getSource(), items);
			List<VariableDeclarationNode> newFormalList = new LinkedList<VariableDeclarationNode>();

			newFormalList.add(nodeFactory.newVariableDeclarationNode(null,
					nodeFactory.newIdentifierNode(null, "_rank"),
					new CommonBasicTypeNode(null, BasicTypeKind.INT)));
			SequenceNode<VariableDeclarationNode> formals = nodeFactory
					.newSequenceNode(null, "FormalParameterDeclarations",
							newFormalList);
			FunctionTypeNode mpiMainType = new CommonFunctionTypeNode(null,
					new CommonVoidTypeNode(null), formals, true);
			FunctionDefinitionNode mpiProcess = nodeFactory
					.newFunctionDefinitionNode(null,
							nodeFactory.newIdentifierNode(null, "MPI_Process"),
							mpiMainType, null, mpiMainBody);
			/*
			 * $input int NPROCS; void main() { $proc procs[NPROCS];
			 * 
			 * for (int i=0; i<NPROCS; i++) procs[i] = $spawn MPI_Process(i);
			 * for (int i=0; i<NPROCS; i++) $wait(procs[i]);
			 * $gcomm_destroy(GCOMM_WORLD); }
			 */

			// $input int NPROCS;
			TypeNode nprocsType = new CommonBasicTypeNode(null,
					BasicTypeKind.INT);
			nprocsType.setInputQualified(true);
			VariableDeclarationNode nprocsVar = nodeFactory
					.newVariableDeclarationNode(null,
							nodeFactory.newIdentifierNode(null, "NPROCS"),
							nprocsType);
			// $gcomm GCOMM_WORLD = $gcomm_create($here, NPROCS);
			TypeNode gcommType = nodeFactory.newTypedefNameNode(
					nodeFactory.newIdentifierNode(null, "$gcomm"), null);
			List<ExpressionNode> gcommCreateArgs = new ArrayList<>(2);
			gcommCreateArgs.add(nodeFactory.newHereNode(null));
			gcommCreateArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "NPROCS")));
			ExpressionNode gcommCreate = nodeFactory.newFunctionCallNode(null,
					nodeFactory.newIdentifierExpressionNode(null, nodeFactory
							.newIdentifierNode(null, "$gcomm_create")),
					gcommCreateArgs, null);
			VariableDeclarationNode gcommWorld = nodeFactory
					.newVariableDeclarationNode(null,
							nodeFactory.newIdentifierNode(null, "GCOMM_WORLD"),
							gcommType, gcommCreate);

			// main
			/*
			 * void main() { $proc procs[NPROCS];
			 * 
			 * for (int i=0; i<NPROCS; i++) procs[i] = $spawn MPI_Process(i);
			 * for (int i=0; i<NPROCS; i++) $wait(procs[i]);
			 * $gcomm_destroy(GCOMM_WORLD); }
			 */
			items = new LinkedList<BlockItemNode>();
			TypeNode procsType = nodeFactory
					.newArrayTypeNode(null,
							nodeFactory.newTypedefNameNode(nodeFactory
									.newIdentifierNode(null, "$proc"), null),
							nodeFactory.newIdentifierExpressionNode(null,
									nodeFactory.newIdentifierNode(null,
											"NPROCS")));
			VariableDeclarationNode procsVar = nodeFactory
					.newVariableDeclarationNode(null,
							nodeFactory.newIdentifierNode(null, "procs"),
							procsType);
			items.add(procsVar);
			// first for loop:
			// nodeFactory.newForLoopInitializerNode(
			// statementSource, declarations)
			List<VariableDeclarationNode> initialList = new LinkedList<>();
			initialList.add(nodeFactory.newVariableDeclarationNode(null,
					nodeFactory.newIdentifierNode(null, "i"),
					new CommonBasicTypeNode(null, BasicTypeKind.INT)));
			ForLoopInitializerNode initializerNode = nodeFactory
					.newForLoopInitializerNode(null, initialList);
			List<ExpressionNode> operatorArgs = new LinkedList<>();
			operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "i")));
			operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "NPROCS")));

			ExpressionNode loopCondition = nodeFactory.newOperatorNode(null,
					Operator.LT, operatorArgs);
			operatorArgs = new LinkedList<>();
			operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "i")));

			ExpressionNode incrementer = nodeFactory.newOperatorNode(null,
					Operator.POSTINCREMENT, operatorArgs);
			List<ExpressionNode> callArgs = new ArrayList<>(1);
			callArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "i")));
			ExpressionNode spawnProc = nodeFactory.newSpawnNode(null,
					nodeFactory.newFunctionCallNode(null, nodeFactory
							.newIdentifierExpressionNode(null, nodeFactory
									.newIdentifierNode(null, "MPI_Process")),
							callArgs, null));
			operatorArgs = new LinkedList<>();
			operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "procs")));
			operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
					nodeFactory.newIdentifierNode(null, "i")));
			ExpressionNode leftHandSide = nodeFactory.newOperatorNode(null,
					Operator.SUBSCRIPT, operatorArgs);
			operatorArgs = new LinkedList<>();
			operatorArgs.add(leftHandSide);
			operatorArgs.add(spawnProc);
			StatementNode assign = nodeFactory
					.newExpressionStatementNode(nodeFactory.newOperatorNode(
							null, Operator.ASSIGN, operatorArgs));
			ForLoopNode forLoop = nodeFactory.newForLoopNode(null,
					initializerNode, loopCondition, incrementer, assign, null);

			/*
			 * ForLoopNode newForLoopNode(Source source, ForLoopInitializerNode
			 * initializer, ExpressionNode condition, ExpressionNode
			 * incrementer, StatementNode body, ExpressionNode invariant);
			 */

			// the translated program is:
			// input variables;
			// MPI_Process() definition;
			// main function.
			List<ASTNode> externalList = new LinkedList<>();
			externalList.add(nprocsVar);
			externalList.add(gcommWorld);
			externalList.add(mpiProcess);
			externalList.add(forLoop);

			SequenceNode<ASTNode> newRootNode = nodeFactory.newSequenceNode(
					null, "TranslationUnit", externalList);

			newAst = astFactory.newTranslationUnit(newRootNode);
			return newAst;
		}
	}
	// private ASTNode mpiMainNode(ASTNode root) {
	// return root;
	//
	// }

}
