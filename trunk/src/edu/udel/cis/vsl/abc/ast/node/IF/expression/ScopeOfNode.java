package edu.udel.cis.vsl.abc.ast.node.IF.expression;

/**
 * An expression which takes an Identifier expression which is a variable name
 * and returns the scope (value of type $scope) in which that variable is
 * declared.
 * 
 * @author siegel
 * 
 */
public interface ScopeOfNode extends ExpressionNode {

	ExpressionNode expression();

}
