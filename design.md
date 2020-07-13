# Ignite-to-ETCD Shim Layer
## Goals
## Non-goals
# ETCD service endpoints
To facilitate the Kubernetes-Shim interoperability, the Shim will implement the gRCP services exposed by ETCD based on
the ```.proto``` services and messages definitions from the ETCD project [1].
## KV Service
The ETCD KV service provides an interface for multi-version KV store with linearizable updates to all keys. The service
assigns a monotonic version to all updates applied by the service, thus providing a global total orderfor all updates.
## Watch Service
Watch service allows requester to receive updates applied to the given key range starting a given version assigned by
the KV service.
# Implementing watch version in Shim
## Maintaining update version
The shim layer will introduce a counter to assign a version for each update. The possible mechanics for the
counter updates are (the applicability of each mechanic is the subject of investigation during the PoC phase):
 * Shim implements counter as an Ignite key which is updated on each KV service operation. This implementation provides
 strict correspondence to the original ETCD implementation, but induces contention on the counter which limits the 
 overall throughput of updates to KV store.
 * Shim implements counter as an in-memory counter which upper-bound value is persisted to an Ignite key, similarly to
 the Google Percolator timestamp oracle [2]. This implementation provides a significantly higher throughput than a 
 per-update version increment, but introduces differences from the original ETCD behavior. Namely, after a timestamp
 oracle crash, the next update timestamp will be reloaded from the stored upper-bound, leaving a 'gap' in the versions
 stored in the KV service: there will be a range of versions for which there are no corresponding updates stored in the
 KV service.
 * Shim implements counter as a Hybrid Logical Clock [3] value providing causal ordering of events on different nodes. 
 This implementation does not induce any contention as the HLC value can be calculated locally, but breaks the total
 ordering guarantee provided by ETCD.
 
The key-value update will be executed as

    Ignite transaction {
        lock keys
        acquire update version
        write keys with version
        commit
    }

After the Ignite transaction commits, the corresponding update version becomes eligible for notification.

The update version is exposed to SQL layer and indexed so that the updates stream can be constructed on any node of the 
cluster. 

## Streaming updates to watch clients
The watch lifecycle begins from a watch registration on a watch agent, upon which a client provides a ``start version`` 
for the watch and the watched key range. As updates are applied to the KV storage, watch agents track the safe 
notification intervals and send the updates to watch clients. Safe intervals' definition depends on the version 
counter update mechanics.

The ongoing updates can be delivered to the watch agents both via Ignite Continuous Queries and accumulated in-memory
(in case when the watch client keeps up with the update rate), or obtained in batches via SQL queries based on version
ranges.
# Implementing Multi-Versioning in Shim
Shim layer implements multi-version ETCD-like key-value store in Ignite using a straightforward way of embedding the
version in Ignite key. For speed, shim maintains both the storage of the latest (actual) key-value pairs, and 
historical storage allowing shim to read in the past.

The data model for the key-value pairs is as follows:
    
    Key {
        keyBytes: byte[] // Key bytes as provided by the user.
    }
    
    HistoricalKey {
        data: byte[] // Key bytes as provided by the user
        version: long // Current version
    }
    
    Value {
        data: byte[] // Value bytes as provided by the user
        version: long // Current version
        prevVersion: long // Previous version for historical reads
    }
    
The ``prevVersion`` embedded in the value allows to construct an instance of ``HistoricalKey`` and traverse backwards
in history for any particular key.

Given the data model, querying a history of updates can be implemented as a following SQL query:
```SELECT keyBytes, data, version from HISTORY where version >= ? and version < ?```. Arbitrary key range filter can 
be attached to the given query. 

## ``HistoricalKey`` collocation
The history query above in general case will generate a broadcast query which is suboptimal for the cases of a narrow
version range, and a large number of nodes in Ignite cluster. We can narrow the query topology by using the ``version``
field of ``HistoricalKey`` class as an affinity key and grouping close versions together. For example, putting versions
in buckets of 10 elements (essentially, using ``version / 10`` as an affinity key) we can send the query only
to ``(versionHigh - versionLow) / 10`` cluster nodes, instead of a broadcast query. This approach, however, should be
carefully benchmarked as in most cases all watch agents will likely be reading the same versions range resulting in 
hitting the same nodes anyway.   

# Links
[1] https://github.com/etcd-io/etcd/blob/release-3.4/etcdserver/etcdserverpb/rpc.proto

[2] http://notes.stephenholiday.com/Percolator.pdf

[3] https://cse.buffalo.edu/tech-reports/2014-04.pdf