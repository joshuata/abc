package edu.udel.cis.vsl.abc.err.IF;

/**
 * The root of the ABC non-runtime-exception hierarchy. These are exceptions
 * that must be caught.
 * 
 * @author siegel
 * 
 */
public class ABCException extends Exception {

	/**
	 * Generated if for serialization.
	 */
	private static final long serialVersionUID = -915898726262293701L;

	/**
	 * Constructs a new ABCException with given message
	 * 
	 * @param message
	 *            a message that will be reported to user
	 */
	public ABCException(String message) {
		super(message);
	}

	public ABCException(String message, String location) {
		super(message + " at " + location);
	}

}
