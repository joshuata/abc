package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.LinkageKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
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
		if (getNumEntities() <= 1)
			return;
		for (Pair<Integer, OrdinaryEntity> pair : internals) {
			int tuid = pair.left;

			plan[tuid].addRenameAction(pair.right, newName(tuid));
		}
	}

}
