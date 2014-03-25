package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;

public interface OmpParallelNode extends OmpStatementNode {
	/**
	 * Returns the expression node of num_threads(). NULL if num_threads() is
	 * absent.
	 * 
	 * @return
	 */
	ExpressionNode numThreads();
	
	void setNumThreads(ExpressionNode value);

	/**
	 * Returns the expression node of if(). NULL if if() is absent.
	 * 
	 * @return
	 */
	ExpressionNode ifClause();
	
	void setIfClause(ExpressionNode value);

	/**
	 * true iff default(shared), false iff default(none). The default(none)
	 * clause requires that each variable that is referenced in the construct,
	 * and that does not have a predetermined data-sharing attribute, must have
	 * its data-sharing attribute explicitly determined by being listed in a
	 * data-sharing attribute clause. By default, default(shared) is assumed.
	 */
	boolean isDefaultShared();

	void setDefault(boolean shared);

}
