/* a1.c.  To be linked with a0.c and a2.c  In this translation unit,
 * struct S is incomplete, and is therefore compatible with the
 * struct S in a0 and the struct S in a2, even though struct S
 * in a0 is incompatible with that in a2.
 */
#include <stdio.h>
struct S;
int f(struct S *p); // defined in a0.c
void h(struct S *p) { // called from a2.c
  printf("%d\n", f(p));
  fflush(stdout);
}
