package edu.udel.cis.vsl.abc.program.IF;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;

public interface ProgramFactory {

	ASTFactory getASTFactory();

	Analyzer getStandardAnalyzer();

	Transformer getPruner();

	Transformer getSideEffectRemover();

	Program newProgram(AST ast) throws SyntaxException;

}
