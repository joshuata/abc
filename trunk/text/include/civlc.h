/* This header file defines standard types and provides
 * function prototypes used in the CIVL-C language.
 */
 
#ifdef __CIVLC__
#else
#define __CIVLC__

typedef unsigned long int size_t;
 
/* The CIVL-C process reference type */ 
typedef struct __proc__ $proc;

/* The CIVL-C heap type, used to represent a heap */
typedef struct __heap__ $heap;

/* The CIVL-C scope type, used to represent a scope */
typedef struct __scope__ $scope;

/* The CIVL-C dynamic type, used to represent a symbolic type */
typedef struct __dynamic__ $dynamic;

/* The CIVL-C malloc function, which takes a reference to a heap */
void* $malloc($heap *h, int size);

/* Copies a region of memory, just as in standard C */
void memcpy(void *p, void *q, size_t size);

/* The CIVL-C de-allocation function, which takes a reference to a heap */
void $free($heap *h, void *p);

/* Nondeterministic choice of integer i, such that 0<=i<n. */
int $choose_int(int n);

/* Printf, just as in standard C */
void printf(const char * restrict format, ...);

// Message passing:

/* A system type for any segment of memory used as a buffer,
 * formed using union types */
typedef struct __buffer__ __buffer__;

/* A message formed by $message_pack */ 
typedef struct __message__ {
  int source;
  int dest;
  int tag;
  __buffer__ data;
  int size;
} $message;

/* creates a new message, copying data from the specified buffer */ 
$message $message_pack(int source, int dest, int tag,
  void *data, int size);

/* returns the message source */ 
int $message_source($message * message);

/* returns the message tag */
int $message_tag($message * message);

/* returns the message destination */ 
int $message_dest($message * message);

/* returns the message size */ 
int $message_size($message * message);

/* transfers message data to buf, throwing exception if message
 * size exceeds specified size */ 
void $message_unpack($message * message, void *buf, int size);

/* an abstract datatype representing a communicator, or set
 * of message channels between every pair of processes in a
 * set of processes */
typedef struct __comm__ {
  int nprocs;
  $proc procs[];
} $comm;

/* creates a new comm from the given sequence of processes,
 * by allocating memory and copying the process sequence;
 * the new comm has no messages */
$comm $comm_create(int nprocs, $proc * procs);

/* destroys the comm, deallocating whatever was allocated
 * in its creation */ 
void $comm_destroy($comm * comm);

/* returns the number of processes associated to the comm */ 
int $comm_nprocs($comm * comm);

/* returns a pointer to the procs array in comm */ 
$proc * $comm_procs($comm * comm);

/* adds the message to the comm */
void $comm_enqueue($comm * comm, $message * message);

/* returns true iff a matching message exists in comm */
_Bool $comm_probe($comm * comm, int source, int dest, int tag);

/* finds the first matching message and returns pointer
 * to it without modifying comm */
$message * $comm_seek($comm * comm, int source, int dest, int tag);

/* finds the first matching message, removes it from
 * comm, and returns pointer to message */ 
$message * $comm_dequeue($comm * comm, int source, int dest, int tag);

/* returns the number of messages from source to dest stored
 * in comm */ 
int $comm_chan_size($comm * comm, int source, int dest);

/* returns the total number of messages in the comm */ 
int $comm_total_size($comm * comm);

// message passing constants:

/* Like MPI_ANY_SOURCE, can be used in probe, seek, dequeue
 * to match a message with any source */
#define $COMM_ANY_SOURCE -1

/* Like $COMM_ANY_SOURCE above, except for tags */
#define $COMM_ANY_TAG -2


#endif
