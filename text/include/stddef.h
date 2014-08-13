/* The stddef.h header defines various variable types and macros. 
Many of these definitions also appear in other headers. */
#ifdef __STDDEF__
#else
#define __STDDEF__
typedef unsigned long int size_t;
typedef signed long int ptrdiff_t;

#define NULL ((void*)0)
#endif
