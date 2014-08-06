package edu.udel.cis.vsl.abc.config.IF;

import java.math.BigInteger;

/**
 * Configuration constants.
 * 
 * numbers of bits in types, etc.
 * 
 * unsigned char unsigned short int unsigned int unsigned long int unsigned long
 * long int
 * 
 * signed versions of above, low and high
 * 
 * @author siegel
 * 
 */
public interface Configuration {

	public static enum Language {
		/** The programming language C, as specified in the C11 Standard */
		C,
		/**
		 * The programming language CIVL-C, an extension of C for concurrency
		 * and verification. See <a
		 * href="http://vsl.cis.udel.edu/civl">http://vsl.cis.udel.edu/civl</a>.
		 */
		CIVL_C
	};

	/**
	 * The language of the program being processed. C is the default, but if the
	 * file suffix ends in ".cvl" the command line processor will change it to
	 * CIVL_C. As this is a public static variable, it can also be set manually.
	 */
	// public static Language language = Language.C;

	Language getLanguage();

	void setLanguage(Language language);

	BigInteger unsignedCharMax();

	BigInteger unsignedShortIntMax();

	BigInteger unsignedIntMax();

	BigInteger unsignedLongIntMax();

	BigInteger unsignedLongLongIntMax();

	BigInteger signedCharMin();

	BigInteger signedCharMax();

	BigInteger signedShortIntMin();

	BigInteger signedShortIntMax();

	BigInteger signedIntMin();

	BigInteger signedIntMax();

	BigInteger signedLongIntMin();

	BigInteger signedLongIntMax();

	BigInteger signedLongLongIntMin();

	BigInteger signedLongLongIntMax();

	BigInteger charMin();

	BigInteger charMax();

	boolean inRangeUnsignedChar(BigInteger value);

	boolean inRangeUnsignedShort(BigInteger value);

	boolean inRangeUnsignedInt(BigInteger value);

	boolean inRangeUnsignedLongInt(BigInteger value);

	boolean inRangeUnsignedLongLongInt(BigInteger value);

	boolean inRangeSignedChar(BigInteger value);

	boolean inRangeSignedShort(BigInteger value);

	boolean inRangeSignedInt(BigInteger value);

	boolean inRangeSignedLongInt(BigInteger value);

	boolean inRangeSignedLongLongInt(BigInteger value);
}
