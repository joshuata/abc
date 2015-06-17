package edu.udel.cis.vsl.abc.ast.node.IF.expression;

/**
 * Constant <code>$nothing</code>, argument of <code>$assigns / $reads</code>
 * contract clauses.
 * 
 * @author zmanchun
 *
 */
public interface NothingNode extends ConstantNode {
	@Override
	NothingNode copy();
}
