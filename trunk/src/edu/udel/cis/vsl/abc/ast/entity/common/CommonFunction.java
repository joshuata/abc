package edu.udel.cis.vsl.abc.ast.entity.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.entity.IF.ProgramEntity;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope;
import edu.udel.cis.vsl.abc.ast.entity.IF.Scope.ScopeKind;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
import edu.udel.cis.vsl.abc.ast.type.IF.Type;
import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;

public class CommonFunction extends CommonOrdinaryEntity implements Function {

	private boolean isInlined, doesNotReturn;

	private List<ExpressionNode> preconditions = new LinkedList<ExpressionNode>();

	private List<ExpressionNode> postconditions = new LinkedList<ExpressionNode>();

	public CommonFunction(String name, ProgramEntity.LinkageKind linkage, Type type) {
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

}
