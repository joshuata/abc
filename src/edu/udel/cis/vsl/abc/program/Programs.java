package edu.udel.cis.vsl.abc.program;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.program.common.CommonProgramFactory;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class Programs {

	public static ProgramFactory newProgramFactory(ASTFactory factory,
			Analyzer standardAnalyzer, Transformer pruner,
			Transformer sideEffectRemover, Transformer accuracyHelper) {
		return new CommonProgramFactory(factory, standardAnalyzer, pruner,
				sideEffectRemover, accuracyHelper);
	}

}
