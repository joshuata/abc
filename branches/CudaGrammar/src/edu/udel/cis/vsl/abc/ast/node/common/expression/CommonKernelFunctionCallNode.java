package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonKernelFunctionCallNode extends CommonExpressionNode
		implements FunctionCallNode {

	public CommonKernelFunctionCallNode(Source source, ExpressionNode function,
			SequenceNode<ExpressionNode> contextArguments,
			SequenceNode<ExpressionNode> arguments,
			SequenceNode<ExpressionNode> scopeList) {
		super(source, function, contextArguments, scopeList);
	}

	@Override
	public ExpressionNode getFunction() {
		return (ExpressionNode) child(0);
	}

	@Override
	public void setFunction(ExpressionNode function) {
		setChild(0, function);
	}

	public int getNumberOfContextArguments() {
		return child(1).numChildren();
	}
	
	@Override
	public int getNumberOfArguments() {
		return child(2).numChildren();
	}
	
	public ExpressionNode getContextArgument(int index) {
		return (ExpressionNode) child(1).child(index);
	}

	@Override
	public ExpressionNode getArgument(int index) {
		return (ExpressionNode) child(2).child(index);
	}

	public void setContextArgument(int index, ExpressionNode value) {
		((CommonASTNode) child(1)).setChild(index, value);
	}
	
	@Override
	public void setArgument(int index, ExpressionNode value) {
		((CommonASTNode) child(2)).setChild(index, value);
	}
	
	public void setContextArguments(SequenceNode<ExpressionNode> arguments) {
		this.setChild(1, arguments);
	}

	@Override
	public void setArguments(SequenceNode<ExpressionNode> arguments) {
		this.setChild(2, arguments);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("FunctionCallNode");
	}

	@Override
	public boolean isConstantExpression() {
		return false;
	}

	@Override
	public FunctionCallNode copy() {
		@SuppressWarnings("unchecked")
		SequenceNode<ExpressionNode> contextArguments = (SequenceNode<ExpressionNode>) child(1);
		@SuppressWarnings("unchecked")
		SequenceNode<ExpressionNode> arguments = (SequenceNode<ExpressionNode>) child(2);
		@SuppressWarnings("unchecked")
		SequenceNode<ExpressionNode> scopeList = (SequenceNode<ExpressionNode>) child(3);

		return new CommonKernelFunctionCallNode(getSource(),
				duplicate(getFunction()), duplicate(contextArguments),
				duplicate(arguments), duplicate(scopeList));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ExpressionNode> getScopeList() {
		return (SequenceNode<ExpressionNode>) child(3);
	}

	@Override
	public ExpressionKind expressionKind() {
		return ExpressionKind.FUNCTION_CALL;
	}

	@Override
	public boolean isSideEffectFree(boolean errorsAreSideEffects) {
		ExpressionNode function = getFunction();
		boolean result = true;

		if (function instanceof IdentifierExpressionNode) {
			IdentifierNode functionIdentifier = ((IdentifierExpressionNode) function)
					.getIdentifier();
			DeclarationNode functionDeclaration;

			if (functionIdentifier.getEntity() == null) {
				// FIXME: Why do we need this? Not having this check was
				// causing a failure with ring2.cvl
				return false;
			}
			functionDeclaration = ((OrdinaryEntity) functionIdentifier
					.getEntity()).getFirstDeclaration();
			// Check if this is an abstract function.
			if (functionDeclaration instanceof AbstractFunctionDefinitionNode) {
				for (int i = 0; i < getNumberOfContextArguments(); i++) {
					result = result
							&& getContextArgument(i).isSideEffectFree(
									errorsAreSideEffects);
				}
				for (int i = 0; i < getNumberOfArguments(); i++) {
					result = result
							&& getArgument(i).isSideEffectFree(
									errorsAreSideEffects);
				}
			} else {
				result = false;
			}
		} else {
			// Assume this isn't an abstract function.
			result = false;
		}
		return result;
	}

}
