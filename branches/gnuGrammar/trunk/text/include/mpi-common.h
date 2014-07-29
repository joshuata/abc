/* -*- Mode: C; c-basic-offset:4 ; -*- */
/*  
 *  (C) 2001 by Argonne National Laboratory.
 *      See COPYRIGHT in top-level directory.
 */
/* src/include/mpi.h.  Generated from mpi.h.in by configure. */
#ifndef __MPI_COMMON__
#define __MPI_COMMON__

/* Results of the compare operations. */
#define MPI_IDENT     0
#define MPI_CONGRUENT 1
#define MPI_SIMILAR   2
#define MPI_UNEQUAL   3

typedef enum {
    MPI_CHAR,           
    MPI_SIGNED_CHAR,
    MPI_UNSIGNED_CHAR,
    MPI_BYTE,           
    MPI_WCHAR,          
    MPI_SHORT,          
    MPI_UNSIGNED_SHORT,
    MPI_INT,            
    MPI_UNSIGNED,       
    MPI_LONG,          
    MPI_UNSIGNED_LONG, 
    MPI_FLOAT,          
    MPI_DOUBLE,         
    MPI_LONG_DOUBLE,
    MPI_LONG_LONG_INT,  
    MPI_UNSIGNED_LONG_LONG,
    MPI_LONG_LONG,
    MPI_PACKED,
    MPI_LB,
    MPI_UB,
    MPI_FLOAT_INT,        
    MPI_DOUBLE_INT,       
    MPI_LONG_INT,         
    MPI_SHORT_INT,        
    MPI_2INT,             
    MPI_LONG_DOUBLE_INT,  
    MPI_AINT,
    MPI_OFFSET
} MPI_Datatype;

typedef long MPI_Aint;
typedef int MPI_Fint;
typedef struct MPI_Comm MPI_Comm;
typedef struct MPI_Group MPI_Group;
typedef struct MPI_Win MPI_Win;
typedef struct MPI_Status MPI_Status;
typedef struct MPI_Request * MPI_Request;
typedef struct MPIX_Message MPIX_Message;
typedef struct MPI_File MPI_File;
typedef struct MPI_Errhandler MPI_Errhandler;
typedef struct MPI_Info MPI_Info;
typedef struct MPI_User_function MPI_User_function;
typedef struct MPI_Copy_function MPI_Copy_function;
typedef struct MPI_Delete_function MPI_Delete_function;

#define MPI_SUCCESS     ( 0)
#define MPI_ANY_SOURCE 	(-1)
#define MPI_ANY_TAG     (-2)
#define MPI_PROC_NULL   (-3)
#define MPI_STATUS_IGNORE   NULL
#define MPI_STATUSES_IGNORE NULL
#define MPI_COMM_NULL       NULL


/**************************** Communicators  ************************************/
MPI_Comm MPI_COMM_WORLD;
MPI_Comm MPI_COMM_SELF;

