package edu.udel.cis.vsl.abc.ast.node.common;

import java.util.List;

import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.AttributeKey;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PairNode;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.ArrayDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.DesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.FieldDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ContractNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnsuresNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.RequiresNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.ScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.AlignOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ArrowNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CharacterConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DerivativeExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DotNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.EnumerationConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FloatingConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.QuantifiedExpressionNode.Quantifier;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RemoteExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ScopeOfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.StringLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.SwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpFunctionReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSymbolReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode.OmpSyncNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorkshareNode.OmpWorkshareNodeKind;
//import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.GotoNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.IfNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.JumpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.JumpNode.JumpKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.LoopNode.LoopKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.ReturnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WaitNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.AtomicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.BasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.PointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypedefNameNode;
import edu.udel.cis.vsl.abc.ast.node.common.compound.CommonArrayDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.common.compound.CommonCompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.common.compound.CommonDesignationNode;
import edu.udel.cis.vsl.abc.ast.node.common.compound.CommonFieldDesignatorNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonAbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonEnsuresNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonEnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonFieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonFunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonRequiresNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonScopeParameterizedDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonTypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.declaration.CommonVariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonAlignOfNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonArrowNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonCastNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonCharacterConstantNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonCollectiveExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonCompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonDerivativeExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonDotNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonEnumerationConstantNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonFunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonHereOrRootNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonIdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonIntegerConstantNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonOperatorNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonProcnullNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonQuantifiedExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonRemoteExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonResultNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonScopeOfNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonSelfNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonSizeofNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonSpawnNode;
import edu.udel.cis.vsl.abc.ast.node.common.expression.CommonStringLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.common.label.CommonOrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.common.label.CommonSwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpDeclarativeNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpForNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpFunctionReductionNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpSymbolReductionNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.common.omp.CommonOmpWorkshareNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonAssumeNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonAtomicNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonChooseStatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonCompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonDeclarationListNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonExpressionStatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonForLoopNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonGotoNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonIfNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonJumpNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonLabeledStatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonLoopNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonNullStatementNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonReturnNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonSwitchNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonWaitNode;
import edu.udel.cis.vsl.abc.ast.node.common.statement.CommonWhenNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonAtomicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonBasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonEnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonFunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonPointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonScopeTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonStructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonTypedefNameNode;
import edu.udel.cis.vsl.abc.ast.node.common.type.CommonVoidTypeNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ObjectType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardUnsignedIntegerType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardUnsignedIntegerType.UnsignedIntKind;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.ast.value.IF.CharacterValue;
import edu.udel.cis.vsl.abc.ast.value.IF.IntegerValue;
import edu.udel.cis.vsl.abc.ast.value.IF.StringValue;
import edu.udel.cis.vsl.abc.ast.value.IF.Value;
import edu.udel.cis.vsl.abc.ast.value.IF.ValueFactory;
import edu.udel.cis.vsl.abc.parse.common.CivlCParser;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.ExecutionCharacter;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.StringLiteral;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

public class CommonNodeFactory implements NodeFactory {

	private int attributeCount = 0;

	private LiteralInterpreter literalInterpreter;

	private ValueFactory valueFactory;

	private StandardUnsignedIntegerType booleanType;

	private ObjectType processType;

	private ObjectType scopeType;

	public CommonNodeFactory(TypeFactory typeFactory, ValueFactory valueFactory) {
		this.literalInterpreter = new LiteralInterpreter(typeFactory,
				valueFactory);
		// this.typeFactory = typeFactory;
		this.valueFactory = valueFactory;
		this.booleanType = typeFactory
				.unsignedIntegerType(UnsignedIntKind.BOOL);
		this.processType = typeFactory.processType();
		this.scopeType = typeFactory.scopeType();
	}

	@Override
	public ValueFactory getValueFactory() {
		return valueFactory;
	}

	@Override
	public AttributeKey newAttribute(String attributeName,
			Class<? extends Object> attributeClass) {
		AttributeKey key = new CommonAttributeKey(attributeCount,
				attributeName, attributeClass);

		attributeCount++;
		return key;
	}

	@Override
	public <T extends ASTNode> SequenceNode<T> newSequenceNode(Source source,
			String name, List<T> nodes) {
		return new CommonSequenceNode<T>(source, name, nodes);
	}

