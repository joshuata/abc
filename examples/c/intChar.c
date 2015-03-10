#include<string.h>
#include<assert.h>

int main(){
  char a[2];

  memset(a, 0, 2);
  for(int i=0; i<2; i++)
    assert(a[i] == 0);
}
