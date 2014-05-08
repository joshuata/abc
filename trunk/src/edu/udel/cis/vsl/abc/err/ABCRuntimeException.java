package edu.udel.cis.vsl.abc.err;

/**
 * The root of the ABC runtime exception hierarchy. These are exceptions that do
 * not have to be declared or caught.
 * 
 * @author siegel
 * 
 */
public class ABCRuntimeException extends RuntimeException {

	/**
	 * Generated ID for serialization.
	 */
	private static final long serialVersionUID = 2113037197514976162L;

	public ABCRuntimeException(String message) {
		super(message);
	}

	public ABCRuntimeException(String message, String location) {
		super(message + " at " + location);
	}

}
