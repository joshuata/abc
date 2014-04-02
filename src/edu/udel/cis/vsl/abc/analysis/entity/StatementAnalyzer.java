package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Iterator;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.Label;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.SwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpFunctionReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpReductionNode.OmpReductionNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode.OmpStatementNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
//import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.DeclarationListNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.JumpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.NullStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WaitNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.UnsourcedException;

public class StatementAnalyzer {

	// ***************************** Fields *******************************

	private EntityAnalyzer entityAnalyzer;

	private NodeFactory nodeFactory;

	private ExpressionAnalyzer expressionAnalyzer;

	// ************************** Constructors ****************************

	StatementAnalyzer(EntityAnalyzer entityAnalyzer,
			ExpressionAnalyzer expressionAnalyzer) {
		this.entityAnalyzer = entityAnalyzer;
		this.nodeFactory = entityAnalyzer.nodeFactory;
		this.expressionAnalyzer = expressionAnalyzer;
	}

	// ************************* Private Methods **************************

	private SyntaxException error(String message, ASTNode node) {
		return entityAnalyzer.error(message, node);
	}

	private SyntaxException error(UnsourcedException e, ASTNode node) {
		return entityAnalyzer.error(e, node);
	}

	private void processExpression(ExpressionNode expression)
			throws SyntaxException {
		if (expression != null)
			expressionAnalyzer.processExpression(expression);
	}

	private void processIf(IfNode node) throws SyntaxException {
		processExpression(node.getCondition());
		processStatement(node.getTrueBranch());
		if (node.getFalseBranch() != null)
			processStatement(node.getFalseBranch());
	}

	private SwitchNode enclosingSwitch(SwitchLabelNode labelNode) {
		for (ASTNode node = labelNode.parent(); node != null; node = node
				.parent()) {
			if (node instanceof SwitchNode)
				return (SwitchNode) node;
		}
		return null;
	}

	private ASTNode enclosingSwitchOrChoose(SwitchLabelNode labelNode) {
		for (ASTNode node = labelNode.parent(); node != null; node = node
				.parent()) {
			if (node instanceof SwitchNode
					|| node instanceof ChooseStatementNode)
				return node;
		}
		return null;
	}

	private void processLabeledStatement(LabeledStatementNode node)
			throws SyntaxException {
		LabelNode labelNode = node.getLabel();
		StatementNode statementNode = node.getStatement();
		Function function = entityAnalyzer.enclosingFunction(node);

		if (function == null)
			throw error("Label occurs outside of function", node);
		labelNode.setStatement(statementNode);
		if (labelNode instanceof OrdinaryLabelNode)
			processOrdinaryLabel((OrdinaryLabelNode) labelNode, function);
		else if (labelNode instanceof SwitchLabelNode)
			processSwitchLabel(node, (SwitchLabelNode) labelNode, function);
		else
			throw new RuntimeException("unreachable");
		processStatement(statementNode);

	}

	private void processOrdinaryLabel(OrdinaryLabelNode node, Function function)
			throws SyntaxException {
		Label label = entityAnalyzer.entityFactory.newLabel(node);

		node.setFunction(function);
		node.setEntity(label);
		node.getIdentifier().setEntity(label);
		try {
			function.getScope().add(label);
		} catch (UnsourcedException e) {
			throw error(e, node);
		}
	}

	private void processSwitchLabel(LabeledStatementNode labeledStatement,
			SwitchLabelNode switchLabel, Function function)
			throws SyntaxException {

		if (switchLabel.isDefault()) {
			ASTNode enclosing = enclosingSwitchOrChoose(switchLabel);

			if (enclosing instanceof ChooseStatementNode) {
				ChooseStatementNode choose = (ChooseStatementNode) enclosing;
				LabeledStatementNode oldDefault = choose.getDefaultCase();

				if (oldDefault != null)
					throw error(
							"Two default cases in choose statement.  First was at "
									+ oldDefault.getSource(), switchLabel);
				choose.setDefaultCase(labeledStatement);
				return;
			}
		}

		SwitchNode switchNode = enclosingSwitch(switchLabel);

		if (switchNode == null)
			throw error("Switch label occurs outside of any switch statement",
					switchLabel);
		if (switchLabel.isDefault()) {
			LabeledStatementNode oldDefault = switchNode.getDefaultCase();

			if (oldDefault != null)
				throw error(
						"Two default cases in switch statement.  First was at "
								+ oldDefault.getSource(), switchLabel);
			switchNode.setDefaultCase(labeledStatement);
		} else {
			ExpressionNode caseExpression = switchLabel.getExpression();
			Iterator<LabeledStatementNode> cases = switchNode.getCases();
			Value constant;

			expressionAnalyzer.processExpression(caseExpression);
			if (!caseExpression.isConstantExpression())
				error("Case expression not constant", caseExpression);
			constant = nodeFactory.getConstantValue(caseExpression);
			while (cases.hasNext()) {
				SwitchLabelNode labelNode = (SwitchLabelNode) cases.next()
						.getLabel();
				ExpressionNode oldExpression = labelNode.getExpression();
				Value oldConstant = nodeFactory.getConstantValue(oldExpression);

				if (constant.equals(oldConstant))
					throw error(
							"Case constant appears twice: first time was at "
									+ oldExpression, caseExpression);
			}
			switchNode.addCase(labeledStatement);
		}
	}

