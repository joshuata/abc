package edu.udel.cis.vsl.abc.ast.node.IF.expression;

import edu.udel.cis.vsl.abc.ast.value.IF.RealFloatingValue;

public interface FloatingConstantNode extends ConstantNode {

	String wholePart();

	String fractionPart();

	String exponent();

	@Override
	RealFloatingValue getConstantValue();

	@Override
	FloatingConstantNode copy();

}
