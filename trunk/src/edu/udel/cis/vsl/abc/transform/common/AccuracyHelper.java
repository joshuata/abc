package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode.Quantifier;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class AccuracyHelper implements Transformer {

	private ASTFactory unitFactory;
	private NodeFactory factory;
	private List<AbstractFunctionDefinitionNode> abstractFunctions = new ArrayList<AbstractFunctionDefinitionNode>();
	private final String tempVarName = "ACC_TEMP_";
	private int tempVarIndex = 0;

	@Override
	public AST transform(AST unit) throws SyntaxException {
		ASTNode rootNode = unit.getRootNode();
		List<ExternalDefinitionNode> definitions = new ArrayList<ExternalDefinitionNode>();

		unitFactory = unit.getASTFactory();
		factory = unitFactory.getNodeFactory();
		for (int i = 0; i < rootNode.numChildren(); i++) {
			ASTNode node = rootNode.child(i);

			// For now, assume abstract functions are in the outermost scope.
			if (node instanceof AbstractFunctionDefinitionNode) {
				abstractFunctions.add((AbstractFunctionDefinitionNode) node);
			}
		}
		if (abstractFunctions.size() == 0) {
			// No abstract functions. Don't do anything.
			return unit;
		}
		unit.release();
		definitions.addAll(bigOFacts());
		for (int i = 0; i < rootNode.numChildren(); i++) {
			ExternalDefinitionNode node = (ExternalDefinitionNode) rootNode
					.child(i);

			definitions.add(node.copy());
			if (abstractFunctions.size() > 0 && abstractFunctions.get(0).equals(node)) {
				definitions
						.addAll(taylorExpansions(abstractFunctions.remove(0)));
			}
		}
		return unitFactory.newTranslationUnit(factory.newTranslationUnitNode(
				rootNode.getSource(), definitions));
	}

	private List<ExternalDefinitionNode> bigOFacts() throws SyntaxException {
		List<ExternalDefinitionNode> result = new ArrayList<ExternalDefinitionNode>();
		Source source = abstractFunctions.get(0).getSource();

		result.add(bigOPositive(source));
		result.add(bigOMultiplication(source));
		result.add(bigOConstantMultiplication(source));
		return result;
	}

	/** $assume $forall {double x | x > 0} $O(x) > 0; */
	private ExternalDefinitionNode bigOPositive(Source source)
			throws SyntaxException {
		VariableDeclarationNode variable = factory
				.newVariableDeclarationNode(
						source,
						factory.newIdentifierNode(source, tempVarName
								+ tempVarIndex++),
						factory.newBasicTypeNode(source, BasicTypeKind.INT));
		List<ExpressionNode> restrictionArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> bigOArgument = new ArrayList<ExpressionNode>();
		List<ExpressionNode> expressionArguments = new ArrayList<ExpressionNode>();
		ExternalDefinitionNode result;

		restrictionArguments.add(factory.newIdentifierExpressionNode(source,
				variable.getIdentifier().copy()));
		restrictionArguments.add(factory.newIntegerConstantNode(source, "0"));
		bigOArgument.add(factory.newIdentifierExpressionNode(source, variable
				.getIdentifier().copy()));
		expressionArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument));
		expressionArguments.add(factory.newIntegerConstantNode(source, "0"));
		result = factory.newAssumeNode(source, factory
				.newQuantifiedExpressionNode(source, Quantifier.FORALL,
						variable, factory.newOperatorNode(source, Operator.GT,
								restrictionArguments), factory.newOperatorNode(
								source, Operator.GT, expressionArguments)));
		return result;
	}

	/** $assume $forall {double x | x > 0} x*$O(x) == $O(x*x); */
	private ExternalDefinitionNode bigOMultiplication(Source source)
			throws SyntaxException {
		VariableDeclarationNode variable = factory
				.newVariableDeclarationNode(
						source,
						factory.newIdentifierNode(source, tempVarName
								+ tempVarIndex++),
						factory.newBasicTypeNode(source, BasicTypeKind.INT));
		IdentifierExpressionNode x = factory.newIdentifierExpressionNode(
				source, variable.getIdentifier().copy());
		List<ExpressionNode> restrictionArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> bigOArgument1 = new ArrayList<ExpressionNode>();
		List<ExpressionNode> xTimesBigOArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> xTimesxArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> bigOArgument2 = new ArrayList<ExpressionNode>();
		List<ExpressionNode> expressionArguments = new ArrayList<ExpressionNode>();
		ExternalDefinitionNode result;

		restrictionArguments.add(x.copy());
		restrictionArguments.add(factory.newIntegerConstantNode(source, "0"));
		bigOArgument1.add(x.copy());
		xTimesBigOArguments.add(x.copy());
		xTimesBigOArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument1));
		xTimesxArguments.add(x.copy());
		xTimesxArguments.add(x.copy());
		bigOArgument2.add(factory.newOperatorNode(source, Operator.TIMES,
				xTimesxArguments));
		expressionArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument1));
		expressionArguments.add(factory.newOperatorNode(source, Operator.TIMES,
				xTimesBigOArguments));
		expressionArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument2));
		result = factory.newAssumeNode(source, factory
				.newQuantifiedExpressionNode(source, Quantifier.FORALL,
						variable, factory.newOperatorNode(source, Operator.GT,
								restrictionArguments), factory.newOperatorNode(
								source, Operator.EQUALS, expressionArguments)));
		return result;

	}

	// $assume $forall {i=1 .. 10} $forall {double x | x > 0} i*$O(x) <= $O(x);
	private ExternalDefinitionNode bigOConstantMultiplication(Source source)
			throws SyntaxException {
		VariableDeclarationNode variable0 = factory
				.newVariableDeclarationNode(
						source,
						factory.newIdentifierNode(source, tempVarName
								+ tempVarIndex++),
						factory.newBasicTypeNode(source, BasicTypeKind.INT));
		VariableDeclarationNode variable1 = factory
				.newVariableDeclarationNode(
						source,
						factory.newIdentifierNode(source, tempVarName
								+ tempVarIndex++),
						factory.newBasicTypeNode(source, BasicTypeKind.INT));
		IdentifierExpressionNode i = factory.newIdentifierExpressionNode(
				source, variable0.getIdentifier().copy());
		IdentifierExpressionNode x = factory.newIdentifierExpressionNode(
				source, variable0.getIdentifier().copy());
		List<ExpressionNode> restrictionArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> bigOArgument = new ArrayList<ExpressionNode>();
		List<ExpressionNode> iTimesBigOArguments = new ArrayList<ExpressionNode>();
		List<ExpressionNode> expressionArguments = new ArrayList<ExpressionNode>();
		ExpressionNode restriction;
		ExpressionNode expression;
		ExpressionNode innerForall;
		ExternalDefinitionNode result;

		bigOArgument.add(x.copy());
		iTimesBigOArguments.add(i);
		iTimesBigOArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument));
		restrictionArguments.add(x.copy());
		restrictionArguments.add(factory.newIntegerConstantNode(source, "0"));
		restriction = factory.newOperatorNode(source, Operator.GT,
				restrictionArguments);
		expressionArguments.add(factory.newOperatorNode(source, Operator.TIMES,
				iTimesBigOArguments));
		expressionArguments.add(factory.newOperatorNode(source, Operator.BIG_O,
				bigOArgument));
		expression = factory.newOperatorNode(source, Operator.LTE,
				expressionArguments);
		innerForall = factory.newQuantifiedExpressionNode(source,
				Quantifier.FORALL, variable1, restriction, expression);
		result = factory.newAssumeNode(source, factory
				.newQuantifiedExpressionNode(source, Quantifier.FORALL,
						variable0, factory.newIntegerConstantNode(source, "1"),
						factory.newIntegerConstantNode(source, "10"),
						innerForall));
		return result;
	}

	private List<ExternalDefinitionNode> taylorExpansions(
			AbstractFunctionDefinitionNode abstractFunction) {
		List<ExternalDefinitionNode> result = new ArrayList<ExternalDefinitionNode>();
		int continuity = abstractFunction.continuity();

		switch (continuity) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			throw new ABCUnsupportedException(
					"Taylor expansions of abstract functions with continuity higher than "
							+ continuity);
		}
		return result;
	}
}
