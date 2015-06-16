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

	/**
	 * The kinds of contract nodes
	 * 
	 * @author Manchun Zheng
	 *
	 */
	public enum ContractKind {
		/**
		 * defines memory units assigned by the function
		 */
		ASSIGNS_READS,
		/**
		 * defines features of the dependent processes of the current one
		 */
		DEPENDS,
		/**
		 * An "ensures" node encodes a post-condition in a procedure contract. A
		 * node of this kind can be safely cast to {@link EnsuresNode}.
		 */
		ENSURES,
		/**
		 * A "guard" node represents the guard of a CIVL-C function. A node of
		 * this kind may be safely cast to {@link GuardNode}.
		 */
		GUARD,
		/**
		 * A "requires" node represents a pre-condition in a CIVL-C procedure
		 * contract. May be safely cast to {@link RequiresNode}.
		 */
		REQUIRES,
	}

	/**
	 * The kind of this contract node.
	 * 
	 * @return
	 */
	ContractKind contractKind();

	@Override
	ContractNode copy();
}
