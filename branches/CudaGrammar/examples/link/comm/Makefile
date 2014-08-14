ring: ring.c mpi.cvl concurrency.cvl comm.cvl 
	abc -I . -lang=civlc ring.c  mpi.cvl concurrency.cvl comm.cvl
	
message: messageUnpack.cvl comm.cvl
	abc -I . messageUnpack.cvl comm.cvl
	
clean:
	rm -f ring
