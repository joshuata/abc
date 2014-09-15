package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.ProgramEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.Enumerator;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.util.IF.Pair;

public class OrdinaryEntityInfo extends EntityInfo {

	private ArrayList<Pair<Integer, OrdinaryEntity>> externals = new ArrayList<>();

	private ArrayList<Pair<Integer, OrdinaryEntity>> internals = new ArrayList<>();

	public OrdinaryEntityInfo(String name, int numTranslationUnits) {
		super(name, numTranslationUnits);
	}

	@Override
	protected void addEntity(int tuid, ProgramEntity entity) {
		ProgramEntity.LinkageKind kind = entity.getLinkage();
		Pair<Integer, OrdinaryEntity> pair = new Pair<>(tuid,
				(OrdinaryEntity) entity);

		if (kind == ProgramEntity.LinkageKind.EXTERNAL)
			externals.add(pair);
		else
			internals.add(pair);
	}

	/**
	 * Is it the case that all of the entities are typedefs to types that are
	 * all compatible with each other? Then just keep one typedef.
	 * 
	 * First, complete all tagged types that are incomplete but are deemed to be
	 * consistent. Do that before doing this.
	 * 
	 * 
	 * @return
	 */
	private boolean areEquivTypedefs() {
		if (!externals.isEmpty())
			return false;

		int n = internals.size();

		if (n == 0)
			return false;

		Pair<Integer, OrdinaryEntity> first = internals.get(0);
		Entity entity0 = first.right;

		if (!(entity0 instanceof Typedef))
			return false;

		Type type0 = ((Typedef) entity0).getType();

		for (int i = 1; i < n; i++) {
			Pair<Integer, OrdinaryEntity> pair = internals.get(i);
			Entity entity = pair.right;

			if (!(entity instanceof Typedef))
				return false;

			Type type = ((Typedef) entity).getType();

			if (!(type0.equivalentTo(type)))
				return false;
		}
		return true;
	}

	// if all of these are enumerators from same
	// complete enumeration class, nothing to do.
	// Otherwise:

	// get entity e_i. if e_i is an enumerator,
	// get its enumeration E.
	// Get the rep tuid j of the class
	// containing i in E; rename to newName(j). Else, rename to
	// newName(i).

	/**
	 * Returns an array of length internals.size(). In position i will be the
	 * new ID number to associate to the entity internals[i]. The new ID number
	 * will be used to issue a new name to the entity. The exception is if all
	 * are assigned the same ID number, then there is no need to rename
	 * anything.
	 * 
	 * @param enumMergeMap
	 * @return
	 */
	private int[] computeNewIDs(Map<EnumerationType, Integer> enumMergeMap) {
		// int n = getNumTranslationUnits();
		int numInternals = internals.size();
		int[] newIDs = new int[numInternals];

		for (int i = 0; i < numInternals; i++) {
			Pair<Integer, OrdinaryEntity> pair = internals.get(i);
			OrdinaryEntity entity = pair.right;

			if (entity instanceof Enumerator) {
				EnumerationType enumeration = ((Enumerator) entity).getType();
				int newID = enumMergeMap.get(enumeration);

				newIDs[i] = newID;
			} else {
				newIDs[i] = i;
			}
		}
		return newIDs;
	}

	private boolean isConstant(int[] array) {
		int length = array.length;

		if (length > 0) {
			int first = array[0];

			for (int i = 1; i < length; i++)
				if (array[i] != first)
					return false;
		}
		return true;
	}

	public void computeTypedefRemovals(Plan[] plan,
			Map<EnumerationType, Integer> enumMergeMap) {
		if (getNumEntities() > 1) {
			int numInternals = internals.size();

			if (areEquivTypedefs()) {
				Pair<Integer, OrdinaryEntity> pair0 = internals.get(0);
				Typedef typedef0 = (Typedef) pair0.right;
				int tuid0 = pair0.left;

				for (int i = 1; i < numInternals; i++) {
					Pair<Integer, OrdinaryEntity> pair = internals.get(i);

					plan[pair.left].addEntityRemoveAction(pair.right);
				}
				if (typedef0.getType().kind() == TypeKind.ENUMERATION) {
					for (Pair<Integer, OrdinaryEntity> pair : internals) {
						Typedef typedef = (Typedef) pair.right;
						EnumerationTypeNode enumTypeNode = (EnumerationTypeNode) typedef
								.getDefinition().getTypeNode();
						EnumerationType enumeration = enumTypeNode.getType();

						enumMergeMap.put(enumeration, tuid0);
					}
				}
			}
		}
	}

	public void computeRenamings(Plan[] plan,
			Map<EnumerationType, Integer> enumMergeMap) {
		if (getNumEntities() > 1) {
			if (areEquivTypedefs()) {
				// already dealt with
			} else {
				// rename all internal instances to unique names...
				int[] newIDs = computeNewIDs(enumMergeMap);

				if (externals.isEmpty() && isConstant(newIDs)) {
					// no renaming necessary
				} else {
					int numInternals = internals.size();

					for (int i = 0; i < numInternals; i++) {
						Pair<Integer, OrdinaryEntity> pair = internals.get(i);
						int tuid = pair.left;

						plan[tuid].addRenameAction(pair.right,
								newName(newIDs[i]));
					}
				}
			}
		}
	}
}
