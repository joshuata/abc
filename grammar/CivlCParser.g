/* Grammar for programming CIVL-C.
 * Based on C11 grammar.
 *
 * Author: Stephen F. Siegel
 * Last modified:
 *
 * This grammar assumes the input token stream is the result of
 * translation phase 7, as specified in the Standard.
 * In particular, all the preprocessing has already been
 * done.  
 *
 * In addition to the Standard, I borrowed from the older
 * C grammar included with the ANTLR distribution.
 *
 */
parser grammar CivlCParser;

options
{
	language=Java;
	tokenVocab=PreprocessorParser;
	output=AST;
}

tokens
{
	ABSENT;               // represents missing syntactic element
	GENERIC_ASSOC_LIST;   // generic association list
	GENERIC_ASSOCIATION;  // a generic association
	ENUMERATION_CONSTANT; // use of enumeration constant
	COMPOUND_LITERAL;     // literal for structs, etc.
	CONTRACT;             // procedure contracts
	CALL;                 // function call
	INDEX;                // array subscript operator
	ARGUMENT_LIST;        // list of arguments to an operator
	POST_INCREMENT;
	POST_DECREMENT;
	PRE_INCREMENT;
	PRE_DECREMENT;
	OPERATOR;             // symbol indicating an operator
	TYPE;                 // symbol indicating "type"
	EXPR;                 // symbol indicating "expression"
	PARENTHESIZED_EXPRESSION;
	CAST;                 // type cast operator
	DECLARATION;          // a declaration
	DECLARATION_SPECIFIERS;   // list of declaration specifiers
	INIT_DECLARATOR_LIST;     // list of initializer-declarator pairs
	INIT_DECLARATOR;          // initializer-declaration pair
 	STRUCT_DECLARATION_LIST;  // list of field declarations
	STRUCT_DECLARATION;       // a field declaration
	SPECIFIER_QUALIFIER_LIST; // list of type specifiers and qualifiers
	STRUCT_DECLARATOR_LIST;   // list of struct/union declarators
	STRUCT_DECLARATOR;        // a struct/union declarator
	ENUMERATOR_LIST;      // list of enumerators in enum type definition
	ENUMERATOR;           // identifier and optional int constant
	DECLARATOR;           // a declarator
	DIRECT_DECLARATOR;    // declarator after removing leading *s
	TYPE_QUALIFIER_LIST;  // list of type qualifiers
	ARRAY_SUFFIX;         // [..] used in declarator
	FUNCTION_SUFFIX;      // (..) used in declarator
	POINTER;              // * used in declarator
	PARAMETER_TYPE_LIST;  // parameter list and optional "..."
	PARAMETER_LIST;       // list of parameter decls in function decl
	PARAMETER_DECLARATION;// parameter declaration in function decl
	IDENTIFIER_LIST;      // list of parameter names only in function decl
	TYPE_NAME;            // type specification without identifier
	ABSTRACT_DECLARATOR;  // declarator without identifier
	DIRECT_ABSTRACT_DECLARATOR; // direct declarator sans identifier
	SCALAR_INITIALIZER;   // 
	INITIALIZER_LIST;
	DESIGNATED_INITIALIZER;
	DESIGNATION;
	ARRAY_ELEMENT_DESIGNATOR;
	FIELD_DESIGNATOR;
	IDENTIFIER_LABELED_STATEMENT;
	CASE_LABELED_STATEMENT;
	DEFAULT_LABELED_STATEMENT;
	COMPOUND_STATEMENT;
	BLOCK_ITEM_LIST;
	EXPRESSION_STATEMENT;
	TRANSLATION_UNIT;
	DECLARATION_LIST;
	FUNCTION_DEFINITION;
	TYPEDEF_NAME;
	TOKEN_LIST;
	SCOPE_NAME;		// use of a CIVL-C scope name
	SCOPE_LIST;		// e.g., "<s1,s2,s3>"
	PARTIAL;
	PARTIAL_LIST;
	DERIVATIVE_EXPRESSION;
}

scope Symbols {
    Set<String> types; // to keep track of typedefs
    Set<String> enumerationConstants; // to keep track of enum constants
    Set<String> scopeNames; // to keep track of CIVL-C scope names
    boolean isFunctionDefinition; // "function scope": entire function definition
}

scope DeclarationScope {
    boolean isTypedef; // is the current declaration a typedef
    Set<String> scopeNames; // to keep track of CIVL-C scope names
}

@header
{
package edu.udel.cis.vsl.abc.parse.common;

import java.util.Set;
import java.util.HashSet;
import edu.udel.cis.vsl.abc.parse.IF.RuntimeParseException;
}

