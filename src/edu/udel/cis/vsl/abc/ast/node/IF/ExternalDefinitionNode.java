package edu.udel.cis.vsl.abc.ast.node.IF;

/**
 * A marker interface to mark nodes that can appear in global scope, such as
 * variable declarations, function definitions, etc.
 * 
 * Every instance of ExternalDefinitionNode is also an instance of exactly one
 * of the following types:
 * 
 * <ul>
 * <li>VariableDeclarationNode</li>
 * <li>FunctionDeclarationNode (includes FunctionDefinitionNode)</li>
 * <li>StructureOrUnionTypeNode</li>
 * <li>EnumerationTypeNode</li>
 * <li>PragmaNode</li>
 * <li>StaticAssertionNode</li>
 * <li>TypedefDeclarationNode</li>
 * <li>AssumeNode</li>
 * </ul>
 */
public interface ExternalDefinitionNode extends ASTNode {

	@Override
	ExternalDefinitionNode copy();

}
