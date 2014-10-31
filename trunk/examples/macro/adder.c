#include<stdio.h>
#ifdef _CIVL
#include <civlc.cvh>
$input int N;
$assume 0 < N && N < 10;
#else
#define N 100
#endif

int input[N];
int sum = 0;

int main(){
  for(int i = 0; i < N; i++)
    input[i] = i;
  for(int i = 0; i < N; i++)
    sum += input[i];
#ifdef _CIVL
  $assert sum == N*(N-1)/2;
#endif
  printf("N = %d, sum = %d\n", N, sum);
  fflush(stdout);
  return 0;
}
