package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Iterator;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.ArrayDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.FieldDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.LiteralObject;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.IntegerType;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.IntegerValue;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.UnsourcedException;

/**
 * 
 * Initialization, including of compound objects, is specified in C11 6.7.9.
 * Note in particular the following:
 * 
 * <pre>
 * 2. No initializer shall attempt to provide a value for an object
 *    not contained within the entity being initialized.
 * 3. The type of the entity to be initialized shall be an array
 *    of unknown size or a complete object type that is not a
 *    variable length array type.
 * 4. All the expressions in an initializer for an object that has
 *    static or thread storage duration shall be constant expressions
 *    or string literals.
 * 5. If the declaration of an identifier has block scope, and
 *    the identifier has external or internal linkage, the
 *    declaration shall have no initializer for the identifier.
 * 6. If a designator has the form
 *          [ constant-expression ]
 * 	  then the current object (defined below) shall have array
 *    type and the expression shall be an integer constant expression.
 *    If the array is of unknown size, any nonnegative value is valid.
 * 7. If a designator has the form
 *          . identifier
 *    then the current object (defined below) shall have structure
 *    or union type and the identifier shall be the name of a
 *    member of that type.
 * </pre>
 * 
 * The array extents must be either constants (e.g., [3]) or empty ([]). After
 * the first non-array type is reached from the root, they must all be
 * constants. Hence there is a prefix of array types which may or may not be
 * complete, followed by types which must be complete.
 * 
 * Strategy:
 * 
 * - LiteralTypeNode: construct a type tree representing the declared type of
 * the literal. nodes can be either array, or structOrUnion. Array nodes can be
 * either complete (with constant int) or incomplete. Incomplete nodes can only
 * occur in the prefix (before any non-array nodes occur). The tree will have a
 * spot in the incomplete type in the prefix which can be set. You must be able
 * to take a node and consider it the root of another tree. There is only one
 * copy of each node, they are shared by all methods. * the corresponding
 * declared Type * array, struct, or union (method) * fixed : boolean (is the
 * length fixed) * length : int * children LiteralTypeNode,
 * LiteralArrayTypeNode, LiteralStructOrUnionTypeNode (scalar type node will
 * belong to neither of above)
 * 
 * - create navigator and Designation with no mention of type
 * 
 * - increment: Designation, LiteralTypeNode (modifies Designation)
 * 
 * - create compound literal class that has no mention of type
 * 
 * - set: CompoundLiteral, Designation, LiteralTypeNode, Literal: modifies the
 * compound literal and possibly the literal type node
 * 
 * - get: CompoundLiteral, Designation, LiteralTypeNode: modifies nothing but
 * will throw exception if bounds are violated
 * 
 * - using the above tree, parse the literal nodes, building up the literal
 * object
 * 
 */
public class CompoundLiteralAnalyzer {

	// ***************************** Fields *******************************

	/** The entity analyzer controlling this declaration analyzer. */
	private EntityAnalyzer entityAnalyzer;

	private NodeFactory nodeFactory;

	private TypeFactory typeFactory;

	private ValueFactory valueFactory;

	private IntegerType intType;

	// ************************** Constructors ****************************

	public CompoundLiteralAnalyzer(EntityAnalyzer entityAnalyzer) {
		this.entityAnalyzer = entityAnalyzer;
		this.nodeFactory = entityAnalyzer.nodeFactory;
		this.typeFactory = entityAnalyzer.typeFactory;
		this.valueFactory = entityAnalyzer.valueFactory;
		this.intType = (IntegerType) typeFactory.basicType(BasicTypeKind.INT);
	}

	// ************************* Exported Methods *************************

	void processCompoundInitializer(CompoundInitializerNode compoundInitNode,
			ObjectType type) throws SyntaxException {
		LiteralTypeNode ltNode = makeTypeTree(type);
		CommonCompoundLiteralObject literalObject = interpret(compoundInitNode,
				ltNode);
		ObjectType completeType = extractType(ltNode);

		fill(literalObject);
		compoundInitNode.setLiteralObject(literalObject);
		compoundInitNode.setType(completeType);
	}

	// ************************* Private Methods **************************

	private SyntaxException error(String message, ASTNode node) {
		return entityAnalyzer.error(message, node);
	}

	private SyntaxException error(UnsourcedException e, ASTNode node) {
		return entityAnalyzer.error(e, node);
	}

	private LiteralTypeNode makeTypeTree(ObjectType type) {
		switch (type.kind()) {
		case ARRAY: {
			LiteralTypeNode child = makeTypeTree(((ArrayType) type)
					.getElementType());
			LiteralTypeNode result = new LiteralArrayTypeNode((ArrayType) type,
					child);

			child.setParent(result);
			return result;
		}
		case STRUCTURE_OR_UNION: {
			StructureOrUnionType sut = (StructureOrUnionType) type;
			int numFields = sut.getNumFields();
			LiteralTypeNode[] children = new LiteralTypeNode[numFields];
			LiteralTypeNode result;

			for (int i = 0; i < numFields; i++) {
				Field field = sut.getField(i);
				ObjectType fieldType = field.getType();
				LiteralTypeNode child = makeTypeTree(fieldType);

				children[i] = child;
			}
			result = new LiteralStructOrUnionTypeNode(sut, children);
			for (int i = 0; i < numFields; i++)
				children[i].setParent(result);
			return result;
		}
		default:
			return new LiteralScalarTypeNode(type);
		}
	}

