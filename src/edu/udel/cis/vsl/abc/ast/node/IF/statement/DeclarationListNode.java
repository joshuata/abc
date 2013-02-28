package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;

public interface DeclarationListNode extends
		SequenceNode<VariableDeclarationNode>, ForLoopInitializerNode {

}
