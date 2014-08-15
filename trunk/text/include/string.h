/* ABC model of string.h */

#ifdef __STRING__
#else
#define __STRING__

#include <stddef.h>

int strcmp(const char *s1, const char *s2);

/* copies n characters from the object pointed to by s2 into the 
   object pointed to by s1. If copying takes place between objects that overlap, 
   the behavior is undefined. */
void* memcpy(void *p, void *q, const size_t size);

/* copies the string pointed to by s2 (including the terminating null character) 
   into the array pointed to by s1. If copying takes place between objects that 
   overlap, the behavior is undefined. */
char *strcpy(char * restrict s1, const char * restrict s2);

/* computes the length of the string pointed to by s. */
size_t strlen(const char *s);

#endif
