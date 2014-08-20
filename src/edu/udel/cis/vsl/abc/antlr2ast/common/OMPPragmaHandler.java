package edu.udel.cis.vsl.abc.antlr2ast.common;

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
import java.util.List;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilder;
import edu.udel.cis.vsl.abc.antlr2ast.IF.ASTBuilderWorker;
import edu.udel.cis.vsl.abc.antlr2ast.IF.PragmaHandler;
import edu.udel.cis.vsl.abc.antlr2ast.IF.SimpleScope;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
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
import edu.udel.cis.vsl.abc.parse.IF.ParseTree;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class OMPPragmaHandler implements PragmaHandler {

	private ParseTree parseTree;

	private ASTBuilderWorker worker;

	/**
	 * The node factory used to create new AST nodes.
	 */
	private NodeFactory nodeFactory;

	/**
	 * The token factory used to create new tokens.
	 */
	private TokenFactory tokenFactory;

	public OMPPragmaHandler(ASTBuilder builder, ParseTree parseTree) {
		ASTFactory astFactory = builder.getASTFactory();

		this.parseTree = parseTree;
		this.worker = builder.getWorker(parseTree);
		this.nodeFactory = astFactory.getNodeFactory();
		this.tokenFactory = astFactory.getTokenFactory();
	}

	// Private methods...

	/**
	 * Translates C tokens into OpenMP tokens.
	 * 
	 * @param ctokens
	 *            The list of C tokens to be translated.
	 * @return
	 */
	private void modifyTokens(PragmaNode pragmaNode) {
		int number = pragmaNode.getNumTokens();

		for (CToken token : pragmaNode.getTokens()) {
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
				case "single":
					token.setType(SINGLE);
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
		}
		if (number >= 1)
			pragmaNode.getToken(number - 1).setNext(null);
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
							(tokenFactory.newSource((CToken) sectionsClause
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
			default: // case RUNTIME:
				forNode.setSchedule(OmpScheduleKind.RUNTIME);
			}
			if (forClause.getChildCount() > 1) {
				CommonTree chunkSizeTree = (CommonTree) forClause.getChild(1);

				// TODO: is null acceptable for a SimpleScope?

				ExpressionNode chunkSizeNode = worker.translateExpression(
						chunkSizeTree, null);

				forNode.setChunsize(chunkSizeNode);
			}

			break;
		case COLLAPSE: {
			CommonTree constant = (CommonTree) forClause.getChild(0);
			IntegerConstantNode constantNode = nodeFactory
					.newIntegerConstantNode(null, constant.getText());

			forNode.setCollapse(constantNode.getConstantValue()
					.getIntegerValue().intValue());
			break;
		}
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
								(tokenFactory.newSource((CToken) parallelClause
										.getToken()).getSummary(false)));
					}
				} else if (result == NUM_THREADS) {
					if (!hasNumThreads) {
						hasNumThreads = true;
					} else {
						throw new ABCRuntimeException(
								"At most one num_threads() clause is allowed in an OpenMP parallel construct.",
								(tokenFactory.newSource((CToken) parallelClause
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
			expression = worker.translateExpression(
					(CommonTree) child.getChild(0), null);
			parallelNode.setIfClause(expression);
			return IF;
		case NUM_THREADS:
			expression = worker.translateExpression(
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
		Source rootSource = tokenFactory.newSource((CToken) reduction
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
		Source source = tokenFactory.newSource(token);

		return nodeFactory.newIdentifierNode(source, token.getText());
	}

	// Public methods....

	@Override
	public EntityKind getEntityKind() {
		return EntityKind.PRAGMA_HANDLER;
	}

	@Override
	public String getName() {
		return "omp";
	}

	@Override
	public ParseTree getParseTree() {
		return parseTree;
	}

	@Override
	public ASTNode processPragmaNode(PragmaNode pragmaNode, SimpleScope scope)
			throws SyntaxException {
		Source source = pragmaNode.getSource();
		CTokenSource tokenSource;
		TokenStream tokens;

		modifyTokens(pragmaNode);
		tokenSource = pragmaNode.newTokenSource();
		tokens = new CommonTokenStream(tokenSource);
		try {
			CommonTree rootTree = OmpCParser.parse(tokens);
			int type = rootTree.getType();

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
			case CRITICAL: {
				OmpSyncNode criticalNode = nodeFactory.newOmpCriticalNode(
						source, null, null);

				if (rootTree.getChildCount() > 0) {
					criticalNode.setCriticalName(this
							.translateIdentifier((CommonTree) rootTree
									.getChild(0)));
				}
				return criticalNode;
			}
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
			throw new SyntaxException(e.getMessage(), source);
		}
	}
}
