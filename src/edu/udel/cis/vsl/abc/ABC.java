package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * The main class for the ABC C front end. Provides a simple command line
 * interface as well as static methods to produce an {@link Activator} for more
 * advanced uses.
 * 
 * Include paths are searched as follows. For a file included with angle
 * brackets, first the paths occurring in the array <code>systemIncludes</code>
 * are searched, in order; if file is not found there, then the built-in system
 * include paths inside of ABC are searched. For a file included with double
 * quotes, first the paths occurring in the array <code>userIncludes</code> are
 * searched (in order) and if the file is not found there, the protocol for
 * angle brackets is followed.
 * 
 * 
 * @author siegel
 * 
 */
public class ABC {

	public final static String version = "0.1";

	public final static String date = "01-mar-2013";

	public static Activator activator(File file, File[] systemIncludes,
			File[] userIncludes) {
		return new Activator(file, systemIncludes, userIncludes);
	}

	public static Activator activator(File file) {
		return new Activator(file);
	}

	private static Config parseCommandLind(String[] args)
			throws FileNotFoundException {
		String infileName = null;
		String outfileName = null;
		// the following are updated by -I
		ArrayList<File> systemIncludeList = new ArrayList<File>();
		// the following are updated by -iquote
		ArrayList<File> userIncludeList = new ArrayList<File>();
		File infile;
		File[] systemIncludes, userIncludes;
		boolean preprocOnly = false;
		boolean verbose = false;
		Config result = new Config();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.startsWith("-o")) {
				String name;

				if (arg.length() == 2) {
					i++;
					if (i >= args.length)
						throw new IllegalArgumentException(
								"Filename must follow -o");
					name = args[i];
				} else {
					name = arg.substring(2);
				}
				if (outfileName == null)
					outfileName = name;
				else
					throw new IllegalArgumentException(
							"More than one use of -o");
			} else if (arg.startsWith("-I")) {
				String name;

				if (arg.length() == 2) {
					i++;
					if (i >= args.length)
						throw new IllegalArgumentException(
								"Filename must follow -I");
					name = args[i];
				} else {
					name = arg.substring(2);
				}
				systemIncludeList.add(new File(name));
			} else if (arg.startsWith("-iquote")) {
				String name;

				if (arg.length() == "-iquote".length()) {
					i++;
					if (i >= args.length)
						throw new IllegalArgumentException(
								"Filename must follow -iquote");
					name = args[i];
				} else {
					name = arg.substring("-iquote".length());
				}
				userIncludeList.add(new File(name));
			} else if (arg.equals("-E")) {
				preprocOnly = true;
			} else if (arg.equals("-v")) {
				verbose = true;
			} else if (arg.startsWith("-")) {
				throw new IllegalArgumentException(
						"Unknown command line option: " + arg);
			} else {
				if (infileName == null)
					infileName = arg;
				else
					throw new IllegalArgumentException(
							"More than one input file specified (previous was "
									+ infileName + "): " + arg);
			}
		}
		if (infileName == null)
			throw new IllegalArgumentException("No input file specified");
		infile = new File(infileName);
		userIncludes = userIncludeList.toArray(new File[0]);
		systemIncludes = systemIncludeList.toArray(new File[0]);
		if (outfileName == null)
			result.out = System.out;
		else
			result.out = new PrintStream(new File(outfileName));
		result.activator = new Activator(infile, systemIncludes, userIncludes);
		result.verbose = verbose;
		result.preprocOnly = preprocOnly;
		return result;
	}

	// TODO:
	// add -D support. Need to create a token with "source" the command line.
	// may treat command line as (virtual) file called "commandline"?

	public static void main(String[] args) throws PreprocessorException,
			ParseException, SyntaxException, IOException {
		Config config;

		System.out.println("ABC v" + version + " of " + date
				+ " -- http://vsl.cis.udel.edu\n");
		System.out.flush();
		config = parseCommandLind(args);
		if (config.preprocOnly)
			config.activator.preprocess(config.out);
		else if (config.verbose)
			config.activator.showTranslation(config.out);
		else {
			Program program = config.activator.getProgram();
			
			program.prune();
			program.removeSideEffects();
			program.print(config.out);
			config.out.flush();
		}
		config.out.close();
	}
}

class Config {
	Activator activator;
	boolean preprocOnly;
	PrintStream out;
	boolean verbose;
}
