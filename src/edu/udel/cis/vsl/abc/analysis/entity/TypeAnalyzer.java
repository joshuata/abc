package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ABC;
import edu.udel.cis.vsl.abc.ABC.Language;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.EntityKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Enumeration;
import edu.udel.cis.vsl.abc.ast.entity.IF.Enumerator;
import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.entity.IF.StructureOrUnion;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.ast.entity.IF.Variable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.AtomicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.BasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.PointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypedefNameNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.IntegerType;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.QualifiedObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;
import edu.udel.cis.vsl.abc.ast.value.IF.IntegerValue;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.UnsourcedException;

public class TypeAnalyzer {

	// ***************************** Fields *******************************

	private EntityAnalyzer entityAnalyzer;

	private TypeFactory typeFactory;

	private NodeFactory nodeFactory;

	private EntityFactory entityFactory;

	private ValueFactory valueFactory;

	/**
	 * The type used for enumerators, i.e., the elements of enumeration types.
	 * It is an unspecified integer type according to the C Standard.
	 */
	private IntegerType enumeratorType;

	// ************************** Constructors ****************************

	TypeAnalyzer(EntityAnalyzer entityAnalyzer, TypeFactory typeFactory,
			EntityFactory entityFactory) {
		this.entityAnalyzer = entityAnalyzer;
		this.nodeFactory = entityAnalyzer.nodeFactory;
		this.typeFactory = typeFactory;
		this.entityFactory = entityFactory;
		this.valueFactory = entityAnalyzer.valueFactory;
		this.enumeratorType = (IntegerType) typeFactory
				.basicType(BasicTypeKind.INT);
	}

	// ************************* Exported Methods **************************

	Type processTypeNode(TypeNode typeNode) throws SyntaxException {
		return processTypeNode(typeNode, false);
	}

	/**
	 * Process a TypeNode. The Type defined by that type node is computed and
	 * associated to the TypeNode.
	 * 
	 * This method may also entail the creation and/or modification of entities.
	 * For example, if the type node defines a strucuture or union type, or
	 * enumeration type, then the corresponding entities may be created or
	 * completed.
	 * 
	 * @param typeNode
	 * @return
	 * @throws SyntaxException
	 */
	Type processTypeNode(TypeNode typeNode, boolean isParameter)
			throws SyntaxException {
		TypeNodeKind kind = typeNode.kind();
		Type type;

		switch (kind) {
		case VOID:
			type = typeFactory.voidType();
			break;
		case BASIC:
			type = processBasicType((BasicTypeNode) typeNode);
			break;
		case ENUMERATION:
			type = processEnumerationType((EnumerationTypeNode) typeNode);
			break;
		case ARRAY:
			type = processArrayType((ArrayTypeNode) typeNode, isParameter);
			break;
		case STRUCTURE_OR_UNION:
			type = processStructureOrUnionType((StructureOrUnionTypeNode) typeNode);
			break;
		case FUNCTION:
			type = processFunctionType((FunctionTypeNode) typeNode, isParameter);
			break;
		case POINTER:
			type = processPointerType((PointerTypeNode) typeNode);
			break;
		case ATOMIC:
			type = processAtomicType((AtomicTypeNode) typeNode);
			break;
		case TYPEDEF_NAME:
			type = processTypedefName((TypedefNameNode) typeNode);
			break;
		case SCOPE:
			type = typeFactory.scopeType();
			break;
		default:
			throw new RuntimeException("Unreachable");
		}
		typeNode.setType(type);
		return type;
	}

