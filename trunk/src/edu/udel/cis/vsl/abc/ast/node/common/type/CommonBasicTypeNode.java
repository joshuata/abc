package edu.udel.cis.vsl.abc.ast.node.common.type;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.type.BasicTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonBasicTypeNode extends CommonTypeNode implements
		BasicTypeNode {

	private BasicTypeKind basicTypeKind;

	public CommonBasicTypeNode(Source source, BasicTypeKind basicTypeKind) {
		super(source, TypeNodeKind.BASIC);
		this.basicTypeKind = basicTypeKind;
	}

	@Override
	public BasicTypeKind getBasicTypeKind() {
		return basicTypeKind;
	}

	@Override
	protected void printBody(PrintStream out) {
		String qualifiers = qualifierString();

		out.print("BasicType[" + basicTypeKind);
		if (!qualifiers.isEmpty())
			out.print(", " + qualifiers);
		out.print("]");
	}

	@Override
	public BasicTypeNode copy() {
		CommonBasicTypeNode result = new CommonBasicTypeNode(getSource(),
				getBasicTypeKind());

		copyData(result);
		return result;
	}

	@Override
	public TypeNodeKind typeNodeKind() {
		return TypeNodeKind.BASIC;
	}

}
