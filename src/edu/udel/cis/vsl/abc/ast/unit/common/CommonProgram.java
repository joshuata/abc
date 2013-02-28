package edu.udel.cis.vsl.abc.ast.unit.common;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.unit.IF.Program;
import edu.udel.cis.vsl.abc.ast.unit.IF.TranslationUnit;

public class CommonProgram implements Program {

	private List<TranslationUnit> translationUnits;

	public CommonProgram(List<TranslationUnit> translationUnits) {
		this.translationUnits = translationUnits;

		for (TranslationUnit t : this.translationUnits) {
			t.getClass();
		}
	}

}
