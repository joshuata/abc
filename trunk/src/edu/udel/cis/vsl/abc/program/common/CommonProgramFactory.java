package edu.udel.cis.vsl.abc.program.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
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

	public final static boolean debug = false;

	public final static PrintStream out = System.out;

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

		for (TaggedEntity entity : plan.getMakeIncompleteActions()) {
			DeclarationNode def = entity.getDefinition();

			// System.out.println("Making incomplete: " + def);
			// System.out.flush();

			if (def instanceof StructureOrUnionTypeNode) {
				((StructureOrUnionTypeNode) def).makeIncomplete();
			} else if (def instanceof EnumerationTypeNode) {
				((EnumerationTypeNode) def).makeIncomplete();
			} else
				throw new ABCRuntimeException("unreachable: " + def);
		}
		for (Entity entity : plan.getEntityRemoveActions()) {
			Iterator<DeclarationNode> declIter = entity.getDeclarations();
			boolean isSysTypedef = entity instanceof Typedef
					&& ((Typedef) entity).isSystem();

			// system typedefs require special handling because there
			// is one entity shared by all ASTs. The declarations
			// in that entity span all ASTs. But we only want
			// to remove the decl from one AST. We can tell if
			// the decl belongs to the one AST because its parent
			// will be root...

			while (declIter.hasNext()) {
				DeclarationNode decl = declIter.next();
				ASTNode parent = decl.parent();

				if (parent != null && (!isSysTypedef || parent == root)) {
					int declIndex = decl.childIndex();

					parent.removeChild(declIndex);
				}
			}
		}
		renamer.renameFrom(root);
	}

	/**
	 * Merges non-anonymous tagged classes for which it has been determined that
	 * a merge is safe. Any incomplete type which is merged with a complete one
	 * will be completed to be in accord with the complete version.
	 * 
	 * @return <code>true</code> iff at least one non-trivial merge occurs
	 */
	private Map<String, TaggedEntityInfo> tagMerge(AST[] asts) {
		int n = asts.length;
		boolean changed = true;
		Map<String, TaggedEntityInfo> taggedInfoMap = new HashMap<>();

		for (int i = 0; i < n; i++) {
			Scope scope = asts[i].getRootNode().getScope();

			for (TaggedEntity entity : scope.getTaggedEntities()) {
				String name = entity.getName();

				if (name != null) {
					TaggedEntityInfo info = taggedInfoMap.get(name);

					if (info == null) {
						info = new TaggedEntityInfo(name, n);
						taggedInfoMap.put(name, info);
					}
					info.add(i, entity);
				}
			}
		}
		while (changed) {
			changed = false;
			for (TaggedEntityInfo info : taggedInfoMap.values())
				changed = changed || info.merge();
		}
		return taggedInfoMap;
	}

	// PROBLEM: it is the anonymous enums which are getting
	// blended. they don't have infos. but the typedef
	// ends up merging several enums because they are equivalent.
	// only the first typedef is kept. The others are removed.
	// The enumerations being remooved still need to be mapped back
	// to the first one.

	private void prepareASTs(AST[] translationUnits) throws SyntaxException {
		int n = translationUnits.length;
		Plan[] plans = new Plan[n];
		Map<String, OrdinaryEntityInfo> ordinaryInfoMap = new HashMap<>();
		Map<EnumerationType, Integer> enumMergeMap = new HashMap<>();
		Map<String, TaggedEntityInfo> taggedInfoMap;

		for (int i = 0; i < n; i++)
			plans[i] = new Plan();
		for (AST ast : translationUnits) {
			standardAnalyzer.clear(ast);
			standardAnalyzer.analyze(ast);
		}
		taggedInfoMap = tagMerge(translationUnits);

		for (TaggedEntityInfo info : taggedInfoMap.values())
			info.computeActions(plans, enumMergeMap);

		for (int i = 0; i < n; i++) {
			AST ast = translationUnits[i];
			Scope scope = ast.getRootNode().getScope();

			for (OrdinaryEntity entity : scope.getOrdinaryEntities()) {
				String name = entity.getName();
				OrdinaryEntityInfo info = ordinaryInfoMap.get(name);

				if (info == null) {
					info = new OrdinaryEntityInfo(name, n);
					ordinaryInfoMap.put(name, info);
				}
				info.add(i, entity);
			}
		}
		for (OrdinaryEntityInfo info : ordinaryInfoMap.values())
			info.computeTypedefRemovals(plans, enumMergeMap);

		// System.out.println("enumMergeMap: " + enumMergeMap);
		// System.out.flush();

		for (OrdinaryEntityInfo info : ordinaryInfoMap.values())
			info.computeRenamings(plans, enumMergeMap);
		for (int i = 0; i < n; i++) {
			AST ast = translationUnits[i];
			SequenceNode<ExternalDefinitionNode> root = ast.getRootNode();

			ast.release();
			transform(root, plans[i]);
		}
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
	 * 
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
		// Collection<String> systemTypedefs = new HashSet<String>();
		AST result;

		for (int i = 0; i < n; i++)
			roots.add(translationUnits[i].getRootNode());
		prepareASTs(translationUnits);

		if (debug) {
			out.println("Transformed translation units: ");
			out.println();
			for (int i = 0; i < n; i++) {
				SequenceNode<ExternalDefinitionNode> root = roots.get(i);
				SequenceNode<ExternalDefinitionNode> rootClone = root.copy();
				AST ast = astFactory.newAST(rootClone);

				ast.prettyPrint(out, false);
				out.println();
				out.flush();
			}
		}

		for (SequenceNode<ExternalDefinitionNode> root : roots) {
			int numChildren = root.numChildren();

			for (int i = 0; i < numChildren; i++) {
				ExternalDefinitionNode def = root.removeChild(i);

				if (def != null) {
					// if (def instanceof TypedefDeclarationNode) {
					// Typedef typedef = ((TypedefDeclarationNode) def)
					// .getEntity();
					//
					// if (typedef.isSystem()) {
					// // only add these once...
					// String name = typedef.getName();
					//
					// if (systemTypedefs.contains(name))
					// continue;
					// systemTypedefs.add(name);
					// }
					// }
					definitions.add(def);
				}
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
