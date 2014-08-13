/*
 * test2_0.c and test2_1.c are not equivalent because 
 * they have different initial value for the variable b.
*/

void main(){
  int a = 10;
  int b = 1;

  a = a + 1;
  a = a + b;
}
