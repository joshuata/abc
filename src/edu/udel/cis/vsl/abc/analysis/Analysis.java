package edu.udel.cis.vsl.abc.analysis;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.analysis.common.StandardAnalyzer;
import edu.udel.cis.vsl.abc.ast.conversion.Conversions;
import edu.udel.cis.vsl.abc.ast.conversion.IF.ConversionFactory;
import edu.udel.cis.vsl.abc.ast.entity.Entities;
import edu.udel.cis.vsl.abc.ast.entity.IF.EntityFactory;
import edu.udel.cis.vsl.abc.ast.node.Nodes;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.unit.IF.TranslationUnit;
import edu.udel.cis.vsl.abc.ast.unit.IF.UnitFactory;
import edu.udel.cis.vsl.abc.ast.value.Values;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class Analysis {

	public static Analyzer newStandardAnalyzer(EntityFactory entityFactory,
			NodeFactory nodeFactory, TokenFactory sourceFactory,
			ConversionFactory conversionFactory) {
		return new StandardAnalyzer(entityFactory, nodeFactory, sourceFactory,
				conversionFactory);
	}

	public static void performStandardAnalysis(TranslationUnit unit)
			throws SyntaxException {
		EntityFactory entityFactory = Entities.newEntityFactory();
		UnitFactory unitFactory = unit.getUnitFactory();
		TypeFactory typeFactory = unitFactory.getTypeFactory();
		ConversionFactory conversionFactory = Conversions
				.newConversionFactory(typeFactory);
		TokenFactory sourceFactory = unitFactory.getTokenFactory();
		ValueFactory valueFactory = Values.newValueFactory(typeFactory);
		NodeFactory nodeFactory = Nodes.newNodeFactory(typeFactory,
				valueFactory);
		Analyzer analyzer = newStandardAnalyzer(entityFactory, nodeFactory,
				sourceFactory, conversionFactory);

		analyzer.clear(unit);
		analyzer.analyze(unit);
	}

}