	@Override
	public <S extends ASTNode, T extends ASTNode> PairNode<S, T> newPairNode(
			Source source, S node1, T node2) {
		return new CommonPairNode<S, T>(source, node1, node2);
	}

	@Override
	public IdentifierNode newIdentifierNode(Source source, String name) {
		return new CommonIdentifierNode(source, name);
	}

	@Override
	public BasicTypeNode newBasicTypeNode(Source source, BasicTypeKind kind) {
		return new CommonBasicTypeNode(source, kind);
	}

	@Override
	public TypeNode newVoidTypeNode(Source source) {
		return new CommonVoidTypeNode(source);
	}

	@Override
	public EnumerationTypeNode newEnumerationTypeNode(Source source,
			IdentifierNode tag,
			SequenceNode<EnumeratorDeclarationNode> enumerators) {
		return new CommonEnumerationTypeNode(source, tag, enumerators);
	}

	@Override
	public ArrayTypeNode newArrayTypeNode(Source source, TypeNode elementType,
			ExpressionNode extent) {
		return new CommonArrayTypeNode(source, elementType, extent);
	}

	@Override
	public AtomicTypeNode newAtomicTypeNode(Source source, TypeNode baseType) {
		return new CommonAtomicTypeNode(source, baseType);
	}

	@Override
	public PointerTypeNode newPointerTypeNode(Source source,
			TypeNode referencedType, ExpressionNode scopeModifier) {
		return new CommonPointerTypeNode(source, referencedType, scopeModifier);
	}

	@Override
	public StructureOrUnionTypeNode newStructOrUnionTypeNode(Source source,
			boolean isStruct, IdentifierNode tag,
			SequenceNode<FieldDeclarationNode> structDeclList) {
		return new CommonStructureOrUnionTypeNode(source, isStruct, tag,
				structDeclList);
	}

	@Override
	public FunctionTypeNode newFunctionTypeNode(Source source,
			TypeNode returnType, SequenceNode<VariableDeclarationNode> formals,
			boolean hasIdentifierList) {
		return new CommonFunctionTypeNode(source, returnType, formals,
				hasIdentifierList);
	}

	@Override
	public TypeNode newScopeTypeNode(Source source) {
		return new CommonScopeTypeNode(source);
	}

	@Override
	public ScopeParameterizedDeclarationNode newScopeParameterizedDeclarationNode(
			Source source, SequenceNode<VariableDeclarationNode> scopeList,
			DeclarationNode baseDeclaration) {
		return new CommonScopeParameterizedDeclarationNode(source, scopeList,
				baseDeclaration);
	}

	@Override
	public TypedefNameNode newTypedefNameNode(IdentifierNode name,
			SequenceNode<ExpressionNode> scopeList) {
		return new CommonTypedefNameNode(name.getSource(), name, scopeList);
	}

	@Override
	public CharacterConstantNode newCharacterConstantNode(Source source,
			String representation, ExecutionCharacter character)
			throws SyntaxException {
		CharacterValue constant = valueFactory.characterValue(character);

		return new CommonCharacterConstantNode(source, representation, constant);
	}

	@Override
	public StringLiteralNode newStringLiteralNode(Source source,
			String representation, StringLiteral literal) {
		StringValue stringValue = valueFactory.stringValue(literal);

		return new CommonStringLiteralNode(source, representation, stringValue);
	}

	@Override
	public IntegerConstantNode newIntegerConstantNode(Source source,
			String representation) throws SyntaxException {
		return literalInterpreter.integerConstant(source, representation);
	}

	@Override
	public FloatingConstantNode newFloatingConstantNode(Source source,
			String representation) throws SyntaxException {
		return literalInterpreter.floatingConstant(source, representation);
	}

	@Override
	public EnumerationConstantNode newEnumerationConstantNode(
			IdentifierNode name) {
		return new CommonEnumerationConstantNode(name.getSource(), name);
	}

	@Override
	public CompoundLiteralNode newCompoundLiteralNode(Source source,
			TypeNode typeNode, CompoundInitializerNode initializerList) {
		return new CommonCompoundLiteralNode(source, typeNode, initializerList);
	}

	@Override
	public IdentifierExpressionNode newIdentifierExpressionNode(Source source,
			IdentifierNode identifier) {
		return new CommonIdentifierExpressionNode(source, identifier);
	}

