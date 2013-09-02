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

	public static enum Language {
		C, CIVL_C
	};

	/**
	 * The language of the program being processed. C is the default, but if the
	 * file suffix ends in ".cvl" the command line processor will change it to
	 * CIVL_C. As this is a public static variable, it can also be set manually.
	 */
	public static Language language = Language.C;

	public static Activator activator(File file, File[] systemIncludes,
			File[] userIncludes) {
		return new Activator(file, systemIncludes, userIncludes);
	}

	public static Activator activator(File file) {
		return new Activator(file);
	}

	public static void setLanguageFromName(String name) {
		int dotIndex = name.lastIndexOf('.');

		if (dotIndex >= 0) {
			String suffix = name.substring(dotIndex + 1, name.length());

			if ("cvl".equals(suffix))
				language = Language.CIVL_C;
		}
	}

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
		out.println("-prune");
		out.println("  prune nodes unreachable from main from AST");
		out.println("-sef");
		out.println("  transform expressions with side-effects");
		out.println("-lang=[c|civlc]");
		out.println("  set language (default determined by file suffix)");
		out.println("");
	}

	private static void err(String msg) {
		System.out.println("Error: " + msg);
		System.out.println();
		help(System.out);
		System.out.flush();
		System.exit(1);
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
		boolean prune = false; // prune nodes unreachable from main
		boolean sef = false; // make side-effect-free
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
			} else if (arg.startsWith("-prune")) {
				if (arg.equals("-prune") || arg.equals("-prune=true"))
					prune = true;
				else if (arg.equals("-prune=false"))
					prune = false;
				else
					err("Unknown command line option: " + arg);
			} else if (arg.startsWith("-sef")) {
				if (arg.equals("-sef") || arg.equals("-sef=true"))
					sef = true;
				else if (arg.equals("-sef=false"))
					sef = false;
				else
					err("Unknown command line option: " + arg);
			} else if (arg.startsWith("-lang")) {
				if (arg.equals("-lang=C"))
					languageChoice = Language.C;
				else if (arg.equals("-lang=civlc"))
					languageChoice = Language.CIVL_C;
				else
					err("Unknown command line option: " + arg);
			} else if (arg.startsWith("-")) {
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
		if (languageChoice == null)
			setLanguageFromName(infileName);
		else
			language = languageChoice;
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
		result.prune = prune;
		result.sef = sef;
		return result;
	}

	// TODO:
	// add -D support. Need to create a token with "source" the command line.
	// may treat command line as (virtual) file called "commandline"?

	public static void main(String[] args) throws ABCException, IOException {
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
			int numNodes1 = program.getAST().getNumberOfNodes();
			int numNodes2;

			System.out.println("AST has " + numNodes1 + " nodes.");
			if (config.prune) {
				System.out.print("Pruning... ");
				System.out.flush();
				program.prune();
				numNodes2 = program.getAST().getNumberOfNodes();
				System.out.println("pruned " + (numNodes1 - numNodes2)
						+ " nodes yielding AST with " + numNodes2 + " nodes.");
				System.out.flush();
			}
			if (config.sef) {
				System.out.print("Removing side-effects... ");
				System.out.flush();
				program.removeSideEffects();
				System.out.println("resulting AST has "
						+ program.getAST().getNumberOfNodes() + " nodes.");
				System.out.flush();
			}
			System.out.println();
			System.out.flush();
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
	boolean prune; // prune nodes unreachable from main?
	boolean sef; // make side-effect-free?
}
