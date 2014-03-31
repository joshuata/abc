package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpDeclarativeNode extends CommonOmpNode implements
		OmpDeclarativeNode {

	// private SequenceNode<IdentifierNode> variables;

	/**
	 * Child 1: varabile list
	 * 
	 * @param source
	 * @param identifier
	 * @param body
	 * @param eofToken
	 * @param varList
	 */
	public CommonOmpDeclarativeNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken,
			SequenceNode<IdentifierExpressionNode> varList) {
		super(source, identifier, body, eofToken);
		this.addChild(varList);// child 1
	}

	@Override
	public OmpNodeKind ompNodeKind() {
		return OmpNodeKind.DECLARATIVE;
	}

	@Override
	public OmpDeclarativeNodeKind ompDeclarativeNodeKind() {
		return OmpDeclarativeNodeKind.THREADPRIVATE;
	}

	@Override
	public void setList(SequenceNode<IdentifierExpressionNode> list) {
		this.setChild(1, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<IdentifierExpressionNode> variables() {
		return (SequenceNode<IdentifierExpressionNode>) this.child(1);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("OmpThreadprivate");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void printExtras(String prefix, PrintStream out) {
		int count;
		SequenceNode<IdentifierExpressionNode> variables = (SequenceNode<IdentifierExpressionNode>) this
				.child(1);

		if (variables != null) {
			count = variables.numChildren();
			if (count > 0) {
				out.println();
				out.print(prefix + "threadprivate(");
				for (int i = 0; i < count; i++) {
					out.print(variables.getSequenceChild(i).getIdentifier().name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
	}

}
