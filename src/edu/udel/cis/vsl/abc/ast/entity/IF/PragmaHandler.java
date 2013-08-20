package edu.udel.cis.vsl.abc.ast.entity.IF;

/**
 * An entity responsible for dealing with a family of pragmas. Every #pragma
 * begins with an identifier (the first token following #pragma), as in
 * 
 * <code>
 * #pragma TASS ...
 * </code>
 * 
 * That identifier signifies the pragma family. Each family has one handler for
 * dealing with those pragmas.
 * 
 * Fow now, this is a stump class that doesn't do anything with the pragmas.
 * 
 * @author siegel
 * 
 */
public interface PragmaHandler extends Entity {

}
