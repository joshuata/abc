package edu.udel.cis.vsl.abc.antlr2ast.impl;

import static edu.udel.cis.vsl.abc.parse.common.OmpParser.AMPERSAND;
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
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.IDENTIFIER;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.IF;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.LST_PRIVATE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.MASTER;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NONE;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NOWAIT;
import static edu.udel.cis.vsl.abc.parse.common.OmpParser.NUM_THREADS;
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

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.ABCRuntimeException;
import edu.udel.cis.vsl.abc.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
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
import edu.udel.cis.vsl.abc.preproc.common.PreprocessorUtils;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class OmpBuilder {

	private OmpNodeFactory ompNodeFactory;
	private NodeFactory nodeFactory;
	private TokenFactory sourceFactory;
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
		this.ctokens = ctokens;
		source = PreprocessorUtils.makeTokenSourceFromList(ctokens.get(0));
		return new CommonTokenStream(source);
	}

	public OmpNode getOmpNode(Source source, IdentifierNode identifier,
			SimpleScope scope, List<CToken> ctokens, CToken eofToken)
			throws SyntaxException {
		try {
			TokenStream tokens = this.ompTokenStream(ctokens);
			OmpParser parser;
			CommonTree rootTree;
			int type;

			this.scope = scope;
			this.source = source;
			this.ompIdentifierNode = identifier;
			this.eofToken = eofToken;
			parser = new OmpParser(tokens);
			rootTree = (CommonTree) parser.openmp_construct().getTree();
			type = rootTree.getType();
			switch (type) {
			case PARALLEL_FOR:
				return translateParallelFor(rootTree);
			case PARALLEL_SECTIONS:
				return translateParallelSections(rootTree);
			case PARALLEL:
				return translateParallel(rootTree);
			case FOR:
				return translateFor(rootTree);
			case SECTIONS:
				return translateWorkshare(rootTree,
						OmpWorkshareNodeKind.SECTIONS);
			case SINGLE:
				return translateWorkshare(rootTree, OmpWorkshareNodeKind.SINGLE);
			case MASTER:
				return ompNodeFactory.newSyncNode(source, identifier, ctokens,
						eofToken, OmpSyncNodeKind.MASTER);
			case CRITICAL:
				OmpSyncNode criticalNode = ompNodeFactory
						.newSyncNode(source, identifier, ctokens, eofToken,
								OmpSyncNodeKind.CRITICAL);

				if (rootTree.getChildCount() > 0) {
					criticalNode
							.setCriticalName(getIdentifierNode((CToken) ((CommonTree) rootTree
									.getChild(0)).getToken()).getIdentifier());
				}
				return criticalNode;
			case ORDERED:
				return ompNodeFactory.newSyncNode(source, identifier, ctokens,
						eofToken, OmpSyncNodeKind.ORDERED);
			case SECTION:
				return ompNodeFactory.newWorkshareNode(source, identifier,
						ctokens, eofToken, OmpWorkshareNodeKind.SECTION);
			case BARRIER:
				return ompNodeFactory.newSyncNode(source, identifier, ctokens,
						eofToken, OmpSyncNodeKind.BARRIER);
			case FLUSH:
				OmpSyncNode flushNode = ompNodeFactory.newSyncNode(source,
						identifier, ctokens, eofToken, OmpSyncNodeKind.FLUSH);

				flushNode.setFlushedList(translateIdentifierList("flushList",
						(CommonTree) rootTree.getChild(0)));
				return flushNode;
			case THD_PRIVATE:
				return ompNodeFactory.newDeclarativeNode(
						source,
						identifier,
						this.ctokens,
						eofToken,
						translateIdentifierList("threadprivateList",
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

	private OmpWorkshareNode translateWorkshare(CommonTree workshareTree,
			OmpWorkshareNodeKind kind) {
		int numChildren = workshareTree.getChildCount();
		OmpWorkshareNode workshareNode = ompNodeFactory.newWorkshareNode(
				source, this.ompIdentifierNode, this.ctokens, eofToken, kind);
		boolean hasNowait = false;

		for (int i = 0; i < numChildren; i++) {
			CommonTree sectionsClause = (CommonTree) workshareTree.getChild(i);
			int type = sectionsClause.getType();

			switch (type) {
			case DATA_CLAUSE:
				this.translateDataClause(sectionsClause, workshareNode);
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

	private OmpNode translateParallel(CommonTree paralle)
			throws SyntaxException {
		int numChildren = paralle.getChildCount();
		OmpParallelNode parallelNode = ompNodeFactory.newParallelNode(
				this.source, this.ompIdentifierNode, this.ctokens,
				this.eofToken);
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
				this.translateDataClause(parallelClause, parallelNode);
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
					(CommonTree) child.getChild(0), scope);
			parallelNode.setIfClause(expression);
			return IF;
		case NUM_THREADS:
			expression = astBuilder.translateExpression(
					(CommonTree) child.getChild(0), scope);
			parallelNode.setNumThreads(expression);
			return NUM_THREADS;
		default:
			throw new ABCRuntimeException("Unreachable");
		}
	}

	private OmpParallelNode translateParallelFor(CommonTree parallelFor)
			throws SyntaxException {
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
				this.translateUniqueParallel(parallelForClause, parallelNode);
				break;
			case UNIQUE_FOR:
				this.translateUniqueForClause(parallelForClause, forNode);
				break;
			case DATA_CLAUSE:
				this.translateDataClause(parallelForClause, parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		parallelNode.setStatementNode(forNode);
		return parallelNode;
	}

	private OmpParallelNode translateParallelSections(
			CommonTree parallelSections) throws SyntaxException {
		int numChildren = parallelSections.getChildCount();
		OmpParallelNode parallelNode = ompNodeFactory.newParallelNode(
				this.source, this.ompIdentifierNode, this.ctokens,
				this.eofToken);
		OmpWorkshareNode sectionsNode = ompNodeFactory.newWorkshareNode(source,
				null, this.ctokens, eofToken, OmpWorkshareNodeKind.SECTIONS);

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
				this.translateDataClause(parallelSectionsClause, parallelNode);
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
		}
		parallelNode.setStatementNode(sectionsNode);
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
			ompStatementNode.setPrivateList(translateIdentifierList(
					"privateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case FST_PRIVATE:
			ompStatementNode
					.setFirstprivateList(translateIdentifierList(
							"firstprivateList",
							(CommonTree) dataClauseEle.getChild(0)));
			break;
		case LST_PRIVATE:
			ompStatementNode.setLastprivateList(translateIdentifierList(
					"lastprivateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case SHARED:
			ompStatementNode.setSharedList(translateIdentifierList(
					"sharedList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case COPYIN:
			ompStatementNode.setCopyinList(translateIdentifierList(
					"copyinList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case COPYPRIVATE:
			ompStatementNode.setCopyprivateList(translateIdentifierList(
					"copyprivateList", (CommonTree) dataClauseEle.getChild(0)));
			break;
		case DEFAULT:
			if (dataClause.getChild(0).getChild(0).getType() == NONE)
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

	private SequenceNode<OperatorNode> translateReductionClause(
			CommonTree reduction) {
		Operator operator = translateOperator(reduction.getChild(0).getType());
		List<IdentifierExpressionNode> list = translateIdentifierList((CommonTree) reduction
				.getChild(1));
		int count = list.size();
		List<OperatorNode> operators = new ArrayList<>(count);
		Source rootSource = sourceFactory.newSource((CToken) reduction
				.getToken());

		for (int i = 0; i < count; i++) {
			IdentifierExpressionNode node = list.get(i);
			Source source = node.getSource();
			List<ExpressionNode> arguments = new ArrayList<>(1);

			arguments.add(node);
			arguments.add(node.copy());
			operators.add(nodeFactory.newOperatorNode(source, operator,
					arguments));
		}
		return nodeFactory.newSequenceNode(rootSource, "reductionList",
				operators);
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
			String name, CommonTree identifierList) {
		List<IdentifierExpressionNode> list = translateIdentifierList(identifierList);

		return nodeFactory.newSequenceNode(source, name, list);
	}

	private List<IdentifierExpressionNode> translateIdentifierList(
			CommonTree identifierList) {
		int numChildren = identifierList.getChildCount();
		List<IdentifierExpressionNode> list = new ArrayList<>(numChildren);

		for (int i = 0; i < numChildren; i++) {
			list.add(getIdentifierNode((CToken) ((CommonTree) identifierList
					.getChild(i)).getToken()));
		}
		return list;
	}

	private IdentifierExpressionNode getIdentifierNode(CToken token) {
		Source source = sourceFactory.newSource(token);

		return nodeFactory.newIdentifierExpressionNode(source,
				nodeFactory.newIdentifierNode(source, token.getText()));
	}

	public OmpNodeFactory ompNodeFactory() {
		return this.ompNodeFactory;
	}

}
