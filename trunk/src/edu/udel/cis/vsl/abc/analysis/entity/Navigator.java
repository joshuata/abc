package edu.udel.cis.vsl.abc.analysis.entity;

/**
 * A Navigator is a reference to an immediate member of compound literal object.
 * It comprises a type (the type of the compound literal object) and an integer
 * index (the index of the member of that object).
 * 
 * @author siegel
 * 
 */
public class Navigator {

	// /**
	// * The type of the object to which this naviagator can be applied. Must be
	// * either an array, struct, or union type. The type is not necessarily
	// * complete.
	// */
	// private ObjectType type;

	/**
	 * The index of the member of the object to which this navigator applies.
	 */
	private int index;

	public Navigator(int index) {
		// this.type = type;
		this.index = index;
	}

	// public ObjectType getType() {
	// return type;
	// }

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		// switch (type.kind()) {
		// case STRUCTURE_OR_UNION:
		// return "."
		// + ((StructureOrUnionType) type).getField(index).getName();
		// case ARRAY:
		return "[" + index + "]";
		// default:
		// throw new ABCRuntimeException("unreachable");
		// }
	}

}
