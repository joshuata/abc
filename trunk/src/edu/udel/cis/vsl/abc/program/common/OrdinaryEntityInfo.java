package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.LinkageKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Typedef;
import edu.udel.cis.vsl.abc.util.IF.Pair;

public class OrdinaryEntityInfo extends EntityInfo {

	private ArrayList<Pair<Integer, OrdinaryEntity>> externals = new ArrayList<>();

	private ArrayList<Pair<Integer, OrdinaryEntity>> internals = new ArrayList<>();

	public OrdinaryEntityInfo(String name) {
		super(name);
	}

	@Override
	protected void addEntity(int tuid, Entity entity) {
		LinkageKind kind = entity.getLinkage();
		Pair<Integer, OrdinaryEntity> pair = new Pair<>(tuid,
				(OrdinaryEntity) entity);

		if (kind == LinkageKind.EXTERNAL)
			externals.add(pair);
		else
			internals.add(pair);
	}

	@Override
	public void computeActions(Plan[] plan) {
		// if only one AST has an entity with this name, nothing to do...
		if (getNumEntities() > 1) {
			int numInternals = internals.size();

			// only those with internal linkage need to be modified...
			if (numInternals >= 1) {
				Entity e0 = internals.get(0).right;

				if (e0 instanceof Typedef && ((Typedef) e0).isSystem()) {
					// for system typedefs, remove all but the first occurrence,
					// and do not rename anything...
					// for (int i = 1; i < numInternals; i++) {
					// Pair<Integer, OrdinaryEntity> pair = internals.get(i);
					//
					// plan[pair.left].addEntityRemoveAction(pair.right);
					// }
				} else {
					// rename all internal instances to unique names...
					for (Pair<Integer, OrdinaryEntity> pair : internals) {
						int tuid = pair.left;

						plan[tuid].addRenameAction(pair.right, newName(tuid));
					}
				}
			}
		}
	}
}
