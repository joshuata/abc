package edu.udel.cis.vsl.abc.ast.entity.IF;

import java.util.Enumeration;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Enumerator;
import edu.udel.cis.vsl.abc.ast.type.IF.Field;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;

/**
 * <p>
 * An entity is an underlying program "conceptual thing" that can be named by an
 * identifier. The kinds of things include: variables, functions, typedefs,
 * structures, unions, enumerations, enumerators, fields, and labels.
 * </p>
 * 
 * <p>
 * An entity object has some list of declarations associated to it. It begins
 * with no declarations; a declaration is added using method
 * {@link #addDeclaration(DeclarationNode)}. There are also methods to get the
 * number of declarations and to get the i-th declaration.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface Entity {

	/**
	 * The different kinds of Entity.
	 * 
	 */
	public static enum EntityKind {
		/**
		 * A variable: this is the static notion of variable. A variable is
		 * named thing that can store a value. Note that a single variable can
		 * be declared multiple times in C.
		 */
		VARIABLE,
		/**
		 * A function. A function is a thing that takes inputs, executes a
		 * statement and possibly returns a result. Note that a function can be
		 * declared multiple times in C, but should be defined at most once.
		 */
		FUNCTION,
		/**
		 * A typedef, the entity corresponding to an occurrence of
		 * <code>typedef</code> in the program. The typedef binds an identifier
		 * to a type.
		 */
		TYPEDEF,
		/**
		 * A structure or union, the entity corresponding to an occurrence of
		 * <code>struct</code> or <code>union</code> in the program.
		 */
		STRUCTURE_OR_UNION,
		/**
		 * An enumeration, the entity corresponding to an occurrence of an
		 * <code>enum</code> definition in a program. Note that an enumeration
		 * can be declared multiple times, but at most one instance can be
		 * complete, i.e., contain the curly brackets with the enumerator list.
		 */
		ENUMERATION,
		/**
		 * An enumerator, the identifier that occurs in the list inside an
		 * <code>enum</code> definition. An enumerator is an enumeration
		 * constant and in C has a constant integer value.
		 */
		ENUMERATOR,
		/**
		 * A field, the entity corresponding to a field declaration in the list
		 * inside a complete <code>struct</code> or <code>union</code>
		 * definition.
		 */
		FIELD,
		/**
		 * An ordinary label, which occurs in a labeled statement of the form
		 * <code>labelName : stmt</code>. (Note that neither the
		 * <code>case</code> nor <code>default</code> constructs in a
		 * <code>switch</code> statement generate an entity.)
		 */
		LABEL,
		/**
		 * A pragma domain, named by the first token following the
		 * <code># pragma</code> in a pragma. For example, all OpenMP pragmas
		 * begin <code># pragma omp</code>; the <code>omp</code> names an entity
		 * which is the OpenMP pragma domain. Each pragma domain may have a
		 * pragma handler which is used to process pragmas of its domain.
		 */
		PRAGMA_HANDLER
	};

	/**
	 * The different kinds of linkage an entity may have: external, internal, or
	 * none. Roughly speaking, an entity with external linkage refers to a thing
	 * which may lie outside of the translation union; one with internal linkage
	 * refers to a single thing which lies in the translation unit but may be
	 * declared in multiple scopes; something with no linkage can only be
	 * declared in the one scope where it resides.
	 */
	public static enum LinkageKind {
		EXTERNAL, INTERNAL, NONE
	};

	/**
	 * <p>
	 * The kind of entity this is.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#VARIABLE}, this entity may be safely
	 * cast to {@link Variable}
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#FUNCTION}, this entity may be safely
	 * cast to {@link Function}.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#TYPEDEF}, this entity may be safely cast
	 * to {@link Typedef}.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#STRUCTURE_OR_UNION}, this entity may be
	 * safely cast to {@link StructureOrUnion}.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#ENUMERATION}, this entity may be safely
	 * cast to {@link Enumeration}.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#ENUMERATOR}, this entity may be safely
	 * cast to {@link Enumerator}. An enumerator is an element of an
	 * enumeration.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#FIELD}, this entity may be safely cast
	 * to {@link Field}. A "field" is a member of a structure or union.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#LABEL}, this entity may be safely cast
	 * to {@link Label}.
	 * </p>
	 * 
	 * <p>
	 * If the kind is {@link EntityKind#PRAGMA_HANDLER}, this entity may be
	 * safely cast to {@link PragmaHandler}.
	 * </p>
	 * 
	 * @return the entity kind
	 */
	EntityKind getEntityKind();

	/**
	 * Gets the name of this entity. This is the identifier used in the
	 * declaration of the entity. It can be null in certain situations (e.g., an
	 * unnamed field).
	 * 
	 * @return the name of this entity
	 */
	String getName();

	/**
	 * Returns an iterator over all the known delcarations of this entity. An
	 * entity may be declared multiple times. This includes the definition.
	 * 
	 * @return iterator over declarations of this entity
	 */
	Iterable<DeclarationNode> getDeclarations();

	/**
	 * Gets one of the declarations of this entity.
	 * 
	 * @return a declaration of this entity or <code>null</code> if there aren't
	 *         any
	 */
	DeclarationNode getFirstDeclaration();

	/**
	 * Returns the number of declarations of this entity.
	 * 
	 * @return the number of declarations of this entity
	 */
	int getNumDeclarations();

	/**
	 * Returns the index-th declaration of this entity.
	 * 
	 * @param index
	 *            an integer in the range [0,n), where n is the number of
	 *            declarations of this entity
	 * @return the index-th declaration of this entity
	 */
	DeclarationNode getDeclaration(int index);

	/**
	 * Adds a declaration to this entity.
	 * 
	 * @param declaration
	 *            a declaration of this entity
	 */
	void addDeclaration(DeclarationNode declaration);

	/**
	 * <p>
	 * Gets the definition, i.e., the defining declaration of this entity. Every
	 * entity has at most one definition. The definition is a declaration of a
	 * special kind. For example, for an object (variable), a definition is the
	 * declaration that allocates storage for that object. For a function, a
	 * definition is the declaration the contains the function body.
	 * </p>
	 * 
	 * <p>
	 * The definition is initially <code>null</code>, but can be set using
	 * method {@link #setDefinition(DeclarationNode)}.
	 * </p>
	 * 
	 * @return the definition of this entity or <code>null</code>
	 */
	DeclarationNode getDefinition();

	/**
	 * <p>
	 * Sets the definition for this entity. Every entity has at most one
	 * definition. The definition is a declaration of a special kind. For
	 * example, for an object (variable), a definition is the declaration that
	 * allocates storage for that object. For a function, a definition is the
	 * declaration the contains the function body.
	 * </p>
	 * 
	 * <p>
	 * The definition is initially <code>null</code>, and can be set using this
	 * method. Note that this does not affect the list of declarations of this
	 * entity. It is the client's responsibility to add the definition to the
	 * list of declarations as well as to invoke this method, to ensure that the
	 * definition occurs in the list of declarations.
	 * </p>
	 * 
	 * @param declaration
	 *            the declaration node for the definition
	 */
	void setDefinition(DeclarationNode declaration);

	/**
	 * Returns the kind of linkage this entity has.
	 * 
	 * @return the kind of linkage this entity has
	 */
	LinkageKind getLinkage();

	/**
	 * Sets the linkage of this entity. It is initially {@link LinkageKind#NONE}
	 * .
	 * 
	 * @param linkage
	 *            the linkage kind of this entity
	 */
	void setLinkage(LinkageKind linkage);

	/**
	 * <p>
	 * Other than {@link Label}, and {@link PragmaHandler}, every kind of Entity
	 * has a type, returned by this method. For a {@link Label} or
	 * {@link PragmaHandler}, this returns <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * The type is initially <code>null</code>. It can be set using method
	 * {@link #setType(Type)}.
	 * </p>
	 * 
	 * @return the type of this entity or <code>null</code>
	 */
	Type getType();

	/**
	 * Sets the type of this entity.
	 * 
	 * @param type
	 *            the type of this entity
	 */
	void setType(Type type);

	/**
	 * Is this a system-defined entity (as opposed to a user-defined one)?
	 * Examples include standard types, like <code>size_t</code>. The default is
	 * false; it can be changed using method {@link #setIsSystem(boolean)}.
	 */
	boolean isSystem();

	/**
	 * Declares that this entity is or is not a system-defined entity.
	 * 
	 * @param value
	 *            if <code>true</code>, declares this to be a system-defined
	 *            entity; if <code>false</code>, declares this to be a
	 *            user-defined entity. The default is <code>false</code>.
	 */
	void setIsSystem(boolean value);

}
