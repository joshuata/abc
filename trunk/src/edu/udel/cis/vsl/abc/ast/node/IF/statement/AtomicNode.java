package edu.udel.cis.vsl.abc.ast.node.IF.statement;

/**
 * An atomic node is associated to <code>$atomic</code> or <code>$datomic</code>
 * , where $atomic requires its body to be finite and non-blocking; whereas
 * $datomic has an extra requirement that its body should be also
 * deterministic.
 * 
 * @author zheng
 * 
 */

public interface AtomicNode extends StatementNode {
	/**
	 * The body of the "atomic" statement.
	 * 
	 * @return
	 */
	StatementNode getBody();

	@Override
	AtomicNode copy();

	/**
	 * 
	 * @return True iff this is a deterministic atomic node, i.e., $datomic.
	 */
	boolean isDeterministic();
}
