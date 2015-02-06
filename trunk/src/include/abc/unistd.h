#ifdef __UNISTD__
#else
#define __UNISTD__

int sleep(int n){
  return 0;
}

int usleep(int n){
  return 0;
}
#endif
