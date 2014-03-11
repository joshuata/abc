package edu.udel.cis.vsl.abc.reason.common;

import edu.udel.cis.vsl.abc.reason.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

/**
 * The symbolic reasoner to be used. Translation from ABC expressions to SARL
 * expressions can be implemented here.
 * 
 * @author
 * 
 */
public class CommonReasoner implements Reasoner {

	private SymbolicUniverse universe;

	public CommonReasoner(SymbolicUniverse universe) {
		this.universe = universe;
	}

	@Override
	public SymbolicUniverse universe() {
		return this.universe;
	}

}