/* We require that the C compiler support prototypes */
/* Begin Prototypes */
int MPI_Send(void*, int, MPI_Datatype, int, int, MPI_Comm);
int MPI_Recv(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Status *);
int MPI_Get_count( MPI_Status *, MPI_Datatype, int *);
int MPI_Bsend(void*, int, MPI_Datatype, int, int, MPI_Comm);
int MPI_Ssend(void*, int, MPI_Datatype, int, int, MPI_Comm);
int MPI_Rsend(void*, int, MPI_Datatype, int, int, MPI_Comm);
int MPI_Buffer_attach( void*, int);
int MPI_Buffer_detach( void*, int *);
int MPI_Isend(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Ibsend(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Issend(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Irsend(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Irecv(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Wait(MPI_Request *, MPI_Status *);
int MPI_Test(MPI_Request *, int *, MPI_Status *);
int MPI_Request_free(MPI_Request *);
int MPI_Waitany(int, MPI_Request *, int *, MPI_Status *);
int MPI_Testany(int, MPI_Request *, int *, int *, MPI_Status *);
int MPI_Waitall(int, MPI_Request *, MPI_Status *);
int MPI_Testall(int, MPI_Request *, int *, MPI_Status *);
int MPI_Waitsome(int, MPI_Request *, int *, int *, MPI_Status *);
int MPI_Testsome(int, MPI_Request *, int *, int *, MPI_Status *);
int MPI_Iprobe(int, int, MPI_Comm, int *, MPI_Status *);
int MPI_Probe(int, int, MPI_Comm, MPI_Status *);
int MPI_Cancel(MPI_Request *);
int MPI_Test_cancelled(MPI_Status *, int *);
int MPI_Send_init(void*, int, MPI_Datatype, int, int, MPI_Comm, MPI_Request *);
int MPI_Bsend_init(void*, int, MPI_Datatype, int,int, MPI_Comm, MPI_Request *);
int MPI_Ssend_init(void*, int, MPI_Datatype, int,int, MPI_Comm, MPI_Request *);
int MPI_Rsend_init(void*, int, MPI_Datatype, int,int, MPI_Comm, MPI_Request *);
int MPI_Recv_init(void*, int, MPI_Datatype, int,int, MPI_Comm, MPI_Request *);
int MPI_Start(MPI_Request *);
int MPI_Startall(int, MPI_Request *);
int MPI_Sendrecv(void *, int, MPI_Datatype,int, int, void *, int, MPI_Datatype, int, int, MPI_Comm, MPI_Status *);
int MPI_Sendrecv_replace(void*, int, MPI_Datatype, int, int, int, int, MPI_Comm, MPI_Status *);
int MPI_Type_contiguous(int, MPI_Datatype, MPI_Datatype *);
int MPI_Type_vector(int, int, int, MPI_Datatype, MPI_Datatype *);
int MPI_Type_hvector(int, int, MPI_Aint, MPI_Datatype, MPI_Datatype *);
int MPI_Type_indexed(int, int *, int *, MPI_Datatype, MPI_Datatype *);
int MPI_Type_hindexed(int, int *, MPI_Aint *, MPI_Datatype, MPI_Datatype *);
int MPI_Type_struct(int, int *,  MPI_Aint *, MPI_Datatype *, MPI_Datatype *);
int MPI_Address(void*, MPI_Aint *);
int MPI_Type_extent(MPI_Datatype, MPI_Aint *);
int MPI_Type_size(MPI_Datatype, int *);
int MPI_Type_lb(MPI_Datatype, MPI_Aint *);
int MPI_Type_ub(MPI_Datatype, MPI_Aint *);
int MPI_Type_commit(MPI_Datatype *);
int MPI_Type_free(MPI_Datatype *);
int MPI_Get_elements(MPI_Status *, MPI_Datatype, int *);
int MPI_Pack(void*, int, MPI_Datatype, void *, int, int *,  MPI_Comm);
int MPI_Unpack(void*, int, int *, void *, int, MPI_Datatype, MPI_Comm);
int MPI_Pack_size(int, MPI_Datatype, MPI_Comm, int *);
int MPI_Barrier(MPI_Comm );
int MPI_Bcast(void*, int, MPI_Datatype, int, MPI_Comm);
int MPI_Gather(void* , int, MPI_Datatype, void*, int, MPI_Datatype, int, MPI_Comm);
int MPI_Gatherv(void* , int, MPI_Datatype, void*, int *,
                int *, MPI_Datatype, int, MPI_Comm);
int MPI_Scatter(void* , int, MPI_Datatype, void*, int, MPI_Datatype, int, MPI_Comm);
int MPI_Scatterv(void* , int *,int *,  MPI_Datatype, void*, int, MPI_Datatype, int, MPI_Comm);
int MPI_Allgather(void* , int, MPI_Datatype, void*, int, MPI_Datatype, MPI_Comm);
int MPI_Allgatherv(void* , int, MPI_Datatype, void*, int *,
                   int *, MPI_Datatype, MPI_Comm);
int MPI_Alltoall(void* , int, MPI_Datatype, void*, int, MPI_Datatype, MPI_Comm);
int MPI_Alltoallv( void* ,  int *,  int *, MPI_Datatype,
		   void*,  int *,  int *, MPI_Datatype, MPI_Comm);
int MPI_Reduce( void* , void*, int, MPI_Datatype, MPI_Op, int, MPI_Comm);
int MPI_Op_create(MPI_User_function *, int, MPI_Op *);
int MPI_Op_free( MPI_Op *);
int MPI_Allreduce( void*, void*, int, MPI_Datatype, MPI_Op, MPI_Comm);
int MPI_Reduce_scatter(  void* , void*,   int *, MPI_Datatype, MPI_Op, MPI_Comm);
int MPI_Scan(void* , void*, int, MPI_Datatype, MPI_Op, MPI_Comm);
int MPI_Group_size(MPI_Group, int *);
int MPI_Group_rank(MPI_Group, int *);
int MPI_Group_translate_ranks (MPI_Group, int,   int *, MPI_Group, int *);
int MPI_Group_compare(MPI_Group, MPI_Group, int *);
int MPI_Comm_group(MPI_Comm, MPI_Group *);
int MPI_Group_union(MPI_Group, MPI_Group, MPI_Group *);
int MPI_Group_intersection(MPI_Group, MPI_Group, MPI_Group *);
int MPI_Group_difference(MPI_Group, MPI_Group, MPI_Group *);
int MPI_Group_incl(MPI_Group, int,   int *, MPI_Group *);
int MPI_Group_excl(MPI_Group, int,   int *, MPI_Group *);
int MPI_Group_range_incl(MPI_Group, int, int [][3], MPI_Group *);
int MPI_Group_range_excl(MPI_Group, int, int [][3], MPI_Group *);
int MPI_Group_free(MPI_Group *);
int MPI_Comm_size(MPI_Comm, int *);
int MPI_Comm_rank(MPI_Comm, int *);
int MPI_Comm_compare(MPI_Comm, MPI_Comm, int *);
int MPI_Comm_dup(MPI_Comm, MPI_Comm *);
int MPI_Comm_create(MPI_Comm, MPI_Group, MPI_Comm *);
int MPI_Comm_split(MPI_Comm, int, int, MPI_Comm *);
int MPI_Comm_free(MPI_Comm *);
int MPI_Comm_test_inter(MPI_Comm, int *);
int MPI_Comm_remote_size(MPI_Comm, int *);
int MPI_Comm_remote_group(MPI_Comm, MPI_Group *);
int MPI_Intercomm_create(MPI_Comm, int, MPI_Comm, int, int, MPI_Comm * );
int MPI_Intercomm_merge(MPI_Comm, int, MPI_Comm *);
int MPI_Keyval_create(MPI_Copy_function *, MPI_Delete_function *, int *, void*);
int MPI_Keyval_free(int *);
int MPI_Attr_put(MPI_Comm, int, void*);
int MPI_Attr_get(MPI_Comm, int, void *, int *);
int MPI_Attr_delete(MPI_Comm, int);
int MPI_Topo_test(MPI_Comm, int *);
int MPI_Cart_create(MPI_Comm, int,   int *,   int *, int, MPI_Comm *);
int MPI_Dims_create(int, int, int *);
int MPI_Graph_create(MPI_Comm, int,   int *,   int *, int, MPI_Comm *);
int MPI_Graphdims_get(MPI_Comm, int *, int *);
int MPI_Graph_get(MPI_Comm, int, int, int *, int *);
int MPI_Cartdim_get(MPI_Comm, int *);
int MPI_Cart_get(MPI_Comm, int, int *, int *, int *);
int MPI_Cart_rank(MPI_Comm, int *, int *);
int MPI_Cart_coords(MPI_Comm, int, int, int *);
int MPI_Graph_neighbors_count(MPI_Comm, int, int *);
int MPI_Graph_neighbors(MPI_Comm, int, int, int *);
int MPI_Cart_shift(MPI_Comm, int, int, int *, int *);
int MPI_Cart_sub(MPI_Comm,   int *, MPI_Comm *);
int MPI_Cart_map(MPI_Comm, int,   int *,   int *, int *);
int MPI_Graph_map(MPI_Comm, int,   int *,   int *, int *);
int MPI_Get_processor_name(char *, int *);
int MPI_Get_version(int *, int *);
/* int MPI_Errhandler_create(MPI_Handler_function *, MPI_Errhandler *); */
int MPI_Errhandler_set(MPI_Comm, MPI_Errhandler);
int MPI_Errhandler_get(MPI_Comm, MPI_Errhandler *);
int MPI_Errhandler_free(MPI_Errhandler *);
int MPI_Error_string(int, char *, int *);
int MPI_Error_class(int, int *);
double MPI_Wtime(void);
double MPI_Wtick(void);
int MPI_Init(int *, char ***);
int MPI_Finalize(void);
int MPI_Initialized(int *);
int MPI_Abort(MPI_Comm, int);


/* Note that we may need to define a @PCONTROL_LIST@ depending on whether 
   stdargs are supported */
int MPI_Pcontrol(const int, ...);

int MPI_DUP_FN ( MPI_Comm, int, void *, void *, void *, int * );


/* MPI-2 functions */

/* Process Creation and Management */
int MPI_Close_port(  char *);
int MPI_Comm_accept(  char *, MPI_Info, int, MPI_Comm, MPI_Comm *);
int MPI_Comm_connect(  char *, MPI_Info, int, MPI_Comm, MPI_Comm *);
int MPI_Comm_disconnect(MPI_Comm *);
int MPI_Comm_get_parent(MPI_Comm *);
int MPI_Comm_join(int, MPI_Comm *);
int MPI_Comm_spawn(  char *, char *[], int, MPI_Info, int, MPI_Comm, MPI_Comm *,
                   int []);
int MPI_Comm_spawn_multiple(int, char *[], char **[],   int [],   MPI_Info [], int,
			    MPI_Comm, MPI_Comm *, int []); 
int MPI_Lookup_name(  char *, MPI_Info, char *);
int MPI_Open_port(MPI_Info, char *);
int MPI_Publish_name(  char *, MPI_Info,   char *);
int MPI_Unpublish_name(  char *, MPI_Info,   char *);

/* One-Sided Communications */
int MPI_Accumulate(  void *, int, MPI_Datatype, int, MPI_Aint, int,
                   MPI_Datatype,  MPI_Op, MPI_Win) ;
int MPI_Get(void *, int, MPI_Datatype, int, MPI_Aint, int, MPI_Datatype,
            MPI_Win) ;
int MPI_Put(  void *, int, MPI_Datatype, int, MPI_Aint, int, MPI_Datatype,
            MPI_Win) ;
int MPI_Win_complete(MPI_Win);
int MPI_Win_create(void *, MPI_Aint, int, MPI_Info, MPI_Comm, MPI_Win *);
int MPI_Win_fence(int, MPI_Win);
int MPI_Win_free(MPI_Win *);
int MPI_Win_get_group(MPI_Win, MPI_Group *);
int MPI_Win_lock(int, int, int, MPI_Win);
int MPI_Win_post(MPI_Group, int, MPI_Win);
int MPI_Win_start(MPI_Group, int, MPI_Win);
int MPI_Win_test(MPI_Win, int *);
int MPI_Win_unlock(int, MPI_Win);
int MPI_Win_wait(MPI_Win);

/* Extended Collective Operations */
int MPI_Alltoallw(  void *,   int [],   int [],
                    MPI_Datatype [], void *,   int [],
		    int [],   MPI_Datatype [], MPI_Comm);
int MPI_Exscan(  void *, void *, int, MPI_Datatype, MPI_Op, MPI_Comm);
 
/* External Interfaces */
int MPI_Add_error_class(int *);
int MPI_Add_error_code(int, int *);
int MPI_Add_error_string(int,   char *);
int MPI_Comm_call_errhandler(MPI_Comm, int);
/*int MPI_Comm_create_keyval(MPI_Comm_copy_attr_function *, 
  MPI_Comm_delete_attr_function *, int *, void *); */
int MPI_Comm_delete_attr(MPI_Comm, int);
int MPI_Comm_free_keyval(int *);
int MPI_Comm_get_attr(MPI_Comm, int, void *, int *);
int MPI_Comm_get_name(MPI_Comm, char *, int *);
int MPI_Comm_set_attr(MPI_Comm, int, void *);
int MPI_Comm_set_name(MPI_Comm,   char *);
int MPI_File_call_errhandler(MPI_File, int);
int MPI_Grequest_complete(MPI_Request);
/*int MPI_Grequest_start(MPI_Grequest_query_function *, 
                       MPI_Grequest_free_function *, 
                       MPI_Grequest_cancel_function *, void *, MPI_Request *);*/
int MPI_Init_thread(int *, char ***, int, int *);
int MPI_Is_thread_main(int *);
int MPI_Query_thread(int *);
int MPI_Status_set_cancelled(MPI_Status *, int);
int MPI_Status_set_elements(MPI_Status *, MPI_Datatype, int);
/*int MPI_Type_create_keyval(MPI_Type_copy_attr_function *, 
  MPI_Type_delete_attr_function *, int *, void *);*/
int MPI_Type_delete_attr(MPI_Datatype, int);
int MPI_Type_dup(MPI_Datatype, MPI_Datatype *);
int MPI_Type_free_keyval(int *);
int MPI_Type_get_attr(MPI_Datatype, int, void *, int *);
int MPI_Type_get_contents(MPI_Datatype, int, int, int, int [], MPI_Aint [], 
                          MPI_Datatype []);
int MPI_Type_get_envelope(MPI_Datatype, int *, int *, int *, int *);
int MPI_Type_get_name(MPI_Datatype, char *, int *);
int MPI_Type_set_attr(MPI_Datatype, int, void *);
int MPI_Type_set_name(MPI_Datatype,   char *);
int MPI_Type_match_size( int, int, MPI_Datatype *);
int MPI_Win_call_errhandler(MPI_Win, int);
/*int MPI_Win_create_keyval(MPI_Win_copy_attr_function *, 
  MPI_Win_delete_attr_function *, int *, void *);*/
int MPI_Win_delete_attr(MPI_Win, int);
int MPI_Win_free_keyval(int *);
int MPI_Win_get_attr(MPI_Win, int, void *, int *);
int MPI_Win_get_name(MPI_Win, char *, int *);
int MPI_Win_set_attr(MPI_Win, int, void *);
int MPI_Win_set_name(MPI_Win,   char *);

/* Miscellany */
MPI_Comm MPI_Comm_f2c(MPI_Fint);
MPI_Datatype MPI_Type_f2c(MPI_Fint);
MPI_File MPI_File_f2c(MPI_Fint);
MPI_Fint MPI_Comm_c2f(MPI_Comm);
MPI_Fint MPI_File_c2f(MPI_File);
MPI_Fint MPI_Group_c2f(MPI_Group);
MPI_Fint MPI_Info_c2f(MPI_Info);
MPI_Fint MPI_Op_c2f(MPI_Op);
MPI_Fint MPI_Request_c2f(MPI_Request);
MPI_Fint MPI_Type_c2f(MPI_Datatype);
MPI_Fint MPI_Win_c2f(MPI_Win);
MPI_Group MPI_Group_f2c(MPI_Fint);
MPI_Info MPI_Info_f2c(MPI_Fint);
MPI_Op MPI_Op_f2c(MPI_Fint);
MPI_Request MPI_Request_f2c(MPI_Fint);
MPI_Win MPI_Win_f2c(MPI_Fint);

int MPI_Alloc_mem(MPI_Aint, MPI_Info info, void *baseptr);
/*int MPI_Comm_create_errhandler(MPI_Comm_errhandler_function *, MPI_Errhandler *);*/
int MPI_Comm_get_errhandler(MPI_Comm, MPI_Errhandler *);
int MPI_Comm_set_errhandler(MPI_Comm, MPI_Errhandler);
/*int MPI_File_create_errhandler(MPI_File_errhandler_function *, MPI_Errhandler *);*/
int MPI_File_get_errhandler(MPI_File, MPI_Errhandler *);
int MPI_File_set_errhandler(MPI_File, MPI_Errhandler);
int MPI_Finalized(int *);
int MPI_Free_mem(void *);
int MPI_Get_address(  void *, MPI_Aint *);
int MPI_Info_create(MPI_Info *);
int MPI_Info_delete(MPI_Info,   char *);
int MPI_Info_dup(MPI_Info, MPI_Info *);
int MPI_Info_free(MPI_Info *info);
int MPI_Info_get(MPI_Info,   char *, int, char *, int *);
int MPI_Info_get_nkeys(MPI_Info, int *);
int MPI_Info_get_nthkey(MPI_Info, int, char *);
int MPI_Info_get_valuelen(MPI_Info,   char *, int *, int *);
int MPI_Info_set(MPI_Info,   char *,   char *);
int MPI_Pack_external(  char *,   void *, int, MPI_Datatype, void *,
                      MPI_Aint, MPI_Aint *);
int MPI_Pack_external_size(  char *, int, MPI_Datatype, MPI_Aint *);
int MPI_Request_get_status(MPI_Request, int *, MPI_Status *);
int MPI_Status_c2f(  MPI_Status *, MPI_Fint *);
int MPI_Status_f2c(  MPI_Fint *, MPI_Status *);
int MPI_Type_create_darray(int, int, int,   int [],   int [],
                             int [],   int [], int,
                           MPI_Datatype, MPI_Datatype *);
int MPI_Type_create_hindexed(int,   int [],   MPI_Aint [], MPI_Datatype,
                             MPI_Datatype *);
int MPI_Type_create_hvector(int, int, MPI_Aint, MPI_Datatype, MPI_Datatype *);
int MPI_Type_create_indexed_block(int, int,   int [], MPI_Datatype,
                                  MPI_Datatype *);
int MPIX_Type_create_hindexed_block(int, int, const MPI_Aint [], MPI_Datatype, MPI_Datatype *);
int MPI_Type_create_resized(MPI_Datatype, MPI_Aint, MPI_Aint, MPI_Datatype *);
int MPI_Type_create_struct(int,   int [],   MPI_Aint [],
                             MPI_Datatype [], MPI_Datatype *);
int MPI_Type_create_subarray(int,   int [],   int [],   int [],
                             int, MPI_Datatype, MPI_Datatype *);
int MPI_Type_get_extent(MPI_Datatype, MPI_Aint *, MPI_Aint *);
int MPI_Type_get_true_extent(MPI_Datatype, MPI_Aint *, MPI_Aint *);
int MPI_Unpack_external(  char *,   void *, MPI_Aint, MPI_Aint *, void *,
                        int, MPI_Datatype);
/*int MPI_Win_create_errhandler(MPI_Win_errhandler_function *, MPI_Errhandler *);*/
int MPI_Win_get_errhandler(MPI_Win, MPI_Errhandler *);
int MPI_Win_set_errhandler(MPI_Win, MPI_Errhandler);

/* Fortran 90-related functions.  These routines are available only if
   Fortran 90 support is enabled 
*/
int MPI_Type_create_f90_integer( int, MPI_Datatype * );
int MPI_Type_create_f90_real( int, int, MPI_Datatype * );
int MPI_Type_create_f90_complex( int, int, MPI_Datatype * );

/* MPI-2.2 functions */
int MPI_Reduce_local(  void *inbuf, void *inoutbuf, int count, MPI_Datatype datatype, MPI_Op op);
int MPI_Op_commutative(MPI_Op op, int *commute);
int MPI_Reduce_scatter_block(  void *sendbuf, void *recvbuf, int recvcount, MPI_Datatype datatype, MPI_Op op, MPI_Comm comm);
int MPI_Dist_graph_create_adjacent(MPI_Comm comm_old, int indegree,   int [],   int [], int outdegree,   int [],   int [], MPI_Info info, int reorder, MPI_Comm *comm_dist_graph);
int MPI_Dist_graph_create(MPI_Comm comm_old, int n,   int [],   int [],   int [],   int [], MPI_Info info, int reorder, MPI_Comm *comm_dist_graph);
int MPI_Dist_graph_neighbors_count(MPI_Comm comm, int *indegree, int *outdegree, int *weighted);
int MPI_Dist_graph_neighbors(MPI_Comm comm, int maxindegree, int [], int [], int maxoutdegree, int [], int []);
#endif

