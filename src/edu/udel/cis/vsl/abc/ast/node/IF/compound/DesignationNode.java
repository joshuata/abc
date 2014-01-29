package edu.udel.cis.vsl.abc.ast.node.IF.compound;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;

/**
 * A designation node specifies a sequence of designators. Each designator is
 * either an array designator or field designator. The sequence naviages to a
 * point within a compound structure.
 * 
 * 
 * 
 * @author siegel
 * 
 */
public interface DesignationNode extends SequenceNode<DesignatorNode> {

	@Override
	DesignationNode copy();
}
