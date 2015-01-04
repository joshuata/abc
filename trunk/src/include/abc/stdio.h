/* stdio.h: The ABC representation of standard C library stdio.
 * Based on C11 Standard.
 */
#ifndef __STDIO__
#define __STDIO__

/* Needed from stdarg.h: */

#include <stdarg.h>

/* Types */

typedef unsigned long int size_t;

typedef struct FILE FILE;

typedef int fpos_t;

/* Macros */

#define NULL ((void*)0)
#define _IOFBF 1
#define _IOLBF 2
#define _IONBF 3
#define BUFSIZ 100
#define EOF (-100)
#define FOPEN_MAX 100
#define FILENAME_MAX 500
#define L_tmpnam 500
#define SEEK_CUR 1
#define SEEK_END 2
#define SEEK_SET 3
#define TMP_MAX 100
#define stdin (FILE*)0
#define stdout (FILE*)1
#define stderr (FILE*)2

/* Functions */

#include<stdio-common.h>

#endif
