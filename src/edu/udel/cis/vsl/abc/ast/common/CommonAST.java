package edu.udel.cis.vsl.abc.ast.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.IF.ASTException;
import edu.udel.cis.vsl.abc.ast.IF.ASTFactory;
import edu.udel.cis.vsl.abc.ast.entity.IF.Entity.LinkageKind;
import edu.udel.cis.vsl.abc.ast.entity.IF.Field;
import edu.udel.cis.vsl.abc.ast.entity.IF.OrdinaryEntity;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.ExternalDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
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
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.err.IF.ABCUnsupportedException;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Standard implementation of the {@link AST} interface.
 * 
 * @author siegel
 * 
 */
public class CommonAST implements AST {

	private final static String indention = "  ";

	private ASTFactory astFactory;

	private SequenceNode<ExternalDefinitionNode> root;

	private int nodeCount;

	private ASTNode[] nodes;

	private Map<String, OrdinaryEntity> internalOrExternalEntityMap = new LinkedHashMap<String, OrdinaryEntity>();

	private ArrayList<OrdinaryEntity> internalEntities = new ArrayList<OrdinaryEntity>();

	private ArrayList<OrdinaryEntity> externalEntities = new ArrayList<OrdinaryEntity>();

	public CommonAST(ASTFactory astFactory,
			SequenceNode<ExternalDefinitionNode> root, boolean hasOmpPragma)
			throws SyntaxException {
		this.root = root;
		this.astFactory = astFactory;
		initialize();
	}

	@Override
	public ASTFactory getASTFactory() {
		return astFactory;
	}

	@Override
	public SequenceNode<ExternalDefinitionNode> getRootNode() {
		return root;
	}

	@Override
	public int getNumberOfNodes() {
		return nodeCount;
	}

	@Override
	public ASTNode getNode(int id) {
		return nodes[id];
	}

	@Override
	public void print(PrintStream out) {
		print("", out, root, true);
	}

	private void print(String prefix, PrintStream out, ASTNode node,
			boolean includeSource) {
		if (node == null) {
			out.println(prefix + "<absent>");
		} else {
			Iterable<ASTNode> children = node.children();
			int childCount = 0;

			node.print(prefix, out, includeSource);
			out.println();
			prefix += "| ";
			// out.println(prefix + node.getScope());
			for (ASTNode child : children) {
				if (child == null)
					out.println(prefix + childCount + " <absent>");
				else
					print(prefix, out, child, includeSource);
				childCount++;
			}
		}
	}

	@Override
	public void release() {
		nullifyOwners(root);
		externalEntities = null;
		internalEntities = null;
		internalOrExternalEntityMap = null;
		nodeCount = 0;
		nodes = null;
		root = null;
		astFactory = null;
	}

	private void nullifyOwners(ASTNode node) {
		if (node == null)
			return;
		else {
			Iterable<ASTNode> children = node.children();

			node.setOwner(null);
			node.setId(-1);
			for (ASTNode child : children)
				nullifyOwners(child);
		}
	}

	// supporting methods...

	private void initialize() throws SyntaxException {
		this.nodeCount = 0;
		setIDsAndOwner(root);
		this.nodes = new ASTNode[nodeCount];
		initializeNodeArray(root);
		// ScopeAnalyzer.setScopes(this);
	}

	private void setIDsAndOwner(ASTNode node) {
		Iterable<ASTNode> children;

		if (node == null)
			return;
		if (node.getOwner() != null) {
			throw new ASTException(
					"Node cannot be added to new AST until old AST is released:\n"
							+ node);
		}
		node.setId(nodeCount);
		node.setOwner(this);
		nodeCount++;
		children = node.children();
		for (ASTNode child : children) {
			setIDsAndOwner(child);
		}
	}

	private void initializeNodeArray(ASTNode node) {
		Iterable<ASTNode> children;

		if (node == null)
			return;
		this.nodes[node.id()] = node;
		children = node.children();
		for (ASTNode child : children) {
			initializeNodeArray(child);
		}
	}

	@Override
	public void add(OrdinaryEntity entity) {
		LinkageKind linkage = entity.getLinkage();

		if (linkage == LinkageKind.EXTERNAL)
			externalEntities.add(entity);
		else if (linkage == LinkageKind.INTERNAL)
			internalEntities.add(entity);
		else
			throw new IllegalArgumentException(
					"Can only add entities with internal or external linkage to translation unit: "
							+ entity);
		internalOrExternalEntityMap.put(entity.getName(), entity);
	}

