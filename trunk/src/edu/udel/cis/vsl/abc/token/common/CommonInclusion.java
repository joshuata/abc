package edu.udel.cis.vsl.abc.token.common;

import org.antlr.runtime.Token;

import edu.udel.cis.vsl.abc.token.IF.Inclusion;
import edu.udel.cis.vsl.abc.token.IF.SourceFile;

public class CommonInclusion implements Inclusion {

	/**
	 * The file included. Always non-null;
	 */
	private SourceFile file;

	/**
	 * The token containing the file name in the include directive that named
	 * the file. Will be null for the original file (which wasn't included from
	 * anything).
	 */
	private Token includeToken;

	public CommonInclusion(SourceFile file) {
		assert file != null;
		this.file = file;
		this.includeToken = null;
	}

	public CommonInclusion(SourceFile file, Token includeToken) {
		assert file != null;
		this.file = file;
		this.includeToken = includeToken;
	}

	@Override
	public String suffix() {
		if (includeToken != null)
			return " included from " + includeToken;
		else
			return "";
	}

	@Override
	public SourceFile getLastFile() {
		return file;
	}

	@Override
	public SourceFile getFile() {
		return file;
	}

	@Override
	public Token getIncludeToken() {
		return includeToken;
	}

	// @Override
	// public String fileShortName() {
	// return this.shortName;
	// }

}
