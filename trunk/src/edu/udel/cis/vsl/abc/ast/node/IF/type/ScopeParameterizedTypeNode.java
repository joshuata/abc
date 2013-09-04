package edu.udel.cis.vsl.abc.ast.node.IF.type;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;

public interface ScopeParameterizedTypeNode extends TypeNode {

	SequenceNode<VariableDeclarationNode> getScopeParameters();

	TypeNode getBody();

}
