package edu.udel.cis.vsl.abc.ast.entity.IF;

/**
 * <p>
 * An entity responsible for dealing with a family of pragmas. Every
 * <code>#pragma</code> line begins with an identifier (the first token
 * following <code>#pragma</code>), as in
 * 
 * <pre>
 * #pragma TASS ...
 * </pre>
 * 
 * That identifier signifies the pragma family. Each family has one handler for
 * dealing with those pragmas.
 * </p>
 * 
 * <p>
 * Fow now, this is a stump class that doesn't do anything with the pragmas.
 * </p>
 * 
 * @author siegel
 * 
 */
public interface PragmaHandler extends Entity {

}
