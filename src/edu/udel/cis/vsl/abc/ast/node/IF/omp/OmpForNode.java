package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;

/**
 * This interface
 * 
 * @author Manchun Zheng
 * 
 */
public interface OmpForNode extends OmpWorkshareNode {

	public enum OmpScheduleKind {
		STATIC, DYNAMIC, GUIDED, AUTO, RUNTIME
	};

	/**
	 * Returns the schedule kind of the loop construct TODO The default schedule
	 * is implementation defined,can we choose STATIC or DYNAMIC?
	 * 
	 * @return
	 */
	OmpScheduleKind schedule();

	void setSchedule(OmpScheduleKind ompScheduleKind);

	/**
	 * Returns the number of loops associated with the loop construct.
	 * 1 by default if there is no collapse clause.
	 * 
	 * @return
	 */
	int collapse();

	void setCollapse(int value);

	/**
	 * Returns true iff <code>ordered</code> clause is present.
	 * 
	 * @return
	 */
	boolean ordered();

	void setOrdered(boolean value);

	/**
	 * Returns the chunk size. NULL if there is no schedule clause.
	 * 
	 * @return
	 */
	ExpressionNode chunkSize();

	void setChunsize(ExpressionNode chunkSize);

	/**
	 * Returns the assertions to be checked before the entry of the loop.
	 * 
	 * @return
	 */
	List<FunctionCallNode> assertions();

	void setAssertions(List<FunctionCallNode> assertions);

	/**
	 * Returns the loop invariant.
	 * 
	 * @return
	 */
	FunctionCallNode invariant();

	void setInvariant(FunctionCallNode invariant);
}
