package edu.udel.cis.vsl.abc.program.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;

/**
 * <p>
 * A TaggedEntityInfo records information about the file-scope tagged entities
 * in different translation units sharing a common tag.
 * </p>
 * 
 * @author siegel
 */
public class TaggedEntityInfo extends EntityInfo {

	/**
	 * Information about those entities using the tag and which are structs.
	 */
	private TagCategoryInfo structInfo;

	/**
	 * Information about those entities using the tag and which are unions.
	 */
	private TagCategoryInfo unionInfo;

	/**
	 * Information about those entities using the tag and which are enums.
	 */
	private TagCategoryInfo enumInfo;

	/**
	 * Creates a new tagged entity info object with the given tag. Initially,
	 * all information is empty.
	 * 
	 * @param tag
	 *            the tag
	 */
	public TaggedEntityInfo(String tag) {
		super(tag);
		structInfo = new TagCategoryInfo(this);
		unionInfo = new TagCategoryInfo(this);
		enumInfo = new TagCategoryInfo(this);
	}

	/**
	 * Determines whether no renamings are necessary. If this returns
	 * <code>true</code> all uses of the tag are compatible, so there is no need
	 * to rename. However, you still need to nullify every definition after the
	 * first.
	 * 
	 * @return
	 */
	private boolean isConsistent() {
		if (structInfo.isConsistent() && unionInfo.isConsistent()
				&& enumInfo.isConsistent()) {
			int numEmpty = 0;

			if (structInfo.isEmpty())
				numEmpty++;
			if (unionInfo.isEmpty())
				numEmpty++;
			if (enumInfo.isEmpty())
				numEmpty++;
			return numEmpty >= 2;
		}
		return false;
	}

	@Override
	protected void addEntity(int tuid, Entity entity) {
		TaggedEntity taggedEntity = (TaggedEntity) entity;
		Type type = entity.getType();
		TypeKind kind = type.kind();

		if (kind == TypeKind.STRUCTURE_OR_UNION) {
			if (((StructureOrUnionType) type).isStruct())
				structInfo.add(tuid, taggedEntity);
			else
				unionInfo.add(tuid, taggedEntity);
		} else if (kind == TypeKind.ENUMERATION)
			enumInfo.add(tuid, taggedEntity);
		else
			throw new ABCRuntimeException("Unreachable");
	}

	@Override
	public void computeActions(Plan[] plans) {
		if (isConsistent()) {
			if (!structInfo.isEmpty())
				structInfo.addDefDelActions(plans);
			else if (!enumInfo.isEmpty())
				enumInfo.addDefDelActions(plans);
			else if (!unionInfo.isEmpty())
				unionInfo.addDefDelActions(plans);
		} else {
			structInfo.addActions(plans);
			unionInfo.addActions(plans);
			enumInfo.addActions(plans);
		}
	}
}
