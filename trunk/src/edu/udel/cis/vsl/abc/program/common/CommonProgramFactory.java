package edu.udel.cis.vsl.abc.program.common;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

public class CommonProgramFactory implements ProgramFactory {

	private ASTFactory astFactory;

	private Analyzer standardAnalyzer;

	public CommonProgramFactory(ASTFactory factory, Analyzer standardAnalyzer) {
		this.astFactory = factory;
		this.standardAnalyzer = standardAnalyzer;
	}

	@Override
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	// @Override
	// public Analyzer getStandardAnalyzer() {
	// return standardAnalyzer;
	// }

	// @Override
	// public Transformer getPruner() {
	// return pruner;
	// }
	//
	// @Override
	// public Transformer getSideEffectRemover() {
	// return sideEffectRemover;
	// }

	@Override
	public Program newProgram(AST ast, SymbolicUniverse universe)
			throws SyntaxException {
		return new CommonProgram(standardAnalyzer, ast, universe);
	}

}
