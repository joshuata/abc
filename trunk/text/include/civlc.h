/* This header file defines standard types and provides
 * function prototypes used in the CIVL-C language.
 */
 
#ifdef __CIVLC__
#else
#define __CIVLC__

typedef unsigned long int size_t;
 
/* The CIVL-C process reference type */ 
typedef int $proc;

/* The CIVL-C heap type, used to represent a heap */
typedef int $heap;

/* The CIVL-C malloc function, which takes a reference to a heap */
void* $malloc($heap *h, int size);

/* Copies a region of memory, just as in standard C */
void memcpy(void *p, void *q, size_t size);

/* The CIVL-C de-allocation function, which takes a reference to a heap */
void $free($heap *h, void *p);

#endif
