package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.parse.common.CivlCParser;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Formation;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class CommonProgramFactory implements ProgramFactory {

	// Fields...

	private ASTFactory astFactory;

	private Analyzer standardAnalyzer;

	// Constructors...

	public CommonProgramFactory(ASTFactory factory, Analyzer standardAnalyzer) {
		this.astFactory = factory;
		this.standardAnalyzer = standardAnalyzer;
	}

	// Supporting methods...

	/**
	 * <p>
	 * Transforms a translation unit AST in preparation for merging it with the
	 * other translation units. The given plan specifies the transformation
	 * actions.
	 * </p>
	 * 
	 * <p>
	 * <strong>Precondition:</strong> the given AST is analyzed.
	 * </p>
	 * 
	 * <p>
	 * <strong>Postcondition:</strong> the resulting transformed AST is
	 * transformed according to the plan but is not yet analyzed.
	 * </p>
	 * 
	 * @param translationUnit
	 *            an AST representing a translation unit
	 * @param plan
	 *            a plan specifying actions that must be performed on this AST.
	 */
	private void transform(SequenceNode<ExternalDefinitionNode> root, Plan plan) {
		Renamer renamer = new Renamer(plan.getRenameMap());

		for (TaggedEntity entity : plan.getDeleteActions()) {
			DeclarationNode def = entity.getDefinition();

			if (def instanceof StructureOrUnionTypeNode) {
				((StructureOrUnionTypeNode) def).makeIncomplete();
			} else if (def instanceof EnumerationTypeNode) {
				((EnumerationTypeNode) def).makeIncomplete();
			} else
				throw new ABCRuntimeException("unreachable");
		}
		renamer.renameFrom(root);
	}

	/**
	 * First, analyzes the ASTs, then links them.
	 * 
	 * <p>
	 * <strong>Precondition:</strong> each AST represents one translation unit.
	 * It does not matter whether they have any analysis data as they will be
	 * cleared and analyzed first.
	 * </p>
	 * 
	 * <p>
	 * <strong>Postcondition:</strong> the original ASTs are destroyed. The
	 * resulting AST is not clean: it needs to be cleared and analyzed.
	 * </p>
	 * 
	 * @param translationUnits
	 * @return
	 * @throws SyntaxException
	 */
	private AST link(AST[] translationUnits) throws SyntaxException {
		int n = translationUnits.length;
		ArrayList<SequenceNode<ExternalDefinitionNode>> roots = new ArrayList<>(
				n);
		NodeFactory nodeFactory = astFactory.getNodeFactory();
		TokenFactory tokenFactory = astFactory.getTokenFactory();
		Formation formation = tokenFactory.newSystemFormation("Program");
		CToken fakeToken = tokenFactory.newCToken(CivlCParser.PROGRAM,
				"Program", formation);
		Source fakeSource = tokenFactory.newSource(fakeToken);
		List<ExternalDefinitionNode> definitions = new LinkedList<>();
		SequenceNode<ExternalDefinitionNode> newRoot;
		Plan[] plans = new Plan[n];
		Map<String, OrdinaryEntityInfo> ordinaryInfoMap = new HashMap<>();
		Map<String, TaggedEntityInfo> taggedInfoMap = new HashMap<>();
		AST result;

		for (int i = 0; i < n; i++)
			plans[i] = new Plan();
		for (AST ast : translationUnits) {
			standardAnalyzer.clear(ast);
			standardAnalyzer.analyze(ast);
		}
		for (int i = 0; i < n; i++) {
			AST ast = translationUnits[i];
			Scope scope = ast.getRootNode().getScope();

			for (OrdinaryEntity entity : scope.getOrdinaryEntities()) {
				String name = entity.getName();
				OrdinaryEntityInfo info = ordinaryInfoMap.get(name);

				if (info == null) {
					info = new OrdinaryEntityInfo(name);
					ordinaryInfoMap.put(name, info);
				}
				info.add(i, entity);
			}
			for (TaggedEntity entity : scope.getTaggedEntities()) {
				String name = entity.getName();
				TaggedEntityInfo info = taggedInfoMap.get(name);

				if (info == null) {
					info = new TaggedEntityInfo(name);
					taggedInfoMap.put(name, info);
				}
				info.add(i, entity);
			}
			// TODO: what about external variables with multiple initializers?
			// need to move the initializer up. Get rid of other decls?
		}
		for (TaggedEntityInfo info : taggedInfoMap.values())
			info.computeActions(plans);
		for (OrdinaryEntityInfo info : ordinaryInfoMap.values())
			info.computeActions(plans);
		for (int i = 0; i < n; i++) {
			AST ast = translationUnits[i];
			roots.add(ast.getRootNode());

			ast.release();
			transform(roots.get(i), plans[i]);
		}
		for (SequenceNode<ExternalDefinitionNode> root : roots) {
			int numChildren = root.numChildren();

			for (int i = 0; i < numChildren; i++) {
				ExternalDefinitionNode def = root.removeChild(i);

				definitions.add(def);
			}
		}
		newRoot = nodeFactory.newProgramNode(fakeSource, definitions);
		result = astFactory.newAST(newRoot);
		return result;
	}

	// Public methods from ProgramFactory...

	@Override
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	@Override
	public Program newProgram(AST ast) throws SyntaxException {
		return new CommonProgram(standardAnalyzer, ast);
	}

	@Override
	public Program newProgram(AST[] asts) throws SyntaxException {
		return newProgram(link(asts));
	}
}
