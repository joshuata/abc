package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeVariable;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory.Answer;

public class CommonScopeVariable extends CommonVariable implements
		ScopeVariable {

	public CommonScopeVariable(String name, LinkageKind linkage, Type type) {
		super(name, linkage, type);
	}

	@Override
	public boolean isScalar() {
		return false;
	}

	@Override
	public Answer isZero() {
		return Answer.NO;
	}
}
