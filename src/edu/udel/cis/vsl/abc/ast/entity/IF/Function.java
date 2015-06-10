package edu.udel.cis.vsl.abc.ast.entity.IF;

import java.util.Iterator;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;

/**
 * A function is an entity which takes inputs, executes a statement, and
 * possibly returns a result.
 * 
 * @author siegel
 * 
 */
public interface Function extends OrdinaryEntity {

	@Override
	FunctionType getType();

	/**
	 * Is the function declared with the <code>inline</code> specifier,
	 * indicating that this is an inline function?
	 * 
	 * @return <code>true</code> iff the function is an inline function
	 */
	boolean isInlined();

	/**
	 * Sets whether this function is an inline function
	 * 
	 * @param value
	 *            <code>true</code> if inlined, <code>false</code> if not
	 */
	void setIsInlined(boolean value);

	/**
	 * Is the function declared with the <code>_Noreturn</code> specifier,
	 * indicating that the function does not return.
	 * 
	 * @return <code>true</code> iff the function is declared with
	 *         <code>_Noreturn</code>
	 */
	boolean doesNotReturn();

	/**
	 * Sets whether this function is declared with <code>_Noreturn</code>.
	 * 
	 * @param value
	 *            <code>true</code> if <code>_Noreturn</code>,
	 *            <code>false</code> if not
	 */
	void setDoesNotReturn(boolean value);

	@Override
	FunctionDefinitionNode getDefinition();

	/**
	 * Returns the function scope associated to this function. This is the scope
	 * in which the ordinary labels are declared. It is the outermost scope of
	 * the function body.
	 * 
	 * @return the function scope associated to this function
	 */
	Scope getScope();

	/**
	 * Returns the set of functions that call this function either by name or
	 * through a pointer dereference (the latter is relation is safely overapproximated). 
	 * 
	 * Transitive calling relationships are not reflected in this set, i.e., if a calls 
	 * b which calls c, then a is not in getCallers() of c (unless of course a directly 
	 * calls c as well).
	 * 
	 * The set is initially empty; a call to
	 * {@link edu.udel.cis.vsl.abc.analysis.common.CallAnalyzer#analyze(edu.udel.cis.vsl.abc.ast.IF.AST)}
	 * will populate it.
	 * 
	 * @return the set of functions that call this function
	 */
	Set<Function> getCallers();

	/**
	 * Returns the set of functions called by this function either by name or
	 * through a pointer dereference (the latter is relation is safely overapproximated).  
	 * 
	 * Transitive calling relationships are not reflected in this set, i.e., if a calls 
	 * b which calls c, then c is not in getCallees() of a (unless of course a directly calls c as well).
	 * 
	 * The set is initially empty; a call to
	 * {@link edu.udel.cis.vsl.abc.analysis.common.CallAnalyzer#analyze(edu.udel.cis.vsl.abc.ast.IF.AST)}
	 * will populate it.
	 * 
	 * @return the set of functions called by this function
	 */
	Set<Function> getCallees();

	/**
	 * <p>
	 * Gets the preconditions specified in this function's contract. The
	 * precondition list is initially empty. Preconditions can be added to the
	 * list using method {@link #addPrecondition(ExpressionNode)}.
	 * </p>
	 * 
	 * <p>
	 * This method is specific to CIVL-C.
	 * </p>
	 * 
	 * @return iterator over the preconditions
	 */
	Iterator<ExpressionNode> getPreconditions();

	/**
	 * Gets the list of postconditions specified in this function's contract.
	 * The postcondition list is initially empty. Postconditions can be added to
	 * the list using method {@link #addPostcondition(ExpressionNode)}.
	 * 
	 * <p>
	 * This method is specific to CIVL-C.
	 * </p>
	 * 
	 * @return iterator over the postconditions
	 */
	Iterator<ExpressionNode> getPostconditions();

	/**
	 * Adds the given expression to the list of preconditions for the function.
	 * Should be a boolean expression.
	 * 
	 * <p>
	 * This method is specific to CIVL-C.
	 * </p>
	 * 
	 * @param expression
	 *            a boolean expression
	 */
	void addPrecondition(ExpressionNode expression);

	/**
	 * Adds the given expression to the list of postconditions for the function.
	 * Should be a boolean expression.
	 * 
	 * <p>
	 * This method is specific to CIVL-C.
	 * </p>
	 * 
	 * @param expression
	 *            a boolean expression
	 */
	void addPostcondition(ExpressionNode expression);

	void addDepends(ExpressionNode expression);

	void addAssigns(ExpressionNode expression);

	void addGuard(ExpressionNode expression);

	Iterator<ExpressionNode> getDepends();

	Iterator<ExpressionNode> getGuard();

	Iterator<ExpressionNode> getAssigns();

	// TODO: perhaps more information is needed. About each parameter:
	// does it have static extent? What is the extent (constant
	// or expression)?

	/*
	 * The word "static" may appear between the brackets in certain situations.
	 * See C11 6.7.6.3(7) for its meaning. It can only be used in the
	 * declaration of a function parameter.
	 */

}
