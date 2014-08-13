package edu.udel.cis.vsl.abc.ast.node.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.token.IF.Source;

public class CommonIdentifierNode extends CommonASTNode implements
		IdentifierNode {

	private String name;

	private Entity entity;

	public CommonIdentifierNode(Source source, String name) {
		super(source);
		this.name = name;
	}

	@Override
	public void printBody(PrintStream out) {
		out.print("Identifier[" + name + "]");
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public IdentifierNode copy() {
		return new CommonIdentifierNode(getSource(), name);
	}

	@Override
	public NodeKind nodeKind() {
		return NodeKind.IDENTIFIER;
	}

}