	@Override
	public AlignOfNode newAlignOfNode(Source source, TypeNode type) {
		return new CommonAlignOfNode(source, type);
	}

	@Override
	public CastNode newCastNode(Source source, TypeNode type,
			ExpressionNode argument) {
		return new CommonCastNode(source, type, argument);
	}

	@Override
	public FunctionCallNode newFunctionCallNode(Source source,
			ExpressionNode function, List<ExpressionNode> arguments,
			SequenceNode<ExpressionNode> scopeList) {
		SequenceNode<ExpressionNode> argumentSequenceNode = newSequenceNode(
				source, "ActualParameterList", arguments);

		return new CommonFunctionCallNode(source, function,
				argumentSequenceNode, scopeList);
	}

	@Override
	public DotNode newDotNode(Source source, ExpressionNode structure,
			IdentifierNode fieldName) {
		return new CommonDotNode(source, structure, fieldName);
	}

	@Override
	public ArrowNode newArrowNode(Source source,
			ExpressionNode structurePointer, IdentifierNode fieldName) {
		return new CommonArrowNode(source, structurePointer, fieldName);
	}

	@Override
	public OperatorNode newOperatorNode(Source source, Operator operator,
			List<ExpressionNode> arguments) {
		return new CommonOperatorNode(source, operator, arguments);
	}

	@Override
	public SizeofNode newSizeofNode(Source source, SizeableNode argument) {
		return new CommonSizeofNode(source, argument);
	}

	@Override
	public ScopeOfNode newScopeOfNode(Source source, ExpressionNode argument) {
		return new CommonScopeOfNode(source, argument);
	}

	@Override
	public VariableDeclarationNode newVariableDeclarationNode(Source source,
			IdentifierNode name, TypeNode type) {
		return new CommonVariableDeclarationNode(source, name, type);
	}

	@Override
	public VariableDeclarationNode newVariableDeclarationNode(Source source,
			IdentifierNode name, TypeNode type, InitializerNode initializer) {
		return new CommonVariableDeclarationNode(source, name, type,
				initializer);
	}

	@Override
	public FunctionDeclarationNode newFunctionDeclarationNode(Source source,
			IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract) {
		return new CommonFunctionDeclarationNode(source, name, type, contract);
	}

	@Override
	public EnumeratorDeclarationNode newEnumeratorDeclarationNode(
			Source source, IdentifierNode name, ExpressionNode value) {
		return new CommonEnumeratorDeclarationNode(source, name, value);
	}

	@Override
	public FieldDeclarationNode newFieldDeclarationNode(Source source,
			IdentifierNode name, TypeNode type) {
		return new CommonFieldDeclarationNode(source, name, type);
	}

	@Override
	public FieldDeclarationNode newFieldDeclarationNode(Source source,
			IdentifierNode name, TypeNode type, ExpressionNode bitFieldWidth) {
		return new CommonFieldDeclarationNode(source, name, type, bitFieldWidth);
	}

	@Override
	public OrdinaryLabelNode newStandardLabelDeclarationNode(Source source,
			IdentifierNode name, StatementNode statement) {
		CommonOrdinaryLabelNode label = new CommonOrdinaryLabelNode(source,
				name);

		label.setStatement(statement);
		return label;
	}

	@Override
	public SwitchLabelNode newCaseLabelDeclarationNode(Source source,
			ExpressionNode constantExpression, StatementNode statement) {
		CommonSwitchLabelNode label = new CommonSwitchLabelNode(source,
				constantExpression);

		label.setStatement(statement);
		return label;
	}

	@Override
	public SwitchLabelNode newDefaultLabelDeclarationNode(Source source,
			StatementNode statement) {
		CommonSwitchLabelNode label = new CommonSwitchLabelNode(source);

		label.setStatement(statement);
		return label;
	}

	@Override
	public TypedefDeclarationNode newTypedefDeclarationNode(Source source,
			IdentifierNode name, TypeNode type) {
		return new CommonTypedefDeclarationNode(source, name, type);
	}

	@Override
	public CompoundInitializerNode newCompoundInitializerNode(Source source,
			List<PairNode<DesignationNode, InitializerNode>> initList) {
		return new CommonCompoundInitializerNode(source, initList);
	}

	@Override
	public DesignationNode newDesignationNode(Source source,
			List<DesignatorNode> designators) {
		return new CommonDesignationNode(source, designators);
	}

