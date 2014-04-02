package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode.NodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ArrowNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DotNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode.ExpressionKind;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode.BlockItemKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode.StatementKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.BaseTransformer;

public class IOTransformer extends BaseTransformer {

	public static String CODE = "io";
	public static String LONG_NAME = "IOTransformer";
	public static String SHORT_DESCRIPTION = "transforms C program with IO to CIVL-C";

	public static String PRINTF = "printf";
	public static String FPRINTF = "fprintf";
	public static String SCANF = "scanf";
	public static String FSCANF = "fscanf";
	public static String STD_OUT = "stdout";
	public static String STD_IN = "stdin";

	public IOTransformer(ASTFactory astFactory) {
		super(IOTransformer.CODE, IOTransformer.LONG_NAME,
				IOTransformer.SHORT_DESCRIPTION, astFactory);
	}

	@Override
	public AST transform(AST unit) throws SyntaxException {
		ASTNode rootNode = unit.getRootNode();

		assert this.astFactory == unit.getASTFactory();
		assert this.nodeFactory == astFactory.getNodeFactory();
		unit.release();
		for (int i = 0; i < rootNode.numChildren(); i++) {
			ASTNode node = rootNode.child(i);

			if (node instanceof FunctionDefinitionNode) {
				processFunctionDefinition((FunctionDefinitionNode) node);
			}
		}
		return astFactory.newTranslationUnit(rootNode);
	}

	private void processFunctionDefinition(FunctionDefinitionNode function) {
		processCompoundStatement(function.getBody());
	}

	private void processCompoundStatement(CompoundStatementNode statement) {
		int count = statement.numChildren();

		for (int i = 0; i < count; i++) {
			BlockItemNode item = statement.getSequenceChild(i);
			BlockItemKind kind = item.blockItemKind();

			switch (kind) {
			case STATEMENT:
				this.processStatement((StatementNode) item);
				break;
			case ORDINARY_DECLARATION:
				if (item.nodeKind() == NodeKind.FUNCTION_DEFINITION) {
					processFunctionDefinition((FunctionDefinitionNode) item);
				}
				break;
			default:
			}
		}
	}

	/**
	 * Remove side effects if the statement contains them. Note that
	 * <code>goto</code>, <code>jump</code>, and <code>null</code> statements
	 * cannot have side effects, and so will never be modified.
	 * 
	 * @param statement
	 * @return
	 * @throws SyntaxException
	 */
	private void processStatement(StatementNode statement) {
		StatementKind kind = statement.statementKind();

		switch (kind) {
		case ASSUME:
			processAssumeStatement((AssumeNode) statement);
			break;
		case CHOOSE:
			processChooseStatement((ChooseStatementNode) statement);
			break;
		case COMPOUND:
			processCompoundStatement((CompoundStatementNode) statement);
			break;
		case EXPRESSION:
			processExpression(((ExpressionStatementNode) statement)
					.getExpression());
			break;
		case FOR:
			processLoopStatement((ForLoopNode) statement);
			break;
		case IF:
			processIfStatement((IfNode) statement);
			break;
		case LABELED:
			processStatement(((LabeledStatementNode) statement).getStatement());
			break;
		case LOOP:
			processLoopStatement((LoopNode) statement);
			break;
		case RETURN:
			processReturnStatement((ReturnNode) statement);
			break;
		case SWITCH:
			processSwitchStatement((SwitchNode) statement);
			break;
		case WHEN:
			processExpression(((WhenNode) statement).getGuard());
			processStatement(((WhenNode) statement).getBody());
			break;
		case ATOMIC:
			processStatement(((AtomicNode) statement).getBody());
			break;
		default:

		}
	}

	private void processSwitchStatement(SwitchNode switchNode) {
		processExpression(switchNode.getCondition());
		processStatement(switchNode.getBody());
	}

	private void processReturnStatement(ReturnNode returnNode) {
		ExpressionNode expression = returnNode.getExpression();
		if (expression != null)
			processExpression(expression);
	}

	private void processIfStatement(IfNode ifNode) {
		processExpression(ifNode.getCondition());
		processStatement(ifNode.getTrueBranch());
		processStatement(ifNode.getFalseBranch());
	}

	private void processLoopStatement(LoopNode loop) {
		processExpression(loop.getCondition());
		processStatement(loop.getBody());
		if (loop.statementKind() == StatementKind.FOR) {
			ForLoopNode forLoop = (ForLoopNode) loop;

			processExpression(forLoop.getIncrementer());
		}
	}

	private void processChooseStatement(ChooseStatementNode choose) {
		// TODO Auto-generated method stub
		for (StatementNode statement : choose) {
			processStatement(statement);
		}
	}

	private void processAssumeStatement(AssumeNode statement) {
		ExpressionNode expression = statement.getExpression();

		processExpression(expression);
	}

	private void processExpression(ExpressionNode expression) {
		ExpressionKind expressionKind = expression.expressionKind();

		switch (expressionKind) {
		case OPERATOR:
			processOperator((OperatorNode) expression);
			break;
		case FUNCTION_CALL:
			processFunctionCall((FunctionCallNode) expression);
			break;
		case CAST:
			processExpression(((CastNode) expression).getArgument());
			break;
		case DOT:
			processExpression(((DotNode) expression).getStructure());
			break;
		case ARROW:
			processExpression(((ArrowNode) expression).getStructurePointer());
			break;
		case SPAWN:
			processExpression(((SpawnNode) expression).getCall());
			break;
		default:
		}
	}

	private void processFunctionCall(FunctionCallNode functionCall) {
		if (functionCall.getFunction().expressionKind() == ExpressionKind.IDENTIFIER_EXPRESSION) {
			IdentifierExpressionNode functionExpression = (IdentifierExpressionNode) functionCall
					.getFunction();
			String functionName = functionExpression.getIdentifier().name();
			boolean needToTransform = false;
			String firstArgName = "";

			if (functionName.equals(PRINTF)) {
				functionExpression.getIdentifier().setName(FPRINTF);
				firstArgName = STD_OUT;
				needToTransform = true;
			} else if (functionName.equals(SCANF)) {
				functionExpression.getIdentifier().setName(FSCANF);
				firstArgName = STD_IN;
				needToTransform = true;
			}
			if (needToTransform) {
				Source source = functionCall.getFunction().getSource();
				IdentifierExpressionNode firstArg = nodeFactory
						.newIdentifierExpressionNode(source, nodeFactory
								.newIdentifierNode(source, firstArgName));
				int oldCount = functionCall.getNumberOfArguments();
				List<ExpressionNode> arguments = new ArrayList<>(oldCount + 1);

				arguments.add(firstArg);
				for (int i = 0; i < oldCount; i++) {
					ExpressionNode argument = functionCall.getArgument(i);

					argument.parent().removeChild(argument.childIndex());
					arguments.add(argument);
				}
				functionCall.setChild(1, nodeFactory.newSequenceNode(source,
						"ActualParameterList", arguments));
			}
		}
	}

	private void processOperator(OperatorNode expression) {
		int count = expression.getNumberOfArguments();

		for (int i = 0; i < count; i++) {
			processExpression(expression.getArgument(i));
		}
	}

}
