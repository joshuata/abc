#ifndef __SVCOMP__
#define __SVCOMP__
#include<gnuc.h>

/**************************** macros *****************************/
#define __const const
#define __inline inline
#define __inline__ inline
#define __restrict restrict
#define __thread _Thread_local
#define __extension__
#define __asm__(X)
// #define __attribute__(X) this couldn't work here
#define __signed__ signed
#define __volatile volatile
#define __PRETTY_FUNCTION__ (void*)0


/**************************** types *****************************/
typedef unsigned long int size_t;


/**************************** functions *****************************/

void __VERIFIER_error(void);
void __VERIFIER_assume(int);
void __VERIFIER_assert(int);
extern int printf(const char * restrict format, ...);
extern void* malloc(size_t size);
extern void assert(_Bool);
extern void assume(_Bool);


/*
__VERIFIER_nondet_X(): In order to model nondeterministic values, 
the following functions can be assumed to return an arbitrary value of 
the indicated type: __VERIFIER_nondet_X() (and nondet_X(), deprecated) 
with X in {bool, char, int, float, loff_t, long, pchar, pointer, pthread_t, 
sector_t, short, size_t, u32, uchar, uint, ulong, unsigned, ushort} 
(no side effects, pointer for void *, etc.). The verification tool can 
assume that the functions are implemented according to the following template:
X __VERIFIER_nondet_X() { X val; return val; }
*/
int __VERIFIER_nondet_int(void);
unsigned int __VERIFIER_nondet_uint(void);
void* __VERIFIER_nondet_pointer(void);
int __VERIFIER_nondet_bool(void);
#endif
