parser grammar OmpParser;

options
{
	language=Java;
	tokenVocab=OmpLexer;
	output=AST;
}

import CivlCParser;

tokens
{
	IDENTIFIER_LIST;	
	PARALLEL_FOR;
	PARALLEL_SECTIONS;
}

/* ANTLR 3.4 doesn't allow redefinition of headers in compositive grammars.
Our solution for this is: add the header (package, imported package)
to the generated java file in ant.
@header
{
package edu.udel.cis.vsl.abc.parse.common;
}*/

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
}

// openMP grammar :  a bit old
//  missing some things, e.g., collapse, block, ...
  
openmp_construct
  : 
    parallel_for_directive
  | parallel_sections_directive
  | parallel_directive
  | for_directive
  | sections_directive
  | single_directive
  | master_directive
  | critical_directive
  | atomic_directive
  | ordered_directive
  | section_directive
  | barrier_directive 
  | flush_directive
  | threadprivate_directive
  ;

parallel_directive
  : PARALLEL WS* p+=parallel_clause*
  -> ^(PARALLEL $p*)
  ;

parallel_clause
  : unique_parallel_clause
  | data_clause
  ;
  
master_directive
  : MASTER -> ^(MASTER)
  ;

critical_directive
  : CRITICAL WS* r=region_phrase?
  -> ^(CRITICAL $r?)
  ;
  
sections_directive
  : SECTIONS WS* s+=sections_clause*
  -> ^(SECTIONS $s*)
  ;

sections_clause
  : data_clause
  | NOWAIT -> ^(NOWAIT)
  ;

section_directive
  : SECTION -> ^(SECTION)
  ;
  
parallel_for_directive
  : PARALLEL WS+ FOR WS* p+=parallel_for_clause*
    -> ^(PARALLEL_FOR $p*)
  ;

parallel_for_clause
  : unique_parallel_clause
  | unique_for_clause
  | data_clause
  ;

parallel_sections_directive
  : PARALLEL WS+ SECTIONS WS* p+=parallel_sections_clause*
    -> ^(PARALLEL_SECTIONS $p*)
  ;

parallel_sections_clause
  : unique_parallel_clause
  | data_clause
  ;

single_directive
  : SINGLE WS* s+=single_clause*
    -> ^(SINGLE $s*)
  ;

single_clause
  : data_clause
  | NOWAIT
  ;

region_phrase
  : LPAREN WS* IDENTIFIER WS* RPAREN
    -> ^(IDENTIFIER)
  ;

barrier_directive
  : BARRIER -> ^(BARRIER)
  ;

atomic_directive
  : ATOMIC -> ^(ATOMIC)
  ;

flush_directive
  : FLUSH WS* f=flush_vars?
    -> ^(FLUSH $f?)
  ;

flush_vars
  : LPAREN WS*  i=identifier_list WS* RPAREN
    -> ^(IDENTIFIER_LIST $i)
  ;

ordered_directive
  : ORDERED -> ^(ORDERED)
  ;

threadprivate_directive
  : THD_PRIVATE WS* LPAREN WS* i=identifier_list WS* RPAREN
    -> ^(THD_PRIVATE $i)
  ;

for_directive
  : FOR f+=for_clause*
    -> ^(FOR $f*)
  ;

for_clause
  : unique_for_clause
  | data_clause
  | NOWAIT ->^(NOWAIT)
  ;

unique_for_clause
  : ORDERED ->^(ORDERED)
  | SCHEDULE LPAREN s=schedule_kind RPAREN
    -> ^(SCHEDULE $s)
  | SCHEDULE LPAREN s1=schedule_kind COMMA e=expression RPAREN
    -> ^(SCHEDULE $s1 $e)
  | COLLAPSE LPAREN i=INTEGER_CONSTANT RPAREN
    -> ^(COLLAPSE $i)
  ;

schedule_kind
  : STATIC -> ^(STATIC)
  | DYNAMIC -> ^(DYNAMIC)
  | GUIDED -> ^(GUIDED)
  | RUNTIME -> ^(RUNTIME)
  ;

unique_parallel_clause
  : IF LPAREN e1=expression RPAREN
    -> ^(IF $e1)
  | NUM_THREADS LPAREN e2=expression RPAREN
    -> ^(NUM_THREADS $e2)
  ;

data_clause
  : PRIVATE WS* LPAREN WS* i1=identifier_list WS* RPAREN 
    -> ^(PRIVATE $i1)
  | FST_PRIVATE WS* LPAREN WS* i2=identifier_list WS* RPAREN
    -> ^(FST_PRIVATE $i2)
  | LST_PRIVATE WS* LPAREN WS* i3=identifier_list WS* RPAREN
    -> ^(LST_PRIVATE $i3)
  | SHARED WS* LPAREN WS* i4=identifier_list WS* RPAREN
    -> ^(SHARED $i4)
  | DEFAULT WS* LPAREN WS* SHARED WS* RPAREN
    -> ^(DEFAULT SHARED)
  | DEFAULT WS* LPAREN WS* NONE WS* RPAREN
    -> ^(DEFAULT NONE)
  | REDUCTION WS* LPAREN WS* r=reduction_operator WS* COLON WS* i5=identifier_list WS* RPAREN
    -> ^(REDUCTION $r $i5)
  | COPYIN WS* LPAREN WS* i6=identifier_list WS* RPAREN
    -> ^(COPYIN $i6)
  | COPYPRIVATE WS* LPAREN WS* i7=identifier_list WS* RPAREN
    -> ^(COPYPRIVATE $i7)
  ;

reduction_operator
  : PLUS -> ^(PLUS)
  | STAR -> ^(STAR)
  | SUB -> ^(SUB)
  | AMPERSAND -> ^(AMPERSAND)
  | BITXOR -> ^(BITXOR)
  | BITOR -> ^(BITOR)
  | AND -> ^(AND)
  | OR -> ^(OR)
  | IDENTIFIER -> ^(IDENTIFIER)
  ;

identifier_list
  :
  i1=IDENTIFIER (WS* COMMA WS* i2+=IDENTIFIER)* 
  -> ^(IDENTIFIER_LIST $i1 $i2*)
  ;
  