	@Override
	public FieldDesignatorNode newFieldDesignatorNode(Source source,
			IdentifierNode name) {
		return new CommonFieldDesignatorNode(source, name);
	}

	@Override
	public ArrayDesignatorNode newArrayDesignatorNode(Source source,
			ExpressionNode index) {
		return new CommonArrayDesignatorNode(source, index);
	}

	@Override
	public CompoundStatementNode newCompoundStatementNode(Source source,
			List<BlockItemNode> items) {
		return new CommonCompoundStatementNode(source, items);
	}

	@Override
	public ExpressionStatementNode newExpressionStatementNode(
			ExpressionNode expression) {
		return new CommonExpressionStatementNode(expression.getSource(),
				expression);
	}

	@Override
	public StatementNode newNullStatementNode(Source source) {
		return new CommonNullStatementNode(source);
	}

	@Override
	public ForLoopNode newForLoopNode(Source source,
			ForLoopInitializerNode initializer, ExpressionNode condition,
			ExpressionNode incrementer, StatementNode body,
			ExpressionNode invariant) {
		return new CommonForLoopNode(source, condition, body, initializer,
				incrementer, invariant);
	}

	@Override
	public ForLoopInitializerNode newForLoopInitializerNode(Source source,
			List<VariableDeclarationNode> declarations) {
		return new CommonDeclarationListNode(source, declarations);
	}

	@Override
	public LoopNode newWhileLoopNode(Source source, ExpressionNode condition,
			StatementNode body, ExpressionNode invariant) {
		return new CommonLoopNode(source, LoopKind.WHILE, condition, body,
				invariant);
	}

	@Override
	public LoopNode newDoLoopNode(Source source, ExpressionNode condition,
			StatementNode body, ExpressionNode invariant) {
		return new CommonLoopNode(source, LoopKind.DO_WHILE, condition, body,
				invariant);
	}

	@Override
	public GotoNode newGotoNode(Source source, IdentifierNode label) {
		return new CommonGotoNode(source, label);
	}

	@Override
	public IfNode newIfNode(Source source, ExpressionNode condition,
			StatementNode trueBranch) {
		return new CommonIfNode(source, condition, trueBranch);
	}

	@Override
	public IfNode newIfNode(Source source, ExpressionNode condition,
			StatementNode trueBranch, StatementNode falseBranch) {
		return new CommonIfNode(source, condition, trueBranch, falseBranch);
	}

	@Override
	public JumpNode newContinueNode(Source source) {
		return new CommonJumpNode(source, JumpKind.CONTINUE);
	}

	@Override
	public JumpNode newBreakNode(Source source) {
		return new CommonJumpNode(source, JumpKind.BREAK);
	}

	@Override
	public ReturnNode newReturnNode(Source source, ExpressionNode argument) {
		return new CommonReturnNode(source, argument);
	}

	@Override
	public LabeledStatementNode newLabeledStatementNode(Source source,
			LabelNode label, StatementNode statement) {
		return new CommonLabeledStatementNode(source, label, statement);
	}

	@Override
	public SwitchNode newSwitchNode(Source source, ExpressionNode condition,
			StatementNode body) {
		CommonSwitchNode switchNode = new CommonSwitchNode(source, condition,
				body);

		return switchNode;
	}

	@Override
	public StaticAssertionNode newStaticAssertionNode(Source source,
			ExpressionNode expression, StringLiteralNode message) {
		return new CommonStaticAssertionNode(source, expression, message);
	}

	@Override
	public PragmaNode newPragmaNode(Source source, IdentifierNode identifier,
			List<CToken> body, CToken newlineToken) {
		newlineToken.setType(CivlCParser.EOF);
		return new CommonPragmaNode(source, identifier, body, newlineToken);
	}

	@Override
	public FunctionDefinitionNode newFunctionDefinitionNode(Source source,
			IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract, CompoundStatementNode body) {
		return new CommonFunctionDefinitionNode(source, name, type, contract,
				body);
	}

	@Override
	public AbstractFunctionDefinitionNode newAbstractFunctionDefinitionNode(
			Source source, IdentifierNode name, FunctionTypeNode type,
			SequenceNode<ContractNode> contract, int continuity) {
		return new CommonAbstractFunctionDefinitionNode(source, name, type,
				contract, continuity);
	}

