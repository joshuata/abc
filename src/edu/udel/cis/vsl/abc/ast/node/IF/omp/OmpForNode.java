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
	 * 
	 * @return
	 */
	int collapse();

	void setCollapse(int value);

	boolean ordered();

	void setOrdered(boolean value);

	ExpressionNode chunkSize();

	void setChunsize(ExpressionNode chunkSize);

	List<FunctionCallNode> assertions();// assertions to be checked before entry

	FunctionCallNode invariant();// loop invariant
}
