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
	 * The version number of this release of ABC: {@value} .
	 */
	public final static String version = "0.2";

	/**
	 * The date of this release of ABC: {@value} .
	 */
	public final static String date = "31-mar-2014";

	/**
	 * Determines language from filename. If it ends in ".cvl" or ".cvh", it's
	 * CIVL_C, else it's C.
	 * 
	 * @param name
	 *            filename
	 * @return CIVL_C or C
	 */
	private static Language getLanguageFromName(String name) {
		int dotIndex = name.lastIndexOf('.');

		if (dotIndex >= 0) {
			String suffix = name.substring(dotIndex + 1, name.length());

			if ("cvl".equals(suffix) || "cvh".equals(suffix))
				return Language.CIVL_C;
		}
		return Language.C;
	}

	/**
	 * Prints a message to the output stream specifying the command line syntax.
	 * 
	 * @param out
	 *            the stream to which the output should be printed
	 */
	private static void help(PrintStream out) {
		out.println("Usage: abc [options] filename0 filename1 ...");
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

	private static TranslationTask parseCommandLine(String[] args)
			throws FileNotFoundException {
		ArrayList<String> infileNames = new ArrayList<>();
		String outfileName = null;
		// the following are updated by -I
		ArrayList<File> systemIncludeList = new ArrayList<File>();
		// the following are updated by -iquote
		ArrayList<File> userIncludeList = new ArrayList<File>();
		boolean preprocOnly = false;
		boolean verbose = false;
		List<String> transformCodes = new LinkedList<>();
		Language language = null;
		TranslationTask result = new TranslationTask();
		int nfiles;

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
					language = Language.C;
				else if (arg.equals("-lang=civlc"))
					language = Language.CIVL_C;
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
				infileNames.add(arg);
			}
		}
		nfiles = infileNames.size();
		if (nfiles == 0)
			err("No input file specified");
		result.files = new File[nfiles];
		for (int i = 0; i < nfiles; i++)
			result.files[i] = new File(infileNames.get(i));
		result.userIncludes = userIncludeList.toArray(new File[0]);
		result.systemIncludes = systemIncludeList.toArray(new File[0]);
		if (outfileName == null)
			result.out = System.out;
		else
			result.out = new PrintStream(new File(outfileName));
		result.language = language == null ? getLanguageFromName(infileNames
				.get(0)) : language;
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
		TranslationTask config;
		FrontEnd frontEnd;

		System.out.println("ABC v" + version + " of " + date
				+ " -- http://vsl.cis.udel.edu\n");
		System.out.flush();
		config = parseCommandLine(args);
		frontEnd = new FrontEnd();
		frontEnd.showTranslation(config);
		config.out.close();
	}
}