	@Override
	public ASTNode newTranslationUnitNode(Source source,
			List<ExternalDefinitionNode> definitions) {
		return newSequenceNode(source, "TranslationUnit", definitions);
	}

	@Override
	public Value getConstantValue(ExpressionNode expression)
			throws SyntaxException {
		CommonExpressionNode commonNode = (CommonExpressionNode) expression;

		if (commonNode.constantComputed()) {
			return commonNode.getConstantValue();
		} else {
			Value value = valueFactory.evaluate(expression);

			commonNode.setConstantValue(value);
			return value;
		}
	}

	@Override
	public SpawnNode newSpawnNode(Source source, FunctionCallNode callNode) {
		return new CommonSpawnNode(source, callNode);
	}

	@Override
	public RemoteExpressionNode newRemoteExpressionNode(Source source,
			ExpressionNode left, IdentifierExpressionNode right) {
		return new CommonRemoteExpressionNode(source, left, right);
	}

	@Override
	public CollectiveExpressionNode newCollectiveExpressionNode(Source source,
			ExpressionNode processPointerExpression,
			ExpressionNode lengthExpression, ExpressionNode body) {
		return new CommonCollectiveExpressionNode(source,
				processPointerExpression, lengthExpression, body);
	}

	@Override
	public ScopeOfNode newScopeOfNode(Source source,
			IdentifierExpressionNode variableExpression) {
		return new CommonScopeOfNode(source, variableExpression);
	}

	@Override
	public WaitNode newWaitNode(Source source, ExpressionNode expression) {
		return new CommonWaitNode(source, expression);
	}

	// @Override
	// public AssertNode newAssertNode(Source source, ExpressionNode expression)
	// {
	// return new CommonAssertNode(source, expression);
	// }

	@Override
	public AssumeNode newAssumeNode(Source source, ExpressionNode expression) {
		return new CommonAssumeNode(source, expression);
	}

	@Override
	public WhenNode newWhenNode(Source source, ExpressionNode guard,
			StatementNode body) {
		return new CommonWhenNode(source, guard, body);
	}

	@Override
	public ChooseStatementNode newChooseStatementNode(Source source,
			List<StatementNode> statements) {
		return new CommonChooseStatementNode(source, statements);
	}

	@Override
	public ConstantNode newBooleanConstantNode(Source source, boolean value) {
		IntegerValue theValue;
		String representation;

		if (value) {
			representation = "\\true";
			theValue = valueFactory.integerValue(booleanType, 1);
		} else {
			representation = "\\false";
			theValue = valueFactory.integerValue(booleanType, 0);
		}
		return new CommonIntegerConstantNode(source, representation, theValue);
	}

	@Override
	public ExpressionNode newSelfNode(Source source) {
		ExpressionNode result = new CommonSelfNode(source, processType);

		result.setInitialType(processType);
		return result;
	}

	@Override
	public ExpressionNode newHereNode(Source source) {
		ExpressionNode result = new CommonHereOrRootNode(source, "$here",
				scopeType);

		result.setInitialType(scopeType);
		return result;
	}

	@Override
	public ExpressionNode newRootNode(Source source) {
		ExpressionNode result = new CommonHereOrRootNode(source, "$root",
				scopeType);

		result.setInitialType(scopeType);
		return result;
	}

	@Override
	public ExpressionNode newResultNode(Source source) {
		return new CommonResultNode(source);
	}

	@Override
	public RequiresNode newRequiresNode(Source source, ExpressionNode expression) {
		return new CommonRequiresNode(source, expression);
	}

	@Override
	public EnsuresNode newEnsuresNode(Source source, ExpressionNode expression) {
		return new CommonEnsuresNode(source, expression);
	}

	@Override
	public QuantifiedExpressionNode newQuantifiedExpressionNode(Source source,
			Quantifier quantifier, VariableDeclarationNode variable,
			ExpressionNode restriction, ExpressionNode expression) {
		return new CommonQuantifiedExpressionNode(source, quantifier, variable,
				restriction, expression);
	}

	@Override
	public QuantifiedExpressionNode newQuantifiedExpressionNode(Source source,
			Quantifier quantifier, VariableDeclarationNode variable,
			ExpressionNode lower, ExpressionNode upper,
			ExpressionNode expression) {
		return new CommonQuantifiedExpressionNode(source, quantifier, variable,
				lower, upper, expression);
	}

