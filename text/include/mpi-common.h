#ifdef __MPI_COMMON__
#else
  #define __MPI_COMMON__  
  #ifdef __CIVLC__
  #else
    #include<civlc.h>
  #endif

/********************************* Types **********************************************/
/* Datatypes (type MPI_Datatype) */
typedef enum {
  MPI_INT,
  MPI_FLOAT,
  MPI_DOUBLE,
  MPI_CHAR
} MPI_Datatype;

/* Operators (type MPI_Op) */
typedef enum {
  MPI_SUM,
  MPI_MAX,
  MPI_MIN
} MPI_Op;

typedef struct {
  int MPI_SOURCE;
  int MPI_TAG;
  int MPI_ERROR;
  int size;
} MPI_Status;

struct __MPI_Comm_record {
  int id;
  // TODO: will add a lot more fields here
};
typedef struct __MPI_Comm_record *MPI_Request;

/********************************* Constants *******************************************/

/* Ranks and tags (type int): */

#define MPI_ANY_SOURCE -1
#define MPI_ANY_TAG -2
#define MPI_PROC_NULL -3
#define MPI_SUCCESS 0

#define BCAST_TAG 999
#define REDUCE_TAG 998

#define MPI_STATUS_IGNORE NULL
#define MPI_STATUSES_IGNORE NULL

/********************************* Communicator *******************************************/

typedef enum __MPI_Status__ {
  __UNINIT,
  __INIT,
  __FINALIZED
} __MPI_Status__;

/* Definition of CMPI_Gcomm and MPI_Comm */
typedef struct CMPI_Gcomm {
  $gcomm p2p; // point-to-point communication
  $gcomm col; // collective communication
} CMPI_Gcomm;

typedef struct MPI_Comm {
  $comm p2p; // point-to-point communication
  $comm col; // collective communication
  __MPI_Status__ status;
} MPI_Comm;

MPI_Comm MPI_COMM_WORLD;

/********************************* Functions *******************************************/

int MPI_Init(int *argc, char *** argv);

int __MPI_Init(MPI_Comm* comm);

int MPI_Finalize(void);

int __MPI_Finalize(MPI_Comm* comm);

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

double MPI_Wtime(void);

/******************************* CIVL Communicator functions  *************************/
CMPI_Gcomm CMPI_Gcomm_create($scope scope, int size);

MPI_Comm MPI_Comm_create($scope scope, CMPI_Gcomm gc, int rank);

void CMPI_Gcomm_destroy(CMPI_Gcomm gc);

void MPI_Comm_destroy(MPI_Comm comm);

#endif
