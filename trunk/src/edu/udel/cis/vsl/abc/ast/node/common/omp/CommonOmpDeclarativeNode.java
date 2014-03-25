package edu.udel.cis.vsl.abc.ast.node.common.omp;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonOmpDeclarativeNode extends CommonOmpNode implements
		OmpDeclarativeNode {

	private ArrayList<IdentifierNode> variables;

	public CommonOmpDeclarativeNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken eofToken,
			ArrayList<IdentifierNode> varList) {
		super(source, identifier, body, eofToken);
		this.variables = varList;
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
	public void setList(ArrayList<IdentifierNode> list) {
		this.variables = list;
	}

	@Override
	public ArrayList<IdentifierNode> variables() {
		return this.variables;
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("OmpThreadprivate");
	}

	@Override
	protected void printExtras(String prefix, PrintStream out) {
		int count;

		if (this.variables != null) {
			count = variables.size();
			if (count > 0) {
				out.println();
				out.print(prefix + "threadprivate(");
				for (int i = 0; i < count; i++) {
					out.print(variables.get(i).name());
					if (i < count - 1)
						out.print(",");
				}
				out.print(")");
			}
		}
	}

}
