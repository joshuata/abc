package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.ScopeValue;
import edu.udel.cis.vsl.abc.ast.entity.IF.Variable;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;

public class CommonScopeVariable extends CommonVariable implements Variable,
		ScopeValue {

	public CommonScopeVariable(String name, LinkageKind linkage, Type type) {
		super(name, linkage, type);
	}
}