	/**
	 * Creates new enumeration entity and type and adds them to the scope of the
	 * given node.
	 * 
	 * @param node
	 *            an enumeration type node with non-null enumerators
	 * @return the new enumeration entity
	 * @throws SyntaxException
	 */
	private Enumeration createEnumeration(EnumerationTypeNode node)
			throws SyntaxException {
		SequenceNode<EnumeratorDeclarationNode> enumerators = node
				.enumerators();
		Scope scope = node.getScope();
		String tag = node.getName(); // could be null
		Iterator<EnumeratorDeclarationNode> enumeratorIter = enumerators
				.childIterator();
		List<Enumerator> enumeratorList = new LinkedList<Enumerator>();
		EnumerationType enumerationType = typeFactory
				.enumerationType(node, tag);
		Enumeration enumeration;
		IntegerValue value = null;

		// clear it, in case it was used in previous analysis pass
		enumerationType.clear();
		enumeration = entityFactory.newEnumeration(enumerationType);
		scope.add(enumeration);
		enumeration.setDefinition(node);
		enumeration.addDeclaration(node);
		while (enumeratorIter.hasNext()) {
			EnumeratorDeclarationNode decl = enumeratorIter.next();
			ExpressionNode constantNode = decl.getValue();
			Enumerator enumerator;

			// need to process the constantNode

			// also: when you add an enumerator to a scope, that contains
			// its value, right?

			if (constantNode == null) {
				if (value == null)
					value = valueFactory.integerValue(enumeratorType, 0);
				else
					value = valueFactory.plusOne(value);
			} else {
				Value tmpValue;

				entityAnalyzer.expressionAnalyzer
						.processExpression(constantNode);
				if (!constantNode.isConstantExpression())
					throw error(
							"Non-constant expression used in enumerator definition",
							constantNode);
				tmpValue = nodeFactory.getConstantValue(constantNode);
				if (!(tmpValue instanceof IntegerValue))
					throw error(
							"Constant expression of concrete integer type expected, not "
									+ tmpValue, constantNode);
				value = (IntegerValue) tmpValue;
			}
			enumerator = entityFactory.newEnumerator(decl, enumerationType,
					value);
			enumerator.addDeclaration(decl);
			enumerator.setDefinition(decl);
			decl.setEntity(enumerator);
			decl.getIdentifier().setEntity(enumerator);
			enumeratorList.add(enumerator);
			try {
				scope.add(enumerator);
			} catch (UnsourcedException e) {
				throw error(e, decl);
			}
		}
		enumerationType.complete(enumeratorList);
		return enumeration;
	}

	/**
	 * See C11 6.7.2.2 and 6.7.2.3. The procedure is as follows:
	 * 
	 * If there is no tag: there has to be an enumerator list, and this defines
	 * a new unnamed entity and enumeration type in the current scope.
	 * 
	 * If there is a tag, proceed as follows:
	 * 
	 * If there is an enumerator list: check that there is no tagged entity with
	 * the same tag in the current scope. If this check fails, syntax error.
	 * Create a new enumeration entity and type, add it to the current scope.
	 * 
	 * If there is not an enumerator list: check (1) there is a visible tagged
	 * entity with the same tag; (2) that tagged entity is an enum; and (3) that
	 * previous enum is complete. If any of these fails: syntax error. Else, use
	 * the old entity and type.
	 * 
	 * @param node
	 * @return
	 * @throws SyntaxException
	 */
	Type processEnumerationType(EnumerationTypeNode node)
			throws SyntaxException {
		Scope scope = node.getScope();
		IdentifierNode identifier = node.getIdentifier(); // could be null
		String tag = node.getName(); // could be null
		SequenceNode<EnumeratorDeclarationNode> enumerators = node
				.enumerators(); // could be null
		Enumeration enumeration;

		if (node.isRestrictQualified())
			throw error("Use of restrict qualifier with non-pointer type", node);
		if (tag != null) {
			if (enumerators != null) {
				TaggedEntity oldEntity = scope.getTaggedEntity(tag);

				if (oldEntity != null)
					throw error("Re-use of tag " + tag
							+ " for enumeration.  Previous use was at "
							+ oldEntity.getFirstDeclaration().getSource(), node);
				enumeration = createEnumeration(node);
			} else {
				TaggedEntity oldEntity = scope.getLexicalTaggedEntity(tag);

				if (oldEntity == null)
					throw error(
							"See C11 6.7.2.3(3):\n\"A type specifier of the form\n"
									+ "    enum identifier\n"
									+ "without an enumerator list shall only appear after the type\n"
									+ "it specifies is complete.\"", node);
				if (!(oldEntity instanceof Enumeration))
					throw error(
							"Re-use of tag "
									+ tag
									+ " for enumeration when tag is visible with different kind.  Previous use was at "
									+ oldEntity.getFirstDeclaration()
											.getSource(), node);
				enumeration = (Enumeration) oldEntity;
				assert enumeration.getType().isComplete();
				// if not, you would have caught the earlier incomplete use
				enumeration.addDeclaration(node);
			}
		} else {
			// no tag: create new anonymous enumeration
			if (enumerators == null)
				throw error("Anonymous enumeration with no enumerator list",
						node);
			enumeration = createEnumeration(node);
		}
		node.setEntity(enumeration);
		if (identifier != null)
			identifier.setEntity(enumeration);
		{
			boolean constQ = node.isConstQualified();
			boolean volatileQ = node.isVolatileQualified();
			UnqualifiedObjectType unqualifiedType = enumeration.getType();

			if (node.isAtomicQualified())
				unqualifiedType = typeFactory.atomicType(unqualifiedType);
			if (constQ || volatileQ)
				return typeFactory.qualifiedType(unqualifiedType, constQ,
						volatileQ, false, false, false);
			return unqualifiedType;
		}
	}

