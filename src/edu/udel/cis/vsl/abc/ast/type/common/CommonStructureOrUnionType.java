package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;

public class CommonStructureOrUnionType extends CommonObjectType implements
		StructureOrUnionType {

	private final static int classCode = CommonStructureOrUnionType.class
			.hashCode();

	/** Key, cannot be null. */
	private Object key;

	/** Tag: may be null */
	private String tag;

	private boolean isStruct;

	/** Fields: will be null initially, until completed. */
	private ArrayList<Field> fields;

	/**
	 * A map from field name to fields, but only for those fields that have
	 * names.
	 */
	private Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();

	public CommonStructureOrUnionType(Object key, String tag, boolean isStruct) {
		super(TypeKind.STRUCTURE_OR_UNION);
		assert key != null;
		this.key = key;
		this.tag = tag;
		this.isStruct = isStruct;
		this.fields = null;
	}

	@Override
	public Object getKey() {
		return key;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public boolean isStruct() {
		return isStruct;
	}

	@Override
	public boolean isUnion() {
		return !isStruct;
	}

	@Override
	public int getNumFields() {
		if (fields == null)
			return 0;
		return fields.size();
	}

	@Override
	public Field getField(int index) {
		return fields.get(index);
	}

	@Override
	public Iterator<Field> getFields() {
		return fields.iterator();
	}

	@Override
	public void complete(List<Field> fields) {
		this.fields = new ArrayList<Field>(fields);
		for (Field field : fields) {
			String name = field.getName();

			if (name != null)
				fieldMap.put(name, field);
		}
	}

	@Override
	public boolean isComplete() {
		return fields != null;
	}

	@Override
	public boolean isVariablyModified() {
		return false;
	}

	/**
	 * Returns the string that is used to check compatibility of this tag with
	 * another tag. This removes any suffix beginning with <code>$TU</code>. In
	 * CIVL-C semantics, such a suffix is ignored for the purposes of checking
	 * compatibility of two tags. It is used by CIVL to give unique names to
	 * tagged entities in different translation units so they can be merged into
	 * a single AST, but such entities should still be considered compatible.
	 * 
	 * @return the tag with any suffix beginning with '$' removed
	 */
	private String getCompatibilityString() {
		if (tag == null)
			return null;
		else {
			int dollarIndex = tag.indexOf("$TU");

			return dollarIndex < 0 ? tag : tag.substring(0, dollarIndex);
		}
	}

	/**
	 * "Moreover, two structure, union, or enumerated types declared in separate
	 * translation units are compatible if their tags and members satisfy the
	 * following requirements: If one is declared with a tag, the other shall be
	 * declared with the same tag. If both are completed anywhere within their
	 * respective translation units, then the following additional requirements
	 * apply: there shall be a one-to-one correspondence between their members
	 * such that each pair of corresponding members are declared with compatible
	 * types; if one member of the pair is declared with an alignment specifier,
	 * the other is declared with an equivalent alignment specifier; and if one
	 * member of the pair is declared with a name, the other is declared with
	 * the same name. For two structures, corresponding members shall be
	 * declared in the same order. For two structures or unions, corresponding
	 * bit-fields shall have the same widths."
	 */
	@Override
	public boolean compatibleWith(Type type) {
		if (type instanceof CommonStructureOrUnionType) {
			CommonStructureOrUnionType that = (CommonStructureOrUnionType) type;

			if (this.tag != null) {
				if (!this.getCompatibilityString().equals(
						that.getCompatibilityString()))
					return false;
			} else {
				if (that.tag != null)
					return false;
			}
			if (this.isComplete() && that.isComplete()) {
				int numFields = this.getNumFields();

				if (numFields != that.getNumFields())
					return false;
				for (int i = 0; i < numFields; i++) {
					Field thisField = this.getField(i);
					Field thatField = that.getField(i);
					String thisName = thisField.getName();
					String thatName = thatField.getName();
					ObjectType thisType = thisField.getType();
					ObjectType thatType = thatField.getType();
					Value thisWidth = thisField.getBitWidth();
					Value thatWidth = thatField.getBitWidth();

					if (thisName == null) {
						if (thatName != null)
							return false;
					} else if (!thisName.equals(thatName))
						return false;
					if (thisType == null) {
						if (thatType != null)
							return false;
					} else if (!thisType.equals(thatType))
						return false;
					if (thisWidth == null) {
						if (thatWidth != null)
							return false;
					} else if (!thisWidth.equals(thatWidth))
						return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean equivalentTo(Type type) {
		if (type instanceof CommonStructureOrUnionType) {
			CommonStructureOrUnionType that = (CommonStructureOrUnionType) type;

			if (this.tag != null) {
				if (!this.tag.equals(that.tag))
					return false;
			} else {
				if (that.tag != null)
					return false;
			}
			if (!this.isComplete()) {
				if (that.isComplete())
					return false;
			} else {
				if (!that.isComplete())
					return false;
				else {
					int numFields = this.getNumFields();

					if (numFields != that.getNumFields())
						return false;
					for (int i = 0; i < numFields; i++) {
						Field thisField = this.getField(i);
						Field thatField = that.getField(i);
						String thisName = thisField.getName();
						String thatName = thatField.getName();
						ObjectType thisType = thisField.getType();
						ObjectType thatType = thatField.getType();
						Value thisWidth = thisField.getBitWidth();
						Value thatWidth = thatField.getBitWidth();

						if (thisName == null) {
							if (thatName != null)
								return false;
						} else if (!thisName.equals(thatName))
							return false;
						if (thisType == null) {
							if (thatType != null)
								return false;
						} else if (!thisType.equivalentTo(thatType))
							return false;
						if (thisWidth == null) {
							if (thatWidth != null)
								return false;
						} else if (!thisWidth.equals(thatWidth))
							return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public Field getField(String fieldName) {
		return fieldMap.get(fieldName);
	}

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.print(isStruct ? "Structure" : "Union");
		out.print("[tag=" + tag + ", key=" + key + "]");
		if (!abbrv && fields != null) {
			for (Field field : fields) {
				Type fieldType = field.getType();
				String fieldName = field.getName();
				Value bitWidth = field.getBitWidth();

				out.println();
				out.print(prefix + "| Field[name=" + fieldName);
				if (bitWidth != null)
					out.print(", bitWidth=" + bitWidth);
				out.print("]");
				if (fieldType != null) {
					out.println();
					out.print(prefix + "| | ");
					fieldType.print(prefix + "| | ", out, true);
				}
			}
		}

	}

	@Override
	public boolean isScalar() {
		return false;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof CommonStructureOrUnionType) {
			CommonStructureOrUnionType that = (CommonStructureOrUnionType) object;

			return key.equals(that.key)
					&& (tag == null || tag.equals(that.tag))
					&& isStruct == that.isStruct;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = classCode ^ key.hashCode();

		if (tag != null)
			result ^= tag.hashCode();
		if (isStruct)
			result ^= 1;
		return result;
	}

	@Override
	public void clear() {
		fieldMap = new LinkedHashMap<String, Field>();
		fields = null;
	}

	@Override
	public String toString() {
		if (isStruct)
			return "struct " + tag;
		else
			return "union " + tag;
	}

}
