package edu.udel.cis.vsl.abc.ast.node.IF.statement;

/**
 * An atomic node is associated to <code>$atomic</code> or <code>$atom</code> ,
 * where $atomic has no restriction to its body; whereas $atom requires that its
 * body should be finite, non-blocking and deterministic.
 * 
 * @author Manchun Zheng (zmanchun)
 * 
 */

public interface AtomicNode extends StatementNode {
	@Override
	AtomicNode copy();
	
	/**
	 * The body of the "atomic" statement.
	 * 
	 * @return
	 */
	StatementNode getBody();

	/**
	 * 
	 * @return True iff this is a deterministic atomic node, i.e., $atom.
	 */
	boolean isDeterministic();
}
