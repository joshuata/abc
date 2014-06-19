package edu.udel.cis.vsl.abc.antlr2ast.impl;

import static edu.udel.cis.vsl.abc.parse.IF.CParser.ABSENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ABSTRACT_DECLARATOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ALIGNOF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.AMPERSAND;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.AND;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ARRAY_ELEMENT_DESIGNATOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ARRAY_SUFFIX;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ARROW;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ASSIGN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ASSUME;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.AT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ATOMIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BIG_O;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BITANDEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BITOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BITOREQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BITXOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BITXOREQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BREAK;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CALL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CASE_LABELED_STATEMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CAST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CHARACTER_CONSTANT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CHOOSE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CIVLATOM;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CIVLATOMIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.COLLECTIVE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.COMMA;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.COMPOUND_LITERAL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.COMPOUND_STATEMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CONST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CONTINUE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DECLARATION;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DECLARATOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DEFAULT_LABELED_STATEMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DERIVATIVE_EXPRESSION;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DIV;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DIVEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DO;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DOT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ELLIPSIS;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ENSURES;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ENUMERATION_CONSTANT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.EQUALS;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.EXISTS;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.EXPR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.EXPRESSION_STATEMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FALSE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FIELD_DESIGNATOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FLOATING_CONSTANT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FORALL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FUNCTION_DEFINITION;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FUNCTION_SUFFIX;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.GENERIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.GOTO;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.GT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.GTE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.HERE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.IDENTIFIER;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.IDENTIFIER_LABELED_STATEMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.IDENTIFIER_LIST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.IF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.IMPLIES;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INDEX;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INITIALIZER_LIST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INTEGER_CONSTANT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.LT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.LTE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.MOD;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.MODEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.NEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.NOT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.OPERATOR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.OR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PARAMETER_TYPE_LIST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PARENTHESIZED_EXPRESSION;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PLUS;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PLUSEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.POST_DECREMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.POST_INCREMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PRAGMA;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PRE_DECREMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PRE_INCREMENT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.PROCNULL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.QMARK;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.REQUIRES;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.RESTRICT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.RESULT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.RETURN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ROOT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SCALAR_INITIALIZER;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SCOPE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SCOPEOF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SELF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHIFTLEFT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHIFTLEFTEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHIFTRIGHT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHIFTRIGHTEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SIZEOF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SPAWN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STAR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STAREQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STATIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STATICASSERT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STRING_LITERAL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STRUCT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SUB;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SUBEQ;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SWITCH;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TILDE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TRUE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TYPE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TYPE_NAME;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.UNIFORM;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.VOLATILE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.WHEN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.WHILE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.SimpleScope;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CharacterConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DerivativeExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FloatingConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode.Quantifier;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.StringLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.SwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode.StatementKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.AtomicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypedefNameNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CharacterToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.StringToken;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

/**
 * Builds an AST from an ANTLR tree.
 * 
 * TODO: standardize token names across grammar and classes.
 * 
 * @author siegel
 * 
 */
public class CommonASTBuilder implements ASTBuilder {

	// Instance fields...

	private CParser parser;

	private NodeFactory nodeFactory;

	private TokenFactory sourceFactory;

	private ASTFactory astFactory;

	private CommonTree rootTree;

	// Constructors...

	/**
	 * Constructs a new ASTBuilder for the given ANTLR tree.
	 * 
	 * @param factory
	 *            an ASTFactory to use
	 * @param rootTree
	 *            the root of the ANTLR tree
	 * @param tokenSource
	 *            the CTokenSource used to produce the ANTLR tree
	 * 
	 */
	public CommonASTBuilder(CParser parser, ASTFactory astFactory,
			CommonTree rootTree) {
		this.parser = parser;
		this.astFactory = astFactory;
		this.nodeFactory = astFactory.getNodeFactory();
		this.sourceFactory = astFactory.getTokenFactory();
		this.rootTree = rootTree;
	}

	// Public methods...

	/**
	 * The main method: given an ANTLR tree, produces a TranslationUnit.
	 * 
	 * @param tree
	 *            an ANTLR syntax tree
	 * @return a TranslationUnit representing the given syntax tree
	 * @throws SyntaxException
	 *             if there is something in the tree that does not conform to
	 *             the C11 standard
	 */
	@Override
	public AST getTranslationUnit() throws SyntaxException {
		ASTNode root = translateTranslationUnit(rootTree);

		return astFactory.newAST(root);
	}

	@Override
	public ExpressionNode translateExpression(CommonTree expressionTree,
			SimpleScope scope) throws SyntaxException {
		int kind = expressionTree.getType();

		if (kind == ABSENT)
			return null;
		return translateExpression(newSource(expressionTree), expressionTree,
				scope);
	}

	// Supporting methods...

	private SyntaxException error(String message, CommonTree tree) {
		return new SyntaxException(message, newSource(tree));
	}

	private SyntaxException error(String message, ASTNode node) {
		return new SyntaxException(message, node.getSource());
	}

	private Source newSource(CommonTree tree) {
		return parser.source(tree);
	}

	private SpecifierAnalysis newSpecifierAnalysis(CommonTree specifiers)
			throws SyntaxException {
		return new SpecifierAnalysis(specifiers, parser);
	}

	private boolean isFunction(TypeNode type, SimpleScope scope)
			throws SyntaxException {
		return isFunction(type, scope, new HashSet<String>());
	}

	private boolean isFunction(TypeNode type, SimpleScope scope,
			Set<String> seenNames) throws SyntaxException {
		TypeNodeKind kind = type.kind();

		switch (kind) {
		case FUNCTION:
			return true;
		case TYPEDEF_NAME: {
			String typeName = ((TypedefNameNode) type).getName().name();
			TypeNode referencedNode = scope.getReferencedType(typeName);

			if (seenNames.contains(typeName))
				throw error("Cycle in typedefs", type);
			while (referencedNode == null) {
				scope = scope.getParent();
				if (scope == null)
					throw error("Could not resolve typedef name", type);
				referencedNode = scope.getReferencedType(typeName);
			}
			seenNames.add(typeName);
			return isFunction(referencedNode, scope, seenNames);
		}
		default:
			return false;
		}
	}

	private IdentifierNode translateIdentifier(CommonTree identifier) {
		Token idToken = identifier.getToken();
		CToken token;
		Source source;

		if (idToken instanceof CToken)
			token = (CToken) idToken;
		else {
			token = sourceFactory.newCToken(idToken, null);
		}
		source = sourceFactory.newSource(token);

		return nodeFactory.newIdentifierNode(source, token.getText());
	}

	private SequenceNode<VariableDeclarationNode> translateScopeListDef(
			CommonTree list) {
		int kind = list.getType();

		if (kind == ABSENT) {
			return null;
		} else {
			int numChildren = list.getChildCount();
			LinkedList<VariableDeclarationNode> nodeList = new LinkedList<VariableDeclarationNode>();
			SequenceNode<VariableDeclarationNode> result;
			Source source = newSource(list);

			for (int i = 0; i < numChildren; i++) {
				CommonTree child = (CommonTree) list.getChild(i);
				IdentifierNode identifierNode = translateIdentifier(child);
				Source childSource = identifierNode.getSource();
				TypeNode typeNode = nodeFactory.newScopeTypeNode(childSource);
				VariableDeclarationNode declNode = nodeFactory
						.newVariableDeclarationNode(childSource,
								identifierNode, typeNode);

				nodeList.add(declNode);
			}
			result = nodeFactory.newSequenceNode(source, "scope list def",
					nodeList);
			return result;
		}
	}

	private SequenceNode<ExpressionNode> translateScopeListUse(CommonTree list) {
		int kind = list.getType();

		if (kind == ABSENT) {
			return null;
		} else {
			int numChildren = list.getChildCount();
			LinkedList<ExpressionNode> nodeList = new LinkedList<ExpressionNode>();
			SequenceNode<ExpressionNode> result;
			Source source = newSource(list);

			for (int i = 0; i < numChildren; i++) {
				CommonTree child = (CommonTree) list.getChild(i);
				IdentifierNode identifierNode = translateIdentifier(child);
				Source childSource = identifierNode.getSource();
				IdentifierExpressionNode exprNode = nodeFactory
						.newIdentifierExpressionNode(childSource,
								identifierNode);

				nodeList.add(exprNode);
			}
			result = nodeFactory.newSequenceNode(source, "scope list use",
					nodeList);
			return result;
		}
	}

	private IntegerConstantNode translateIntegerConstant(Source source,
			CommonTree integerConstant) throws SyntaxException {
		return nodeFactory.newIntegerConstantNode(source,
				integerConstant.getText());
	}

	private FloatingConstantNode translateFloatingConstant(Source source,
			CommonTree floatingConstant) throws SyntaxException {
		return nodeFactory.newFloatingConstantNode(source,
				floatingConstant.getText());
	}

	private CharacterConstantNode translateCharacterConstant(Source source,
			CommonTree characterConstant) throws SyntaxException {
		CharacterToken token = (CharacterToken) characterConstant.getToken();

		return nodeFactory.newCharacterConstantNode(source,
				characterConstant.getText(), token.getExecutionCharacter());
	}

	private ConstantNode translateTrue(Source source) {
		return nodeFactory.newBooleanConstantNode(source, true);
	}

	private ConstantNode translateFalse(Source source) {
		return nodeFactory.newBooleanConstantNode(source, false);
	}

	private StringLiteralNode translateStringLiteral(Source source,
			CommonTree stringLiteral) throws SyntaxException {
		StringToken token = (StringToken) stringLiteral.getToken();

		return nodeFactory.newStringLiteralNode(source,
				stringLiteral.getText(), token.getStringLiteral());
	}

