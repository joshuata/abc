package edu.udel.cis.vsl.abc.program.common;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.transform.Transform;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

public class CommonProgram implements Program {

	private Analyzer standardAnalyzer;

	// private Transformer pruner;

	// private Transformer sideEffectRemover;

	// private MPITransformer mpiTransformer;

	private AST ast;

	public CommonProgram(Analyzer standardAnalyzer, AST ast,
			SymbolicUniverse universe) throws SyntaxException {
		this.standardAnalyzer = standardAnalyzer;
		// this.pruner = pruner;
		// this.sideEffectRemover = sideEffectRemover;
		this.ast = ast;
		// this.mpiTransformer = mpiTransformer;
		 standardAnalyzer.clear(ast);
		 standardAnalyzer.analyze(ast);
	}

	@Override
	public AST getAST() {
		return ast;
	}

	// @Override
	// public void removeSideEffects() throws SyntaxException {
	// ast = sideEffectRemover.transform(ast);
	// standardAnalyzer.clear(ast);
	// // debugging code...
	// // System.out.println("\n\nRemoved side effects...\n\n");
	// // ast.print(System.out);
	// standardAnalyzer.analyze(ast);
	// }

	// @Override
	// public void prune() throws SyntaxException {
	// ast = pruner.transform(ast);
	// standardAnalyzer.clear(ast);
	// standardAnalyzer.analyze(ast);
	// }

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
	public void apply(Transformer transformer) throws SyntaxException {
		ast = transformer.transform(ast);
		// MPI transformer ignores the standard analysis for this moment.
		// if (!transformer.getCode().equals(MPITransformer.CODE)) {
		standardAnalyzer.clear(ast);
		standardAnalyzer.analyze(ast);
		// }
	}

	@Override
	public void applyTransformer(String code) throws SyntaxException {
		Transformer transformer = Transform.newTransformer(code,
				ast.getASTFactory());

		apply(transformer);
	}

	@Override
	public void apply(Iterable<Transformer> transformers)
			throws SyntaxException {
		for (Transformer transformer : transformers) {
			ast = transformer.transform(ast);
			standardAnalyzer.clear(ast);
			standardAnalyzer.analyze(ast);
		}
	}

	@Override
	public void applyTransformers(Iterable<String> codes)
			throws SyntaxException {
		List<Transformer> transformers = new LinkedList<>();
		ASTFactory astFactory = ast.getASTFactory();

		for (String code : codes)
			transformers.add(Transform.newTransformer(code, astFactory));
		apply(transformers);
	}
}
