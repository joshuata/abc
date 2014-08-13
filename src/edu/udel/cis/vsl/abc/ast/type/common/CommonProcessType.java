package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;

public class CommonProcessType extends CommonObjectType implements
		UnqualifiedObjectType {

	private static int classCode = CommonProcessType.class.hashCode();

	public CommonProcessType() {
		super(TypeKind.PROCESS);
	}

	@Override
	public boolean isComplete() {
		return true;
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
		return object instanceof CommonProcessType;
	}

	@Override
	public boolean compatibleWith(Type type) {
		return equals(type);
	}

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.print("$proc");
	}

	@Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public boolean equivalentTo(Type type) {
		return type instanceof CommonProcessType;
	}

}
