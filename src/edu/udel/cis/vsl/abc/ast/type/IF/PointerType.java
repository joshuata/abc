package edu.udel.cis.vsl.abc.ast.type.IF;

import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;

public interface PointerType extends UnqualifiedObjectType {

	Type referencedType();

	/**
	 * Returns the Scope represented by the scope modifier, i.e., the scope in
	 * which the scope variable is declared. This is equivalent to
	 * scopeModifier().scope() except that it returns null if scopeModifier()
	 * does.
	 * 
	 * For a scope-restricted CIVL-C pointer, this returns the scope. Example in
	 * source code: <code>double *<s5> p</code>. This returns the scope
	 * referenced by s5.
	 * 
	 * In CIVL-C, code
	 * 
	 * <code>
	 * $input int x;
	 * int w;
	 * void f() {
	 *   int l;
	 * }
	 * void main() {
	 *   int z;
	 *   S;
	 * }
	 * </code>
	 * 
	 * will be translated to:
	 * 
	 * <code>
	 * system(int x) { // root scope
	 *   int w;
	 *   void f() { int l; }
	 *   {
	 *     int z;
	 *     S;
	 *   }
	 * }
	 * </code>
	 * 
	 * so the outermost scope, i.e., the root scope, i.e., the scope with no
	 * parent scope is the scope of the external decls in the original source
	 * program and can be named as usual with an external decl.
	 * 
	 * @return scope restriction of this pointer type, or null
	 */
	Scope scope();
}
