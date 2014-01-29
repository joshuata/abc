package edu.udel.cis.vsl.abc.ast.node.IF.compound;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;

/**
 * A compound literal is an abstract representation of a literal array, struct,
 * or union value in a C program.
 * 
 * Reads that go beyond the bounds of the inferred type will result in a
 * NoSuchElementException.
 * 
 * @author siegel
 * 
 */
public interface CompoundLiteralObject extends LiteralObject {

	/**
	 * Returns the number of members of this compound literal. This will be
	 * consistent with the inferred type. For example, if the inferred type is
	 * "array of T of length 5", the size will be 5. If the inferred type is
	 * "struct S ...", the size will be the number of members of S. If the
	 * inferred type is "union S ..." the size will be 1.
	 * 
	 * @return the number of members of this compound literal value
	 */
	int size();

	/**
	 * Return the index-th element of this compound literal. May be null.
	 * 
	 * @param index
	 *            integer in range [0,size-1]
	 * @return
	 */
	LiteralObject get(int index);

	/**
	 * Return the corresponding AST node of the compound literal object.
	 * 
	 * @return
	 */
	ASTNode getSourceNode();

}
