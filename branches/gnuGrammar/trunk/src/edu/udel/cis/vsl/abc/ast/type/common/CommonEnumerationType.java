package edu.udel.cis.vsl.abc.ast.type.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.entity.IF.Enumerator;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;

public class CommonEnumerationType extends CommonIntegerType implements
		EnumerationType {

	private final static int classCode = CommonEnumerationType.class.hashCode();

	private Object key;

	private String tag;

	private ArrayList<Enumerator> enumerators = null;

	public CommonEnumerationType(Object key, String tag) {
		super(TypeKind.ENUMERATION);
		assert key != null;
		this.key = key;
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public int getNumEnumerators() {
		if (!isComplete())
			throw new RuntimeException("Enumeration type " + tag
					+ " is incomplete");
		return enumerators.size();
	}

	@Override
	public Enumerator getEnumerator(int index) {
		if (!isComplete())
			throw new RuntimeException("Enumeration type " + tag
					+ " is incomplete");
		return enumerators.get(index);
	}

	@Override
	public Iterator<Enumerator> getEnumerators() {
		if (!isComplete())
			throw new RuntimeException("Enumeration type " + tag
					+ " is incomplete");
		return enumerators.iterator();
	}

	@Override
	public boolean isComplete() {
		return enumerators != null;
	}

	public void complete(List<Enumerator> enumeratorList) {
		if (isComplete())
			throw new RuntimeException("Enumerator type " + tag
					+ " is already complete");
		enumerators = new ArrayList<Enumerator>(enumeratorList);
	}

	@Override
	public boolean isEnumeration() {
		return true;
	}

	@Override
	public boolean compatibleWith(Type type) {
		if (this == type)
			return true;
		if (type instanceof CommonEnumerationType) {
			CommonEnumerationType that = (CommonEnumerationType) type;

			if (tag == null) {
				if (that.tag != null)
					return false;
			} else if (!tag.equals(that.tag))
				return false;
			if (enumerators == null) {
				if (that.enumerators != null)
					return false;
			} else {
				int numEnumerators = enumerators.size();

				if (that.enumerators == null)
					return false;
				if (numEnumerators != that.enumerators.size())
					return false;
				for (int i = 0; i < numEnumerators; i++) {
					Enumerator enum1 = enumerators.get(i);
					Enumerator enum2 = that.enumerators.get(i);

					if (enum1 == null) {
						if (enum2 != null)
							return false;
					} else {
						String name1 = enum1.getName(), name2;
						Value value1 = enum1.getValue(), value2;

						if (enum2 == null)
							return false;
						name2 = enum2.getName();
						if (name1 == null) {
							if (name2 != null)
								return false;
						} else if (!name1.equals(name2))
							return false;
						value2 = enum2.getValue();
						if (value1 == null) {
							if (value2 != null)
								return false;
						} else if (!value1.equals(value2))
							return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "EnumerationType[tag=" + tag + "]";
	}

	@Override
	public void print(String prefix, PrintStream out, boolean abbrv) {
		out.print("Enumeration[tag=" + tag + ", key=" + key + "]");
		if (!abbrv && enumerators != null) {
			for (Enumerator enumerator : enumerators) {
				out.println();
				out.print(prefix + "| " + enumerator.getName());
			}
		}
	}

	@Override
	public boolean isInteger() {
		return true;
	}

	@Override
	public Object getKey() {
		return key;
	}

	@Override
	public void clear() {
		enumerators = null;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof CommonEnumerationType) {
			CommonEnumerationType that = (CommonEnumerationType) object;

			return key.equals(that.key)
					&& (tag == null || tag.equals(that.tag));
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = classCode ^ key.hashCode();

		if (tag != null)
			result ^= tag.hashCode();
		return result;
	}

}