	/**
	 * Translates an expression.
	 * 
	 * @param expressionTree
	 *            any CommonTree node representing an expression
	 * @return an ExpressionNode
	 * @throws SyntaxException
	 */
	private ExpressionNode translateExpression(Source source,
			CommonTree expressionTree, SimpleScope scope)
			throws SyntaxException {
		int kind = expressionTree.getType();

		switch (kind) {
		case INTEGER_CONSTANT:
			return translateIntegerConstant(source, expressionTree);
		case FLOATING_CONSTANT:
			return translateFloatingConstant(source, expressionTree);
		case ENUMERATION_CONSTANT:
			return nodeFactory
					.newEnumerationConstantNode(translateIdentifier((CommonTree) expressionTree
							.getChild(0)));
		case CHARACTER_CONSTANT:
			return translateCharacterConstant(source, expressionTree);
		case STRING_LITERAL:
			return translateStringLiteral(source, expressionTree);
		case IDENTIFIER:
			return nodeFactory.newIdentifierExpressionNode(source,
					translateIdentifier(expressionTree));
		case PARENTHESIZED_EXPRESSION:
			return translateExpression(source,
					(CommonTree) expressionTree.getChild(1), scope);
		case GENERIC: // TODO: genericSelection
			throw new UnsupportedOperationException(
					"Generic selections not yet implemented");
		case CALL:
			return translateCall(source, expressionTree, scope);
		case DOT:
		case ARROW:
			return translateDotOrArrow(source, expressionTree, scope);
		case COMPOUND_LITERAL:
			return translateCompoundLiteral(source, expressionTree, scope);
		case OPERATOR:
			return translateOperatorExpression(source, expressionTree, scope);
		case SIZEOF:
			return translateSizeOf(source, expressionTree, scope);
		case SCOPEOF:
			return translateScopeOf(source, expressionTree, scope);
		case ALIGNOF:
			return nodeFactory.newAlignOfNode(
					source,
					translateTypeName((CommonTree) expressionTree.getChild(0),
							scope));
		case CAST:
			return nodeFactory.newCastNode(
					source,
					translateTypeName((CommonTree) expressionTree.getChild(0),
							scope),
					translateExpression(
							(CommonTree) expressionTree.getChild(1), scope));
		case SELF:
			return nodeFactory.newSelfNode(source);
		case PROCNULL:
			return nodeFactory.newProcnullNode(source);
		case HERE:
			return nodeFactory.newHereNode(source);
		case ROOT:
			return nodeFactory.newRootNode(source);
		case SPAWN: {
			return nodeFactory.newSpawnNode(source,
					translateCall(source, expressionTree, scope));
		}
		case TRUE:
			return translateTrue(source);
		case FALSE:
			return translateFalse(source);
		case RESULT:
			return nodeFactory.newResultNode(source);
		case AT: {
			CommonTree procExprTree = (CommonTree) expressionTree.getChild(0);
			CommonTree identifierTree = (CommonTree) expressionTree.getChild(1);
			ExpressionNode procExpr = translateExpression(procExprTree, scope);
			IdentifierNode identifierNode = translateIdentifier(identifierTree);

			return nodeFactory.newRemoteExpressionNode(source, procExpr,
					nodeFactory.newIdentifierExpressionNode(
							newSource(identifierTree), identifierNode));
		}
		case COLLECTIVE:
			return nodeFactory.newCollectiveExpressionNode(
					source,
					translateExpression(
							(CommonTree) expressionTree.getChild(0), scope),
					translateExpression(
							(CommonTree) expressionTree.getChild(1), scope),
					translateExpression(
							(CommonTree) expressionTree.getChild(2), scope));
		case FORALL: {
			SimpleScope newScope = new SimpleScope(scope);
			VariableDeclarationNode variable;

			if (expressionTree.getChild(0).getType() == TYPE_NAME) {
				ExpressionNode restriction = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);

				variable = nodeFactory.newVariableDeclarationNode(
						source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(1)),
						translateTypeName(
								(CommonTree) expressionTree.getChild(0),
								newScope));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.FORALL,
						variable,
						restriction,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			} else {
				
				ExpressionNode lower, upper;

				lower = translateExpression(
						(CommonTree) expressionTree.getChild(1), newScope);
				upper = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);
				variable = nodeFactory.newVariableDeclarationNode(source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(0)), nodeFactory.newBasicTypeNode(
								source, BasicTypeKind.INT));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.FORALL,
						variable,
						lower,
						upper,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			}
		}
		case UNIFORM: {
			SimpleScope newScope = new SimpleScope(scope);
			VariableDeclarationNode variable;

			if (expressionTree.getChild(0).getType() == TYPE_NAME) {
				ExpressionNode restriction = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);

				variable = nodeFactory.newVariableDeclarationNode(
						source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(1)),
						translateTypeName(
								(CommonTree) expressionTree.getChild(0),
								newScope));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.UNIFORM,
						variable,
						restriction,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			} else {
				ExpressionNode lower, upper;

				lower = translateExpression(
						(CommonTree) expressionTree.getChild(1), newScope);
				upper = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);
				variable = nodeFactory.newVariableDeclarationNode(source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(0)), nodeFactory.newBasicTypeNode(
								source, BasicTypeKind.INT));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.UNIFORM,
						variable,
						lower,
						upper,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			}
		}
		case EXISTS: {
			SimpleScope newScope = new SimpleScope(scope);
			VariableDeclarationNode variable;

			if (expressionTree.getChild(0).getType() == TYPE_NAME) {
				ExpressionNode restriction = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);

				variable = nodeFactory.newVariableDeclarationNode(
						source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(1)),
						translateTypeName(
								(CommonTree) expressionTree.getChild(0),
								newScope));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.EXISTS,
						variable,
						restriction,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			} else {
				ExpressionNode lower, upper;

				lower = translateExpression(
						(CommonTree) expressionTree.getChild(1), newScope);
				upper = translateExpression(
						(CommonTree) expressionTree.getChild(2), newScope);
				variable = nodeFactory.newVariableDeclarationNode(source,
						translateIdentifier((CommonTree) expressionTree
								.getChild(0)), nodeFactory.newBasicTypeNode(
								source, BasicTypeKind.INT));
				return nodeFactory.newQuantifiedExpressionNode(
						source,
						Quantifier.EXISTS,
						variable,
						lower,
						upper,
						translateExpression(
								(CommonTree) expressionTree.getChild(3),
								newScope));
			}
		}
		case DERIVATIVE_EXPRESSION:
			return translateDeriv(source, expressionTree, scope);
		default:
			throw error("Unknown expression kind", expressionTree);
		}
	}

	private ExpressionNode translateScopeOf(Source source,
			CommonTree expressionTree, SimpleScope scope)
			throws SyntaxException {
		ExpressionNode expression = this.translateExpression(
				(CommonTree) expressionTree.getChild(0), scope);

		return nodeFactory.newScopeOfNode(source, expression);
	}

	/**
	 * Translates a derivative expression
	 * 
	 * @param source
	 *            The source information.
	 * @param expressionTree
	 *            CommonTree of type DERIV, representing a derivative
	 *            expression.
	 * @param scope
	 *            The scope containing this expression.
	 * @return A DerivativeExpressionNode corresponding to the ANTLR tree.
	 * @throws SyntaxException
	 */
	private DerivativeExpressionNode translateDeriv(Source source,
			CommonTree derivTree, SimpleScope scope) throws SyntaxException {
		CommonTree functionTree = (CommonTree) derivTree.getChild(0);
		CommonTree partialListTree = (CommonTree) derivTree.getChild(1);
		CommonTree argumentListTree = (CommonTree) derivTree.getChild(2);
		int numPartials = partialListTree.getChildCount();
		int numArgs = argumentListTree.getChildCount();
		ExpressionNode function = translateExpression(functionTree, scope);
		List<PairNode<IdentifierExpressionNode, IntegerConstantNode>> partials;
		List<ExpressionNode> arguments;

		partials = new LinkedList<PairNode<IdentifierExpressionNode, IntegerConstantNode>>();
		arguments = new LinkedList<ExpressionNode>();
		for (int i = 0; i < numPartials; i++) {
			CommonTree partialTree = (CommonTree) partialListTree.getChild(i);
			ExpressionNode partialIdentifier = translateExpression(
					(CommonTree) partialTree.getChild(0), scope);
			ExpressionNode partialDegree = translateExpression(
					(CommonTree) partialTree.getChild(1), scope);

			assert partialIdentifier instanceof IdentifierExpressionNode;
			assert partialDegree instanceof IntegerConstantNode;
			partials.add(nodeFactory.newPairNode(newSource(partialTree),
					(IdentifierExpressionNode) partialIdentifier,
					(IntegerConstantNode) partialDegree));
		}
		for (int i = 0; i < numArgs; i++) {
			CommonTree argumentTree = (CommonTree) argumentListTree.getChild(i);
			ExpressionNode argumentNode = translateExpression(argumentTree,
					scope);

			arguments.add(argumentNode);
		}
		return nodeFactory.newDerivativeExpressionNode(source, function,
				nodeFactory.newSequenceNode(newSource(partialListTree),
						"partials", partials), nodeFactory.newSequenceNode(
						newSource(argumentListTree), "arguments", arguments));
	}

	/**
	 * Translates a function call expression.
	 * 
	 * @param callTree
	 *            CommonTree node of type CALL, representing a function call
	 * @return a FunctionCallNode corresponding to the ANTLR tree
	 * @throws SyntaxException
	 */
	private FunctionCallNode translateCall(Source source, CommonTree callTree,
			SimpleScope scope) throws SyntaxException {
		CommonTree functionTree = (CommonTree) callTree.getChild(1);
		CommonTree argumentListTree = (CommonTree) callTree.getChild(2);
		ExpressionNode functionNode = translateExpression(functionTree, scope);
		int numArgs = argumentListTree.getChildCount();
		List<ExpressionNode> argumentList = new LinkedList<ExpressionNode>();
		SequenceNode<ExpressionNode> scopeList = translateScopeListUse((CommonTree) callTree
				.getChild(4));

		for (int i = 0; i < numArgs; i++) {
			CommonTree argumentTree = (CommonTree) argumentListTree.getChild(i);
			ExpressionNode argumentNode = translateExpression(argumentTree,
					scope);

			argumentList.add(argumentNode);
		}
		return nodeFactory.newFunctionCallNode(source, functionNode,
				argumentList, scopeList);
	}

	/**
	 * 
	 * @param compoundLiteralTree
	 * @return
	 * @throws SyntaxException
	 */
	private CompoundLiteralNode translateCompoundLiteral(Source source,
			CommonTree compoundLiteralTree, SimpleScope scope)
			throws SyntaxException {
		// tree has form:
		// ^(COMPOUND_LITERAL LPAREN typeName initializerList RCURLY)
		TypeNode typeNode = translateTypeName(
				(CommonTree) compoundLiteralTree.getChild(1), scope);
		CompoundInitializerNode initializerList = (CompoundInitializerNode) translateInitializer(
				(CommonTree) compoundLiteralTree.getChild(2), scope);

		return nodeFactory.newCompoundLiteralNode(source, typeNode,
				initializerList);
	}

	/**
	 * 
	 * @param expressionTree
	 * @return
	 * @throws SyntaxException
	 */
	private ExpressionNode translateDotOrArrow(Source source,
			CommonTree expressionTree, SimpleScope scope)
			throws SyntaxException {
		int kind = expressionTree.getType();
		CommonTree argumentNode = (CommonTree) expressionTree.getChild(0);
		CommonTree fieldNode = (CommonTree) expressionTree.getChild(1);
		ExpressionNode argument = translateExpression(argumentNode, scope);
		IdentifierNode fieldName = translateIdentifier(fieldNode);

		if (kind == DOT)
			return nodeFactory.newDotNode(source, argument, fieldName);
		else
			return nodeFactory.newArrowNode(source, argument, fieldName);
	}

	/**
	 * 
	 * @param expressionTree
	 * @return
	 * @throws SyntaxException
	 */
	private OperatorNode translateOperatorExpression(Source source,
			CommonTree expressionTree, SimpleScope scope)
			throws SyntaxException {
		CommonTree operatorTree = (CommonTree) expressionTree.getChild(0);
		int operatorKind = operatorTree.getType();
		CommonTree argumentList = (CommonTree) expressionTree.getChild(1);
		int numArgs = argumentList.getChildCount();
		List<ExpressionNode> arguments = new LinkedList<ExpressionNode>();
		Operator operator;

		for (int i = 0; i < numArgs; i++) {
			ExpressionNode argument = translateExpression(
					(CommonTree) argumentList.getChild(i), scope);

			arguments.add(argument);
		}
		switch (operatorKind) {
		case AMPERSAND:
			operator = numArgs == 1 ? Operator.ADDRESSOF : Operator.BITAND;
			break;
		case ASSIGN:
			operator = Operator.ASSIGN;
			break;
		case BIG_O:
			operator = Operator.BIG_O;
			break;
		case BITANDEQ:
			operator = Operator.BITANDEQ;
			break;
		case TILDE:
			operator = Operator.BITCOMPLEMENT;
			break;
		case BITOR:
			operator = Operator.BITOR;
			break;
		case BITOREQ:
			operator = Operator.BITOREQ;
			break;
		case BITXOR:
			operator = Operator.BITXOR;
			break;
		case BITXOREQ:
			operator = Operator.BITXOREQ;
			break;
		case COMMA:
			operator = Operator.COMMA;
			break;
		case QMARK:
			operator = Operator.CONDITIONAL;
			break;
		case STAR:
			operator = numArgs == 1 ? Operator.DEREFERENCE : Operator.TIMES;
			break;
		case DIV:
			operator = Operator.DIV;
			break;
		case DIVEQ:
			operator = Operator.DIVEQ;
			break;
		case EQUALS:
			operator = Operator.EQUALS;
			break;
		case GT:
			operator = Operator.GT;
			break;
		case GTE:
			operator = Operator.GTE;
			break;
		case AND:
			operator = Operator.LAND;
			break;
		case OR:
			operator = Operator.LOR;
			break;
		case IMPLIES:
			operator = Operator.IMPLIES;
			break;
		case LT:
			operator = Operator.LT;
			break;
		case LTE:
			operator = Operator.LTE;
			break;
		case SUB:
			operator = numArgs == 1 ? Operator.UNARYMINUS : Operator.MINUS;
			break;
		case SUBEQ:
			operator = Operator.MINUSEQ;
			break;
		case MOD:
			operator = Operator.MOD;
			break;
		case MODEQ:
			operator = Operator.MODEQ;
			break;
		case NEQ:
			operator = Operator.NEQ;
			break;
		case NOT:
			operator = Operator.NOT;
			break;
		case PLUS:
			operator = numArgs == 1 ? Operator.UNARYPLUS : Operator.PLUS;
			break;
		case PLUSEQ:
			operator = Operator.PLUSEQ;
			break;
		case POST_DECREMENT:
			operator = Operator.POSTDECREMENT;
			break;
		case POST_INCREMENT:
			operator = Operator.POSTINCREMENT;
			break;
		case PRE_DECREMENT:
			operator = Operator.PREDECREMENT;
			break;
		case PRE_INCREMENT:
			operator = Operator.PREINCREMENT;
			break;
		case SHIFTLEFT:
			operator = Operator.SHIFTLEFT;
			break;
		case SHIFTLEFTEQ:
			operator = Operator.SHIFTLEFTEQ;
			break;
		case SHIFTRIGHT:
			operator = Operator.SHIFTRIGHT;
			break;
		case SHIFTRIGHTEQ:
			operator = Operator.SHIFTRIGHTEQ;
			break;
		case INDEX:
			operator = Operator.SUBSCRIPT;
			break;
		case STAREQ:
			operator = Operator.TIMESEQ;
			break;
		default:
			throw error("Unknown operator :", operatorTree);
		}
		return nodeFactory.newOperatorNode(source, operator, arguments);
	}

	/**
	 * 
	 * @param expressionTree
	 * @return
	 * @throws SyntaxException
	 */
	private SizeofNode translateSizeOf(Source source,
			CommonTree expressionTree, SimpleScope scope)
			throws SyntaxException {
		int kind = expressionTree.getChild(0).getType();
		CommonTree child = (CommonTree) expressionTree.getChild(1);
		SizeableNode sizeable;

		if (kind == EXPR)
			sizeable = translateExpression(child, scope);
		else if (kind == TYPE)
			sizeable = translateTypeName(child, scope);
		else
			throw error("Unexpected argument to sizeof", expressionTree);
		return nodeFactory.newSizeofNode(source, sizeable);
	}

	/**
	 * If typeNode is a struct, union, or enumeration type node, make it in
	 * complete, i.e., delete the "body" (list of fields, or enumerators) if it
	 * is present. Otherwise, a no-op.
	 * 
	 * @param typeNode
	 *            any type node
	 */
	private TypeNode makeIncomplete(TypeNode typeNode) {
		if (typeNode instanceof StructureOrUnionTypeNode) {
			((StructureOrUnionTypeNode) typeNode).makeIncomplete();
		} else if (typeNode instanceof EnumerationTypeNode) {
			((EnumerationTypeNode) typeNode).makeIncomplete();
		}
		return typeNode;
	}

	private VariableDeclarationNode translateScopeDeclaration(
			CommonTree scopeDeclTree, SimpleScope scope) throws SyntaxException {
		CommonTree identifierTree = (CommonTree) scopeDeclTree.getChild(0);
		IdentifierNode identifierNode = translateIdentifier(identifierTree);
		Source source = newSource(scopeDeclTree);
		TypeNode scopeType = nodeFactory.newScopeTypeNode(source);
		VariableDeclarationNode result = nodeFactory
				.newVariableDeclarationNode(source, identifierNode, scopeType);

		return result;
	}

	/**
	 * Returns a list consisting of the following kinds of external definitions:
	 * 
	 * <ul>
	 * <li>VariableDeclarationNode</li>
	 * <li>FunctionDeclarationNode</li> (includes FunctionDefinitionNode)
	 * <li>StructureOrUnionTypeNode</li>
	 * <li>EnumerationTypeNode</li>
	 * <li>TypedefDeclarationNode</li>
	 * </ul>
	 * 
	 * @param declarationTree
	 *            CommonTree node of type DECLARATION (not static assertions)
	 * @return list of external definitions
	 * @throws SyntaxException
	 *             if the declaration does not conform to the C11 Standard
	 */
	private List<ExternalDefinitionNode> translateDeclaration(
			CommonTree declarationTree, SimpleScope scope)
			throws SyntaxException {
		CommonTree declarationSpecifiers = (CommonTree) declarationTree
				.getChild(0);
		CommonTree initDeclaratorList = (CommonTree) declarationTree
				.getChild(1);
		CommonTree contractTree = (CommonTree) declarationTree.getChild(2);
		CommonTree scopeListTree = (CommonTree) declarationTree.getChild(3);
		SequenceNode<ContractNode> contract = getContract(contractTree, scope);
		SpecifierAnalysis analysis = newSpecifierAnalysis(declarationSpecifiers);
		int numDeclarators = initDeclaratorList.getChildCount();
		ArrayList<ExternalDefinitionNode> definitionList = new ArrayList<ExternalDefinitionNode>();
		Source source = newSource(declarationTree);
		SequenceNode<VariableDeclarationNode> scopeList = translateScopeListDef(scopeListTree);

		if (numDeclarators == 0) {
			TypeNode baseType;
			ExternalDefinitionNode definition;

			// C11 Sec. 6.7 Constraint 2:
			// "A declaration other than a static_assert declaration shall
			// declare at least a declarator (other than the parameters of a
			// function or the members of a structure or union), a tag, or the
			// members of an enumeration."

			// error if $input or $output occur here:
			if (analysis.inputQualifier || analysis.outputQualifier)
				throw error("Use of $input or $output without variable",
						declarationTree);
			baseType = newSpecifierType(analysis, scope);
			if (baseType instanceof EnumerationTypeNode)
				definition = (EnumerationTypeNode) baseType;
			else if (baseType instanceof StructureOrUnionTypeNode)
				definition = (StructureOrUnionTypeNode) baseType;
			else
				throw error("Declaration missing declarator", declarationTree);
			definitionList.add(definition);
			return definitionList;
		}
		for (int i = 0; i < numDeclarators; i++) {
			CommonTree initDeclarator = (CommonTree) initDeclaratorList
					.getChild(i);
			CommonTree declaratorTree = (CommonTree) initDeclarator.getChild(0);
			CommonTree initializerTree = (CommonTree) initDeclarator
					.getChild(1);
			InitializerNode initializer = translateInitializer(initializerTree,
					scope);
			TypeNode baseType = i == 0 ? newSpecifierType(analysis, scope)
					: makeIncomplete(newSpecifierType(analysis, scope));
			DeclaratorData data = processDeclarator(declaratorTree, baseType,
					scope);
			ExternalDefinitionNode definition;

			// special handling of $input and $output qualifiers required
			// these must not go in base type but must be pulled all the
			// way out to modify the final declarator.
			// So remove them from the base type, and add them back at end.
			// $input int x, y;
			// $input const double a[n];

			if (analysis.typedefCount > 0) {
				TypeNode typeNode = data.type;
				String name;

				definition = nodeFactory.newTypedefDeclarationNode(source,
						data.identifier, typeNode);
				if (data.identifier == null)
					throw error("Missing identifier in typedef", declaratorTree);
				name = data.identifier.name();
				scope.putMapping(name, data.type);
			} else if (isFunction(data.type, scope)) {
				FunctionTypeNode typeNode = (FunctionTypeNode) data.type;
				FunctionDeclarationNode declaration;

				if (analysis.abstractSpecifier) {
					declaration = nodeFactory
							.newAbstractFunctionDefinitionNode(source,
									data.identifier, typeNode, contract,
									analysis.continuity);
				} else {
					declaration = nodeFactory.newFunctionDeclarationNode(
							source, data.identifier, typeNode, contract);
				}
				setFunctionSpecifiers(declaration, analysis);
				setStorageSpecifiers(declaration, analysis, scope);
				if (initializer != null)
					throw error("Initializer used in function declaration",
							initializerTree);
				checkAlignmentSpecifiers(declaration, analysis);
				definition = declaration;
			} else {
				VariableDeclarationNode declaration;

				if (analysis.inputQualifier)
					data.type.setInputQualified(true);
				if (analysis.outputQualifier)
					data.type.setOutputQualified(true);
				declaration = nodeFactory.newVariableDeclarationNode(source,
						data.identifier, data.type);
				if (initializer != null)
					declaration.setInitializer(initializer);
				setStorageSpecifiers(declaration, analysis, scope);
				setAlignmentSpecifiers(declaration, analysis, scope);
				checkFunctionSpecifiers(declaration, analysis);
				definition = declaration;
			}
			if (scopeList != null) {
				definition = nodeFactory.newScopeParameterizedDeclarationNode(
						sourceFactory.join(scopeList.getSource(),
								definition.getSource()), scopeList,
						(DeclarationNode) definition);
			}
			definitionList.add(definition);
		}
		return definitionList;
	}

	/**
	 * Creates a new type node based on the result of analyzing a set of type
	 * specifiers.
	 * 
	 * Input and output specifiers are ignored, since these require special
	 * handling: they must be pulled all the way up to the final type node for
	 * the variable being declared.
	 * 
	 * @param analysis
	 * @param scope
	 * @return
	 * @throws SyntaxException
	 */
	private TypeNode newSpecifierType(SpecifierAnalysis analysis,
			SimpleScope scope) throws SyntaxException {
		TypeNode result;

		switch (analysis.typeNameKind) {
		case VOID:
			result = nodeFactory
					.newVoidTypeNode(newSource(analysis.typeSpecifierNode));
			break;
		case BASIC:
			result = nodeFactory.newBasicTypeNode(
					newSource(analysis.specifierListNode),
					analysis.basicTypeKind);
			break;
		case TYPEDEF_NAME: {
			CommonTree typedefNameTree = (CommonTree) analysis.typeSpecifierNode;
			CommonTree identifierTree = (CommonTree) typedefNameTree
					.getChild(0);
			CommonTree scopeListTree = (CommonTree) typedefNameTree.getChild(1);
			IdentifierNode identifierNode = translateIdentifier(identifierTree);
			SequenceNode<ExpressionNode> scopeListNode = translateScopeListUse(scopeListTree);

			result = nodeFactory.newTypedefNameNode(identifierNode,
					scopeListNode);
			break;
		}
		case STRUCTURE_OR_UNION:
			result = translateStructOrUnionType(analysis.typeSpecifierNode,
					scope);
			break;
		case ENUMERATION:
			result = translateEnumerationType(analysis.typeSpecifierNode, scope);
			break;
		case ATOMIC:
			result = translateAtomicType(analysis.typeSpecifierNode, scope);
			break;
		case DOMAIN: {
			CommonTree node = analysis.typeSpecifierNode;
			Source source = newSource(node);

			if (node.getChildCount() != 0) {
				CommonTree child = (CommonTree) node.getChild(0);

				if (child.getToken().getType() != CParser.ABSENT) {
					ExpressionNode dimensionNode = translateExpression(child,
							scope);

					result = nodeFactory.newDomainTypeNode(source,
							dimensionNode);
					break;
				}
			}
			result = nodeFactory.newDomainTypeNode(source);
			break;
		}
		default:
			throw new RuntimeException("Should not happen.");
		}
		if (analysis.constQualifier)
			result.setConstQualified(true);
		if (analysis.volatileQualifier)
			result.setVolatileQualified(true);
		if (analysis.restrictQualifier)
			result.setRestrictQualified(true);
		if (analysis.atomicQualifier)
			result.setAtomicQualified(true);
		// if (analysis.inputQualifier)
		// result.setInputQualified(true);
		// if (analysis.outputQualifier)
		// result.setOutputQualified(true);
		return result;
	}

	/**
	 * 
	 * @param structTree
	 *            CommonTree of type STRUCT or UNION
	 * @return
	 * @throws SyntaxException
	 */
	private StructureOrUnionTypeNode translateStructOrUnionType(
			CommonTree structTree, SimpleScope scope) throws SyntaxException {
		int kind = structTree.getType();
		boolean isStruct = kind == STRUCT;
		CommonTree tagTree = (CommonTree) structTree.getChild(0);
		CommonTree declListTree = (CommonTree) structTree.getChild(1);
		IdentifierNode tag;
		SequenceNode<FieldDeclarationNode> structDeclList;

		if (tagTree.getType() == ABSENT) {
			tag = null;
		} else {
			tag = translateIdentifier(tagTree);
		}
		if (declListTree.getType() == ABSENT) {
			structDeclList = null;
		} else {
			int numFields = declListTree.getChildCount();
			List<FieldDeclarationNode> fieldDecls = new LinkedList<FieldDeclarationNode>();

			for (int i = 0; i < numFields; i++) {
				CommonTree declTree = (CommonTree) declListTree.getChild(i);
				List<FieldDeclarationNode> fieldDeclarations = translateFieldDeclaration(
						declTree, scope);

				fieldDecls.addAll(fieldDeclarations);
			}
			structDeclList = nodeFactory.newSequenceNode(
					newSource(declListTree), "FieldDeclarations", fieldDecls);
		}
		return nodeFactory.newStructOrUnionTypeNode(newSource(structTree),
				isStruct, tag, structDeclList);
	}

	private List<FieldDeclarationNode> translateFieldDeclaration(
			CommonTree declarationTree, SimpleScope scope)
			throws SyntaxException {
		CommonTree declarationSpecifiers = (CommonTree) declarationTree
				.getChild(0); // may be ABSENT
		CommonTree structDeclaratorList = (CommonTree) declarationTree
				.getChild(1); // may be ABSENT
		SpecifierAnalysis analysis = newSpecifierAnalysis(declarationSpecifiers);
		int numDeclarators = structDeclaratorList.getChildCount();
		List<FieldDeclarationNode> result = new LinkedList<FieldDeclarationNode>();
		Source source = newSource(declarationTree);

		if (numDeclarators == 0) {
			// this can happen if the specifier is an anonymous struct or union
			TypeNode baseType = newSpecifierType(analysis, scope);

			result.add(nodeFactory.newFieldDeclarationNode(source, null,
					baseType, null));
		} else {
			for (int i = 0; i < numDeclarators; i++) {
				TypeNode baseType = i == 0 ? newSpecifierType(analysis, scope)
						: makeIncomplete(newSpecifierType(analysis, scope));
				CommonTree structDeclarator = (CommonTree) structDeclaratorList
						.getChild(i);
				CommonTree declaratorTree = (CommonTree) structDeclarator
						.getChild(0); // could be ABSENT
				CommonTree bitFieldTree = (CommonTree) structDeclarator
						.getChild(1); // could be ABSENT
				ExpressionNode bitFieldWidth = translateExpression(
						bitFieldTree, scope);
				DeclaratorData data = processDeclarator(declaratorTree,
						baseType, scope);
				FieldDeclarationNode declaration;

				if (bitFieldWidth == null)
					declaration = nodeFactory.newFieldDeclarationNode(source,
							data.identifier, data.type);
				else
					declaration = nodeFactory.newFieldDeclarationNode(source,
							data.identifier, data.type, bitFieldWidth);

				result.add(declaration);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param enumerationTree
	 * @return
	 * @throws SyntaxException
	 */
	private EnumerationTypeNode translateEnumerationType(
			CommonTree enumerationTree, SimpleScope scope)
			throws SyntaxException {
		// tagTree may be ABSENT:
		CommonTree tagTree = (CommonTree) enumerationTree.getChild(0);
		// enumeratorListTree may be ABSENT:
		CommonTree enumeratorListTree = (CommonTree) enumerationTree
				.getChild(1);
		IdentifierNode tag;
		SequenceNode<EnumeratorDeclarationNode> enumerators;

		if (tagTree.getType() == ABSENT)
			tag = null;
		else
			tag = translateIdentifier(tagTree);
		if (enumeratorListTree.getType() == ABSENT) {
			enumerators = null;
		} else {
			int numEnumerators = enumeratorListTree.getChildCount();
			List<EnumeratorDeclarationNode> enumeratorList = new LinkedList<EnumeratorDeclarationNode>();

			for (int i = 0; i < numEnumerators; i++) {
				CommonTree enumeratorTree = (CommonTree) enumeratorListTree
						.getChild(i);
				CommonTree enumeratorNameTree = (CommonTree) enumeratorTree
						.getChild(0);
				IdentifierNode enumeratorName = translateIdentifier(enumeratorNameTree);
				CommonTree constantTree = (CommonTree) enumeratorTree
						.getChild(1);
				ExpressionNode constant = translateExpression(constantTree,
						scope);
				EnumeratorDeclarationNode decl = nodeFactory
						.newEnumeratorDeclarationNode(
								newSource(enumeratorTree), enumeratorName,
								constant);

				enumeratorList.add(decl);
			}
			enumerators = nodeFactory.newSequenceNode(
					newSource(enumeratorListTree), "EnumeratorList",
					enumeratorList);
		}
		return nodeFactory.newEnumerationTypeNode(newSource(enumerationTree),
				tag, enumerators);
	}

	/**
	 * 
	 * @param atomicTree
	 * @return
	 * @throws SyntaxException
	 */
	private AtomicTypeNode translateAtomicType(CommonTree atomicTree,
			SimpleScope scope) throws SyntaxException {
		TypeNode type = translateTypeName((CommonTree) atomicTree.getChild(0),
				scope);

		return nodeFactory.newAtomicTypeNode(newSource(atomicTree), type);
	}

	/**
	 * 
	 * @param declaration
	 * @param analysis
	 */
	private void setFunctionSpecifiers(FunctionDeclarationNode declaration,
			SpecifierAnalysis analysis) {
		if (analysis.inlineSpecifier)
			declaration.setInlineFunctionSpecifier(true);
		if (analysis.noreturnSpecifier)
			declaration.setNoreturnFunctionSpecifier(true);
	}

	private void checkFunctionSpecifiers(VariableDeclarationNode declaration,
			SpecifierAnalysis analysis) throws SyntaxException {
		if (analysis.inlineSpecifier)
			throw error("Use of inline specifier in object delcaration",
					declaration);
		if (analysis.noreturnSpecifier)
			throw error("Use of _Noreturn specifier in object delcaration",
					declaration);
	}

	/**
	 * 
	 * @param declaration
	 * @param analysis
	 * @throws SyntaxException
	 */
	private void setAlignmentSpecifiers(VariableDeclarationNode declaration,
			SpecifierAnalysis analysis, SimpleScope scope)
			throws SyntaxException {
		if (!analysis.alignmentTypeNodes.isEmpty()) {
			List<TypeNode> typeAlignmentSpecifiers = new ArrayList<TypeNode>();

			for (CommonTree node : analysis.alignmentTypeNodes)
				typeAlignmentSpecifiers.add(translateTypeName(node, scope));
			declaration.setTypeAlignmentSpecifiers(nodeFactory.newSequenceNode(
					newSource(analysis.specifierListNode),
					"TypeAlignmentSpecifiers", typeAlignmentSpecifiers));
		}
		if (!analysis.alignmentExpressionNodes.isEmpty()) {
			List<ExpressionNode> constantAlignmentSpecifiers = new ArrayList<ExpressionNode>();

			for (CommonTree node : analysis.alignmentExpressionNodes)
				constantAlignmentSpecifiers
						.add(translateExpression(node, scope));

			declaration.setConstantAlignmentSpecifiers(nodeFactory
					.newSequenceNode(newSource(analysis.specifierListNode),
							"ConstantAlignmentSpecifiers",
							constantAlignmentSpecifiers));
		}
	}

	private void checkAlignmentSpecifiers(FunctionDeclarationNode declaration,
			SpecifierAnalysis analysis) throws SyntaxException {
		if (!analysis.alignmentTypeNodes.isEmpty()
				|| !analysis.alignmentExpressionNodes.isEmpty())
			throw error("Use of alignment specifiers in function declaration",
					declaration);
	}

	/**
	 * 
	 * @param declaration
	 * @param analysis
	 */
	private void setStorageSpecifiers(VariableDeclarationNode declaration,
			SpecifierAnalysis analysis, SimpleScope scope) {

		if (analysis.externCount > 0)
			declaration.setExternStorage(true);
		if (analysis.staticCount > 0)
			declaration.setStaticStorage(true);
		if (analysis.threadLocalCount > 0)
			declaration.setThreadLocalStorage(true);
		if (analysis.autoCount > 0)
			declaration.setAutoStorage(true);
		if (analysis.registerCount > 0)
			declaration.setRegisterStorage(true);
	}

	private void setStorageSpecifiers(FunctionDeclarationNode declaration,
			SpecifierAnalysis analysis, SimpleScope scope)
			throws SyntaxException {

		if (analysis.externCount > 0)
			declaration.setExternStorage(true);
		if (analysis.staticCount > 0)
			declaration.setStaticStorage(true);
		if (analysis.threadLocalCount > 0)
			throw new SyntaxException(
					"Use of _Thread_local in function declaration",
					declaration.getSource());
		if (analysis.autoCount > 0)
			throw new SyntaxException("Use of auto in function declaration",
					declaration.getSource());
		if (analysis.registerCount > 0)
			throw new SyntaxException(
					"Use of register in function declaration",
					declaration.getSource());
	}

	/**
	 * Creates new DeclaratorData based on given declarator tree node and base
	 * type. The declarator may be abstract. The data gives the new type formed
	 * by applying the type derivation operations of the declarator to the base
	 * type. The data also gives the identifier being declared, though this may
	 * be null in the case of an abstract declarator.
	 * 
	 * @param declarator
	 *            CommonTree node of type DECLARATOR, ABSTRACT_DECLARATOR, or
	 *            ABSENT
	 * @param type
	 *            the start type before applying declarator operations
	 * 
	 * @return new DeclaratorData with type derived from given type and
	 *         identifier
	 * 
	 * @throws SyntaxException
	 */
	private DeclaratorData processDeclarator(CommonTree declarator,
			TypeNode type, SimpleScope scope) throws SyntaxException {
		if (declarator.getType() == ABSENT) {
			return new DeclaratorData(type, null);
		} else {
			CommonTree pointerTree = (CommonTree) declarator.getChild(0);
			CommonTree directDeclarator = (CommonTree) declarator.getChild(1);
			type = translatePointers(pointerTree, type, scope);

			return processDirectDeclarator(directDeclarator, type, scope);
		}
	}

	/**
	 * Creates a new DeclaratorData based on given direct declarator tree node
	 * and base type. The direct declarator may be abstract.
	 * 
	 * @param directDeclarator
	 *            CommonTree node of type DIRECT_DECLARATOR,
	 *            DIRECT_ABSTRACT_DECLARATOR, or ABSENT
	 * @param type
	 *            base type
	 * @return new DeclaratorData with derived type and identifier
	 * @throws SyntaxException
	 */
	private DeclaratorData processDirectDeclarator(CommonTree directDeclarator,
			TypeNode type, SimpleScope scope) throws SyntaxException {
		if (directDeclarator.getType() == ABSENT) {
			return new DeclaratorData(type, null);
		} else {
			int numChildren = directDeclarator.getChildCount();
			CommonTree prefix = (CommonTree) directDeclarator.getChild(0);

			// need to peel off right-most suffix first. Example:
			// T prefix [](); : (array of function returning T) prefix;
			for (int i = numChildren - 1; i >= 1; i--)
				type = translateDeclaratorSuffix(
						(CommonTree) directDeclarator.getChild(i), type, scope);
			switch (prefix.getType()) {
			case IDENTIFIER:
				return new DeclaratorData(type, translateIdentifier(prefix));
			case DECLARATOR:
			case ABSTRACT_DECLARATOR:
				return processDeclarator(prefix, type, scope);
			case ABSENT:
				return new DeclaratorData(type, null);
			default:
				throw error("Unexpected node for direct declarator prefix",
						prefix);
			}
		}
	}

	/**
	 * 
	 * @param initializerTree
	 * @return
	 * @throws SyntaxException
	 */
	private InitializerNode translateInitializer(CommonTree initializerTree,
			SimpleScope scope) throws SyntaxException {
		int kind = initializerTree.getType();

		if (kind == ABSENT)
			return null;
		if (kind == SCALAR_INITIALIZER) {
			return translateExpression(
					(CommonTree) initializerTree.getChild(0), scope);
		}
		if (kind == INITIALIZER_LIST) {
			int numInits = initializerTree.getChildCount();
			List<PairNode<DesignationNode, InitializerNode>> initList = new LinkedList<PairNode<DesignationNode, InitializerNode>>();

			for (int i = 0; i < numInits; i++) {
				CommonTree designatedInitializer = (CommonTree) initializerTree
						.getChild(i);
				CommonTree designation = (CommonTree) designatedInitializer
						.getChild(0);
				CommonTree initializer = (CommonTree) designatedInitializer
						.getChild(1);
				InitializerNode initializerNode = translateInitializer(
						initializer, scope);
				DesignationNode designationNode;

				if (designation.getType() == ABSENT) {
					designationNode = null;
				} else {
					int numDesignators = designation.getChildCount();
					List<DesignatorNode> designators = new LinkedList<DesignatorNode>();

					for (int j = 0; j < numDesignators; j++) {
						CommonTree designator = (CommonTree) designation
								.getChild(j);
						CommonTree designatorChild = (CommonTree) designator
								.getChild(0);
						int designatorKind = designator.getType();
						DesignatorNode designatorNode;
						Source designatorSource = newSource(designator);

						if (designatorKind == ARRAY_ELEMENT_DESIGNATOR) {
							designatorNode = nodeFactory
									.newArrayDesignatorNode(
											designatorSource,
											translateExpression(
													designatorChild, scope));
						} else if (designatorKind == FIELD_DESIGNATOR) {
							designatorNode = nodeFactory
									.newFieldDesignatorNode(
											designatorSource,
											translateIdentifier(designatorChild));
						} else {
							throw error("Unknown kind of designator",
									designator);
						}
						designators.add(designatorNode);
					}
					designationNode = nodeFactory.newDesignationNode(
							newSource(designation), designators);
				}
				initList.add(nodeFactory.newPairNode(
						newSource(designatedInitializer), designationNode,
						initializerNode));
			}
			return nodeFactory.newCompoundInitializerNode(
					newSource(initializerTree), initList);
		} else {
			throw error("Unrecognized kind of initializer", initializerTree);
		}
	}

	/**
	 * Returns the new type obtained by taking the given type and applying the
	 * pointer operations to it. For example, if the old type is "int" and the
	 * pointerTree is "*", the result is the type "pointer to int".
	 * 
	 * @param pointerTree
	 *            CommonTree node of type POINTER or ABSENT
	 * @param type
	 *            base type
	 * @return modified type
	 * @throws SyntaxException
	 *             if an unknown kind of type qualifier appears
	 */
	private TypeNode translatePointers(CommonTree pointerTree, TypeNode type,
			SimpleScope scope) throws SyntaxException {
		int numChildren = pointerTree.getChildCount();
		Source source = type.getSource();

		for (int i = 0; i < numChildren; i++) {
			CommonTree starNode = (CommonTree) pointerTree.getChild(i);
			CommonTree qualifiers = (CommonTree) starNode.getChild(0);

			source = sourceFactory.join(source, newSource(starNode));
			type = nodeFactory.newPointerTypeNode(source, type);
			applyQualifiers(qualifiers, type);
		}
		return type;
	}

	/**
	 * Given a base type and a declarator suffix, returns the new derived type.
	 * Example: base type is "int", suffix is "[10]", returns the type
	 * "array of int of length 10".
	 * 
	 * @param suffix
	 *            a CommonTree node of type ARRAY_SUFFIX or FUNCTION_SUFFIX
	 * @param type
	 * @return new type
	 * @throws SyntaxException
	 *             if the kind of suffix is not function or array
	 */
	private TypeNode translateDeclaratorSuffix(CommonTree suffix,
			TypeNode baseType, SimpleScope scope) throws SyntaxException {
		int kind = suffix.getType();

		if (kind == ARRAY_SUFFIX)
			return translateArraySuffix(suffix, baseType, scope);
		else if (kind == FUNCTION_SUFFIX)
			return translateFunctionSuffix(suffix, baseType, scope);
		else
			throw error("Unknown declarator suffix", suffix);
	}

	/**
	 * Applies the qualifires in the given qualifier list to the given type.
	 * Modifes the type accordingly.
	 * 
	 * @param qualifierList
	 *            CommonTree node which is root of list of qualifier nodes, or
	 *            ABSENT
	 * @param type
	 *            the type to modify by applying qualifiers
	 * @throws SyntaxException
	 *             if a childe of the qualifierList is not a type qualifier
	 */
	private void applyQualifiers(CommonTree qualifierList, TypeNode type)
			throws SyntaxException {
		int numQualifiers = qualifierList.getChildCount();

		for (int i = 0; i < numQualifiers; i++) {
			CommonTree qualifier = (CommonTree) qualifierList.getChild(i);

			switch (qualifier.getType()) {
			case CONST:
				type.setConstQualified(true);
				break;
			case VOLATILE:
				type.setVolatileQualified(true);
				break;
			case RESTRICT:
				type.setRestrictQualified(true);
				break;
			case ATOMIC:
				type.setAtomicQualified(true);
				break;
			default:
				throw error("Unknown type qualifier", qualifier);
			}
		}
	}

	private void applyArrayQualifiers(CommonTree qualifierList,
			ArrayTypeNode type) throws SyntaxException {
		int numQualifiers = qualifierList.getChildCount();

		for (int i = 0; i < numQualifiers; i++) {
			CommonTree qualifier = (CommonTree) qualifierList.getChild(i);

			switch (qualifier.getType()) {
			case CONST:
				type.setConstInBrackets(true);
				break;
			case VOLATILE:
				type.setVolatileInBrackets(true);
				break;
			case RESTRICT:
				type.setRestrictInBrackets(true);
				break;
			case ATOMIC:
				type.setAtomicInBrackets(true);
				break;
			default:
				throw error("Unknown type qualifier", qualifier);
			}
		}
	}

	/**
	 * 
	 * @param suffix
	 * @param baseType
	 * @return
	 * @throws SyntaxException
	 */
	private ArrayTypeNode translateArraySuffix(CommonTree suffix,
			TypeNode baseType, SimpleScope scope) throws SyntaxException {
		CommonTree staticNode = (CommonTree) suffix.getChild(1);
		CommonTree qualifiers = (CommonTree) suffix.getChild(2);
		CommonTree extentNode = (CommonTree) suffix.getChild(3);
		int extentNodeType = extentNode.getType();
		boolean unspecifiedVariableLength = false;
		ExpressionNode extent = null;
		ArrayTypeNode result;
		Source source = sourceFactory.join(baseType.getSource(),
				newSource(suffix));

		switch (extentNodeType) {
		case ABSENT:
			break;
		case STAR:
			unspecifiedVariableLength = true;
			break;
		default:
			extent = translateExpression(extentNode, scope);
		}
		result = nodeFactory.newArrayTypeNode(source, baseType, extent);
		if (unspecifiedVariableLength)
			result.setUnspecifiedVariableLength(true);
		if (staticNode.getType() == STATIC)
			result.setStaticExtent(true);
		applyArrayQualifiers(qualifiers, result);
		return result;
	}

	/**
	 * 
	 * @param suffix
	 * @param baseType
	 * @return
	 * @throws SyntaxException
	 */
	private FunctionTypeNode translateFunctionSuffix(CommonTree suffix,
			TypeNode baseType, SimpleScope scope) throws SyntaxException {
		CommonTree child = (CommonTree) suffix.getChild(1);
		int childKind = child.getType();
		FunctionTypeNode result;
		Source source = sourceFactory.join(baseType.getSource(),
				newSource(suffix));

		if (!scope.isFunctionScope()) {
			// this is not a function definition.
			// need a "function prototype" scope...
			scope = new SimpleScope(scope);
		}
		if (childKind == PARAMETER_TYPE_LIST) {
			CommonTree parameterListNode = (CommonTree) child.getChild(0);
			CommonTree ellipsisNode = (CommonTree) child.getChild(1);
			int numParameters = parameterListNode.getChildCount();
			List<VariableDeclarationNode> parameterDeclarations = new ArrayList<VariableDeclarationNode>(
					numParameters);

			for (int i = 0; i < numParameters; i++) {
				CommonTree parameterDeclarationNode = (CommonTree) parameterListNode
						.getChild(i);
				Source parameterDeclarationSource = newSource(parameterDeclarationNode);
				CommonTree specifiers = (CommonTree) parameterDeclarationNode
						.getChild(0);
				SpecifierAnalysis analysis = newSpecifierAnalysis(specifiers);
				TypeNode parameterBaseType = newSpecifierType(analysis, scope);
				CommonTree declarator = (CommonTree) parameterDeclarationNode
						.getChild(1);
				int declaratorKind = declarator.getType();
				VariableDeclarationNode declaration;
				// TODO: do adjustments here?

				if (declaratorKind == ABSENT) {
					declaration = nodeFactory
							.newVariableDeclarationNode(
									parameterDeclarationSource, null,
									parameterBaseType);
				} else if (declaratorKind == DECLARATOR
						|| declaratorKind == ABSTRACT_DECLARATOR) {
					DeclaratorData data = processDeclarator(declarator,
							parameterBaseType, scope);

					declaration = nodeFactory.newVariableDeclarationNode(
							parameterDeclarationSource, data.identifier,
							data.type);
				} else {
					throw error("Unknown kind of declarator", declarator);
				}
				// TODO: C11 6.7.6.3(2):
				// "The only storage-class specifier that shall occur in a
				// parameter declaration is register."
				// setFunctionSpecifiers(declaration, analysis);
				setAlignmentSpecifiers(declaration, analysis, scope);
				setStorageSpecifiers(declaration, analysis, scope);
				parameterDeclarations.add(declaration);
			}
			result = nodeFactory.newFunctionTypeNode(source, baseType,
					nodeFactory.newSequenceNode(newSource(parameterListNode),
							"FormalParameterList", parameterDeclarations),
					false);
			if (ellipsisNode.getType() == ELLIPSIS)
				result.setVariableArgs(true);
		} else if (childKind == IDENTIFIER_LIST || childKind == ABSENT) {
			int numParameters = child.getChildCount();
			List<VariableDeclarationNode> parameterDeclarations = new ArrayList<VariableDeclarationNode>(
					numParameters);

			for (int i = 0; i < numParameters; i++) {
				CommonTree identifierNode = (CommonTree) child.getChild(i);
				IdentifierNode identifier = translateIdentifier(identifierNode);
				Source identifierSource = newSource(identifierNode);
				VariableDeclarationNode declaration = nodeFactory
						.newVariableDeclarationNode(identifierSource,
								identifier, null);

				parameterDeclarations.add(declaration);
			}
			result = nodeFactory.newFunctionTypeNode(source, baseType,
					nodeFactory.newSequenceNode(source, "FormalParameterList",
							parameterDeclarations), true);
		} else {
			throw error("Unexpected kind of function suffix", child);
		}
		return result;
	}

	/**
	 * 
	 * @param typeNameTree
	 * @return
	 * @throws SyntaxException
	 */
	private TypeNode translateTypeName(CommonTree typeNameTree,
			SimpleScope scope) throws SyntaxException {
		CommonTree specifiers = (CommonTree) typeNameTree.getChild(0);
		CommonTree abstractDeclarator = (CommonTree) typeNameTree.getChild(1);
		SpecifierAnalysis analysis = newSpecifierAnalysis(specifiers);
		TypeNode baseType = newSpecifierType(analysis, scope);
		DeclaratorData data = processDeclarator(abstractDeclarator, baseType,
				scope);

		return data.type;
	}

	private AssumeNode translateAssume(Source source, CommonTree assumeTree,
			SimpleScope scope) throws SyntaxException {
		return nodeFactory
				.newAssumeNode(
						source,
						translateExpression(
								(CommonTree) assumeTree.getChild(0), scope));
	}

	/**
	 * 
	 * @param statementTree
	 * @return
	 * @throws SyntaxException
	 */
	private StatementNode translateStatement(CommonTree statementTree,
			SimpleScope scope) throws SyntaxException {
		int kind = statementTree.getType();

		if (kind == ABSENT)
			return null;

		Source statementSource = newSource(statementTree);

		switch (kind) {
		case IDENTIFIER_LABELED_STATEMENT: {
			IdentifierNode labelName = translateIdentifier((CommonTree) statementTree
					.getChild(0));
			StatementNode statement = translateStatement(
					(CommonTree) statementTree.getChild(1), scope);
			OrdinaryLabelNode labelDecl = nodeFactory
					.newStandardLabelDeclarationNode(labelName.getSource(),
							labelName, statement);

			return nodeFactory.newLabeledStatementNode(statementSource,
					labelDecl, statement);
		}
		case CASE_LABELED_STATEMENT: {
			CToken caseToken = (CToken) ((CommonTree) statementTree.getChild(0))
					.getToken();
			CommonTree expression = (CommonTree) statementTree.getChild(1);
			ExpressionNode expressionNode = translateExpression(expression,
					scope);
			StatementNode statement = translateStatement(
					(CommonTree) statementTree.getChild(2), scope);
			Source expressionSource = newSource(expression);
			Source labelSource = sourceFactory
					.join(expressionSource, caseToken);
			SwitchLabelNode labelDecl = nodeFactory
					.newCaseLabelDeclarationNode(labelSource, expressionNode,
							statement);

			return nodeFactory.newLabeledStatementNode(statementSource,
					labelDecl, statement);
		}
		case DEFAULT_LABELED_STATEMENT: {
			CToken defaultToken = (CToken) ((CommonTree) statementTree
					.getChild(0)).getToken();
			Source labelSource = sourceFactory.newSource(defaultToken);
			StatementNode statement = translateStatement(
					(CommonTree) statementTree.getChild(1), scope);
			SwitchLabelNode labelDecl = nodeFactory
					.newDefaultLabelDeclarationNode(labelSource, statement);

			return nodeFactory.newLabeledStatementNode(statementSource,
					labelDecl, statement);
		}
		case COMPOUND_STATEMENT:
			return translateCompoundStatement(statementTree, scope);
		case EXPRESSION_STATEMENT: {
			CommonTree expression = (CommonTree) statementTree.getChild(0);
			ExpressionNode expressionNode = translateExpression(expression,
					scope);

			if (expressionNode == null)
				return nodeFactory.newNullStatementNode(statementSource);
			else
				return nodeFactory.newExpressionStatementNode(expressionNode);
		}
		case IF: {
			SimpleScope ifScope = new SimpleScope(scope);
			ExpressionNode condition = translateExpression(
					(CommonTree) statementTree.getChild(0), ifScope);
			StatementNode trueBranch = translateStatement(
					(CommonTree) statementTree.getChild(1), new SimpleScope(
							ifScope));
			StatementNode falseBranch = translateStatement(
					(CommonTree) statementTree.getChild(2), new SimpleScope(
							ifScope));

			if (falseBranch == null)
				return nodeFactory.newIfNode(statementSource, condition,
						trueBranch);
			else
				return nodeFactory.newIfNode(statementSource, condition,
						trueBranch, falseBranch);
		}
		case SWITCH: {
			CommonTree expressionTree = (CommonTree) statementTree.getChild(0);
			CommonTree bodyTree = (CommonTree) statementTree.getChild(1);
			SimpleScope switchScope = new SimpleScope(scope);
			SimpleScope bodyScope = new SimpleScope(switchScope);
			ExpressionNode expressionNode = translateExpression(expressionTree,
					switchScope);
			StatementNode statementNode = translateStatement(bodyTree,
					bodyScope);
			SwitchNode switchNode = nodeFactory.newSwitchNode(statementSource,
					expressionNode, statementNode);

			return switchNode;
		}
		case WHILE: {
			SimpleScope loopScope = new SimpleScope(scope);

			return nodeFactory.newWhileLoopNode(
					statementSource,
					translateExpression((CommonTree) statementTree.getChild(0),
							loopScope),
					translateStatement((CommonTree) statementTree.getChild(1),
							new SimpleScope(loopScope)),
					getInvariant((CommonTree) statementTree.getChild(2),
							loopScope));
		}
		case DO: {
			SimpleScope loopScope = new SimpleScope(scope);

			return nodeFactory.newDoLoopNode(
					statementSource,
					translateExpression((CommonTree) statementTree.getChild(1),
							loopScope),
					translateStatement((CommonTree) statementTree.getChild(0),
							new SimpleScope(loopScope)),
					getInvariant((CommonTree) statementTree.getChild(2),
							loopScope));
		}
		case FOR: {
			SimpleScope loopScope = new SimpleScope(scope);
			CommonTree initializerTree = (CommonTree) statementTree.getChild(0);
			ForLoopInitializerNode initializerNode;

			if (initializerTree.getType() == DECLARATION) {
				List<ExternalDefinitionNode> definitions = translateDeclaration(
						initializerTree, loopScope);
				List<VariableDeclarationNode> declarations = new LinkedList<VariableDeclarationNode>();

				for (ExternalDefinitionNode definition : definitions) {
					if (!(definition instanceof VariableDeclarationNode))
						throw error(
								"For-loop initializer declaration "
										+ "\"shall only declare identifiers for objects having storage class auto or register.\"",
								initializerTree);
					declarations.add((VariableDeclarationNode) definition);
				}
				initializerNode = nodeFactory.newForLoopInitializerNode(
						statementSource, declarations);
			} else {
				initializerNode = translateExpression(initializerTree,
						loopScope);
			}
			return nodeFactory.newForLoopNode(
					statementSource,
					initializerNode,
					translateExpression((CommonTree) statementTree.getChild(1),
							loopScope),
					translateExpression((CommonTree) statementTree.getChild(2),
							loopScope),
					translateStatement((CommonTree) statementTree.getChild(3),
							new SimpleScope(loopScope)),
					getInvariant((CommonTree) statementTree.getChild(4),
							loopScope));
		}
		case GOTO:
			return nodeFactory
					.newGotoNode(statementSource,
							translateIdentifier((CommonTree) statementTree
									.getChild(0)));
		case CONTINUE:
			return nodeFactory.newContinueNode(statementSource);
		case BREAK:
			return nodeFactory.newBreakNode(statementSource);
		case RETURN:
			return nodeFactory.newReturnNode(
					statementSource,
					translateExpression((CommonTree) statementTree.getChild(0),
							scope));
		case PRAGMA:
			return translatePragma(statementSource, statementTree, scope);
			// case WAIT:
			// return nodeFactory.newWaitNode(
			// statementSource,
			// translateExpression((CommonTree) statementTree.getChild(0),
			// scope));
			// case ASSERT:
			// return nodeFactory.newAssertNode(
			// statementSource,
			// translateExpression((CommonTree) statementTree.getChild(0),
			// scope));

		case ASSUME:
			return translateAssume(statementSource, statementTree, scope);
		case WHEN:
			return nodeFactory.newWhenNode(
					statementSource,
					translateExpression((CommonTree) statementTree.getChild(0),
							scope),
					translateStatement((CommonTree) statementTree.getChild(1),
							scope));
		case CHOOSE:
			return translateChooseStatement(statementTree, scope);
		case CIVLATOMIC:
			StatementNode body = translateStatement(
					(CommonTree) statementTree.getChild(0), new SimpleScope(
							scope));

			return nodeFactory.newAtomicStatementNode(statementSource, false,
					body);
		case CIVLATOM:
			StatementNode datomicBody = translateStatement(
					(CommonTree) statementTree.getChild(0), new SimpleScope(
							scope));

			return nodeFactory.newAtomicStatementNode(statementSource, true,
					datomicBody);
		default:
			throw error("Unknown statement type", statementTree);
		}
	}

	private ExpressionNode getInvariant(CommonTree invariantTree,
			SimpleScope scope) throws SyntaxException {
		if (invariantTree == null)
			return null;
		if (invariantTree.getType() == ABSENT)
			return null;
		else {
			CommonTree exprTree = (CommonTree) invariantTree.getChild(0);

			return translateExpression(exprTree, scope);
		}
	}

	private PragmaNode translatePragma(Source source, CommonTree pragmaTree,
			SimpleScope scope) throws SyntaxException {
		CommonTree identifierTree = (CommonTree) pragmaTree.getChild(0);
		IdentifierNode identifier = translateIdentifier(identifierTree);
		CommonTree bodyTree = (CommonTree) pragmaTree.getChild(1);
		CommonTree newlineTree = (CommonTree) pragmaTree.getChild(2);
		int numTokens = bodyTree.getChildCount();
		List<CToken> body = new LinkedList<>();
		CToken newlineToken = (CToken) newlineTree.getToken();

		for (int i = 0; i < numTokens; i++) {
			CToken token = (CToken) ((CommonTree) bodyTree.getChild(i))
					.getToken();

			body.add(token);
		}
		return nodeFactory
				.newPragmaNode(source, identifier, body, newlineToken);
	}

	/**
	 * 
	 * @param compoundStatementTree
	 * @return
	 * @throws SyntaxException
	 */
	private CompoundStatementNode translateCompoundStatement(
			CommonTree compoundStatementTree, SimpleScope scope)
			throws SyntaxException {
		SimpleScope newScope = new SimpleScope(scope);
		Source source = newSource(compoundStatementTree);
		CommonTree blockItems = (CommonTree) compoundStatementTree.getChild(1);
		int numChildren = blockItems.getChildCount();
		List<BlockItemNode> items = new LinkedList<BlockItemNode>();
		OmpStatementNode ompStatementNode = null;

		for (int i = 0; i < numChildren; i++) {
			CommonTree childTree = (CommonTree) blockItems.getChild(i);
			int kind = childTree.getType();

			if (kind == DECLARATION) {
				for (ExternalDefinitionNode declaration : translateDeclaration(
						childTree, newScope))
					items.add((BlockItemNode) declaration);
			} else if (kind == SCOPE) {
				items.add(translateScopeDeclaration(childTree, newScope));
			} else if (kind == STATICASSERT) {
				items.add(translateStaticAssertion(childTree, newScope));
			} else if (kind == FUNCTION_DEFINITION) {
				items.add((BlockItemNode) translateFunctionDefinition(
						childTree, newScope));
			} else {
				StatementNode statementNode = translateStatement(childTree,
						newScope);

				if (ompStatementNode != null) {
					ompStatementNode.setStatementNode(statementNode);
					ompStatementNode = null;
				} else {
					items.add(statementNode);
					if (statementNode.statementKind() == StatementKind.OMP_STATEMENT) {
						ompStatementNode = (OmpStatementNode) statementNode;
						if (ompStatementNode.isComplete())
							ompStatementNode = null;
					}
				}
			}
		}
		return nodeFactory.newCompoundStatementNode(source, items);
	}

	@Override
	public List<BlockItemNode> translateBlockItemNode(
			CommonTree blockItemTree, SimpleScope scope) throws SyntaxException {
		int kind = blockItemTree.getType();
		List<BlockItemNode> items = new LinkedList<BlockItemNode>();

		if (kind == DECLARATION) {
			for (ExternalDefinitionNode declaration : translateDeclaration(
					blockItemTree, scope))
				items.add((BlockItemNode) declaration);
		} else if (kind == SCOPE) {
			items.add(translateScopeDeclaration(blockItemTree, scope));
		} else if (kind == STATICASSERT) {
			items.add(translateStaticAssertion(blockItemTree, scope));
		} else if (kind == FUNCTION_DEFINITION) {
			items.add((BlockItemNode) translateFunctionDefinition(
					blockItemTree, scope));
		} else {
			StatementNode statementNode = translateStatement(blockItemTree,
					scope);

			items.add(statementNode);
		}
		return items;
	}

	private ChooseStatementNode translateChooseStatement(
			CommonTree chooseStatementTree, SimpleScope scope)
			throws SyntaxException {
		int numChildren = chooseStatementTree.getChildCount();
		List<StatementNode> statements = new LinkedList<StatementNode>();

		for (int i = 0; i < numChildren; i++) {
			CommonTree statementTree = (CommonTree) chooseStatementTree
					.getChild(i);
			StatementNode statement = translateStatement(statementTree, scope);

			statements.add(statement);
		}
		return nodeFactory.newChooseStatementNode(
				newSource(chooseStatementTree), statements);
	}

	/**
	 * 
	 * @param staticAssertTree
	 * @return
	 * @throws SyntaxException
	 */
	private StaticAssertionNode translateStaticAssertion(
			CommonTree staticAssertTree, SimpleScope scope)
			throws SyntaxException {
		CommonTree stringLiteral = (CommonTree) staticAssertTree.getChild(1);
		Source stringLiteralSource = newSource(stringLiteral);

		return nodeFactory.newStaticAssertionNode(
				newSource(staticAssertTree),
				translateExpression((CommonTree) staticAssertTree.getChild(0),
						scope),
				translateStringLiteral(stringLiteralSource,
						(CommonTree) staticAssertTree.getChild(1)));
	}

	/**
	 * 
	 * @param functionDefinitionTree
	 * @return
	 * @throws SyntaxException
	 */
	private ExternalDefinitionNode translateFunctionDefinition(
			CommonTree functionDefinitionTree, SimpleScope scope)
			throws SyntaxException {
		// two different ways of declaring parameters:
		// (1) parameter-type list and no declarations
		// (2) identifier list and declarations
		SimpleScope newScope = new SimpleScope(scope, true);
		CommonTree specifiers = (CommonTree) functionDefinitionTree.getChild(0);
		CommonTree declarator = (CommonTree) functionDefinitionTree.getChild(1);
		CommonTree declarationList = (CommonTree) functionDefinitionTree
				.getChild(2);
		CommonTree compoundStatementTree = (CommonTree) functionDefinitionTree
				.getChild(3);
		CommonTree contractTree = (CommonTree) functionDefinitionTree
				.getChild(4);
		CommonTree scopeListTree = (CommonTree) functionDefinitionTree
				.getChild(5);
		SequenceNode<VariableDeclarationNode> scopeListNode = translateScopeListDef(scopeListTree);
		SpecifierAnalysis analysis = newSpecifierAnalysis(specifiers);
		TypeNode baseType = newSpecifierType(analysis, newScope);
		DeclaratorData data = processDeclarator(declarator, baseType, newScope);
		FunctionTypeNode functionType = (FunctionTypeNode) data.type;
		CompoundStatementNode body;
		ExternalDefinitionNode result;

		if (functionType.hasIdentifierList()) {
			SequenceNode<VariableDeclarationNode> formalSequenceNode = functionType
					.getParameters();
			int numFormals = formalSequenceNode.numChildren();
			int numDeclarations = declarationList.getChildCount();

			if (numFormals == 0) {
				if (numDeclarations != 0)
					throw error(
							"Function with empty identifier list has parameter declarations",
							declarationList);
			} else {
				SequenceNode<VariableDeclarationNode> newFormalSequenceNode;
				List<VariableDeclarationNode> newFormalList = new LinkedList<VariableDeclarationNode>();
				Map<String, VariableDeclarationNode> declMap = new HashMap<String, VariableDeclarationNode>();

				for (int i = 0; i < numDeclarations; i++) {
					CommonTree declarationTree = (CommonTree) declarationList
							.getChild(i);
					List<ExternalDefinitionNode> declNodes = translateDeclaration(
							declarationTree, newScope);

					for (ExternalDefinitionNode definition : declNodes) {
						String parameterName;
						VariableDeclarationNode oldDeclaration;

						if (!(definition instanceof VariableDeclarationNode))
							throw error("Illegal parameter declaration",
									declarationTree);
						parameterName = ((VariableDeclarationNode) definition)
								.getIdentifier().name();
						if (parameterName == null)
							throw error("Illegal parameter declaration",
									declarationTree);
						oldDeclaration = declMap.get(parameterName);
						if (oldDeclaration != null)
							throw error(
									"Re-declaration of parameter. Old declaration was at "
											+ oldDeclaration, declarationTree);
						declMap.put(parameterName,
								(VariableDeclarationNode) definition);
					}
				}
				for (VariableDeclarationNode formal : formalSequenceNode) {
					String parameterName = formal.getIdentifier().name();
					VariableDeclarationNode newDeclaration;

					if (parameterName == null)
						throw error(
								"Formal parameter declaration missing name: "
										+ formal, declarator);
					newDeclaration = declMap.get(parameterName);
					if (newDeclaration == null)
						throw error("Missing declaration for parameter "
								+ parameterName, declarationList);
					newFormalList.add(newDeclaration);
					declMap.remove(parameterName);
				}
				if (!declMap.isEmpty())
					throw error(
							"Function contains declarations for variables that are not parameters",
							declarationList);
				newFormalSequenceNode = nodeFactory.newSequenceNode(
						newSource(declarationList),
						"FormalParameterDeclarations", newFormalList);
				functionType.setParameters(newFormalSequenceNode);
			}
		}
		body = translateCompoundStatement(compoundStatementTree, newScope);
		result = nodeFactory.newFunctionDefinitionNode(
				newSource(functionDefinitionTree), data.identifier,
				(FunctionTypeNode) data.type,
				getContract(contractTree, newScope), body);
		if (scopeListNode != null)
			result = nodeFactory.newScopeParameterizedDeclarationNode(
					sourceFactory.join(scopeListNode.getSource(),
							result.getSource()), scopeListNode,
					(DeclarationNode) result);
		return result;
	}

	private SequenceNode<ContractNode> getContract(CommonTree contractTree,
			SimpleScope scope) throws SyntaxException {
		SequenceNode<ContractNode> contract;

		if (contractTree == null)
			contract = null;
		else {
			int kind = contractTree.getType();

			if (kind == ABSENT)
				contract = null;
			else {
				int numItems = contractTree.getChildCount();
				List<ContractNode> items = new LinkedList<ContractNode>();

				if (numItems == 0) {
					contract = null;
				} else {
					for (int i = 0; i < numItems; i++) {
						CommonTree itemTree = (CommonTree) contractTree
								.getChild(i);
						int itemKind = itemTree.getType();
						CommonTree exprTree = (CommonTree) itemTree.getChild(0);
						ExpressionNode expr = translateExpression(exprTree,
								scope);
						ContractNode contractNode;
						Source source = newSource(itemTree);

						if (itemKind == ENSURES) {
							contractNode = nodeFactory.newEnsuresNode(source,
									expr);
						} else if (itemKind == REQUIRES) {
							contractNode = nodeFactory.newRequiresNode(source,
									expr);
						} else {
							throw error("Unknown kind of contract item: "
									+ itemTree, itemTree);
						}
						items.add(contractNode);
					}
					contract = nodeFactory.newSequenceNode(
							newSource(contractTree), "Contract", items);
				}
			}
		}
		return contract;
	}

	/**
	 * 
	 * @param translationUnit
	 * @return
	 * @throws SyntaxException
	 */
	private ASTNode translateTranslationUnit(CommonTree translationUnit)
			throws SyntaxException {
		int numChildren = translationUnit.getChildCount();
		ArrayList<ExternalDefinitionNode> definitions = new ArrayList<ExternalDefinitionNode>();
		SimpleScope scope = new SimpleScope(null);

		if (numChildren == 0) {
			throw error("Translation unit contains no definitions",
					translationUnit);
		}
		for (int i = 0; i < numChildren; i++) {
			CommonTree definitionTree = (CommonTree) translationUnit
					.getChild(i);
			int definitionType = definitionTree.getType();

			if (definitionType == DECLARATION)
				definitions.addAll(translateDeclaration(definitionTree, scope));
			else if (definitionType == SCOPE)
				definitions
						.add(translateScopeDeclaration(definitionTree, scope));
			else if (definitionType == FUNCTION_DEFINITION)
				definitions.add(translateFunctionDefinition(definitionTree,
						scope));
			else if (definitionType == STATICASSERT)
				definitions
						.add(translateStaticAssertion(definitionTree, scope));
			else if (definitionType == PRAGMA)
				definitions.add(translatePragma(newSource(definitionTree),
						definitionTree, scope));
			else if (definitionType == ASSUME)
				definitions.add(translateAssume(newSource(definitionTree),
						definitionTree, scope));
			else
				throw error("Unknown type of external definition",
						definitionTree);
		}
		return nodeFactory.newTranslationUnitNode(newSource(translationUnit),
				definitions);
	}
}

/**
 * 
 * @author siegel
 * 
 */
class DeclaratorData {
	TypeNode type;
	IdentifierNode identifier;

	DeclaratorData(TypeNode type, IdentifierNode identifier) {
		this.type = type;
		this.identifier = identifier;
	}
}
