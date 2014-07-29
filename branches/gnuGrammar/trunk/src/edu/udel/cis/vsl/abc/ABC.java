package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;
import edu.udel.cis.vsl.abc.err.IF.ABCException;
import edu.udel.cis.vsl.abc.program.IF.Program;
import edu.udel.cis.vsl.abc.transform.IF.Transform;

/**
 * <p>
 * The main class for the ABC C front end. Provides a simple command line
 * interface as well as static methods to produce an {@link Activator} for more
 * advanced uses.
 * </p>
 * 
 * <p>
 * By default, this command line interface will open and scan a file, preprocess
 * it, parse it to produce an ANTLR tree, translate that tree into an ABC
 * Abstract Syntax Tree (AST), and analyze the AST to determine scopes, types,
 * and entities associated to every identifier. It prints out the final result
 * in a plain text human-readable form. Various options can control how much
 * information is printed, and can also cause various transformations to be
 * applied to the AST.
 * </p>
 * 
 * <p>
 * Include paths are searched as follows: for a file included with angle
 * brackets, first the paths occurring in the array <code>systemIncludes</code>
 * are searched, in order; if the file is not found there, then the built-in
 * system include paths inside of ABC are searched. For a file included with
 * double quotes, first the paths occurring in the array
 * <code>userIncludes</code> are searched (in order) and if the file is not
 * found there, the protocol for angle brackets is followed.
 * </p>
 * 
 * 
 * @author siegel
 * 
 */
public class ABC {

	/**
	 * The version number of this release of ABC: {@value}.
	 */
	public final static String version = "0.2";

	/**
	 * The date of this release of ABC: {@value}.
	 */
	public final static String date = "31-mar-2014";

	/**
	 * Creates a new Activator for operating on the given file, using the given
	 * include paths for preprocessing. Uses the default language.
	 * 
	 * @param file
	 *            the file to be parsed
	 * @param systemIncludes
	 *            a list of directories into which to search for system header
	 *            files
	 * @param userIncludes
	 *            a list of directories in which to search for user header files
	 * @return an Activator, which can be used to preprocess, parse, and
	 *         transform the file
	 */
	public static Activator activator(File file, File[] systemIncludes,
			File[] userIncludes) {
		return new Activator(file, systemIncludes, userIncludes);
	}

	/**
	 * Creates a new Activator for operating on the given file, using the
	 * specified source language to guide the parsing and analysis.
	 * 
	 * @param file
	 *            the file to be parsed
	 * @param systemIncludes
	 *            a list of directories into which to search for system header
	 *            files
	 * @param userIncludes
	 *            a list of directories in which to search for user header files
	 * @param kind
	 *            the language in which the program contained in the file should
	 *            be written; e.g. C or CIVL_C
	 * @return an Activator, which can be used to preprocess, parse, and
	 *         transform the file
	 */
	public static Activator activator(File file, File[] systemIncludes,
			File[] userIncludes, Language kind) {
		return new Activator(kind, file, systemIncludes, userIncludes);
	}

	/**
	 * Creates a new Activator for operating on the given file, using empty
	 * include paths and the language determined by the file name suffix.
	 * 
	 * @param file
	 *            the file to be processes
	 * @return the activator for processing that file
	 */
	public static Activator activator(File file) {
		return new Activator(file);
	}

	/**
	 * Prints a message to the output stream specifying the command line syntax.
	 * 
	 * @param out
	 *            the stream to which the output should be printed
	 */
	private static void help(PrintStream out) {
		out.println("Usage: abc [options] filename");
		out.println("Options:");
		out.println("-I <path>");
		out.println("  add path to system include list");
		out.println("-iquote <path>");
		out.println("  add path to user include list");
		out.println("-o <filename>");
		out.println("  send output to filename");
		out.println("-E");
		out.println("  preprocess only");
		out.println("-v");
		out.println("  verbose mode, show all processing steps");
		out.println("-lang=[c|civlc]");
		out.println("  set language (default determined by file suffix)");
		for (String code : Transform.getCodes()) {
			String description = Transform.getShortDescription(code);

			out.println("-" + code);
			out.println("  " + description);
		}
		out.println();
		out.flush();
	}

	private static void err(String msg) {
		System.out.println("Error: " + msg);
		System.out.println();
		help(System.out);
		System.out.flush();
		System.exit(1);
	}

	private static Config parseCommandLine(String[] args)
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
		List<String> transformCodes = new LinkedList<>();
		Language languageChoice = null;
		Config result = new Config();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.startsWith("-o")) {
				String name;

				if (arg.length() == 2) {
					i++;
					if (i >= args.length)
						err("Filename must follow -o");
					name = args[i];
				} else {
					name = arg.substring(2);
				}
				if (outfileName == null)
					outfileName = name;
				else
					err("More than one use of -o");
			} else if (arg.startsWith("-I")) {
				String name;

				if (arg.length() == 2) {
					i++;
					if (i >= args.length)
						err("Filename must follow -I");
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
						err("Filename must follow -iquote");
					name = args[i];
				} else {
					name = arg.substring("-iquote".length());
				}
				userIncludeList.add(new File(name));
			} else if (arg.equals("-E")) {
				preprocOnly = true;
			} else if (arg.equals("-v")) {
				verbose = true;
			} else if (arg.startsWith("-lang")) {
				if (arg.equals("-lang=C"))
					languageChoice = Language.C;
				else if (arg.equals("-lang=civlc"))
					languageChoice = Language.CIVL_C;
				else
					err("Unknown command line option: " + arg);
			} else if (arg.startsWith("-")) {
				// try transform code...
				String code = arg.substring(1);

				if (Transform.getCodes().contains(code))
					transformCodes.add(code);
				else
					err("Unknown command line option: " + arg);
			} else {
				if (infileName == null)
					infileName = arg;
				else
					err("More than one input file specified (previous was "
							+ infileName + "): " + arg);
			}
		}
		if (infileName == null)
			err("No input file specified");
		infile = new File(infileName);
		userIncludes = userIncludeList.toArray(new File[0]);
		systemIncludes = systemIncludeList.toArray(new File[0]);
		if (outfileName == null)
			result.out = System.out;
		else
			result.out = new PrintStream(new File(outfileName));
		if (languageChoice == null)
			result.activator = new Activator(infile, systemIncludes,
					userIncludes);
		else
			result.activator = new Activator(languageChoice, infile,
					systemIncludes, userIncludes);
		result.verbose = verbose;
		result.preprocOnly = preprocOnly;
		result.transformCodes = transformCodes;
		return result;
	}

	// TODO:
	// add -D support. Need to create a token with "source" the command line.
	// may treat command line as (virtual) file called "commandline"?

	/**
	 * Executes the commands specified by the command line.
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws ABCException
	 *             if something goes wrong when processing the file, such as a
	 *             syntax exception
	 * @throws IOException
	 *             if the file cannot be opened
	 */
	public static void main(String[] args) throws ABCException, IOException {
		Config config;

		System.out.println("ABC v" + version + " of " + date
				+ " -- http://vsl.cis.udel.edu\n");
		System.out.flush();
		config = parseCommandLine(args);
		if (config.preprocOnly)
			config.activator.preprocess(config.out);
		else if (config.verbose)
			config.activator.showTranslation(config.out, config.transformCodes);
		else {
			Program program = config.activator.getProgram();

			program.applyTransformers(config.transformCodes);
			program.print(config.out);
		}
		config.out.close();
	}
}

class Config {
	Activator activator;
	boolean preprocOnly;
	PrintStream out;
	boolean verbose;
	List<String> transformCodes;
}