	@Override
	public OrdinaryEntity getInternalOrExternalEntity(String name) {
		return internalOrExternalEntityMap.get(name);
	}

	@Override
	public Iterator<OrdinaryEntity> getInternalEntities() {
		return internalEntities.iterator();
	}

	@Override
	public Iterator<OrdinaryEntity> getExternalEntities() {
		return externalEntities.iterator();
	}

	@Override
	public void prettyPrint(PrintStream out, boolean ignoreStdLibs) {
		Map<String, StringBuffer> results = new LinkedHashMap<>();
		Set<String> headers = new LinkedHashSet<>();
		SequenceNode<ExternalDefinitionNode> root = getRootNode();
		int numChildren = root.numChildren();

		for (int i = 0; i < numChildren; i++) {
			ExternalDefinitionNode child = root.getSequenceChild(i);

			if (child != null)
				this.externalDef2CIVL(child, results, headers, ignoreStdLibs);
		}
		for (Entry<String, StringBuffer> entry : results.entrySet()) {
			out.print("================ ");
			out.print(entry.getKey());
			out.println(" ================");
			for (String header : headers) {
				out.print("#include <");
				out.print(header);
				out.println(">");
			}
			out.print(entry.getValue());
		}
	}

	private void externalDef2CIVL(ExternalDefinitionNode extern,
			Map<String, StringBuffer> results, Set<String> headers,
			boolean ignoreStdLibs) {
		String sourceFile = extern.getSource().getFirstToken().getSourceFile()
				.getName();
		StringBuffer myBuffer;

		if (ignoreStdLibs)
			switch (sourceFile) {
			case "assert.h":
			case "civlc.h":
			case "civlc.cvh":
			case "bundle.cvh":
			case "comm.cvh":
			case "concurrency.cvh":
			case "pointer.cvh":
			case "scope.cvh":
			case "seq.cvh":
			case "float.h":
			case "math.h":
			case "mpi.h":
			case "omp.h":
			case "pthread.h":
			case "stdbool.h":
			case "stddef.h":
			case "stdio.h":
			case "stdlib.h":
			case "string.h":
			case "time.h":
				headers.add(sourceFile);
				return;
			case "civlc-common.cvh":
			case "bundle-common.cvh":
			case "comm-common.cvh":
			case "concurrency-common.cvh":
			case "omp.cvl":
			case "pointer-common.cvh":
			case "scope-common.cvh":
			case "seq-common.cvh":
			case "stdlib-common.h":
			case "string-common.h":
			case "stdio-common.h":
			case "omp-common.h":
			case "mpi-common.h":
			case "civlc-common.h":
			case "civlc-omp.cvl":
			case "stdio-c.cvl":
			case "stdio.cvl":
			case "mpi.cvl":
			case "pthread-c.cvl":
			case "pthread.cvl":
			case "time-common.h":
			case "math.cvl":
				return;
			default:
			}
		if (!results.containsKey(sourceFile))
			results.put(sourceFile, new StringBuffer());
		myBuffer = results.get(sourceFile);
		if (extern instanceof AssumeNode) {
			myBuffer.append(assume2CIVL("", (AssumeNode) extern));
			myBuffer.append(";");
		} else if (extern instanceof EnumerationTypeNode) {
			myBuffer.append(enumType2CIVL("", (EnumerationTypeNode) extern));
			myBuffer.append(";");
		} else if (extern instanceof OrdinaryDeclarationNode) {
			int length;
			char lastChar;

			myBuffer.append(ordinaryDeclaration2CIVL("",
					(OrdinaryDeclarationNode) extern));
			length = myBuffer.length();
			lastChar = myBuffer.charAt(length - 1);
			if (lastChar != '}' && lastChar != ';')
				myBuffer.append(";");
		} else if (extern instanceof PragmaNode) {
			myBuffer.append(pragma2CIVL("", (PragmaNode) extern));
		} else if (extern instanceof StaticAssertionNode) {
			myBuffer.append(staticAssertion2CIVL("",
					(StaticAssertionNode) extern));
			myBuffer.append(";");
		} else if (extern instanceof StructureOrUnionTypeNode) {
			myBuffer.append(structOrUnion2CIVL("",
					(StructureOrUnionTypeNode) extern));
			myBuffer.append(";");
		} else if (extern instanceof TypedefDeclarationNode) {
			myBuffer.append(typedefDeclaration2CIVL("",
					(TypedefDeclarationNode) extern));
			myBuffer.append(";");
		} else if (extern instanceof OmpDeclarativeNode) {
			myBuffer.append(ompDeclarative2CIVL("", (OmpDeclarativeNode) extern));
		}
		myBuffer.append("\n");
	}

