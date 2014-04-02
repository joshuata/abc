package edu.udel.cis.vsl.abc.transform.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Variable;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.AttributeKey;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.DeclarationListNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.transform.IF.BaseTransformer;
import edu.udel.cis.vsl.abc.util.ExpressionEvaluator;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;

/**
 * Transform OpenMP Constructs to CIVL-C.
 * 
 * This transform operates in two phases:
 * 
 * 1) Analyze and transform the OpenMP constructs that cannot influence
 * behavior, i.e., those that are exclusively performance related, those that
 * can be expressed as a logically equivalent sequential form
 * 
 * 2) Transform the remaining constructs to CIVL-C
 * 
 * @author dwyer
 * 
 */
public class OpenMPTransformer extends BaseTransformer {

	public static String CODE = "omp";
	public static String LONG_NAME = "OMPTransformer";
	public static String SHORT_DESCRIPTION = "transforms C/OpenMP program to CIVL-C";

	private AttributeKey dependenceKey;

	// Visitor identifies scalars through their "defining" declaration
	private Set<Entity> writeVars;
	private Set<Entity> readVars;

	private Set<OperatorNode> writeArrayRefs;
	private Set<OperatorNode> readArrayRefs;

	private List<Entity> privateIDs;

	private SymbolicUniverse universe;

	public OpenMPTransformer(ASTFactory astFactory) {
		super(CODE, LONG_NAME, SHORT_DESCRIPTION, astFactory);
	}

	public AST transform(AST unit) throws SyntaxException {
		System.out.println("LoopDependenceAnnotator Activated");
		annotate(unit.getRootNode());
		return unit;
	}

	AttributeKey getAttributeKey() {
		return this.dependenceKey;
	}

