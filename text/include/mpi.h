/* ABC model of mpi.h.  This is just a bare-bones beginning of the most basic
 * MPI primitives, just to get things rolling. */

#include<stdlib.h>
 
/* Communicators (type MPI_Comm): */
#define MPI_COMM_WORLD 1

/* Ranks and tags (type int): */

#define MPI_ANY_SOURCE -1
#define MPI_ANY_TAG -2
#define MPI_PROC_NULL -3

/* Operations (type MPI_Op): */
#define MPI_SUM 1
#define MPI_MAX 2
#define MPI_MIN 3

/* Datatypes (type MPI_Datatype): */
#define MPI_INT 1
#define MPI_FLOAT 2
#define MPI_DOUBLE 3
#define MPI_CHAR 4
#define MPI_BYTE 5


/********************************* Types **********************************************/

typedef int MPI_Comm;

typedef int MPI_Op;

typedef int MPI_Datatype;

typedef  struct MPIX_Comm_Record {
  int id;
} MPIX_Comm_Record;

typedef MPIX_Comm_Record *MPI_Request;

typedef struct MPI_Status {
  int MPI_SOURCE;
  int MPI_TAG;
  int SIZE;
} MPI_Status;

/********************************* Constants *******************************************/


/* Communication requests (type MPI_Request): */

#define MPI_REQUEST_NULL (MPIX_Comm_Record*)NULL

/* Status objects (type MPI_Status and MPI_Status*): */

#define MPI_STATUS_IGNORE (MPI_Status*)NULL

#define MPI_STATUSES_IGNORE (MPI_Status*)NULL

/*************************************** Functions ***************************************/

int MPI_Init(int *argc, char* (*argv)[]);

int MPI_Finalize();

int MPI_Comm_rank(MPI_Comm comm, int *rank);

int MPI_Comm_size(MPI_Comm comm, int *size);

int MPI_Isend(void* buf, int count, MPI_Datatype datatype, int dest, int tag, 
              MPI_Comm comm, MPI_Request *request);

int MPI_Irecv(void* buf, int count, MPI_Datatype datatype, int source, int tag, 
              MPI_Comm comm, MPI_Request *request);

int MPI_Wait(MPI_Request *request, MPI_Status *status);

int MPI_Waitall(int count, MPI_Request *array_of_requests, 
		MPI_Status *array_of_statuses);

int MPI_Send(void* buf, int count, MPI_Datatype datatype, int dest, int tag,
             MPI_Comm comm);

int MPI_Recv(void* buf, int count, MPI_Datatype datatype, int source, int tag,
             MPI_Comm comm, MPI_Status *status);

int MPI_Sendrecv(void *sendbuf, int sendcount, MPI_Datatype sendtype, int dest,
                 int sendtag, void *recvbuf, int recvcount,
                 MPI_Datatype recvtype, int source, int recvtag, MPI_Comm comm,
                 MPI_Status *status);

int MPI_Sendrecv_replace(void *buf, int count, MPI_Datatype datatype, 
                         int dest, int sendtag, int source, int recvtag,
                         MPI_Comm comm, MPI_Status *status);

int MPI_Get_count(MPI_Status *status, MPI_Datatype datatype, int *count);

int MPI_Barrier(MPI_Comm comm);

int MPI_Bcast(void* buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm );

int MPI_Reduce(void* sendbuf, void* recvbuf, int count, MPI_Datatype datatype,
               MPI_Op op, int root, MPI_Comm comm);

int MPI_Allreduce(void* sendbuf, void* recvbuf, int count, MPI_Datatype datatype,
                  MPI_Op op, MPI_Comm comm);
                  
int MPI_Type_contiguous(int count, MPI_Datatype old_type,
                        MPI_Datatype *new_type_p);

int MPI_Type_commit(MPI_Datatype *datatype);
                      
int MPI_Type_free(MPI_Datatype *datatype);

double MPI_Wtime( void );
