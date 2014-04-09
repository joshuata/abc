package edu.udel.cis.vsl.abc.ast.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.abc.antlr2ast.impl.ASTBuilder;
import edu.udel.cis.vsl.abc.ast.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeVariable;
import edu.udel.cis.vsl.abc.ast.entity.IF.StructureOrUnion;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.AtomicType;
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.QualifiedObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class CommonASTFactory implements ASTFactory {

	private NodeFactory nodeFactory;

	private TokenFactory tokenFactory;

	private TypeFactory typeFactory;

	private EntityFactory entityFactory;

	private ASTBuilder astBuilder;

	public CommonASTFactory(NodeFactory nodeFactory, TokenFactory tokenFactory,
			TypeFactory typeFactory) {
		this.nodeFactory = nodeFactory;
		this.tokenFactory = tokenFactory;
		this.typeFactory = typeFactory;
	}

	@Override
	public AST newTranslationUnit(ASTNode root) throws SyntaxException {
		AST unit = new CommonAST(this, (CommonASTNode) root);

		// do some preparation?
		return unit;
	}

	@Override
	public NodeFactory getNodeFactory() {
		return nodeFactory;
	}

	@Override
	public TokenFactory getTokenFactory() {
		return tokenFactory;
	}

	@Override
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	private ArrayType setElementType(ArrayType arrayType,
			ObjectType newElementType) {
		ObjectType oldElementType = arrayType.getElementType();

		if (oldElementType.equals(newElementType))
			return arrayType;
		if (arrayType.isComplete()) {
			if (arrayType.hasKnownConstantSize())
				return typeFactory.arrayType(newElementType,
						arrayType.getConstantSize());
			if (arrayType.hasUnspecifiedVariableLength())
				return typeFactory
						.unspecifiedVariableLengthArrayType(newElementType);
			return typeFactory.variableLengthArrayType(newElementType,
					arrayType.getVariableSize());
		} else
			return typeFactory.incompleteArrayType(newElementType);
	}

	@Override
	public Type substituteScopes(Type type, Map<ScopeVariable, ScopeValue> map) {
		TypeKind kind = type.kind();
		Type result;

		switch (kind) {
		case ARRAY: {
			ArrayType arrayType = (ArrayType) type;
			ObjectType elementType = arrayType.getElementType();
			ObjectType newElementType = (ObjectType) substituteScopes(
					elementType, map);

			result = setElementType(arrayType, newElementType);
			break;
		}
		case ATOMIC: {
			UnqualifiedObjectType baseType = ((AtomicType) type).getBaseType();
			UnqualifiedObjectType newBaseType = (UnqualifiedObjectType) substituteScopes(
					baseType, map);

			if (baseType != newBaseType)
				result = typeFactory.atomicType(newBaseType);
			else
				result = type;
			break;
		}
		case BASIC:
			result = type;
			break;
		case ENUMERATION:
			// initializers?
			result = type;
			break;
		case FUNCTION: {
			FunctionType functionType = (FunctionType) type;
			ObjectType returnType = functionType.getReturnType();
			ObjectType newReturnType = (ObjectType) substituteScopes(
					returnType, map);
			List<ObjectType> newInputTypes = new LinkedList<>();
			int numInputs = functionType.getNumParameters();
			boolean change = newReturnType != returnType;

			for (int i = 0; i < numInputs; i++) {
				ObjectType oldInputType = functionType.getParameterType(i);
				ObjectType newInputType = (ObjectType) substituteScopes(
						oldInputType, map);

				newInputTypes.add(newInputType);
				change = change || oldInputType != newInputType;
			}
			result = change ? typeFactory.functionType(newReturnType,
					functionType.fromIdentifierList(), newInputTypes,
					functionType.hasVariableArgs()) : type;
			break;
		}
		case HEAP:
			result = type;
			break;
		case OTHER_INTEGER:
			result = type;
			break;
		case POINTER: {
			Type baseType = ((PointerType) type).referencedType();
			Type newBaseType = substituteScopes(baseType, map);
			ScopeValue scopeValue = ((PointerType) type).scopeRestriction();
			ScopeValue newScopeValue;

			if (scopeValue == null)
				newScopeValue = null;
			else if (scopeValue instanceof ScopeVariable) {
				newScopeValue = map.get((ScopeVariable) scopeValue);
				if (newScopeValue == null)
					newScopeValue = scopeValue;
			} else
				newScopeValue = scopeValue;
			if (baseType != newBaseType
					|| (scopeValue != null && scopeValue != newScopeValue))
				result = typeFactory.pointerType(newBaseType, newScopeValue);
			else
				result = type;
			break;
		}
		case PROCESS:
			result = type;
			break;
		case QUALIFIED: {
			QualifiedObjectType qType = (QualifiedObjectType) type;
			UnqualifiedObjectType baseType = qType.getBaseType();
			UnqualifiedObjectType newBaseType = (UnqualifiedObjectType) substituteScopes(
					baseType, map);

			if (baseType == newBaseType) {
				result = type;
			} else {
				result = typeFactory.qualifiedType(newBaseType,
						qType.isConstQualified(), qType.isVolatileQualified(),
						qType.isRestrictQualified(), qType.isInputQualified(),
						qType.isOutputQualified());
			}
			break;
		}
		case SCOPE:
			result = type;
			break;
		case STRUCTURE_OR_UNION: {
			StructureOrUnionType sType = (StructureOrUnionType) type;
			int numFields = sType.getNumFields();
			List<Field> newFields = new LinkedList<Field>();
			StructureOrUnion newStructureOrUnion = null;
			StructureOrUnionType newStructureOrUnionType = null;

			for (int i = 0; i < numFields; i++) {
				Field oldField = sType.getField(i);
				ObjectType oldFieldType = oldField.getType();
				ObjectType newFieldType = (ObjectType) substituteScopes(
						oldFieldType, map);

				if (oldFieldType == newFieldType) {
					newFields.add(oldField);
				} else {
					if (newStructureOrUnion == null) {
						newStructureOrUnionType = typeFactory
								.structureOrUnionType(new Object(),
										sType.isStruct(), sType.getTag());
						newStructureOrUnion = entityFactory
								.newStructureOrUnion(newStructureOrUnionType);
					}
					newFields.add(entityFactory.newField(
							oldField.getDefinition(), newFieldType,
							oldField.getBidWidth(), newStructureOrUnion));
				}
			}
			if (newStructureOrUnion == null)
				result = type;
			else {
				newStructureOrUnionType.complete(newFields);
				result = newStructureOrUnionType;
			}
			break;
		}
		case VOID:
			result = type;
			break;
		default:
			throw new ASTException("unreachable");
		}
		return result;
	}

	@Override
	public ASTBuilder getASTBuilder() {
		return this.astBuilder;
	}

	@Override
	public void setASTBuilder(ASTBuilder astBuilder) {
		this.astBuilder = astBuilder;
	}
}