	/**
	 * Extracts the complete type form the ltNode after it has been refined
	 * through the constructions of the literal object.
	 * 
	 * As soon as you hit a non-array type, you can stop, because the fields of
	 * structs or unions have to be complete, except for the last
	 * "flexible member", but that can't be initialized.
	 * 
	 * @param ltNode
	 * @return
	 */
	private ObjectType extractType(LiteralTypeNode ltNode) {
		if (ltNode instanceof LiteralArrayTypeNode) {
			LiteralTypeNode child = ((LiteralArrayTypeNode) ltNode)
					.getElementNode();
			ObjectType elementType = extractType(child);
			int length = ltNode.length();

			return typeFactory.arrayType(elementType,
					valueFactory.integerValue(intType, length));
		}
		return ltNode.getType();
	}

	/**
	 * Constructs an abstract Designation from an AST designation node.
	 * 
	 * @param desNode
	 * @param ltNode
	 * @return
	 * @throws SyntaxException
	 */
	private Designation processDesignation(DesignationNode desNode,
			LiteralTypeNode ltNode) throws SyntaxException {
		Iterator<DesignatorNode> iter = desNode.childIterator();
		Designation result = new Designation(ltNode);

		while (iter.hasNext()) {
			DesignatorNode designatorNode = iter.next();
			int index;

			if (designatorNode instanceof FieldDesignatorNode) {
				IdentifierNode fieldId = ((FieldDesignatorNode) designatorNode)
						.getField();
				String fieldName = fieldId.name();
				StructureOrUnionType suType = (StructureOrUnionType) ltNode
						.getType();
				Field field = suType.getField(fieldName);

				if (field == null)
					throw error("Structure or union type " + suType.getTag()
							+ " contains no field named " + fieldName, fieldId);
				fieldId.setEntity(field);
				index = field.getMemberIndex();
			} else if (designatorNode instanceof ArrayDesignatorNode) {
				ExpressionNode indexExpr = ((ArrayDesignatorNode) designatorNode)
						.getIndex();
				IntegerValue indexValue;

				entityAnalyzer.expressionAnalyzer.processExpression(indexExpr);
				indexValue = (IntegerValue) nodeFactory
						.getConstantValue(indexExpr);
				index = indexValue.getIntegerValue().intValue();
			} else
				throw new ABCRuntimeException("unreachable");
			result.add(new Navigator(index));
		}
		return result;
	}

	private CommonCompoundLiteralObject interpret(
			CompoundInitializerNode compoundInitNode, LiteralTypeNode ltNode)
			throws SyntaxException {
		CommonCompoundLiteralObject result = new CommonCompoundLiteralObject(
				ltNode, compoundInitNode);
		Designation position = new Designation(ltNode);
		Iterator<PairNode<DesignationNode, InitializerNode>> iter = compoundInitNode
				.childIterator();

		while (iter.hasNext()) {
			PairNode<DesignationNode, InitializerNode> pair = iter.next();
			DesignationNode desNode = pair.getLeft();
			InitializerNode initNode = pair.getRight();
			LiteralObject subLiteral;
			LiteralTypeNode subType;

			if (desNode != null) {
				position = processDesignation(desNode, ltNode);
			} else {
				if (position.length() == 0)
					position.add(new Navigator(0));
				else
					position.increment(ltNode);
			}
			if (initNode instanceof CompoundInitializerNode) {
				subType = position.getDesignatedType();
				subLiteral = interpret((CompoundInitializerNode) initNode,
						subType);
			} else {
				ExpressionNode expr = (ExpressionNode) initNode;

				position.descendToScalar();
				subType = position.getDesignatedType();
				assert subType instanceof LiteralScalarTypeNode;
				entityAnalyzer.expressionAnalyzer.processExpression(expr);
				// add conversions as necessary to expr:
				try {
					entityAnalyzer.expressionAnalyzer.processAssignment(
							subType.getType(), expr);
				} catch (UnsourcedException e) {
					throw error(e, expr);
				}
				subLiteral = new CommonScalarLiteralObject(subType, expr);
			}
			result.set(position, subLiteral);
		}
		return result;
	}

	/**
	 * Fills in missing spaces with 0s. Needs to create fake expression nodes
	 * for the 0s. Creates them with source the entire surrounding compound
	 * initializer.
	 * 
	 * @param object
	 *            compound literal object that has already been processed
	 * @throws SyntaxException
	 */
	private void fill(CommonCompoundLiteralObject object)
			throws SyntaxException {
		// for proper sourcing, need a node for each compound
		// literal object, or source
		LiteralTypeNode ltNode = object.getTypeNode();
		int length = ltNode.length();
		ASTNode sourceNode = object.getSourceNode();
		Source source = sourceNode.getSource();

		for (int i = 0; i < length; i++) {
			LiteralObject member = object.get(i);

			if (member == null) {
				// what is the type of this member supposed to be?
				LiteralTypeNode child = ltNode.getChild(i);

				if (child instanceof LiteralScalarTypeNode) {
					ExpressionNode fakeNode = nodeFactory
							.newIntegerConstantNode(source, "0");

					try {
						entityAnalyzer.expressionAnalyzer.processAssignment(
								child.getType(), fakeNode);
					} catch (UnsourcedException e) {
						throw error(e, sourceNode);
					}
					member = new CommonScalarLiteralObject(child, fakeNode);
				} else {
					member = new CommonCompoundLiteralObject(child, sourceNode);
				}
				object.setElement(i, member);
			}
			if (member instanceof CommonCompoundLiteralObject) {
				fill((CommonCompoundLiteralObject) member);
			}
		}
	}
}