	/**
	 * See C11 6.7.2.1 and 6.7.2.3. The procedure is as follows:
	 * 
	 * If there is no tag, there has to be a declarator list, and this defines a
	 * new unnamed struct or union entity and type in the current scope.
	 * 
	 * If there is a tag, proceed as follows:
	 * 
	 * If there is a declarator list: check that there is no tagged entity with
	 * same tag in the current scope. If this check fails, syntax error. Else,
	 * create a new struct/union entity and type and add it to the current
	 * scope.
	 * 
	 * If there is no declarator list: see if there exists a visible tagged
	 * entity with same tag. If there does exist such an entity, check it has
	 * same kind as this one (struct or union), and use it. If there does not
	 * exist such an entity, create a new incomplete struct or union entity and
	 * type and add it to the current scope.
	 * 
	 * 
	 * @param node
	 * @return
	 * @throws SyntaxException
	 */
	Type processStructureOrUnionType(StructureOrUnionTypeNode node)
			throws SyntaxException {
		Scope scope = node.getScope();
		IdentifierNode identifier = node.getIdentifier();
		String tag = node.getName(); // could be null
		SequenceNode<FieldDeclarationNode> fieldDecls = node
				.getStructDeclList();
		StructureOrUnion structureOrUnion = null;
		StructureOrUnionType structureOrUnionType = null;

		if (node.isRestrictQualified())
			throw error("Use of restrict qualifier with non-pointer type", node);
		if (tag != null) {
			TaggedEntity entity = scope.getTaggedEntity(tag);

			if (entity != null) {
				if (entity.getEntityKind() != EntityKind.STRUCTURE_OR_UNION)
					throw error("Re-use of tag " + tag
							+ " for structure or union.  Previous use was at "
							+ entity.getFirstDeclaration().getSource(), node);
				structureOrUnion = (StructureOrUnion) entity;
				structureOrUnionType = structureOrUnion.getType();
				identifier.setEntity(structureOrUnion);
			}
		}
		if (structureOrUnion == null) {
			structureOrUnionType = typeFactory.structureOrUnionType(node,
					node.isStruct(), tag);
			// in case this was used in previous analysis pass, clear it:
			structureOrUnionType.clear();
			structureOrUnion = entityFactory
					.newStructureOrUnion(structureOrUnionType);
			structureOrUnion.addDeclaration(node);
			scope.add(structureOrUnion);
			if (identifier != null)
				identifier.setEntity(structureOrUnion);
		} else {
			structureOrUnion.addDeclaration(node);
		}
		if (fieldDecls != null) {
			Iterator<FieldDeclarationNode> fieldIter = fieldDecls
					.childIterator();
			List<Field> fieldList = new LinkedList<Field>();

			if (structureOrUnionType.isComplete()) {
				DeclarationNode definition = structureOrUnion.getDefinition();
				String message = "";

				if (definition != null)
					message = "Original definition at "
							+ definition.getSource();
				throw error("Re-definition of structure or union.  " + message,
						node);
			}
			structureOrUnion.setDefinition(node);
			while (fieldIter.hasNext()) {
				FieldDeclarationNode decl = fieldIter.next();
				TypeNode fieldTypeNode = decl.getTypeNode();
				ExpressionNode bitWidthExpression = decl.getBitFieldWidth();
				Value bitWidth;
				ObjectType fieldType;
				Field field;

				if (fieldTypeNode == null)
					fieldType = null;
				else {
					Type tempType = processTypeNode(fieldTypeNode);

					if (!(tempType instanceof ObjectType))
						throw error(
								"Non-object type for structure or union member",
								fieldTypeNode);
					fieldType = (ObjectType) tempType;
				}
				if (bitWidthExpression == null) {
					bitWidth = null;
				} else {
					if (!bitWidthExpression.isConstantExpression())
						throw error(
								"Non-constant expression used for bit width in field declaration",
								bitWidthExpression);
					bitWidth = nodeFactory.getConstantValue(bitWidthExpression);
				}
				field = entityFactory.newField(decl, fieldType, bitWidth,
						structureOrUnion);
				decl.setEntity(field);
				if (decl.getIdentifier() != null)
					decl.getIdentifier().setEntity(field);
				fieldList.add(field);
			}
			structureOrUnionType.complete(fieldList);
		}
		{
			boolean constQ = node.isConstQualified();
			boolean volatileQ = node.isVolatileQualified();
			UnqualifiedObjectType unqualifiedType = structureOrUnionType;

			if (node.isAtomicQualified())
				unqualifiedType = typeFactory.atomicType(unqualifiedType);
			if (constQ || volatileQ)
				return typeFactory.qualifiedType(unqualifiedType, constQ,
						volatileQ, false, false, false);
			return unqualifiedType;
		}
	}

