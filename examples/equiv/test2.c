/*
 * test1.c and test2.c become equivalent after pruner is applied.
*/

#include<stdio.h>

int f(int k){
  return k;
}

int main(){
  for(int i = 0; i < 100; i++){
    printf("Current i is %d.\n", i);
  }
}
