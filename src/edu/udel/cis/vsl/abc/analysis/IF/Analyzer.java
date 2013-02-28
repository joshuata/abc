package edu.udel.cis.vsl.abc.analysis.IF;

import edu.udel.cis.vsl.abc.ast.unit.IF.TranslationUnit;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public interface Analyzer {

	void analyze(TranslationUnit unit) throws SyntaxException;

}
