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
	UNIQUE_FOR;
	UNIQUIE_PARLLEL;
	DATA_CLAUSE;
}

/* ANTLR 3.4 doesn't allow redefinition of headers in composite grammars.
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
  : ORDERED ->^(UNIQUE_FOR ORDERED)
  | s1=schedule_clause -> ^(UNIQUE_FOR $s1)
  | c=collapse_clause -> ^(UNIQUE_FOR $c)
  ;
  
schedule_clause
	: SCHEDULE LPAREN s=schedule_kind RPAREN
	  -> ^(SCHEDULE $s)
    | SCHEDULE LPAREN s1=schedule_kind COMMA e=expression RPAREN
      -> ^(SCHEDULE $s1 $e)
	;
	
collapse_clause
	:
	COLLAPSE LPAREN i=INTEGER_CONSTANT RPAREN
    -> ^(COLLAPSE $i)
	;

schedule_kind
  : STATIC -> ^(STATIC)
  | DYNAMIC -> ^(DYNAMIC)
  | GUIDED -> ^(GUIDED)
  | RUNTIME -> ^(RUNTIME)
  ;

unique_parallel_clause
  : i=if_clause 
    -> ^(UNIQUIE_PARLLEL $i)
  | n=num_threads_clause 
    -> ^(UNIQUIE_PARLLEL $n)
  ;
  
if_clause
  : IF LPAREN e1=expression RPAREN
    -> ^(IF $e1)
  ;
  
num_threads_clause
  : NUM_THREADS LPAREN e2=expression RPAREN
    -> ^(NUM_THREADS $e2)
  ;

data_clause
  : d1=private_clause
    -> ^(DATA_CLAUSE $d1)
  | d2=firstprivate_clause
    -> ^(DATA_CLAUSE $d2)
  | d3=lastprivate_clause
    -> ^(DATA_CLAUSE $d3)
  | d4=shared_clause
    -> ^(DATA_CLAUSE $d4)
  | d5=default_clause
    -> ^(DATA_CLAUSE $d5)
  | d6=reduction_clause
    -> ^(DATA_CLAUSE $d6)
  | d7=copyin_clause
    -> ^(DATA_CLAUSE $d7)
  | d8=copyprivate_clause
    -> ^(DATA_CLAUSE $d8)
  ;
  
private_clause
  : PRIVATE WS* LPAREN WS* i1=identifier_list WS* RPAREN 
    -> ^(PRIVATE $i1)
  ;
  
firstprivate_clause
  : FST_PRIVATE WS* LPAREN WS* i2=identifier_list WS* RPAREN
    -> ^(FST_PRIVATE $i2)
  ;
  
lastprivate_clause
  : LST_PRIVATE WS* LPAREN WS* i3=identifier_list WS* RPAREN
    -> ^(LST_PRIVATE $i3)
  ;
  
shared_clause
  : SHARED WS* LPAREN WS* i4=identifier_list WS* RPAREN
    -> ^(SHARED $i4)
  ;
  
default_clause
  : DEFAULT WS* LPAREN WS* SHARED WS* RPAREN
    -> ^(DEFAULT SHARED)
  | DEFAULT WS* LPAREN WS* NONE WS* RPAREN
    -> ^(DEFAULT NONE)
  ;
  
reduction_clause
  : REDUCTION WS* LPAREN WS* r=reduction_operator WS* COLON WS* i5=identifier_list WS* RPAREN
    -> ^(REDUCTION $r $i5)
  ;
  
copyin_clause
  : COPYIN WS* LPAREN WS* i6=identifier_list WS* RPAREN
    -> ^(COPYIN $i6)
  ;
  
copyprivate_clause
  : COPYPRIVATE WS* LPAREN WS* i7=identifier_list WS* RPAREN
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
  