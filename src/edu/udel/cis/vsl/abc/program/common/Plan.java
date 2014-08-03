package edu.udel.cis.vsl.abc.program.common;

import java.util.Collection;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;

/**
 * A plan on how to transform an AST to prepare it for merging.
 * 
 * @author siegel
 * 
 */
public class Plan {

	/**
	 * The set of entities in the translation unit whose definitions should be
	 * nullified.
	 */
	private Collection<TaggedEntity> defDeleteSet;

	/**
	 * Mapping from entity that must be renamed to its new name.
	 */
	private Map<Entity, String> renameMap;

	public Plan() {
	}

	public void addDefDeleteAction(TaggedEntity entity) {
		defDeleteSet.add(entity);
	}

	public void addRenameAction(Entity entity, String newName) {
		renameMap.put(entity, newName);
	}

	public Iterable<TaggedEntity> getDeleteActions() {
		return defDeleteSet;
	}

	public Map<Entity, String> getRenameMap() {
		return renameMap;
	}

}
