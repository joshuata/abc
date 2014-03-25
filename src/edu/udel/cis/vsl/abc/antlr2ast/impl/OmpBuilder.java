package edu.udel.cis.vsl.abc.antlr2ast.impl;

import static edu.udel.cis.vsl.abc.parse.common.OmpParser.AMPERSAND;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.AND;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.ATOMIC;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.BARRIER;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.BITOR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.BITXOR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.COLLAPSE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.COPYIN;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.COPYPRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.CRITICAL;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.DATA_CLAUSE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.DEFAULT;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.DYNAMIC;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.FLUSH;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.FOR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.FST_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.GUIDED;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.IF;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.LST_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.MASTER;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NONE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NOWAIT;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NUM_THREADS;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.OR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.ORDERED;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.PARALLEL;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.PARALLEL_FOR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.PARALLEL_SECTIONS;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.PLUS;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.PRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.REDUCTION;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.RUNTIME;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SCHEDULE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SECTION;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SECTIONS;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SHARED;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SINGLE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.STAR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.STATIC;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.SUB;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.THD_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.UNIQUE_FOR;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.UNIQUE_PARALLEL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode.OmpScheduleKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode.OmpSyncNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode.OmpWorkshareNodeKind;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpNodeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.parse.common.OmpParser;
import edu.udel.cis.vsl.abc.preproc.common.OmpLexer;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Formation;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.util.Pair;

public class OmpBuilder {

	private OmpNodeFactory ompNodeFactory;
	private NodeFactory nodeFactory;
	private TokenFactory sourceFactory;
	private Formation formation;
	private List<CToken> ctokens;
	private Source source;
	private IdentifierNode ompIdentifierNode;
	private CToken eofToken;
	private ASTBuilder astBuilder;
	private SimpleScope scope;

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
	public OmpBuilder(ValueFactory valueFactory, NodeFactory nodeFactory,
			TokenFactory tokenFactory, ASTBuilder astBuilder) {
		this.ompNodeFactory = new CommonOmpNodeFactory(valueFactory);
		this.nodeFactory = nodeFactory;
		this.sourceFactory = tokenFactory;
		this.astBuilder = astBuilder;
	}

