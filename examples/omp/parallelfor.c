#include <omp.h>

#define N 10

int main (int argc, char *argv[]) {
  
  double a[N], b[N], c[N][N];
  int i, sum;
  
  c[0][0] = 1.0;

#pragma omp parallel for
    for (i=0; i < N; i++)
      a[i] = 0.0;
      
#pragma omp parallel for
    for (i=0; i < N-1; i++)
      b[i+1] = a[i+1] + 2*i;
      
#pragma omp parallel for private(sum)
    for (i=0; i < N; i++)
      sum = sum + i;
      
#pragma omp parallel for 
    for (i=0; i < N; i++)
      sum = sum + i;
      
    return a[0]+b[0]+sum+c[0][0];
}
