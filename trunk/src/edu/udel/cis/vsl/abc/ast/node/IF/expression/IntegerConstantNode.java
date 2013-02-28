package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.value.IF.IntegerValue;

public interface IntegerConstantNode extends ConstantNode {

	@Override
	IntegerValue getConstantValue();

}
