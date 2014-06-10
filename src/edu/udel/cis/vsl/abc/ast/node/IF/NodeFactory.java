package edu.udel.cis.vsl.abc.ast.node.IF;

import java.beans.Expression;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.ArrayDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.FieldDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnsuresNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.RequiresNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.AlignOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ArrowNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CharacterConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DerivativeExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DotNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.EnumerationConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FloatingConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode.Quantifier;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RemoteExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ScopeOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.StringLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.SwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpFunctionReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSymbolReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode.OmpWorksharingNodeKind;
//import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.GotoNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.JumpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.AtomicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.BasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.PointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypedefNameNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.ExecutionCharacter;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.StringLiteral;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * <p>
 * The factory used to construct the nodes of Abstract Syntax Trees.
 * </p>
 * 
 * <p>
 * The user constructs the nodes of an AST using the methods in this class.
 * These nodes have the structure of a tree, the root node being the node
 * representing the translation unit or program; the children of the root node
 * correspond to the "external definitions" of the unit. Once these have been
 * constructed, the {@link ASTFactory#newAST} method is invoked on the root node
 * to actually construct the AST object. This performs a number of analyses and
 * stores additional information about the AST. A number of errors can be
 * detected and reported at this stage. Among other things, this also computes
 * the abstract "type" of every variable, function, and expression. It also
 * computes the scope and linkage of all identifiers.
 * </p>
 * 
 * <p>
 * After the AST is created, the AST (and all of its nodes) become immutable.
 * Every node has an "owner" (originally <code>null</code>), which is set to the
 * new AST object at this time. If you want to modify the tree, you must first
 * invoke the {@link AST#release()} method, which frees the nodes from ownership
 * by the AST object, setting the "owner" fields again to <code>null</code>.
 * They can then be modified, and then {@link ASTFactory#newAST} called again to
 * re-analylze and re-build an AST. Alternatively, you can also clone the tree,
 * if you want to keep the old AST around for some reason.
 * </p>
 * 
 * <p>
 * Finally, one or more ASTs can be combined to form a complete "program" using
 * the newProgram method. This corresponds to "linking" in the usual compiler
 * sense. (Not yet implemented.)
 * </p>
 * 
 * @author siegel
 * 
 */
public interface NodeFactory {

	/**
	 * Creates an attribute slot for all AST nodes. This is a mechanism for
	 * extending the functionality of nodes. This may be used to hang any kind
	 * of data onto nodes. Initially, the attribute value associated to the new
	 * key will be null in every node.
	 * 
	 * @param attributeName
	 *            a name for the new attribute, unique among all attribute names
	 * @param attributeClass
	 *            the class to which attribute values of the new kind will
	 *            belong
	 * @return a new attribute key which can be used to assign attribute values
	 *         to nodes
	 */
	AttributeKey newAttribute(String attributeName,
			Class<? extends Object> attributeClass);

	/**
	 * Creates a new sequence node, i.e., a node which has some finite ordered
	 * sequence of children belonging to a particular class.
	 * 
	 * @param source
	 *            source information for the whole sequence
	 * @param name
	 *            a name to use when printing this sequence node
	 * @param nodes
	 *            a list of nodes that will form the children of the new
	 *            sequence node
	 * @return the new sequence node with the children set
	 */
	<T extends ASTNode> SequenceNode<T> newSequenceNode(Source source,
			String name, List<T> nodes);

	/**
	 * Creates a new ordered pair node, i.e., a node with exactly two children
	 * belonging to two specific classes.
	 * 
	 * @param node1
	 *            the first child node
	 * @param node2
	 *            the second child node
	 * @return the new pair node with the children set
	 */
	<S extends ASTNode, T extends ASTNode> PairNode<S, T> newPairNode(
			Source source, S node1, T node2);

	// Identifiers...

	/**
	 * Constructs and returns a new identifier node with given source object and
	 * name.
	 * 
	 * @param source
	 *            source information for the identifier use
	 * @param name
	 *            the name of this identifier
	 * @return a new identifier node
	 */
	IdentifierNode newIdentifierNode(Source source, String name);

	// Type Nodes ...

	/**
	 * Returns a new type node for a basic type.
	 * 
	 * @param source
	 *            source information for the occurrence of the basic type
	 * @param kind
	 *            the kind of the basic type
	 * @return the new basic type node
	 */
	BasicTypeNode newBasicTypeNode(Source source, BasicTypeKind kind);

	/**
	 * Returns a new void type node.
	 * 
	 * @param source
	 *            source information for the occurrence of "void"
	 * @return the new void type node
	 */
	TypeNode newVoidTypeNode(Source source);

	/**
	 * Constructs and returns a new enumeration type node.
	 * 
	 * @param source
	 *            source information for the occurrence of the enumeration type
	 * @param tag
	 *            the enumeration tag, i.e., the name of the enumeration, the
	 *            string that follows <code>enum</code>
	 * @param enumerators
	 * @return the new enumeration type node
	 */
	EnumerationTypeNode newEnumerationTypeNode(Source source,
			IdentifierNode tag,
			SequenceNode<EnumeratorDeclarationNode> enumerators);

	/**
	 * Constructs and returns a new array type node.
	 * 
	 * @param source
	 *            source information for the occurrence of the array type
	 * @param elementType
	 *            the node representing the element type
	 * @param extent
	 *            the node representing the expression in square brackets, i.e.,
	 *            the array length or "extent"
	 * @return the new array type node
	 */
	ArrayTypeNode newArrayTypeNode(Source source, TypeNode elementType,
			ExpressionNode extent);

	/**
	 * Constructs and returns a new atomic type node.
	 * 
	 * @param source
	 *            the source information for the occurrence of the atomic type
	 * @param baseType
	 *            the base type, i.e., the type modified by the "atomic"
	 * @return the new atomic type node
	 */
	AtomicTypeNode newAtomicTypeNode(Source source, TypeNode baseType);

	/**
	 * Constructs and returns a new pointer type node.
	 * 
	 * @param source
	 *            source information for the occurrence of the pointer type
	 * @param referencedType
	 *            the type pointed to
	 * @param scopeModifier
	 *            optional scope modifier expression (a CIVL-C construct)
	 * @return the new pointer type node
	 */
	PointerTypeNode newPointerTypeNode(Source source, TypeNode referencedType,
			ExpressionNode scopeModifier);

	/**
	 * Constructs and returns a new structure or union type node.
	 * 
	 * @param source
	 *            source information for the occurrence of the structure or
	 *            union type node
	 * @param isStruct
	 *            <code>true</code> for a structure type, <code>false</code> for
	 *            a union type
	 * @param tag
	 *            the tag of the structure or union, i.e., the string that
	 *            follows <code>struct</code> or <code>union</code>. Maybe
	 *            <code>null</code>.
	 * @param structDeclList
	 *            the sequence of field declarations; may be <code>null</code>
	 * @return the new structure or union type node
	 */
	StructureOrUnionTypeNode newStructOrUnionTypeNode(Source source,
			boolean isStruct, IdentifierNode tag,
			SequenceNode<FieldDeclarationNode> structDeclList);

	/**
	 * Constructs and returns a new function type node.
	 * 
	 * @param source
	 *            source information for the occurrence of the function type
	 * @param returnType
	 *            the node representing the return type of the function type
	 * @param formals
	 *            the sequence of formal parameter declaration nodes for the
	 *            function type
	 * @param hasIdentifierList
	 *            <code>true</code> if the function is declared using an
	 *            identifier list (i.e., without types associated to the
	 *            parameters); <code>false</code> if the function is declared
	 *            with a parameter declaration list
	 * @return the function type node
	 */
	FunctionTypeNode newFunctionTypeNode(Source source, TypeNode returnType,
			SequenceNode<VariableDeclarationNode> formals,
			boolean hasIdentifierList);

	/**
	 * Returns a new scope type node ("<code>$scope</code>"). This is a CIVL-C
	 * type.
	 * 
	 * @param source
	 *            source information for the occurrence of <code>$scope</code>
	 * @return the new instance of scope type
	 */
	TypeNode newScopeTypeNode(Source source);

	/**
	 * Returns a new instance of a typedef name node. This is a use of a typedef
	 * name. The source is the same as that of the identifier name.
	 * 
	 * @param name
	 *            the identifier node representing the use of the typedef name
	 * @param scopeList
	 *            optional CIVL-C construct: list of scope parameters used to
	 *            instantiate a scope-parameterized typedef
	 * @return the new typedef name node wrapping the given identifier node
	 */
	TypedefNameNode newTypedefNameNode(IdentifierNode name,
			SequenceNode<ExpressionNode> scopeList);

	// Expressions...

	/**
	 * If the expression can be evaluated statically to yield a constant value,
	 * this method returns that value, else it returns null.
	 * 
	 * Every "constant expression" will yield a (non-null) value, but other
	 * expressions not strictly considered "constant expressions" may also yield
	 * non-null constant values. Hence if method isConstantExpression() returns
	 * true, this method should return a non-null value; if
	 * isConstantExpression() returns false, this method may or may not return a
	 * non-null value.
	 * 
	 * @param expression
	 *            an expression node
	 * @return the constant value obtained by evaluating this expression, or
	 *         null if the expression cannot be evaluated
	 */
	Value getConstantValue(ExpressionNode expression) throws SyntaxException;

	/**
	 * If for some reason you know what the constant value of a node is supposed
	 * to be, tell it by invoking this method.
	 * 
	 * @param expression
	 *            the expression node that has been determined to have a
	 *            constant value
	 * @param value
	 *            the constant vale to associate to that expression node
	 */
	void setConstantValue(ExpressionNode expression, Value value);

	// Constant and literal expressions ...

	/**
	 * Returns a new character constant node. A character constant is a literal
	 * charcter in a program, something like <code>'a'</code>. C distinguishes
	 * between characters in the source code, and "execution characters" which
	 * are encoded in various ways by source code elements. Unicode characters
	 * can all be encoded using appropriate escape sequences.
	 * 
	 * @param source
	 *            the source information for the occurrence of the character
	 *            constant
	 * @param representation
	 *            the way the character literal actually appears in the program
	 *            source code
	 * @param character
	 *            the execution character represented by the character constant
	 * @return the new character constant node
	 */
	CharacterConstantNode newCharacterConstantNode(Source source,
			String representation, ExecutionCharacter character);

	/**
	 * Constructs a new string literal node. A string literal occurs in the
	 * program source code as <code>"..."</code>.
	 * 
	 * @param source
	 *            the source information for the occurrence of the string
	 *            literal. The string literal is usually a single token.
	 * @param representation
	 *            the way the string literal actually appears in the program
	 *            source code, with escape sequences intact
	 * @param literal
	 *            the string literal object obtained by interpreting the
	 *            representation
	 * @return the new string literal node
	 */
	StringLiteralNode newStringLiteralNode(Source source,
			String representation, StringLiteral literal);

	/**
	 * Constructs a new integer constant node. An integer constant is an
	 * occurrence of a literal integer in the source, which encodes a concrete
	 * integer value. The C11 Standard specifies the format for integer
	 * constants, which includes various letter suffixes that can occur at the
	 * end of the constant, in Sec. 6.4.4.1. The integer constant value is
	 * constructed by interpreting the representation.
	 * 
	 * @param source
	 *            the source information for the integer constant
	 * @param representation
	 *            the way the integer actually appears in the program source
	 *            code
	 * @return the new integer constant node
	 * @throws SyntaxException
	 *             if the representation does not conform to the format
	 *             specified in the C11 Standard
	 */
	IntegerConstantNode newIntegerConstantNode(Source source,
			String representation) throws SyntaxException;

	/**
	 * Constructs a new floating constant node. A floating constant is an
	 * occurrence of a literal floating point number in the source, which
	 * encodes a concrete floating point value. The C11 Standard specifies the
	 * format for floating constants, which includes various letter suffixes
	 * that can occur at the end of the constant, in Sec. 6.4.4.2. The floating
	 * constant value is constructed by interpreting the representation.
	 * 
	 * @param source
	 *            the source information for the floating constant
	 * @param representation
	 *            the way the floating constant actually appears in the program
	 *            source code
	 * @return the new floating constant node
	 * @throws SyntaxException
	 *             if the representation does not conform to the format
	 *             specified in the C11 Standard
	 */
	FloatingConstantNode newFloatingConstantNode(Source source,
			String representation) throws SyntaxException;

	/**
	 * Constructs a new enumeration constant node. This represents an occurrence
	 * of an enumeration constant, i.e., a use of a previously declared
	 * enumerator, in the program. This node just wraps an identifier node. The
	 * source is same as that of identifier.
	 * 
	 * @param name
	 *            the identifier node which is the occurrence of the enumeration
	 *            constant
	 * @return the new enumeration constant node
	 */
	EnumerationConstantNode newEnumerationConstantNode(IdentifierNode name);

	/**
	 * Returns a new compound literal node. A compound literal is a C construct
	 * used to represent a literal array, structure, or union value. Compound
	 * literals are described in the C11 Standard in Secs. 6.5.2.5 and 6.7.9.
	 * 
	 * From Sec. 6.5.2, the syntax is:
	 * 
	 * <pre>
	 * ( type-name ) { initializer-list }
	 * ( type-name ) { initializer-list , }
	 * </pre>
	 * 
	 * and from Sec. 6.7.9:
	 * 
	 * <pre>
	 * initializer:
	 *   assignment-expression
	 *   { initializer-list } { initializer-list , }
	 * 
	 * initializer-list:
	 *   designationopt initializer
	 *   initializer-list , designationopt initializer
	 * 
	 * designation:
	 *   designator-list =
	 * 
	 * designator-list:
	 *   designator
	 *   designator-list designator
	 * 
	 * designator:
	 *   [ constant-expression ]
	 *   . identifier
	 * </pre>
	 * 
	 * 
	 * @param source
	 *            source information for the entire compound literal construct
	 * @param typeNode
	 *            node representing the type name portion of the compound
	 *            literal
	 * @param initializerList
	 *            node representing the initializer list portion of the compound
	 *            literal
	 * @return the new compound literal node
	 */
	CompoundLiteralNode newCompoundLiteralNode(Source source,
			TypeNode typeNode, CompoundInitializerNode initializerList);

	/**
	 * Constructs a new node representing a CIVL-C boolean constant, "
	 * <code>$true</code>" or "<code>$false</code>".
	 * 
	 * @param source
	 *            source information for the occurrence of the boolean constant
	 * @param value
	 *            <code>true</code> for <code>$true</code>, <code>false</code>
	 *            for <code>$false</code>
	 * @return the new boolean constant node
	 */
	ConstantNode newBooleanConstantNode(Source source, boolean value);

	/**
	 * Constructs a new node representing an occurrence of the CIVL-C "self"
	 * process constant, written "<code>$self</code>".
	 * 
	 * @param source
	 *            source information for the occurrence of the constant
	 *            <code>$self</code>
	 * @return the new expression node representing the constant
	 */
	ExpressionNode newSelfNode(Source source);

	/**
	 * Constructs a new node representing an occurrence of the CIVL-C
	 * "null process" constant, written <code>$proc_null</code>.
	 * 
	 * @param source
	 *            source information for the occurrence of the constant
	 *            <code>$proc_null</code>
	 * @return the new expression node representing the constant
	 */
	ExpressionNode newProcnullNode(Source source);

	/**
	 * Constructs a new node representing an occurrence of the CIVL-C expression
	 * "<code>$result</code>", used in function constracts to represent the
	 * result returned by the function.
	 * 
	 * @param source
	 *            source information for the occurrence of the expression
	 *            <code>$result</code>
	 * @return the new expression node
	 */
	ExpressionNode newResultNode(Source source);

	// Other Expressions...

	/**
	 * Constructs a new identifier expression node. This is an expression node
	 * which just wraps an identifier. Idenfiers can be used as expressions in
	 * various ways in C: a variable or the name of a function, for example.
	 * 
	 * The source is not necessarily the same as the identifier because you
	 * might want to include surrounding parentheses in the expression.
	 * 
	 * @param identifier
	 *            the identifier node being wrapped
	 * @return the new identifier expression node
	 */
	IdentifierExpressionNode newIdentifierExpressionNode(Source source,
			IdentifierNode identifier);

	/**
	 * Constructs a new "align-of" node. This represents an occurrence of the
	 * C11 construct <code>_Alignof(typename)</code>. See C11 Sec. 6.5.3.4. The
	 * value is considered an integer constant, i.e., it is known at
	 * compile-time.
	 * 
	 * @param source
	 *            source information for the occurrence of the expression
	 * @param type
	 *            the type name portion of the expression
	 * @return the new align-of node
	 */
	AlignOfNode newAlignOfNode(Source source, TypeNode type);

	/**
	 * Constructs a new cast node. This represents an occurrence of a cast
	 * expression <code>(typename)argument</code>.
	 * 
	 * @param source
	 *            source information for the occurrence of the complete cast
	 *            expression
	 * @param type
	 *            node representing the type name
	 * @param argument
	 *            the argument part of the expression
	 * @return the new cast node
	 */
	CastNode newCastNode(Source source, TypeNode type, ExpressionNode argument);

	FunctionCallNode newFunctionCallNode(Source source,
			ExpressionNode function, List<ExpressionNode> arguments,
			SequenceNode<ExpressionNode> scopeList);

	DotNode newDotNode(Source source, ExpressionNode structure,
			IdentifierNode fieldName);

	ArrowNode newArrowNode(Source source, ExpressionNode structurePointer,
			IdentifierNode fieldName);

	OperatorNode newOperatorNode(Source source, Operator operator,
			List<ExpressionNode> arguments);

	SizeofNode newSizeofNode(Source source, SizeableNode argument);

	SpawnNode newSpawnNode(Source source, FunctionCallNode callNode);

	/**
	 * A remote expression node, representing an expression of the form
	 * "proc_expr@x". This refers to a variable in the process p referenced by
	 * the expression proc_expr. The static variable x can be determined
	 * statically now. Later it will be evaluated in a dynamic state in p's
	 * context.
	 */
	RemoteExpressionNode newRemoteExpressionNode(Source source,
			ExpressionNode left, IdentifierExpressionNode right);

	/**
	 * Creates a new collective expression node. This expression can be used in
	 * an assertion to form a collective assertion. It can also be used in an
	 * assume statement, a loop invariant, or a procedure contract.
	 * 
	 * The set of processes over which this collective expression spans is
	 * specified by an array whose elements have type \proc.
	 * 
	 * @param source
	 *            the source code elements
	 * @param processPointerExpression
	 *            a pointer to the first element of an array of process
	 *            references
	 * @param lengthExpression
	 *            the number of processes in the array
	 * @param body
	 *            the expression to be interpreted in the collective context
	 * @return the new collective expression node with given children
	 */
	CollectiveExpressionNode newCollectiveExpressionNode(Source source,
			ExpressionNode processPointerExpression,
			ExpressionNode lengthExpression, ExpressionNode body);

	ScopeOfNode newScopeOfNode(Source source,
			IdentifierExpressionNode variableExpression);

	/**
	 * 
	 * @param source
	 *            The source code elements.
	 * @param quantifier
	 *            The quantifier. One of {EXISTS, FORALL, UNIFORM}.
	 * @param variable
	 *            The quantified variable.
	 * @param restriction
	 *            A boolean-valued expression that holds true when the
	 *            quantified variable is in the domain.
	 * @param expression
	 *            The quantified expression.
	 * @return The new quantified expression with the given children.
	 */
	QuantifiedExpressionNode newQuantifiedExpressionNode(Source source,
			Quantifier quantifier, VariableDeclarationNode variable,
			ExpressionNode restriction, ExpressionNode expression);

	/**
	 * 
	 * @param source
	 *            The source code elements.
	 * @param quantifier
	 *            The quantifier. One of {EXISTS, FORALL, UNIFORM}.
	 * @param variable
	 *            The quantified variable.
	 * @param lower
	 *            Integer-valued expression for the lower bound on the
	 *            quantified variable.
	 * @param upper
	 *            Integer-valued expression for the upper bound on the
	 *            quantified variable.
	 * @param expression
	 *            The quantified expression.
	 * @return The new quantified expression with the given children.
	 */
	QuantifiedExpressionNode newQuantifiedExpressionNode(Source source,
			Quantifier quantifier, VariableDeclarationNode variable,
			ExpressionNode lower, ExpressionNode upper,
			ExpressionNode expression);

	/**
	 * 
	 * @param source
	 *            The source code elements.
	 * @param function
	 *            The abstract function whose derivative is being taken.
	 * @param partials
	 *            The list of partial derivatives.
	 * @param arguments
	 *            The arguments to the uninterpreted function evaluation.
	 * @return The new derivative expression with the given children.
	 */
	DerivativeExpressionNode newDerivativeExpressionNode(
			Source source,
			ExpressionNode function,
			SequenceNode<PairNode<IdentifierExpressionNode, IntegerConstantNode>> partials,
			SequenceNode<ExpressionNode> arguments);

	// Declarations...

	/**
	 * Returns a new declaration of an "object" variable with no initializer.
	 * 
	 * @param source
	 *            the source information for the variable declaration
	 * @param name
	 *            the identifier node corresponding to the name of the variable
	 *            in its declaration
	 * @param type
	 *            the node corresponding to the type in the declaration
	 * @return the new variable declaration node with the given chidren
	 */
	VariableDeclarationNode newVariableDeclarationNode(Source source,
			IdentifierNode name, TypeNode type);

	/**
	 * Returns a new declaration for an "object" variable with an initializer.
	 * 
	 * @param name
	 *            identifier being declared
	 * @param type
	 *            the type
	 * @param initializer
	 *            optional initializer (for variables only) or null
	 * @return a new declaration for an "ordinary identifier"
	 */
	VariableDeclarationNode newVariableDeclarationNode(Source source,
			IdentifierNode name, TypeNode type, InitializerNode initializer);

	/**
	 * Returns a new function declaration with no body (so it is not a function
	 * "definition").
	 * 
	 * @param source
	 *            source information for this declaration
	 * @param name
	 *            the identifier node for the name of this function
	 * @param type
	 *            node representing the type of the function
	 * @param contract
	 *            sequence of contract elements or <code>null</code>
	 * @return the new function declaration node formed from given children
	 */
	FunctionDeclarationNode newFunctionDeclarationNode(Source source,
			IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract);

	EnumeratorDeclarationNode newEnumeratorDeclarationNode(Source source,
			IdentifierNode name, ExpressionNode value);

	FieldDeclarationNode newFieldDeclarationNode(Source source,
			IdentifierNode name, TypeNode type);

	FieldDeclarationNode newFieldDeclarationNode(Source source,
			IdentifierNode name, TypeNode type, ExpressionNode bitFieldWidth);

	OrdinaryLabelNode newStandardLabelDeclarationNode(Source source,
			IdentifierNode name, StatementNode statement);

	SwitchLabelNode newCaseLabelDeclarationNode(Source source,
			ExpressionNode constantExpression, StatementNode statement);

	SwitchLabelNode newDefaultLabelDeclarationNode(Source source,
			StatementNode statement);

	/**
	 * Returns a new typedef declaration. If the typedef was scope
	 * parameterized, the type argument will be a ScopeParameterizedTypeNode.
	 * 
	 * @param source
	 *            source code reference
	 * @param name
	 *            the name of the typedef as an IdentifierNode
	 * @param type
	 *            the type node being bound to the identifier (this may be scope
	 *            parameterized)
	 * @return a new typedef declaration node
	 */
	TypedefDeclarationNode newTypedefDeclarationNode(Source source,
			IdentifierNode name, TypeNode type);

	CompoundInitializerNode newCompoundInitializerNode(Source source,
			List<PairNode<DesignationNode, InitializerNode>> initList);

	DesignationNode newDesignationNode(Source source,
			List<DesignatorNode> designators);

	FieldDesignatorNode newFieldDesignatorNode(Source source,
			IdentifierNode name);

	ArrayDesignatorNode newArrayDesignatorNode(Source source,
			ExpressionNode index);

	ScopeParameterizedDeclarationNode newScopeParameterizedDeclarationNode(
			Source source, SequenceNode<VariableDeclarationNode> scopeList,
			DeclarationNode baseDeclaration);

	// Statements...

	CompoundStatementNode newCompoundStatementNode(Source source,
			List<BlockItemNode> items);

	/**
	 * Constructs a new expression statement node. This is a statement which
	 * wraps an expression. The source is the same as that of the expression.
	 * 
	 * @param expression
	 *            the expression node
	 * @return the new expression statement node wrapping that expression
	 */
	ExpressionStatementNode newExpressionStatementNode(ExpressionNode expression);

	StatementNode newNullStatementNode(Source source);

	/**
	 * Constructs a new <code>for</code> loop node.
	 * 
	 * @param initializer
	 *            the initializer part of the <code>for</code> loop, an
	 *            {@link Expression} or another instance of
	 *            {@link ForLoopInitializerNode}, such as one produced from a
	 *            list of delcarations; may be <code>null</code>
	 * @param condition
	 *            the condition part of the <code>for</code> loop
	 * @param incrementer
	 *            the incrementer part of the <code>for</code> loop
	 * @param body
	 *            the body of the <code>for</code> loop
	 * @param invariant
	 *            loop invariant: may be <code>null</code>
	 * @return the new <code>for</code> loop node
	 */
	ForLoopNode newForLoopNode(Source source,
			ForLoopInitializerNode initializer, ExpressionNode condition,
			ExpressionNode incrementer, StatementNode body,
			ExpressionNode invariant);

	ForLoopInitializerNode newForLoopInitializerNode(Source source,
			List<VariableDeclarationNode> declarations);

	LoopNode newWhileLoopNode(Source source, ExpressionNode condition,
			StatementNode body, ExpressionNode invariant);

	LoopNode newDoLoopNode(Source source, ExpressionNode condition,
			StatementNode body, ExpressionNode invariant);

	GotoNode newGotoNode(Source source, IdentifierNode label);

	/**
	 * Creates new <code>if</code> statement node when there is no false
	 * ("else") branch.
	 * 
	 * @param source
	 *            source information for the statement
	 * @param condition
	 *            the condition expression
	 * @param trueBranch
	 *            the body of the <code>if</code> statement
	 * @return the new <code>if</code> statement node formed from given children
	 */
	IfNode newIfNode(Source source, ExpressionNode condition,
			StatementNode trueBranch);

	/**
	 * Creates a new <code>if</code> statement node. False branch may be null if
	 * there is no "else" clause.
	 * 
	 * @param condition
	 *            the branch condition
	 * @param trueBranch
	 *            the statement for the "true" branch
	 * @param falseBranch
	 *            the statement for the "false" branch
	 * @return the new <code>if</code> statement node
	 */
	IfNode newIfNode(Source source, ExpressionNode condition,
			StatementNode trueBranch, StatementNode falseBranch);

	JumpNode newContinueNode(Source source);

	JumpNode newBreakNode(Source source);

	/**
	 * Creates a new <code>return</code> statement node. Argument may be
	 * <code>null</code>.
	 * 
	 * @param argument
	 *            the expression being returned or <code>null</code> if there is
	 *            none
	 * @return the new <code>return</code> statement node
	 */
	ReturnNode newReturnNode(Source source, ExpressionNode argument);

	LabeledStatementNode newLabeledStatementNode(Source source,
			LabelNode label, StatementNode statement);

	SwitchNode newSwitchNode(Source source, ExpressionNode condition,
			StatementNode body);

	AssumeNode newAssumeNode(Source source, ExpressionNode expression);

	WhenNode newWhenNode(Source source, ExpressionNode guard, StatementNode body);

	ChooseStatementNode newChooseStatementNode(Source source,
			List<StatementNode> statements);

	// misc. nodes ...

	StaticAssertionNode newStaticAssertionNode(Source source,
			ExpressionNode expression, StringLiteralNode message);

	PragmaNode newPragmaNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken newlineToken);

	RequiresNode newRequiresNode(Source source, ExpressionNode expression);

	EnsuresNode newEnsuresNode(Source source, ExpressionNode expression);

	// external definitions...

	FunctionDefinitionNode newFunctionDefinitionNode(Source source,
			IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract, CompoundStatementNode body);

	/**
	 * Creates a new abstract function definition.
	 * 
	 * @param source
	 *            The source information for the abstract function definition.
	 * @param name
	 *            The name of the abstract function.
	 * @param type
	 *            The function type with the appropriate parameters and return
	 *            type.
	 * @param contract
	 *            Any code contract associated with the function.
	 * @param continuity
	 *            The number of derivatives that may be taken.
	 * @return An abstract function definition with the specified properties.
	 */
	AbstractFunctionDefinitionNode newAbstractFunctionDefinitionNode(
			Source source, IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract, int continuity);

	ASTNode newTranslationUnitNode(Source source,
			List<ExternalDefinitionNode> definitions);

	ValueFactory getValueFactory();

	/**
	 * Creates a new atomic statement node with the data provided.
	 * 
	 * @param statementSource
	 *            The source information of the node
	 * @param deterministic
	 *            The flag to denote whether the atomic node is $atom or $atomic
	 * @param body
	 *            The body statement node of the atomic node
	 * @return The new atomic statement node
	 */
	AtomicNode newAtomicStatementNode(Source statementSource,
			boolean deterministic, StatementNode body);

	/**
	 * Creates a new constant expression node representing <code>$here</code>.
	 * 
	 * @param source
	 *            The source code element of the new node
	 * @return a new constant expression node representing <code>$here</code>
	 */
	ExpressionNode newHereNode(Source source);

	/**
	 * Creates a new constant expression node representing <code>$root</code>.
	 * 
	 * @param source
	 *            The source code element of the new node
	 * @return a new constant expression node representing <code>$root</code>
	 */
	ExpressionNode newRootNode(Source source);

	/**
	 * Creates a new expression node representing <code>$scopeof(expr)</code>.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param argument
	 *            The argument of the <code>$scopeof(expr)</code> expression.
	 * @return a new constant expression node representing <code>$here</code>
	 */
	ScopeOfNode newScopeOfNode(Source source, ExpressionNode argument);

	/**
	 * Creates a new OpenMP parallel node, representing
	 * <code>#pragma omp parallel...</code>. The clauses of the node can be
	 * updated by calling the corresponding setters, e.g, setStatementNode(),
	 * setPrivateList(), etc.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the parallel construct.
	 * @return The new OpenMP parallel statement node created.
	 */
	OmpParallelNode newOmpParallelNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP for node, representing
	 * <code>#pragma omp for...</code>. The clauses of the node can be updated
	 * by calling the corresponding setters, e.g, setStatementNode(),
	 * setPrivateList(), etc.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the parallel construct.
	 * @return The new OpenMP for node created.
	 */
	OmpForNode newOmpForNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP master node, representing
	 * <code>#pragma omp master...</code>. A master node has exactly one child
	 * node, i.e., the statement node corresponding to the block affected by the
	 * master construct. The syntax of the master construct is:<br>
	 * <code>#pragma omp master new-line <br>
	 * structured-block</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the master construct.
	 * @return The new OpenMP master node created.
	 */
	OmpSyncNode newOmpMasterNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP critical node, representing
	 * <code>#pragma omp critical...</code>. A critical node has at most two
	 * children the name of the critical section and the statement node
	 * corresponding to the block affected by the critical construct. The syntax
	 * of the critical construct is:<br>
	 * <code>#pragma omp critical [(name)] new-line <br>
	 * structured-block</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param name
	 *            The name of the critical section.
	 * @param statement
	 *            The statement node of the critical construct.
	 * @return The new OpenMP critical node created.
	 */
	OmpSyncNode newOmpCriticalNode(Source source, IdentifierNode name,
			StatementNode statement);

	/**
	 * Creates a new OpenMP barrier node, representing
	 * <code>#pragma omp barrier...</code>. A barrier node has NO child node.
	 * The syntax of the barrier construct is:<br>
	 * <code>#pragma omp barrier new-line</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @return The new OpenMP barrier node created.
	 */
	OmpSyncNode newOmpBarrierNode(Source source);

	/**
	 * Creates a new OpenMP barrier node, representing
	 * <code>#pragma omp flush...</code>. A flush node has at most one child
	 * node: the list of variables of the flush operation. The syntax of the
	 * flush construct is:<br>
	 * <code>#pragma omp flush [(list)] new-line</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param variables
	 *            The list of variables of the flush operation.
	 * @return The new OpenMP flush node created.
	 */
	OmpSyncNode newOmpFlushNode(Source source,
			SequenceNode<IdentifierExpressionNode> variables);

	/**
	 * Creates a new OpenMP ordered node, representing
	 * <code>#pragma omp ordered...</code>. An ordered node has exactly one
	 * child node, i.e., the statement node corresponding to the block affected
	 * by the ordered construct. The syntax of the ordered construct is:<br>
	 * <code>#pragma omp ordered new-line<br>
	 * structured-block</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the ordered construct.
	 * @return The new OpenMP ordered node created.
	 */
	OmpSyncNode newOmpOrederedNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP sections node, representing
	 * <code>#pragma omp sections...</code>. The clauses of the node can be
	 * updated by calling the corresponding setters, e.g, setStatementNode(),
	 * setPrivateList(), etc.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the ordered construct.
	 * @return The new OpenMP sections statement node created.
	 */
	OmpWorksharingNode newOmpSectionsNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP section node, representing
	 * <code>#pragma omp section...</code>. A section node has exactly one child
	 * node, i.e., the statement node corresponding to the block affected by the
	 * section construct. The syntax of the section construct is:<br>
	 * <code>#pragma omp section new-line <br>
	 * structured-block</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the section construct.
	 * @return The new OpenMP section node created.
	 */
	OmpWorksharingNode newOmpSectionNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP single node, representing
	 * <code>#pragma omp single...</code>. The syntax of the single construct is
	 * as follows:<br>
	 * <code>#pragma omp single [clause[[,] clause] ...] new-line<br>
	 * structured-block</code><br>
	 * where clause is one of the following:<br>
	 * <code>private(list) </code><br>
	 * <code>firstprivate(list)  </code><br>
	 * <code>copyprivate(list)</code><br>
	 * <code>nowait</code>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param statement
	 *            The statement node of the section construct.
	 * @return The new OpenMP single node created.
	 */
	OmpWorksharingNode newOmpSingleNode(Source source, StatementNode statement);

	/**
	 * Creates a new OpenMP threadprivate node.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param variables
	 *            The list of variables declared by the clause.
	 * @return The new OpenMP threadprivate node created.
	 */
	OmpDeclarativeNode newOmpThreadprivateNode(Source source,
			SequenceNode<IdentifierExpressionNode> variables);

	/**
	 * Creates a new OpenMP reduction node with a standard operator.
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param operator
	 *            The operator of the reduction node.
	 * @param variables
	 *            The variables of the reduction clause.
	 * @return The new OpenMP reduction node.
	 */
	OmpSymbolReductionNode newOmpSymbolReductionNode(Source source,
			Operator operator, SequenceNode<IdentifierExpressionNode> variables);

	/**
	 * Creates a new OpenMP reduction node with an identifier operator (i.e.,
	 * function names).
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param function
	 *            The name of the function of the reduction node.
	 * @param variables
	 *            The variables of the reduction clause.
	 * @return The new OpenMP reduction node.
	 */
	OmpFunctionReductionNode newOmpFunctionReductionNode(Source source,
			IdentifierExpressionNode function,
			SequenceNode<IdentifierExpressionNode> variables);

	/**
	 * Creates a new OpenMP worksharing node with a specific kind. The kind
	 * could be:
	 * <ul>
	 * <li>SECTIONS</li>
	 * <li>SINGLE</li>
	 * <li>SECTION</li>
	 * <li>FOR</li>
	 * </ul>
	 * 
	 * @param source
	 *            The source code element of the new node.
	 * @param kind
	 *            The kind of the worksharing node, either FOR, SECTIONS,
	 *            SECTION or SINGLE.
	 * @return The new OpenMP worksharing node.
	 */
	OmpWorksharingNode newWorksharingNode(Source source,
			OmpWorksharingNodeKind kind);
}