	// ************************** Private Methods *****************************

	private SyntaxException error(String message, ASTNode node) {
		return entityAnalyzer.error(message, node);
	}

	private SyntaxException error(UnsourcedException e, ASTNode node) {
		return entityAnalyzer.error(e, node);
	}

	private Type processBasicType(BasicTypeNode node) throws SyntaxException {
		UnqualifiedObjectType unqualifiedType = typeFactory.basicType(node
				.getBasicTypeKind());
		boolean constQ = node.isConstQualified();
		boolean volatileQ = node.isVolatileQualified();
		boolean inputQ = node.isInputQualified();
		boolean outputQ = node.isOutputQualified();

		if (node.isRestrictQualified())
			throw error("restrict qualifier used with basic type", node);
		if (node.isAtomicQualified())
			unqualifiedType = typeFactory.atomicType(unqualifiedType);
		if (constQ || volatileQ || inputQ || outputQ)
			return typeFactory.qualifiedType(unqualifiedType, constQ,
					volatileQ, false, inputQ, outputQ);
		else
			return unqualifiedType;
	}

	/**
	 * 
	 * C11 6.7.6.2(1): "The element type shall not be an incomplete or function
	 * type. The optional type qualifiers and the keyword static shall appear
	 * only in a declaration of a function parameter with an array type, and
	 * then only in the outermost array type derivation."
	 * 
	 * @param node
	 * @return
	 * @throws SyntaxException
	 */
	private ObjectType processArrayType(ArrayTypeNode node, boolean isParameter)
			throws SyntaxException {
		TypeNode elementTypeNode = node.getElementType(); // non-null
		Type tempElementType = processTypeNode(elementTypeNode);
		ObjectType elementType;
		ExpressionNode sizeExpression;
		boolean constQ = node.isConstQualified();
		boolean volatileQ = node.isVolatileQualified();
		boolean restrictQ = node.isRestrictQualified();
		boolean inputQ = node.isInputQualified();
		boolean outputQ = node.isOutputQualified();
		ObjectType result;

		if (!(tempElementType instanceof ObjectType))
			throw error("Non-object type used for element type of array type",
					elementTypeNode);
		elementType = (ObjectType) tempElementType;
		if (ABC.language == Language.C && !isParameter
				&& !elementType.isComplete())
			throw error("Element type of array type is not complete",
					elementTypeNode);
		// C11 6.7.3(3):
		// "The type modified by the _Atomic qualifier shall not be an
		// array type or a function type."
		if (node.isAtomicQualified())
			throw error("_Atomic qualifier used with array type", node);
		// C11 6.7.3(9):
		// "If the specification of an array type includes any type qualifiers,
		// the element type is so-qualified, not the array type."
		// but don't apply that rule to $input and $output
		elementType = typeFactory.qualify(elementType, constQ, volatileQ,
				restrictQ, false, false);
		if (restrictQ
				&& elementType instanceof QualifiedObjectType
				&& ((QualifiedObjectType) elementType).getBaseType().kind() != TypeKind.POINTER)
			throw error("Use of restrict qualifier with non-pointer type", node);
		if (isParameter) {
			// no scope restriction on pointer given, so use null...
			PointerType pointerType = typeFactory
					.pointerType(elementType, null);
			UnqualifiedObjectType unqualifiedType = (node.hasAtomicInBrackets() ? typeFactory
					.atomicType(pointerType) : pointerType);

			return typeFactory.qualify(unqualifiedType,
					node.hasConstInBrackets(), node.hasVolatileInBrackets(),
					node.hasRestrictInBrackets(), false, false);
		}
		if (node.hasAtomicInBrackets() || node.hasConstInBrackets()
				|| node.hasVolatileInBrackets() || node.hasRestrictInBrackets())
			throw error("Type qualifiers in [...] in an array declarator "
					+ "can only appear in a parameter declaration",
					elementTypeNode);
		if (node.hasUnspecifiedVariableLength()) { // "*"
			result = typeFactory
					.unspecifiedVariableLengthArrayType(elementType);
		} else {
			sizeExpression = node.getExtent();
			if (sizeExpression == null) {
				result = typeFactory.incompleteArrayType(elementType);
			} else {
				entityAnalyzer.expressionAnalyzer
						.processExpression(sizeExpression);
				if (sizeExpression.isConstantExpression()) {
					result = typeFactory.arrayType(elementType,
							(IntegerValue) nodeFactory
									.getConstantValue(sizeExpression));
				} else {
					// C11 6.7.6.2(5): "If the size is an expression that is not
					// an integer constant expression: if it occurs in a
					// declaration at function prototype scope, it is treated as
					// if it were replaced by *"
					if (node.getScope().getScopeKind() == ScopeKind.FUNCTION_PROTOTYPE)
						result = typeFactory
								.unspecifiedVariableLengthArrayType(elementType);
					else
						result = typeFactory.variableLengthArrayType(
								elementType, sizeExpression);
				}
			}
		}
		if (inputQ || outputQ)
			result = typeFactory.qualify(result, false, false, false, inputQ,
					outputQ);
		return result;
	}

