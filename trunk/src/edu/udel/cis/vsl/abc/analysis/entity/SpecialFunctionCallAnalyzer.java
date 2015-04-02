package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;

/**
 * This class maintains the hard-coded information of analyzes the variable
 * parameters of some specific functions, like scanf, fscanf, whose varialbe
 * arguments should all be of pointer type.
 * 
 * @author Manchun Zheng
 *
 */
public class SpecialFunctionCallAnalyzer {
	// names of special functions handled in this class
	private final static String SCANF = "scanf";
	private final static String FSCANF = "fscanf";

	/** the type factory, for generating types. */
	@SuppressWarnings("unused")
	private TypeFactory typeFactory;

	/** the void pointer type (void *) */
	private ObjectType voidPointerType;

	/**
	 * The names of functions handled by this analyzer
	 */
	private final Set<String> specialFunctioinNames = new HashSet<>(
			Arrays.asList(SCANF, FSCANF));

	/**
	 * Creates a new instance of special function call analyzer.
	 * 
	 * @param typeFactory
	 *            The type factory to be used.
	 */
	public SpecialFunctionCallAnalyzer(TypeFactory typeFactory) {
		this.typeFactory = typeFactory;
		this.voidPointerType = typeFactory.pointerType(typeFactory.voidType());
	}

	/**
	 * Is the given function handled in this analyzer?
	 * 
	 * @param function
	 *            Name of the function
	 * @return true iff the given function is in this analyzer
	 */
	boolean isSpecialFunction(String function) {
		return this.specialFunctioinNames.contains(function);
	}

	/**
	 * Returns the type of a variable parameter of a certain index of the given
	 * function.
	 * <p>
	 * Precondition: the given function is a special function handled in this
	 * analyzer and the index-th parameter is a variable one.
	 * 
	 * @param function
	 *            Name of the function
	 * @param index
	 *            index of the parameter whose type is to be obtained
	 * @return
	 */
	ObjectType parameterType(String function, int index) {
		assert this.isSpecialFunction(function);
		switch (function) {
		case SCANF:
			return this.parameterTypeOfScanf(index);
		case FSCANF:
			return this.parameterTypeOfScanf(index - 1);
		default:
			throw new ABCUnsupportedException("The function " + function
					+ " isn't a special function that needs "
					+ "type checking of its variable parameters");
		}
	}

	ObjectType parameterTypeOfScanf(int index) {
		assert index > 0;
		return this.voidPointerType;
	}
}
