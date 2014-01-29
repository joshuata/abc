package edu.udel.cis.vsl.abc.analysis.entity;

import java.util.ArrayList;

import edu.udel.cis.vsl.abc.ABCRuntimeException;

/**
 * A designation is specifies (or "designates") a point in a compound literal
 * object. It consists of a sequence of Navigators.
 * 
 * The points in a compound literal object form a rooted tree. The root is a
 * reference to the whole object. The children of the root are references to the
 * immediate sub-objects, and so on. The leaves in this are references to simple
 * literals which wrap expressions. The edges in the tree correspond to
 * Navigators. A Designation specifies a path in the tree starting from the
 * root.
 * 
 * @see {@link Navigator}
 * 
 * @author siegel
 * 
 */
public class Designation {

	private LiteralTypeNode rootType;

	private ArrayList<Navigator> navigators;

	Designation(LiteralTypeNode rootType, ArrayList<Navigator> navigators) {
		this.rootType = rootType;
		this.navigators = navigators;
	}

	public Designation(LiteralTypeNode rootType) {
		this(rootType, new ArrayList<Navigator>());
	}

	/**
	 * Returns the number of naviagators in the sequence which comprises this
	 * designation.
	 * 
	 * @return the number of navigators
	 */
	public int length() {
		return navigators.size();
	}

	public Navigator get(int index) {
		return navigators.get(index);
	}

	public void add(Navigator navigator) {
		navigators.add(navigator);
	}

	public void removeLast() {
		navigators.remove(navigators.size() - 1);
	}

	public void append(Designation that) {
		navigators.addAll(that.navigators);
	}

	/**
	 * Modifies this designation so that it refers to the next point in the
	 * compound literal tree in depth-first-search order.
	 */
	public void increment(LiteralTypeNode typeNode) {
		LiteralTypeNode subType = getDesignatedType().parent();
		int length = navigators.size();

		while (true) {
			Navigator last = navigators.get(length - 1);
			int newIndex = last.getIndex() + 1;

			if (subType.hasFixedLength() && newIndex >= subType.length()) {
				// backtrack
				if (length == 0)
					throw new ABCRuntimeException("unreachable");
				removeLast();
				subType = subType.parent();
				length--;
			} else {
				navigators.set(length - 1, new Navigator(newIndex));
				return;
			}
		}
	}

	@Override
	public String toString() {
		String result = "";

		for (Navigator n : navigators)
			result += n;
		return result;
	}

	public LiteralTypeNode getRootType() {
		return rootType;
	}

	public LiteralTypeNode getDesignatedType() {
		LiteralTypeNode result = rootType;

		for (Navigator navigator : navigators) {
			int index = navigator.getIndex();

			if (result instanceof LiteralArrayTypeNode) {
				result = ((LiteralArrayTypeNode) result).getElementNode();
			} else if (result instanceof LiteralStructOrUnionTypeNode) {
				result = ((LiteralStructOrUnionTypeNode) result)
						.getMemberNode(index);
			} else {
				throw new ABCRuntimeException("unreachable");
			}
		}
		return result;
	}

	public void descendToScalar() {
		LiteralTypeNode subtype = getDesignatedType();

		while (!(subtype instanceof LiteralScalarTypeNode)) {
			if (subtype instanceof LiteralArrayTypeNode) {
				subtype = ((LiteralArrayTypeNode) subtype).getElementNode();
				navigators.add(new Navigator(0));
			} else if (subtype instanceof LiteralStructOrUnionTypeNode) {
				subtype = ((LiteralStructOrUnionTypeNode) subtype)
						.getMemberNode(0);
				navigators.add(new Navigator(0));
			} else
				throw new ABCRuntimeException("unreachable");
		}
	}

}
