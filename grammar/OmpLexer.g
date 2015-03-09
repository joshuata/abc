lexer grammar OmpLexer;

options 
{
     tokenVocab=CivlCParser;
}

import PreprocessorLexer;

/* OpenMP keywords */
BARRIER		:	'barrier'		NotLineStart;
COLLAPSE	:	'collapse' 		NotLineStart;
COPYIN		:	'copyin'		NotLineStart;
COPYPRIVATE	:	'copyprivate'	NotLineStart;
CRITICAL	:	'critical'		NotLineStart;
DEFAULT		:	'default'		NotLineStart;
DYNAMIC		:	'dynamic'		NotLineStart;
FST_PRIVATE	:	'firstprivate'	NotLineStart;
FLUSH		:	'flush'			NotLineStart;
GUIDED		:	'guided'		NotLineStart;
LST_PRIVATE	:	'lastprivate'	NotLineStart;
MASTER		:	'master'		NotLineStart;
NONE		:	'none'			NotLineStart;
NOWAIT		:	'nowait'		NotLineStart;
NUM_THREADS	:	'num_threads'	NotLineStart;
ORDERED		:	'ordered'		NotLineStart;
PARALLEL	:	'parallel'		NotLineStart;
PRIVATE		:	'private'		NotLineStart;
REDUCTION	:	'reduction'		NotLineStart;
RUNTIME		:	'runtime'		NotLineStart;
SCHEDULE	:	'schedule'		NotLineStart;
SECTIONS	:	'sections'		NotLineStart;
SECTION		:	'section'		NotLineStart;
SHARED		:	'shared'		NotLineStart;
SINGLE		: 	'single'		NotLineStart;
STATIC		:	'static'		NotLineStart;
THD_PRIVATE	:	'threadprivate'	NotLineStart;
