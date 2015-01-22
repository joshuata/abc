#ifdef __BAR__
#else
#define __BAR__

typedef struct foo foo;
struct foo {
  foo *next;
};     

#endif