	@SuppressWarnings("unchecked")
	public OmpNode getOmpNode(String text, Formation formation, int line,
			int offset, Source source, IdentifierNode identifier,
			CToken eofToken, SimpleScope scope) throws SyntaxException {
		this.formation = formation;
		try {
			ANTLRInputStream input = new ANTLRInputStream(
					new ByteArrayInputStream(text.getBytes()));
			OmpLexer lexer = new OmpLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			OmpParser parser;
			CommonTree rootTree;
			int type;
			
			this.scope = scope;
			this.ctokens = new ArrayList<>();
			this.source = source;
			this.ompIdentifierNode = identifier;
			this.eofToken = eofToken;
			parser = new OmpParser(tokens);
			processTokens(tokens.getTokens(), line, offset);
			rootTree = (CommonTree) parser.openmp_construct().getTree();
			getCTokenList(rootTree);
			type = rootTree.getType();
			switch (type) {
			case PARALLEL_FOR:
				return translateParallelFor(rootTree);
			case PARALLEL_SECTIONS:
				break;
			case PARALLEL:
				return translateParallel(rootTree);
			case FOR:
				return translateFor(rootTree);
			case SECTIONS:
				return translateSections(rootTree);
			case SINGLE:
				break;
			case MASTER:
				return ompNodeFactory.newSyncNode(source, identifier, ctokens,
						eofToken, OmpSyncNodeKind.MASTER);
			case CRITICAL:
				OmpSyncNode criticalNode = ompNodeFactory
						.newSyncNode(source, identifier, ctokens, eofToken,
								OmpSyncNodeKind.CRITICAL);

				if (rootTree.getChildCount() > 0) {
					criticalNode
							.setCriticalName(getIdentifierNode(((CommonTree) rootTree
									.getChild(0)).getToken()));
				}
				return criticalNode;
			case ATOMIC:
				break;
			case ORDERED:
				break;
			case SECTION:
				return ompNodeFactory.newWorkshareNode(source, identifier,
						ctokens, eofToken, OmpWorkshareNodeKind.SECTION);
			case BARRIER:
				return ompNodeFactory.newSyncNode(source, identifier, ctokens,
						eofToken, OmpSyncNodeKind.BARRIER);
			case FLUSH:
			case THD_PRIVATE:
			default:
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private OmpWorkshareNode translateSections(CommonTree sectionsTree) {
		int numChildren = sectionsTree.getChildCount();
		OmpWorkshareNode sectionsNode = ompNodeFactory.newWorkshareNode(source,
				this.ompIdentifierNode, this.ctokens, eofToken,
				OmpWorkshareNodeKind.SECTIONS);

		for (int i = 0; i < numChildren; i++) {
			CommonTree sectionsClause = (CommonTree) sectionsTree.getChild(i);
			int type = sectionsClause.getType();

			switch (type) {
			case DATA_CLAUSE:
				this.translateDataClause(sectionsClause, sectionsNode);
				break;
			case NOWAIT:
				sectionsNode.setNowait(true);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}

		return sectionsNode;
	}

	private OmpForNode translateFor(CommonTree forTree) throws SyntaxException {
		int numChildren = forTree.getChildCount();
		OmpForNode forNode = ompNodeFactory.newForNode(this.source,
				this.ompIdentifierNode, this.ctokens, this.eofToken);

		for (int i = 0; i < numChildren; i++) {
			CommonTree forClause = (CommonTree) (forTree.getChild(i))
					.getChild(0);
			int type = forClause.getType();

			switch (type) {
			case UNIQUE_FOR:
				translateUniqueForClause((CommonTree) forClause.getChild(0),
						forNode);
				break;
			case DATA_CLAUSE:
				this.translateDataClause(forClause, forNode);
				break;
			case NOWAIT:
				forNode.setNowait(true);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}

		return forNode;
	}

	private void translateUniqueForClause(CommonTree forClause,
			OmpForNode forNode) throws SyntaxException {
		int type = forClause.getType();

		switch (type) {
		case ORDERED:
			break;
		case SCHEDULE:
			int scheduleType = forClause.getChild(0).getType();

			switch (scheduleType) {
			case STATIC:
				forNode.setSchedule(OmpScheduleKind.STATIC);
				break;
			case DYNAMIC:
				forNode.setSchedule(OmpScheduleKind.DYNAMIC);
				break;
			case GUIDED:
				forNode.setSchedule(OmpScheduleKind.GUIDED);
				break;
			case RUNTIME:
				forNode.setSchedule(OmpScheduleKind.RUNTIME);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
			if (forClause.getChildCount() > 1) {
				CommonTree chunkSizeTree = (CommonTree) forClause.getChild(1);
				ExpressionNode chunkSizeNode = astBuilder.translateExpression(
						chunkSizeTree, this.scope);

				forNode.setChunsize(chunkSizeNode);
			}

			break;
		case COLLAPSE:
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	private void getCTokenList(CommonTree rootTree) {
		int number = rootTree.getChildCount();
		Token token = rootTree.getToken();
		CToken ctoken = sourceFactory.newCToken(token, formation);

		this.ctokens.add(ctoken);
		for (int i = 0; i < number; i++) {
			getCTokenList((CommonTree) rootTree.getChild(i));
		}
	}

	private OmpNode translateParallel(CommonTree paralle)
			throws SyntaxException {
		int numChildren = paralle.getChildCount();
		OmpParallelNode parallelNode = ompNodeFactory.newParallelNode(
				this.source, this.ompIdentifierNode, this.ctokens,
				this.eofToken);

		for (int i = 0; i < numChildren; i++) {
			CommonTree parallelClause = (CommonTree) paralle.getChild(i);
			int type = parallelClause.getType();

			switch (type) {
			case UNIQUE_PARALLEL:
				this.translateUniqueParallel(parallelClause, parallelNode);
				break;
			case DATA_CLAUSE:
				this.translateDataClause(parallelClause, parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		return parallelNode;
	}

	private void translateUniqueParallel(CommonTree parallelClause,
			OmpParallelNode parallelNode) throws SyntaxException {
		CommonTree child = (CommonTree) parallelClause.getChild(0);
		ExpressionNode expression;

		switch (child.getType()) {
		case IF:
			expression = astBuilder.translateExpression(
					(CommonTree) child.getChild(0), scope);
			parallelNode.setIfClause(expression);
			break;
		case NUM_THREADS:
			expression = astBuilder.translateExpression(
					(CommonTree) child.getChild(0), scope);
			parallelNode.setNumThreads(expression);
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}

	}

	private void processTokens(List<Token> tokens, int line, int colOffset) {
		int number = tokens.size();

		for (int i = 0; i < number; i++) {
			Token token = tokens.get(i);

			token.setLine(line);
			token.setCharPositionInLine(token.getCharPositionInLine()
					+ colOffset);
		}
	}

	private OmpParallelNode translateParallelFor(CommonTree parallelFor) {
		int numChildren = parallelFor.getChildCount();
		OmpParallelNode parallelNode = ompNodeFactory.newParallelNode(
				this.source, this.ompIdentifierNode, this.ctokens,
				this.eofToken);
		OmpForNode forNode = ompNodeFactory.newForNode(this.source, null,
				this.ctokens, this.eofToken);

		for (int i = 0; i < numChildren; i++) {
			CommonTree parallelForClause = (CommonTree) parallelFor.getChild(i);
			int type = parallelForClause.getType();

			switch (type) {
			case UNIQUE_PARALLEL:
				break;
			case UNIQUE_FOR:
				break;
			case DATA_CLAUSE:
				this.translateDataClause(parallelForClause, forNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		parallelNode.setStatementNode(forNode);
		return parallelNode;
	}

	private void translateDataClause(CommonTree dataClause,
			OmpStatementNode ompStatementNode) {
		int numChildren = dataClause.getChildCount();
		CommonTree dataClauseEle;
		int type;

		assert numChildren == 1;
		dataClauseEle = (CommonTree) dataClause.getChild(0);
		type = dataClauseEle.getType();
		switch (type) {
		case PRIVATE:
			ompStatementNode
					.setPrivateList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case FST_PRIVATE:
			ompStatementNode
					.setFirstprivateList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case LST_PRIVATE:
			ompStatementNode
					.setLastprivateList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case SHARED:
			ompStatementNode
					.setSharedList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case COPYIN:
			ompStatementNode
					.setCopyinList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case COPYPRIVATE:
			ompStatementNode
					.setCopyprivateList(translateIdentifierList((CommonTree) dataClauseEle
							.getChild(0)));
			break;
		case DEFAULT:
			if (dataClause.getChild(0).getType() == NONE)
				((OmpParallelNode) ompStatementNode).setDefault(false);
			break;
		case REDUCTION:
			ompStatementNode
					.setReductionList(translateReductionClause(dataClauseEle));
			break;
		default:
			throw new ABCRuntimeException("Invalid data clause");
		}
	}

	private Pair<Operator, ArrayList<IdentifierNode>> translateReductionClause(
			CommonTree reduction) {
		Operator operator = translateOperator(reduction.getChild(0).getType());
		ArrayList<IdentifierNode> list = translateIdentifierList((CommonTree) reduction
				.getChild(1));

		return new Pair<>(operator, list);
	}

	private Operator translateOperator(int type) {
		switch (type) {
		case PLUS:
			return Operator.PLUS;
		case STAR:
			return Operator.TIMES;
		case SUB:
			return Operator.MINUS;
		case AMPERSAND:
			return Operator.BITAND;
		case BITXOR:
			return Operator.BITXOR;
		case BITOR:
			return Operator.BITOR;
		case AND:
			return Operator.LAND;
		case OR:
			return Operator.LOR;
		default:
			throw new ABCUnsupportedException("reduction operator of type "
					+ type);
		}
	}

	private ArrayList<IdentifierNode> translateIdentifierList(
			CommonTree identifierList) {
		int numChildren = identifierList.getChildCount();
		ArrayList<IdentifierNode> list = new ArrayList<>(numChildren);

		for (int i = 0; i < numChildren; i++) {
			list.add(getIdentifierNode(((CommonTree) identifierList.getChild(i))
					.getToken()));
		}
		return list;
	}

	private IdentifierNode getIdentifierNode(Token token) {
		CToken ctoken = sourceFactory.newCToken(token, formation);
		Source source = sourceFactory.newSource(ctoken);

		return nodeFactory.newIdentifierNode(source, ctoken.getText());
	}

	public OmpNodeFactory ompNodeFactory() {
		return this.ompNodeFactory;
	}

}
