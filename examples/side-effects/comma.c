#include <stdio.h>

int * f() {
        return NULL;
}

int main() {
  int * p;
  int a[10];

  *(&p) = f(), &a[11];
  *p = *(f()), 5/0;
  *p=1, a[1]=1, a[2]=2;
}
