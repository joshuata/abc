package edu.udel.cis.vsl.abc.ast.node.IF.statement;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;

/**
 * An item that can appear in a "block". Every instance of BlockItemNode is also
 * an instance of one of the following:
 * 
 * <ul>
 * <li>{@link StatementNode} (includes {@link PragmaNode})</li>
 * <li>{@link StructureOrUnionTypeNode}</li>
 * <li>{@link EnumerationTypeNode}</li>
 * <li>{@link StaticAssertionNode}</li>
 * <li>{@link VariableDeclarationNode}</li>
 * <li>{@link FunctionDeclarationNode}</li>
 * <li>{@link TypedefDeclarationNode}</li>
 * <li>{@link FunctionDefinitionNode}</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public interface BlockItemNode extends ASTNode {

	public enum BlockItemKind {
		STATEMENT,
		STRUCT_OR_UNION,
		ENUMERATOR,
		STATIC_ASSERTION,
		ORDINARY_DECLARATION,
		TYPEDEF,
		PRAGMA,
		SCOPED_DECLARATION
	}

	@Override
	BlockItemNode copy();

	BlockItemKind blockItemKind();

}
