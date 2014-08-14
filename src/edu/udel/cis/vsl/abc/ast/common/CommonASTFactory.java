package edu.udel.cis.vsl.abc.ast.common;

import java.io.PrintStream;
import java.util.Iterator;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode.NodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.NodeFactory;
import edu.udel.cis.vsl.abc.ast.node.IF.PragmaNode;
import edu.udel.cis.vsl.abc.ast.node.IF.SequenceNode;
import edu.udel.cis.vsl.abc.ast.node.IF.StaticAssertionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundInitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.CompoundLiteralObject;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.LiteralObject;
import edu.udel.cis.vsl.abc.ast.node.IF.compound.ScalarLiteralObject;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AbstractFunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.EnumeratorDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FieldDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.InitializerNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.OrdinaryDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.OrdinaryDeclarationNode.OrdinaryDeclarationKind;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.TypedefDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.VariableDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ArrowNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CastNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.CompoundLiteralNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ConstantNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.DotNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode.ExpressionKind;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.OperatorNode.Operator;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.RegularRangeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeableNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SizeofNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.SpawnNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.LabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.OrdinaryLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.label.SwitchLabelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpDeclarativeNode.OmpDeclarativeNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpFunctionReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpNode.OmpNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpParallelNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpStatementNode.OmpStatementNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSymbolReductionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpSyncNode.OmpSyncNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode;
import edu.udel.cis.vsl.abc.ast.node.IF.omp.OmpWorksharingNode.OmpWorksharingNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssumeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AtomicNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.BlockItemNode.BlockItemKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CivlForNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.CompoundStatementNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.DeclarationListNode;
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
import edu.udel.cis.vsl.abc.ast.node.IF.statement.StatementNode.StatementKind;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.SwitchNode;
import edu.udel.cis.vsl.abc.ast.node.IF.statement.WhenNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.ArrayTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.BasicTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.DomainTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.EnumerationTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.FunctionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.PointerTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.StructureOrUnionTypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypeNode.TypeNodeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.type.TypedefNameNode;
import edu.udel.cis.vsl.abc.ast.type.IF.ArrayType;
import edu.udel.cis.vsl.abc.ast.type.IF.StandardBasicType.BasicTypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.StructureOrUnionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.ast.type.IF.Type.TypeKind;
import edu.udel.cis.vsl.abc.ast.type.IF.TypeFactory;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;

public class CommonASTFactory implements ASTFactory {

	private static String indention = "  ";

	private NodeFactory nodeFactory;

	private TokenFactory tokenFactory;

	private TypeFactory typeFactory;

	public CommonASTFactory(NodeFactory nodeFactory, TokenFactory tokenFactory,
			TypeFactory typeFactory) {
		this.nodeFactory = nodeFactory;
		this.tokenFactory = tokenFactory;
		this.typeFactory = typeFactory;
	}

	@Override
	public NodeFactory getNodeFactory() {
		return nodeFactory;
	}

	@Override
	public TokenFactory getTokenFactory() {
		return tokenFactory;
	}

	@Override
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	@Override
	public AST newAST(SequenceNode<ExternalDefinitionNode> root)
			throws SyntaxException {
		AST unit = new CommonAST(this, root, false);

		// do some preparation?
		return unit;
	}

	@Override
	public void prettyPrint(ASTNode node, PrintStream out) {
		NodeKind kind = node.nodeKind();
		StringBuffer result = null;

		switch (kind) {
		case DECLARATION_LIST:
			result = declarationList2CIVL((DeclarationListNode) node);
			break;
		case ENUMERATOR_DECLARATION:
			result = enumeratorDeclaration2CIVL((EnumeratorDeclarationNode) node);
			break;
		case EXPRESSION:
			result = expression2CIVL((ExpressionNode) node);
			break;
		case FIELD_DECLARATION:
			result = fieldDeclaration2CIVL("", (FieldDeclarationNode) node);
			break;
		case FUNCTION_DECLARATION:
			functionDeclaration2CIVL("", (FunctionDeclarationNode) node);
			break;
		case IDENTIFIER:
			result = new StringBuffer(((IdentifierNode) node).name());
			break;
		case OMP_NODE:
			result = ompNode2CIVL("", (OmpNode) node);
			break;
		case OMP_REDUCTION_OPERATOR:
			result = ompReductionNode2CIVL((OmpReductionNode) node);
			break;
		case ORDINARY_LABEL:
		case SWITCH_LABEL:
			result = labelNode2CIVL((LabelNode) node);
			break;
		case PRAGMA:
			result = pragma2CIVL("", (PragmaNode) node);
			break;
		case STATEMENT:
			result = statement2CIVL("", (StatementNode) node);
			break;
		case STATIC_ASSERTION:
			result = staticAssertion2CIVL("", (StaticAssertionNode) node);
			break;
		case TYPE:
			result = type2CIVL("", (TypeNode) node, true);
			break;
		case TYPEDEF:
			result = typedefDeclaration2CIVL("", (TypedefDeclarationNode) node);
			break;
		case VARIABLE_DECLARATION:
			result = variableDeclaration2CIVL("",
					(VariableDeclarationNode) node);
			break;
		case PAIR:
		case SEQUENCE:
		default:
			throw new ABCUnsupportedException(
					"The pretty printing of AST node of " + kind
							+ " kind is not supported yet.", node.getSource()
							.getLocation(false));
		}
		if (result == null)
			out.print(result);
	}

	private static StringBuffer ompNode2CIVL(String prefix, OmpNode ompNode) {
		OmpNodeKind kind = ompNode.ompNodeKind();

		switch (kind) {
		case DECLARATIVE:
			return ompDeclarative2CIVL(prefix, (OmpDeclarativeNode) ompNode);
		default:// EXECUTABLE
			return ompStatement2CIVL(prefix, (OmpStatementNode) ompNode);
		}
	}

