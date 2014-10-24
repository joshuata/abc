#ifdef CIVL_PROG
#include <civlc.cvh>
$input int N;
$assume 0 < N && N < 10;
#else
#define N NVALUE
#endif

int input[N];
int sum = 0;

void main(){

for(int i = 0; i < N; i++)
  input[i] = i;
for(int i = 0; i < N; i++)
  sum == input[i];
#ifdef CIVL_PROG
$assert sum == N*(N-1)/2;
#endif
}

