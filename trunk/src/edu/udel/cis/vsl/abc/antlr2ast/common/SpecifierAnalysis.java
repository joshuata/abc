package edu.udel.cis.vsl.abc.antlr2ast.common;

import static edu.udel.cis.vsl.abc.parse.IF.CParser.ABSTRACT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ALIGNAS;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ATOMIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.AUTO;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.BOOL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CHAR;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.COMPLEX;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.CONST;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DOMAIN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.DOUBLE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.ENUM;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.EXTERN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FATOMIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.FLOAT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.GLOBAL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INLINE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INPUT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.INT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.LONG;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.NORETURN;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.OUTPUT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.RANGE;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.REAL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.REGISTER;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.RESTRICT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHARED;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SHORT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.SIGNED;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STATIC;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.STRUCT;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.THREADLOCAL;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TYPEDEF;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.TYPEDEF_NAME;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.UNION;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.UNSIGNED;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.VOID;
import static edu.udel.cis.vsl.abc.parse.IF.CParser.VOLATILE;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * This class is used to analyze the "declaration specifier" section of a C
 * declaration. The specifier information is complex and involves many different
 * elements of the language. It consists of type specifiers and qualifiers,
 * storage class specifiers, function specifiers, and alignment specifiers. See
 * C11 Sections 6.7.1 through 6.7.5 for details.
 * 
 * Sec. 6.7.2 of the C11 Standard covers "type specifiers". The set of type
 * specifiers must fall into one of the following categories: a "basic multiset"
 * (see class BasicMultiset for the enumeration of those sets); or one of the
 * singleton sets VOID; STRUCT; UNION; ENUM; TYPEDEF_NAME; or ATOMIC. (Note that
 * some of these are actually structured types, such as STRUCT, UNION, ENUM, and
 * ATOMIC.) Each of these categories is represented by an element of the
 * enumerated type TypeName.TypeNameKind. This class will determine which of the
 * categories the given set of type specifiers belongs to, and other
 * information.
 * 
 * Sec. 6.7.3 covers "type qualifiers": CONST, RESTRICT, VOLATILE, ATOMIC. A
 * qualifier can appear more than once---it is the same as appearing once. This
 * class records whether or not each qualifier occurs.
 * 
 * The storage class specifiers are TYPEDEF, EXTERN, STATIC, THREADLOCAL, AUTO,
 * and REGISTER.
 * 
 * The function specifiers are INLINE and NORETURN.
 * 
 * The alignment specifiers fall into two categories: ALIGNAS ( type ) and
 * ALIGNAS ( expression ). There can be any number of both.
 * 
 * There are a lot of restrictions on the allowable combinations of specifiers.
 * See the C11 Standard for details.
 */
public class SpecifierAnalysis {

	// the basic type specifier keywords and VOID...

	// Instance variables...

	private ParseTree parseTree;

	/**
	 * The given tree node whose children are the declaration specifiers to be
	 * analyzed.
	 */
	CommonTree specifierListNode;

	/**
	 * The kind of type name represented by these specifiers. One of BASIC,
	 * VOID, STRUCTURE, UNION, ENUMERATION, TYPEDEF_NAME, or ATOMIC.
	 */
	TypeNodeKind typeNameKind = null;

	/**
	 * If the type name kind is NOT BASIC, this variable will hold a reference
	 * to the child of the specifierListNode that is the sole type specifier.
	 * 
	 * If the type name kind is BASIC, this will be null. (Reason: basic types
	 * require a set of type specifiers, which is why the multisets are needed.)
	 */
	CommonTree typeSpecifierNode = null;

	/**
	 * If the type name kind is BASIC, this will contain a list of the type
	 * specifiers.
	 */
	List<CommonTree> basicSpecifierNodes = null;

	/**
	 * If the typeNameKind is BASIC, this will hold the kind of BASIC type.
	 * Otherwise it will be null.
	 */
	BasicTypeKind basicTypeKind = null;

