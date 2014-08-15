#ifdef __MPI__
#else
#define __MPI__

typedef enum {
    MPIX_NO_OP,
    MPI_MAX, 
    MPI_MIN,     
    MPI_SUM,
    MPI_PROD,
    MPI_LAND,   
    MPI_BAND,   
    MPI_LOR,     
    MPI_BOR,     
    MPI_LXOR,    
    MPI_BXOR,    
    MPI_MINLOC,  
    MPI_MAXLOC,  
    MPI_REPLACE
} MPI_Op;

#include <mpi-common.h>
#endif
