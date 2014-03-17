package edu.udel.cis.vsl.abc.antlr2ast.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.parse.common.OmpParser;
import edu.udel.cis.vsl.abc.preproc.common.OmpLexer;

public class OmpBuilder {
	private OmpParser parser;
	private CommonTree rootTree;

	/**
	 * Constructs a new OmpBuilder for the given ANTLR tree of OpenMP.
	 * 
	 * @param factory
	 *            an ASTFactory to use
	 * @param rootTree
	 *            the root of the ANTLR tree
	 * @param tokenSource
	 *            the CTokenSource used to produce the ANTLR tree
	 * 
	 */
	public OmpBuilder(String text) {
		try {
			ANTLRInputStream input = new ANTLRInputStream(
					new ByteArrayInputStream(text.getBytes()));
			OmpLexer lexer = new OmpLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			parser = new OmpParser(tokens);
			rootTree = (CommonTree) parser.openmp_construct().getTree();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//TODO
	public ASTNode getOmpNode() {
		return null;
	}

	public CommonTree rootTree() {
		return this.rootTree;
	}

}