	// multiset specifiers:
	BasicMultiset set = new BasicMultiset();
	// other types:
	int voidTypeCount = 0;
	int atomicTypeCount = 0; // _Atomic(typeName): has one child
	int structTypeCount = 0;
	int unionTypeCount = 0;
	int enumTypeCount = 0;
	int typedefNameCount = 0;
	// qualifiers:
	boolean constQualifier = false;
	boolean restrictQualifier = false;
	boolean volatileQualifier = false;
	boolean atomicQualifier = false; // _Atomic: has 0 children
	boolean inputQualifier = false;
	boolean outputQualifier = false;
	// storage class specifiers
	int typedefCount = 0;
	int externCount = 0;
	int staticCount = 0;
	int threadLocalCount = 0;
	int autoCount = 0;
	int registerCount = 0;
	int sharedCount = 0;
	// function specifiers: may appear multiple times
	boolean inlineSpecifier = false;
	boolean noreturnSpecifier = false;
	boolean abstractSpecifier = false;
	boolean fatomicSpecifier = false;
	boolean globalSpecifier = false;
	// CIVL-C continuity for abstract functions: can occur only once
	int continuity = 0;
	// CIVL-C domain specifier: can occur only once
	int domainTypeCount = 0;
	// int domainDimension = -1;
	int rangeTypeCount = 0;
	// alignment specifiers
	List<CommonTree> alignmentTypeNodes = new LinkedList<CommonTree>();
	List<CommonTree> alignmentExpressionNodes = new LinkedList<CommonTree>();
	private Configuration configuration;

	/**
	 * Creates a new analysis object and conducts the analysis. The
	 * specifierListNode is the root of a tree which is a list of declaration
	 * specifiers. It may have type DECLARATION_SPECIFIERS or
	 * SPECIFIER_QUALIFIER_LIST.
	 * 
	 * @param specifierListNode
	 * @throws SyntaxException
	 */
	SpecifierAnalysis(CommonTree specifierListNode, ParseTree parseTree,
			Configuration configuration) throws SyntaxException {
		this.specifierListNode = specifierListNode;
		this.parseTree = parseTree;
		this.configuration = configuration;
		analyze();
	}

	private SyntaxException error(String message, CommonTree tree) {
		return parseTree.newSyntaxException(message, tree);
	}

