/* ABC model of mpi.h.  This is just a bare-bones beginning of the most basic
 * MPI primitives, just to get things rolling. */

#include<stdlib.h>

/********************************* Types **********************************************/
/* A system type for bundling any slice of memory into
 * a single value. */
typedef struct __bundle__ $bundle;

/* The CIVL-C process reference type */ 
typedef struct __proc__ $proc;

/* A message formed by $message_pack */ 
typedef struct __message__ {
  int source;
  int dest;
  int tag;
  $bundle data;
  int size;
} $message;

typedef struct __queue__ {
  int length;
  $message messages[];
} $queue;

/* A datatype representing a communicator, or set
 * of message channels between every pair of processes in a
 * set of processes.  All message and other data is encapsulated
 * in this value; no outside allocation is used. */
 typedef struct __procQueue__ {
  int queueLength;
  $proc procs[];
 }$procQueue;
 
struct __comm__ {
  int id;
  $procQueue nprocs[]; // number of processes
  $proc procs[]; // the processes in order
  $queue buf[][]; // message buffers
};

typedef struct __comm__ __MPI_Comm_obj;
typedef __MPI_Comm_obj *MPI_Comm;

/* creates a new comm from the given sequence of processes,
 * by allocating memory and copying the process sequence;
 * the new comm has no messages */
__MPI_Comm_obj $comm_create(int nprocs, $proc * procs);

struct __MPI_Op {
  int id;
};
typedef struct __MPI_Op MPI_Op;

struct __MPI_Datatype {
  int id;
};
typedef struct __MPI_Datatype MPI_Datatype;

struct __MPI_Comm_record {
  int id;
  // TODO: will add a lot more fields here
};
typedef struct __MPI_Comm_record *MPI_Request;

struct __MPI_Status {
  int MPI_SOURCE;
  int MPI_TAG;
  int MPI_ERROR;
  int size;
};
typedef struct __MPI_Status MPI_Status;

/********************************* Constants *******************************************/

/* Ranks and tags (type int): */

#define MPI_ANY_SOURCE -1
#define MPI_ANY_TAG -2
#define MPI_PROC_NULL -3

/* Communicators (type MPI_Comm) */

__MPI_Comm_obj __MPI_Comm_world;
MPI_Comm MPI_COMM_WORLD = &__MPI_Comm_world;

/* Communication request handles (type MPI_Request): */

struct __MPI_Comm_record __MPI_Request_null;
MPI_Request MPI_REQUEST_NULL = &__MPI_Request_null;

/* Status object pointers (type MPI_Status*): */

MPI_Status __MPI_Status_ignore;
MPI_Status *MPI_STATUS_IGNORE = &__MPI_Status_ignore;
MPI_Status __MPI_Statuses_ignore;
MPI_Status *MPI_STATUSES_IGNORE = &__MPI_Statuses_ignore;

/* Datatypes (type MPI_Datatype) */

MPI_Datatype MPI_INT     = { .id=1 };
MPI_Datatype MPI_FLOAT   = { .id=2 };
MPI_Datatype MPI_DOUBLE  = { .id=3 };
MPI_Datatype MPI_CHAR    = { .id=4 };
MPI_Datatype MPI_BYTE    = { .id=5 };
// etc.

/* Operators (type MPI_Op) */
/*
MPI_Op MPI_SUM    = (struct __MPI_Op){ .id=1 };
MPI_Op MPI_MAX    = (struct __MPI_Op){ .id=2 };
MPI_Op MPI_MIN    = (struct __MPI_Op){ .id=3 };
*/
// etc.


/*************************************** Functions ***************************************/

int MPI_Init(int *argc, char *** argv);

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

// etc.
