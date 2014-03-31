package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
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
import edu.udel.cis.vsl.abc.transform.IF.BaseTransformer;

public class MPITransformer extends BaseTransformer {

	public static String CODE = "mpi";
	public static String LONG_NAME = "MPITransformer";
	public static String SHORT_DESCRIPTION = "transforms C/MPI program to CIVL-C";

	public MPITransformer(ASTFactory astFactory) {
		super(CODE, LONG_NAME, SHORT_DESCRIPTION, astFactory);
	}

	private FunctionDefinitionNode mpiProcess(SequenceNode<ASTNode> root) {
		List<BlockItemNode> items;
		int number;
		ExpressionStatementNode callMain;
		CompoundStatementNode mpiMainBody;
		List<VariableDeclarationNode> newFormalList;
		SequenceNode<VariableDeclarationNode> formals;
		FunctionTypeNode mpiProcessType;
		FunctionDefinitionNode mpiProcess;

		callMain = nodeFactory.newExpressionStatementNode(nodeFactory
				.newFunctionCallNode(null, nodeFactory
						.newIdentifierExpressionNode(null,
								nodeFactory.newIdentifierNode(null, "main")),
						new ArrayList<ExpressionNode>(), null));
		// add call main: main();
		root.addSequenceChild(callMain);
		// build MPI_Process() function:
		// void MPI_Process(int _rank)
		// {
		// original program; (function/variable declaration/definition)
		// main();
		// }
		items = new LinkedList<BlockItemNode>();
		number = root.numChildren();
		for (int i = 0; i < number; i++) {
			ASTNode child = root.child(i);
			root.removeChild(i);
			items.add((BlockItemNode) child);
		}
		mpiMainBody = nodeFactory.newCompoundStatementNode(root.getSource(),
				items);
		newFormalList = new LinkedList<>();
		newFormalList.add(nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "_rank"),
				new CommonBasicTypeNode(null, BasicTypeKind.INT)));
		formals = nodeFactory.newSequenceNode(null,
				"FormalParameterDeclarations", newFormalList);
		mpiProcessType = new CommonFunctionTypeNode(null,
				new CommonVoidTypeNode(null), formals, true);
		mpiProcess = nodeFactory.newFunctionDefinitionNode(null,
				nodeFactory.newIdentifierNode(null, "MPI_Process"),
				mpiProcessType, null, mpiMainBody);
		return mpiProcess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AST transform(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		Function main = (Function) root.getScope().getOrdinaryEntity("main");

		assert this.astFactory == ast.getASTFactory();
		assert this.nodeFactory == astFactory.getNodeFactory();
		if (main == null)
			return ast;
		if (main.getDefinition() == null)
			throw new ASTException("Main function missing definition");
		else {
			AST newAst;
			FunctionDefinitionNode mpiProcess, mainFunction;
			VariableDeclarationNode gcommWorld;
			List<ASTNode> externalList;
			VariableDeclarationNode nprocsVar;
			SequenceNode<ASTNode> newRootNode;

			ast.release();
			// $input int NPROCS;
			nprocsVar = this.nprocsDeclaration();
			// $gcomm GCOMM_WORLD = $gcomm_create($here, NPROCS);
			gcommWorld = this.gcommDeclaration();
			// MPI_Process(__rank){}
			mpiProcess = this.mpiProcess((SequenceNode<ASTNode>) ast
					.getRootNode());
			// main function
			mainFunction = mainFunction();
			// the translated program is:
			// input variables;
			// gcomm
			// MPI_Process() definition;
			// main function.
			externalList = new LinkedList<>();
			externalList.add(nprocsVar);
			externalList.add(gcommWorld);
			externalList.add(mpiProcess);
			externalList.add(mainFunction);
			newRootNode = nodeFactory.newSequenceNode(null, "TranslationUnit",
					externalList);
			newAst = astFactory.newTranslationUnit(newRootNode);
			return newAst;
		}
	}

	private VariableDeclarationNode nprocsDeclaration() {
		// $input int NPROCS;
		TypeNode nprocsType = new CommonBasicTypeNode(null, BasicTypeKind.INT);

		nprocsType.setInputQualified(true);
		return nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "NPROCS"), nprocsType);
	}

	private VariableDeclarationNode gcommDeclaration() {
		TypeNode gcommType;
		List<ExpressionNode> gcommCreateArgs;
		ExpressionNode gcommCreate;

		gcommType = nodeFactory.newTypedefNameNode(
				nodeFactory.newIdentifierNode(null, "$gcomm"), null);
		gcommCreateArgs = new ArrayList<>(2);
		gcommCreateArgs.add(nodeFactory.newHereNode(null));
		gcommCreateArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "NPROCS")));
		gcommCreate = nodeFactory.newFunctionCallNode(
				null,
				nodeFactory.newIdentifierExpressionNode(null,
						nodeFactory.newIdentifierNode(null, "$gcomm_create")),
				gcommCreateArgs, null);

		return nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "GCOMM_WORLD"), gcommType,
				gcommCreate);
	}

	private FunctionDefinitionNode mainFunction() {
		List<BlockItemNode> items = new LinkedList<BlockItemNode>();
		TypeNode procsType;
		VariableDeclarationNode procsVar;
		List<VariableDeclarationNode> initialList;
		ForLoopInitializerNode initializerNode;
		List<ExpressionNode> operatorArgs;
		ExpressionNode loopCondition, incrementer, spawnProc, leftHandSide;
		List<ExpressionNode> callArgs;
		StatementNode assign;
		ForLoopNode forLoop;
		CompoundStatementNode mainBody;
		LinkedList<VariableDeclarationNode> newFormalList;
		SequenceNode<VariableDeclarationNode> formals;
		FunctionTypeNode mainType;
		FunctionDefinitionNode mainFunction;

		// main
		/*
		 * void main() { $proc procs[NPROCS];
		 * 
		 * for (int i=0; i<NPROCS; i++) procs[i] = $spawn MPI_Process(i); for
		 * (int i=0; i<NPROCS; i++) $wait(procs[i]);
		 * $gcomm_destroy(GCOMM_WORLD); }
		 */
		procsType = nodeFactory.newArrayTypeNode(
				null,
				nodeFactory.newTypedefNameNode(
						nodeFactory.newIdentifierNode(null, "$proc"), null),
				nodeFactory.newIdentifierExpressionNode(null,
						nodeFactory.newIdentifierNode(null, "NPROCS")));
		procsVar = nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "procs"), procsType);
		items.add(procsVar);

		// first for loop:
		// nodeFactory.newForLoopInitializerNode(
		// statementSource, declarations)
		initialList = new LinkedList<>();
		initialList.add(nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "i"),
				new CommonBasicTypeNode(null, BasicTypeKind.INT)));
		initializerNode = nodeFactory.newForLoopInitializerNode(null,
				initialList);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "i")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "NPROCS")));

		loopCondition = nodeFactory.newOperatorNode(null, Operator.LT,
				operatorArgs);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "i")));

		incrementer = nodeFactory.newOperatorNode(null, Operator.POSTINCREMENT,
				operatorArgs);
		callArgs = new ArrayList<>(1);
		callArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "i")));
		spawnProc = nodeFactory.newSpawnNode(null, nodeFactory
				.newFunctionCallNode(null, nodeFactory
						.newIdentifierExpressionNode(null, nodeFactory
								.newIdentifierNode(null, "MPI_Process")),
						callArgs, null));
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "procs")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(null,
				nodeFactory.newIdentifierNode(null, "i")));
		leftHandSide = nodeFactory.newOperatorNode(null, Operator.SUBSCRIPT,
				operatorArgs);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(leftHandSide);
		operatorArgs.add(spawnProc);
		assign = nodeFactory.newExpressionStatementNode(nodeFactory
				.newOperatorNode(null, Operator.ASSIGN, operatorArgs));
		forLoop = nodeFactory.newForLoopNode(null, initializerNode,
				loopCondition, incrementer, assign, null);
		items.add(forLoop);

		mainBody = nodeFactory.newCompoundStatementNode(null, items);

		newFormalList = new LinkedList<>();
		newFormalList.add(nodeFactory.newVariableDeclarationNode(null,
				nodeFactory.newIdentifierNode(null, "_rank"),
				new CommonBasicTypeNode(null, BasicTypeKind.INT)));
		formals = nodeFactory.newSequenceNode(null,
				"FormalParameterDeclarations", newFormalList);
		mainType = new CommonFunctionTypeNode(null,
				new CommonVoidTypeNode(null), formals, true);
		mainFunction = nodeFactory.newFunctionDefinitionNode(null,
				nodeFactory.newIdentifierNode(null, "MPI_Process"), mainType,
				null, mainBody);

		return mainFunction;
	}
}
