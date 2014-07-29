package edu.udel.cis.vsl.abc.ast.entity.common;

import edu.udel.cis.vsl.abc.ast.entity.IF.Enumeration;
import edu.udel.cis.vsl.abc.ast.entity.IF.Enumerator;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.type.IF.EnumerationType;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;

public class CommonEnumerator extends CommonOrdinaryEntity implements
		Enumerator {

	private Value value;

	/**
	 * The enumeration to which this enumerator belongs.
	 */
	private Enumeration enumeration;

	/**
	 * Constructs new Enumerator.
	 * 
	 * @param declaration
	 *            the declaration node for this enumerator
	 * @param type
	 *            the enumeration type to which this enumerator belongs
	 * @param value
	 *            optional constant value; may be null
	 */
	public CommonEnumerator(EnumeratorDeclarationNode declaration,
			Enumeration enumeration, Value value) {
		super(EntityKind.ENUMERATOR, declaration.getName(), LinkageKind.NONE,
				enumeration.getType());
		this.addDeclaration(declaration);
		this.setDefinition(declaration);
		this.enumeration = enumeration;
		this.value = value;
	}

	@Override
	public EnumeratorDeclarationNode getDefinition() {
		return (EnumeratorDeclarationNode) super.getDefinition();
	}

	@Override
	public EnumerationType getType() {
		return (EnumerationType) super.getType();
	}

	@Override
	public Value getValue() {
		return value;
	}

	@Override
	public Enumeration getEnumeration() {
		return enumeration;
	}

}
