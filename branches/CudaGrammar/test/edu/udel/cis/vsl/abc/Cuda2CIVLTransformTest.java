package edu.udel.cis.vsl.abc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import edu.udel.cis.vsl.abc.config.IF.Configuration.Language;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorException;
import edu.udel.cis.vsl.abc.preproc.IF.PreprocessorRuntimeException;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class Cuda2CIVLTransformTest {

	public static void main(String[] args) {
		TranslationTask config = null;
		FrontEnd frontEnd;
		PrintStream err = System.err;

		config = new TranslationTask(Language.CIVL_C, new File("../CIVL/examples/translation/cuda", "simple.cu"));
		config.setPrettyPrint(true);
		config.setVerbose(false);
		frontEnd = new FrontEnd();
		try {
			frontEnd.showTranslation(config);
		} catch (PreprocessorException e) {
			err.println(e.getMessage());
			err.flush();
			System.exit(2);
		} catch (PreprocessorRuntimeException e) {
			err.println(e.getMessage());
			err.flush();
			System.exit(2);
		} catch (ParseException e) {
			err.println(e.getMessage());
			err.flush();
			System.exit(3);
		} catch (SyntaxException e) {
			err.println(e.getMessage());
			err.flush();
			System.exit(4);
		} catch (IOException e) {
			err.println(e.getMessage());
			err.flush();
			System.exit(5);
		}
		config.getOut().close();
	}
}
