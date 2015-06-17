package edu.udel.cis.vsl.abc.ast.conversion.IF;


import edu.udel.cis.vsl.abc.ast.type.IF.MemoryType;

public interface MemoryConversion extends Conversion {
	@Override
	MemoryType getNewType();
}
