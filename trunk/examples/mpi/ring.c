#include<mpi.h>
int myrank;
int nprocs;
int left;
int right;

void main() {
    int argc;
    char **argv;
    int i;
	
    //MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
    left = (myrank+nprocs-1)%nprocs;
    right = (myrank+nprocs+1)%nprocs;
    i=0;
    if (myrank%2==0) {
        MPI_Send(NULL, 0, MPI_INT, right, 0, MPI_COMM_WORLD);
        MPI_Recv(NULL, 0, MPI_INT, left, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    } else {
        MPI_Recv(NULL, 0, MPI_INT, left, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Send(NULL, 0, MPI_INT, right, 0, MPI_COMM_WORLD);
    }
    MPI_Finalize();
}
