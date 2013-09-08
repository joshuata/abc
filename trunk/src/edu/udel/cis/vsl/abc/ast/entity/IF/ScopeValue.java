package edu.udel.cis.vsl.abc.ast.entity.IF;

/**
 * A scope value is a general abstract expression that represents a scope. It
 * may be thought of as a symbolic expression of type "scope". The "concrete"
 * scope values are instances of Scope. A variable scope value is an instance of
 * ScopeVariable. Both of those types are subtypes of ScopeValue.
 * 
 * In the future, there may be more interesting scope values, such as symbolic
 * expressions of the form "join(scope1,scope2)".
 * 
 * @author siegel
 * 
 */
public interface ScopeValue {

}