	/*
	 * Generically traverse the AST. When a ForLoopNode is we collect
	 * constraints on array index expressions and formulate and solve
	 * constraints that indicate dependence.
	 */
	private void annotate(ASTNode node) {
		if (node instanceof OmpParallelNode) {
			/*
			 * TBD: this code does not yet handle: - nested parallel blocks -
			 * sections workshares - collapse clauses - chunk clauses -
			 * multi-dimensional arrays
			 */

			/*
			 * Determine the private variables since they cannot generate
			 * dependences. Look at default clauses, private clauses,
			 * threadprivate directives (global somewhere?)
			 */
			SequenceNode<IdentifierExpressionNode> privateList = ((OmpParallelNode) node)
					.privateList();
			if (privateList != null) {
				privateIDs = new ArrayList<Entity>();
				Iterator<IdentifierExpressionNode> it = privateList.childIterator();
				while (it.hasNext()) {
					Entity idEnt = it.next().getIdentifier().getEntity();
					privateIDs.add(idEnt);
				}
			} else {
				privateIDs = null;
			}
			System.out.println("Found OmpParallelNode with private:"
					+ privateIDs);

			// Visit the rest of this node
			Iterator<ASTNode> iter = node.children();
			while (iter.hasNext()) {
				ASTNode child = iter.next();
				annotate(child);
			}

		} else if (node instanceof OmpForNode) {
			/*
			 * We do not currently check for issues with canonical form.
			 * Compilers are not required to check those constraints, so thee is
			 * some value in doing so.
			 */
			ForLoopNode fln = (ForLoopNode) ((OmpForNode) node).statementNode();

			/*
			 * Condition must be of the form: var relop expr or expr relop var
			 * collect this expression - call it "endBound" record the direction
			 * of the test (i.e., less/greater)
			 */
			boolean lessThanComparison = true;
			IdentifierNode loopVariable = null;
			ExpressionNode condition = fln.getCondition();

			if (condition instanceof OperatorNode) {
				OperatorNode relop = (OperatorNode) condition;
				OperatorNode.Operator op = relop.getOperator();
				if (op == OperatorNode.Operator.LT
						|| op == OperatorNode.Operator.LTE) {
					lessThanComparison = true;
				} else if (op == OperatorNode.Operator.GT
						|| op == OperatorNode.Operator.GTE) {
					lessThanComparison = false;
				} else {
					assert false : "OpenMP Canonical Loop Form violated (condition must be one of >, >=, <, or <=) :"
							+ relop;
				}

				ExpressionNode left = relop.getArgument(0);
				ExpressionNode right = relop.getArgument(1);

				// variable may be either left or right.
				if (left instanceof IdentifierExpressionNode) {
					loopVariable = ((IdentifierExpressionNode) left)
							.getIdentifier();
				} else if (right instanceof IdentifierExpressionNode) {
					loopVariable = ((IdentifierExpressionNode) right)
							.getIdentifier();
				} else {
					assert false : "OpenMP Canonical Loop Form violated (requires variable condition operand) :"
							+ condition;
				}
			} else {
				assert false : "OpenMP Canonical Loop Form violated (condition malformed) :"
						+ condition;
			}

			/*
			 * Could check here to ensure that the increment matches the
			 * ordering, i.e., increment positively for "less" and negatively
			 * for "greater", and magnitude constraints of OpenMP. This may
			 * require knowing the sign of the "incr" in the OpenMP canonical
			 * loop form (section 2.6 of the manual), but this would be easy to
			 * assert for checking at runtime.
			 */
			ExpressionNode incrementer = fln.getIncrementer();

			/*
			 * Initializer must be of the form: (type?) var = expr which appears
			 * as either a DeclarationList or OperatorExpression
			 * 
			 * Need to compute collect name of var and expression construct an
			 * expression that honors the ordering of the test e.g., if less
			 * then create "var >= expression" call the resulting expression
			 * "beginBound"
			 */
			ForLoopInitializerNode initializer = fln.getInitializer();
			if (initializer instanceof OperatorNode) {
			} else if (initializer instanceof DeclarationListNode) {
				if (initializer instanceof SequenceNode<?>) {
					SequenceNode<VariableDeclarationNode> decls = (SequenceNode<VariableDeclarationNode>) initializer;
					Iterator<VariableDeclarationNode> it = (Iterator<VariableDeclarationNode>) decls
							.childIterator();
					VariableDeclarationNode vdn = it.next();
					if (it.hasNext()) {
						assert false : "OpenMP Canonical Loop Form violated (single initializer only) :"
								+ initializer;
					}

					Variable v = vdn.getEntity();
					if (v != loopVariable.getEntity()) {
						assert false : "OpenMP Canonical Loop Form violated (initializer/condition variable mismatch) :"
								+ initializer;

					}

				} else {
					assert false : "Expected SequenceNode<VariableDeclarationNode>: "
							+ initializer;
				}

			} else {
				assert false : "Expected OperatorNode or DeclarationListNode: "
						+ initializer;
			}

			/*
			 * A challenge that we do not consider here is ensuring that certain
			 * expressions in the increment and test are loop invariant.
			 * 
			 * Note that we set up "beginBound" and "endBound" to be used to
			 * constraint the range of the index expression in case it is needed
			 * in determining the equivalence of loop index expressions.
			 */

			/*
			 * Visit the statement body looking for assignments e.g., catch
			 * OperatorExpression node's with operator=ASSIGN
			 * 
			 * Accumulate the set of memory-referencing expressions, i.e.,
			 * variable references, array index expressions, on the LHS and the
			 * RHS
			 */
			StatementNode body = fln.getBody();
			writeVars = new HashSet<Entity>();
			readVars = new HashSet<Entity>();
			writeArrayRefs = new HashSet<OperatorNode>();
			readArrayRefs = new HashSet<OperatorNode>();

			collectAssignRefExprs(body);

			System.out.println("Loop Dependence Analysis Info:");
			System.out.println("   writeVars:" + writeVars);
			System.out.println("   readVars:" + readVars);
			System.out.println("   writeArrayRefs:" + writeArrayRefs);
			System.out.println("   readArrayRefs:" + readArrayRefs);

			/*
			 * Check for name-based dependences
			 */
			writeVars.retainAll(readVars);
			System.out.println("OMP For has scalar "
					+ (writeVars.isEmpty() ? "in" : "")
					+ "dependent loop iterations");

			/*
			 * Check for array-based dependences
			 */
			boolean hasDeps = hasArrayRefDependences(writeArrayRefs, readArrayRefs);
			System.out.println("OMP For has array "
					+ (hasDeps ? "" : "in")
					+ "dependent loop iterations");

		} else if (node != null) {
			// BUG: can get here with null values in parallelfor.c example

			/*
			 * Could match other types here that have no ForLoopNode below them
			 * and skip their traversal to speed things up.
			 */
			Iterator<ASTNode> iter = node.children();
			while (iter.hasNext()) {
				ASTNode child = iter.next();
				annotate(child);
			}
		}
	}

