package edu.udel.cis.vsl.abc.ast.node.common.expression;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.NothingNode;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonNothingNode extends CommonConstantNode implements
		NothingNode {

	public CommonNothingNode(Source source, Type memoryType) {
		super(source, "$nothing", memoryType);
	}

	@Override
	public ConstantKind constantKind() {
		return ConstantKind.NOTHING;
	}

	@Override
	public NothingNode copy() {
		return null;
	}

	@Override
	protected void printBody(PrintStream out) {
		// TODO Auto-generated method stub

	}

}
