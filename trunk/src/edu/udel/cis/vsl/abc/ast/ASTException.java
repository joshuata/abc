package edu.udel.cis.vsl.abc.ast;

import edu.udel.cis.vsl.abc.ABCRuntimeException;

/**
 * Runtime exception thrown when something goes wrong with the AST.
 * 
 * @author siegel
 * 
 */
public class ASTException extends ABCRuntimeException {

	/**
	 * Eclipse made me do it.
	 */
	private static final long serialVersionUID = 2671174559513915757L;

	public ASTException(String msg) {
		super(msg);
	}
	
	// TODO: add method with sourceable thing like AST node?

}
