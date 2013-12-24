package edu.udel.cis.vsl.abc.ast.node.IF.statement;

public interface StatementNode extends BlockItemNode {

	public enum StatementKind {
		PRAGMA, WHEN, ASSERT, ASSUME, EXPRESSION, COMPOUND, FOR, LOOP, IF, NULL, CHOOSE, GOTO, LABELED, RETURN, SWITCH, JUMP, WAIT, ATOMIC
	}

	@Override
	StatementNode copy();

	/**
	 * Different statement nodes have different statement kind. For example, a
	 * while statement node has the statement kind WHILE, an if statement node
	 * has the kind IF, etc.
	 * 
	 * @return The statement kind defined as an enum element
	 */
	StatementKind statementKind();
}
