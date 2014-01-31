// a is initialized using a compound literal to define
// a literal array.  The array is converted to pointer to int.
void main() {
  int *a = (int[]){ [2] = 3 };
}
