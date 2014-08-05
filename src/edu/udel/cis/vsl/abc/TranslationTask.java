package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;

public class TranslationTask {

	public Language language;

	public File[] systemIncludes;

	public File[] userIncludes;

	public PrintStream out;

	public boolean preprocOnly;

	public boolean verbose;

	public File[] files;

	public List<String> transformCodes;

	public TranslationTask() {

	}

	public TranslationTask(Language language, File[] files) {
		this.language = language;
		this.files = files;
		this.out = System.out;
		systemIncludes = new File[0];
		userIncludes = new File[0];
		preprocOnly = false;
		verbose = true;
		transformCodes = new LinkedList<>();
	}

	public TranslationTask(Language language, File file) {
		this(language, new File[] { file });
	}

}
