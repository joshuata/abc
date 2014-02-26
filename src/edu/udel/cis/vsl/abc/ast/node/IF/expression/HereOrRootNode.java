package edu.udel.cis.vsl.abc.ast.node.IF.expression;

public interface HereOrRootNode extends ConstantNode {
	@Override
	HereOrRootNode copy();

	boolean isHereNode();

	boolean isRootNode();
}