	private void analyze() throws SyntaxException {
		int numChildren = specifierListNode.getChildCount();

		if (numChildren == 0) {
			if (this.configuration.svcomp()) {
				typeNameKind = TypeNodeKind.BASIC;
				basicTypeKind = BasicTypeKind.INT;
			} else
				error("Declaration is missing a type name", specifierListNode);
		} else {
			for (int i = 0; i < numChildren; i++) {
				CommonTree node = (CommonTree) specifierListNode.getChild(i);
				int kind = node.getType();

				switch (kind) {
				case CHAR:
				case SHORT:
				case INT:
				case LONG:
				case FLOAT:
				case DOUBLE:
				case REAL:
				case SIGNED:
				case UNSIGNED:
				case BOOL:
				case COMPLEX:
					set.add(kind);
					setTypeNameKind(TypeNodeKind.BASIC);
					if (basicSpecifierNodes == null)
						basicSpecifierNodes = new LinkedList<CommonTree>();
					basicSpecifierNodes.add(node);
					break;
				case VOID:
					voidTypeCount++;
					setTypeNameKind(TypeNodeKind.VOID);
					setTypeSpecifierNode(node);
					break;
				case ATOMIC:
					if (node.getChildCount() > 0) {
						atomicTypeCount++;
						setTypeNameKind(TypeNodeKind.ATOMIC);
						setTypeSpecifierNode(node);
					} else {
						atomicQualifier = true;
					}
					break;
				case STRUCT:
					structTypeCount++;
					setTypeNameKind(TypeNodeKind.STRUCTURE_OR_UNION);
					setTypeSpecifierNode(node);
					break;
				case UNION:
					unionTypeCount++;
					setTypeNameKind(TypeNodeKind.STRUCTURE_OR_UNION);
					setTypeSpecifierNode(node);
					break;
				case ENUM:
					enumTypeCount++;
					setTypeNameKind(TypeNodeKind.ENUMERATION);
					setTypeSpecifierNode(node);
					break;
				case TYPEDEF_NAME:
					typedefNameCount++;
					setTypeNameKind(TypeNodeKind.TYPEDEF_NAME);
					setTypeSpecifierNode(node);
					break;
				case DOMAIN:
					domainTypeCount++;
					setTypeNameKind(TypeNodeKind.DOMAIN);
					setTypeSpecifierNode(node);
					// if (node.getChildCount() != 0) {
					// CommonTree child = (CommonTree) node.getChild(0);
					//
					// if (child.getToken().getType() != CParser.ABSENT)
					// domainDimension = parseInt(child);
					// }
					break;
				case RANGE:
					rangeTypeCount++;
					setTypeNameKind(TypeNodeKind.RANGE);
					setTypeSpecifierNode(node);
					break;
				case CONST:
					constQualifier = true;
					break;
				case RESTRICT:
					restrictQualifier = true;
					break;
				case VOLATILE:
					volatileQualifier = true;
					break;
				case INPUT:
					inputQualifier = true;
					break;
				case OUTPUT:
					outputQualifier = true;
					break;
				case TYPEDEF:
					typedefCount++;
					break;
				case EXTERN:
					externCount++;
					break;
				case STATIC:
					staticCount++;
					break;
				case THREADLOCAL:
					threadLocalCount++;
					break;
				case AUTO:
					autoCount++;
					break;
				case REGISTER:
					registerCount++;
					break;
				case SHARED:
					sharedCount++;
					break;
				case INLINE:
					inlineSpecifier = true;
					break;
				case NORETURN:
					noreturnSpecifier = true;
					break;
				case GLOBAL:
					globalSpecifier = true;
					break;
				case FATOMIC:
					fatomicSpecifier = true;
					break;
				case ALIGNAS: {
					int alignKind = ((CommonTree) node.getChild(0)).getType();
					CommonTree argument = (CommonTree) node.getChild(1);

					if (alignKind == CParser.TYPE) {
						alignmentTypeNodes.add(argument);
					} else if (kind == CParser.EXPR) {
						alignmentExpressionNodes.add(argument);
					} else {
						throw error("Unexpected kind of ALIGN_AS argument",
								node);
					}
					break;
				}
				case ABSTRACT:
					abstractSpecifier = true;
					if (node.getChildCount() == 0) {
						continuity = 0;
					} else {
						continuity = parseInt((CommonTree) node.getChild(0));
					}
					break;
				default:
					throw error("Unknown declaration specifier", node);
				}
			}
			if (typeNameKind == null)
				throw error("Declaration is missing a type name",
						specifierListNode);
			if (typeNameKind == TypeNodeKind.BASIC) {
				basicTypeKind = BasicMultiset.getBasicTypeKind(set);
				if (basicTypeKind == null)
					throw error("Illegal type specifiers", specifierListNode);
			}
		}
	}

	public BasicTypeKind getBasicTypeKind() {
		return basicTypeKind;
	}

	private void setTypeNameKind(TypeNodeKind kind) throws SyntaxException {
		if (typeNameKind != null && typeNameKind != kind)
			throw error(
					"Two different kinds of types specified in declaration specifier list: "
							+ typeNameKind + " and " + kind, specifierListNode);
		typeNameKind = kind;
	}

	private void setTypeSpecifierNode(CommonTree node) throws SyntaxException {
		if (typeSpecifierNode != null)
			throw error(
					"Two type specifiers in declaration. Previous specifier was at "
							+ error("", typeSpecifierNode).getSource(), node);
		typeSpecifierNode = node;
	}

	/**
	 * Parses a node expected to contain an integer constant.
	 * 
	 * @param node
	 *            a CommonTree node expected to contain integer constant
	 * @return the int value of that integer constant
	 * @throws SyntaxException
	 *             if the text of the node cannot be parsed to yield an integer
	 */
	private int parseInt(CommonTree node) throws SyntaxException {
		try {
			int result = Integer.parseInt(node.getText());

			return result;
		} catch (Exception e) {
			throw error("Expected integer constant", node);
		}
	}

}
