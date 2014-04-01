#ifdef __MPI__
#else
  #define __MPI__
  #ifdef __MPI_COMMON__
  #else
    #include<mpi-common.h>
  #endif
#endif
