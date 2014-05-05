package edu.udel.cis.vsl.abc.ast.node.common.type;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.PointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonPointerTypeNode extends CommonTypeNode implements
		PointerTypeNode {

	public CommonPointerTypeNode(Source source, TypeNode baseType,
			ExpressionNode scopeModifier) {
		super(source, TypeNodeKind.POINTER, baseType, scopeModifier);
	}

	@Override
	public TypeNode referencedType() {
		return (TypeNode) child(0);
	}

	@Override
	protected void printBody(PrintStream out) {
		String qualifiers = qualifierString();

		out.print("PointerType");
		if (!qualifiers.isEmpty())
			out.print("[" + qualifierString() + "]");
	}

	@Override
	public PointerTypeNode copy() {
		CommonPointerTypeNode result = new CommonPointerTypeNode(getSource(),
				duplicate(referencedType()), duplicate(scopeModifier()));

		copyData(result);
		return result;
	}

	@Override
	public ExpressionNode scopeModifier() {
		return (ExpressionNode) child(1);
	}

	@Override
	public TypeNodeKind typeNodeKind() {
		return TypeNodeKind.POINTER;
	}
}