	private void processJump(JumpNode statement) throws SyntaxException {
		switch (statement.getKind()) {
		case RETURN: {
			ExpressionNode expression = ((ReturnNode) statement)
					.getExpression();
			Function function = entityAnalyzer.enclosingFunction(statement);
			ObjectType returnType = function.getType().getReturnType();
			boolean returnTypeIsVoid = returnType.kind() == TypeKind.VOID;

			if (expression == null) {
				if (!returnTypeIsVoid)
					throw error("Missing expression in return statement",
							statement);
			} else {
				if (returnTypeIsVoid)
					throw error(
							"Argument for return in function returning void",
							statement);
				if (expression != null)
					processExpression(expression);
				try {
					expressionAnalyzer
							.processAssignment(returnType, expression);
				} catch (UnsourcedException e) {
					throw error(e, expression);
				}
			}
		}
		case GOTO: // taken care of later in processGotos
		case BREAK: // nothing to do
		case CONTINUE: // nothing to do
			break;
		default:
			throw new RuntimeException("Unreachable");
		}
	}

	private void processLoop(LoopNode loopNode) throws SyntaxException {
		switch (loopNode.getKind()) {
		case WHILE:
			processExpression(loopNode.getCondition());
			processStatement(loopNode.getBody());
			processExpression(loopNode.getInvariant());
			break;
		case DO_WHILE:
			processStatement(loopNode.getBody());
			processExpression(loopNode.getCondition());
			processExpression(loopNode.getInvariant());
			break;
		case FOR: {
			ForLoopNode forNode = (ForLoopNode) loopNode;
			ForLoopInitializerNode initializer = forNode.getInitializer();

			if (initializer == null) {
			} else if (initializer instanceof ExpressionNode) {
				processExpression((ExpressionNode) initializer);
			} else if (initializer instanceof DeclarationListNode) {
				DeclarationListNode declarationList = (DeclarationListNode) initializer;

				for (VariableDeclarationNode child : declarationList)
					entityAnalyzer.declarationAnalyzer
							.processVariableDeclaration(child);
			} else
				throw error("Unknown kind of initializer clause in for loop",
						initializer);
			processExpression(loopNode.getCondition());
			processExpression(forNode.getIncrementer());
			processStatement(loopNode.getBody());
			processExpression(loopNode.getInvariant());
			break;
		}
		default:
			throw new RuntimeException("Unreachable");
		}
	}

	// ************************* Exported Methods **************************

	void processStatement(StatementNode statement) throws SyntaxException {
		if (statement instanceof CompoundStatementNode)
			processCompoundStatement((CompoundStatementNode) statement);
		else if (statement instanceof ExpressionStatementNode)
			processExpression(((ExpressionStatementNode) statement)
					.getExpression());
		else if (statement instanceof IfNode) {
			processIf((IfNode) statement);
		} else if (statement instanceof JumpNode) {
			processJump((JumpNode) statement);
		} else if (statement instanceof LabeledStatementNode) {
			processLabeledStatement((LabeledStatementNode) statement);
		} else if (statement instanceof LoopNode) {
			processLoop((LoopNode) statement);
		} else if (statement instanceof SwitchNode) {
			processExpression(((SwitchNode) statement).getCondition());
			processStatement(((SwitchNode) statement).getBody());
		} else if (statement instanceof PragmaNode) {
			entityAnalyzer.processPragma((PragmaNode) statement);
			if (statement instanceof OmpStatementNode) {
				processOmpStatement((OmpStatementNode) statement);
			}
		} else if (statement instanceof NullStatementNode) {
			// nothing to do
		} else if (statement instanceof AssumeNode) {
			processExpression(((AssumeNode) statement).getExpression());
		} else if (statement instanceof WhenNode) {
			ExpressionNode guard = ((WhenNode) statement).getGuard();
			Type guardType;

			processExpression(guard);
			guardType = guard.getConvertedType();
			// check guardType can be converted to a boolean...
			if (!guardType.isScalar())
				throw error("Guard has non-scalar type " + guardType, guard);
			processStatement(((WhenNode) statement).getBody());
		} else if (statement instanceof ChooseStatementNode) {
			ChooseStatementNode chooseStatement = (ChooseStatementNode) statement;

			for (StatementNode child : chooseStatement)
				processStatement(child);
		} else if (statement instanceof WaitNode) {
			processExpression(((WaitNode) statement).getExpression());
		} else if (statement instanceof AtomicNode) {
			processStatement(((AtomicNode) statement).getBody());
		} else
			throw error("Unknown kind of statement", statement);
	}

