package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

/**
 * An abstract function definition contains the information for an abstract
 * function (i.e. a function in the mathematical sense, treated as uninterpreted
 * in the code).
 * 
 * An abstract function has an identifier, return type, parameters, and an
 * integer specifying the number of partial derivatives that may be taken.
 * 
 * @author zirkel
 * 
 */
public interface AbstractFunctionDefinitionNode extends FunctionDeclarationNode {

	/**
	 * @return The total number of partial derivatives (of any parameter) that
	 *         may be taken.
	 */
	int continuity();

	@Override
	AbstractFunctionDefinitionNode copy();

}
