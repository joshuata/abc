package edu.udel.cis.vsl.abc.ast;

import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.common.CommonASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class ASTs {

	public static ASTFactory newUnitFactory(NodeFactory nodeFactory,
			TokenFactory tokenFactory, TypeFactory typeFactory) {
		return new CommonASTFactory(nodeFactory, tokenFactory, typeFactory);
	}

}
