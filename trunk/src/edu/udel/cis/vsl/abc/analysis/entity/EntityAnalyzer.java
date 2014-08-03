package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.Variable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.EnumerationConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.UnsourcedException;

/**
 * Performs standard analysis of a translation unit, creating the following
 * information which is attached to the AST:
 * 
 * <ul>
 * <li>entities: an entity is any thing that can be represented by an
 * identifier. An IdentifierNode has a method to get and set the Entity
 * associated to the identifier. This Analyzer creates the Entity object and
 * sets it in each identifier.</li>
 * <li>types: every TypeNode and ExpressionNode will have an associated Type
 * object associated to it</li>
 * <li>linkage: each entity has a kind of linkage which is determined and set</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public class EntityAnalyzer implements Analyzer {

	// Exported Fields...

	DeclarationAnalyzer declarationAnalyzer;

	ExpressionAnalyzer expressionAnalyzer;

	CompoundLiteralAnalyzer compoundLiteralAnalyzer;

	StatementAnalyzer statementAnalyzer;

	TypeAnalyzer typeAnalyzer;

	EntityFactory entityFactory;

	TypeFactory typeFactory;

	NodeFactory nodeFactory;

	ASTFactory astFactory;

	ValueFactory valueFactory;

	StandardTypes standardTypes;

	Map<String, PragmaHandler> pragmaHandlerMap;

	Configuration configuration;

	// Private fields...

	private TokenFactory sourceFactory;

	// Constructors...

	public EntityAnalyzer(Configuration configuration, ASTFactory astFactory,
			EntityFactory entityFactory, ConversionFactory conversionFactory) {
		this.configuration = configuration;
		this.astFactory = astFactory;
		this.nodeFactory = astFactory.getNodeFactory();
		this.typeFactory = conversionFactory.getTypeFactory();
		this.valueFactory = nodeFactory.getValueFactory();
		this.sourceFactory = astFactory.getTokenFactory();
		this.entityFactory = entityFactory;
		this.standardTypes = new StandardTypes(entityFactory, typeFactory);
		this.declarationAnalyzer = new DeclarationAnalyzer(this);
		declarationAnalyzer.setIgnoredTypes(standardTypes
				.getStandardTypeNames());
		this.expressionAnalyzer = new ExpressionAnalyzer(this,
				conversionFactory, typeFactory);
		this.compoundLiteralAnalyzer = new CompoundLiteralAnalyzer(this);
		this.statementAnalyzer = new StatementAnalyzer(this, expressionAnalyzer);
		this.typeAnalyzer = new TypeAnalyzer(this, typeFactory, entityFactory);
		this.pragmaHandlerMap = new LinkedHashMap<String, PragmaHandler>();
	}

	// Public methods...

	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public void analyze(AST ast) throws SyntaxException {
		ASTNode root = ast.getRootNode();
		Iterable<ASTNode> children = root.children();
		Scope rootScope = root.getScope();

		try {
			standardTypes.addToScope(rootScope);
		} catch (UnsourcedException e) {
			throw error(e, root);
		}
		for (ASTNode child : children) {
			processExternalDefinitions((ExternalDefinitionNode) child);
		}
		findTentativeDefinitions(rootScope);
	}

	// Package private methods...

	SyntaxException error(String message, ASTNode node) {
		if (node == null)
			throw new NullPointerException("Null node and " + message);
		else
			return sourceFactory.newSyntaxException(message, node.getSource());
	}

	SyntaxException error(UnsourcedException e, ASTNode node) {
		return new SyntaxException(e, node.getSource());
	}

	Value valueOf(ExpressionNode expression) throws SyntaxException {
		return nodeFactory.getConstantValue(expression);
	}

	void processStaticAssertion(StaticAssertionNode node)
			throws SyntaxException {
		ExpressionNode expression = node.getExpression();
		Value value;

		value = valueOf(expression);
		if (value == null)
			throw error("Expression in static assertion not constant",
					expression);
		switch (valueFactory.isZero(value)) {
		case YES:
			throw error("Static assertion violation: "
					+ node.getMessage().getConstantValue(), node);
		case MAYBE:
			throw error("Possible static assertion violation: "
					+ node.getMessage().getConstantValue(), node);
		default:
		}
	}

	Function enclosingFunction(ASTNode someNode) {
		for (ASTNode node = someNode; node != null; node = node.parent()) {
			if (node instanceof FunctionDeclarationNode) {
				return ((FunctionDeclarationNode) node).getEntity();
			}
		}
		return null;
	}

	// Private methods...

	/**
	 * Process an ExternalDefinitionNode.
	 * 
	 */
	private void processExternalDefinitions(ExternalDefinitionNode node)
			throws SyntaxException {
		if (node == null)
			return;
		if (node instanceof VariableDeclarationNode) {
			declarationAnalyzer
					.processVariableDeclaration((VariableDeclarationNode) node);
		} else if (node instanceof FunctionDeclarationNode) {
			declarationAnalyzer
					.processFunctionDeclaration((FunctionDeclarationNode) node);
		} else if (node instanceof TypedefDeclarationNode) {
			declarationAnalyzer
					.processTypedefDeclaration((TypedefDeclarationNode) node);
		} else if (node instanceof PragmaNode) {
			processPragma((PragmaNode) node);
		} else if (node instanceof OmpDeclarativeNode) {
			processOmpDeclarativeNode((OmpDeclarativeNode) node);
		} else if (node instanceof StaticAssertionNode) {
			processStaticAssertion((StaticAssertionNode) node);
		} else if (node instanceof StructureOrUnionTypeNode) {
			((StructureOrUnionTypeNode) node)
					.setType(typeAnalyzer
							.processStructureOrUnionType((StructureOrUnionTypeNode) node));
		} else if (node instanceof EnumerationTypeNode) {
			((EnumerationTypeNode) node).setType(typeAnalyzer
					.processEnumerationType((EnumerationTypeNode) node));
		} else if (node instanceof AssumeNode) {
			statementAnalyzer.processStatement((AssumeNode) node);
		} else if (node instanceof ScopeParameterizedDeclarationNode) {
			declarationAnalyzer
					.processScopeParameterizedDeclaration((ScopeParameterizedDeclarationNode) node);
		} else {
			throw new RuntimeException("Unreachable");
		}
	}

	private void processOmpDeclarativeNode(OmpDeclarativeNode node)
			throws SyntaxException {
		SequenceNode<IdentifierExpressionNode> variables = node.variables();
		int count = variables.numChildren();

		for (int i = 0; i < count; i++) {
			this.expressionAnalyzer.processExpression(variables
					.getSequenceChild(i));
		}
	}

	void processPragma(PragmaNode node) throws SyntaxException {
		IdentifierNode identifier = node.getPragmaIdentifier();

		if (identifier == null) {
			return;
		} else {
			String name = identifier.name();
			PragmaHandler handler = pragmaHandlerMap.get(name);

			if (handler == null) {
				handler = entityFactory.newPragmaHandler(name);

				pragmaHandlerMap.put(name, handler);
			}
			identifier.setEntity(handler);
		}
	}

	/**
	 * For objects that don't have definitions, see if they have a tentative
	 * definition. Choose the first one and make it the definition. From C11
	 * Sec. 6.9.2:
	 * 
	 * <blockquote> A declaration of an identifier for an object that has file
	 * scope without an initializer, and without a storage-class specifier or
	 * with the storage-class specifier static, constitutes a tentative
	 * definition. If a translation unit contains one or more tentative
	 * definitions for an identifier, and the translation unit contains no
	 * external definition for that identifier, then the behavior is exactly as
	 * if the translation unit contains a file scope declaration of that
	 * identifier, with the composite type as of the end of the translation
	 * unit, with an initializer equal to 0. </blockquote>
	 * 
	 * @param scope
	 */
	private void findTentativeDefinitions(Scope scope) {
		if (scope.getScopeKind() != ScopeKind.FILE)
			throw new IllegalArgumentException(
					"Tentative definition only exist at file scope");

		Iterator<Variable> variableIter = scope.getVariables();

		while (variableIter.hasNext()) {
			Variable variable = variableIter.next();
			VariableDeclarationNode declaration = variable.getDefinition();

			if (declaration == null) {
				Iterator<DeclarationNode> declIter = variable.getDeclarations();

				while (declIter.hasNext()) {
					declaration = (VariableDeclarationNode) declIter.next();

					if (declaration.getInitializer() == null
							&& !(declaration.hasAutoStorage()
									|| declaration.hasRegisterStorage()
									|| declaration.hasThreadLocalStorage() || declaration
										.hasExternStorage())) {
						variable.setDefinition(declaration);
						declaration.setIsDefinition(true);
						break;
					}
				}
			}
		}
	}

	@Override
	public void clear(AST unit) {
		clearNode(unit.getRootNode());
	}

	// TODO: why don't nodes have "clear" method in them?
	private void clearNode(ASTNode node) {
		if (node != null) {
			Iterable<ASTNode> children = node.children();

			if (node instanceof DeclarationNode) {
				((DeclarationNode) node).setEntity(null);
				((DeclarationNode) node).setIsDefinition(false);
			}
			if (node instanceof TypeNode) {
				((TypeNode) node).setType(null);
			}
			if (node instanceof IdentifierNode) {
				((IdentifierNode) node).setEntity(null);
			}
			if (node instanceof ExpressionNode) {
				ExpressionNode expr = (ExpressionNode) node;
				Type type = expr.getType();

				if (expr instanceof ConstantNode) {
					if (expr instanceof EnumerationConstantNode) {
						((ConstantNode) expr).setInitialType(null);
					}
				} else {
					expr.setInitialType(null);
				}
				((ExpressionNode) node).removeConversions();
				// clear constant value for scopes only, because they will
				// change (maybe they shouldn't be constant?)
				if (type != null && type.kind() == TypeKind.SCOPE)
					nodeFactory.setConstantValue(expr, null);
			}
			if (node instanceof LabelNode) {
				((LabelNode) node).setStatement(null);
			}
			if (node instanceof OrdinaryLabelNode) {
				((OrdinaryLabelNode) node).setFunction(null);
			}
			if (node instanceof ChooseStatementNode) {
				((ChooseStatementNode) node).setDefaultCase(null);
			}
			if (node instanceof SwitchNode) {
				((SwitchNode) node).clear();
			}
			for (ASTNode child : children)
				clearNode(child);
		}
	}
}
