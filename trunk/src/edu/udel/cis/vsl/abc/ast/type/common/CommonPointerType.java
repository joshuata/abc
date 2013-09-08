package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;

public class CommonPointerType extends CommonObjectType implements PointerType {

	private static int classCode = CommonPointerType.class.hashCode();

	private Type referencedType;

	private ScopeValue scopeRestriction; // may be null

	public CommonPointerType(Type referencedType, ScopeValue scopeRestriction) {
		super(TypeKind.POINTER);
		this.referencedType = referencedType;
		this.scopeRestriction = scopeRestriction;
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

		if (scopeRestriction != null)
			result += scopeRestriction.hashCode();
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
			if (scopeRestriction == null)
				return that.scopeRestriction == null;
			else
				return scopeRestriction.equals(that.scopeRestriction);
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
			else {
				ScopeValue thisScopeValue = this.scopeRestriction();
				ScopeValue thatScopeValue = that.scopeRestriction();

				if (thisScopeValue == null || thatScopeValue == null)
					return true;
				if (thisScopeValue instanceof Scope
						&& thatScopeValue instanceof Scope) {
					Scope thisScope = (Scope) thisScopeValue;
					Scope thatScope = (Scope) thatScopeValue;

					if (thisScope.isDescendantOf(thatScope)
							|| thatScope.isDescendantOf(thisScope))
						return true;
				}
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
			out.println();
			if (scopeRestriction != null)
				out.print(prefix + "| " + scopeRestriction);
		}
	}

	@Override
	public String toString() {
		String result = "pointer<";

		if (scopeRestriction != null) {
			result += scopeRestriction.toString();
		}
		result += ">[" + referencedType + "]";
		return result;
	}

	@Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public ScopeValue scopeRestriction() {
		return scopeRestriction;
	}

}
