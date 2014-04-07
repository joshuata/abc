package edu.udel.cis.vsl.abc.token.IF;

import java.io.File;

public interface Formation {

	String suffix();

	File getLastFile();
	
	String fileShortName();
}
