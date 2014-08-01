package edu.udel.cis.vsl.abc.program.common;

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
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.LinkageKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.parse.common.CivlCParser;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.program.IF.ProgramFactory;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Formation;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;
import edu.udel.cis.vsl.abc.transform.common.NameTransformer;
import edu.udel.cis.vsl.abc.util.IF.Pair;

/**
 * <p>
 * This factory is used to produce whole Programs (instance of {@link Program})
 * from a set of ASTs representing translation units.
 * </p>
 * 
 * <p>
 * Strategy for linking n translation units TU_0,...,TU_(n-1).
 * </p>
 * 
 * <p>
 * For each namespace (ordinary, tags):
 * </p>
 * 
 * <p>
 * For each i in [0,n-1], let Rename[i] be an empty map of {@link Entity} to
 * {@link String} . The keys in this map will be the entities in TU_i that need
 * to be renamed. The value will be the new name.
 * </p>
 * 
 * <p>
 * For tag namespace only: let Tags[i] be initially an empty map of
 * {@link String} to {@link TaggedEntity}. This set will eventually contain all
 * tagged entities in file scope of TU_i which are also complete in TU_i.
 * 
 * NEED TO KNOW: given any tag (string): is there a TU that contains
 * a tagged entity with that tag in file scope?  If so, is there a TU
 * that contains a COMPLETE tagged entity with that tag in file scope?
 * 
 * TU_0: TAG:complete, TU_1: TAG: incomplete, TU_2:no, TU_3: TAG:complete, ...
 * 
 * Result: for each i, tags to be made incomplete, tags to be renamed.
 * Protocol: given tagged entity: is there an existing tagged entity with
 * that name in an earlier TU?  If so, are they compatible?  If yes, 
 * 
 * 
 * Break up into compatibility classes. Problem is that "compatible" is
 * not transitive.  "struct S {int x}" in TU_0 is compatible with "struct S"
 * in TU_1 is compatible with "struct S {int y}" in TU_2,
 * but "struct S {int x}" is not compatible with "struct S {double z}".  But 
 * "having the same tag" is transitive.  Therefore there is no way
 * to make one translation unit representing all 3.   However, it should
 * be OK to make them incompatible...example:
 * 
 * TU_0:
 * struct S {int x;};
 * void g(struct S x);
 * 
 * TU_1:
 * struct S;
 * void g(struct S x);
 * void h(struct S x);
 * void f(struct S x) {
 *   g(x);
 *   h(x);
 * }
 * 
 * TU_2:
 * struct s {double y;};
 * void h(struct S x);
 * 
 * </p>
 * 
 * <p>
 * Let S=empty map of {@link String} to {@link Integer}. This will map an
 * identifier to either a nonnegative integer which is the index of the sole TU
 * with an internal/global-no-linkage entity of that name, or -1 if more than
 * one TU is using that name (and therefore the name will have to be changed).
 * </p>
 * 
 * For i=0..n-1: forall entities E in TU_i with internal linkage or
 * file-scope-no-linkage: let name = E.name. if there is no entry with key name
 * in S, add to S a new entry (name,i) and continue. Otherwise: let j be the
 * value of the existing entry. Put (entity, newName(name, i)) in Rename[i]. If
 * j>=0, put (entity, newName(name, j)) in Rename[j] and put (name, -1) in S.
 * 
 * Now for each i: run the rename transformer on TU_i using map Rename[i].
 * 
 * Now release all the ASTs. Then form a new AST from a new root node whose
 * children are obtained by concatenating the children of the roots of all the
 * ASTs.
 * 
 * 
 * <p>
 * Collapsing compatible struct/union/enum declarations. If two tagged types
 * declared in file scope in two different translation units are compatible, all
 * complete definitions after the first are replaced by incomplete declarations;
 * this replacement must occur everywhere in the second translation unit, not
 * just in the file scope. If the two types have the same name but are
 * incompatible, at least one of them must be renamed; the renaming must occur
 * throughout the entire translation unit.
 * </p>
 * 
 * <p>
 * Collapsing typedefs: if two typedefs declared in file scope in two different
 * translation units have the same name and compatible types, keep only the
 * first one. (Note: I don't think this is strictly necessary, since C allows
 * multiple typedefs with the same name if the types are compatible.) If they
 * have the same name but the types are not compatible, at least one of the
 * typedefs must be renamed; the renaming must occur throughout the entire
 * translation unit.
 * </p>
 * 
 * <p>
 * Example 1:
 * 
 * TU1:
 * 
 * <pre>
 * struct S {int x;};
 * </pre>
 * 
 * TU2:
 * 
 * <pre>
 * typedef struct S {int x;} T;
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * The two struct definitions are compatible, therefore the second one is
 * replaced by <code>typedef struct S T;</code>. Next, the two typedefs have the
 * same name (<code>T</code>) and bind to compatible types, so the code in TU2
 * is removed entirely.
 * </p>
 * 
 * 
 * <p>
 * Example 2:
 * 
 * TU1:
 * 
 * <pre>
 * struct S {int x;};
 * </pre>
 * 
 * TU2:
 * 
 * <pre>
 * typedef struct S {int y;} T;
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * The two struct definitions are incompatible, therefore the second one must be
 * renamed and is transformed to something like:
 * 
 * <pre>
 * typedef struct _TU2_S {int y;} T;
 * </pre>
 * 
 * Next, the two typedefs have the same name, but bind to two incompatible
 * types, so the typedefs must also be renamed, to something like:
 * 
 * <pre>
 * typedef struct _TU2_S {int y;} _TU2_T;
 * </pre>
 * 
 * </p>
 */
