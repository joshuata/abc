package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.CommonEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.PragmaHandler;

public class CommonPragmaHandler extends CommonEntity implements PragmaHandler {

	public CommonPragmaHandler(String name) {
		super(EntityKind.PRAGMA_HANDLER, name, LinkageKind.NONE);
	}

	public String toString() {
		return "PragmaHandler[" + getName() + "]";
	}

}
