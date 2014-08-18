package edu.udel.cis.vsl.abc.program.common;

import java.util.ArrayList;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.ProgramEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.TaggedEntity;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.util.IF.Pair;

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
	 * Translation unit ID-Entity pairs for all tagged entities sharing the name
	 * corresponding to this info object.
	 */
	private ArrayList<Pair<Integer, TaggedEntity>> entityPairs = new ArrayList<>();

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
	public TaggedEntityInfo(String tag, int numTranslationUnits) {
		super(tag, numTranslationUnits);
		structInfo = new TagCategoryInfo(this);
		unionInfo = new TagCategoryInfo(this);
		enumInfo = new TagCategoryInfo(this);
	}

	boolean isExclusive() {
		int numEmpty = 0;

		if (structInfo.isEmpty())
			numEmpty++;
		if (unionInfo.isEmpty())
			numEmpty++;
		if (enumInfo.isEmpty())
			numEmpty++;
		return numEmpty >= 2;
	}

	ArrayList<Pair<Integer, TaggedEntity>> getEntityPairs() {
		return entityPairs;
	}

	@Override
	protected void addEntity(int tuid, ProgramEntity entity) {
		TaggedEntity taggedEntity = (TaggedEntity) entity;
		Pair<Integer, TaggedEntity> pair = new Pair<>(tuid, taggedEntity);
		Type type = entity.getType();
		TypeKind kind = type.kind();

		entityPairs.add(pair);
		if (kind == TypeKind.STRUCTURE_OR_UNION) {
			if (((StructureOrUnionType) type).isStruct())
				structInfo.add(pair);
			else
				unionInfo.add(pair);
		} else if (kind == TypeKind.ENUMERATION)
			enumInfo.add(pair);
		else
			throw new ABCRuntimeException("Unreachable");
	}

	public void computeActions(Plan[] plans,
			Map<EnumerationType, Integer> enumMergeMap) {
		structInfo.addActions(plans);
		unionInfo.addActions(plans);
		enumInfo.addToEnumMergeMap(enumMergeMap);
		enumInfo.addActions(plans);
	}

	public boolean merge() {
		boolean result = false;

		result = result || enumInfo.merge();
		result = result || structInfo.merge();
		result = result || unionInfo.merge();
		return result;
	}
}
