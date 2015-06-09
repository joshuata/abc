package edu.udel.cis.vsl.abc.analysis.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.abc.analysis.IF.Analyzer;
import edu.udel.cis.vsl.abc.ast.IF.AST;
import edu.udel.cis.vsl.abc.ast.entity.IF.Function;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDeclarationNode;
import edu.udel.cis.vsl.abc.ast.node.IF.declaration.FunctionDefinitionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.ExpressionNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.FunctionCallNode;
import edu.udel.cis.vsl.abc.ast.node.IF.expression.IdentifierExpressionNode;
import edu.udel.cis.vsl.abc.ast.type.IF.FunctionType;
import edu.udel.cis.vsl.abc.ast.type.IF.PointerType;
import edu.udel.cis.vsl.abc.ast.type.common.CommonFunctionType;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;

/**
 * Given an AST, determines caller/callee relationships among functions.
 * 
 * Calls through a function pointer are approximated by the set of functions whose type
 * matches the function pointer type.
 * 
 * Analysis is two-phase: First "collect" the set of functions declared for each function type.
 * Second "process" call nodes using the function-type relation to resolve indirect calls.
 * 
 * @author dwyer
 * 
 */
public class CallAnalyzer implements Analyzer {
	Map<FunctionType,Set<Function>> functionsOfAType = new HashMap<FunctionType,Set<Function>>();
	
	private void collectProgram(ASTNode node) {
		if (node instanceof FunctionDefinitionNode) {
			collectFunctionDefinitionNode((FunctionDefinitionNode)node);
		} else if (node instanceof FunctionDeclarationNode) {
			// Will only reach this code if this is a prototype declaration
			collectFunctionDeclarationNode((FunctionDeclarationNode)node);
		} else if (node != null) {
			for (ASTNode child : node.children()) {
				collectProgram(child);
			}
		}
	}
	private void collectFunctionDefinitionNode(FunctionDefinitionNode funNode) {
		Function fEntity = funNode.getEntity();
		
		FunctionType funType = (FunctionType) funNode.getTypeNode().getType();

		collectFunctionType(funType);
		
		Set<Function> funsOfThisType = getFunctionsOfAType(funType);
		funsOfThisType.add(fEntity);	
	}

	private void collectFunctionDeclarationNode(FunctionDeclarationNode funcNode) {
		collectFunctionType((FunctionType)(funcNode.getTypeNode().getType()));
	}
	
	private void collectFunctionType(FunctionType funType) {
		if (getFunctionsOfAType(funType) == null) {
			functionsOfAType.put(funType, new HashSet<Function>());
		}
	}
		
	private Set<Function> getFunctionsOfAType(FunctionType funType) {
		for (FunctionType fType : functionsOfAType.keySet()) {
			if (funType.compatibleWith(fType)) {
				return functionsOfAType.get(fType);
			}
		}
		return null;
	}
	
	private void processFunctionDefinitionNode(FunctionDefinitionNode funcNode) {
		Function fEntity = funcNode.getEntity();	
		processFunctionBody(funcNode.getBody(), fEntity);
	}
	
	private void processFunctionBody(ASTNode node, Function caller) {
		if (node instanceof FunctionCallNode) {
			FunctionCallNode fcn = (FunctionCallNode)node;
			
			if (fcn.getFunction() instanceof IdentifierExpressionNode) {
				IdentifierNode calledFunId = ((IdentifierExpressionNode)fcn.getFunction()).getIdentifier();
				
				// Call directly to a function
				if (calledFunId.getEntity() instanceof Function) {
					Function callee = (Function)calledFunId.getEntity();
					caller.getCallees().add(callee);
					callee.getCallers().add(caller);
				} else {
					// Call through an expression (an identifier) 
					PointerType pFunType = (PointerType)fcn.getFunction().getConvertedType();
					FunctionType funType = (FunctionType)pFunType.referencedType();
				
					Set<Function> callees = getFunctionsOfAType(funType);
					
					assert callees != null : "CallAnalyzer: no function type for indirect call";
					
					for (Function callee : callees) {
						caller.getCallees().add(callee);
						callee.getCallers().add(caller);
					}
				}
			} else {				
				PointerType pFuncType = (PointerType)fcn.getFunction().getConvertedType();
				Set<Function> callees = functionsOfAType.get(pFuncType.referencedType());
				for (Function callee : callees) {
					caller.getCallees().add(callee);
					callee.getCallers().add(caller);
				}
			}
			
			// Check arguments for nested calls
			for (ExpressionNode arg : fcn.getArguments()) {
				processFunctionBody(arg, caller);
			}
		} else if (node != null) {
			for (ASTNode child : node.children()) {
				processFunctionBody(child, caller);
			}
		}
	}

	private void processProgram(ASTNode node) {
		if (node instanceof FunctionDefinitionNode) {
			processFunctionDefinitionNode((FunctionDefinitionNode)node);
		} else if (node != null) {
			for (ASTNode child : node.children()) {
				processProgram(child);
			}
		}
	}

	@Override
	public void clear(AST unit) {
		functionsOfAType.clear();
		clearNode(unit.getRootNode());
	}
	
	private void clearNode(ASTNode node) {
		if (node != null) {
			if (node instanceof FunctionDefinitionNode) {
				Function f = ((FunctionDefinitionNode)node).getEntity();
				if (f != null) {
					Set<Function> callers = f.getCallers();
					if (callers != null) callers.clear();
					Set<Function> callees = f.getCallees();
					if (callees != null) callees.clear();
				}
			}
			for (ASTNode child : node.children()) {
				clearNode(child);
			}
		}
	}

	@Override
	public void analyze(AST unit) throws SyntaxException {
		ASTNode root = unit.getRootNode();
		collectProgram(root);
		processProgram(root);
	}
	
	static public void printCallGraph(AST unit) {
		ASTNode root = unit.getRootNode();
		printCallGraphNodes(root);
	}
	
	static private void printCallGraphNodes(ASTNode node) {
		if (node instanceof FunctionDefinitionNode) {
			Function f = ((FunctionDefinitionNode)node).getEntity();
			System.out.println("Function "+f+" is called by:");
			for (Function caller : f.getCallers()) {
				System.out.println("   "+caller);
			}
			if (f.getCallers().isEmpty()) 
				System.out.println("   nothing");
			System.out.println("and calls:");
			for (Function callee : f.getCallees()) {
				System.out.println("   "+callee);
			}
			if (f.getCallees().isEmpty()) 
				System.out.println("   nothing");
		} else if (node != null) {
			for (ASTNode child : node.children()) {
				printCallGraphNodes(child);
			}
		}
	}

}
