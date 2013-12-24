package edu.udel.cis.vsl.abc.ast.node.IF.statement;

/**
 * An "atomic" statement has the form "$atomic body", where body is a list of
 * statements.
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
}
