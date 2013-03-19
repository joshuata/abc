package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.udel.cis.vsl.abc.analysis.Analysis;
import edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST;
import edu.udel.cis.vsl.abc.ast.unit.IF.TranslationUnit;
import edu.udel.cis.vsl.abc.parse.Parse;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.Preprocess;
import edu.udel.cis.vsl.abc.preproc.IF.Preprocessor;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class ABC {

	public final static String version = "0.1";
	public final static String date = "01-mar-2013";

	// TODO:
	// add -D support. Need to create a token with "source" the command line.
	// may treat command line as (virtual) file called "commandline"?

	public static void main(String[] args) throws PreprocessorException,
			ParseException, SyntaxException, FileNotFoundException {
		String infileName = null;
		String outfileName = null;
		// the following are updated by -I
		ArrayList<File> systemIncludeList = new ArrayList<File>();
		// the following are updated by -iquote
		ArrayList<File> userIncludeList = new ArrayList<File>();
		PreprocessorFactory preprocessorFactory;
		Preprocessor preprocessor;
		File infile;
		PrintStream out;
		File[] systemIncludes, userIncludes;
		boolean preprocOnly = false;
		boolean verbose = false;

		System.out.println("ABC v" + version + " of " + date
				+ " -- http://vsl.cis.udel.edu\n");
		System.out.flush();
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
			out = System.out;
		else
			out = new PrintStream(new File(outfileName));
		preprocessorFactory = Preprocess.newPreprocessorFactory();
		preprocessor = preprocessorFactory.newPreprocessor(systemIncludes,
				userIncludes);
		if (preprocOnly) {
			preprocessor.printOutput(out, infile);
		} else {
			// ideally, would only like to print earlier stages
			// if something goes wrong
			PrintStream parseOut = verbose ? out : null;
			CParser parser = Parse.newCParser(preprocessor, infile);
			TranslationUnit unit = Antlr2AST.buildAST(parser, parseOut);
			String bar = "===================";

			if (verbose) {
				out.println(bar + " AST " + bar + "\n");
				unit.print(out);
			}

			Analysis.performStandardAnalysis(unit);
			out.println(bar + " Analyzed AST " + bar + "\n");
			unit.print(out);
			out.println("\n\n" + bar + " Symbol Table " + bar + "\n");
			unit.getRootNode().getScope().print(out);
			out.println("\n\n" + bar + " Types " + bar + "\n");
			unit.getUnitFactory().getTypeFactory().printTypes(out);
			out.println();
			out.flush();

		}
	}
}
