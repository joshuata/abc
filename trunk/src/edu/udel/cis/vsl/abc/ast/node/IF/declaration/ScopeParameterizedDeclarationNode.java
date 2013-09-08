package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;

public interface ScopeParameterizedDeclarationNode extends DeclarationNode,
		ExternalDefinitionNode , BlockItemNode {

	SequenceNode<VariableDeclarationNode> parameters();

	DeclarationNode baseDeclaration();

	ScopeParameterizedDeclarationNode copy();

}
