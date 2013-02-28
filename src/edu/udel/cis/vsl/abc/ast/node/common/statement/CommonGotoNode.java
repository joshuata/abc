package edu.udel.cis.vsl.abc.ast.node.common.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.GotoNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonGotoNode extends CommonJumpNode implements GotoNode {

	public CommonGotoNode(Source source, IdentifierNode label) {
		super(source, JumpKind.GOTO);
		addChild(label);
	}

	@Override
	public IdentifierNode getLabel() {
		return (IdentifierNode) child(0);
	}

}
