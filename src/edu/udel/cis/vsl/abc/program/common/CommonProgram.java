package edu.udel.cis.vsl.abc.program.common;

import java.io.PrintStream;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public class CommonProgram implements Program {

	private Analyzer standardAnalyzer;

	private Transformer pruner;

	private Transformer sideEffectRemover;

	private AST ast;

	public CommonProgram(Analyzer standardAnalyzer, Transformer pruner,
			Transformer sideEffectRemover, AST ast)
			throws SyntaxException {
		this.standardAnalyzer = standardAnalyzer;
		this.pruner = pruner;
		this.sideEffectRemover = sideEffectRemover;
		this.ast = ast;
		standardAnalyzer.clear(ast);
		standardAnalyzer.analyze(ast);
	}

	@Override
	public AST getAST() {
		return ast;
	}

	@Override
	public void removeSideEffects() throws SyntaxException {
		ast = sideEffectRemover.transform(ast);
		standardAnalyzer.clear(ast);
		// debugging code...
		// System.out.println("\n\nRemoved side effects...\n\n");
		// ast.print(System.out);
		standardAnalyzer.analyze(ast);
	}

	@Override
	public void prune() throws SyntaxException {
		ast = pruner.transform(ast);
		standardAnalyzer.clear(ast);
		standardAnalyzer.analyze(ast);
	}

	@Override
	public void print(PrintStream out) {
		ast.print(out);
	}

	@Override
	public void printSymbolTable(PrintStream out) {
		ast.getRootNode().getScope().print(out);
	}

	@Override
	public TokenFactory getTokenFactory() {
		return ast.getASTFactory().getTokenFactory();
	}

	@Override
	public void transform(TransformMode mode) {
		// TODO Auto-generated method stub
		
	}

}
