package edu.udel.cis.vsl.abc.analysis.IF;

import edu.udel.cis.vsl.abc.analysis.common.StandardAnalyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.conversion.IF.Conversions;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entities;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.config.IF.Configuration;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class Analysis {

	public static Analyzer newStandardAnalyzer(Configuration configuration,
			ASTFactory astFactory, EntityFactory entityFactory,
			ConversionFactory conversionFactory) {
		return new StandardAnalyzer(configuration, astFactory, entityFactory,
				conversionFactory);
	}

	public static void performStandardAnalysis(Configuration configuration,
			AST unit) throws SyntaxException {
		EntityFactory entityFactory = Entities.newEntityFactory();
		ASTFactory astFactory = unit.getASTFactory();
		TypeFactory typeFactory = astFactory.getTypeFactory();
		ConversionFactory conversionFactory = Conversions
				.newConversionFactory(typeFactory);
		Analyzer analyzer = newStandardAnalyzer(configuration, astFactory,
				entityFactory, conversionFactory);

		analyzer.clear(unit);
		analyzer.analyze(unit);
	}

}
