package edu.udel.cis.vsl.abc.antlr2ast.impl;

import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.AMPERSAND;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.ATOMIC;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.BARRIER;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.BITOR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.BITXOR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.COLLAPSE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.COPYIN;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.COPYPRIVATE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.CRITICAL;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.DATA_CLAUSE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.DEFAULT;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.DYNAMIC;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.FLUSH;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.FOR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.FST_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.GUIDED;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.IDENTIFIER;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.IF;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.LST_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.MASTER;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.NONE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.NOWAIT;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.NUM_THREADS;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.ORDERED;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.PARALLEL;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.PARALLEL_FOR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.PARALLEL_SECTIONS;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.PLUS;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.PRIVATE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.REDUCTION;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.RUNTIME;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SCHEDULE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SECTION;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SECTIONS;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SHARED;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SINGLE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.STAR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.STATIC;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.SUB;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.THD_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.UNIQUE_FOR;
import static edu.udel.cis.vsl.abc.parse.IF.OmpCParser.UNIQUE_PARALLEL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.OmpBuilder;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode.OmpScheduleKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode.OmpWorksharingNodeKind;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.parse.IF.OmpCParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.IF.TokenUtils;

/**
 * Builds an OpenMP AST node from a given token collection representing a
 * certain OpenMP pragma.
 * 
 * @author Manchun Zheng (zmanchun)
 * 
 */
public class CommonOmpBuilder implements OmpBuilder {

	/**
	 * The node factory used to create new AST nodes.
	 */
	private NodeFactory nodeFactory;

	/**
	 * The token factory used to create new tokens.
	 */
	private TokenFactory sourceFactory;

	/**
	 * The AST builder to be used to create expressions.
	 */
	private ASTBuilder astBuilder;

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
	public CommonOmpBuilder(ASTFactory astFactory, ASTBuilder astBuilder) {
		this.nodeFactory = astFactory.getNodeFactory();
		this.sourceFactory = astFactory.getTokenFactory();
		this.astBuilder = astBuilder;
	}

	/**
	 * Translates C tokens into OpenMP tokens.
	 * 
	 * @param ctokens
	 *            The list of C tokens to be translated.
	 * @return
	 */
	private TokenStream ompTokenStream(List<CToken> ctokens) {
		int number = ctokens.size();
		TokenSource source;

		for (int i = 0; i < number; i++) {
			CToken token = ctokens.get(i);
			int type = token.getType();

			if (type == IDENTIFIER) {
				switch (token.getText()) {
				case "barrier":
					token.setType(BARRIER);
					break;
				case "collapse":
					token.setType(COLLAPSE);
					break;
				case "copyin":
					token.setType(COPYIN);
					break;
				case "copyprivate":
					token.setType(COPYPRIVATE);
					break;
				case "critical":
					token.setType(CRITICAL);
					break;
				case "default":
					token.setType(DEFAULT);
					break;
				case "dynamic":
					token.setType(DYNAMIC);
					break;
				case "firstprivate":
					token.setType(FST_PRIVATE);
					break;
				case "flush":
					token.setType(FLUSH);
					break;
				case "guided":
					token.setType(GUIDED);
					break;
				case "lastprivate":
					token.setType(LST_PRIVATE);
					break;
				case "master":
					token.setType(MASTER);
					break;
				case "none":
					token.setType(NONE);
					break;
				case "nowait":
					token.setType(NOWAIT);
					break;
				case "num_threads":
					token.setType(NUM_THREADS);
					break;
				case "ordered":
					token.setType(ORDERED);
					break;
				case "parallel":
					token.setType(PARALLEL);
					break;
				case "private":
					token.setType(PRIVATE);
					break;
				case "reduction":
					token.setType(REDUCTION);
					break;
				case "runtime":
					token.setType(RUNTIME);
					break;
				case "schedule":
					token.setType(SCHEDULE);
					break;
				case "sections":
					token.setType(SECTIONS);
					break;
				case "section":
					token.setType(SECTION);
					break;
				case "shared":
					token.setType(SHARED);
					break;
				case "static":
					token.setType(STATIC);
					break;
				case "threadprivate":
					token.setType(THD_PRIVATE);
					break;
				default:
				}
			}
			if (i == number - 1) {
				token.setNext(null);
			}
		}
		source = TokenUtils.makeTokenSourceFromList(ctokens.get(0));
		return new CommonTokenStream(source);
	}