	// /**
	// * Processes a scope modifier in a pointer type declaration, as in double
	// * *<s> p.
	// *
	// * @param expressionNode
	// * the expression s occurring in the pointer type node
	// * @return not yet clear
	// * @throws SyntaxException
	// */
	// private ScopeVariable processScopeModifier(ExpressionNode expressionNode)
	// throws SyntaxException {
	// if (expressionNode instanceof IdentifierExpressionNode) {
	// IdentifierNode identifierNode = ((IdentifierExpressionNode)
	// expressionNode)
	// .getIdentifier();
	// String name = identifierNode.name();
	// OrdinaryEntity entity = identifierNode.getScope()
	// .getLexicalOrdinaryEntity(name);
	// EntityKind kind;
	//
	// if (entity == null)
	// throw error("Undeclared identifier " + name, identifierNode);
	// kind = entity.getEntityKind();
	// switch (kind) {
	// case VARIABLE:
	// break;
	// default:
	// throw error("Use of " + kind + " " + name + " as scope name",
	// identifierNode);
	// }
	// identifierNode.setEntity(entity);
	// expressionNode.setInitialType(entity.getType());
	// return (ScopeVariable) entity;
	// } else
	// throw new SyntaxException("Unknown kind of scope expression",
	// expressionNode.getSource());
	// }

	private Type processPointerType(PointerTypeNode node)
			throws SyntaxException {
		TypeNode referencedTypeNode = node.referencedType();
		Type referencedType = processTypeNode(referencedTypeNode);
		ExpressionNode scopeModifier = node.scopeModifier();
		ScopeValue pointerScope = scopeModifier == null ? null
				: entityAnalyzer.expressionAnalyzer
						.evaluateScopeExpression(scopeModifier);
		UnqualifiedObjectType unqualifiedType = typeFactory.pointerType(
				referencedType, pointerScope);

		if (node.isAtomicQualified())
			unqualifiedType = typeFactory.atomicType(unqualifiedType);
		return typeFactory.qualify(unqualifiedType, node.isConstQualified(),
				node.isVolatileQualified(), node.isRestrictQualified(),
				node.isInputQualified(), node.isOutputQualified());
	}