	@SuppressWarnings("unchecked")
	private void processOmpStatement(OmpStatementNode statement)
			throws SyntaxException {
		OmpStatementNodeKind kind = statement.ompStatementNodeKind();
		SequenceNode<OmpReductionNode> reductionList = (SequenceNode<OmpReductionNode>) statement
				.child(7);

		for (int i = 1; i <= 6; i++) {
			SequenceNode<ExpressionNode> list = (SequenceNode<ExpressionNode>) statement
					.child(i);

			if (list != null) {
				int count = list.numChildren();

				for (int j = 0; j < count; j++) {
					this.expressionAnalyzer.processExpression(list
							.getSequenceChild(j));
				}
			}
		}
		if (reductionList != null) {
			int count = reductionList.numChildren();

			for (int j = 0; j < count; j++) {
				this.processOmpReductionNode(reductionList.getSequenceChild(j));
			}
		}
		switch (kind) {
		case PARALLEL:
			OmpParallelNode parallel = (OmpParallelNode) statement;

			if (parallel.ifClause() != null)
				processExpression(parallel.ifClause());
			if (parallel.numThreads() != null)
				processExpression(parallel.numThreads());
			break;
		case WORKSHARE:
			OmpWorkshareNode workshare = (OmpWorkshareNode) statement;

			switch (workshare.ompWorkshareNodeKind()) {
			case FOR:
				OmpForNode forNode = (OmpForNode) statement;
				List<FunctionCallNode> assertions = forNode.assertions();
				FunctionCallNode invariant = forNode.invariant();
				ExpressionNode chunkSize = forNode.chunkSize();

				if (assertions != null) {
					for (FunctionCallNode node : assertions)
						processExpression(node);
				}
				if (invariant != null)
					processExpression(invariant);
				if (chunkSize != null)
					processExpression(chunkSize);
				break;
			default:
			}
			break;
		default:

		}
		if (statement.statementNode() != null)
			processStatement(statement.statementNode());
	}

	private void processOmpReductionNode(OmpReductionNode reduction)
			throws SyntaxException {
		OmpReductionNodeKind kind = reduction.ompReductionOperatorNodeKind();
		SequenceNode<IdentifierExpressionNode> list = reduction.variables();
		int count = list.numChildren();

		if (kind == OmpReductionNodeKind.FUNCTION) {
			this.expressionAnalyzer
					.processExpression(((OmpFunctionReductionNode) reduction)
							.function());
		}
		for (int i = 0; i < count; i++) {
			this.expressionAnalyzer.processExpression(list.getSequenceChild(i));
		}
	}

	/**
	 * <ul>
	 * <li>StatementNode</li> (includes PragmaNode)
	 * <li>StructureOrUnionTypeNode</li>
	 * <li>EnumerationTypeNode</li>
	 * <li>StaticAssertionNode</li>
	 * <li>VariableDeclarationNode</li>
	 * <li>FunctionDeclarationNode</li> (but not a FunctionDefinitionNode)
	 * <li>TypedefDeclarationNode</li>
	 * </ul>
	 * 
	 * @param node
	 * @throws SyntaxException
	 */
	void processCompoundStatement(CompoundStatementNode node)
			throws SyntaxException {
		for (BlockItemNode item : node) {
			if (item instanceof StatementNode)
				processStatement((StatementNode) item);
			else if (item instanceof StructureOrUnionTypeNode)
				entityAnalyzer.typeAnalyzer
						.processStructureOrUnionType((StructureOrUnionTypeNode) item);
			else if (item instanceof EnumerationTypeNode)
				entityAnalyzer.typeAnalyzer
						.processEnumerationType((EnumerationTypeNode) item);
			else if (item instanceof StaticAssertionNode)
				entityAnalyzer
						.processStaticAssertion((StaticAssertionNode) item);
			else if (item instanceof VariableDeclarationNode)
				entityAnalyzer.declarationAnalyzer
						.processVariableDeclaration((VariableDeclarationNode) item);
			else if (item instanceof FunctionDeclarationNode)
				entityAnalyzer.declarationAnalyzer
						.processFunctionDeclaration((FunctionDeclarationNode) item);
			else if (item instanceof TypedefDeclarationNode)
				entityAnalyzer.declarationAnalyzer
						.processTypedefDeclaration((TypedefDeclarationNode) item);
			else
				throw error("Unknown kind of block item", item);
		}
	}
}
