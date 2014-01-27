package edu.udel.cis.vsl.abc.program.common;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class CommonProgramFactory implements ProgramFactory {

	private ASTFactory astFactory;

	private Analyzer standardAnalyzer;

	private Transformer pruner;

	private Transformer sideEffectRemover;

	private Transformer accuracyHelper;

	public CommonProgramFactory(ASTFactory factory, Analyzer standardAnalyzer,
			Transformer pruner, Transformer sideEffectRemover,
			Transformer accuracyHelper) {
		this.astFactory = factory;
		this.standardAnalyzer = standardAnalyzer;
		this.pruner = pruner;
		this.sideEffectRemover = sideEffectRemover;
		this.accuracyHelper = accuracyHelper;
	}

	@Override
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	@Override
	public Analyzer getStandardAnalyzer() {
		return standardAnalyzer;
	}

	@Override
	public Transformer getPruner() {
		return pruner;
	}

	@Override
	public Transformer getSideEffectRemover() {
		return sideEffectRemover;
	}

	@Override
	public Transformer getAccuracyHelper() {
		return accuracyHelper;
	}

	@Override
	public Program newProgram(AST ast) throws SyntaxException {
		return new CommonProgram(standardAnalyzer, pruner, sideEffectRemover,
				accuracyHelper, ast);
	}

}
