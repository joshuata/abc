package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;

/**
 * A contract node represents an element that may occur in a procedure contract.
 * The procedure contract consists of a sequence of contract nodes.
 * 
 * @author siegel
 * 
 */
public interface ContractNode extends ASTNode {

	@Override
	ContractNode copy();
}
