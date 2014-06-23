/* This header file defines standard types and provides
 * function prototypes used in the CIVL-C language.
 */
 
#ifdef __CIVLC_COMMON__
#else
#define __CIVLC_COMMON__

// Standard constants, typedefs:

#define $true 1

#define $false 0

#define NULL ((void*)0)

typedef unsigned long int size_t;

// Basic CIVL-C types:
 
/* The CIVL-C process reference type */ 
typedef struct __proc__ $proc;

/* The CIVL-C scope type, used to represent a scope */
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

// Special types and operations:

/* A system type for bundling any slice of memory into
 * a single value. */
typedef struct __bundle__ $bundle;

int $bundle_size($bundle b);

/* Creates a bundle from the memory region specified by
 * ptr and size, copying the data into the new bundle */
$bundle $bundle_pack(void *ptr, int size);

/* Copies the data out of the bundle into the region specified */
void $bundle_unpack($bundle bundle, void *ptr);

// Memory functions:

/* The CIVL-C malloc function, which takes a reference to a scope */
void* $malloc($scope s, int size);

/* The CIVL-C de-allocation function, which takes a pointer, just like 
 * the standard "free" */
void $free(void *p);

// Message passing:

// message passing constants:

/* Like MPI_ANY_SOURCE, can be used in probe, seek, dequeue
 * to match a message with any source */
#define $COMM_ANY_SOURCE -1

/* Like $COMM_ANY_SOURCE above, except for tags */
#define $COMM_ANY_TAG -2

/* The message type, declared here as an incomplete
 * struct type, which is all you need for constructing
 * the AST.  For the complete version, see the CIVL
 * project.
 */
typedef struct __message__ $message;

/* creates a new message, copying data from the specified buffer */ 
$message $message_pack(int source, int dest, int tag, void *data, int size);
  
/* returns the message source */ 
int $message_source($message message);

/* returns the message tag */
int $message_tag($message message) ;

/* returns the message destination */ 
int $message_dest($message message) ;

/* returns the message size */ 
int $message_size($message message); 

/* transfers message data to buf, throwing exception if message
 * size exceeds specified size */ 
void $message_unpack($message message, void *buf, int size);

/* A datatype representing a queue of messages.  All message
 * data is encapsulated inside this value; no external allocation
 * is used. */ 
typedef struct __queue__ $queue;
 
/* A global communicator datatype which must be operated by local communicators.
 * This communicator type has the same meaning as the communicator type in MPI
 * standards*/
typedef struct __gcomm__ * $gcomm;

/* A datatype representing a local communicator which is used for 
 * operating global communicators. The local communicator type has 
 * a handle of a global communicator. This type represents for 
 * a set of processes which have ranks in common.
 */
typedef struct __comm__ * $comm;

/* A datatype representing a global barrier which must be operated by local
 * barriers. 
 */
typedef struct __gbarrier__ * $gbarrier;
 
/* A datatype representing a global barrier which used for 
 * operating global barriers. The local barrier type has 
 * a handle of a global barrier.
 */
typedef struct __barrier__ * $barrier;
 
typedef struct __int_iter__ * $int_iter;


/* Creates a new barrier object and returns a handle to it.
 * The barrier has the specified size.
 * The new object will be allocated in the given scope. */
$gbarrier $gbarrier_create($scope scope, int size);

/* Destroys the gbarrier */
void $gbarrier_destroy($gbarrier barrier);

/* Creates a new local barrier object and returns a handle to it.
 * The new barrier will be affiliated with the specified global
 * barrier.   This local barrier handle will be used as an
 * argument in most barrier functions.  The place must be in
 * [0,size-1] and specifies the place in the global barrier
 * that will be occupied by the local barrier.  
 * Only one call to $barrier_create may occur for each barrier-place pair.
 * The new object will be allocated in the given scope. */
$barrier $barrier_create($scope scope, $gbarrier gbarrier, int place);

void $barrier_enter($barrier barrier);

void $barrier_exit($barrier barrier);

/* Calls the barrier associated with this local barrier object.*/
void $barrier_call($barrier barrier);

/* Destroys the barrier. */
void $barrier_destroy($barrier barrier);

/* This version of gcomm_create should only be called if size is concrete. */ 
$gcomm $gcomm_create2($scope scope, int size);

/* Creates a new global communicator object and returns a handle to it.
 * The global communicator will have size communication places.  The
 * global communicator defines a communication "universe" and encompasses
 * message buffers and all other components of the state associated to
 * message-passing.  The new object will be allocated in the given scope. */
$gcomm $gcomm_create($scope scope, int size);

/* De-allocation a __gcomm__ object */
void $gcomm_destroy($gcomm gcomm);

/* Returns the size (number of places) in the global communicator associated
 * to the given comm. */
int $comm_size($comm comm);

/* De-allocation a __comm__ object */
void $comm_destroy($comm comm);

/* Returns the place of the local communicator.  This is the same as the
 * place argument used to create the local communicator. */
int $comm_place($comm comm);

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

/* Returns $true iff gcomm points to a valid object. */
_Bool $gcomm_defined($gcomm gcomm);

/* Returns $true iff comm points to a valid object. */
_Bool $comm_defined($comm comm);

/* Tells whether the integer iterator has any more elements */
_Bool $int_iter_hasNext($int_iter iter);

/* Returns the next element in the iterator (and updates the iterator) */
int $int_iter_next($int_iter iter);

/* Creates a new iterator for an array of integers, and returns the
 * handle of the iterator. */
$int_iter $int_iter_create($scope scope, int *array, int size);

/* Deletes an $int_iter object from the heap. */
void $int_iter_destroy($int_iter iter);

#endif