	/*
	 * This is a visitor that processes assignment statements
	 */
	private void collectAssignRefExprs(ASTNode node) {
		if (node instanceof OperatorNode
				&& ((OperatorNode) node).getOperator() == OperatorNode.Operator.ASSIGN) {
			/*
			 * Need to handle all of the *EQ operators as well.
			 */
			OperatorNode assign = (OperatorNode) node;

			ExpressionNode lhs = assign.getArgument(0);
			if (lhs instanceof IdentifierExpressionNode) {
				Entity idEnt = ((IdentifierExpressionNode) lhs).getIdentifier().getEntity();
				if (privateIDs == null || !privateIDs.contains(idEnt)) {
					writeVars.add(idEnt);
				}
			} else if (lhs instanceof OperatorNode
					&& ((OperatorNode) lhs).getOperator() == OperatorNode.Operator.SUBSCRIPT) {
				writeArrayRefs.add((OperatorNode) lhs);

			} else {
				System.out.println("DependenceAnnotator found lhs:" + lhs);
			}

			// The argument at index 1 is the RHS
			collectRHSRefExprs(assign.getArgument(1));

		} else if (node != null) {
			// BUG: can get here with null values in parallelfor.c example

			/*
			 * Could match other types here that have no ForLoopNode below them
			 * and skip their traversal to speed things up.
			 */
			Iterator<ASTNode> iter = node.children();
			while (iter.hasNext()) {
				ASTNode child = iter.next();
				collectAssignRefExprs(child);
			}
		}
	}

	/*
	 * This is a visitor that processes assignment statements
	 */
	private void collectRHSRefExprs(ASTNode node) {
		if (node instanceof IdentifierExpressionNode) {
			Entity idEnt = ((IdentifierExpressionNode) node)
					.getIdentifier().getEntity();
			if (privateIDs == null || !privateIDs.contains(idEnt)) {
				readVars.add(idEnt);
			}

		} else if (node instanceof OperatorNode
				&& ((OperatorNode) node).getOperator() == OperatorNode.Operator.SUBSCRIPT) {
			readArrayRefs.add((OperatorNode) node);

		} else if (node != null) {
			// BUG: can get here with null values in parallelfor.c example

			/*
			 * Could match other types here that have no ForLoopNode below them
			 * and skip their traversal to speed things up.
			 */
			Iterator<ASTNode> iter = node.children();
			while (iter.hasNext()) {
				ASTNode child = iter.next();
				collectRHSRefExprs(child);
			}
		}
	}

	/*
	 * Check array read/write sets for dependences
	 */
	private boolean hasArrayRefDependences(Set<OperatorNode> writes,
			Set<OperatorNode> reads) {
		for (OperatorNode w : writes) {
			IdentifierExpressionNode baseWrite = (IdentifierExpressionNode) w.getArgument(0);

			for (OperatorNode r : reads) {
				IdentifierExpressionNode baseRead = (IdentifierExpressionNode) r.getArgument(0);

				if (baseWrite.getIdentifier().getEntity() == baseRead.getIdentifier().getEntity()) {
					// Need to check logical equality of these expressions
					if (!ExpressionEvaluator.isEqualIntExpr(w.getArgument(1), r.getArgument(1))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
}
