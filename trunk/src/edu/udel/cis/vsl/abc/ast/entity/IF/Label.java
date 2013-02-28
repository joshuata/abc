package edu.udel.cis.vsl.abc.ast.entity.IF;

import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;

public interface Label extends Entity {

	OrdinaryLabelNode getDefinition();
}
