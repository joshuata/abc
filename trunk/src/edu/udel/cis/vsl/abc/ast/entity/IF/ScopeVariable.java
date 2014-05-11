package edu.udel.cis.vsl.abc.ast.entity.IF;

/**
 * A variable of type "$scope". Such a variable comes into being from either a
 * declaration in the code, such as "$scope s0;", which allows the user to give
 * a name to a concrete scope (the scope in which the declaration occurs), or
 * through the use of a scope-parameterized declaration, such as
 * <code><s1,s2> $double *<s1> f($double *<s2>)</code> *
 * 
 * @author siegel
 * @deprecated
 * 
 */
public interface ScopeVariable extends Variable, ScopeValue {

}
