package edu.udel.cis.vsl.abc.token.common;

import java.io.File;

import edu.udel.cis.vsl.abc.token.IF.Formation;

/**
 * This formation is used to represent the formation of a token through some
 * "system" means, instead of token derived from a source file.
 * 
 * @author siegel
 * 
 */
public class SystemFormation implements Formation {

	private String identifier;

	private File file;

	public SystemFormation(String identifier) {
		this.identifier = identifier;
		this.file = new File(identifier);
	}

	@Override
	public String suffix() {
		return identifier;
	}

	@Override
	public File getLastFile() {
		return file;
	}

	@Override
	public String fileShortName() {
		return identifier;
	}

}
