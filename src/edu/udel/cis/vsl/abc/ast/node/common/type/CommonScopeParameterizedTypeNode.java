package edu.udel.cis.vsl.abc.ast.node.common.type;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ScopeParameterizedTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonScopeParameterizedTypeNode extends CommonTypeNode implements
		ScopeParameterizedTypeNode {

	public CommonScopeParameterizedTypeNode(Source source,
			SequenceNode<VariableDeclarationNode> scopeList, TypeNode body) {
		super(source, TypeNodeKind.SCOPE_PARAMETERIZED, scopeList, body);
	}

	@Override
	public TypeNode copy() {
		return new CommonScopeParameterizedTypeNode(getSource(),
				duplicate(getScopeParameters()), duplicate(getBody()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<VariableDeclarationNode> getScopeParameters() {
		return (SequenceNode<VariableDeclarationNode>) child(0);
	}

	@Override
	public TypeNode getBody() {
		return (TypeNode) child(1);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("ScopeParameterizedTypeNode");
	}

}
