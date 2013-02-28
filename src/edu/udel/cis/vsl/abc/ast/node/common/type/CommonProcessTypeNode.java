package edu.udel.cis.vsl.abc.ast.node.common.type;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonProcessTypeNode extends CommonTypeNode {

	public CommonProcessTypeNode(Source source) {
		super(source, TypeNodeKind.PROCESS);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("\\proc");
	}

}
