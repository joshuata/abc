package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;

/**
 * An item in a block (compound statement): either a statement or a
 * "declaration."
 * 
 * Every instance of BlockItemNode is also an instance of one of the following:
 * 
 * <ul>
 * <li>StatementNode</li> (includes PragmaNode)
 * <li>StructureOrUnionTypeNode</li>
 * <li>EnumerationTypeNode</li>
 * <li>StaticAssertionNode</li>
 * <li>VariableDeclarationNode</li>
 * <li>FunctionDeclarationNode</li>
 * <li>TypedefDeclarationNode</li>
 * <li>FunctionDefinitionNode</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public interface BlockItemNode extends ASTNode {
	
	public enum BlockItemKind{
		STATEMENT, STRUCT_OR_UNION, ENUMERATOR, 
		STATIC_ASSERTION, 
		ORDINARY_DECLARATION,TYPEDEF, PRAGMA, 
		SCOPED_DECLARATION
	}

	@Override
	BlockItemNode copy();
	
	BlockItemKind blockItemKind();
	
}
