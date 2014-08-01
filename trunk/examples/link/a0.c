/* a0.c.  To be linked with a1.c and a2.c.  In this translation unit,
 * struct S is complete */
struct S {int x;};
int f(struct S *p) {
  return p->x + 1;
}
