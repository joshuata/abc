package edu.udel.cis.vsl.abc.transform;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.transform.IF.Transformer;
import edu.udel.cis.vsl.abc.transform.common.MPITransformer;
import edu.udel.cis.vsl.abc.transform.common.Pruner;
import edu.udel.cis.vsl.abc.transform.common.SideEffectRemover;

/**
 * This class provides a static method to create a new transformer based on the
 * transformer code. It also provides a method that lists of all the known
 * codes.
 * 
 * If you create a new Transformer, you may edit this file so it knows about
 * your transformer, following the pattern of the others. It is not required for
 * you to do so---your transformer can be used to transform a program whether or
 * not it appears here. The advantage of appearing here is that a switch will be
 * automatically added to the command line interface for all transformers
 * appearing here.
 * 
 * @author siegel
 * 
 */
public class Transform {

	/**
	 * A list of the transformers available to the command line interface. Add
	 * one entry here when you create a new transformer, following the same
	 * pattern as the others.
	 */
	private static TransformRecord[] records = new TransformRecord[] {

			new TransformRecord(SideEffectRemover.CODE,
					SideEffectRemover.LONG_NAME,
					SideEffectRemover.SHORT_DESCRIPTION) {
				@Override
				public Transformer create(ASTFactory astFactory) {
					return new SideEffectRemover(astFactory);
				}
			},

			new TransformRecord(MPITransformer.CODE, MPITransformer.LONG_NAME,
					MPITransformer.SHORT_DESCRIPTION) {
				@Override
				public Transformer create(ASTFactory astFactory) {
					return new MPITransformer(astFactory);
				}
			},

			new TransformRecord(Pruner.CODE, Pruner.LONG_NAME,
					Pruner.SHORT_DESCRIPTION) {
				@Override
				public Transformer create(ASTFactory astFactory) {
					return new Pruner(astFactory);
				}
			}

	};

	private static Map<String, TransformRecord> codeToRecord = new LinkedHashMap<>();

	static {
		for (TransformRecord record : records) {
			codeToRecord.put(record.code, record);
		}
	}

	/**
	 * Returns the set of transformer codes.
	 * 
	 * @return the set of transformer codes
	 */
	public static Collection<String> getCodes() {
		return codeToRecord.keySet();
	}

	public static String getShortDescription(String code) {
		TransformRecord record = codeToRecord.get(code);

		if (record == null)
			return null;
		return record.shortDescription;
	}

	public static String getLongName(String code) {
		TransformRecord record = codeToRecord.get(code);

		if (record == null)
			return null;
		return record.name;
	}

	/**
	 * Produces a new transformer using the given AST Factory. The kind of
	 * transformer produced is determined by the given code, which is a string.
	 * This string is a short name used to identify the transformer, e.g.,
	 * "prune" for the Pruner. The short name can also be used as a commandline
	 * flag to induce use of that transformer, e.g., "abc -prune foo.c".
	 * 
	 * The given AST Factory become permanently identified with the new
	 * transformer. That transformer can only operate on ASTs that were produced
	 * with the same AST Factory.
	 * 
	 * Current transformers are (code followed by class name):
	 * <ul>
	 * <li>sef: SideEffectRemover</li>
	 * <li>mpi: MPITransformer</li>
	 * <li>prune: Pruner</li>
	 * </ul>
	 * 
	 * @param code
	 *            a short string indicating the kind of transformer to produce
	 * @param astFactory
	 *            the AST factory that will is used to produce all the ASTs upon
	 *            which the new transformer will operate
	 * @return a new transformer instance of the specified kind
	 */
	public static Transformer newTransformer(String code, ASTFactory astFactory) {
		TransformRecord record = codeToRecord.get(code);

		if (record == null)
			return null;
		return record.create(astFactory);
	}

}
