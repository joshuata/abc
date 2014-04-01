package edu.udel.cis.vsl.abc.util;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/*
 * This class provides support for evaluating certain classes of ABC
 * expressions.   It does this by transforming  ABC expressions to SARL 
 * expressions and evaluating those.
 */
public class ExpressionEvaluator {
	static boolean isEqualIntExpr(OperatorNode o1, OperatorNode o2) {
		// Check the type of the operator nodes
		SymbolicUniverse universe = SARL.newStandardUniverse();

		
		BooleanExpression context = universe.trueExpression();
		Reasoner reasoner = universe.reasoner(context);
		SymbolicType integerType = universe.integerType();
		
		NumericSymbolicConstant x1 = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("X1"), integerType);
		NumericSymbolicConstant x2 = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("X2"), integerType);


		NumericExpression n1 = toSarl(o1);
		NumericExpression n2 = toSarl(o2);
		BooleanExpression equiv = universe.equals(n1, n2);
		return reasoner.isValid(equiv);
	}
	
	/*
	 * Visit the operator node and convert it to a SARL expression.
	 * The following are not supported in operator nodes:
	 *   - function calls
	 *   - array references
	 */
	private static NumericExpression toSarl(OperatorNode o) {
		return null;
	}

}
