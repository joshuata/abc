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
	 * Strategy:
	 * 
	 * For each namespace (ordinary, tags):
	 * 
	 * For each i, let Rename[i] be an empty map of Entity to String. This will
	 * collect the Entities in TU_i that need to be renamed to have a unique
	 * name. The key will be the entity that needs to be renamed and the value
	 * will be the new name.
	 * 
	 * Let S=empty map of String to Integer. This will map an identifier to
	 * either a nonnegative integer which is the index of the sole TU with an
	 * internal/global-no-linkage entity of that name, or -1 if more than one TU
	 * is using that name (and therefore the name will have to be changed).
	 * 
	 * For i=0..n-1: forall entities E in TU_i with internal linkage or
	 * file-scope-no-linkage: let name = E.name. if there is no entry with key
	 * name in S, add to S a new entry (name,i) and continue. Otherwise: let j
	 * be the value of the existing entry. Put (entity, newName(name, i)) in
	 * Rename[i]. If j>=0, put (entity, newName(name, j)) in Rename[j] and put
	 * (name, -1) in S.
	 * 
	 * Now for each i: run the rename transformer on TU_i using map Rename[i].
	 * 
	 * Now release all the ASTs. Then form a new AST from a new root node whose
	 * children are obtained by concatenating the children of the roots of all
	 * the ASTs.
	 * 
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
