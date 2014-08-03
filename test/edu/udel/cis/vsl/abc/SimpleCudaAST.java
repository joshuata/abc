package edu.udel.cis.vsl.abc;

import java.util.ArrayList;
import java.util.Arrays;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.IF.ASTs;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.Nodes;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.Types;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.Values;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Formation;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.Tokens;
import edu.udel.cis.vsl.abc.token.common.CommonCToken;
import edu.udel.cis.vsl.abc.token.common.CommonSource;
import edu.udel.cis.vsl.abc.token.common.SystemFormation;

public class SimpleCudaAST {

	private static Formation sysForm = new SystemFormation("System Formation");
	private static CToken firstTok = new CommonCToken(0, "first", sysForm);
	private static CToken lastTok = new CommonCToken(0, "first", sysForm);
	private static Source source = new CommonSource(firstTok, lastTok);
	private static TypeFactory typeF = Types.newTypeFactory();
	private static ValueFactory valueF = Values.newValueFactory(typeF);
	private static NodeFactory nodeF = Nodes.newNodeFactory(typeF, valueF);
	private static TokenFactory tokenF = Tokens.newTokenFactory();
	private static ASTFactory astF = ASTs.newASTFactory(nodeF, tokenF, typeF);

	private static IdentifierNode newId(String name) {
		return nodeF.newIdentifierNode(source, name);
	}

