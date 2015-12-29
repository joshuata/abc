/* Grammar for programming CIVL-C.
 * Based on C11 grammar.
 *
 * Author: Stephen F. Siegel, University of Delaware
 *
 * This grammar assumes the input token stream is the result of
 * translation phase 7, as specified in the C11 Standard.
 * In particular, all the preprocessing has already been
 * done.  
 *
 * In addition to the Standard, I borrowed from the older
 * C grammar included with the ANTLR distribution.
 *
 */
parser grammar CivlCParser;

options {
	language=Java;
	tokenVocab=PreprocessorParser;
	output=AST;
}

import BaseCParser;

/*
scope Symbols {
    Set<String> types; // to keep track of typedefs
    Set<String> enumerationConstants; // to keep track of enum constants
    boolean isFunctionDefinition; // "function scope": entire function definition
}

scope DeclarationScope {
    boolean isTypedef; // is the current declaration a typedef
}
*/

@header
{
package edu.udel.cis.vsl.abc.front.c.parse.common;

import java.util.Set;
import java.util.HashSet;
import edu.udel.cis.vsl.abc.front.c.parse.IF.RuntimeParseException;
}

/*
@members {
	public void setSymbols_stack(Stack<ScopeSymbols> symbols){
		this.Symbols_stack = new Stack();
		while(!symbols.isEmpty()){
			ScopeSymbols current = symbols.pop();
			Symbols_scope mySymbols = new Symbols_scope();

			mySymbols.types = current.types;
			mySymbols.enumerationConstants = current.enumerationConstants;
			Symbols_stack.add(mySymbols);
		}
	}

	@Override
	public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
		String hdr = getErrorHeader(e);
		String msg = getErrorMessage(e, tokenNames);

		throw new RuntimeParseException(hdr+" "+msg, e.token);
	}

	@Override
	public void emitErrorMessage(String msg) { // don't try to recover!
	    throw new RuntimeParseException(msg);
	}

	boolean isTypeName(String name) {
		for (Object scope : Symbols_stack)
			if (((Symbols_scope)scope).types.contains(name)) return true;
		return false;
	}

	boolean isEnumerationConstant(String name) {
		boolean answer = false;

		// System.err.print("Is "+name+" an enumeration constant: ");
		for (Object scope : Symbols_stack) {
			if (((Symbols_scope)scope).enumerationConstants.contains(name)) {
				answer=true;
				break;
			}
		}
		// System.err.println(answer);
		// System.err.flush();
		return answer;
	}
}
*/

compilationUnit : primaryExpression;
