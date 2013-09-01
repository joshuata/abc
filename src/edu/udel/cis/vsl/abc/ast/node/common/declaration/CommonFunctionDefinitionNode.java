package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonFunctionDefinitionNode extends CommonFunctionDeclarationNode
		implements FunctionDefinitionNode {

	public CommonFunctionDefinitionNode(Source source,
			IdentifierNode identifier, FunctionTypeNode type,
			SequenceNode<ContractNode> contract,
			SequenceNode<IdentifierNode> scopeList,
			CompoundStatementNode statement) {
		super(source, identifier, type, contract, scopeList);
		addChild(statement);
	}

	@Override
	public CompoundStatementNode getBody() {
		return (CompoundStatementNode) child(4);
	}

	@Override
	public void setBody(CompoundStatementNode statement) {
		setChild(4, statement);
	}

	@Override
	protected void printKind(PrintStream out) {
		out.print("FunctionDefinition");
	}

	@Override
	public FunctionDefinitionNode copy() {
		CommonFunctionDefinitionNode result = new CommonFunctionDefinitionNode(
				getSource(), duplicate(getIdentifier()),
				duplicate(getTypeNode()), duplicate(getContract()),
				duplicate(getScopeList()), duplicate(getBody()));

		result.setInlineFunctionSpecifier(hasInlineFunctionSpecifier());
		result.setNoreturnFunctionSpecifier(hasNoreturnFunctionSpecifier());
		copyStorage(result);
		return result;
	}

}
