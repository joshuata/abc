package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;

public class CommonVoidType extends CommonObjectType implements
		UnqualifiedObjectType {

	private static int classCode = CommonVoidType.class.hashCode();

	public CommonVoidType() {
		super(TypeKind.VOID);
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public boolean isVariablyModified() {
		return false;
	}

	@Override
	public int hashCode() {
		return classCode;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof CommonVoidType;
	}

	@Override
	public boolean equivalentTo(Type type) {
		return type instanceof CommonVoidType;
	}

	@Override
	public boolean compatibleWith(Type type) {
		return equals(type);
	}

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.print("void");
	}

	@Override
	public boolean isScalar() {
		return false;
	}

}