	static StringBuffer structOrUnion2CIVL(String prefix,
			StructureOrUnionTypeNode strOrUnion) {
		StringBuffer result = new StringBuffer();
		String myIndent = prefix + indention;
		SequenceNode<FieldDeclarationNode> fields = strOrUnion
				.getStructDeclList();

		result.append(prefix);
		if (strOrUnion.isStruct())
			result.append("struct ");
		else
			result.append("union ");
		if (strOrUnion.getName() != null)
			result.append(strOrUnion.getName());
		if (fields != null) {
			int numFields = fields.numChildren();

			result.append("{");
			for (int i = 0; i < numFields; i++) {
				FieldDeclarationNode field = fields.getSequenceChild(i);

				result.append("\n");
				if (!(field.getTypeNode() instanceof StructureOrUnionTypeNode))
					result.append(myIndent);
				result.append(fieldDeclaration2CIVL(myIndent, field));
				result.append(";");
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private static StringBuffer fieldDeclaration2CIVL(String prefix,
			FieldDeclarationNode field) {
		String type;
		StringBuffer result = new StringBuffer();

		type = type2CIVL(prefix, field.getTypeNode(), true).toString();
		if (type.endsWith("]")) {
			int start = type.indexOf("[");
			String suffix = type.substring(start);

			type = type.substring(0, start);
			result.append(type);
			result.append(" ");
			result.append(field.getName());
			result.append(suffix);
		} else {
			result.append(type);
			result.append(" ");
			result.append(field.getName());
		}
		return result;
	}

	static StringBuffer staticAssertion2CIVL(String prefix,
			StaticAssertionNode assertion) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("(");
		result.append(expression2CIVL(assertion.getExpression()));
		result.append(", \"");
		result.append(assertion.getMessage().getStringRepresentation());
		result.append("\")");
		return result;
	}

	static StringBuffer pragma2CIVL(String prefix, PragmaNode pragma) {
		StringBuffer result = new StringBuffer();
		Iterator<CToken> tokens = pragma.getTokens();

		result.append(prefix);
		result.append("#pragma ");
		result.append(pragma.getPragmaIdentifier().name());

		while (tokens.hasNext()) {
			CToken token = tokens.next();

			result.append(" ");
			result.append(token.getText());
		}
		return result;
	}

	static StringBuffer ordinaryDeclaration2CIVL(String prefix,
			OrdinaryDeclarationNode declaration) {
		OrdinaryDeclarationKind kind = declaration.ordinaryDeclarationKind();

		switch (kind) {
		case VARIABLE_DECLARATION:
			return variableDeclaration2CIVL(prefix,
					(VariableDeclarationNode) declaration);
		default: // cases FUNCTION_DECLARATION, FUNCTION_DEFINITION,
					// ABSTRACT_FUNCTION_DEFINITION:
			return functionDeclaration2CIVL(prefix,
					(FunctionDeclarationNode) declaration);
		}
	}

	static StringBuffer enumType2CIVL(String prefix,
			EnumerationTypeNode enumeration) {
		StringBuffer result = new StringBuffer();
		IdentifierNode tag = enumeration.getTag();
		SequenceNode<EnumeratorDeclarationNode> enumerators = enumeration
				.enumerators();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append("enum ");
		if (tag != null)
			result.append(tag.name());
		if (enumerators != null) {
			int num = enumerators.numChildren();

			result.append("{");
			for (int i = 0; i < num; i++) {
				EnumeratorDeclarationNode enumerator = enumerators
						.getSequenceChild(i);

				if (i != 0)
					result.append(",");
				result.append("\n");
				result.append(myIndent);
				result.append(enumeratorDeclaration2CIVL(enumerator));
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private static StringBuffer enumeratorDeclaration2CIVL(
			EnumeratorDeclarationNode enumerator) {
		StringBuffer result = new StringBuffer();

		result.append(enumerator.getName());
		if (enumerator.getValue() != null) {
			result.append("=");
			result.append(expression2CIVL(enumerator.getValue()));
		}
		return result;
	}

	static StringBuffer ompDeclarative2CIVL(String prefix,
			OmpDeclarativeNode ompDeclarative) {
		StringBuffer result = new StringBuffer();
		OmpDeclarativeNodeKind kind = ompDeclarative.ompDeclarativeNodeKind();

		result.append("#pragma omp ");
		switch (kind) {
		case REDUCTION:
			result.append("reduction");
			break;
		case THREADPRIVATE:
			result.append("threadprivate");
			break;
		default:
			throw new ABCUnsupportedException(
					"The OpenMP declarative directive " + kind
							+ " is not supported yet.", ompDeclarative
							.getSource().getLocation(false));
		}
		result.append("(");
		result.append(sequenceExpressionNode2CIVL(ompDeclarative.variables()));
		result.append(")");
		return result;
	}

	private static StringBuffer functionDeclaration2CIVL(String prefix,
			FunctionDeclarationNode function) {
		StringBuffer result = new StringBuffer();
		FunctionTypeNode typeNode = function.getTypeNode();
		TypeNode returnType = typeNode.getReturnType();
		SequenceNode<VariableDeclarationNode> paras = typeNode.getParameters();
		int numOfParas = paras.numChildren();

		if (function instanceof AbstractFunctionDefinitionNode)
			result.append("$abstract ");
		result.append(prefix);
		result.append(type2CIVL(prefix, returnType, false));
		result.append(" ");
		result.append(function.getName());
		result.append("(");
		for (int i = 0; i < numOfParas; i++) {
			if (i != 0)
				result.append(", ");
			result.append(variableDeclaration2CIVL("",
					paras.getSequenceChild(i)));
		}
		result.append(")");

		if (function instanceof FunctionDefinitionNode) {
			CompoundStatementNode body = ((FunctionDefinitionNode) function)
					.getBody();

			result.append("\n");
			result.append(compoundStatement2CIVL(prefix + indention, body));
		} else
			result.append(";");
		return result;
	}

	private static StringBuffer compoundStatement2CIVL(String prefix,
			CompoundStatementNode compound) {
		StringBuffer result = new StringBuffer();
		int numChildren = compound.numChildren();
		String myPrefix = prefix.substring(0, prefix.length() - 2);

		result.append(myPrefix);
		result.append("{\n");
		for (int i = 0; i < numChildren; i++) {
			BlockItemNode child = compound.getSequenceChild(i);

			if (child != null) {
				result.append(blockItem2CIVL(prefix, child));
				result.append("\n");
			}
		}
		result.append(myPrefix);
		result.append("}");
		return result;
	}

	private static StringBuffer blockItem2CIVL(String prefix,
			BlockItemNode block) {
		BlockItemKind kind = block.blockItemKind();

		switch (kind) {
		case STATEMENT:
			return statement2CIVL(prefix, (StatementNode) block);
		case ORDINARY_DECLARATION:
			if (block instanceof VariableDeclarationNode) {
				StringBuffer result = variableDeclaration2CIVL(prefix,
						(VariableDeclarationNode) block);

				result.append(";");
				return result;
			} else if (block instanceof FunctionDeclarationNode) {
				return functionDeclaration2CIVL(prefix,
						(FunctionDeclarationNode) block);
			}
		case TYPEDEF:
			return typedefDeclaration2CIVL(prefix,
					(TypedefDeclarationNode) block);
		case ENUMERATOR:
			return new StringBuffer(prefix + "enum;");
		case PRAGMA:
			return pragma2CIVL(prefix, (PragmaNode) block);
		default:
			throw new ABCUnsupportedException("translating block item node of "
					+ kind + " kind into CIVL code");
		}
	}

	static StringBuffer typedefDeclaration2CIVL(String prefix,
			TypedefDeclarationNode typedef) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("typdef ");
		result.append(" ");
		result.append(type2CIVL(prefix, typedef.getTypeNode(), true));
		result.append(" ");
		result.append(typedef.getName());
		return result;
	}

	private static StringBuffer statement2CIVL(String prefix,
			StatementNode statement) {
		StatementKind kind = statement.statementKind();

		switch (kind) {
		case ASSUME:
			return assume2CIVL(prefix, (AssumeNode) statement);
		case ATOMIC:
			return atomic2CIVL(prefix, (AtomicNode) statement);
		case COMPOUND:
			return compoundStatement2CIVL(prefix,
					(CompoundStatementNode) statement);
		case EXPRESSION:
			return expressionStatement2CIVL(prefix,
					(ExpressionStatementNode) statement);
		case CIVL_FOR:
			return civlForStatement2CIVL(prefix, (CivlForNode) statement);
		case FOR:
			return for2CIVL(prefix, (ForLoopNode) statement);
		case GOTO:
			return goto2CIVL(prefix, (GotoNode) statement);
		case IF:
			return if2CIVL(prefix, (IfNode) statement);
		case JUMP:
			return jump2CIVL(prefix, (JumpNode) statement);
		case LABELED:
			return labeled2CIVL(prefix, (LabeledStatementNode) statement);
		case LOOP:
			return loop2CIVL(prefix, (LoopNode) statement);
		case NULL:
			return new StringBuffer(";");
		case OMP_STATEMENT:
			return ompStatement2CIVL(prefix, (OmpStatementNode) statement);
		case RETURN:
			return return2CIVL(prefix, (ReturnNode) statement);
		case SWITCH:
			return switch2CIVL(prefix, (SwitchNode) statement);
		case WHEN:
			return when2CIVL(prefix, (WhenNode) statement);
		default:
			// throw new CIVLUnimplementedFeatureException(
			// "translating statement node of " + kind
			// + " kind into CIVL code", statement.getSource());
			return new StringBuffer(kind.toString());
		}
	}

	private static StringBuffer ompStatement2CIVL(String prefix,
			OmpStatementNode ompStmt) {
		StringBuffer result = new StringBuffer();
		OmpStatementNodeKind kind = ompStmt.ompStatementNodeKind();
		SequenceNode<IdentifierExpressionNode> privateList = ompStmt
				.privateList(), firstPrivateList = ompStmt.firstprivateList(), sharedList = ompStmt
				.sharedList(), copyinList = ompStmt.copyinList(), copyPrivateList = ompStmt
				.copyprivateList(), lastPrivateList = ompStmt.lastprivateList();
		SequenceNode<OmpReductionNode> reductionList = ompStmt.reductionList();
		boolean nowait = ompStmt.nowait();
		String myIndent = prefix + indention;
		StatementNode block = ompStmt.statementNode();

		result.append(prefix);
		result.append("#pragma omp ");
		switch (kind) {
		case PARALLEL:
			result.append(ompParallel2CIVL(prefix, (OmpParallelNode) ompStmt));
			break;
		case SYNCHRONIZATION:
			result.append(ompSync2CIVL(prefix, (OmpSyncNode) ompStmt));
			break;
		default: // case WORKSHARING:
			result.append(ompWorksharing2CIVL(prefix,
					(OmpWorksharingNode) ompStmt));
			break;
		}
		if (nowait)
			result.append("nowait");
		if (privateList != null) {
			result.append("private(");
			result.append(sequenceExpressionNode2CIVL(privateList));
			result.append(") ");
		}
		if (firstPrivateList != null) {
			result.append("firstprivate(");
			result.append(sequenceExpressionNode2CIVL(firstPrivateList));
			result.append(") ");
		}
		if (sharedList != null) {
			result.append("shared(");
			result.append(sequenceExpressionNode2CIVL(sharedList));
			result.append(") ");
		}
		if (copyinList != null) {
			result.append("copyin(");
			result.append(sequenceExpressionNode2CIVL(copyinList));
			result.append(") ");
		}
		if (copyPrivateList != null) {
			result.append("copyprivate(");
			result.append(sequenceExpressionNode2CIVL(copyPrivateList));
			result.append(") ");
		}
		if (lastPrivateList != null) {
			result.append("lastprivate(");
			result.append(sequenceExpressionNode2CIVL(lastPrivateList));
			result.append(") ");
		}
		if (reductionList != null) {
			result.append(sequenceReductionNode2CIVL(reductionList));
		}
		result.append("\n");
		if (block != null)
			result.append(statement2CIVL(myIndent, block));
		return result;
	}

	private static StringBuffer ompWorksharing2CIVL(String prefix,
			OmpWorksharingNode ompWs) {
		StringBuffer result = new StringBuffer();
		OmpWorksharingNodeKind kind = ompWs.ompWorkshareNodeKind();

		switch (kind) {
		case FOR: {
			OmpForNode forNode = (OmpForNode) ompWs;
			int collapse = forNode.collapse();

			result.append("for schedule(");
			switch (forNode.schedule()) {
			case AUTO:
				result.append("auto");
				break;
			case DYNAMIC:
				result.append("dynamic");
				break;
			case GUIDED:
				result.append("guided");
				break;
			case RUNTIME:
				result.append("runtime");
				break;
			default:// STATIC
				result.append("static");
				break;
			}
			if (forNode.chunkSize() != null) {
				result.append(", ");
				result.append(expression2CIVL(forNode.chunkSize()));
			}
			result.append(") ");
			if (collapse > 1) {
				result.append("collapse(");
				result.append(collapse);
				result.append(")");
			}
			if (forNode.ordered())
				result.append("ordered");
			break;
		}
		case SECTIONS:
			result.append("sections ");
			break;
		case SINGLE:
			result.append("single ");
			break;
		default: // case SECTION:
			result.append("section ");
		}
		return result;
	}

	private static StringBuffer ompSync2CIVL(String prefix, OmpSyncNode ompSync) {
		OmpSyncNodeKind kind = ompSync.ompSyncNodeKind();
		StringBuffer result = new StringBuffer();

		switch (kind) {
		case MASTER:
			result.append("master ");
			break;
		case CRITICAL:
			result.append("critical ");
			if (ompSync.criticalName() != null) {
				result.append("(");
				result.append(ompSync.criticalName().name());
				result.append(")");
			}
			break;
		case BARRIER:
			result.append("barrier ");
			break;
		case FLUSH:
			result.append("flush ");
			if (ompSync.flushedList() != null) {
				result.append("(");
				result.append(sequenceExpressionNode2CIVL(ompSync.flushedList()));
				result.append(")");
			}
			break;
		default:// ORDERED
			result.append("ordered ");
		}
		return result;
	}

	private static StringBuffer ompParallel2CIVL(String prefix,
			OmpParallelNode para) {
		StringBuffer result = new StringBuffer();
		ExpressionNode ifClause = para.ifClause(), numThreads = para
				.numThreads();
		boolean isDefaultShared = para.isDefaultShared();

		result.append("parallel ");
		if (ifClause != null) {
			result.append("if(");
			result.append(expression2CIVL(ifClause));
			result.append(") ");
		}
		if (numThreads != null) {
			result.append("num_threads(");
			result.append(expression2CIVL(numThreads));
			result.append(") ");
		}
		if (isDefaultShared)
			result.append("default(shared) ");
		else
			result.append("default(none) ");
		return result;
	}

	private static StringBuffer sequenceReductionNode2CIVL(
			SequenceNode<OmpReductionNode> sequence) {
		StringBuffer result = new StringBuffer();
		int num = sequence.numChildren();

		for (int i = 0; i < num; i++) {
			OmpReductionNode reduction = sequence.getSequenceChild(i);

			result.append(ompReductionNode2CIVL(reduction));
			if (i < num - 1)
				result.append(" ");
		}
		return result;
	}

	private static StringBuffer ompReductionNode2CIVL(OmpReductionNode reduction) {
		StringBuffer result = new StringBuffer();

		result.append("reduction(");
		switch (reduction.ompReductionOperatorNodeKind()) {
		case FUNCTION: {
			OmpFunctionReductionNode funcNode = (OmpFunctionReductionNode) reduction;

			result.append(expression2CIVL(funcNode.function()));
			break;
		}
		default: // operator
		{
			OmpSymbolReductionNode symbol = (OmpSymbolReductionNode) reduction;

			switch (symbol.operator()) {
			case PLUSEQ:
				result.append("+");
				break;
			case MINUSEQ:
				result.append("-");
				break;
			case TIMESEQ:
				result.append("*");
				break;
			case BITANDEQ:
				result.append("&");
				break;
			case BITOREQ:
				result.append("|");
				break;
			case BITXOREQ:
				result.append("^");
				break;
			case LAND:
				result.append("&&");
				break;
			case LOR:
				result.append("||");
				break;
			default:
				throw new ABCRuntimeException(
						"Invalid operator for OpenMP reduction: "
								+ symbol.operator(), reduction.getSource()
								.getLocation(false));
			}
		}
		}
		result.append(": ");
		result.append(sequenceExpressionNode2CIVL(reduction.variables()));
		result.append(")");
		return result;
	}

	private static StringBuffer sequenceExpressionNode2CIVL(
			SequenceNode<IdentifierExpressionNode> sequence) {
		StringBuffer result = new StringBuffer();
		int numExpressions = sequence.numChildren();

		for (int i = 0; i < numExpressions; i++) {
			IdentifierExpressionNode expression = sequence.getSequenceChild(i);

			if (i != 0)
				result.append(", ");
			result.append(expression2CIVL(expression));
		}
		return result;
	}

	private static StringBuffer civlForStatement2CIVL(String prefix,
			CivlForNode civlFor) {
		civlFor.getDomain();
		// civlFor.

		// TODO Auto-generated method stub
		return null;
	}

	private static StringBuffer loop2CIVL(String prefix, LoopNode loop) {
		StringBuffer result = new StringBuffer();
		LoopKind loopKind = loop.getKind();
		StringBuffer condition = expression2CIVL(loop.getCondition());
		String myIndent = prefix + indention;
		StatementNode bodyNode = loop.getBody();
		StringBuffer body = bodyNode == null ? null : statement2CIVL(myIndent,
				loop.getBody());

		switch (loopKind) {
		case WHILE:
			result.append("while(");
			result.append(condition);
			result.append(")");
			if (body == null)
				result.append(";");
			else {
				result.append("\n");
				result.append(body);
			}
		case DO_WHILE:
			result.append("do");
			if (body == null)
				result.append(";");
			else {
				result.append("\n");
				result.append(body);
			}
			result.append("while(");
			result.append(condition);
			result.append(");");
			break;
		default:
			throw new ABCUnsupportedException(
					"The "
							+ loopKind
							+ " loop node is unreachable here because it should already been taken care of priorly.");
		}
		return result;
	}

	private static StringBuffer atomic2CIVL(String prefix, AtomicNode atomicNode) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		if (atomicNode.isAtom())
			result.append("$atom\n");
		else
			result.append("$atomic\n");
		result.append(statement2CIVL(prefix + indention, atomicNode.getBody()));
		return result;
	}

	private static StringBuffer goto2CIVL(String prefix, GotoNode go2) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("goto ");
		result.append(go2.getLabel().name());
		result.append(";");
		return result;
	}

	private static StringBuffer labeled2CIVL(String prefix,
			LabeledStatementNode labeled) {
		LabelNode label = labeled.getLabel();
		StatementNode statement = labeled.getStatement();
		StringBuffer result = new StringBuffer();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append(labelNode2CIVL(label));
		result.append("\n");
		result.append(statement2CIVL(myIndent, statement));
		return result;
	}

	private static StringBuffer labelNode2CIVL(LabelNode label) {
		StringBuffer result = new StringBuffer();

		if (label instanceof OrdinaryLabelNode) {
			OrdinaryLabelNode ordinary = (OrdinaryLabelNode) label;
			result.append(ordinary.getName());
			result.append(":");
		} else {
			// switch label
			SwitchLabelNode switchLabel = (SwitchLabelNode) label;
			boolean isDefault = switchLabel.isDefault();

			if (isDefault)
				result.append("default:");
			else {
				result.append("case ");
				result.append(expression2CIVL(switchLabel.getExpression()));
				result.append(":");
			}
		}
		return result;
	}

	private static StringBuffer switch2CIVL(String prefix, SwitchNode swtichNode) {
		return new StringBuffer("switch");
		// TODO throw new CIVLUnimplementedFeatureException(
		// "translating switch node into CIVL code",
		// swtichNode.getSource());
	}

	private static StringBuffer jump2CIVL(String prefix, JumpNode jump) {
		JumpKind kind = jump.getKind();
		StringBuffer result = new StringBuffer();

		switch (kind) {
		case GOTO:
			result.append("goto");
			break;
		case CONTINUE:
			result.append("continue;");
			break;
		case BREAK:
			result.append("break;");
			break;
		case RETURN:
			return return2CIVL(prefix, (ReturnNode) jump);
		default:
			throw new ABCUnsupportedException("translating jump node of "
					+ kind + " kind into CIVL code");
		}
		return result;
	}

	private static StringBuffer return2CIVL(String prefix, ReturnNode returnNode) {
		StringBuffer result = new StringBuffer();
		ExpressionNode expr = returnNode.getExpression();

		result.append(prefix);
		result.append("return");
		if (expr != null) {
			result.append(" ");
			result.append(expression2CIVL(expr));
		}
		result.append(";");
		return result;
	}

	private static StringBuffer if2CIVL(String prefix, IfNode ifNode) {
		StringBuffer result = new StringBuffer();
		ExpressionNode condition = ifNode.getCondition();
		StatementNode trueBranch = ifNode.getTrueBranch();
		StatementNode falseBranch = ifNode.getFalseBranch();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append("if(");
		if (condition != null)
			result.append(expression2CIVL(condition));
		result.append(")");
		if (trueBranch == null)
			result.append(";");
		else {
			result.append("\n");
			result.append(statement2CIVL(myIndent, trueBranch));
		}
		if (falseBranch != null) {
			result.append("\n");
			result.append(prefix);
			result.append("else\n");
			result.append(statement2CIVL(myIndent, falseBranch));
		}
		return result;
	}

	private static StringBuffer for2CIVL(String prefix, ForLoopNode loop) {
		StringBuffer result = new StringBuffer();
		ForLoopInitializerNode init = loop.getInitializer();
		ExpressionNode condition = loop.getCondition();
		ExpressionNode incrementer = loop.getIncrementer();
		StatementNode body = loop.getBody();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append("for(");
		if (init != null) {
			if (init instanceof ExpressionNode)
				result.append(expression2CIVL((ExpressionNode) init));
			else if (init instanceof DeclarationListNode)
				result.append(declarationList2CIVL((DeclarationListNode) init));
		}
		result.append("; ");
		if (condition != null) {
			result.append(expression2CIVL(condition));
		}
		result.append("; ");
		if (incrementer != null) {
			result.append(expression2CIVL(incrementer));
		}
		result.append(")");
		if (body == null)
			result.append(";");
		else {
			result.append("\n");
			result.append(statement2CIVL(myIndent, body));
		}
		return result;
	}

	private static StringBuffer declarationList2CIVL(DeclarationListNode list) {
		int num = list.numChildren();
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < num; i++) {
			VariableDeclarationNode var = list.getSequenceChild(i);

			if (i != 0)
				result.append(", ");
			result.append(variableDeclaration2CIVL("", var));
		}
		return result;
	}

	private static StringBuffer expressionStatement2CIVL(String prefix,
			ExpressionStatementNode expr) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append(expression2CIVL(expr.getExpression()));
		result.append(";");
		return result;
	}

