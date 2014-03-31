package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpForNode extends CommonOmpWorkshareNode implements
		OmpForNode {

	private OmpScheduleKind schedule;
	// private ExpressionNode chunkSize;
	private int collapse;
	private boolean ordered;
	private List<FunctionCallNode> assertions;
	private FunctionCallNode invariant;

	/**
	 * Children
	 * <ul>
	 * <li>Children 0-8: same as {@link CommonOmpStatementNode};</li>
	 * <li>Child 9: ExpressionNode, the expression of chunk_size in
	 * <code>schedule()</code> ;</li>
	 * </ul>
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 */
	public CommonOmpForNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken) {
		super(source, identifier, body, eofToken, OmpWorkshareNodeKind.FOR);
		collapse = 1;
		schedule = OmpScheduleKind.STATIC;
		ordered = false;
		this.addChild(null);// child 9
		// chunkSize = null;
	}

	@Override
	public OmpScheduleKind schedule() {
		return this.schedule;
	}

	@Override
	public int collapse() {
		return this.collapse;
	}

	@Override
	public boolean ordered() {
		return this.ordered;
	}

	@Override
	public List<FunctionCallNode> assertions() {
		return this.assertions;
	}

	@Override
	public FunctionCallNode invariant() {
		return this.invariant;
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("OmpFor");
	}

	@Override
	public ExpressionNode chunkSize() {
		return (ExpressionNode) this.child(9);
	}

	@Override
	public void setSchedule(OmpScheduleKind ompScheduleKind) {
		this.schedule = ompScheduleKind;
	}

	@Override
	public void setCollapse(int value) {
		this.collapse = value;
	}

	@Override
	public void setOrdered(boolean value) {
		this.ordered = value;
	}

	@Override
	public void setChunsize(ExpressionNode chunkSize) {
		this.setChild(9, chunkSize);
	}

	@Override
	protected void printExtras(String prefix, PrintStream out) {
		String scheduleText;
		ExpressionNode chunkSize = (ExpressionNode) this.child(9);

		// STATIC, DYNAMIC, GUIDED, AUTO, RUNTIME
		switch (schedule) {
		case STATIC:
			scheduleText = "static";
			break;
		case DYNAMIC:
			scheduleText = "dynamic";
			break;
		case GUIDED:
			scheduleText = "guided";
			break;
		case AUTO:
			scheduleText = "auto";
			break;
		case RUNTIME:
			scheduleText = "runtime";
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
		if (chunkSize != null) {
			out.println();
			out.print(prefix + "schedule(");
			out.print(scheduleText);
			out.print(",");
			out.print(chunkSize.toString());
			out.print(")");
		}
		if (collapse > 1) {
			out.println();
			out.print(prefix + "collapse(");
			out.print(this.collapse);
			out.print(")");
		}
		if (this.ordered) {
			out.println();
			out.print("ordered");
		}
		super.printExtras(prefix, out);
	}

	@Override
	public void setAssertions(List<FunctionCallNode> assertions) {
		this.assertions = assertions;
	}

	@Override
	public void setInvariant(FunctionCallNode invariant) {
		this.invariant = invariant;
	}
}
