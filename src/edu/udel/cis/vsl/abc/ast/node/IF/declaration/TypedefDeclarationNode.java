package edu.udel.cis.vsl.abc.ast.node.IF.declaration;

import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;

public interface TypedefDeclarationNode extends DeclarationNode,
		ExternalDefinitionNode, BlockItemNode {

	@Override
	Typedef getEntity();

	/**
	 * Returns the AST node for the type that is being associated to the typedef
	 * name.
	 * 
	 * @return the type assigned to this typedef name
	 */
	TypeNode getTypeNode();

	void setTypeNode(TypeNode type);
	
	/**
	 * A typedef declaration can have any number of scope parameters associated
	 * to it, e.g.:
	 * 
	 * <pre>
	 * <s1,s2,s3> typedef struct _triple {
	 *   double *<s1> a;
	 *   double *<s2> b;
	 *   double *<s3> c;
	 * } triple;
	 * </pre>
	 * 
	 * When triple is used
	 * 
	 * <pre>
	 * triple<t1,t2,t3> t;
	 * </pre>
	 * 
	 * @return the list of scope identifiers in the declaration, or null if no
	 *         such list occurred
	 */
	SequenceNode<IdentifierNode> getScopeList();


	@Override
	TypedefDeclarationNode copy();
}