	private static StringBuffer when2CIVL(String prefix, WhenNode when) {
		StringBuffer result = new StringBuffer();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append("$when(");
		result.append(expression2CIVL(when.getGuard()));
		result.append(")\n");
		result.append(statement2CIVL(myIndent, when.getBody()));
		return result;
	}

	static private StringBuffer variableDeclaration2CIVL(String prefix,
			VariableDeclarationNode variable) {
		StringBuffer result = new StringBuffer();
		InitializerNode init = variable.getInitializer();
		TypeNode typeNode = variable.getTypeNode();
		String type;

		result.append(prefix);
		if (typeNode.isInputQualified())
			result.append("$input ");
		if (typeNode.isOutputQualified())
			result.append("$output ");
		type = type2CIVL(prefix, typeNode, false).toString();
		if (type.endsWith("]")) {
			int start = type.indexOf('[');
			String suffix = type.substring(start);

			type = type.substring(0, start);
			result.append(type);
			result.append(" ");
			result.append(variable.getName());
			result.append(suffix);
		} else {
			result.append(type);
			result.append(" ");
			result.append(variable.getName());
		}
		if (init != null) {
			result.append(" = ");
			result.append(initializer2CIVL(init));
		}
		return result;
	}

	private static StringBuffer initializer2CIVL(InitializerNode init) {
		if (init instanceof CompoundInitializerNode) {
			CompoundInitializerNode compoundInit = (CompoundInitializerNode) init;

			return compoundLiteralObject2CIVL(init.getSource(),
					compoundInit.getType(), compoundInit.getLiteralObject());
		} else if (init instanceof ExpressionNode)
			return expression2CIVL((ExpressionNode) init);
		else
			throw new ABCRuntimeException("Invalid initializer: "
					+ init.toString());
	}