	@Override
	public DerivativeExpressionNode newDerivativeExpressionNode(
			Source source,
			ExpressionNode function,
			SequenceNode<PairNode<IdentifierExpressionNode, IntegerConstantNode>> partials,
			SequenceNode<ExpressionNode> arguments) {
		return new CommonDerivativeExpressionNode(source, function, partials,
				arguments);
	}

	@Override
	public void setConstantValue(ExpressionNode expression, Value value) {
		((CommonExpressionNode) expression).setConstantValue(value);
	}

	@Override
	public AtomicNode newAtomicStatementNode(Source statementSource,
			boolean deterministic, StatementNode body) {
		return new CommonAtomicNode(statementSource, deterministic, body);
	}

	@Override
	public ExpressionNode newProcnullNode(Source source) {
		ExpressionNode result = new CommonProcnullNode(source, processType);

		result.setInitialType(processType);
		return result;
	}

	@Override
	public OmpParallelNode newOmpParallelNode(Source source) {
		return new CommonOmpParallelNode(source);
	}

	@Override
	public OmpForNode newOmpForNode(Source source) {
		return new CommonOmpForNode(source);
	}

	@Override
	public OmpSyncNode newOmpMasterNode(Source source, StatementNode statement) {
		OmpSyncNode masterNode = new CommonOmpSyncNode(source,
				OmpSyncNodeKind.MASTER);

		masterNode.setStatementNode(statement);
		return masterNode;
	}

	@Override
	public OmpSyncNode newOmpBarrierNode(Source source) {
		return new CommonOmpSyncNode(source, OmpSyncNodeKind.BARRIER);
	}

	@Override
	public OmpWorkshareNode newOmpSectionsNode(Source source) {
		return new CommonOmpWorkshareNode(source, OmpWorkshareNodeKind.SECTIONS);
	}

	@Override
	public OmpWorkshareNode newOmpSectionNode(Source source,
			StatementNode statement) {
		OmpWorkshareNode sectionNode = new CommonOmpWorkshareNode(source,
				OmpWorkshareNodeKind.SECTION);

		sectionNode.setStatementNode(statement);
		return sectionNode;
	}

	@Override
	public OmpDeclarativeNode newOmpThreadprivateNode(Source source,
			SequenceNode<IdentifierExpressionNode> variables) {
		return new CommonOmpDeclarativeNode(source, variables);
	}

	@Override
	public OmpSymbolReductionNode newOmpSymbolReductionNode(Source source,
			Operator operator, SequenceNode<IdentifierExpressionNode> variables) {
		return new CommonOmpSymbolReductionNode(source, operator, variables);
	}

	@Override
	public OmpSyncNode newOmpCriticalNode(Source source, IdentifierNode name,
			StatementNode statement) {
		OmpSyncNode criticalNode = new CommonOmpSyncNode(source,
				OmpSyncNodeKind.CRITICAL);

		criticalNode.setCriticalName(name);
		criticalNode.setStatementNode(statement);
		return criticalNode;
	}

	@Override
	public OmpSyncNode newOmpFlushNode(Source source,
			SequenceNode<IdentifierExpressionNode> variables) {
		OmpSyncNode flushNode = new CommonOmpSyncNode(source,
				OmpSyncNodeKind.FLUSH);

		flushNode.setFlushedList(variables);
		return flushNode;
	}

	@Override
	public OmpSyncNode newOmpOrederedNode(Source source, StatementNode statement) {
		OmpSyncNode orderedNode = new CommonOmpSyncNode(source,
				OmpSyncNodeKind.ORDERED);

		orderedNode.setStatementNode(statement);
		return orderedNode;
	}

	@Override
	public OmpWorkshareNode newOmpSingleNode(Source source) {
		return new CommonOmpWorkshareNode(source, OmpWorkshareNodeKind.SINGLE);
	}

	@Override
	public OmpFunctionReductionNode newOmpFunctionReductionNode(Source source,
			IdentifierExpressionNode function,
			SequenceNode<IdentifierExpressionNode> variables) {
		return new CommonOmpFunctionReductionNode(source, function, variables);
	}

	@Override
	public OmpWorkshareNode newWorkshareNode(Source source,
			OmpWorkshareNodeKind kind) {
		return new CommonOmpWorkshareNode(source, kind);
	}

}
