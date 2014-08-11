package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.HereOrRootNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonHereOrRootNode extends CommonConstantNode implements
		HereOrRootNode {
	private boolean isRootNode;

	public CommonHereOrRootNode(Source source, String representation,
			Type scopeType) {
		super(source, representation, scopeType);
		if (representation.equalsIgnoreCase("$root"))
			isRootNode = true;
		else
			isRootNode = false;
	}

	@Override
	public HereOrRootNode copy() {
		String representation = "$here";

		if (isRootNode)
			representation = "$root";
		return new CommonHereOrRootNode(getSource(), representation,
				getInitialType());
	}

	@Override
	public boolean isHereNode() {
		return !this.isRootNode;
	}

	@Override
	public boolean isRootNode() {
		return this.isRootNode;
	}

	@Override
	protected void printBody(PrintStream out) {
		if (isRootNode)
			out.print("$root");
		else
			out.print("$here");
	}

	@Override
	protected boolean equivWork(ASTNode that) {
		if (that instanceof HereOrRootNode)
			return this.isRootNode == ((HereOrRootNode) that).isRootNode();
		return false;
	}

	@Override
	public ConstantKind constantKind() {
		return ConstantKind.HERE_OR_ROOT;
	}
}