	private static StringBuffer compoundLiteralObject2CIVL(Source source,
			Type type, CompoundLiteralObject compoundObj) {
		StringBuffer result = new StringBuffer();
		TypeKind typeKind = type.kind();
		int size = compoundObj.size();

		switch (typeKind) {
		case ARRAY: {
			Type elementType = ((ArrayType) type).getElementType();

			result.append("{");
			for (int i = 0; i < size; i++) {
				if (i != 0)
					result.append(", ");
				// result.append("[");
				// result.append(i);
				// result.append("]=");
				result.append(literalObject2CIVL(source, elementType,
						compoundObj.get(i)));
			}
			result.append("}");
		}
			break;
		case STRUCTURE_OR_UNION: {
			StructureOrUnionType structType = (StructureOrUnionType) type;

			if (!structType.isStruct())
				throw new ABCRuntimeException(
						"Invalid type of compound initializer: union");
			result.append("{");
			for (int i = 0; i < size; i++) {
				Field field = structType.getField(i);

				if (i != 0)
					result.append(", ");
				result.append(".");
				result.append(field.getName());
				result.append("=");
				result.append(literalObject2CIVL(source, field.getType(),
						compoundObj.get(i)));
			}
			result.append("}");
		}
			break;
		default:
			throw new ABCRuntimeException(
					"Invalid type of compound initializer: " + typeKind);
		}
		return result;
	}