	public static void main(String[] args) throws SyntaxException {

		// build _thread definition
		// SequenceNode<VariableDeclarationNode> threadFormals = nodeF
		// .newSequenceNode(source, "threadFormals",
		// new ArrayList<VariableDeclarationNode>() {
		// {
		// add(nodeF.newVariableDeclarationNode(source,
		// newId("threadIdx"), nodeF
		// .newTypedefNameNode(
		// newId("uint3"), null)));
		// }
		// });

		SequenceNode<VariableDeclarationNode> threadFormals = nodeF
				.newSequenceNode(
						source,
						"threadFormals",
						Arrays.asList(nodeF.newVariableDeclarationNode(source,
								newId("threadIdx"),
								nodeF.newTypedefNameNode(newId("uint3"), null))));

		FunctionTypeNode threadType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), threadFormals, false);
		CompoundStatementNode threadBody = nodeF.newCompoundStatementNode(
				source, new ArrayList<BlockItemNode>());
		final FunctionDefinitionNode threadDef = nodeF
				.newFunctionDefinitionNode(source, newId("_thread"),
						threadType, null, threadBody);

		// build the _block definition
		SequenceNode<VariableDeclarationNode> blockFormals = nodeF
				.newSequenceNode(source, "blockFormals",
						new ArrayList<VariableDeclarationNode>() {
							{
								add(nodeF.newVariableDeclarationNode(source,
										newId("blockIdx"), nodeF
												.newTypedefNameNode(
														newId("uint3"), null)));
							}
						});
		FunctionTypeNode blockType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), blockFormals, false);

		final FunctionCallNode runThreadsCall = nodeF.newFunctionCallNode(
				source,
				nodeF.newIdentifierExpressionNode(source, newId("_runProcs")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("blockDim")));
						add(nodeF.newIdentifierExpressionNode(source,
								newId("_thread")));
					}
				}, null);
		CompoundStatementNode blockBody = nodeF.newCompoundStatementNode(
				source, new ArrayList<BlockItemNode>() {
					{
						add(threadDef);
						add(nodeF.newExpressionStatementNode(runThreadsCall));
					}
				});
		final FunctionDefinitionNode blockDef = nodeF
				.newFunctionDefinitionNode(source, newId("_block"), blockType,
						null, blockBody);

		// build the innerKernel definition
		SequenceNode<VariableDeclarationNode> innerKernelFormals = nodeF
				.newSequenceNode(source, "innerKernelFormals",
						new ArrayList<VariableDeclarationNode>() {
							{
								add(nodeF.newVariableDeclarationNode(
										source,
										newId("this"),
										nodeF.newPointerTypeNode(
												source,
												nodeF.newTypedefNameNode(
														newId("_kernelInstance"),
														null))));
								add(nodeF.newVariableDeclarationNode(source,
										newId("e"), nodeF.newTypedefNameNode(
												newId("cudaEvent_t"), null)));
							}
						});
		FunctionTypeNode innerKernelType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), innerKernelFormals, false);
		final FunctionCallNode waitInQueueCall = nodeF.newFunctionCallNode(
				source, nodeF.newIdentifierExpressionNode(source,
						newId("_waitInQueue")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("this")));
						add(nodeF.newIdentifierExpressionNode(source,
								newId("e")));
					}
				}, null);
		final FunctionCallNode runBlocksCall = nodeF.newFunctionCallNode(
				source,
				nodeF.newIdentifierExpressionNode(source, newId("_runProcs")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("gridDim")));
						add(nodeF.newIdentifierExpressionNode(source,
								newId("_block")));
					}
				}, null);
		final FunctionCallNode kernelFinishCall = nodeF.newFunctionCallNode(
				source, nodeF.newIdentifierExpressionNode(source,
						newId("_kernelFinish")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("this")));
					}
				}, null);
		CompoundStatementNode innerKernelBody = nodeF.newCompoundStatementNode(
				source, new ArrayList<BlockItemNode>() {
					{
						add(blockDef);
						add(nodeF.newExpressionStatementNode(waitInQueueCall));
						add(nodeF.newExpressionStatementNode(runBlocksCall));
						add(nodeF.newExpressionStatementNode(kernelFinishCall));
					}
				});
		final FunctionDefinitionNode innerKernelDef = nodeF
				.newFunctionDefinitionNode(source, newId("_kernel"),
						innerKernelType, null, innerKernelBody);

		// build the kernel definition
		SequenceNode<VariableDeclarationNode> kernelFormals = nodeF
				.newSequenceNode(source, "kernelFormals",
						new ArrayList<VariableDeclarationNode>() {
							{
								add(nodeF.newVariableDeclarationNode(source,
										newId("gridDim"), nodeF
												.newTypedefNameNode(
														newId("dim3"), null)));
								add(nodeF.newVariableDeclarationNode(source,
										newId("blockDim"), nodeF
												.newTypedefNameNode(
														newId("dim3"), null)));
								add(nodeF.newVariableDeclarationNode(source,
										newId("s"), nodeF.newTypedefNameNode(
												newId("cudaStream_t"), null)));
							}
						});
		FunctionTypeNode kernelType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), kernelFormals, false);
		final FunctionCallNode enqueueKernelCall = nodeF.newFunctionCallNode(
				source, nodeF.newIdentifierExpressionNode(source,
						newId("_enqueueKernel")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("s")));
						add(nodeF.newIdentifierExpressionNode(source,
								newId("_kernel")));
					}
				}, null);
		CompoundStatementNode kernelBody = nodeF.newCompoundStatementNode(
				source, new ArrayList<BlockItemNode>() {
					{
						add(innerKernelDef);
						add(nodeF.newExpressionStatementNode(enqueueKernelCall));
					}
				});
		final FunctionDefinitionNode kernelDef = nodeF
				.newFunctionDefinitionNode(source, newId("_kernel_simple"),
						kernelType, null, kernelBody);

		// build the _main definition
		SequenceNode<VariableDeclarationNode> innerMainFormals = nodeF
				.newSequenceNode(source, "innerMainFormals",
						new ArrayList<VariableDeclarationNode>());
		FunctionTypeNode innerMainType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), innerMainFormals, false);
		FunctionCallNode t1Init = nodeF.newFunctionCallNode(source,
				nodeF.newIdentifierExpressionNode(source, newId("_toDim3")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIntegerConstantNode(source, "1"));
					}
				}, null);
		final VariableDeclarationNode t1Decl = nodeF
				.newVariableDeclarationNode(source, newId("_t1"),
						nodeF.newTypedefNameNode(newId("dim3"), null), t1Init);
		FunctionCallNode t2Init = nodeF.newFunctionCallNode(source,
				nodeF.newIdentifierExpressionNode(source, newId("_toDim3")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIntegerConstantNode(source, "1"));
					}
				}, null);
		final VariableDeclarationNode t2Decl = nodeF
				.newVariableDeclarationNode(source, newId("_t2"),
						nodeF.newTypedefNameNode(newId("dim3"), null), t2Init);
		final FunctionCallNode kernelCall = nodeF.newFunctionCallNode(source,
				nodeF.newIdentifierExpressionNode(source,
						newId("_kernel_simple")),
				new ArrayList<ExpressionNode>() {
					{
						add(nodeF.newIdentifierExpressionNode(source,
								newId("_t1")));
						add(nodeF.newIdentifierExpressionNode(source,
								newId("_t2")));
						add(nodeF.newIntegerConstantNode(source, "0"));
					}
				}, null);
		CompoundStatementNode innerMainBody = nodeF.newCompoundStatementNode(
				source, new ArrayList<BlockItemNode>() {
					{
						add(t1Decl);
						add(t2Decl);
						add(nodeF.newExpressionStatementNode(kernelCall));
						add(nodeF.newReturnNode(source,
								nodeF.newIntegerConstantNode(source, "0")));
					}
				});
		final FunctionDefinitionNode innerMainDef = nodeF
				.newFunctionDefinitionNode(source, newId("_main"),
						innerMainType, null, innerMainBody);

		// build the main definition
		SequenceNode<VariableDeclarationNode> mainFormals = nodeF
				.newSequenceNode(source, "mainFormals",
						new ArrayList<VariableDeclarationNode>());
		FunctionTypeNode mainType = nodeF.newFunctionTypeNode(source,
				nodeF.newVoidTypeNode(source), mainFormals, false);
		final FunctionCallNode cudaInitCall = nodeF.newFunctionCallNode(source,
				nodeF.newIdentifierExpressionNode(source, newId("_cudaInit")),
				new ArrayList<ExpressionNode>(), null);
		final FunctionCallNode innerMainCall = nodeF.newFunctionCallNode(
				source,
				nodeF.newIdentifierExpressionNode(source, newId("_main")),
				new ArrayList<ExpressionNode>(), null);
		final FunctionCallNode cudaFinalizeCall = nodeF.newFunctionCallNode(
				source, nodeF.newIdentifierExpressionNode(source,
						newId("_cudaFinalize")),
				new ArrayList<ExpressionNode>(), null);
		CompoundStatementNode mainBody = nodeF.newCompoundStatementNode(source,
				new ArrayList<BlockItemNode>() {
					{
						add(innerMainDef);
						add(nodeF.newExpressionStatementNode(cudaInitCall));
						add(nodeF.newExpressionStatementNode(innerMainCall));
						add(nodeF.newExpressionStatementNode(cudaFinalizeCall));
					}
				});
		final FunctionDefinitionNode mainDef = nodeF.newFunctionDefinitionNode(
				source, newId("main"), mainType, null, mainBody);

		// build the AST
		AST ast = astF.newAST(nodeF.newSequenceNode(source, "definitions",
				new ArrayList<ExternalDefinitionNode>() {
					{
						add(kernelDef);
						add(mainDef);
					}
				}));

		ast.print(System.out);
	}

}