public class CommonProgramFactory implements ProgramFactory {

	private ASTFactory astFactory;

	private Analyzer standardAnalyzer;

	public CommonProgramFactory(ASTFactory factory, Analyzer standardAnalyzer) {
		this.astFactory = factory;
		this.standardAnalyzer = standardAnalyzer;
	}

	/**
	 * 
	 * @param ast
	 *            an analyzed AST representing a single translation unit
	 * @param namespace
	 *            0=ordinary namespace (the namespace for function, variable,
	 *            enumerator, and typedef identifiers); 1=tag namespace (for
	 *            struct, union, and enum tags)
	 * @return the set of entities E in the AST that are in the specified
	 *         namespace and satisfy either (a) E has internal linkage, or (b) E
	 *         has file scope and no linkage. The set is represented as an
	 *         Iterable object.
	 */
	private Iterable<Entity> getRenameableEntities(AST translationUnit,
			int namespace) {
		List<Entity> result = new LinkedList<Entity>();
		Scope fileScope = translationUnit.getRootNode().getScope();

		if (namespace == 0) {
			Iterator<OrdinaryEntity> internalIter = translationUnit
					.getInternalEntities();

			while (internalIter.hasNext()) {
				result.add(internalIter.next());
			}
			for (Entity entity : fileScope.getOrdinaryEntities()) {
				if (entity.getLinkage() == LinkageKind.NONE)
					result.add(entity);
			}
		} else {
			for (Entity entity : fileScope.getTaggedEntities()) {
				if (entity.getLinkage() == LinkageKind.NONE)
					result.add(entity);
			}
		}
		return result;
	}

	private String newName(String oldName, int index) {
		return "_TU" + index + "_" + oldName;
	}

	/**
	 * Given a set of ASTs, each representing a translation unit, returns a
	 * corresponding set of transformed ASTs in which identifiers have been
	 * renamed as necessary to avoid naming collisions so that the the ASTs can
	 * be merged.
	 * 
	 * @param translationUnits
	 *            the ASTs for the translation units; each AST has been analyzed
	 * @return the transformed ASTs
	 * @throws SyntaxException
	 */
	private AST[] renameEntities(AST[] translationUnits) throws SyntaxException {
		int n = translationUnits.length;
		ArrayList<Map<Entity, String>> rename = new ArrayList<Map<Entity, String>>(
				n);
		AST[] result = new AST[n];

		for (int namespace = 0; namespace < 2; namespace++) {
			Map<String, Pair<Integer, Entity>> entityOwner = new HashMap<>();

			for (int i = 0; i < n; i++)
				rename.set(i, new HashMap<Entity, String>());
			for (int i = 0; i < n; i++) {
				for (Entity entity : getRenameableEntities(translationUnits[i],
						namespace)) {
					String name = entity.getName();

					if (name != null) {
						Pair<Integer, Entity> pair = entityOwner.get(name);

						if (pair == null) {
							entityOwner.put(name, new Pair<>(i, entity));
						} else {
							int owner = pair.left;

							rename.get(i).put(entity, newName(name, i));
							if (owner >= 0) {
								rename.get(owner).put(pair.right,
										newName(name, owner));
								entityOwner.put(name, new Pair<>(-1,
										(Entity) null));
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < n; i++) {
			Transformer renamer = new NameTransformer(rename.get(i), astFactory);

			result[i] = renamer.transform(translationUnits[i]);
		}
		return result;
	}

	private AST link(AST[] translationUnits) throws SyntaxException {
		NodeFactory nodeFactory = astFactory.getNodeFactory();
		TokenFactory tokenFactory = astFactory.getTokenFactory();
		Formation formation = tokenFactory.newSystemFormation("Program");
		CToken fakeToken = tokenFactory.newCToken(CivlCParser.PROGRAM,
				"Program", formation);
		Source fakeSource = tokenFactory.newSource(fakeToken);
		AST[] renamedTranslationUnits = renameEntities(translationUnits);
		List<ExternalDefinitionNode> definitions = new LinkedList<>();
		SequenceNode<ExternalDefinitionNode> newRoot;
		AST result;

		for (AST ast : renamedTranslationUnits)
			ast.release();
		for (AST ast : renamedTranslationUnits)
			for (ExternalDefinitionNode def : ast.getRootNode())
				definitions.add(def);
		newRoot = nodeFactory.newTranslationUnitNode(fakeSource, definitions);
		result = astFactory.newAST(newRoot);
		return result;
	}

	/**
	 * @param asts
	 * @return
	 * @throws SyntaxException
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
