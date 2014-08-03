package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;

import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.util.IF.Pair;

public class TagCategoryInfo {

	private TaggedEntityInfo parent;

	private int numEntities = 0;

	private ArrayList<Pair<Integer, TaggedEntity>> incompletes = new ArrayList<>();

	private ArrayList<ArrayList<Pair<Integer, TaggedEntity>>> completeClasses = new ArrayList<>();

	TagCategoryInfo(TaggedEntityInfo parent) {
		this.parent = parent;
	}

	void add(int tuid, TaggedEntity entity) {
		Pair<Integer, TaggedEntity> pair = new Pair<>(tuid, entity);
		ArrayList<Pair<Integer, TaggedEntity>> theClass = null;

		if (entity.isComplete()) {
			Type entityType = entity.getType();

			for (ArrayList<Pair<Integer, TaggedEntity>> completeClass : completeClasses) {
				TaggedEntity representative = completeClass.get(0).right;

				if (representative.getType().compatibleWith(entityType)) {
					theClass = completeClass;
					break;
				}
			}
			if (theClass == null) {
				theClass = new ArrayList<>();
				completeClasses.add(theClass);
			}
		} else {
			theClass = incompletes;
		}
		theClass.add(pair);
		numEntities++;
	}

	int getNumEntities() {
		return numEntities;
	}

	boolean isEmpty() {
		return numEntities == 0;
	}

	boolean isConsistent() {
		return completeClasses.size() <= 1;
	}

	/**
	 * Adds definition delete actions for all but the first complete entity.
	 * Should only be called if the entire system is consistent.
	 * 
	 * @param plans
	 *            plans array to which the actions will be added
	 */
	void addDefDelActions(Plan[] plans) {
		if (!completeClasses.isEmpty()) {
			ArrayList<Pair<Integer, TaggedEntity>> completeClass = completeClasses
					.get(0);
			int n = completeClass.size();

			for (int i = 1; i < n; i++) {
				Pair<Integer, TaggedEntity> pair = completeClass.get(i);

				plans[pair.left].addDefDeleteAction(pair.right);
			}
		}
	}

	/**
	 * Computes the actions that need to be performed regarding this tag in the
	 * case where at least one renaming is necessary.
	 * 
	 */
	void addActions(Plan[] plans) {
		if (isEmpty())
			return;
		if (isConsistent()) {
			// everything goes in one class, name can come from
			// one tuid
			int representative; // tuid of one unit containing entity
			String newName;

			if (completeClasses.isEmpty()) {
				representative = incompletes.get(0).left;
			} else {
				representative = completeClasses.get(0).get(0).left;
			}
			newName = parent.newName(representative);
			for (Pair<Integer, TaggedEntity> pair : incompletes) {
				plans[pair.left].addRenameAction(pair.right, newName);
			}
			for (Pair<Integer, TaggedEntity> pair : completeClasses.get(0)) {
				int tuid = pair.left;
				Plan plan = plans[tuid];

				plan.addRenameAction(pair.right, newName);
				if (tuid > representative)
					plan.addDefDeleteAction(pair.right);
			}
		} else {
			// more than one complete class: each complete class gets unique
			// name, each incomplete entity gets unique name
			for (Pair<Integer, TaggedEntity> pair : incompletes) {
				plans[pair.left].addRenameAction(pair.right,
						parent.newName(pair.left));
			}
			for (ArrayList<Pair<Integer, TaggedEntity>> completeClass : completeClasses) {
				int representative = completeClass.get(0).left;
				String newName = parent.newName(representative);

				for (Pair<Integer, TaggedEntity> pair : completeClass) {
					int tuid = pair.left;
					Plan plan = plans[tuid];

					plan.addRenameAction(pair.right, newName);
					if (tuid > representative)
						plan.addDefDeleteAction(pair.right);
				}
			}
		}
	}
}
