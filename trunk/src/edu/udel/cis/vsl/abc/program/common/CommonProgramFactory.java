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

/**
 * <p>
 * This factory is used to produce a whole {@link Program} from a set of ASTs
 * representing individual translation units. This is similar to the process of
 * "linking". The resulting program is represented by a single AST that is
 * obtained by carefully merging the given ASTs. In the process of merging, some
 * entities may have to be re-named to avoid name conflicts, as described below.
 * </p>
 * 
 * <ol>
 * 
 * <li>For any entity that is renamed, the new name will be obtained by
 * appending a string beginning with <code>$TU</code> to the original name.
 * Hence the substring <code>$TU</code> should never be used in any identifier
 * in the original program, as it is reserved for this use.</li>
 * 
 * <li>The determination of compatibility for two tagged types will first strip
 * any suffix beginning with <code>$TU</code> from the tags. So, for example,
 * structs with tags such as <code>foo</code>, <code>foo$TU1</code>, and
 * <code>foo$TU2</code> may still be compatible, because all of them will be
 * considered to have the tag <code>foo</code> for the purpose of compatibility
 * checking.</li>
 * 
 * <li>After renaming, any two complete compatible tagged file-scope entities
 * will have the same name. The (redundant) complete definitions after the first
 * complete definition will be converted to incomplete declarations. That is, in
 * any subtree of the form <code>struct S { ... }</code>, the node
 * <code>{...}</code> will be replaced with <code>null</code>.</li>
 * 
 * <li>If all of the complete tagged file-scope entities with a given name are
 * compatible, then all those entities will keep the original name (including
 * the incomplete ones), and will therefore become one entity in the resulting
 * merged AST.</li>
 * 
 * <li>If there exist two incompatible file-scope tagged entities with the same
 * name from different translation units: the incompatible entities will be
 * given different names. Furthermore, any incomplete file-scope tagged entity
 * with that name will be given a name unique to its translation unit. However,
 * an incomplete type may still be considered compatible to any of these types,
 * due to the renaming scheme described above.</li>
 * 
 * <li>Given an ordinary file-scope entity E1 with internal or no linkage, if
 * there is another file-scope entity with the same name as E1 in a different
 * translation unit, the name of E1 will be modified to be unique in the whole
 * program, e.g., by appending a string of the form <code>TU</code><i>i</i> to
 * its name, where <i>i</i> is the ID of the translation unit to which E1
 * belongs.</li>
 * 
 * </ol>
 * 
 * <p>
 * Possible further work:
 * <ol>
 * <li>A second analysis pass on the merged AST could be implemented to update
 * the types of functions and objects. For example, the type of a function might
 * change after its definition. This could happen for example if a parameter
 * type had the form <code>struct S *p</code> and S was incomplete at the point
 * at which the function was analyzed. If S is completed later, the type of that
 * parameter (and the function) changes. Hence the analysis of the body of the
 * function should be re-done with the updated type of p---this might lead to
 * discovering an incompatibility. See example a0.c, a1.c, a2.c in
 * examples/link.</li>
 * <li>the Pruner could merge compatible typedefs. This has to happen after the
 * merged AST is anaylzed, because the new type information is needed.</li>
 * </ol>
 * 
 * </p>
 * 
 */
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
		newRoot = nodeFactory.newTranslationUnitNode(fakeSource, definitions);
		result = astFactory.newAST(newRoot);
		return result;
	}

	/**
	 * Links the ASTs, just as in {@link #link(AST[])}, but takes an
	 * {@link Iterable} instead of an array.
	 * 
	 * @param asts
	 *            an Iterable of ASTs, each representing one raw translation
	 *            unit
	 * @return the AST obtained by merging the given ones (unclean)
	 * @throws SyntaxException
	 *             if there is any kind of syntax error in any of the ASTs, or
	 *             they cannot be merged for any reason
	 */
	private AST link(Iterable<AST> asts) throws SyntaxException {
		int i, n = 0;
		AST[] astArray;

		for (@SuppressWarnings("unused")
		AST ast : asts)
			n++;
		astArray = new AST[n];
		i = 0;
		for (AST ast : asts) {
			astArray[i] = ast;
			i++;
		}
		return link(astArray);
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

	@Override
	public Program newProgram(Iterable<AST> asts) throws SyntaxException {
		return newProgram(link(asts));
	}
}
