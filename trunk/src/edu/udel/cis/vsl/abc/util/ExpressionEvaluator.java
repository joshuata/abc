package edu.udel.cis.vsl.abc.util;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;

/*
 * This class provides support for evaluating certain classes of ABC
 * expressions.   It does this by transforming  ABC expressions to SARL 
 * expressions and evaluating those.
 */
public class ExpressionEvaluator {
	// Record mapping from ABC IDs, defined by their Entity, and the translated SARL representation
	private static Map<Entity,NumericSymbolicConstant> translateID;
	
	private static SymbolicUniverse universe = SARL.newStandardUniverse();

	public static boolean isEqualIntExpr(ExpressionNode o1, ExpressionNode o2) {
		/*
		 * Should check that operator nodes are of an integer type
		 */
		
		BooleanExpression context = universe.trueExpression();
		Reasoner reasoner = universe.reasoner(context);
		
		NumericExpression n1 = toSarl(o1);
		NumericExpression n2 = toSarl(o2);
		BooleanExpression equiv = universe.equals(n1, n2);
		
		System.out.println("ExpressionEvaluator translated "+equiv);
		
		return reasoner.isValid(equiv);
	}
	
	/*
	 * Visit the operator node and convert it to a SARL expression.
	 * The following are not supported in operator nodes:
	 *   - function calls
	 *   - array references
	 */
	private static NumericExpression toSarl(ExpressionNode o) {
		translateID = new HashMap<Entity,NumericSymbolicConstant>();
		return toSarlWorker(o);
	}
	
	private static NumericExpression toSarlWorker(ExpressionNode o) {
		if (o instanceof OperatorNode) {
			/*
			 * Works with basic integer operators now.  Could be extended to handle
			 * arrays, etc. (not sure how well that will work, but ...)
			 */
			NumericExpression op1 = toSarlWorker(((OperatorNode) o).getArgument(1));
			OperatorNode.Operator oper = ((OperatorNode) o).getOperator();
			if (oper == OperatorNode.Operator.UNARYPLUS) {
				return op1;
			} else if (oper == OperatorNode.Operator.UNARYMINUS) {
				return universe.minus(op1);
			} else {
				NumericExpression op2 = toSarlWorker(((OperatorNode) o).getArgument(1));
				switch (oper) {
				case DIV:
					return universe.divide(op1,op2);
				case MINUS:
					return universe.subtract(op1,op2);
				case MOD:
					return universe.modulo(op1,op2);
				case PLUS:
					return universe.add(op1,op2);
				case TIMES: 
					return universe.multiply(op1,op2);
				default: 
					assert false : "ExpressionEvaluator : cannot translate "+oper+" to SARL";
				}
			}

		} else if (o instanceof IntegerConstantNode) {
			return universe.integer(((IntegerConstantNode)o).getConstantValue().getIntegerValue());

		} else if (o instanceof IdentifierExpressionNode) {
			Entity idEntity = ((IdentifierExpressionNode)o).getIdentifier().getEntity();
			if (translateID.containsKey(idEntity)) {
				return translateID.get(idEntity);
			} else {
				NumericSymbolicConstant idSarl = (NumericSymbolicConstant) universe
						.symbolicConstant(universe.stringObject(idEntity.toString()), universe.integerType());
				translateID.put(idEntity, idSarl);
				return idSarl;
			}
			
		} else {
			assert false : "ExpressionEvaluator : cannot translate "+o+" to SARL";
		}
		return null;
	}

}
