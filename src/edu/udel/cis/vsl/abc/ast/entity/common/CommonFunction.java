package edu.udel.cis.vsl.abc.ast.entity.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.ProgramEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.AssignsOrReadsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.DependsNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;

public class CommonFunction extends CommonOrdinaryEntity implements Function {

	private boolean isInlined, doesNotReturn;

	private Set<Function> callers = new HashSet<>();
	private Set<Function> callees = new HashSet<>();
	static Function mainFunction;

	private List<ExpressionNode> preconditions = new LinkedList<>();

	private List<ExpressionNode> postconditions = new LinkedList<>();

	private List<DependsNode> depends = new LinkedList<>();
	private List<ExpressionNode> guards = new LinkedList<>();
	private List<AssignsOrReadsNode> assigns = new LinkedList<>();
	private List<AssignsOrReadsNode> reads = new LinkedList<>();

	public CommonFunction(String name, ProgramEntity.LinkageKind linkage,
			Type type) {
		super(EntityKind.FUNCTION, name, linkage, type);
	}

	@Override
	public boolean isInlined() {
		return isInlined;
	}

	@Override
	public void setIsInlined(boolean value) {
		this.isInlined = value;
	}

	@Override
	public boolean doesNotReturn() {
		return doesNotReturn;
	}

	@Override
	public void setDoesNotReturn(boolean value) {
		this.doesNotReturn = value;
	}

	@Override
	public FunctionDefinitionNode getDefinition() {
		return (FunctionDefinitionNode) super.getDefinition();
	}

	@Override
	public Scope getScope() {
		Scope result = getDefinition().getBody().getScope();

		while (result != null) {
			if (result.getScopeKind() == ScopeKind.FUNCTION)
				break;
			result = result.getParentScope();
		}
		if (result == null)
			throw new ABCRuntimeException(
					"Could not find function scope of function " + this);
		return result;
	}

	@Override
	public FunctionType getType() {
		return (FunctionType) super.getType();
	}

	@Override
	public Set<Function> getCallers() {
		return callees;
	}

	@Override
	public Set<Function> getCallees() {
		return callers;
	}

	@Override
	public Iterator<ExpressionNode> getPreconditions() {
		return preconditions.iterator();
	}

	@Override
	public Iterator<ExpressionNode> getPostconditions() {
		return postconditions.iterator();
	}

	@Override
	public void addPrecondition(ExpressionNode expression) {
		preconditions.add(expression);
	}

	@Override
	public void addPostcondition(ExpressionNode expression) {
		postconditions.add(expression);
	}

	@Override
	public void addDepends(DependsNode depends) {
		this.depends.add(depends);
	}

	@Override
	public void addAssigns(AssignsOrReadsNode assigns) {
		this.assigns.add(assigns);
	}

	@Override
	public void addGuard(ExpressionNode expression) {
		this.guards.add(expression);
	}

	@Override
	public Iterator<DependsNode> getDepends() {
		return this.depends.iterator();
	}

	@Override
	public Iterator<ExpressionNode> getGuard() {
		return this.guards.iterator();
	}

	@Override
	public Iterator<AssignsOrReadsNode> getAssigns() {
		return this.assigns.iterator();
	}

	@Override
	public void addReads(AssignsOrReadsNode reads) {
		this.reads.add(reads);
	}

	@Override
	public Iterator<AssignsOrReadsNode> getReads() {
		return this.reads.iterator();
	}

}
