package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonFunctionDeclarationNode extends
		CommonOrdinaryDeclarationNode implements FunctionDeclarationNode {

	private boolean inlineFunctionSpecifier = false;

	private boolean noreturnFunctionSpecifier = false;

	public CommonFunctionDeclarationNode(Source source,
			IdentifierNode identifier, FunctionTypeNode type,
			SequenceNode<ContractNode> contract) {
		super(source, identifier, type);
		addChild(contract); // child 2
	}

	@Override
	public Function getEntity() {
		return (Function) super.getEntity();
	}

	@Override
	public boolean hasInlineFunctionSpecifier() {
		return inlineFunctionSpecifier;
	}

	@Override
	public void setInlineFunctionSpecifier(boolean value) {
		this.inlineFunctionSpecifier = value;
	}

	@Override
	public boolean hasNoreturnFunctionSpecifier() {
		return this.noreturnFunctionSpecifier;
	}

	@Override
	public void setNoreturnFunctionSpecifier(boolean value) {
		this.noreturnFunctionSpecifier = value;
	}

	protected void printKind(PrintStream out) {
		out.print("FunctionDeclaration");
	}

	@Override
	protected void printBody(PrintStream out) {
		boolean needSeparator = false;

		printKind(out);
		if (hasExternStorage()) {
			out.print("[");
			out.print("extern");
			needSeparator = true;
		}
		if (hasStaticStorage()) {
			out.print(needSeparator ? ", " : "[");
			out.print("static");
			needSeparator = true;
		}
		if (inlineFunctionSpecifier) {
			out.print(needSeparator ? ", " : "[");
			out.print("inline");
			needSeparator = true;
		}
		if (noreturnFunctionSpecifier) {
			out.print(needSeparator ? ", " : "[");
			out.print("noreturn");
			needSeparator = true;
		}
		if (needSeparator)
			out.print("]");
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<ContractNode> getContract() {
		return (SequenceNode<ContractNode>) child(2);
	}

	@Override
	public void setContract(SequenceNode<ContractNode> contract) {
		setChild(2, contract);
	}

	@Override
	public FunctionTypeNode getTypeNode() {
		return (FunctionTypeNode) super.getTypeNode();
	}

	@Override
	public FunctionDeclarationNode copy() {
		CommonFunctionDeclarationNode result = new CommonFunctionDeclarationNode(
				getSource(), duplicate(getIdentifier()),
				duplicate(getTypeNode()), duplicate(getContract()));

		result.setInlineFunctionSpecifier(hasInlineFunctionSpecifier());
		result.setNoreturnFunctionSpecifier(hasNoreturnFunctionSpecifier());
		copyStorage(result);
		return result;
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.FUNCTION_DECLARATION;
	}

	@Override
	public OrdinaryDeclarationKind ordinaryDeclarationKind() {
		return OrdinaryDeclarationKind.FUNCTION_DECLARATION;
	}

}
