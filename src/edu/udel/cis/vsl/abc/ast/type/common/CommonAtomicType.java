package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.type.IF.AtomicType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.UnqualifiedObjectType;

public class CommonAtomicType extends CommonObjectType implements AtomicType {

	private static int classCode = CommonAtomicType.class.hashCode();

	private UnqualifiedObjectType baseType;

	public CommonAtomicType(UnqualifiedObjectType baseType) {
		super(TypeKind.ATOMIC);
		this.baseType = baseType;
	}

	@Override
	public UnqualifiedObjectType getBaseType() {
		return baseType;
	}

	@Override
	public boolean isComplete() {
		return baseType.isComplete();
	}

	@Override
	public boolean isVariablyModified() {
		return baseType.isVariablyModified();
	}

	@Override
	public int hashCode() {
		return classCode + baseType.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof CommonAtomicType) {
			CommonAtomicType that = (CommonAtomicType) object;

			return baseType.equals(that.baseType);
		}
		return false;
	}

	@Override
	public boolean compatibleWith(Type type) {
		if (this == type)
			return true;
		if (type instanceof CommonAtomicType) {
			CommonAtomicType that = (CommonAtomicType) type;

			return baseType.compatibleWith(that.baseType);
		}
		return false;
	}

	@Override
	public String toString() {
		return "AtomicType[" + baseType + "]";
	}

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.println("Atomic");
		baseType.print(prefix + "| ", out, true);
	}

	@Override
	public boolean isScalar() {
		return baseType.isScalar();
	}

	@Override
	public boolean equivalentTo(Type type) {
		if (this == type)
			return true;
		if (type instanceof CommonAtomicType) {
			CommonAtomicType that = (CommonAtomicType) type;

			return baseType.equivalentTo(that.baseType);
		}
		return false;
	}

}