	private static StringBuffer literalObject2CIVL(Source source, Type type,
			LiteralObject obj) {
		if (obj instanceof CompoundLiteralObject)
			return compoundLiteralObject2CIVL(source, type,
					(CompoundLiteralObject) obj);
		else if (obj instanceof ScalarLiteralObject)
			return expression2CIVL(((ScalarLiteralObject) obj).getExpression());
		else
			throw new ABCRuntimeException("Invalid literal object: " + obj);
	}

	static StringBuffer assume2CIVL(String prefix, AssumeNode assume) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("$assume ");
		result.append(expression2CIVL(assume.getExpression()));
		result.append(";");
		return result;
	}

	private static StringBuffer expression2CIVL(ExpressionNode expression) {
		StringBuffer result = new StringBuffer();
		ExpressionKind kind = expression.expressionKind();

		switch (kind) {
		case ARROW: {
			ArrowNode arrow = (ArrowNode) expression;

			result.append(expression2CIVL(arrow.getStructurePointer()));
			result.append("->");
			result.append(arrow.getFieldName().name());
			break;
		}
		case CAST: {
			CastNode cast = (CastNode) expression;

			result.append("(");
			result.append(type2CIVL("", cast.getCastType(), false));
			result.append(")");
			result.append(expression2CIVL(cast.getArgument()));
			break;
		}
		case COMPOUND_LITERAL:
			result.append(compoundLiteralNode2CIVL((CompoundLiteralNode) expression));
			break;
		case CONSTANT: {
			String constant = ((ConstantNode) expression)
					.getStringRepresentation();

			if (constant.equals("\\false"))
				constant = "$false";
			else if (constant.equals("\\true"))
				constant = "$true";
			result.append(constant);
			break;
		}
		case DOT: {
			DotNode dot = (DotNode) expression;

			result.append(expression2CIVL(dot.getStructure()));
			result.append(".");
			result.append(dot.getFieldName().name());
			break;
		}
		case FUNCTION_CALL:
			return functionCall2CIVL((FunctionCallNode) expression);
		case IDENTIFIER_EXPRESSION:
			result.append(((IdentifierExpressionNode) expression)
					.getIdentifier().name());
			break;
		case OPERATOR:
			result = operator2CIVL((OperatorNode) expression);
			break;
		case SIZEOF:
			result.append("sizeof(");
			result.append(sizeable2CIVL(((SizeofNode) expression).getArgument()));
			break;
		case SPAWN:
			result.append("$spawn ");
			result.append(functionCall2CIVL(((SpawnNode) expression).getCall()));
			break;
		case REGULAR_RANGE:
			result.append(regularRange2CIVL((RegularRangeNode) expression));
			break;
		default:
			throw new ABCUnsupportedException("translating expression node of "
					+ kind + " kind into CIVL code");
		}
		return result;
	}

	private static StringBuffer compoundLiteralNode2CIVL(
			CompoundLiteralNode expression) {
		StringBuffer result = new StringBuffer();

		// TODO
		return result;
	}

	private static StringBuffer regularRange2CIVL(RegularRangeNode range) {
		StringBuffer result = new StringBuffer();
		ExpressionNode step = range.getStep();

		result.append(expression2CIVL(range.getLow()));
		result.append(" .. ");
		result.append(expression2CIVL(range.getHigh()));
		if (step != null) {
			result.append(" # ");
			result.append(expression2CIVL(step));
		}
		return result;
	}

	private static StringBuffer sizeable2CIVL(SizeableNode argument) {
		if (argument instanceof ExpressionNode)
			return expression2CIVL((ExpressionNode) argument);
		return type2CIVL("", (TypeNode) argument, false);
	}

	private static StringBuffer functionCall2CIVL(FunctionCallNode call) {
		int argNum = call.getNumberOfArguments();
		StringBuffer result = new StringBuffer();

		result.append(expression2CIVL(call.getFunction()));
		result.append("(");
		for (int i = 0; i < argNum; i++) {
			if (i > 0)
				result.append(", ");
			result.append(expression2CIVL(call.getArgument(i)));
		}
		result.append(")");
		return result;
	}

	private static StringBuffer operator2CIVL(OperatorNode operator) {
		StringBuffer result = new StringBuffer();
		Operator op = operator.getOperator();
		StringBuffer arg0 = expression2CIVL(operator.getArgument(0));
		StringBuffer arg1 = operator.getNumberOfArguments() > 1 ? expression2CIVL(operator
				.getArgument(1)) : null;

		switch (op) {
		case ADDRESSOF:
			result.append("&");
			result.append(arg0);
			break;
		case ASSIGN:
			result.append(arg0);
			result.append(" = ");
			result.append(arg1);
			break;
		case BIG_O:
			result.append("$O(");
			result.append(arg0);
			result.append(")");
			break;
		case BITAND:
			result.append(arg0);
			result.append(" & ");
			result.append(arg1);
			break;
		case BITCOMPLEMENT:
			result.append("~");
			result.append(arg0);
			break;
		case BITOR:
			result.append(arg0);
			result.append(" | ");
			result.append(arg1);
			break;
		case BITXOR:
			result.append(arg0);
			result.append(" ^ ");
			result.append(arg1);
			break;
		case CONDITIONAL:
			result.append(arg0);
			result.append(" ? ");
			result.append(arg1);
			result.append(" : ");
			result.append(expression2CIVL(operator.getArgument(2)));
			break;
		case DEREFERENCE:
			result.append("*");
			result.append(arg0);
			break;
		case DIV:
			result.append(arg0);
			result.append(" / ");
			result.append(arg1);
			break;
		case EQUALS:
			result.append(arg0);
			result.append(" == ");
			result.append(arg1);
			break;
		case GT:
			result.append(arg0);
			result.append(" > ");
			result.append(arg1);
			break;
		case GTE:
			result.append(arg0);
			result.append(" >= ");
			result.append(arg1);
			break;
		case IMPLIES:
			result.append(arg0);
			result.append(" => ");
			result.append(arg1);
			break;
		case LAND:
			result.append(arg0);
			result.append(" && ");
			result.append(arg1);
			break;
		case LOR:
			result.append(arg0);
			result.append(" || ");
			result.append(arg1);
			break;
		case LT:
			result.append(arg0);
			result.append(" < ");
			result.append(arg1);
			break;
		case LTE:
			result.append(arg0);
			result.append(" <= ");
			result.append(arg1);
			break;
		case MINUS:
			result.append(arg0);
			result.append(" － ");
			result.append(arg1);
			break;
		case MOD:
			result.append(arg0);
			result.append(" % ");
			result.append(arg1);
			break;
		case NEQ:
			result.append(arg0);
			result.append(" != ");
			result.append(arg1);
			break;
		case NOT:
			result.append("!");
			result.append(arg0);
			break;
		case PLUS:
			result.append(arg0);
			result.append(" + ");
			result.append(arg1);
			break;
		case PLUSEQ:
			result.append(arg0);
			result.append(" += ");
			result.append(arg1);
			break;
		case PREDECREMENT:
			result.append("--");
			result.append(arg0);
		case PREINCREMENT:
			result.append("++");
			result.append(arg0);
		case POSTINCREMENT:
			result.append(arg0);
			result.append("++");
			break;
		case POSTDECREMENT:
			result.append(arg0);
			result.append("--");
			break;
		case SHIFTLEFT:
			result.append(arg0);
			result.append(" << ");
			result.append(arg1);
			break;
		case SHIFTRIGHT:
			result.append(arg0);
			result.append(" >> ");
			result.append(arg1);
			break;
		case SUBSCRIPT:
			result.append(arg0);
			result.append("[");
			result.append(arg1);
			result.append("]");
			break;
		case TIMES:
			result.append(arg0);
			result.append(" * ");
			result.append(arg1);
			break;
		case UNARYMINUS:
			result.append("-");
			result.append(arg0);
			break;
		case UNARYPLUS:
			result.append("+");
			result.append(arg0);
			break;
		default:
			throw new ABCUnsupportedException("translating operator node of "
					+ op + " kind into CIVL code");
		}
		return result;
	}

	private static StringBuffer type2CIVL(String prefix, TypeNode type,
			boolean isTypeDeclaration) {
		StringBuffer result = new StringBuffer();
		TypeNodeKind kind = type.kind();

		switch (kind) {
		case ARRAY: {
			ArrayTypeNode arrayType = (ArrayTypeNode) type;
			ExpressionNode extent = arrayType.getExtent();

			// result.append("(");
			result.append(type2CIVL(prefix, arrayType.getElementType(),
					isTypeDeclaration));
			// result.append(")");
			result.append("[");
			if (extent != null)
				result.append(expression2CIVL(extent));
			result.append("]");
		}
			break;
		case DOMAIN: {
			DomainTypeNode domainType = (DomainTypeNode) type;
			ExpressionNode dim = domainType.getDimension();

			result.append("$domain");
			if (dim != null) {
				result.append("(");
				result.append(expression2CIVL(dim));
				result.append(")");
			}
			break;
		}
		case VOID:
			result.append("void");
			break;
		case BASIC:
			return basicType2CIVL((BasicTypeNode) type);
		case ENUMERATION:
			EnumerationTypeNode enumType = (EnumerationTypeNode) type;

			if (isTypeDeclaration)
				result.append(enumType2CIVL(prefix, enumType));
			else {
				result.append("enum");
				if (enumType.getIdentifier() != null)
					result.append(" " + enumType.getIdentifier().name());
			}
			break;
		case STRUCTURE_OR_UNION: {
			StructureOrUnionTypeNode strOrUnion = (StructureOrUnionTypeNode) type;

			if (isTypeDeclaration)
				result.append(structOrUnion2CIVL(prefix, strOrUnion));
			else {
				if (strOrUnion.isStruct())
					result.append("struct ");
				else
					result.append("union ");
				result.append(strOrUnion.getName());
			}
			break;
		}
		case POINTER:
			result.append(type2CIVL(prefix,
					((PointerTypeNode) type).referencedType(),
					isTypeDeclaration));
			result.append("*");
			break;
		case TYPEDEF_NAME:
			result.append(((TypedefNameNode) type).getName().name());
			break;
		case SCOPE:
			result.append("$scope");
			break;
		case FUNCTION: {
			FunctionTypeNode funcType = (FunctionTypeNode) type;
			SequenceNode<VariableDeclarationNode> paras = funcType
					.getParameters();
			int i = 0;

			result.append(" (");
			result.append(type2CIVL(prefix, funcType.getReturnType(), false));
			result.append(" (");
			for (VariableDeclarationNode para : paras) {
				if (i != 0)
					result.append(", ");
				result.append(variableDeclaration2CIVL("", para));
				i++;
			}
			result.append(")");
			result.append(")");
			break;
		}
		case RANGE:
			result.append("$range");
			break;
		default:
			throw new ABCUnsupportedException("translating type node of "
					+ kind + " kind into CIVL code");
		}
		return result;
	}

	private static StringBuffer basicType2CIVL(BasicTypeNode type) {
		StringBuffer result = new StringBuffer();
		BasicTypeKind basicKind = type.getBasicTypeKind();

		switch (basicKind) {
		case BOOL:
			result.append("_Bool");
			break;
		case CHAR:
			result.append("char");
			break;
		case DOUBLE:
			result.append("double");
			break;
		case DOUBLE_COMPLEX:
			result.append("double _Complex");
			break;
		case FLOAT:
			result.append("float");
			break;
		case FLOAT_COMPLEX:
			result.append("float _Complex");
			break;
		case INT:
			result.append("int");
			break;
		case LONG:
			result.append("long");
			break;
		case LONG_DOUBLE:
			result.append("long double");
			break;
		case LONG_DOUBLE_COMPLEX:
			result.append("long double _Complex");
			break;
		case LONG_LONG:
			result.append("long long");
			break;
		case REAL:
			result.append("real");
			break;
		case SHORT:
			result.append("short");
			break;
		case SIGNED_CHAR:
			result.append("signed char");
			break;
		case UNSIGNED:
			result.append("unsigned");
			break;
		case UNSIGNED_CHAR:
			result.append("unsigned char");
			break;
		case UNSIGNED_LONG:
			result.append("unsigned long");
			break;
		case UNSIGNED_LONG_LONG:
			result.append("unsigned long long");
			break;
		case UNSIGNED_SHORT:
			result.append("unsigned short");
			break;
		default:
			throw new ABCUnsupportedException("translating basic type node of "
					+ basicKind + " kind into CIVL code");
		}
		return result;
	}
}
