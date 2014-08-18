package edu.udel.cis.vsl.abc.antlr2ast.common;

import edu.udel.cis.vsl.abc.antlr2ast.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.antlr2ast.IF.SimpleScope;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;

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
