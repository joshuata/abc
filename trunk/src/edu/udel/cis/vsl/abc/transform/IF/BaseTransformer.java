package edu.udel.cis.vsl.abc.transform.IF;

import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;

public abstract class BaseTransformer implements Transformer {

	protected String code;

	protected String longName;

	protected String shortDescription;

	protected ASTFactory astFactory;

	protected NodeFactory nodeFactory;

	protected BaseTransformer(String code, String longName,
			String shortDescription, ASTFactory astFactory) {
		this.code = code;
		this.longName = longName;
		this.shortDescription = shortDescription;
		this.astFactory = astFactory;
		this.nodeFactory = astFactory.getNodeFactory();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String toString() {
		return longName;
	}

}
