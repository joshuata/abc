package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonSequenceNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonDesignationNode extends CommonSequenceNode<DesignatorNode>
		implements DesignationNode {

	public CommonDesignationNode(Source source, List<DesignatorNode> childList) {
		super(source, "DesignatorList", childList);
	}

}
