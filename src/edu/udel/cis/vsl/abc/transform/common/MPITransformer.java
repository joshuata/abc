package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode.NodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.BaseTransformer;

public class MPITransformer extends BaseTransformer {

	public static String CODE = "mpi";
	public static String LONG_NAME = "MPITransformer";
	public static String SHORT_DESCRIPTION = "transforms C/MPI program to CIVL-C";

	private static String COMM_WORLD = "MPI_COMM_WORLD";
	private static String GCOMM_WORLD = "GCOMM_WORLD";
	private static String GCOMM_TYPE = "$gcomm";
	private static String COMM_TYPE = "$comm";
	private static String GCOMM_CREATE = "$gcomm_create";
	private static String COMM_CREATE = "$comm_create";
	private static String GCOMM_DESTROY = "$gcomm_destroy";
	private static String COMM_DESTROY = "$comm_destroy";

	private Source source;

	public MPITransformer(ASTFactory astFactory) {
		super(CODE, LONG_NAME, SHORT_DESCRIPTION, astFactory);
	}

	private FunctionDefinitionNode mpiProcess(SequenceNode<ASTNode> root,
			List<ASTNode> includedNodes, List<VariableDeclarationNode> vars) {
		List<BlockItemNode> items;
		int number;
		ExpressionStatementNode callMain;
		CompoundStatementNode mpiProcessBody;
		List<VariableDeclarationNode> newFormalList;
		SequenceNode<VariableDeclarationNode> formals;
		FunctionTypeNode mpiProcessType;
		FunctionDefinitionNode mpiProcess;
		VariableDeclarationNode commVar = this.commDeclaration();
		ExpressionStatementNode commDestroy = this.commDestroy(COMM_DESTROY,
				COMM_WORLD);

		callMain = nodeFactory.newExpressionStatementNode(nodeFactory
				.newFunctionCallNode(source,
						nodeFactory
								.newIdentifierExpressionNode(source,
										nodeFactory.newIdentifierNode(source,
												"__main")),
						new ArrayList<ExpressionNode>(), null));
		// add call main: main();
		// root.addSequenceChild(callMain);
		// build MPI_Process() function:
		// void MPI_Process(int _rank)
		// {
		// initialize MPI_COMM_WORLD: $comm MPI_COMM_WORLD=$comm_create($here,
		// GCOMM_WORLD, _rank);
		// original program; (function/variable declaration/definition)
		// function call: main();
		// $comm_destroy(MPI_COMM_WORLD);
		// }
		items = new LinkedList<>();
		number = root.numChildren();
		items.add(commVar);
		for (int i = 0; i < number; i++) {
			ASTNode child = root.child(i);
			String sourceFile = child.getSource().getFirstToken()
					.getSourceFile().getName();

			root.removeChild(i);
			if (sourceFile.endsWith(".h")) {
				includedNodes.add(child);
			} else {
				if (child.nodeKind() == NodeKind.FUNCTION_DEFINITION) {
					FunctionDefinitionNode functionNode = (FunctionDefinitionNode) child;
					IdentifierNode functionName = (IdentifierNode) functionNode
							.child(0);

					if (functionName.name().equals("main")) {
						FunctionTypeNode functionType = functionNode
								.getTypeNode();
						SequenceNode<VariableDeclarationNode> parameters = functionType
								.getParameters();
						int count = parameters.numChildren();

						functionName.setName("__main");
						if (count > 0) {
							List<VariableDeclarationNode> newParameters = new ArrayList<>(
									0);

							for (int k = 0; k < count; k++) {
								VariableDeclarationNode parameter = parameters
										.getSequenceChild(k);

								parameters.removeChild(k);
								parameter.getTypeNode().setInputQualified(true);
								vars.add(parameter);
							}
							functionType.setParameters(nodeFactory
									.newSequenceNode(source,
											"FormalParameterDeclarations",
											newParameters));
						}
					}
				}
				items.add((BlockItemNode) child);
			}
		}
		items.add(callMain);
		items.add(commDestroy);
		mpiProcessBody = nodeFactory.newCompoundStatementNode(root.getSource(),
				items);
		newFormalList = new LinkedList<>();
		newFormalList.add(nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "_rank"),
				nodeFactory.newBasicTypeNode(source, BasicTypeKind.INT)));
		formals = nodeFactory.newSequenceNode(source,
				"FormalParameterDeclarations", newFormalList);
		mpiProcessType = nodeFactory.newFunctionTypeNode(source,
				nodeFactory.newVoidTypeNode(source), formals, true);
		mpiProcess = nodeFactory.newFunctionDefinitionNode(source,
				nodeFactory.newIdentifierNode(source, "MPI_Process"),
				mpiProcessType, null, mpiProcessBody);
		return mpiProcess;
	}

	private ExpressionStatementNode commDestroy(String destroy, String commName) {
		// $comm_destroy
		IdentifierExpressionNode function = nodeFactory
				.newIdentifierExpressionNode(source,
						nodeFactory.newIdentifierNode(source, destroy));
		List<ExpressionNode> arguments = new ArrayList<>(1);

		arguments.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, commName)));
		return nodeFactory.newExpressionStatementNode(nodeFactory
				.newFunctionCallNode(source, function, arguments, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public AST transform(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		AST newAst;
		FunctionDefinitionNode mpiProcess, mainFunction;
		VariableDeclarationNode gcommWorld;
		List<ASTNode> externalList;
		VariableDeclarationNode nprocsVar;
		SequenceNode<ASTNode> newRootNode;
		// List<ASTNode> civlcAstNodes = new ArrayList<>();
		// List<ASTNode> mpiAstNodes = new ArrayList<>();
		List<ASTNode> includedNodes = new ArrayList<ASTNode>();
		List<VariableDeclarationNode> mainParameters = new ArrayList<>();
		int count;

		this.source = root.getSource();
		assert this.astFactory == ast.getASTFactory();
		assert this.nodeFactory == astFactory.getNodeFactory();
		ast.release();
		// $input int NPROCS;
		nprocsVar = this.nprocsDeclaration();
		// $gcomm GCOMM_WORLD = $gcomm_create($here, NPROCS);
		gcommWorld = this.gcommDeclaration();
		// MPI_Process(__rank){}
		mpiProcess = this.mpiProcess((SequenceNode<ASTNode>) root,
				includedNodes, mainParameters);
		// main function
		mainFunction = mainFunction();
		// the translated program is:
		// input variables;
		// gcomm
		// MPI_Process() definition;
		// main function.
		externalList = new LinkedList<>();
		// count = civlcAstNodes.size();
		// for (int i = 0; i < count; i++) {
		// externalList.add(civlcAstNodes.get(i));
		// }
		count = includedNodes.size();
		for (int i = 0; i < count; i++) {
			externalList.add(includedNodes.get(i));
		}
		count = mainParameters.size();
		for (int i = 0; i < count; i++) {
			externalList.add(mainParameters.get(i));
		}
		externalList.add(nprocsVar);
		externalList.add(gcommWorld);
		externalList.add(mpiProcess);
		externalList.add(mainFunction);
		newRootNode = nodeFactory.newSequenceNode(null, "TranslationUnit",
				externalList);
		newAst = astFactory.newTranslationUnit(newRootNode);
		return newAst;
	}

	// private List<ASTNode> astNodesOfFile(String fileName)
	// throws PreprocessorException, ParseException, SyntaxException {
	// AST ast = this.parseFile(fileName);
	// ASTNode root = ast.getRootNode();
	// int number = root.numChildren();
	// List<ASTNode> list = new ArrayList<>(number);
	//
	// for (int i = 0; i < number; i++) {
	// list.add(root.child(i));
	// }
	// ast.release();
	// for (int i = 0; i < number; i++) {
	// root.removeChild(i);
	// }
	// return list;
	// }
	//
	// private AST parseFile(String fileName) throws PreprocessorException,
	// ParseException, SyntaxException {
	// AST ast;
	// CParser parser;
	// CommonTree tree;
	// ASTBuilder builder;
	// PreprocessorFactory preprocessorFactory = Preprocess
	// .newPreprocessorFactory();
	// Preprocessor preprocessor = preprocessorFactory.newPreprocessor(
	// new File[0], new File[0]);
	// String root = new File(".").getAbsolutePath();
	// String suffix = "text/include/" + fileName;
	// File file = new File(new File(root), suffix);
	//
	// parser = Parse.newCParser(preprocessor, file);
	// tree = parser.getTree();
	// ast = null;
	// builder = new ASTBuilder(parser, astFactory, tree);
	// ast = builder.getTranslationUnit(); // creates ast
	// return ast;
	// }

	private VariableDeclarationNode nprocsDeclaration() {
		// $input int NPROCS;
		TypeNode nprocsType = nodeFactory.newBasicTypeNode(source,
				BasicTypeKind.INT);

		nprocsType.setInputQualified(true);
		return nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "NPROCS"), nprocsType);
	}

	private VariableDeclarationNode commDeclaration() {
		TypeNode commType;
		List<ExpressionNode> commCreateArgs;
		ExpressionNode commCreate;

		commType = nodeFactory.newTypedefNameNode(
				nodeFactory.newIdentifierNode(source, COMM_TYPE), null);
		commCreateArgs = new ArrayList<>(3);
		commCreateArgs.add(nodeFactory.newHereNode(source));
		commCreateArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, GCOMM_WORLD)));
		commCreateArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "_rank")));
		commCreate = nodeFactory.newFunctionCallNode(
				source,
				nodeFactory.newIdentifierExpressionNode(source,
						nodeFactory.newIdentifierNode(source, COMM_CREATE)),
				commCreateArgs, null);
		return nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "MPI_COMM_WORLD"),
				commType, commCreate);
	}

	private VariableDeclarationNode gcommDeclaration() {
		TypeNode gcommType;
		List<ExpressionNode> gcommCreateArgs;
		ExpressionNode gcommCreate;

		gcommType = nodeFactory.newTypedefNameNode(
				nodeFactory.newIdentifierNode(source, GCOMM_TYPE), null);
		gcommCreateArgs = new ArrayList<>(2);
		gcommCreateArgs.add(nodeFactory.newHereNode(source));
		gcommCreateArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "NPROCS")));
		gcommCreate = nodeFactory.newFunctionCallNode(
				source,
				nodeFactory.newIdentifierExpressionNode(source,
						nodeFactory.newIdentifierNode(source, GCOMM_CREATE)),
				gcommCreateArgs, null);
		return nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "GCOMM_WORLD"),
				gcommType, gcommCreate);
	}

	private FunctionDefinitionNode mainFunction() throws SyntaxException {
		List<BlockItemNode> items = new LinkedList<BlockItemNode>();
		TypeNode procsType;
		VariableDeclarationNode procsVar;
		List<VariableDeclarationNode> initialList;
		ForLoopInitializerNode initializerNode;
		List<ExpressionNode> operatorArgs;
		ExpressionNode loopCondition, incrementer, spawnProc, waitProc, leftHandSide;
		List<ExpressionNode> callArgs;
		StatementNode assign;
		ForLoopNode forLoop;
		CompoundStatementNode mainBody;
		LinkedList<VariableDeclarationNode> newFormalList;
		SequenceNode<VariableDeclarationNode> formals;
		FunctionTypeNode mainType;
		FunctionDefinitionNode mainFunction;
		ExpressionStatementNode gcommDestroy = this.commDestroy(GCOMM_DESTROY,
				GCOMM_WORLD);

		// main
		/*
		 * void main() { $proc procs[NPROCS];
		 * 
		 * for (int i=0; i<NPROCS; i++) procs[i] = $spawn MPI_Process(i); for
		 * (int i=0; i<NPROCS; i++) $wait(procs[i]);
		 * $gcomm_destroy(GCOMM_WORLD); }
		 */
		procsType = nodeFactory.newArrayTypeNode(
				source,
				nodeFactory.newTypedefNameNode(
						nodeFactory.newIdentifierNode(source, "$proc"), null),
				nodeFactory.newIdentifierExpressionNode(source,
						nodeFactory.newIdentifierNode(source, "NPROCS")));
		procsVar = nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "procs"), procsType);
		items.add(procsVar);

		// first for loop:
		// nodeFactory.newForLoopInitializerNode(
		// statementSource, declarations)
		initialList = new LinkedList<>();
		initialList.add(nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "i"),
				nodeFactory.newBasicTypeNode(source, BasicTypeKind.INT),
				nodeFactory.newIntegerConstantNode(source, "0")));
		initializerNode = nodeFactory.newForLoopInitializerNode(source,
				initialList);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "NPROCS")));

		loopCondition = nodeFactory.newOperatorNode(source, Operator.LT,
				operatorArgs);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));

		incrementer = nodeFactory.newOperatorNode(source,
				Operator.POSTINCREMENT, operatorArgs);
		callArgs = new ArrayList<>(1);
		callArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));
		spawnProc = nodeFactory.newSpawnNode(source, nodeFactory
				.newFunctionCallNode(source, nodeFactory
						.newIdentifierExpressionNode(source, nodeFactory
								.newIdentifierNode(source, "MPI_Process")),
						callArgs, null));
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "procs")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));
		leftHandSide = nodeFactory.newOperatorNode(source, Operator.SUBSCRIPT,
				operatorArgs);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(leftHandSide);
		operatorArgs.add(spawnProc);
		assign = nodeFactory.newExpressionStatementNode(nodeFactory
				.newOperatorNode(source, Operator.ASSIGN, operatorArgs));
		forLoop = nodeFactory.newForLoopNode(source, initializerNode,
				loopCondition, incrementer, assign, null);
		items.add(forLoop);

		// second for loop:
		initialList = new LinkedList<>();
		initialList.add(nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "i"),
				nodeFactory.newBasicTypeNode(source, BasicTypeKind.INT),
				nodeFactory.newIntegerConstantNode(source, "0")));
		initializerNode = nodeFactory.newForLoopInitializerNode(source,
				initialList);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "NPROCS")));

		loopCondition = nodeFactory.newOperatorNode(source, Operator.LT,
				operatorArgs);
		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));

		incrementer = nodeFactory.newOperatorNode(source,
				Operator.POSTINCREMENT, operatorArgs);
		callArgs = new ArrayList<>(1);

		operatorArgs = new LinkedList<>();
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "procs")));
		operatorArgs.add(nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, "i")));
		callArgs.add(nodeFactory.newOperatorNode(source, Operator.SUBSCRIPT,
				operatorArgs));
		waitProc = nodeFactory.newFunctionCallNode(
				source,
				nodeFactory.newIdentifierExpressionNode(source,
						nodeFactory.newIdentifierNode(source, "$wait")),
				callArgs, null);
		forLoop = nodeFactory.newForLoopNode(source, initializerNode,
				loopCondition, incrementer,
				nodeFactory.newExpressionStatementNode(waitProc), null);
		items.add(forLoop);

		items.add(gcommDestroy);

		mainBody = nodeFactory.newCompoundStatementNode(source, items);

		newFormalList = new LinkedList<>();
		newFormalList.add(nodeFactory.newVariableDeclarationNode(source,
				nodeFactory.newIdentifierNode(source, "_rank"),
				nodeFactory.newBasicTypeNode(source, BasicTypeKind.INT)));
		formals = nodeFactory.newSequenceNode(source,
				"FormalParameterDeclarations", newFormalList);
		mainType = nodeFactory.newFunctionTypeNode(source,
				nodeFactory.newVoidTypeNode(source), formals, true);
		mainFunction = nodeFactory.newFunctionDefinitionNode(source,
				nodeFactory.newIdentifierNode(source, "main"), mainType, null,
				mainBody);

		return mainFunction;
	}
}
