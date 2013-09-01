package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;

/**
 * Children: (0) identifier, (1) type, (2) contract.
 * 
 * TODO: add (3) scope. identifier.
 * 
 * @author siegel
 * 
 */
public interface FunctionDeclarationNode extends OrdinaryDeclarationNode {

	@Override
	Function getEntity();

	/**
	 * Does the declaration include the "inline" function specifier? Only
	 * applies to function delcarations.
	 * 
	 * For functions only (not objects).
	 * 
	 * @return true if declaration contains "inline"
	 */
	boolean hasInlineFunctionSpecifier();

	void setInlineFunctionSpecifier(boolean value);

	/**
	 * Does the declaration include the "_Noreturn" function specifier? Only
	 * applies to function delcarations.
	 * 
	 * For functions only (not objects).
	 * 
	 * @return true if declaration contains "_Noreturn"
	 */
	boolean hasNoreturnFunctionSpecifier();

	void setNoreturnFunctionSpecifier(boolean value);

	SequenceNode<ContractNode> getContract();

	void setContract(SequenceNode<ContractNode> contract);

	@Override
	FunctionTypeNode getTypeNode();

	/**
	 * A function declaration can have any number of scope parameters associated
	 * to it, e.g.:
	 * 
	 * <pre>
	 * <s1,s2,s3> double *<s2> f(double *<s1> x, double *<s3> y);
	 * </pre>
	 * 
	 * When f is called:
	 * 
	 * <pre>
	 * f<t1,t2,t3>(a,b)
	 * </pre>
	 * 
	 * @return the list of scope identifiers in the declaration, or null if no
	 *         such list occurred
	 */
	SequenceNode<IdentifierNode> getScopeList();

	@Override
	FunctionDeclarationNode copy();

}