	private StringBuffer structOrUnion2CIVL(String prefix,
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
				result.append(this.type2CIVL(myIndent, field.getTypeNode(),
						true));
				result.append(" ");
				result.append(field.getName());
				result.append(";");
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private StringBuffer staticAssertion2CIVL(String prefix,
			StaticAssertionNode assertion) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("(");
		result.append(this.expression2CIVL(assertion.getExpression()));
		result.append(", \"");
		result.append(assertion.getMessage().getStringRepresentation());
		result.append("\")");
		return result;
	}

	private StringBuffer pragma2CIVL(String prefix, PragmaNode pragma) {
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

	private StringBuffer ordinaryDeclaration2CIVL(String prefix,
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

	private StringBuffer enumType2CIVL(String prefix,
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
				result.append(enumerator.getName());
				if (enumerator.getValue() != null) {
					result.append("=");
					result.append(this.expression2CIVL(enumerator.getValue()));
				}
			}
			result.append("\n");
			result.append(prefix);
			result.append("}");
		}
		return result;
	}

	private StringBuffer ompDeclarative2CIVL(String prefix,
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

	private StringBuffer functionDeclaration2CIVL(String prefix,
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

	private StringBuffer compoundStatement2CIVL(String prefix,
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

	private StringBuffer blockItem2CIVL(String prefix, BlockItemNode block) {
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
		default:
			throw new ABCUnsupportedException("translating block item node of "
					+ kind + " kind into CIVL code");
		}
	}

	private StringBuffer typedefDeclaration2CIVL(String prefix,
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

	private StringBuffer statement2CIVL(String prefix, StatementNode statement) {
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

	private StringBuffer ompStatement2CIVL(String prefix,
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
			result.append(this.sequenceExpressionNode2CIVL(privateList));
			result.append(") ");
		}
		if (firstPrivateList != null) {
			result.append("firstprivate(");
			result.append(this.sequenceExpressionNode2CIVL(firstPrivateList));
			result.append(") ");
		}
		if (sharedList != null) {
			result.append("shared(");
			result.append(this.sequenceExpressionNode2CIVL(sharedList));
			result.append(") ");
		}
		if (copyinList != null) {
			result.append("copyin(");
			result.append(this.sequenceExpressionNode2CIVL(copyinList));
			result.append(") ");
		}
		if (copyPrivateList != null) {
			result.append("copyprivate(");
			result.append(this.sequenceExpressionNode2CIVL(copyPrivateList));
			result.append(") ");
		}
		if (lastPrivateList != null) {
			result.append("lastprivate(");
			result.append(this.sequenceExpressionNode2CIVL(lastPrivateList));
			result.append(") ");
		}
		if (reductionList != null) {
			result.append(sequenceReductionNode2CIVL(reductionList));
		}
		result.append("\n");
		if (block != null)
			result.append(this.statement2CIVL(myIndent, block));
		return result;
	}

	private StringBuffer ompWorksharing2CIVL(String prefix,
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
				result.append(this.expression2CIVL(forNode.chunkSize()));
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

	private StringBuffer ompSync2CIVL(String prefix, OmpSyncNode ompSync) {
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
				result.append(this.sequenceExpressionNode2CIVL(ompSync
						.flushedList()));
				result.append(")");
			}
			break;
		default:// ORDERED
			result.append("ordered ");
		}
		return result;
	}

	private StringBuffer ompParallel2CIVL(String prefix, OmpParallelNode para) {
		StringBuffer result = new StringBuffer();
		ExpressionNode ifClause = para.ifClause(), numThreads = para
				.numThreads();
		boolean isDefaultShared = para.isDefaultShared();

		result.append("parallel ");
		if (ifClause != null) {
			result.append("if(");
			result.append(this.expression2CIVL(ifClause));
			result.append(") ");
		}
		if (numThreads != null) {
			result.append("num_threads(");
			result.append(this.expression2CIVL(numThreads));
			result.append(") ");
		}
		if (isDefaultShared)
			result.append("default(shared) ");
		else
			result.append("default(none) ");
		return result;
	}

	private StringBuffer sequenceReductionNode2CIVL(
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

	private StringBuffer ompReductionNode2CIVL(OmpReductionNode reduction) {
		StringBuffer result = new StringBuffer();

		result.append("reduction(");
		switch (reduction.ompReductionOperatorNodeKind()) {
		case FUNCTION: {
			OmpFunctionReductionNode funcNode = (OmpFunctionReductionNode) reduction;

			result.append(this.expression2CIVL(funcNode.function()));
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
		result.append(this.sequenceExpressionNode2CIVL(reduction.variables()));
		result.append(")");
		return result;
	}

	private StringBuffer sequenceExpressionNode2CIVL(
			SequenceNode<IdentifierExpressionNode> sequence) {
		StringBuffer result = new StringBuffer();
		int numExpressions = sequence.numChildren();

		for (int i = 0; i < numExpressions; i++) {
			IdentifierExpressionNode expression = sequence.getSequenceChild(i);

			if (i != 0)
				result.append(", ");
			result.append(this.expression2CIVL(expression));
		}
		return result;
	}

	private StringBuffer civlForStatement2CIVL(String prefix,
			CivlForNode civlFor) {
		civlFor.getDomain();
		// civlFor.

		// TODO Auto-generated method stub
		return null;
	}

	private StringBuffer loop2CIVL(String prefix, LoopNode loop) {
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

	private StringBuffer atomic2CIVL(String prefix, AtomicNode atomicNode) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		if (atomicNode.isAtom())
			result.append("$atom\n");
		else
			result.append("$atomic\n");
		result.append(statement2CIVL(prefix + indention, atomicNode.getBody()));
		return result;
	}

	private StringBuffer goto2CIVL(String prefix, GotoNode go2) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("goto ");
		result.append(go2.getLabel().name());
		result.append(";");
		return result;
	}

	private StringBuffer labeled2CIVL(String prefix,
			LabeledStatementNode labeled) {
		LabelNode label = labeled.getLabel();
		StatementNode statement = labeled.getStatement();
		StringBuffer result = new StringBuffer();
		String myIndent = prefix + indention;

		result.append(prefix);
		if (label instanceof OrdinaryLabelNode) {
			OrdinaryLabelNode ordinary = (OrdinaryLabelNode) label;
			result.append(ordinary.getName());
			result.append(":\n");
		} else {
			// switch label
			SwitchLabelNode switchLabel = (SwitchLabelNode) label;
			boolean isDefault = switchLabel.isDefault();

			if (isDefault)
				result.append("default:\n");
			else {
				result.append("case ");
				result.append(expression2CIVL(switchLabel.getExpression()));
				result.append(":\n");
			}
		}
		result.append(statement2CIVL(myIndent, statement));
		return result;
	}

	private StringBuffer switch2CIVL(String prefix, SwitchNode swtichNode) {
		return new StringBuffer("switch");
		// TODO throw new CIVLUnimplementedFeatureException(
		// "translating switch node into CIVL code",
		// swtichNode.getSource());
	}

	private StringBuffer jump2CIVL(String prefix, JumpNode jump) {
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

	private StringBuffer return2CIVL(String prefix, ReturnNode returnNode) {
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

	private StringBuffer if2CIVL(String prefix, IfNode ifNode) {
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

	private StringBuffer for2CIVL(String prefix, ForLoopNode loop) {
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
			else if (init instanceof DeclarationListNode) {
				DeclarationListNode list = (DeclarationListNode) init;
				int num = list.numChildren();

				for (int i = 0; i < num; i++) {
					VariableDeclarationNode var = list.getSequenceChild(i);

					if (i != 0)
						result.append(", ");
					result.append(variableDeclaration2CIVL("", var));
				}
			}
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

	private StringBuffer expressionStatement2CIVL(String prefix,
			ExpressionStatementNode expr) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append(expression2CIVL(expr.getExpression()));
		result.append(";");
		return result;
	}

	private StringBuffer when2CIVL(String prefix, WhenNode when) {
		StringBuffer result = new StringBuffer();
		String myIndent = prefix + indention;

		result.append(prefix);
		result.append("$when(");
		result.append(expression2CIVL(when.getGuard()));
		result.append(")\n");
		result.append(statement2CIVL(myIndent, when.getBody()));
		return result;
	}

	private StringBuffer variableDeclaration2CIVL(String prefix,
			VariableDeclarationNode variable) {
		StringBuffer result = new StringBuffer();
		InitializerNode init = variable.getInitializer();
		TypeNode typeNode = variable.getTypeNode();

		result.append(prefix);
		if (typeNode.isInputQualified())
			result.append("$input ");
		if (typeNode.isOutputQualified())
			result.append("$output ");
		result.append(type2CIVL(prefix, typeNode, false));
		result.append(" ");
		result.append(variable.getName());
		if (init != null) {
			result.append(" = ");
			result.append(initializer2CIVL(init));
		}
		return result;
	}

	private StringBuffer initializer2CIVL(InitializerNode init) {
		if (init instanceof CompoundInitializerNode) {
			CompoundInitializerNode compoundInit = (CompoundInitializerNode) init;

			return this.compoundLiteralObject2CIVL(init.getSource(),
					compoundInit.getType(), compoundInit.getLiteralObject());
		} else if (init instanceof ExpressionNode)
			return expression2CIVL((ExpressionNode) init);
		else
			throw new ABCRuntimeException("Invalid initializer: "
					+ init.toString());
	}

	private StringBuffer compoundLiteralObject2CIVL(Source source, Type type,
			CompoundLiteralObject compoundObj) {
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

	private StringBuffer literalObject2CIVL(Source source, Type type,
			LiteralObject obj) {
		if (obj instanceof CompoundLiteralObject)
			return compoundLiteralObject2CIVL(source, type,
					(CompoundLiteralObject) obj);
		else if (obj instanceof ScalarLiteralObject)
			return expression2CIVL(((ScalarLiteralObject) obj).getExpression());
		else
			throw new ABCRuntimeException("Invalid literal object: " + obj);
	}

	private StringBuffer assume2CIVL(String prefix, AssumeNode assume) {
		StringBuffer result = new StringBuffer();

		result.append(prefix);
		result.append("$assume ");
		result.append(expression2CIVL(assume.getExpression()));
		result.append(";");
		return result;
	}

	private StringBuffer expression2CIVL(ExpressionNode expression) {
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
			result.append(this.compoundLiteralNode2CIVL( 
					(CompoundLiteralNode)expression));
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

	private StringBuffer compoundLiteralNode2CIVL(CompoundLiteralNode expression) {
		StringBuffer result = new StringBuffer();
		
		
		return result;
	}

	private StringBuffer regularRange2CIVL(RegularRangeNode range) {
		StringBuffer result = new StringBuffer();
		ExpressionNode step = range.getStep();

		result.append(this.expression2CIVL(range.getLow()));
		result.append(" .. ");
		result.append(this.expression2CIVL(range.getHigh()));
		if (step != null) {
			result.append(" # ");
			result.append(this.expression2CIVL(step));
		}
		return result;
	}

	private StringBuffer sizeable2CIVL(SizeableNode argument) {
		if (argument instanceof ExpressionNode)
			return expression2CIVL((ExpressionNode) argument);
		return type2CIVL("", (TypeNode) argument, false);
	}

	private StringBuffer functionCall2CIVL(FunctionCallNode call) {
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

	private StringBuffer operator2CIVL(OperatorNode operator) {
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

	private StringBuffer type2CIVL(String prefix, TypeNode type,
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
				result.append(this.expression2CIVL(dim));
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
				result.append(this.enumType2CIVL(prefix, enumType));
			else {
				result.append("enum");
				if (enumType.getIdentifier() != null)
					result.append(" " + enumType.getIdentifier().name());
			}
			break;
		case STRUCTURE_OR_UNION: {
			StructureOrUnionTypeNode strOrUnion = (StructureOrUnionTypeNode) type;

			if (isTypeDeclaration)
				result.append(this.structOrUnion2CIVL(prefix, strOrUnion));
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

	private StringBuffer basicType2CIVL(BasicTypeNode type) {
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
