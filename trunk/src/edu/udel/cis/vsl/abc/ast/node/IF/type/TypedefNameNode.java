package edu.udel.cis.vsl.abc.ast.node.IF.type;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;

public interface TypedefNameNode extends TypeNode {

	IdentifierNode getName();

	void setName(IdentifierNode name);

}
