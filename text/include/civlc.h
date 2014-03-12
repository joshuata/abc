/* This header file defines standard types and provides
 * function prototypes used in the CIVL-C language.
 */
 
#ifdef __CIVLC__
#else
#define __CIVLC__

// Standard constants, typedefs:

#define $true 1

#define $false 0

#define NULL ((void*)0)

typedef unsigned long int size_t;

// Basic CIVL-C types:
 
/* The CIVL-C process reference type */ 
typedef struct __proc__ $proc;

/* The CIVL-C heap type, used to represent a heap */
//typedef struct __heap__ $heap;

// /* The CIVL-C scope type, used to represent a scope */
 typedef struct __scope__ $scope;

/* The CIVL-C dynamic type, used to represent a symbolic type */
typedef struct __dynamic__ $dynamic;

// Misc. functions:

/* Wait for another process p to terminate. */
void $wait($proc p);

/* Terminate the calling process. */
void $exit(void);

/* Nondeterministic choice of integer i, such that 0<=i<n. */
int $choose_int(int n);

/* assertion */
void $assert(_Bool expr, ...);

/* Printf, just as in standard C */
//void printf(const char * restrict format, ...);
//moved to stdio.h
//void printf(char * format, ...);

// Special types and operations:

/* A system type for bundling any slice of memory into
 * a single value. */
typedef struct __bundle__ $bundle;

int $bundle_size($bundle b);

/* Creates a bundle from the memory region specified by
 * ptr and size, copying the data into the new bundle */
$bundle $bundle_pack(void *ptr, int size);

/* Copies the data out of the bundle into the region
 * specified */
void $bundle_unpack($bundle bundle, void *ptr);

// Memory functions:

/* The CIVL-C malloc function, which takes a reference to a scope */
void* $malloc($scope s, int size);

/* Copies a region of memory, just as in standard C */
void memcpy(void *p, void *q, size_t size) {
  $atom {
    $bundle bundle = $bundle_pack(q, size);
    $bundle_unpack(bundle, p);
  }
}

/* The CIVL-C de-allocation function, which takes a reference to a heap */
void $free(void *p);

// Message passing:

// message passing constants:

/* Like MPI_ANY_SOURCE, can be used in probe, seek, dequeue
 * to match a message with any source */
#define $COMM_ANY_SOURCE -1

/* Like $COMM_ANY_SOURCE above, except for tags */
#define $COMM_ANY_TAG -2

/* A message formed by $message_pack */ 
typedef struct __message__ {
  int source;
  int dest;
  int tag;
  $bundle data;
  int size;
} $message;

/* creates a new message, copying data from the specified buffer */ 
$message $message_pack(int source, int dest, int tag,
    void *data, int size) {
  $message result;
  
  result.source = source;
  result.dest = dest;
  result.tag = tag;
  result.data = $bundle_pack(data, size);
  result.size = size;
  return result;
}
  
/* returns the message source */ 
int $message_source($message message) {
  return message.source;
}

/* returns the message tag */
int $message_tag($message message) {
  return message.tag;
}

/* returns the message destination */ 
int $message_dest($message message) {
  return message.dest;
}

/* returns the message size */ 
int $message_size($message message) {
  return message.size;
}

/* transfers message data to buf, throwing exception if message
 * size exceeds specified size */ 
void $message_unpack($message message, void *buf, int size) {
  $bundle_unpack(message.data, buf);
  $assert(message.size <= size, 
    "Message of size %d exceeds the specified size %d.", message.size, size);
}

/* A datatype representing a queue of messages.  All message
 * data is encapsulated inside this value; no external allocation
 * is used. */ 
typedef struct __queue__ {
  int length;
  $message messages[];
} $queue;
 
/* A globle communicator datatype which must be operated by local communicators.
 * This communicator type has the same meaning as the communicator type in MPI
 * standards*/
typedef struct __gcomm__ {
  int nprocs; // number of processes
  _Bool isInit[]; // if the local comm has been initiated
  $queue buf[][]; // message buffers
} * $gcomm;


/* A datatype representing a local communicator which used for 
 * operating globle communicators. The local communicator type has 
 * a handle of a globle communicator. This type represents for 
 * a set of processes which have ranks in common.
 * */
 typedef struct __comm__ {
  int place;
  $gcomm gcomm;
 } * $comm;

/* This version of gcomm_create should only be called if
 * size is concrete. */ 
$gcomm $gcomm_create2($scope scope, int size);

/* Creates a new global communicator object and returns a handle to it.
 * The global communicator will have size communication places.  The
 * global communicator defines a communication "universe" and encompasses
 * message buffers and all other components of the state associated to
 * message-passing.  The new object will be allocated in the given scope. */
$gcomm $gcomm_create($scope scope, int size) {
  $gcomm result;
  
  for (int i=0; i<size; i++) ;
  result = $gcomm_create2(scope, size);
  return result;
}

/* Returns the size (number of places) in the global communicator associated
 * to the given comm. */
int $comm_size($comm comm);

/* Returns the place of the local communicator.  This is the same as the
 * place argument used to create the local communicator. */
int $comm_place($comm comm){
 return comm->place;
}

/* Adds the message to the appropriate message queue in the communication
 * universe specified by the comm.  The source of the message must equal
 * the place of the comm. */
void $comm_enqueue($comm comm, $message message);

/* Returns true iff a matching message exists in the communication universe
 * specified by the comm.  A message matches the arguments if the destination
 * of the message is the place of the comm, and the sources and tags match. */
_Bool $comm_probe($comm comm, int source, int tag);

/* Finds the first matching message and returns it without modifying
 * the communication universe.  If no matching message exists, returns a message
 * with source, dest, and tag all negative. */
$message $comm_seek($comm comm, int source, int tag);

/* Finds the first matching message, removes it from the communicator,
 * and returns the message */
$message $comm_dequeue($comm comm, int source, int tag);

/* returns the number of messages from source to dest stored
 * in comm */ 
//int $comm_chan_size($comm comm, int source, int dest);

/* returns the total number of messages in the comm */ 
//int $comm_total_size($comm comm);

/* Creates a new local communicator object and returns a handle to it.
 * The new communicator will be affiliated with the specified global
 * communicator.   This local communicator handle will be used as an
 * argument in most message-passing functions.  The place must be in
 * [0,size-1] and specifies the place in the global communication universe
 * that will be occupied by the local communicator.  The local communicator
 * handle may be used by more than one process, but all of those
 * processes will be viewed as occupying the same place.
 * Only one call to $comm_create may occur for each gcomm-place pair.
 * The new object will be allocated in the given scope. */
$comm $comm_create($scope scope, $gcomm gcomm, int place);

/* Returns the parent scope of the given scope */
$scope $scope_parent($scope s);

/* Returns $true iff p is a defined procecess value (i.e., 
the value of the process id is greater than 0) */
_Bool $proc_defined($proc p);

/* Returns $true iff s is a defined scope value (i.e., 
the scope s is still valid) */
_Bool $scope_defined($scope s);

_Bool $gcomm_defined($gcomm gcomm);

_Bool $comm_defined($comm comm);

#endif
