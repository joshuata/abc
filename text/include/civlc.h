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
typedef struct __heap__ $heap;

// /* The CIVL-C scope type, used to represent a scope */
// typedef struct __scope__ $scope;

/* The CIVL-C dynamic type, used to represent a symbolic type */
typedef struct __dynamic__ $dynamic;


// Memory functions:

/* The CIVL-C malloc function, which takes a reference to a heap */
void* $malloc($heap *h, int size);

/* Copies a region of memory, just as in standard C */
void memcpy(void *p, void *q, size_t size);

/* The CIVL-C de-allocation function, which takes a reference to a heap */
void $free($heap *h, void *p);


// Misc. functions:

/* Nondeterministic choice of integer i, such that 0<=i<n. */
int $choose_int(int n);

/* Printf, just as in standard C */
void printf(const char * restrict format, ...);

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
int $message_source($message * message) {
  return message->source;
}

/* returns the message tag */
int $message_tag($message * message) {
  return message->tag;
}

/* returns the message destination */ 
int $message_dest($message * message) {
  return message->dest;
}

/* returns the message size */ 
int $message_size($message * message) {
  return message->size;
}

/* transfers message data to buf, throwing exception if message
 * size exceeds specified size */ 
void $message_unpack($message * message, void *buf, int size) {
  $bundle_unpack(message->data, buf);
}

/* A datatype representing a queue of messages.  All message
 * data is encapsulated inside this value; no external allocation
 * is used. */ 
typedef struct __queue__ {
  int length;
  $message messages[];
} $queue;

/* A datatype representing a communicator, or set
 * of message channels between every pair of processes in a
 * set of processes.  All message and other data is encapsulated
 * in this value; no outside allocation is used. */
typedef struct __comm__ {
  int nprocs; // number of processes
  $proc procs[]; // the processes in order
  $queue buf[][]; // message buffers
} $comm;

/* creates a new comm from the given sequence of processes,
 * by allocating memory and copying the process sequence;
 * the new comm has no messages */
$comm $comm_create(int nprocs, $proc * procs);

/* returns the number of processes associated to the comm */ 
int $comm_nprocs($comm * comm) {
  return comm->nprocs;
}

/* returns a pointer to the procs array in comm */ 
$proc * $comm_procs($comm * comm) {
  return &comm->procs[0];
}

/* adds the message to the comm */
void $comm_enqueue($comm * comm, $message * message);

// to implement this, just need $append, $remove


/* returns true iff a matching message exists in comm */
_Bool $comm_probe($comm * comm, int source, int dest, int tag) {
  int nprocs = comm->nprocs;
  $queue queue;
  int length;
  
  $assert(0 <= source && source < nprocs);
  $assert(0 <= dest && dest < nprocs);
  queue = comm->buf[source][dest];
  length = queue.length;
  if (tag == $COMM_ANY_TAG)
    return length > 0 ? $true : $false;
  for (int i=0; i<length; i++)
    if (queue.messages[i].tag == tag) return $true;
  return $false;
}

/* finds the first matching message and returns pointer
 * to it without modifying comm */
$message * $comm_seek($comm * comm, int source, int dest, int tag) {
  int nprocs = comm->nprocs;
  $queue queue;
  int length;
  
  $assert(0 <= source && source < nprocs);
  $assert(0 <= dest && dest < nprocs);
  queue = comm->buf[source][dest];
  length = queue.length;
  if (tag == $COMM_ANY_TAG)
    return length > 0 ? &queue.messages[0] : NULL;
  for (int i=0; i<length; i++) {
    $message m = queue.messages[i];
    if (m.tag == tag) return m;
  }
  return NULL;
}


/* finds the first matching message, removes it from
 * comm, and returns pointer to message */ 
$message * $comm_dequeue($comm * comm, int source, int dest, int tag);

/* returns the number of messages from source to dest stored
 * in comm */ 
int $comm_chan_size($comm * comm, int source, int dest) {
  int nprocs = comm->nprocs;
  
  $assert(0 <= source && source < nprocs);
  $assert(0 <= dest && dest < nprocs);
  return comm->buf[source][dest].length;
}

/* returns the total number of messages in the comm */ 
int $comm_total_size($comm * comm) {
  int result = 0;
  int nprocs = comm->nprocs;
  
  for (int i=0; i<nprocs; i++)
    for (int j=0; j<nprocs; j++)
      result += comm->buf[i][j].length;
  return result;
}


#endif
