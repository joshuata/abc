package edu.udel.cis.vsl.abc.ast.node.IF.omp;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;

/**
 * This interface implements the OpenMP loop construct. <br>
 * The syntax of the loop construct is specified in Section 2.7.1, OpenMP 4.0.0,
 * as follows:
 * 
 * <pre>
 * #pragma omp for [clause[[,] clause] ... ] new-line 
 * for-loops
 * </pre>
 * 
 * where clause is one of the following:
 * 
 * <pre>
 * private(list)
 * firstprivate(list) 
 * lastprivate(list) 
 * reduction(reduction-identifier: list)
 * schedule(kind[, chunk_size])
 * collapse(n)
 * ordered
 * nowait
 * </pre>
 * 
 * @author Manchun Zheng
 * 
 */
public interface OmpForNode extends OmpWorksharingNode {

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
	 * Returns the number of loops associated with the loop construct. 1 by
	 * default if there is no collapse clause.
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
	SequenceNode<FunctionCallNode> assertions();

	void setAssertions(SequenceNode<FunctionCallNode> assertions);

	/**
	 * Returns the loop invariant.
	 * 
	 * @return
	 */
	FunctionCallNode invariant();

	void setInvariant(FunctionCallNode invariant);
}