	private Type processAtomicType(AtomicTypeNode node) throws SyntaxException {
		// C11 6.7.2.4(3): "The type name in an atomic type specifier shall not
		// refer to an array type, a function type, an atomic type, or a
		// qualified type."
		Type baseType = processTypeNode(node.getBaseType());
		TypeKind kind = baseType.kind();

		if (kind == TypeKind.ARRAY)
			throw error(
					"Type name used in atomic type specifier refers to an array type",
					node);
		if (kind == TypeKind.FUNCTION)
			throw error(
					"Type name used in atomic type specifier refers to a function type",
					node);
		if (kind == TypeKind.ATOMIC)
			throw error(
					"Type name used in atomic type specifier refers to an atomic type",
					node);
		if (kind == TypeKind.QUALIFIED)
			throw error(
					"Type name used in atomic type specifier refers to a qualified type",
					node);
		return typeFactory.atomicType((UnqualifiedObjectType) baseType);
	}

	private Type processTypedefName(TypedefNameNode typeNode)
			throws SyntaxException {
		String name = typeNode.getName().name();
		Scope scope = typeNode.getScope();
		OrdinaryEntity entity = scope.getLexicalOrdinaryEntity(name);
		EntityKind kind = entity.getEntityKind();
		Typedef typedef;

		if (kind != EntityKind.TYPEDEF)
			throw error(
					"Internal error: typedef name does not refer to typedef",
					typeNode);
		typedef = (Typedef) entity;
		typeNode.getName().setEntity(typedef);
		return typedef.getType();
	}

	private boolean onlyVoid(SequenceNode<VariableDeclarationNode> parameters) {
		if (parameters.numChildren() == 1) {
			VariableDeclarationNode decl = parameters.getSequenceChild(0);

			if (decl != null && decl.getIdentifier() == null) {
				TypeNode typeNode = decl.getTypeNode();

				return typeNode != null && typeNode.kind() == TypeNodeKind.VOID
						&& !typeNode.isAtomicQualified()
						&& !typeNode.isConstQualified()
						&& !typeNode.isRestrictQualified()
						&& !typeNode.isVolatileQualified();
			}
		}
		return false;
	}

	private Type processFunctionType(FunctionTypeNode node, boolean isParameter)
			throws SyntaxException {
		Type result;
		TypeNode returnTypeNode = node.getReturnType();
		SequenceNode<VariableDeclarationNode> parameters = node.getParameters();
		int numParameters = parameters.numChildren();
		boolean isDefinition = node.parent() instanceof FunctionDefinitionNode;
		boolean fromIdentifierList = node.hasIdentifierList();
		boolean hasVariableArgs = node.hasVariableArgs();
		Type tempReturnType = processTypeNode(returnTypeNode);
		ObjectType returnType;

		// "A function declarator shall not specify a return type that is a
		// function type or an array type."
		if (!(tempReturnType instanceof ObjectType))
			throw error(
					"Return type in function declaration is not an object type",
					returnTypeNode);
		if (tempReturnType instanceof ArrayType)
			throw error("Return type in function declaration is an array type",
					returnTypeNode);
		returnType = (ObjectType) tempReturnType;
		if (fromIdentifierList && !isDefinition && numParameters == 0) {
			// no information know about parameters
			result = typeFactory.functionType(returnType);
		} else {
			List<ObjectType> parameterTypes = new LinkedList<ObjectType>();

			if (hasVariableArgs || !onlyVoid(parameters)) {
				Iterator<VariableDeclarationNode> parameterIter = parameters
						.childIterator();

				while (parameterIter.hasNext()) {
					VariableDeclarationNode decl = parameterIter.next();
					TypeNode parameterTypeNode;
					Variable variable = entityAnalyzer.declarationAnalyzer
							.processVariableDeclaration(decl, true);

					parameterTypeNode = decl.getTypeNode();
					if (parameterTypeNode == null)
						throw error("No type specified for function parameter",
								decl);
					// TODO: check alignment specifiers, storage specifiers.
					// decl.setEntity(variable);
					// add to scope...?
					// decl.getScope().\
					parameterTypes
							.add((ObjectType) parameterTypeNode.getType());
				}
			}
			result = typeFactory.functionType(returnType, fromIdentifierList,
					parameterTypes, hasVariableArgs);
		}
		if (isParameter)
			result = typeFactory.pointerType(result, null);
		return result;
	}

}
