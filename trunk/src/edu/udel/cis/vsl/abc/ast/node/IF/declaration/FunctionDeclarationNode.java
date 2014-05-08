package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;

/**
 * A node representing a function declaration. This includes a function
 * prototype as well as a function definition.
 * 
 * The children include: (0) an identifier node, the name of the function; (1) a
 * type node which is the type of the function (necessarily a function type),
 * and (2) a contract node for the function contract, which may be null.
 * 
 * @author siegel
 * 
 */
public interface FunctionDeclarationNode extends OrdinaryDeclarationNode {

	@Override
	Function getEntity();

	/**
	 * Does the declaration include the <code>inline</code> function specifier?
	 * 
	 * @return true if declaration contains <code>inline</code>
	 */
	boolean hasInlineFunctionSpecifier();

	/**
	 * Set the inline function specifier bit to the given value.
	 * 
	 * @param value
	 *            if <code>true</code>, says that this function declaration
	 *            contains the <code>inline</code> specifier, if
	 *            <code>false</code>, it doesn't
	 */
	void setInlineFunctionSpecifier(boolean value);

	/**
	 * Does the declaration include the <code>_Noreturn</code> function
	 * specifier?
	 * 
	 * @return <code>true</code> iff declaration contains <code>_Noreturn</code>
	 */
	boolean hasNoreturnFunctionSpecifier();

	/**
	 * Sets the <code>_Noreturn</code> bit to the given value.
	 * 
	 * @param value
	 *            if <code>true</code>, says that this function declaration
	 *            contains the <code>_Noreturn</code> specifier, if
	 *            <code>false</code>, it doesn't
	 */
	void setNoreturnFunctionSpecifier(boolean value);

	/**
	 * Returns the contract node for this function declaration. May be
	 * <code>null</code>. It is a child node of this node.
	 * 
	 * @return the contract node child of this node
	 */
	SequenceNode<ContractNode> getContract();

	/**
	 * Sets the contract node child of this node to the given node.
	 * 
	 * @param contract
	 *            the contract node to be made a child of this node
	 */
	void setContract(SequenceNode<ContractNode> contract);

	@Override
	FunctionDeclarationNode copy();

	@Override
	FunctionTypeNode getTypeNode();

}