@members {
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
	
	boolean isScopeName(String name) {
		for (Object scope : Symbols_stack)
			if (((Symbols_scope)scope).scopeNames.contains(name)) {
				//System.err.println("Found scope "+name+" in symbol stack");
				return true;				
			}
		for (Object scope : DeclarationScope_stack)
			if (((DeclarationScope_scope)scope).scopeNames.contains(name)) {
				//System.err.println("Found scope "+name+" in decl stack");
				return true; 
			}
		//System.err.println("Did not find scope "+name);
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

/* ****** CIVL-C Scopes ********* */

/* Use of a CIVL-C scope name.   The scope name must have
 * been declared previously, and therefore entered into
 * the scope name set of the current scope or one of its
 * containing scopes. */
//scopeName
//	: {isScopeName(input.LT(1).getText())}? IDENTIFIER
//	  -> ^(SCOPE_NAME IDENTIFIER)
//	;


// uses: double *<s>, f<s1,s2>, typedefName<s1,s2>


/*
 * A scope modifier, a CIVL-C construct such as the "<s>"
 * in the declaration "double *<s> p;" meaning p is a
 * pointer-to-double-in-scope-s.
 */
scopeUse
	: {input.LT(1).getType()==LT && isScopeName(input.LT(2).getText())}?
	  LT IDENTIFIER GT -> ^(SCOPE_NAME IDENTIFIER)
	;


/*
 * An optional scope modifier, providing a non-empty tree
 * "ABSENT" which matches nothing.
 */
scopeUse_opt
	: -> ABSENT
	| scopeUse
	;


/* Used to match a scope list but not produce any tree
 * or modify any sets.  Used in predicates only.

scopeList
	: LT ( GT
	     | IDENTIFIER (COMMA IDENTIFIER)* GT
	     )
	;
*/	  


/* This rule is used to match a list of scopes enclosed in
 * in angular brackets, as in <s1,s2>.  This construct
 * defines new scope symbols (s1 and s2) in the current
 * declaration being parsed.  The strings "s1" and "s2"
 * will be added to the scopeNames set in the current
 * DeclarationScope if this rule is matched.  When that
 * DeclarationScope is popped, that set will disappear.
 * We need to keep track of the scope symbols that are
 * defined at any point in order to resolve ambiguities
 * such as "a<b>(c)" which could be an expression or
 * an invocation of a function a with scope parameter b
 * and argument c.  The ambiguity is resolved by determining
 * whether b is a scope symbol.
 *
 * In CIVL-C, this construct can precede a function declaration
 * or definition, or a typedef.
 */
scopeListDef
	:  LT	(
		  GT -> ^(SCOPE_LIST) // empty list
		| scopeIdentifier (COMMA scopeIdentifier)* GT
		  -> ^(SCOPE_LIST scopeIdentifier+)
		)
	;

/* Matches an identifier and then puts the name of that identifier
 * in the scopeNames set of the current DeclarationScope.
 */
scopeIdentifier
	: IDENTIFIER 
	  {
		$DeclarationScope::scopeNames.add($IDENTIFIER.text);
		//System.err.println("Adding scope name "+$IDENTIFIER.text);
	  }
	;

/* Matches either nothing and returns the trivial tree "ABSENT"
 * or a scopeListDef.  Used to avoid holes in the tree.
 */
scopeListDef_opt
	: -> ABSENT
	| scopeListDef
	;

/* This rule is used to match a list of scopes contained
 * in angular brackets when that list represents a USE
 * (as opposed to a DEFINITION) of scopes.  In CIVL-C,
 * this can be used when using a typedef name or
 * invoking a function.  The scopes occurring in the
 * list should have been defined earlier.
 *
 * To resolve ambiguity with expressions, this rule looks
 * for either "<>" (the empty list) or if the list is non-
 * empty it looks at the first symbol in the list and
 * looks to see if a scope symbol with that name has
 * been defined.  If this condition holds, it applies
 * the rule.
 */
scopeListUse
	: {input.LT(1).getType()==LT &&
		(input.LT(2).getType()==GT ||
			isScopeName(input.LT(2).getText()))}?
	  LT	(
		  GT -> ^(SCOPE_LIST) // empty list
		| IDENTIFIER (COMMA IDENTIFIER)* GT
		  -> ^(SCOPE_LIST IDENTIFIER+)
		)
	;

/* Matches nothing or a scopeListUse, to avoid holes in the
 * tree by returning the "ABSENT" trivial tree for nothing.
 */
scopeListUse_opt
	: -> ABSENT
	| scopeListUse
	;




/* ***** A.2.1: Expressions ***** */

/* Constants from A.1.5 */

constant
	: enumerationConstant
	| INTEGER_CONSTANT
	| FLOATING_CONSTANT
	| CHARACTER_CONSTANT
	| SELF | TRUE | FALSE | RESULT
	;

enumerationConstant
	: {isEnumerationConstant(input.LT(1).getText())}? IDENTIFIER ->
	  ^(ENUMERATION_CONSTANT IDENTIFIER)
	;

/* 6.5.1 */
primaryExpression
	: constant
	| // {!isEnumerationConstant(input.LT(1).getText())}?
	  IDENTIFIER
	| STRING_LITERAL
	| LPAREN expression RPAREN 
	  -> ^(PARENTHESIZED_EXPRESSION LPAREN expression RPAREN)
	| genericSelection
	| derivativeExpression
	;

/* 6.5.1.1 */

genericSelection
	: GENERIC LPAREN assignmentExpression COMMA genericAssocList
	  RPAREN
	  -> ^(GENERIC assignmentExpression genericAssocList)
	;

/* 6.5.1.1 */
genericAssocList
	: genericAssociation (COMMA genericAssociation)*
	  -> ^(GENERIC_ASSOC_LIST genericAssociation+)
	;

/* 6.5.1.1 */
genericAssociation
	: typeName COLON assignmentExpression
	  -> ^(GENERIC_ASSOCIATION typeName assignmentExpression)
	| DEFAULT COLON assignmentExpression
	  -> ^(GENERIC_ASSOCIATION DEFAULT assignmentExpression)
	;

/* 6.5.2 */
postfixExpression
	: (postfixExpressionRoot -> postfixExpressionRoot)
		// array index operator:
	  ( l=LSQUARE expression RSQUARE
	    -> ^(OPERATOR
	           INDEX[$l]
	           ^(ARGUMENT_LIST $postfixExpression expression)
	           RSQUARE)
	  |	// function call with scope parameters:
	    scopeListUse
	    LPAREN argumentExpressionList RPAREN
	    -> ^(CALL LPAREN $postfixExpression argumentExpressionList
	    	 RPAREN scopeListUse)
	  |	// function call without scope modifier:
	    LPAREN argumentExpressionList RPAREN
	    -> ^(CALL LPAREN $postfixExpression argumentExpressionList
	    	 RPAREN ABSENT)
	  | DOT IDENTIFIER
	    -> ^(DOT $postfixExpression IDENTIFIER)
	  | ARROW IDENTIFIER
	    -> ^(ARROW $postfixExpression IDENTIFIER)
	  | p=PLUSPLUS
	    -> ^(OPERATOR POST_INCREMENT[$p]
	         ^(ARGUMENT_LIST $postfixExpression))
	  | // CIVL-C @ operator: remote reference like x@f
	    AT IDENTIFIER
	    -> ^(AT $postfixExpression IDENTIFIER)
	  | m=MINUSMINUS
	    -> ^(OPERATOR POST_DECREMENT[$m]
	         ^(ARGUMENT_LIST $postfixExpression))
	  )*
	;

/*
 * The "(typename) {...}" is a "compound literal".
 * See C11 Sec. 6.5.2.5.  I don't know what
 * it means when it ends with an extra COMMA.
 * I assume it doesn't mean anything and is just
 * allowed as a convenience for the poor C programmer
 * (but why?).
 *
 * Ambiguity: need to distinguish the compound literal
 * "(typename) {...}" from the primaryExpression
 * "(expression)".  Presence of '{' implies it must
 * be the compound literal.
 */
postfixExpressionRoot
	: (LPAREN typeName RPAREN LCURLY)=>
	  LPAREN typeName RPAREN LCURLY initializerList 
		( RCURLY
		| COMMA RCURLY
		)
	  -> ^(COMPOUND_LITERAL LPAREN typeName initializerList RCURLY)
	| primaryExpression
	;

/* 6.5.2 */
argumentExpressionList
	: -> ^(ARGUMENT_LIST)
	| assignmentExpression (COMMA assignmentExpression)*
	  -> ^(ARGUMENT_LIST assignmentExpression+)
	;

/* 6.5.3 */
unaryExpression
	: postfixExpression
	| p=PLUSPLUS unaryExpression
	  -> ^(OPERATOR PRE_INCREMENT[$p]
	       ^(ARGUMENT_LIST unaryExpression))
	| m=MINUSMINUS unaryExpression
	  -> ^(OPERATOR PRE_DECREMENT[$m]
	       ^(ARGUMENT_LIST unaryExpression))
	| unaryOperator castExpression
	  -> ^(OPERATOR unaryOperator ^(ARGUMENT_LIST castExpression))
	| (SIZEOF LPAREN typeName)=> SIZEOF LPAREN typeName RPAREN
	  -> ^(SIZEOF TYPE typeName)
	| SIZEOF unaryExpression
	  -> ^(SIZEOF EXPR unaryExpression)
	| ALIGNOF LPAREN typeName RPAREN
	  -> ^(ALIGNOF typeName)
	| spawnExpression
	;


spawnExpression
	: SPAWN postfixExpressionRoot scopeListUse_opt LPAREN 
	  argumentExpressionList RPAREN 
	  -> ^(SPAWN LPAREN postfixExpressionRoot
	       argumentExpressionList RPAREN scopeListUse_opt)
	;


/* 6.5.3 */
unaryOperator
	: AMPERSAND | STAR | PLUS | SUB | TILDE | NOT | BIG_O
	;

/* 6.5.4 */
// ambiguity: (expr) is a unary expression and looks
// like (typeName).
castExpression
	: (LPAREN typeName RPAREN)=> l=LPAREN typeName RPAREN castExpression
	  -> ^(CAST typeName castExpression $l)
	| unaryExpression
	;

/* 6.5.5 */
multiplicativeExpression
	: (castExpression -> castExpression)
	( STAR y=castExpression
	  -> ^(OPERATOR STAR ^(ARGUMENT_LIST $multiplicativeExpression $y))
	| DIV y=castExpression
	  -> ^(OPERATOR DIV ^(ARGUMENT_LIST $multiplicativeExpression $y))
        | MOD y=castExpression
	  -> ^(OPERATOR MOD ^(ARGUMENT_LIST $multiplicativeExpression $y))
        )*
	;

/* 6.5.6 */
additiveExpression
	: (multiplicativeExpression -> multiplicativeExpression)
        ( PLUS y=multiplicativeExpression
          -> ^(OPERATOR PLUS ^(ARGUMENT_LIST $additiveExpression $y))
        | SUB y=multiplicativeExpression
          -> ^(OPERATOR SUB ^(ARGUMENT_LIST $additiveExpression $y))
        )*
	;

/* 6.5.7 */
shiftExpression
	: (additiveExpression -> additiveExpression)
        ( SHIFTLEFT y=additiveExpression
          -> ^(OPERATOR SHIFTLEFT ^(ARGUMENT_LIST $shiftExpression $y))
        | SHIFTRIGHT y=additiveExpression
          -> ^(OPERATOR SHIFTRIGHT ^(ARGUMENT_LIST $shiftExpression $y))
        )*
	;

/* 6.5.8 */
relationalExpression
	: ( shiftExpression -> shiftExpression )
	  ( relationalOperator y=shiftExpression
	    -> ^(OPERATOR relationalOperator ^(ARGUMENT_LIST $relationalExpression $y))
	  )*
	;

relationalOperator
	: LT | GT | LTE | GTE
	;

/* 6.5.9 */
equalityExpression
	: ( relationalExpression -> relationalExpression )
	  ( equalityOperator y=relationalExpression
	    -> ^(OPERATOR equalityOperator ^(ARGUMENT_LIST $equalityExpression $y))
	  )*
	;

equalityOperator
	: EQUALS | NEQ
	;

/* 6.5.10 */
andExpression
	: ( equalityExpression -> equalityExpression )
	  ( AMPERSAND y=equalityExpression
	    -> ^(OPERATOR AMPERSAND ^(ARGUMENT_LIST $andExpression $y))
	  )*
	;

/* 6.5.11 */
exclusiveOrExpression
	: ( andExpression -> andExpression )
	  ( BITXOR y=andExpression
	    -> ^(OPERATOR BITXOR ^(ARGUMENT_LIST $exclusiveOrExpression $y))
	  )*
	;

/* 6.5.12 */
inclusiveOrExpression
	: ( exclusiveOrExpression -> exclusiveOrExpression )
	  ( BITOR y=exclusiveOrExpression
	    -> ^(OPERATOR BITOR ^(ARGUMENT_LIST $inclusiveOrExpression $y))
	  )*
	;

/* 6.5.13 */
logicalAndExpression
	: ( inclusiveOrExpression -> inclusiveOrExpression )
	  ( AND y=inclusiveOrExpression
	    -> ^(OPERATOR AND ^(ARGUMENT_LIST $logicalAndExpression $y))
	  )*
	;

/* 6.5.14 */
logicalOrExpression
	: ( logicalAndExpression -> logicalAndExpression )
	  ( OR y=logicalAndExpression
	    -> ^(OPERATOR OR ^(ARGUMENT_LIST $logicalOrExpression $y))
	  )*
	;
	
/* Added for CIVL-C.  Usually 6.5.15 would use logicalOrExpression. */
logicalImpliesExpression
	: ( logicalOrExpression -> logicalOrExpression )
	  ( IMPLIES y=logicalOrExpression
	    -> ^(OPERATOR IMPLIES ^(ARGUMENT_LIST $logicalImpliesExpression $y))
	  )*
    	;

/* 6.5.15 */
conditionalExpression
	: logicalImpliesExpression
	( -> logicalImpliesExpression
    	| QMARK expression COLON conditionalExpression
    	  -> ^(OPERATOR QMARK
    	       ^(ARGUMENT_LIST
    	         logicalImpliesExpression
    	         expression
    	         conditionalExpression))
    	)
	;

/* 6.5.16
 * conditionalExpression or
 * Root: OPERATOR
 * Child 0: assignmentOperator
 * Child 1: ARGUMENT_LIST
 * Child 1.0: unaryExpression
 * Child 1.1: assignmentExpression
 */
assignmentExpression
	: (unaryExpression assignmentOperator)=>
	  unaryExpression assignmentOperator assignmentExpression
	  -> ^(OPERATOR assignmentOperator
	       ^(ARGUMENT_LIST unaryExpression assignmentExpression))
	| conditionalExpression
	| quantifierExpression
	;

/* 6.5.16 */
assignmentOperator
	: ASSIGN | STAREQ | DIVEQ | MODEQ | PLUSEQ | SUBEQ
	| SHIFTLEFTEQ | SHIFTRIGHTEQ | BITANDEQ | BITXOREQ | BITOREQ
	;

/* 6.5.17
 * assignmentExpression or
 * Root: OPERATOR
 * Child 0: COMMA
 * Child 1: ARGUMENT_LIST
 * Child 1.0: arg0
 * Child 1.1: arg1
 */
commaExpression
	: ( x=assignmentExpression -> assignmentExpression)
	  ( COMMA y=assignmentExpression
	    -> ^(OPERATOR COMMA ^(ARGUMENT_LIST $x $y))
	  )*
	;

expression
	: COLLECTIVE LPAREN proc=conditionalExpression
	  COMMA intExpr=conditionalExpression RPAREN
	  body=conditionalExpression
	  -> ^(COLLECTIVE $proc $intExpr $body)
	| commaExpression
	;
			
derivativeExpression
	: DERIV LSQUARE IDENTIFIER COMMA partialList RSQUARE 
	  LPAREN argumentExpressionList RPAREN
	  -> ^(DERIVATIVE_EXPRESSION IDENTIFIER partialList argumentExpressionList RPAREN)
	;

partialList
	: partial (COMMA partial)* -> ^(PARTIAL_LIST partial+)
	;

partial
	: LCURLY IDENTIFIER COMMA INTEGER_CONSTANT RCURLY 
	  -> ^(PARTIAL IDENTIFIER INTEGER_CONSTANT)
    ;

/* 6.6 */
constantExpression
	: conditionalExpression
	;
	
quantifierExpression
	: quantifier LCURLY type=typeName id=IDENTIFIER
	  BITOR restrict=conditionalExpression RCURLY 
	  cond=assignmentExpression
	  -> ^(quantifier $type $id $restrict $cond)
	| quantifier LCURLY id=IDENTIFIER ASSIGN lower=assignmentExpression DOTDOT upper=assignmentExpression
	  RCURLY cond=assignmentExpression
	  -> ^(quantifier $id $lower $upper $cond)
	;
	
quantifier
	: FORALL | EXISTS | UNIFORM
	;

/* ***** A.2.2: Declarations ***** */

/* 6.7.
 *
 * This rule will construct either a SCOPE, DECLARATION, or
 * STATICASSERT tree:
 *
 * Root: SCOPE
 * Child 0: Identifier
 *
 * Root: DECLARATION
 * Child 0: declarationSpecifiers
 * Child 1: initDeclaratorList or ABSENT
 * Child 2: contract or ABSENT
 * Child 3: scopeParameterList or ABSENT
 *
 * Root: STATICASSERT
 * Child 0: constantExpression
 * Child 1: stringLiteral
 *
 * The declarationSpecifiers rule returns a bit telling whether
 * "typedef" occurred among the specifiers.  This bit is passed 
 * to the initDeclaratorList rule, and down the call chain,
 * where eventually an IDENTIFIER should be reached.  At that point,
 * if the bit is true, the IDENTIFIER is added to the set of typedef
 * names.
 *
 */
declarationOLD
scope DeclarationScope;
@init {
  $DeclarationScope::isTypedef = false;
  $DeclarationScope::scopeNames = new HashSet<String>();
}
	: SCOPE IDENTIFIER SEMI
	  {
		$Symbols::scopeNames.add($IDENTIFIER.text);
	  }
	  -> ^(SCOPE IDENTIFIER)
	| s=scopeListDef_opt d=declarationSpecifiers
	  ( 
	    i=initDeclaratorList contract_opt SEMI
	    -> ^(DECLARATION $d $i contract_opt $s)
	  | SEMI
	    -> ^(DECLARATION $d ABSENT ABSENT $s)
	  )
	| staticAssertDeclaration
	;


declaration
scope DeclarationScope;
@init {
  $DeclarationScope::isTypedef = false;
  $DeclarationScope::scopeNames = new HashSet<String>();
}
	: scopeListDef_opt! declarationInScopeList[(CommonTree)$scopeListDef_opt.tree]
	;

/*
	| SCOPE IDENTIFIER SEMI
	  {
		$Symbols::scopeNames.add($IDENTIFIER.text);
	  }
	  -> ^(SCOPE IDENTIFIER)
	| staticAssertDeclaration
*/

declarationInScopeList[CommonTree scopes]
	: SCOPE IDENTIFIER SEMI
	  {
	  		$Symbols::scopeNames.add($IDENTIFIER.text);
	  }
	  -> ^(SCOPE IDENTIFIER)
	| staticAssertDeclaration
	| d=declarationSpecifiers
	  ( 
	    i=initDeclaratorList contract_opt SEMI
	    -> ^(DECLARATION $d $i contract_opt {$scopes})
	  | SEMI
	    -> ^(DECLARATION $d ABSENT ABSENT {$scopes})
	  )
	;

/* 6.7
 * Root: DECLARATION_SPECIFIERS
 * Children: declarationSpecifier (any number)
 */
declarationSpecifiers
	: l=declarationSpecifierList
	  -> ^(DECLARATION_SPECIFIERS declarationSpecifierList)
	;

/* Tree: flat list of declarationSpecifier
 */
declarationSpecifierList
	: (
	    {!$DeclarationScope::isTypedef || input.LT(2).getType() != SEMI }?
	    s=declarationSpecifier
	  )+
	;

declarationSpecifier
	: s=storageClassSpecifier
	| typeSpecifierOrQualifier
	| functionSpecifier
	| alignmentSpecifier
	;

/* 
 * I factored this out of the declarationSpecifiers rule
 * to deal with the ambiguity of "ATOMIC" in one place.
 * "ATOMIC ( typeName )" matches atomicTypeSpecifier, which
 * is a typeSpecifier. "ATOMIC" matches typeQualifier.
 * When you see "ATOMIC" all you have to do is look at the
 * next token. If it's '(', typeSpecifier is it.
 */
typeSpecifierOrQualifier
	: (typeSpecifier)=> typeSpecifier
        | typeQualifier
	;

/* 6.7
 * Root: INIT_DECLARATOR_LIST
 * Children: initDeclarator
 */
initDeclaratorList
	: i+=initDeclarator (COMMA i+=initDeclarator)*
	  -> ^(INIT_DECLARATOR_LIST $i+)
	;

/* 6.7
 * Root: INIT_DECLARATOR
 * Child 0: declarator
 * Child 1: initializer or ABSENT
 */
initDeclarator
	: d=declarator
	  (  -> ^(INIT_DECLARATOR $d ABSENT)
	  | (ASSIGN i=initializer) -> ^(INIT_DECLARATOR $d $i)
	  )
	;

/* 6.7.1 */
storageClassSpecifier
	: TYPEDEF {$DeclarationScope::isTypedef = true;}
	| (EXTERN | STATIC | THREADLOCAL | AUTO | REGISTER)
	;

/* 6.7.2 */
typeSpecifier
	: VOID | CHAR | SHORT | INT | LONG | FLOAT | DOUBLE
	| SIGNED | UNSIGNED | BOOL | COMPLEX
	| atomicTypeSpecifier
	| structOrUnionSpecifier
	| enumSpecifier
	| typedefName
	;

/* 6.7.2.1
 * Root: STRUCT or UNION
 * Child 0: IDENTIFIER (the tag) or ABSENT
 * Child 1: structDeclarationList or ABSENT
 */
structOrUnionSpecifier
	: structOrUnion
	    ( IDENTIFIER LCURLY structDeclarationList RCURLY
	      -> ^(structOrUnion IDENTIFIER structDeclarationList RCURLY)
	    | LCURLY structDeclarationList RCURLY
	      -> ^(structOrUnion ABSENT structDeclarationList RCURLY)
	    | IDENTIFIER
	      -> ^(structOrUnion IDENTIFIER ABSENT)
	    )
	;

/* 6.7.2.1 */
structOrUnion
	: STRUCT | UNION
	;

/* 6.7.2.1
 * Root: STRUCT_DECLARATION_LIST
 * Children: structDeclaration
 */
structDeclarationList
	: structDeclaration+
	  -> ^(STRUCT_DECLARATION_LIST structDeclaration+)
	;

/* 6.7.2.1
 * Two possible trees:
 *
 * Root: STRUCT_DECLARATION
 * Child 0: specifierQualifierList
 * Child 1: structDeclaratorList or ABSENT
 *
 * or
 *
 * staticAssertDeclaration (root: STATICASSERT)
 */
structDeclaration
scope DeclarationScope;
@init {
  $DeclarationScope::isTypedef = false;
  $DeclarationScope::scopeNames = new HashSet<String>();
}
    : s=specifierQualifierList
      ( -> ^(STRUCT_DECLARATION $s ABSENT)
      | structDeclaratorList
        -> ^(STRUCT_DECLARATION $s structDeclaratorList)
      )
      SEMI
    | staticAssertDeclaration
    ;

/* 6.7.2.1
 * Root: SPECIFIER_QUALIFIER_LIST
 * Children: typeSpecifierOrQualifier
 */
specifierQualifierList
    : typeSpecifierOrQualifier+
      -> ^(SPECIFIER_QUALIFIER_LIST typeSpecifierOrQualifier+)
    ;

/* 6.7.2.1
 * Root: STRUCT_DECLARATOR_LIST
 * Children: structDeclarator (at least 1)
 */
structDeclaratorList
    : s+=structDeclarator (COMMA s+=structDeclarator)*
      -> ^(STRUCT_DECLARATOR_LIST $s+)
    ;

/* 6.7.2.1
 * Root: STRUCT_DECLARATOR
 * Child 0: declarator or ABSENT
 * Child 1: constantExpression or ABSENT
 */
structDeclarator
    : declarator
      (  -> ^(STRUCT_DECLARATOR declarator ABSENT)
      | COLON constantExpression
         -> ^(STRUCT_DECLARATOR declarator constantExpression)
      )
    | COLON constantExpression
      -> ^(STRUCT_DECLARATOR ABSENT constantExpression)
    ;

/* 6.7.2.2
 * Root: ENUM
 * Child 0: IDENTIFIER (tag) or ABSENT
 * Child 1: enumeratorList
 */
enumSpecifier
    : ENUM 
        ( IDENTIFIER 
          -> ^(ENUM IDENTIFIER ABSENT)
        | IDENTIFIER LCURLY enumeratorList COMMA? RCURLY
          -> ^(ENUM IDENTIFIER enumeratorList)
        | LCURLY enumeratorList COMMA? RCURLY
          -> ^(ENUM ABSENT enumeratorList)
        )
    ;

/* 6.7.2.2
 * Root: ENUMERATOR_LIST
 * Children: enumerator
 */
enumeratorList
    : enumerator (COMMA enumerator)*
      -> ^(ENUMERATOR_LIST enumerator+)
    ;

/* 6.7.2.2
 * Root: ENUMERATOR
 * Child 0: IDENTIFIER
 * Child 1: constantExpression or ABSENT
 */
enumerator
	: IDENTIFIER
    	  {
    		$Symbols::enumerationConstants.add($IDENTIFIER.text);
		// System.err.println("define enum constant "+$IDENTIFIER.text);	
    	  }
    	  (  -> ^(ENUMERATOR IDENTIFIER ABSENT)
    	  | (ASSIGN constantExpression)
    	     -> ^(ENUMERATOR IDENTIFIER constantExpression)
    	  )
	;

/* 6.7.2.4 */
atomicTypeSpecifier
    : ATOMIC LPAREN typeName RPAREN
      -> ^(ATOMIC typeName)
    ;

/* 6.7.3 */
typeQualifier
    : CONST | RESTRICT | VOLATILE | ATOMIC
    | INPUT | OUTPUT
    ;

/* 6.7.4 */
functionSpecifier
    : INLINE | NORETURN 
    | ABSTRACT CONTIN LPAREN INTEGER_CONSTANT RPAREN 
      -> ^(ABSTRACT INTEGER_CONSTANT)
    ;

/* 6.7.5
 * Root: ALIGNAS
 * Child 0: TYPE or EXPR 
 * Child 1: typeName (if Child 0 is TYPE) or constantExpression
 *          (if Child 0 is EXPR)
 */
alignmentSpecifier
    : ALIGNAS LPAREN 
        ( typeName RPAREN
          -> ^(ALIGNAS TYPE typeName)
        | constantExpression RPAREN
          -> ^(ALIGNAS EXPR constantExpression)
        )
    ;

/* 6.7.6
 * Root: DECLARATOR
 * Child 0: pointer or ABSENT
 * Child 1: directDeclarator
 */
declarator
	: d=directDeclarator
	  -> ^(DECLARATOR ABSENT $d)
	| pointer d=directDeclarator
	  -> ^(DECLARATOR pointer $d)
	;

/* 6.7.6
 * Root: DIRECT_DECLARATOR
 * Child 0: directDeclaratorPrefix 
 * Children 1..: list of directDeclaratorSuffix (may be empty)
 */
directDeclarator
	: p=directDeclaratorPrefix
	  ( -> ^(DIRECT_DECLARATOR $p)
	  | s+=directDeclaratorSuffix+ ->^(DIRECT_DECLARATOR $p $s+)
	  )
	;

/*
 * Tree: either an IDENTIFIER or a declarator.
 */
directDeclaratorPrefix
	: IDENTIFIER 
		{
			if ($DeclarationScope::isTypedef) {
				$Symbols::types.add($IDENTIFIER.text);
				//System.err.println("define type "+$IDENTIFIER.text);
			}
		}
	| LPAREN! declarator RPAREN!
	;


directDeclaratorSuffix
	: directDeclaratorArraySuffix
	| directDeclaratorFunctionSuffix
	;

/*
 * Root: ARRAY_SUFFIX
 * child 0: LSQUARE (for source information)
 * child 1: STATIC or ABSENT
 * child 2: TYPE_QUALIFIER_LIST
 * child 3: expression (array extent), 
 *          "*" (unspecified variable length), or ABSENT
 * child 4: RSQUARE (for source information)
 */
directDeclaratorArraySuffix
	: LSQUARE
	  ( typeQualifierList_opt assignmentExpression_opt RSQUARE
	    -> ^(ARRAY_SUFFIX LSQUARE ABSENT typeQualifierList_opt
	         assignmentExpression_opt RSQUARE)
	  | STATIC typeQualifierList_opt assignmentExpression RSQUARE
	    -> ^(ARRAY_SUFFIX LSQUARE STATIC typeQualifierList_opt
	         assignmentExpression RSQUARE)
	  |   typeQualifierList STATIC assignmentExpression RSQUARE
	    -> ^(ARRAY_SUFFIX LSQUARE STATIC typeQualifierList
	         assignmentExpression RSQUARE)
	  |   typeQualifierList_opt STAR RSQUARE
	    -> ^(ARRAY_SUFFIX LSQUARE ABSENT typeQualifierList_opt
	         STAR RSQUARE)
	  )
	;

/*
 * Root: FUNCTION_SUFFIX
 * child 0: LPAREN (for source information)
 * child 1: either parameterTypeList or identifierList or ABSENT
 * child 2: RPAREN (for source information)
 */
directDeclaratorFunctionSuffix
	: LPAREN
	  ( parameterTypeList RPAREN 
	    -> ^(FUNCTION_SUFFIX LPAREN parameterTypeList  RPAREN)
	  | identifierList RPAREN
	    -> ^(FUNCTION_SUFFIX LPAREN identifierList RPAREN)
	  | RPAREN -> ^(FUNCTION_SUFFIX LPAREN ABSENT RPAREN)
	  )
	;

/*
 * Root: TYPE_QUALIFIER_LIST
 * Children: typeQualifier
 */
typeQualifierList_opt
	: typeQualifier* -> ^(TYPE_QUALIFIER_LIST typeQualifier*)
	;

/*
 * Tree: assignmentExpression or ABSENT
 */
assignmentExpression_opt
	:  -> ABSENT
	| assignmentExpression
	;

/* 6.7.6 
 * Root: POINTER
 * chilren: STAR
 */
pointer
    : pointer_part+ -> ^(POINTER pointer_part+)
    ;

/*
 * Root: STAR
 * child 0: TYPE_QUALIFIER_LIST
 * child 1 : SCOPE_MODIFIER or ABSENT
 */
pointer_part
	: STAR scopeUse_opt typeQualifierList_opt
	-> ^(STAR typeQualifierList_opt scopeUse_opt)
	;

/* 6.7.6
 * Root: TYPE_QUALIFIER_LIST
 * children: typeQualifier
 */
typeQualifierList
    : typeQualifier+ -> ^(TYPE_QUALIFIER_LIST typeQualifier+)
    ;

/* 6.7.6 
 * Root: PARAMETER_TYPE_LIST
 * child 0: parameterList (at least 1 parameter declaration)
 * child 1: ELLIPSIS or ABSENT
 * 
 * If the parameterTypeList occurs in a function prototype
 * (that is not part of a function definition), it defines
 * a new scope (a "function prototype scope").  If it occurs
 * in a function definition, it does not define a new scope. 
 */
 
parameterTypeList
	: {$Symbols::isFunctionDefinition}? parameterTypeListWithoutScope
	| parameterTypeListWithScope
	;

parameterTypeListWithScope
scope Symbols;
@init {
	$Symbols::types = new HashSet<String>();
	$Symbols::enumerationConstants = new HashSet<String>();
	$Symbols::scopeNames = new HashSet<String>();
	$Symbols::isFunctionDefinition = false;
}
	: parameterTypeListWithoutScope
	;

parameterTypeListWithoutScope
    : parameterList
      ( -> ^(PARAMETER_TYPE_LIST parameterList ABSENT)
      | COMMA ELLIPSIS
        -> ^(PARAMETER_TYPE_LIST parameterList ELLIPSIS)
      )
    ;

/* 6.7.6
 * Root: PARAMETER_LIST
 * children: parameterDeclaration
 */
parameterList
    : parameterDeclaration (COMMA parameterDeclaration)*
      -> ^(PARAMETER_LIST parameterDeclaration+)
    ;

/* 6.7.6 
 * Root: PARAMETER_DECLARATION
 * Child 0: declarationSpecifiers
 * Child 1: declarator, or abstractDeclarator, or ABSENT
 */
parameterDeclaration
scope DeclarationScope;
@init {
	$DeclarationScope::isTypedef = false;
	$DeclarationScope::scopeNames = new HashSet<String>();
}
    : declarationSpecifiers
      ( -> ^(PARAMETER_DECLARATION declarationSpecifiers ABSENT)
      | declaratorOrAbstractDeclarator
        -> ^(PARAMETER_DECLARATION
             declarationSpecifiers declaratorOrAbstractDeclarator)
      )
    ;


// this has non-LL* decision due to recursive rule invocations
// reachable from alts 1,2...  E.g., both can start with pointer.

declaratorOrAbstractDeclarator
	:	(declarator)=> declarator
	|	abstractDeclarator
	;
	

/* 6.7.6
 * Root: IDENTIFIER_LIST
 * children: IDENTIFIER (at least 1)
 */
identifierList
    : IDENTIFIER ( COMMA IDENTIFIER )*
      -> ^(IDENTIFIER_LIST IDENTIFIER+)
    ;

/* 6.7.6.  This is how a type is described without attaching
 * it to an identifier.
 * Root: TYPE_NAME
 * child 0: specifierQualifierList
 * child 1: abstractDeclarator or ABSENT
 */
typeName
    : specifierQualifierList
      ( -> ^(TYPE_NAME specifierQualifierList ABSENT)
      | abstractDeclarator
        -> ^(TYPE_NAME specifierQualifierList abstractDeclarator)
      )
    ;

/* 6.7.7.  Abstract declarators are like declarators without
 * the IDENTIFIER.
 *
 * Root: ABSTRACT_DECLARATOR
 * Child 0. pointer (may be ABSENT).  Some number of *s with possible
 *   type qualifiers.
 * Child 1. directAbstractDeclarator (may be ABSENT). 
 */
abstractDeclarator
    : pointer
      -> ^(ABSTRACT_DECLARATOR pointer ABSENT)
    | directAbstractDeclarator
      -> ^(ABSTRACT_DECLARATOR ABSENT directAbstractDeclarator)
    | pointer directAbstractDeclarator
      -> ^(ABSTRACT_DECLARATOR pointer directAbstractDeclarator)
    ;

/* 6.7.7
 *
 * Root: DIRECT_ABSTRACT_DECLARATOR
 * Child 0. abstract declarator or ABSENT.  
 * Children 1..: any number of direct abstract declarator suffixes
 *
 * Note that the difference between this and a directDeclarator
 * is that Child 0 of a direct declarator would be either
 * an IDENTIFIER or a declarator, but never ABSENT.
 */
directAbstractDeclarator
    : LPAREN abstractDeclarator RPAREN directAbstractDeclaratorSuffix*
      -> ^(DIRECT_ABSTRACT_DECLARATOR abstractDeclarator
           directAbstractDeclaratorSuffix*)
    | directAbstractDeclaratorSuffix+
      -> ^(DIRECT_ABSTRACT_DECLARATOR ABSENT directAbstractDeclaratorSuffix+)
    ;


/* 6.7.8 
 * Root: TYPEDEF_NAME
 * Child 0: IDENTIFIER
 *
 * Ambiguity: example:
 * typedef int foo;
 * typedef int foo;
 *
 * This is perfectly legal: you can define a typedef twice
 * as long as both definitions are equivalent.  However,
 * the first definition causes foo to be entered into the type name
 * table, so when parsing the second definition, foo is 
 * interpreted as a typedefName (a type specifier), and the
 * declaration would have empty declarator.   This is not
 * what you want, so you have to forbid it somehow.  I do this
 * by requiring that if you are "in" a typedef, a typedef name
 * cannot be immediately followed by a semicolon.  This is sound
 * because the C11 Standard requires at least one declarator
 * to be present in a typedef.
 */
typedefName
    : {isTypeName(input.LT(1).getText())}?
      IDENTIFIER
      scopeUse_opt
      -> ^(TYPEDEF_NAME IDENTIFIER scopeUse_opt)
    ;

/* 6.7.7
 * Two possibilities:
 *
 * Root: ARRAY_SUFFIX
 * Child 0: STATIC or ABSENT
 * Child 1: typeQualifierList or ABSENT
 * Child 2: expression or STAR or ABSENT
 *
 * Root: FUNCTION_SUFFIX
 * Child 0: parameterTypeList or ABSENT
 */
directAbstractDeclaratorSuffix
    : LSQUARE
      ( typeQualifierList_opt assignmentExpression_opt RSQUARE
        -> ^(ARRAY_SUFFIX LSQUARE ABSENT typeQualifierList_opt
             assignmentExpression_opt)
      | STATIC typeQualifierList_opt assignmentExpression RSQUARE
        -> ^(ARRAY_SUFFIX LSQUARE STATIC typeQualifierList_opt
             assignmentExpression)
      | typeQualifierList STATIC assignmentExpression RSQUARE
        -> ^(ARRAY_SUFFIX LSQUARE STATIC typeQualifierList assignmentExpression)
      | STAR RSQUARE
        -> ^(ARRAY_SUFFIX LSQUARE ABSENT ABSENT STAR)
      )
    | LPAREN
      ( parameterTypeList RPAREN
        -> ^(FUNCTION_SUFFIX LPAREN parameterTypeList RPAREN)
      | RPAREN
        -> ^(FUNCTION_SUFFIX LPAREN ABSENT RPAREN)
      )
    ;

/* 6.7.9 */
initializer
    : assignmentExpression -> ^(SCALAR_INITIALIZER assignmentExpression)
    | LCURLY initializerList
        (   RCURLY
        |   COMMA RCURLY
        )
      -> initializerList
    ;

/* 6.7.9 */
initializerList
    : designatedInitializer (COMMA designatedInitializer)*
      -> ^(INITIALIZER_LIST designatedInitializer+)
    ;

designatedInitializer
	: initializer
	  -> ^(DESIGNATED_INITIALIZER ABSENT initializer)
	| designation initializer
	  -> ^(DESIGNATED_INITIALIZER designation initializer)
	;

/* 6.7.9 */
designation
    : designatorList ASSIGN -> ^(DESIGNATION designatorList)
    ;

/* 6.7.9 */
designatorList
    : designator+
    ;

/* 6.7.9 */
designator
    : LSQUARE constantExpression RSQUARE
      -> ^(ARRAY_ELEMENT_DESIGNATOR constantExpression)
    | DOT IDENTIFIER
      -> ^(FIELD_DESIGNATOR IDENTIFIER)
    ;

/* 6.7.10 */
staticAssertDeclaration
    : STATICASSERT LPAREN constantExpression COMMA STRING_LITERAL
      RPAREN SEMI
      -> ^(STATICASSERT constantExpression STRING_LITERAL)
    ;


/* ***** A.2.3: Statements ***** */

/* 6.8 */
statement
    : labeledStatement
    | compoundStatement
    | expressionStatement
    | selectionStatement
    | iterationStatement
    | jumpStatement
    | pragma
    //| assertStatement
    | assumeStatement
    | waitStatement
    | whenStatement
    | chooseStatement
    | atomicStatement
    | datomicStatement
    ;

statementWithScope
scope Symbols;
@init {
	$Symbols::types = new HashSet<String>();
	$Symbols::scopeNames = new HashSet<String>();
	$Symbols::enumerationConstants = new HashSet<String>();
        $Symbols::isFunctionDefinition = false;
}
	: statement
	;

/* 6.8.1
 * Three possible trees:
 *
 * Root: IDENTIFIER_LABELED_STATEMENT
 * Child 0: IDENTIFIER
 * Child 1: statement
 *
 * Root: CASE_LABELED_STATEMENT
 * Child 0: CASE
 * Child 1: constantExpression
 * Child 2: statement
 *
 * Root: DEFAULT_LABELED_STATEMENT
 * Child 0: DEFAULT
 * Child 1: statement
 */
labeledStatement
    : IDENTIFIER COLON statement
      -> ^(IDENTIFIER_LABELED_STATEMENT IDENTIFIER statement)
    | CASE constantExpression COLON statement
      -> ^(CASE_LABELED_STATEMENT CASE constantExpression statement)
    | DEFAULT COLON statement
      -> ^(DEFAULT_LABELED_STATEMENT DEFAULT statement)
    ;

/* 6.8.2
 * Root: BLOCK
 * Child 0: LCURLY (for source information)
 * Child 1: blockItemList or ABSENT
 * Child 2: RCURLY (for source information)
 */
compoundStatement
scope Symbols;
@init {
	$Symbols::types = new HashSet<String>();
	$Symbols::scopeNames = new HashSet<String>();
	$Symbols::enumerationConstants = new HashSet<String>();
        $Symbols::isFunctionDefinition = false;
}
    : LCURLY
      ( RCURLY
        -> ^(COMPOUND_STATEMENT LCURLY ABSENT RCURLY)
      | blockItemList RCURLY
        -> ^(COMPOUND_STATEMENT LCURLY blockItemList RCURLY)
      )
    ;

/* 6.8.2 */
blockItemList
    : blockItem+ -> ^(BLOCK_ITEM_LIST blockItem+)
    ;

/* 6.8.2 */

/*
blockItemOLD
    : (declarationSpecifiers declarator declarationList_opt LCURLY)=>
      functionDefinition
    | declaration
    | statement
    ;
*/

// Overkill: create new DeclarationScope for every little
// statement???

blockItem
scope DeclarationScope;
@init {
  $DeclarationScope::isTypedef = false;
  $DeclarationScope::scopeNames = new HashSet<String>();
}
	: scopeListDef_opt!
		( (declarationSpecifiers declarator contract_opt
	   	    declarationList_opt LCURLY)=>
		  functionDefinition[(CommonTree)$scopeListDef_opt.tree]
		| declarationInScopeList[(CommonTree)$scopeListDef_opt.tree]
		) 
	| statement
	;


/* 6.8.3
 * Root: EXPRESSION_STATEMENT
 * Child 0: expression or ABSENT
 * Child 1: SEMI (for source information)
 */
expressionStatement
    : expression SEMI -> ^(EXPRESSION_STATEMENT expression SEMI)
    | SEMI -> ^(EXPRESSION_STATEMENT ABSENT SEMI)
    ;

/* 6.8.4
 * Two possible trees:
 *
 * Root: IF
 * Child 0: expression
 * Child 1: statement (true branch)
 * Child 2: statement or ABSENT (false branch)
 *
 * Root: SWITCH
 * Child 0: expression
 * Child 1: statement
 */
selectionStatement
scope Symbols;
@init {
	$Symbols::types = new HashSet<String>();
	$Symbols::scopeNames = new HashSet<String>();
	$Symbols::enumerationConstants = new HashSet<String>();
        $Symbols::isFunctionDefinition = false;
}
    : IF LPAREN expression RPAREN s1=statementWithScope
        ( (ELSE)=> ELSE s2=statementWithScope
          -> ^(IF expression $s1 $s2)
        | -> ^(IF expression $s1 ABSENT)
        )
    | SWITCH LPAREN expression RPAREN s=statementWithScope
      -> ^(SWITCH expression $s)
    ;

/* 6.8.5
 * Three possible trees:
 *
 * Root: WHILE
 * Child 0: expression
 * Child 1: statement
 *
 * Root: DO
 * Child 0: statement
 * Child 1: expression
 *
 * Root: FOR
 * Child 0: clause-1: declaration, expression, or ABSENT
 *          (for loop initializer)
 * Child 1: expression or ABSENT (condition)
 * Child 2: expression or ABSENT (incrementer)
 * Child 3: statement (body)
 *
 */
iterationStatement
scope Symbols;
@init {
	$Symbols::types = new HashSet<String>();
	$Symbols::scopeNames = new HashSet<String>();
	$Symbols::enumerationConstants = new HashSet<String>();
        $Symbols::isFunctionDefinition = false;
}
	: WHILE LPAREN expression RPAREN invariant_opt 
	  s=statementWithScope
	  -> ^(WHILE expression $s invariant_opt)
	| DO s=statementWithScope WHILE LPAREN expression RPAREN 
	  invariant_opt SEMI
	  -> ^(DO $s expression invariant_opt)
	| FOR LPAREN 
	  ( 
	    d=declaration e1=expression_opt SEMI e2=expression_opt
	    RPAREN i=invariant_opt s=statementWithScope
	    -> ^(FOR $d $e1 $e2 $s $i)
	  | e0=expression_opt SEMI e1=expression_opt SEMI
	    e2=expression_opt RPAREN i=invariant_opt
	    s=statementWithScope
	    -> ^(FOR $e0 $e1 $e2 $s $i)
	  )
	;

expression_opt
	:	expression
	|	-> ABSENT
	;

invariant_opt
	:	-> ABSENT
	|	INVARIANT LPAREN expression RPAREN
		-> ^(INVARIANT expression)
	;


/* 6.8.6
 * Four possible trees:
 *
 * Root: GOTO
 * Child 0: IDENTIFIER
 * Child 1: SEMI (for source information)
 *
 * Root: CONTINUE
 * Child 0: SEMI (for source information)
 * 
 * Root: BREAK
 * Child 0: SEMI (for source information)
 *
 * Root: RETURN
 * Child 0: expression or ABSENT 
 * Child 1: SEMI (for source information)
 */
jumpStatement
    : GOTO IDENTIFIER SEMI -> ^(GOTO IDENTIFIER SEMI)
    | CONTINUE SEMI -> ^(CONTINUE SEMI)
    | BREAK SEMI -> ^(BREAK SEMI)
    | RETURN expression_opt SEMI -> ^(RETURN expression_opt SEMI)
    ;

/*
 * Root: PRAGMA
 * child 0: IDENTIFIER (first token following # pragma)
 * child 1: TOKEN_LIST (chilren are list of tokens following identifier)
 * child 2: NEWLINE (character which ends the pragma)
 */
pragma	:	PRAGMA IDENTIFIER pragmaBody NEWLINE
		-> ^(PRAGMA IDENTIFIER ^(TOKEN_LIST pragmaBody) NEWLINE)
	;

pragmaBody
	:	(~ NEWLINE)*
	;
/*
assertStatement
	:	ASSERT expression SEMI -> ^(ASSERT expression)
	;*/

assumeStatement
	:	ASSUME expression SEMI -> ^(ASSUME expression)
	;

waitStatement
	:	WAIT expression SEMI -> ^(WAIT expression)
	;

whenStatement
	:	WHEN LPAREN expression RPAREN statement
		-> ^(WHEN expression statement)
	;

chooseStatement
	:	CHOOSE LCURLY statement+ RCURLY 
		-> ^(CHOOSE statement+)
	;

// general atomic block "$atomic{...} or $atomic statement;"
atomicStatement
	:	CIVLATOMIC s=statementWithScope
		-> ^(CIVLATOMIC $s)
	;

// deterministic atomic block "$datomic{...} or $datomic statement;"
datomicStatement
	:	CIVLATOM s=statementWithScope
		-> ^(CIVLATOM $s)
	;

/* ***** A.2.4: External Definitions ***** */

/* 6.9
 * Root: TRANSLATION_UNIT
 * Children: externalDeclaration
 */
translationUnit
scope Symbols; // the global scope
scope DeclarationScope; // just to have an outermost one with isTypedef false
@init {
    $Symbols::types = new HashSet<String>();
    $Symbols::scopeNames = new HashSet<String>();
    $Symbols::enumerationConstants = new HashSet<String>();
    $Symbols::isFunctionDefinition = false;
    $DeclarationScope::isTypedef = false;
    $DeclarationScope::scopeNames = new HashSet<String>();
}
	:	externalDeclaration* EOF
		-> ^(TRANSLATION_UNIT externalDeclaration*)
	;

/* 6.9
 * Need to look ahead to distinguish function definition from
 * a declaration.  As soon as you see the "{", you know you 
 * are in a function definition.
 */
externalDeclaration
scope DeclarationScope;
@init {
  $DeclarationScope::isTypedef = false;
  $DeclarationScope::scopeNames = new HashSet<String>();
}
	: scopeListDef_opt!
		( (declarationSpecifiers declarator contract_opt
	   	    declarationList_opt LCURLY)=>
		  functionDefinition[(CommonTree)$scopeListDef_opt.tree]
		| declarationInScopeList[(CommonTree)$scopeListDef_opt.tree]
		) 
	| pragma
	| assumeStatement
	;


/* 6.9.1
 *
 * Takes as argument a scope list tree s, which may be ABSENT.
 *
 * Root: FUNCTION_DEFINITION
 * Child 0: declarationSpecifiers
 * Child 1: declarator
 * Child 2: declarationList or ABSENT (formal parameters)
 * Child 3: compound statement (body)
 * Child 4: contract or ABSENT (code contract)
 * Child 5: scope parameter list or ABSENT (scope formal params)
 */
functionDefinition[CommonTree s]
scope Symbols; // "function scope"
@init {
    $Symbols::types = new HashSet<String>();
    $Symbols::enumerationConstants = new HashSet<String>();
    $Symbols::scopeNames = new HashSet<String>();
    $Symbols::isFunctionDefinition = true;
}
	: declarationSpecifiers
	  declarator
	  contract_opt
	  declarationList_opt
	  compoundStatement
	  -> ^(FUNCTION_DEFINITION declarationSpecifiers declarator
	       declarationList_opt compoundStatement contract_opt
	       {$s})
	;


/* 6.9.1
 * Root: DECLARATION_LIST
 * Children: declaration (any number)
 */
declarationList_opt
	: declaration* -> ^(DECLARATION_LIST declaration*)
	;

contractItem
	: REQUIRES expression SEMI -> ^(REQUIRES expression)
	| ENSURES expression SEMI -> ^(ENSURES expression)
	;

contract_opt
	: contractItem* -> ^(CONTRACT contractItem*)
	;
