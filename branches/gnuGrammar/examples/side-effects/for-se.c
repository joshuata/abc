
int f(int *x) {
  return (*x)++;
}

void main() {
  int y=0;

  for (int i=0; i++<10; f(&y)--) {
    y=2*y;
  }
}
