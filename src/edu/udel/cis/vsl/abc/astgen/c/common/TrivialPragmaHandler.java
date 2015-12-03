package edu.udel.cis.vsl.abc.astgen.c.common;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.astgen.c.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.astgen.c.IF.SimpleScope;
import edu.udel.cis.vsl.abc.front.c.parse.IF.ParseTree;

public class TrivialPragmaHandler implements PragmaHandler {

	private String name;

	private ParseTree parseTree;

	public TrivialPragmaHandler(String name, ParseTree parseTree) {
		this.name = name;
		this.parseTree = parseTree;
	}

	@Override
	public EntityKind getEntityKind() {
		return EntityKind.PRAGMA_HANDLER;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ASTNode processPragmaNode(PragmaNode pragmaNode, SimpleScope scope) {
		return pragmaNode;
	}

	@Override
	public ParseTree getParseTree() {
		return parseTree;
	}

}
