package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;

public class CommonPointerType extends CommonObjectType implements PointerType {

	private static int classCode = CommonPointerType.class.hashCode();

	private Type referencedType;

	private Scope scope; // may be null

	public CommonPointerType(Type referencedType, Scope scope) {
		super(TypeKind.POINTER);
		this.referencedType = referencedType;
		this.scope = scope;
	}

	@Override
	public Type referencedType() {
		return referencedType;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public boolean isVariablyModified() {
		return referencedType.isVariablyModified();
	}

	@Override
	public int hashCode() {
		int result = classCode + referencedType.hashCode();

		if (scope != null)
			result += scope.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof CommonPointerType) {
			CommonPointerType that = (CommonPointerType) object;

			if (!referencedType.equals(that.referencedType))
				return false;
			if (scope == null)
				return that.scope == null;
			else
				return scope.equals(that.scope);
		}
		return false;
	}

	/**
	 * "For two pointer types to be compatible, both shall be identically
	 * qualified and both shall be pointers to compatible types."
	 * 
	 */
	@Override
	public boolean compatibleWith(Type type) {
		if (type instanceof PointerType) {
			CommonPointerType that = (CommonPointerType) type;

			if (!this.referencedType().compatibleWith(that.referencedType()))
				return false;
			if (scope == null || that.scope == null)
				return true;
			else {
				Scope thisScope = this.scope();
				Scope thatScope = that.scope();

				if (thisScope.isDescendantOf(thatScope)
						|| thatScope.isDescendantOf(thisScope))
					return true;
			}
		}
		return false;
	}

	// public boolean isAssignmentCompatibleWith(Type type) {
	// if (type instanceof PointerType) {
	// CommonPointerType that = (CommonPointerType) type;
	//
	// if (!this.referencedType().compatibleWith(that.referencedType()))
	// return false;
	// if (scope == null || that.scope == null)
	// return true;
	// if (scope.isDescendantOf(that.scope)
	// || that.scope.isDescendantOf(scope))
	// return true;
	// }
	// return false;
	// }

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.print("Pointer");
		if (referencedType != null) {
			out.println();
			out.print(prefix + "| ");
			referencedType.print(prefix + "| ", out, true);
		}
	}

	@Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public Scope scope() {
		return scope;
	}

	// @Override
	// public VariableDeclarationNode scopeModifier() {
	// return scopeModifier;
	// }
}
