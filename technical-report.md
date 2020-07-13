# ETCD Endpoints
ETCD exposes its API as a set of [gRPC](https://grpc.io/) endpoints. For the PoC stage at least the following endpoints
must be implemented:

    service KV {
      // Range gets the keys in the range from the key-value store.
      rpc Range(RangeRequest) returns (RangeResponse) {
      }
    
      // Put puts the given key into the key-value store.
      // A put request increments the revision of the key-value store
      // and generates one event in the event history.
      rpc Put(PutRequest) returns (PutResponse) {
      }
    
      // DeleteRange deletes the given range from the key-value store.
      // A delete request increments the revision of the key-value store
      // and generates a delete event in the event history for every deleted key.
      rpc DeleteRange(DeleteRangeRequest) returns (DeleteRangeResponse) {
      }
    
      // Txn processes multiple requests in a single transaction.
      // A txn request increments the revision of the key-value store
      // and generates events with the same revision for every completed request.
      // It is not allowed to modify the same key several times within one txn.
      rpc Txn(TxnRequest) returns (TxnResponse) {
      }
    
      // Compact compacts the event history in the etcd key-value store. The key-value
      // store should be periodically compacted or the event history will continue to grow
      // indefinitely.
      rpc Compact(CompactionRequest) returns (CompactionResponse) {
      }
    }
    
    service Watch {
      // Watch watches for events happening or that have happened. Both input and output
      // are streams; the input stream is for creating and canceling watchers and the output
      // stream sends events. One watch RPC can watch on multiple key ranges, streaming events
      // for several watches at once. The entire event history can be watched starting from the
      // last compaction revision.
      rpc Watch(stream WatchRequest) returns (stream WatchResponse) {
      }
    }
    
# Watch mechanics for PoC
For the PoC stage, we can implement ETCD watch service using Ignite Continuous Queries by subscribing to updates
in the 'actual' cache, ordering the updates according to the update ``version`` in-memory and streaming the ordered
udpates to clients once the safe interval is closed (see the design doc).
    
# Open Questions
#### Does Kubernetes rely on KV versions to be contiguous?
#### Are there any key subgroups for which Kubernetes does not need global ordering (POD level, Cluster level, etc)
#### Does the concept of ``version`` escape APIServer? Can we replace it with an n-tuple?
#### Does Kubernetes rely on the version to be unique for each update (in other words, if two updates are concurrent, can we assign the same version to them)?
#### Will Kubernetes tolerate watch events coalescing for a key (given updates 1, 2, 3, send only 3)? Some class of keys?
#### Does Kubernetes use leases?  