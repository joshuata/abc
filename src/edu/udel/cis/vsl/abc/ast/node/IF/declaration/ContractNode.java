package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;

/**
 * Part of a procedure contract.
 * 
 * @author siegel
 * 
 */
public interface ContractNode extends ASTNode {

	@Override
	ContractNode copy();
}
