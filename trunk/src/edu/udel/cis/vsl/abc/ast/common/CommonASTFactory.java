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
import edu.udel.cis.vsl.abc.ast.node.IF.statement.AssertNode;
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

		switch (kind) {
		case DECLARATION_LIST:
			out.print(pPrintDeclarationList((DeclarationListNode) node));
			break;
		case ENUMERATOR_DECLARATION:
			out.print(pPrintEnumeratorDeclaration((EnumeratorDeclarationNode) node));
			break;
		case EXPRESSION:
			out.print(pPrintExpression((ExpressionNode) node));
			break;
		case FIELD_DECLARATION:
			out.print(pPrintfieldDeclaration("", (FieldDeclarationNode) node));
			break;
		case FUNCTION_DECLARATION:
			pPrintFunctionDeclaration(out, "", (FunctionDeclarationNode) node);
			break;
		case IDENTIFIER:
			out.print(((IdentifierNode) node).name());
			break;
		case OMP_NODE:
			pPrintOmpNode(out, "", (OmpNode) node);
			break;
		case OMP_REDUCTION_OPERATOR:
			out.print(pPrintOmpReduction((OmpReductionNode) node));
			break;
		case ORDINARY_LABEL:
		case SWITCH_LABEL:
			out.print(pPrintLabelNode((LabelNode) node));
			break;
		case PRAGMA:
			pPrintPragma(out, "", (PragmaNode) node);
			break;
		case STATEMENT:
			pPrintStatement(out, "", (StatementNode) node, true);
			break;
		case STATIC_ASSERTION:
			pPrintStaticAssertion(out, "", (StaticAssertionNode) node);
			break;
		case TYPE:
			out.print(pPrintType("", (TypeNode) node, true));
			break;
		case TYPEDEF:
			pPrintTypedefDeclaration(out, "", (TypedefDeclarationNode) node);
			break;
		case VARIABLE_DECLARATION:
			out.print(pPrintVariableDeclaration("",
					(VariableDeclarationNode) node));
			break;
		case PAIR:
		case SEQUENCE:
		default:
			throw new ABCUnsupportedException(
					"The pretty printing of AST node of " + kind
							+ " kind is not supported yet.", node.getSource()
							.getLocation(false));
		}
	}

	private static void pPrintOmpNode(PrintStream out, String prefix,
			OmpNode ompNode) {
		OmpNodeKind kind = ompNode.ompNodeKind();

		switch (kind) {
		case DECLARATIVE:
			pPrintOmpDeclarative(out, prefix, (OmpDeclarativeNode) ompNode);
		default:// EXECUTABLE
			pPrintOmpStatement(out, prefix, (OmpStatementNode) ompNode);
		}
	}

	static StringBuffer pPrintStructOrUnion(String prefix,
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
				result.append(pPrintfieldDeclaration(myIndent, field));
				result.append(";");
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private static StringBuffer pPrintfieldDeclaration(String prefix,
			FieldDeclarationNode field) {
		String type;
		StringBuffer result = new StringBuffer();

		type = pPrintType(prefix, field.getTypeNode(), true).toString();
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

	static void pPrintStaticAssertion(PrintStream out, String prefix,
			StaticAssertionNode assertion) {
		out.print(prefix);
		out.print("(");
		out.print(pPrintExpression(assertion.getExpression()));
		out.print(", \"");
		out.print(assertion.getMessage().getStringRepresentation());
		out.print("\")");
	}

	static void pPrintPragma(PrintStream out, String prefix, PragmaNode pragma) {
		Iterator<CToken> tokens = pragma.getTokens();

		out.print(prefix);
		out.print("#pragma ");
		out.print(pragma.getPragmaIdentifier().name());

		while (tokens.hasNext()) {
			CToken token = tokens.next();

			out.print(" ");
			out.print(token.getText());
		}
	}

	static void pPrintOrdinaryDeclaration(PrintStream out, String prefix,
			OrdinaryDeclarationNode declaration) {
		OrdinaryDeclarationKind kind = declaration.ordinaryDeclarationKind();

		switch (kind) {
		case VARIABLE_DECLARATION:
			out.print(pPrintVariableDeclaration(prefix,
					(VariableDeclarationNode) declaration));
			out.print(";");
			break;
		default: // cases FUNCTION_DECLARATION, FUNCTION_DEFINITION,
					// ABSTRACT_FUNCTION_DEFINITION:
			pPrintFunctionDeclaration(out, prefix,
					(FunctionDeclarationNode) declaration);
		}
	}

	static StringBuffer pPrintEnumType(String prefix,
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
				result.append(pPrintEnumeratorDeclaration(enumerator));
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private static StringBuffer pPrintEnumeratorDeclaration(
			EnumeratorDeclarationNode enumerator) {
		StringBuffer result = new StringBuffer();

		result.append(enumerator.getName());
		if (enumerator.getValue() != null) {
			result.append("=");
			result.append(pPrintExpression(enumerator.getValue()));
		}
		return result;
	}

	static void pPrintOmpDeclarative(PrintStream out, String prefix,
			OmpDeclarativeNode ompDeclarative) {
		OmpDeclarativeNodeKind kind = ompDeclarative.ompDeclarativeNodeKind();

		out.print("#pragma omp ");
		switch (kind) {
		case REDUCTION:
			out.print("reduction");
			break;
		case THREADPRIVATE:
			out.print("threadprivate");
			break;
		default:
			throw new ABCUnsupportedException(
					"The OpenMP declarative directive " + kind
							+ " is not supported yet.", ompDeclarative
							.getSource().getLocation(false));
		}
		out.print("(");
		out.print(pPrintSequenceExpression(ompDeclarative.variables()));
		out.print(")");
	}

	private static void pPrintFunctionDeclaration(PrintStream out,
			String prefix, FunctionDeclarationNode function) {
		FunctionTypeNode typeNode = function.getTypeNode();
		TypeNode returnType = typeNode.getReturnType();
		SequenceNode<VariableDeclarationNode> paras = typeNode.getParameters();
		int numOfParas = paras.numChildren();

		if (function instanceof AbstractFunctionDefinitionNode)
			out.print("$abstract ");
		out.print(prefix);
		out.print(pPrintType(prefix, returnType, false));
		out.print(" ");
		out.print(function.getName());
		out.print("(");
		for (int i = 0; i < numOfParas; i++) {
			if (i != 0)
				out.print(", ");
			out.print(pPrintVariableDeclaration("", paras.getSequenceChild(i)));
		}
		out.print(")");

		if (function instanceof FunctionDefinitionNode) {
			CompoundStatementNode body = ((FunctionDefinitionNode) function)
					.getBody();

			out.print("\n");
			pPrintCompoundStatement(out, prefix + indention, body, true);
		} else
			out.print(";");
	}

	private static void pPrintCompoundStatement(PrintStream out, String prefix,
			CompoundStatementNode compound, boolean isBody) {
		int numChildren = compound.numChildren();
		String myIndent = prefix;
		String myPrefix = prefix;

		if (isBody) {
			myPrefix = prefix.substring(0, prefix.length() - 2);
			out.print(myPrefix);
		} else {
			out.print(myPrefix);
			myIndent = prefix + indention;
		}
		out.print("{\n");
		for (int i = 0; i < numChildren; i++) {
			BlockItemNode child = compound.getSequenceChild(i);

			if (child != null) {
				pPrintBlockItem(out, myIndent, child);
				out.print("\n");
			}
		}
		out.print(myPrefix);
		out.print("}");
	}

	private static void pPrintBlockItem(PrintStream out, String prefix,
			BlockItemNode block) {
		BlockItemKind kind = block.blockItemKind();

		switch (kind) {
		case STATEMENT:
			pPrintStatement(out, prefix, (StatementNode) block, false);
			break;
		case ORDINARY_DECLARATION:
			if (block instanceof VariableDeclarationNode) {
				out.print(pPrintVariableDeclaration(prefix,
						(VariableDeclarationNode) block));

				out.print(";");
				;
			} else if (block instanceof FunctionDeclarationNode)
				pPrintFunctionDeclaration(out, prefix,
						(FunctionDeclarationNode) block);
			break;
		case TYPEDEF:
			pPrintTypedefDeclaration(out, prefix,
					(TypedefDeclarationNode) block);
			break;
		case ENUMERATOR:
			out.print(prefix + "enum;");
			break;
		case PRAGMA:
			pPrintPragma(out, prefix, (PragmaNode) block);
			break;
		case STRUCT_OR_UNION:
			out.print(pPrintStructOrUnion(prefix,
					(StructureOrUnionTypeNode) block));
			break;
		default:
			throw new ABCUnsupportedException(
					"pretty print of block item node of " + kind + " kind");
		}
	}

	static void pPrintTypedefDeclaration(PrintStream out, String prefix,
			TypedefDeclarationNode typedef) {

		out.print(prefix);
		out.print("typedef ");
		out.print(" ");
		out.print(pPrintType(prefix, typedef.getTypeNode(), true));
		out.print(" ");
		out.print(typedef.getName());
	}

	private static void pPrintStatement(PrintStream out, String prefix,
			StatementNode statement, boolean isBody) {
		StatementKind kind = statement.statementKind();

		switch (kind) {
		case ASSUME:
			pPrintAssume(out, prefix, (AssumeNode) statement);
			break;
		case ASSERT:
			pPrintAssert(out, prefix, (AssertNode) statement);
			break;
		case ATOMIC:
			pPrintAtomic(out, prefix, (AtomicNode) statement);
			break;
		case COMPOUND:
			pPrintCompoundStatement(out, prefix,
					(CompoundStatementNode) statement, isBody);
			break;
		case EXPRESSION:
			expressionStatement2CIVL(out, prefix,
					(ExpressionStatementNode) statement);
			break;
		case CIVL_FOR:
			pPrintCivlForStatement(out, prefix, (CivlForNode) statement);
			break;
		case IF:
			pPrintIf(out, prefix, (IfNode) statement);
			break;
		case JUMP:
			pPrintJump(out, prefix, (JumpNode) statement);
			break;
		case LABELED:
			pPrintLabeled(out, prefix, (LabeledStatementNode) statement);
			break;
		case LOOP:
			pPrintLoop(out, prefix, (LoopNode) statement);
			break;
		case NULL:
			out.print(prefix);
			out.print(";");
			break;
		case OMP_STATEMENT:
			pPrintOmpStatement(out, prefix, (OmpStatementNode) statement);
			break;
		case SWITCH:
			pPrintSwitch(out, prefix, (SwitchNode) statement);
			break;
		case WHEN:
			pPrintWhen(out, prefix, (WhenNode) statement);
			break;
		default:
			throw new ABCUnsupportedException(
					"pretty print of statement node of " + kind + " kind");
		}
	}

	static void pPrintAssert(PrintStream out, String prefix,
			AssertNode assertNode) {
		SequenceNode<ExpressionNode> explanation = assertNode.getExplanation();

		out.print(prefix);
		out.print("$assert ");
		out.print(pPrintExpression(assertNode.getCondition()));
		if (explanation != null) {
			int numArgs = explanation.numChildren();

			out.print(" : ");
			for (int i = 0; i < numArgs; i++) {
				if (i != 0)
					out.print(", ");
				out.print(pPrintExpression(explanation.getSequenceChild(i)));
			}
		}
		out.print(";");
	}

	private static void pPrintOmpStatement(PrintStream out, String prefix,
			OmpStatementNode ompStmt) {
		OmpStatementNodeKind kind = ompStmt.ompStatementNodeKind();
		SequenceNode<IdentifierExpressionNode> privateList = ompStmt
				.privateList(), firstPrivateList = ompStmt.firstprivateList(), sharedList = ompStmt
				.sharedList(), copyinList = ompStmt.copyinList(), copyPrivateList = ompStmt
				.copyprivateList(), lastPrivateList = ompStmt.lastprivateList();
		SequenceNode<OmpReductionNode> reductionList = ompStmt.reductionList();
		boolean nowait = ompStmt.nowait();
		String myIndent = prefix + indention;
		StatementNode block = ompStmt.statementNode();

		out.print(prefix);
		out.print("#pragma omp ");
		switch (kind) {
		case PARALLEL:
			pPrintOmpParallel(out, prefix, (OmpParallelNode) ompStmt);
			break;
		case SYNCHRONIZATION:
			pPrintOmpSync(out, prefix, (OmpSyncNode) ompStmt);
			break;
		default: // case WORKSHARING:
			pPrintOmpWorksharing(out, prefix, (OmpWorksharingNode) ompStmt);
			break;
		}
		if (nowait)
			out.print("nowait");
		if (privateList != null) {
			out.print("private(");
			out.print(pPrintSequenceExpression(privateList));
			out.print(") ");
		}
		if (firstPrivateList != null) {
			out.print("firstprivate(");
			out.print(pPrintSequenceExpression(firstPrivateList));
			out.print(") ");
		}
		if (sharedList != null) {
			out.print("shared(");
			out.print(pPrintSequenceExpression(sharedList));
			out.print(") ");
		}
		if (copyinList != null) {
			out.print("copyin(");
			out.print(pPrintSequenceExpression(copyinList));
			out.print(") ");
		}
		if (copyPrivateList != null) {
			out.print("copyprivate(");
			out.print(pPrintSequenceExpression(copyPrivateList));
			out.print(") ");
		}
		if (lastPrivateList != null) {
			out.print("lastprivate(");
			out.print(pPrintSequenceExpression(lastPrivateList));
			out.print(") ");
		}
		if (reductionList != null) {
			out.print(pPrintSequenceReduction(reductionList));
		}
		out.print("\n");
		if (block != null)
			pPrintStatement(out, myIndent, block, true);
	}

	private static void pPrintOmpWorksharing(PrintStream out, String prefix,
			OmpWorksharingNode ompWs) {
		OmpWorksharingNodeKind kind = ompWs.ompWorkshareNodeKind();

		switch (kind) {
		case FOR: {
			OmpForNode forNode = (OmpForNode) ompWs;
			int collapse = forNode.collapse();

			out.print("for schedule(");
			switch (forNode.schedule()) {
			case AUTO:
				out.print("auto");
				break;
			case DYNAMIC:
				out.print("dynamic");
				break;
			case GUIDED:
				out.print("guided");
				break;
			case RUNTIME:
				out.print("runtime");
				break;
			default:// STATIC
				out.print("static");
				break;
			}
			if (forNode.chunkSize() != null) {
				out.print(", ");
				out.print(pPrintExpression(forNode.chunkSize()));
			}
			out.print(") ");
			if (collapse > 1) {
				out.print("collapse(");
				out.print(collapse);
				out.print(")");
			}
			if (forNode.ordered())
				out.print("ordered");
			break;
		}
		case SECTIONS:
			out.print("sections ");
			break;
		case SINGLE:
			out.print("single ");
			break;
		default: // case SECTION:
			out.print("section ");
		}
	}

	private static void pPrintOmpSync(PrintStream out, String prefix,
			OmpSyncNode ompSync) {
		OmpSyncNodeKind kind = ompSync.ompSyncNodeKind();

		switch (kind) {
		case MASTER:
			out.print("master ");
			break;
		case CRITICAL:
			out.print("critical ");
			if (ompSync.criticalName() != null) {
				out.print("(");
				out.print(ompSync.criticalName().name());
				out.print(")");
			}
			break;
		case BARRIER:
			out.print("barrier ");
			break;
		case FLUSH:
			out.print("flush ");
			if (ompSync.flushedList() != null) {
				out.print("(");
				out.print(pPrintSequenceExpression(ompSync.flushedList()));
				out.print(")");
			}
			break;
		default:// ORDERED
			out.print("ordered ");
		}
	}

	private static void pPrintOmpParallel(PrintStream out, String prefix,
			OmpParallelNode para) {
		ExpressionNode ifClause = para.ifClause(), numThreads = para
				.numThreads();
		boolean isDefaultShared = para.isDefaultShared();

		out.print("parallel ");
		if (ifClause != null) {
			out.print("if(");
			out.print(pPrintExpression(ifClause));
			out.print(") ");
		}
		if (numThreads != null) {
			out.print("num_threads(");
			out.print(pPrintExpression(numThreads));
			out.print(") ");
		}
		if (isDefaultShared)
			out.print("default(shared) ");
		else
			out.print("default(none) ");
	}

	private static StringBuffer pPrintSequenceReduction(
			SequenceNode<OmpReductionNode> sequence) {
		StringBuffer result = new StringBuffer();
		int num = sequence.numChildren();

		for (int i = 0; i < num; i++) {
			OmpReductionNode reduction = sequence.getSequenceChild(i);

			result.append(pPrintOmpReduction(reduction));
			if (i < num - 1)
				result.append(" ");
		}
		return result;
	}

	private static StringBuffer pPrintOmpReduction(OmpReductionNode reduction) {
		StringBuffer result = new StringBuffer();

		result.append("reduction(");
		switch (reduction.ompReductionOperatorNodeKind()) {
		case FUNCTION: {
			OmpFunctionReductionNode funcNode = (OmpFunctionReductionNode) reduction;

			result.append(pPrintExpression(funcNode.function()));
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
		result.append(pPrintSequenceExpression(reduction.variables()));
		result.append(")");
		return result;
	}

	private static StringBuffer pPrintSequenceExpression(
			SequenceNode<IdentifierExpressionNode> sequence) {
		StringBuffer result = new StringBuffer();
		int numExpressions = sequence.numChildren();

		for (int i = 0; i < numExpressions; i++) {
			IdentifierExpressionNode expression = sequence.getSequenceChild(i);

			if (i != 0)
				result.append(", ");
			result.append(pPrintExpression(expression));
		}
		return result;
	}

	private static void pPrintCivlForStatement(PrintStream out, String prefix,
			CivlForNode civlFor) {
		civlFor.getDomain();
		// civlFor.

		// TODO Auto-generated method stub
	}

	private static void pPrintLoop(PrintStream out, String prefix, LoopNode loop) {
		LoopKind loopKind = loop.getKind();
		StringBuffer condition = new StringBuffer();
		String myIndent = prefix + indention;
		StatementNode bodyNode = loop.getBody();

		if (loop.getCondition() != null)
			condition = pPrintExpression(loop.getCondition());
		out.print(prefix);
		switch (loopKind) {
		case WHILE:
			out.print("while(");
			out.print(condition);
			out.print(")");
			if (bodyNode == null)
				out.print(";");
			else {
				out.print("\n");
				pPrintStatement(out, myIndent, bodyNode, true);

			}
			break;
		case DO_WHILE:
			out.print("do");
			if (bodyNode == null)
				out.print(";");
			else {
				out.print("\n");
				pPrintStatement(out, myIndent, bodyNode, true);
			}
			out.print("while(");
			out.print(condition);
			out.print(");");
			break;
		default: // case FOR:
			pPrintFor(out, prefix, (ForLoopNode) loop);
		}
	}

	private static void pPrintAtomic(PrintStream out, String prefix,
			AtomicNode atomicNode) {
		out.print(prefix);
		if (atomicNode.isAtom())
			out.print("$atom\n");
		else
			out.print("$atomic\n");
		pPrintStatement(out, prefix + indention, atomicNode.getBody(), true);
	}

	private static void pPrintGoto(PrintStream out, String prefix, GotoNode go2) {
		out.print(prefix);
		out.print("goto ");
		out.print(go2.getLabel().name());
		out.print(";");
	}

	private static void pPrintLabeled(PrintStream out, String prefix,
			LabeledStatementNode labeled) {
		LabelNode label = labeled.getLabel();
		StatementNode statement = labeled.getStatement();
		String myIndent = prefix + indention;

		out.print(prefix);
		out.print(pPrintLabelNode(label));
		out.print("\n");
		pPrintStatement(out, myIndent, statement, false);
	}

	private static StringBuffer pPrintLabelNode(LabelNode label) {
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
				result.append(pPrintExpression(switchLabel.getExpression()));
				result.append(":");
			}
		}
		return result;
	}

	private static void pPrintSwitch(PrintStream out, String prefix,
			SwitchNode swtichNode) {
		// return new StringBuffer("switch");
		// TODO throw new CIVLUnimplementedFeatureException(
		// "translating switch node into CIVL code",
		// swtichNode.getSource());
	}

	private static void pPrintJump(PrintStream out, String prefix, JumpNode jump) {
		JumpKind kind = jump.getKind();

		switch (kind) {
		case GOTO:
			pPrintGoto(out, prefix, (GotoNode) jump);
			break;
		case CONTINUE:
			out.print("continue;");
			break;
		case BREAK:
			out.print("break;");
			break;
		default: // case RETURN:
			pPrintReturn(out, prefix, (ReturnNode) jump);
		}
	}

	private static void pPrintReturn(PrintStream out, String prefix,
			ReturnNode returnNode) {
		ExpressionNode expr = returnNode.getExpression();

		out.print(prefix);
		out.print("return");
		if (expr != null) {
			out.print(" ");
			out.print(pPrintExpression(expr));
		}
		out.print(";");
	}

	private static void pPrintIf(PrintStream out, String prefix, IfNode ifNode) {
		ExpressionNode condition = ifNode.getCondition();
		StatementNode trueBranch = ifNode.getTrueBranch();
		StatementNode falseBranch = ifNode.getFalseBranch();
		String myIndent = prefix + indention;

		out.print(prefix);
		out.print("if(");
		if (condition != null)
			out.print(pPrintExpression(condition));
		out.print(")");
		if (trueBranch == null)
			out.print(";");
		else {
			out.print("\n");
			pPrintStatement(out, myIndent, trueBranch, true);
		}
		if (falseBranch != null) {
			out.print("\n");
			out.print(prefix);
			out.print("else\n");
			pPrintStatement(out, myIndent, falseBranch, true);
		}
	}

	private static void pPrintFor(PrintStream out, String prefix,
			ForLoopNode loop) {
		ForLoopInitializerNode init = loop.getInitializer();
		ExpressionNode condition = loop.getCondition();
		ExpressionNode incrementer = loop.getIncrementer();
		StatementNode body = loop.getBody();
		String myIndent = prefix + indention;

		out.print(prefix);
		out.print("for(");
		if (init != null) {
			if (init instanceof ExpressionNode)
				out.print(pPrintExpression((ExpressionNode) init));
			else if (init instanceof DeclarationListNode)
				out.print(pPrintDeclarationList((DeclarationListNode) init));
		}
		out.print("; ");
		if (condition != null) {
			out.print(pPrintExpression(condition));
		}
		out.print("; ");
		if (incrementer != null) {
			out.print(pPrintExpression(incrementer));
		}
		out.print(")");
		if (body == null)
			out.print(";");
		else {
			out.print("\n");
			pPrintStatement(out, myIndent, body, true);
		}
	}

	private static StringBuffer pPrintDeclarationList(DeclarationListNode list) {
		int num = list.numChildren();
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < num; i++) {
			VariableDeclarationNode var = list.getSequenceChild(i);

			if (i != 0)
				result.append(", ");
			result.append(pPrintVariableDeclaration("", var));
		}
		return result;
	}

	private static void expressionStatement2CIVL(PrintStream out,
			String prefix, ExpressionStatementNode expr) {
		out.print(prefix);
		out.print(pPrintExpression(expr.getExpression()));
		out.print(";");
	}

	private static void pPrintWhen(PrintStream out, String prefix, WhenNode when) {
		String myIndent = prefix + indention;

		out.print(prefix);
		out.print("$when(");
		out.print(pPrintExpression(when.getGuard()));
		out.print(")\n");
		pPrintStatement(out, myIndent, when.getBody(), true);
	}

	static private StringBuffer pPrintVariableDeclaration(String prefix,
			VariableDeclarationNode variable) {
		StringBuffer result = new StringBuffer();
		InitializerNode init = variable.getInitializer();
		TypeNode typeNode = variable.getTypeNode();
		String type;
		IdentifierNode varName = variable.getIdentifier();

		result.append(prefix);
		if (typeNode.isInputQualified())
			result.append("$input ");
		if (typeNode.isOutputQualified())
			result.append("$output ");
		type = pPrintType(prefix, typeNode, false).toString();
		if (type.endsWith("]")) {
			int start = type.indexOf('[');
			String suffix = type.substring(start);

			type = type.substring(0, start);
			result.append(type);
			result.append(" ");
			if (varName != null) {
				result.append(" ");
				result.append(variable.getName());
			}
			result.append(suffix);
		} else {
			result.append(type);
			if (varName != null) {
				result.append(" ");
				result.append(variable.getName());
			}
		}
		if (init != null) {
			result.append(" = ");
			result.append(pPrintInitializer(init));
		}
		return result;
	}

	private static StringBuffer pPrintInitializer(InitializerNode init) {
		if (init instanceof CompoundInitializerNode) {
			CompoundInitializerNode compoundInit = (CompoundInitializerNode) init;

			return pPrintCompoundLiteralObject(init.getSource(),
					compoundInit.getType(), compoundInit.getLiteralObject());
		} else if (init instanceof ExpressionNode)
			return pPrintExpression((ExpressionNode) init);
		else
			throw new ABCRuntimeException("Invalid initializer: "
					+ init.toString());
	}

	private static StringBuffer pPrintCompoundLiteralObject(Source source,
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
				result.append(pPrintLiteralObject(source, elementType,
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
				result.append(pPrintLiteralObject(source, field.getType(),
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

	private static StringBuffer pPrintLiteralObject(Source source, Type type,
			LiteralObject obj) {
		if (obj instanceof CompoundLiteralObject)
			return pPrintCompoundLiteralObject(source, type,
					(CompoundLiteralObject) obj);
		else if (obj instanceof ScalarLiteralObject)
			return pPrintExpression(((ScalarLiteralObject) obj).getExpression());
		else
			throw new ABCRuntimeException("Invalid literal object: " + obj);
	}

	static void pPrintAssume(PrintStream out, String prefix, AssumeNode assume) {
		out.print(prefix);
		out.print("$assume ");
		out.print(pPrintExpression(assume.getExpression()));
		out.print(";");
	}

	private static StringBuffer pPrintExpression(ExpressionNode expression) {
		StringBuffer result = new StringBuffer();
		ExpressionKind kind = expression.expressionKind();

		switch (kind) {
		case ARROW: {
			ArrowNode arrow = (ArrowNode) expression;

			result.append(pPrintExpression(arrow.getStructurePointer()));
			result.append("->");
			result.append(arrow.getFieldName().name());
			break;
		}
		case CAST: {
			CastNode cast = (CastNode) expression;

			result.append("(");
			result.append(pPrintType("", cast.getCastType(), false));
			result.append(")");
			result.append(pPrintExpression(cast.getArgument()));
			break;
		}
		case COMPOUND_LITERAL:
			result.append(pPrintCompoundLiteral((CompoundLiteralNode) expression));
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

			result.append(pPrintExpression(dot.getStructure()));
			result.append(".");
			result.append(dot.getFieldName().name());
			break;
		}
		case FUNCTION_CALL:
			return pPrintFunctionCall((FunctionCallNode) expression);
		case IDENTIFIER_EXPRESSION:
			result.append(((IdentifierExpressionNode) expression)
					.getIdentifier().name());
			break;
		case OPERATOR:
			result = pPrintOperator((OperatorNode) expression);
			break;
		case SIZEOF:
			result.append("sizeof(");
			result.append(pPrintSizeable(((SizeofNode) expression)
					.getArgument()));
			break;
		case SPAWN:
			result.append("$spawn ");
			result.append(pPrintFunctionCall(((SpawnNode) expression).getCall()));
			break;
		case REGULAR_RANGE:
			result.append(pPrintRegularRange((RegularRangeNode) expression));
			break;
		default:
			throw new ABCUnsupportedException(
					"pretty print of expression node of " + kind + " kind");
		}
		return result;
	}

	private static StringBuffer pPrintCompoundLiteral(
			CompoundLiteralNode expression) {
		StringBuffer result = new StringBuffer();

		// TODO
		return result;
	}

	private static StringBuffer pPrintRegularRange(RegularRangeNode range) {
		StringBuffer result = new StringBuffer();
		ExpressionNode step = range.getStep();

		result.append(pPrintExpression(range.getLow()));
		result.append(" .. ");
		result.append(pPrintExpression(range.getHigh()));
		if (step != null) {
			result.append(" # ");
			result.append(pPrintExpression(step));
		}
		return result;
	}

	private static StringBuffer pPrintSizeable(SizeableNode argument) {
		if (argument instanceof ExpressionNode)
			return pPrintExpression((ExpressionNode) argument);
		return pPrintType("", (TypeNode) argument, false);
	}

	private static StringBuffer pPrintFunctionCall(FunctionCallNode call) {
		int argNum = call.getNumberOfArguments();
		StringBuffer result = new StringBuffer();

		result.append(pPrintExpression(call.getFunction()));
		result.append("(");
		for (int i = 0; i < argNum; i++) {
			if (i > 0)
				result.append(", ");
			result.append(pPrintExpression(call.getArgument(i)));
		}
		result.append(")");
		return result;
	}

	private static StringBuffer pPrintOperator(OperatorNode operator) {
		StringBuffer result = new StringBuffer();
		Operator op = operator.getOperator();
		StringBuffer arg0 = pPrintExpression(operator.getArgument(0));
		StringBuffer arg1 = operator.getNumberOfArguments() > 1 ? pPrintExpression(operator
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
		case COMMA:
			result.append(arg0);
			result.append(", ");
			result.append(arg1);
			break;
		case CONDITIONAL:
			result.append(arg0);
			result.append(" ? ");
			result.append(arg1);
			result.append(" : ");
			result.append(pPrintExpression(operator.getArgument(2)));
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
			throw new ABCUnsupportedException(
					"pretty print of operator node of " + op + " kind");
		}
		return result;
	}

	private static StringBuffer pPrintType(String prefix, TypeNode type,
			boolean isTypeDeclaration) {
		StringBuffer result = new StringBuffer();
		TypeNodeKind kind = type.kind();

		switch (kind) {
		case ARRAY: {
			ArrayTypeNode arrayType = (ArrayTypeNode) type;
			ExpressionNode extent = arrayType.getExtent();

			// result.append("(");
			result.append(pPrintType(prefix, arrayType.getElementType(),
					isTypeDeclaration));
			// result.append(")");
			result.append("[");
			if (extent != null)
				result.append(pPrintExpression(extent));
			result.append("]");
		}
			break;
		case DOMAIN: {
			DomainTypeNode domainType = (DomainTypeNode) type;
			ExpressionNode dim = domainType.getDimension();

			result.append("$domain");
			if (dim != null) {
				result.append("(");
				result.append(pPrintExpression(dim));
				result.append(")");
			}
			break;
		}
		case VOID:
			result.append("void");
			break;
		case BASIC:
			return pPrintBasicType((BasicTypeNode) type);
		case ENUMERATION:
			EnumerationTypeNode enumType = (EnumerationTypeNode) type;

			if (isTypeDeclaration)
				result.append(pPrintEnumType(prefix, enumType));
			else {
				result.append("enum");
				if (enumType.getIdentifier() != null)
					result.append(" " + enumType.getIdentifier().name());
			}
			break;
		case STRUCTURE_OR_UNION: {
			StructureOrUnionTypeNode strOrUnion = (StructureOrUnionTypeNode) type;

			if (isTypeDeclaration)
				result.append(pPrintStructOrUnion(prefix, strOrUnion));
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
			result.append(pPrintType(prefix,
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
			result.append(pPrintType(prefix, funcType.getReturnType(), false));
			result.append(" (");
			for (VariableDeclarationNode para : paras) {
				if (i != 0)
					result.append(", ");
				result.append(pPrintVariableDeclaration("", para));
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
			throw new ABCUnsupportedException("pretty print of type node of "
					+ kind + " kind");
		}
		return result;
	}

	private static StringBuffer pPrintBasicType(BasicTypeNode type) {
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
			throw new ABCUnsupportedException(
					"pretty print of basic type node of " + basicKind + " kind");
		}
		return result;
	}
}
