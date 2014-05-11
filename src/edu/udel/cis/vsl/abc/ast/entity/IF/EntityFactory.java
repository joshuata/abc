package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.LinkageKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;

/**
 * A factory for producing instances of {@link Entity}, and some related utility
 * methods.
 * 
 * @author siegel
 * 
 */
public interface EntityFactory {

	/**
	 * Creates a new scope.
	 * 
	 * @param kind
	 *            the kind of scope to create
	 * @param parent
	 *            the scope immediately containing the new scope; can be null
	 *            for the root scope
	 * @param root
	 *            an AST node to associate to the new scope; it is used only for
	 *            printing to make it easy for the reader to identify where the
	 *            scope comes from
	 * @return the new Scope
	 */
	Scope newScope(ScopeKind kind, Scope parent, ASTNode root);

	/**
	 * Creates a new {@link Variable}.
	 * 
	 * @param name
	 *            the name of the variable
	 * @param linkage
	 *            the kind of linkage the variable has
	 * @param type
	 *            the type of the variable
	 * @return the new variable
	 */
	Variable newVariable(String name, LinkageKind linkage, Type type);

	/**
	 * Creates a new {@link Function}.
	 * 
	 * @param name
	 *            the name of the function
	 * @param linkage
	 *            the kind of linkage the function has
	 * @param type
	 *            the type of the function
	 * @return the new function
	 */
	Function newFunction(String name, LinkageKind linkage, Type type);

	/**
	 * <p>
	 * Creates a new structure or union entity. These are in 1-1 correspondence
	 * with the structure or union types.
	 * </p>
	 * 
	 * <p>
	 * The usual order of events for constructing a structure or union is as
	 * follows: first, the incomplete structure or union type is formed. Then
	 * the structure or union entity is formed using this method. Then the
	 * fields are formed using method
	 * {@link #newField(FieldDeclarationNode, ObjectType, Value, StructureOrUnion)}
	 * . Finally, the structure or union type is completed using method
	 * {@link StructureOrUnionType#complete(java.util.List)}.
	 * </p>
	 * 
	 * @param type
	 *            the structure or union type
	 * @return the new structure or union entity
	 */
	StructureOrUnion newStructureOrUnion(StructureOrUnionType type);

	/**
	 * Creates a new enumeration entity. These are in 1-1 correspondence with
	 * the enumeration types.
	 * 
	 * @param type
	 *            the enumeration type
	 * @return the new enumeration entity
	 */
	Enumeration newEnumeration(EnumerationType type);

	/**
	 * Creates a new enumerator entity. These correspond to the enumerators in
	 * the enumerator list of a complete enumeration definition.
	 * 
	 * @param declaration
	 *            the declaration of the enumerator in the enuemrator list
	 * @param type
	 *            the type of the enumerator (always an integer type, according
	 *            to C)
	 * @param value
	 *            the constant integer value associated to the enumerator
	 * @return the new enumerator entity
	 */
	Enumerator newEnumerator(EnumeratorDeclarationNode declaration,
			EnumerationType type, Value value);

	/**
	 * Creates a new field entity. These correspond to the field declarations in
	 * a complete structure or union definition.
	 * 
	 * @param declaration
	 *            the field declaration
	 * @param type
	 *            the type of the field
	 * @param bitWidth
	 *            the optional bit width parameter
	 * @param structureOrUnion
	 *            the structure or union entity in which this field occurs
	 * @return the new field
	 */
	Field newField(FieldDeclarationNode declaration, ObjectType type,
			Value bitWidth, StructureOrUnion structureOrUnion);

	/**
	 * Creates a new {@link Typedef} entity.
	 * 
	 * @param name
	 *            the name of the typedef
	 * @param type
	 *            the type to which the name is bound in the typedef
	 * @return the new {@link Typedef} entity
	 */
	Typedef newTypedef(String name, Type type);

	/**
	 * Creates a new label entity. This corresponds to an ordinary label
	 * "declaration" in the code. (The name of the label is followed by a colon
	 * and then a statement.)
	 * 
	 * @param declaration
	 *            the ordinary label declaration
	 * @return the new label entity
	 */
	Label newLabel(OrdinaryLabelNode declaration);

	/**
	 * Creates a new pragma handler for the given name. There is just one
	 * univeral name space for the pragma universes.
	 * 
	 * @param name
	 *            the name of the pragma universe, such as <code>omp</code>
	 * @return the pragma handler for that name
	 */
	PragmaHandler newPragmaHandler(String name);

	/**
	 * Computes the join of the two scopes in the scope tree and returns it.
	 * 
	 * @param s1
	 *            non-null scope
	 * @param s2
	 *            non-null scope
	 * @return the youngest common ancestor of s1 and s2
	 */
	Scope join(Scope s1, Scope s2);

}
