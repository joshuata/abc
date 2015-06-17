package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.conversion.IF.Conversion;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * This class maintains the hard-coded information of analyzes the variable
 * parameters of some specific functions, like <code>scanf, fscanf</code>, whose
 * variable arguments should all be of pointer type.
 * 
 * @author Manchun Zheng
 *
 */
public class SpecialFunctionCallAnalyzer {
	// names of special functions handled in this class
	private final static String SCANF = "scanf";
	private final static String FSCANF = "fscanf";
	private final static String ACCESS = "$access";
	private final static String MODIFIED = "$write";
	private final static String READ = "$read";

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

	private final Set<String> memoryTypeFunctionNames = new HashSet<>(
			Arrays.asList(ACCESS, MODIFIED, READ));

	private ConversionFactory conversionFactory;

	/**
	 * Creates a new instance of special function call analyzer.
	 * 
	 * @param typeFactory
	 *            The type factory to be used.
	 */
	public SpecialFunctionCallAnalyzer(TypeFactory typeFactory,
			ConversionFactory conversionFactory) {
		this.typeFactory = typeFactory;
		this.conversionFactory = conversionFactory;
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
	ObjectType variableParameterType(String function, int index) {
		assert this.isSpecialFunction(function);
		switch (function) {
		case SCANF:
			return this.variableParameterTypeOfScanf(index);
		case FSCANF:// fscanf has one more fixed parameter than scanf
			return this.variableParameterTypeOfScanf(index - 1);
		default:
			throw new ABCUnsupportedException("The function " + function
					+ " isn't a special function that needs "
					+ "type checking of its variable parameters");
		}
	}

	void addConversionsForSpecialFunctions(String functionName,
			ExpressionNode argument) throws SyntaxException {
		if (memoryTypeFunctionNames.contains(functionName)) {
			this.addMemoryConversion(argument);
		}
	}

	private void addMemoryConversion(ExpressionNode node)
			throws SyntaxException {
		Type oldType = node.getConvertedType();
		Conversion conversion = conversionFactory.memoryConversion(oldType);

		node.addConversion(conversion);

	}

	/**
	 * Returns the type of the parameter of the given index for
	 * <code>scanf</code>. <code>scanf</code> is expecting any parameter with
	 * index greater than 0 to be of pointer type, i.e.:
	 * <code>scanf(char*, (void*)+);</code>
	 * 
	 * @param index
	 *            The index of the parameter
	 * @return the type of the parameter of the given index for scanf, which is
	 *         always void*.
	 */
	private ObjectType variableParameterTypeOfScanf(int index) {
		assert index > 0;
		return this.voidPointerType;
	}
}