	@Override
	public OmpNode getOmpNode(Source source, Iterator<CToken> ctokens)
			throws SyntaxException {
		try {
			List<CToken> tokenList = new ArrayList<>();
			TokenStream tokens;
			CommonTree rootTree;
			int type;

			while (ctokens.hasNext()) {
				tokenList.add(ctokens.next());
			}
			tokens = this.ompTokenStream(tokenList);
			rootTree = OmpCParser.parse(tokens);
			type = rootTree.getType();
			switch (type) {
			case PARALLEL_FOR:
				return translateParallelFor(source, rootTree);
			case PARALLEL_SECTIONS:
				return translateParallelSections(source, rootTree);
			case PARALLEL:
				return translateParallel(source, rootTree);
			case FOR:
				return translateFor(source, rootTree);
			case SECTIONS:
				return translateWorkshare(source, rootTree,
						OmpWorksharingNodeKind.SECTIONS);
			case SINGLE:
				return translateWorkshare(source, rootTree,
						OmpWorksharingNodeKind.SINGLE);
			case MASTER:
				return nodeFactory.newOmpMasterNode(source, null);
			case CRITICAL:
				OmpSyncNode criticalNode = nodeFactory.newOmpCriticalNode(
						source, null, null);

				if (rootTree.getChildCount() > 0) {
					criticalNode.setCriticalName(this
							.translateIdentifier((CommonTree) rootTree
									.getChild(0)));
				}
				return criticalNode;
			case ORDERED:
				return nodeFactory.newOmpOrederedNode(source, null);
			case SECTION:
				return nodeFactory.newOmpSectionNode(source, null);
			case BARRIER:
				return nodeFactory.newOmpBarrierNode(source);
			case FLUSH:
				return nodeFactory.newOmpFlushNode(
						source,
						translateIdentifierList(source, "flushList",
								(CommonTree) rootTree.getChild(0)));
			case THD_PRIVATE:
				return nodeFactory.newOmpThreadprivateNode(
						source,
						translateIdentifierList(source, "threadprivateList",
								(CommonTree) rootTree.getChild(0)));
			case ATOMIC:
				throw new ABCUnsupportedException("atomic construct of OpenMP");
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		} catch (RecognitionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private OmpWorksharingNode translateWorkshare(Source source,
			CommonTree workshareTree, OmpWorksharingNodeKind kind) {
		int numChildren = workshareTree.getChildCount();
		OmpWorksharingNode workshareNode = nodeFactory.newWorksharingNode(
				source, kind);
		boolean hasNowait = false;

		for (int i = 0; i < numChildren; i++) {
			CommonTree sectionsClause = (CommonTree) workshareTree.getChild(i);
			int type = sectionsClause.getType();

			switch (type) {
			case DATA_CLAUSE:
				this.translateDataClause(source, sectionsClause, workshareNode);
				break;
			case NOWAIT:
				if (!hasNowait) {
					hasNowait = true;
				} else {
					throw new ABCRuntimeException(
							"At most one nowait directive is allowed in an OpenMP construct.",
							(sourceFactory.newSource((CToken) sectionsClause
									.getToken()).getSummary(false)));
				}
				workshareNode.setNowait(true);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		return workshareNode;
	}

	private OmpForNode translateFor(Source source, CommonTree forTree)
			throws SyntaxException {
		int numChildren = forTree.getChildCount();
		OmpForNode forNode = nodeFactory.newOmpForNode(source, null);

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
				this.translateDataClause(source, forClause, forNode);
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
						chunkSizeTree, null);

				forNode.setChunsize(chunkSizeNode);
			}

			break;
		case COLLAPSE:
			break;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	private OmpNode translateParallel(Source source, CommonTree paralle)
			throws SyntaxException {
		int numChildren = paralle.getChildCount();
		OmpParallelNode parallelNode = nodeFactory.newOmpParallelNode(source,
				null);
		boolean hasIf = false;
		boolean hasNumThreads = false;

		for (int i = 0; i < numChildren; i++) {
			CommonTree parallelClause = (CommonTree) paralle.getChild(i);
			int type = parallelClause.getType();

			switch (type) {
			case UNIQUE_PARALLEL:
				int result = this.translateUniqueParallel(parallelClause,
						parallelNode);

				if (result == IF) {
					if (!hasIf) {
						hasIf = true;
					} else {
						throw new ABCRuntimeException(
								"At most one if clause is allowed in an OpenMP parallel construct.",
								(sourceFactory
										.newSource((CToken) parallelClause
												.getToken()).getSummary(false)));
					}
				} else if (result == NUM_THREADS) {
					if (!hasNumThreads) {
						hasNumThreads = true;
					} else {
						throw new ABCRuntimeException(
								"At most one num_threads() clause is allowed in an OpenMP parallel construct.",
								(sourceFactory
										.newSource((CToken) parallelClause
												.getToken()).getSummary(false)));
					}
				}
				break;
			case DATA_CLAUSE:
				this.translateDataClause(source, parallelClause, parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		return parallelNode;
	}

	private int translateUniqueParallel(CommonTree parallelClause,
			OmpParallelNode parallelNode) throws SyntaxException {
		CommonTree child = (CommonTree) parallelClause.getChild(0);
		ExpressionNode expression;

		switch (child.getType()) {
		case IF:
			expression = astBuilder.translateExpression(
					(CommonTree) child.getChild(0), null);
			parallelNode.setIfClause(expression);
			return IF;
		case NUM_THREADS:
			expression = astBuilder.translateExpression(
					(CommonTree) child.getChild(0), null);
			parallelNode.setNumThreads(expression);
			return NUM_THREADS;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	private OmpParallelNode translateParallelFor(Source source,
			CommonTree parallelFor) throws SyntaxException {
		int numChildren = parallelFor.getChildCount();
		OmpParallelNode parallelNode = nodeFactory.newOmpParallelNode(source,
				null);
		OmpForNode forNode = nodeFactory.newOmpForNode(source, null);

		for (int i = 0; i < numChildren; i++) {
			CommonTree parallelForClause = (CommonTree) parallelFor.getChild(i);
			int type = parallelForClause.getType();

			switch (type) {
			case UNIQUE_PARALLEL:
				this.translateUniqueParallel(parallelForClause, parallelNode);
				break;
			case UNIQUE_FOR:
				this.translateUniqueForClause(parallelForClause, forNode);
				break;
			case DATA_CLAUSE:
				this.translateDataClause(source, parallelForClause,
						parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		parallelNode.setStatementNode(forNode);
		return parallelNode;
	}

	private OmpParallelNode translateParallelSections(Source source,
			CommonTree parallelSections) throws SyntaxException {
		int numChildren = parallelSections.getChildCount();
		OmpParallelNode parallelNode = nodeFactory.newOmpParallelNode(source,
				null);
		OmpWorksharingNode sectionsNode = nodeFactory.newOmpSectionsNode(
				source, null);

		for (int i = 0; i < numChildren; i++) {
			CommonTree parallelSectionsClause = (CommonTree) parallelSections
					.getChild(i);
			int type = parallelSectionsClause.getType();

			switch (type) {
			case UNIQUE_PARALLEL:
				this.translateUniqueParallel(parallelSectionsClause,
						parallelNode);
				break;
			case DATA_CLAUSE:
				this.translateDataClause(source, parallelSectionsClause,
						parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		parallelNode.setStatementNode(sectionsNode);
		return parallelNode;
	}

	private void translateDataClause(Source source, CommonTree dataClause,
			OmpStatementNode ompStatementNode) {
		int numChildren = dataClause.getChildCount();
		CommonTree dataClauseEle;
		int type;

		assert numChildren == 1;
		dataClauseEle = (CommonTree) dataClause.getChild(0);
		type = dataClauseEle.getType();
		switch (type) {
		case PRIVATE:
			ompStatementNode.setPrivateList(translateIdentifierList(source,
					"privateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case FST_PRIVATE:
			ompStatementNode.setFirstprivateList(translateIdentifierList(
					source, "firstprivateList",
					(CommonTree) dataClauseEle.getChild(0)));
			break;
		case LST_PRIVATE:
			ompStatementNode.setLastprivateList(translateIdentifierList(source,
					"lastprivateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case SHARED:
			ompStatementNode.setSharedList(translateIdentifierList(source,
					"sharedList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case COPYIN:
			ompStatementNode.setCopyinList(translateIdentifierList(source,
					"copyinList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case COPYPRIVATE:
			ompStatementNode.setCopyprivateList(translateIdentifierList(source,
					"copyprivateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case DEFAULT:
			if (dataClause.getChild(0).getChild(0).getType() == NONE)
				((OmpParallelNode) ompStatementNode).setDefault(false);
			break;
		case REDUCTION:
			OmpReductionNode reductionNode = translateReductionClause(dataClauseEle);
			SequenceNode<OmpReductionNode> reductionList = ompStatementNode
					.reductionList();

			if (reductionList == null) {
				List<OmpReductionNode> nodes = new ArrayList<>(1);

				nodes.add(reductionNode);
				reductionList = nodeFactory.newSequenceNode(
						reductionNode.getSource(), "reductionList", nodes);
			}
			ompStatementNode.setReductionList(reductionList);
			break;
		default:
			throw new ABCRuntimeException("Invalid data clause");
		}
	}

	private OmpReductionNode translateReductionClause(CommonTree reduction) {
		int operatorType = reduction.getChild(0).getType();
		List<IdentifierExpressionNode> list = translateIdentifierList((CommonTree) reduction
				.getChild(1));
		Source rootSource = sourceFactory.newSource((CToken) reduction
				.getToken());
		SequenceNode<IdentifierExpressionNode> nodes = nodeFactory
				.newSequenceNode(rootSource, "reductionList", list);

		if (operatorType == IDENTIFIER) {
			IdentifierExpressionNode function = this
					.translateIdentifierExpression((CommonTree) reduction
							.getChild(0));

			return this.nodeFactory.newOmpFunctionReductionNode(rootSource,
					function, nodes);
		} else {
			Operator operator = translateOperator(reduction.getChild(0)
					.getType());

			return this.nodeFactory.newOmpSymbolReductionNode(rootSource,
					operator, nodes);
		}
	}

	private Operator translateOperator(int type) {
		switch (type) {
		case PLUS:
			return Operator.PLUSEQ;
		case STAR:
			return Operator.TIMESEQ;
		case SUB:
			return Operator.MINUSEQ;
		case AMPERSAND:
			return Operator.BITANDEQ;
		case BITXOR:
			return Operator.BITXOREQ;
		case BITOR:
			return Operator.BITOREQ;
			// case AND:
			// return Operator.LANDEQ;
			// case OR:
			// return Operator.LOREQ;
		default:
			throw new ABCUnsupportedException("reduction operator of type "
					+ type);
		}
	}

	private SequenceNode<IdentifierExpressionNode> translateIdentifierList(
			Source source, String name, CommonTree identifierList) {
		List<IdentifierExpressionNode> list = translateIdentifierList(identifierList);

		return nodeFactory.newSequenceNode(source, name, list);
	}

	private List<IdentifierExpressionNode> translateIdentifierList(
			CommonTree identifierList) {
		int numChildren = identifierList.getChildCount();
		List<IdentifierExpressionNode> list = new ArrayList<>(numChildren);

		for (int i = 0; i < numChildren; i++) {
			list.add(translateIdentifierExpression((CommonTree) identifierList
					.getChild(i)));
		}
		return list;
	}

	private IdentifierExpressionNode translateIdentifierExpression(
			CommonTree identifier) {
		IdentifierNode identifierNode = translateIdentifier(identifier);

		return nodeFactory.newIdentifierExpressionNode(
				identifierNode.getSource(), identifierNode);
	}

	private IdentifierNode translateIdentifier(CommonTree identifier) {
		CToken token = (CToken) identifier.getToken();
		Source source = sourceFactory.newSource(token);

		return nodeFactory.newIdentifierNode(source, token.getText());
	}
}
