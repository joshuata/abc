package edu.udel.cis.vsl.abc.ast.node.common.declaration;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.CommonASTNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonScopeParameterizedDeclarationNode extends CommonASTNode
		implements ScopeParameterizedDeclarationNode {

	public CommonScopeParameterizedDeclarationNode(Source source,
			SequenceNode<VariableDeclarationNode> parameters,
			DeclarationNode baseDeclaration) {
		super(source, parameters, baseDeclaration);
	}

	@Override
	public IdentifierNode getIdentifier() {
		return baseDeclaration().getIdentifier();
	}

	@Override
	public String getName() {
		return baseDeclaration().getName();
	}

	@Override
	public void setIdentifier(IdentifierNode identifier) {
		baseDeclaration().setIdentifier(identifier);
	}

	@Override
	public boolean isDefinition() {
		return baseDeclaration().isDefinition();
	}

	@Override
	public void setIsDefinition(boolean value) {
		baseDeclaration().setIsDefinition(value);
	}

	@Override
	public Entity getEntity() {
		return baseDeclaration().getEntity();
	}

	@Override
	public void setEntity(Entity entity) {
		baseDeclaration().setEntity(entity);
	}

	@Override
	public ScopeParameterizedDeclarationNode copy() {
		return new CommonScopeParameterizedDeclarationNode(getSource(),
				duplicate(parameters()), duplicate(baseDeclaration()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceNode<VariableDeclarationNode> parameters() {
		return (SequenceNode<VariableDeclarationNode>) child(0);
	}

	@Override
	public DeclarationNode baseDeclaration() {
		return (DeclarationNode) child(1);
	}

	@Override
	protected void printBody(PrintStream out) {
		out.print("ScopeParameterizedDeclaration");
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.SCOPE_PARAMETERIZED_DECLARATION;
	}

	@Override
	public BlockItemKind blockItemKind() {
		return BlockItemKind.SCOPED_DECLARATION;
	}

}
