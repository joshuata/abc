package edu.udel.cis.vsl.abc;

public class ABCUnsupportedException extends ABCRuntimeException {

	/**
	 * Generated ID for serialization.
	 */
	private static final long serialVersionUID = -7451309960801544746L;

	public ABCUnsupportedException(String unsupportedFeature) {
		super("ABC does not yet support " + unsupportedFeature);
	}

	public ABCUnsupportedException(String unsupportedFeature, String location) {
		super("ABC does not yet support " + unsupportedFeature, location);
	}

}
