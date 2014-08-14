package edu.udel.cis.vsl.abc.ast.type.IF;

import java.io.PrintStream;

/**
 * <p>
 * An instance of Type represents a C type. A type is a conceptual entity, not a
 * syntactic element. This class is the root of the type hierarchy for all types
 * used to represent C types.
 * </p>
 * 
 * <p>
 * There may also be additional types that are not part of C, but are part of
 * some extension to C (such as CIVL-C).
 * </p>
 * 
 * <p>
 * Summary of type hierarchy:
 * </p>
 * 
 * {@link Type}
 * <ul>
 * <li>{@link FunctionType}
 * <li>{@link ObjectType}
 * <ul>
 * <li>{@link UnqualifiedObjectType}
 * <ul>
 * <li>{@link ArithmeticType}</li>
 * <li>{@link ArrayType}</li>
 * <li>{@link AtomicType}</li>
 * <li>{@link StandardBasicType}</li>
 * <li>etc.</li>
 * </ul>
 * </li>
 * <li>{@link QualifiedObjectType}
 * <ul>
 * <li>uses one or more of <code>const</code>, <code>volatile</code>,
 * <code>restrict</code>, etc.</li>
 * </ul>
 * </li>
 * </ul>
 * </ul>
 * 
 * Note: {@link AtomicType} and {@link QualifiedObjectType} constructors take an
 * {@link UnqualifiedObjectType}.
 * 
 * @author siegel
 * 
 */
public interface Type {

	/**
	 * The different kinds of types.
	 */
	public static enum TypeKind {
		/**
		 * An array type; an instance of {@link ArrayType}
		 */
		ARRAY,
		/**
		 * An atomic type; an instance of {@link AtomicType}
		 */
		ATOMIC,
		/**
		 * A "standard basic type"; an instance of {@link StandardBasicType}
		 */
		BASIC,
		/**
		 * The CIVL-C domain type, <code>$domain</code> or
		 * <code>$domain(n)</code>.
		 */
		DOMAIN,
		/**
		 * An enumeration type; an instance of {@link EnumerationType}
		 */
		ENUMERATION,
		/**
		 * A function type; an instance of {@link FunctionType}
		 */
		FUNCTION,
		/**
		 * The CIVL-C heap type, represened by <code>$heap</code>
		 */
		HEAP,
		/**
		 * An integer type which is not a standard basic type. The C Standard
		 * allows a C implementation to provide additional integer types beyond
		 * those specified in the Standard.
		 */
		OTHER_INTEGER,
		/**
		 * A pointer type; an instance of {@link PointerType}
		 */
		POINTER,
		/**
		 * The CIVL-C process type, represented by <code>$proc</code>
		 */
		PROCESS,
		/**
		 * A qualified object type; an instance of {@link QualifiedObjectType}
		 */
		QUALIFIED,
		/**
		 * The CIVL-C range type, denoted <code>$range</code>
		 */
		RANGE,
		/**
		 * The CIVL-C scope type, represented by <code>$scope</code>
		 */
		SCOPE,
		/**
		 * A structure or union type; an instance of
		 * {@link StructureOrUnionType}
		 */
		STRUCTURE_OR_UNION,
		/**
		 * The <code>void</code> type, used to represent no type in places where
		 * a type is syntactically required.
		 */
		VOID
	};

	/**
	 * Is this type "compatible" with the given type? See C11 Sec. 6.2.7 for the
	 * definition of "compatible".
	 * 
	 * Notion extended to deal with CIVL-C scope-restricted pointers. Two
	 * pointers types: (P1) pointer to T1 in scope S1, and (P2) pointer to T2 in
	 * scope S2 are compatible iff:
	 * 
	 * <ol>
	 * <li>pointer-to-T1 and pointer-to-T2 are compatible in the C sense, and</li>
	 * <li>one of the following holds:
	 * <ol>
	 * <li>at least one of S1, S2 is null</li>
	 * <li>S1 and S2 are non null and one of the two scopes is contained in the
	 * other</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * 
	 * Note that the above does not guarantee "assignment compatibility". For an
	 * assignment "lhs=rhs" where lhs has type P1 and rhs has type P2, we must
	 * have in addition one of the following: (1) S1 is null; or (2) S1 is
	 * non-null and is the root scope; or (3) S1 is a non-null, non-root scope
	 * and S2 is a non-null scope contained in S1. In such cases, there will be
	 * an automatic conversion. In all other cases, an explicit cast is
	 * required.
	 * 
	 * @param type
	 *            the type to compare with this one for compatibility
	 * @return true iff the two types are compatible
	 */
	boolean compatibleWith(Type type);
	
	boolean equivalentTo(Type type);

	/**
	 * The types created by the type factories are given unique id numbers. This
	 * method returns the id number of this type.
	 * 
	 * @return the id number of this type
	 */
	int getId();

	/**
	 * C11 6.2.4(21):
	 * 
	 * "Arithmetic types and pointer types are collectively called scalar types."
	 * 
	 * @return true iff type is scalar
	 */
	boolean isScalar();

	/**
	 * Is this type a "VM" type (variable modified type)? This is defined in the
	 * C11 Standard Sec. 6.7.6:
	 * 
	 * <blockquote> If, in the nested sequence of declarators in a full
	 * declarator, there is a declarator specifying a variable length array
	 * type, the type specified by the full declarator is said to be variably
	 * modified. Furthermore, any type derived by declarator type derivation
	 * from a variably modified type is itself variably modified. </blockquote>
	 * 
	 * The definition of "variable length array type" is given in Sec. 6.7.6.2:
	 * 
	 * <blockquote> If the size is not present, the array type is an incomplete
	 * type. If the size is * instead of being an expression, the array type is
	 * a variable length array type of unspecified size, which can only be used
	 * in declarations or type names with function prototype scope;143) such
	 * arrays are nonetheless complete types. If the size is an integer constant
	 * expression and the element type has a known constant size, the array type
	 * is not a variable length array type; otherwise, the array type is a
	 * variable length array type. (Variable length arrays are a conditional
	 * feature that implementations need not support; see 6.10.8.3.)
	 * </blockquote>
	 * 
	 * @return true iff this type is a VM type
	 */
	boolean isVariablyModified();

	/**
	 * The kind of type this is. See definition of the enumerated type
	 * {@link TypeKind}. These kinds partition the set of all types.
	 * 
	 * @return the kind of this type
	 */
	TypeKind kind();

	/**
	 * Prints the type in a tree-formatted style. The prefix string is prepended
	 * to each line of output other than the first. Output for structure or
	 * union types may leave out the fields by setting abbrv to true.
	 * 
	 * @param prefix
	 *            string to preprend to lines after first
	 * @param out
	 *            PrintStream to which output should be sent
	 * @param abbrv
	 *            if true, abbreviate representations of structure or union
	 *            types by leaving out their fields
	 */
	void print(String prefix, PrintStream out, boolean abbrv);

}
