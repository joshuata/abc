package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonSequenceNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.CompoundValue;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonCompoundInitializerNode extends
		CommonSequenceNode<PairNode<DesignationNode, InitializerNode>>
		implements CompoundInitializerNode {

	private CompoundValue members;

	private Type type;

	public CommonCompoundInitializerNode(Source source,
			List<PairNode<DesignationNode, InitializerNode>> childList) {
		super(source, "CompoundInitializer", childList);
	}

	public void setMembers(CompoundValue members) {
		this.members = members;
	}

	@Override
	public CompoundInitializerNode copy() {
		CommonCompoundInitializerNode result = new CommonCompoundInitializerNode(
				getSource(), childListCopy());

		result.setMembers(members);
		result.setType(type);
		return result;
	}

	@Override
	public CompoundValue getMembers() {
		return members;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

}
