# Ignite-to-ETCD Shim Layer
## Goals
The goal of this project is to asses a possibility of replacing ETCD with Ignite as a storage layer for Kubernetes 
APIserver. Based on the previous attempts to provide a pluggable storage for Kubernetes, the solution must meet the
following functional and non-functional criteria:
#### Reliable Watch
Reliable watch will be implemented as a combination of multi-version KV storage built on top of Ignite native KV 
storage and Ignite Continuous Queries feature. The implementation details are described in the KV and Watch service 
description.
#### Snapshot
While it is not clear whether the Snapshot functionality is used by Kubernetes, the feature implementation is 
straightforward by freezing the MVCC compaction and exporting all values up to the given revision.
#### Pagination
Ignite provides pagination for SQL queries and Continuous queries. On top of that, pagination will be implemented in the
Shim layer based on the revision counter.
#### Multi-object transactions & Optimistic concurrency
Ignite natively supports multi-key cross-cache transactions and both Optimistic and Pessimistic concurrency for 
transactions.
#### Recursive multi-object watch
Ignite continuous queries provide callback which is fired on each object modification. The necessary level of object 
introspection and filtering will be implemented in the Shim Layer.
#### Multiple indexes support
Ignite provides secondary indices and SQL interface for querying data using these indices.
## Non-goals
Provide an identical set of functionality as in the ETCD project. The replacement may be further adjusted and optimized
for specific scenarios run by the Kubernetes API server component.
# ETCD service endpoints
To facilitate the Kubernetes-Shim interoperability, the Shim will implement the gRCP services exposed by ETCD based on
the ```.proto``` services and messages definitions from the ETCD project [1].
## ☑ KV Service (REQUIRED)
KV service provides an interface for multi-version KV store with linearizable updates to all keys. The service
maintains a monotonic gloval update counter (UC) for all updates applied by the service, thus providing a global 
total order for all the updates.

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

For each KV pair stored the service provides three automatically generated values maintained by the storage layer. The
values are ``revision``, ``mod_revision`` and ``version``. The semantics of these values is as follows:
 * ``revision`` is the value of the UC when the value for the given key is first written
 * ``mod_revision`` is the value of the UC at the moment of the latest update
 * ``version`` is a key-local counter that is incremented on each update
 
Kubernetes uses ``mod_revision`` field as a ``ResourceVersion`` in its API server.

We assume the following requirements for the ``mod_revision`` by Kubernetes:
 * ``mod_revision`` assigned to a KV pair is unique across the whole KV storage, i.e. there may be no two KV pairs 
 with the same ``mod_revision``
 * ``mod_revision`` is monotonic, but not necesserily contiguous, i.e. for two consequtive updates ``mod_revision`` can
 be incremented by more than 1
 * ``mod_revision`` provides externally-consistent global ordering of all updates performed by the KV service. This 
 requirement most likely may be relaxed.
    
## ☑ Watch Service (REQUIRED)
Watch service allows requester to receive updates applied to the given key range starting a given version assigned by
the KV service.

    service Watch {
      // Watch watches for events happening or that have happened. Both input and output
      // are streams; the input stream is for creating and canceling watchers and the output
      // stream sends events. One watch RPC can watch on multiple key ranges, streaming events
      // for several watches at once. The entire event history can be watched starting from the
      // last compaction revision.
      rpc Watch(stream WatchRequest) returns (stream WatchResponse) {
      }
    }

## ☑ Lease Service (REQUIRED)
Lease service allows ETCD clients to implement KV pairs which lifetime is bound to the lifetime of a lease, which in 
turn is time-bounded, but may be renewed (in other words, lease service provides a way to implement a consisten 
expiration mechanics). 

    service Lease {
      // LeaseGrant creates a lease which expires if the server does not receive a keepAlive
      // within a given time to live period. All keys attached to the lease will be expired and
      // deleted if the lease expires. Each expired key generates a delete event in the event history.
      rpc LeaseGrant(LeaseGrantRequest) returns (LeaseGrantResponse) {
      }
    
      // LeaseRevoke revokes a lease. All keys attached to the lease will expire and be deleted.
      rpc LeaseRevoke(LeaseRevokeRequest) returns (LeaseRevokeResponse) {
      }
    
      // LeaseKeepAlive keeps the lease alive by streaming keep alive requests from the client
      // to the server and streaming keep alive responses from the server to the client.
      rpc LeaseKeepAlive(stream LeaseKeepAliveRequest) returns (stream LeaseKeepAliveResponse) {
      }
    
      // LeaseTimeToLive retrieves lease information.
      rpc LeaseTimeToLive(LeaseTimeToLiveRequest) returns (LeaseTimeToLiveResponse) {
      }
    
      // LeaseLeases lists all existing leases.
      rpc LeaseLeases(LeaseLeasesRequest) returns (LeaseLeasesResponse) {
      }
    }
    
## Cluster Service (NOT APPLICABLE)
Cluster service is resposible for managing the ETCD cluster topology (e.g. adding and removing cluster 
members, listing the cluster members). We assume that this service endpoint is not directly used by Kubernetes since
the backend management is done by the ETCD administrator. This assumption is to be verified by integration tests.
## Maintenance Service (IN DOUBT)
Maintenance service provides utility methods to run several maintenance operations, such as defragmentation, snapshot
creation and leadership transfer for ETCD cluster. Some service methods may be used by Kubernetes; the full list
of required methods to be identified by integration tests.

    service Maintenance {
      // Alarm activates, deactivates, and queries alarms regarding cluster health.
      rpc Alarm(AlarmRequest) returns (AlarmResponse) {
      }
    
      // Status gets the status of the member.
      rpc Status(StatusRequest) returns (StatusResponse) {
      }
    
      // Defragment defragments a member's backend database to recover storage space.
      rpc Defragment(DefragmentRequest) returns (DefragmentResponse) {
      }
    
      // Hash computes the hash of whole backend keyspace,
      // including key, lease, and other buckets in storage.
      // This is designed for testing ONLY!
      // Do not rely on this in production with ongoing transactions,
      // since Hash operation does not hold MVCC locks.
      // Use "HashKV" API instead for "key" bucket consistency checks.
      rpc Hash(HashRequest) returns (HashResponse) {
      }
    
      // HashKV computes the hash of all MVCC keys up to a given revision.
      // It only iterates "key" bucket in backend storage.
      rpc HashKV(HashKVRequest) returns (HashKVResponse) {
      }
    
      // Snapshot sends a snapshot of the entire backend from a member over a stream to a client.
      rpc Snapshot(SnapshotRequest) returns (stream SnapshotResponse) {
      }
    
      // MoveLeader requests current leader node to transfer its leadership to transferee.
      rpc MoveLeader(MoveLeaderRequest) returns (MoveLeaderResponse) {
      }
    }

## Authentication Service (IN DOUBT)
Authentication service is used to establish a secure connection to ETCD cluster. The service methods provide a 
simple user-role-permission model. Regardless of whether this service is used by Kubernetes, the implementation
seems to be straightforward.

    service Auth {
      // AuthEnable enables authentication.
      rpc AuthEnable(AuthEnableRequest) returns (AuthEnableResponse) {
      }
    
      // AuthDisable disables authentication.
      rpc AuthDisable(AuthDisableRequest) returns (AuthDisableResponse) {
      }
    
      // Authenticate processes an authenticate request.
      rpc Authenticate(AuthenticateRequest) returns (AuthenticateResponse) {
      }
    
      // UserAdd adds a new user. User name cannot be empty.
      rpc UserAdd(AuthUserAddRequest) returns (AuthUserAddResponse) {
      }
    
      // UserGet gets detailed user information.
      rpc UserGet(AuthUserGetRequest) returns (AuthUserGetResponse) {
      }
    
      // UserList gets a list of all users.
      rpc UserList(AuthUserListRequest) returns (AuthUserListResponse) {
      }
    
      // UserDelete deletes a specified user.
      rpc UserDelete(AuthUserDeleteRequest) returns (AuthUserDeleteResponse) {
      }
    
      // UserChangePassword changes the password of a specified user.
      rpc UserChangePassword(AuthUserChangePasswordRequest) returns (AuthUserChangePasswordResponse) {
      }
    
      // UserGrant grants a role to a specified user.
      rpc UserGrantRole(AuthUserGrantRoleRequest) returns (AuthUserGrantRoleResponse) {
      }
    
      // UserRevokeRole revokes a role of specified user.
      rpc UserRevokeRole(AuthUserRevokeRoleRequest) returns (AuthUserRevokeRoleResponse) {
      }
    
      // RoleAdd adds a new role. Role name cannot be empty.
      rpc RoleAdd(AuthRoleAddRequest) returns (AuthRoleAddResponse) {
      }
    
      // RoleGet gets detailed role information.
      rpc RoleGet(AuthRoleGetRequest) returns (AuthRoleGetResponse) {
      }
    
      // RoleList gets lists of all roles.
      rpc RoleList(AuthRoleListRequest) returns (AuthRoleListResponse) {
      }
    
      // RoleDelete deletes a specified role.
      rpc RoleDelete(AuthRoleDeleteRequest) returns (AuthRoleDeleteResponse) {
      }
    
      // RoleGrantPermission grants a permission of a specified key or range to a specified role.
      rpc RoleGrantPermission(AuthRoleGrantPermissionRequest) returns (AuthRoleGrantPermissionResponse) {
      }
    
      // RoleRevokePermission revokes a key or range permission of a specified role.
      rpc RoleRevokePermission(AuthRoleRevokePermissionRequest) returns (AuthRoleRevokePermissionResponse) {
      }
    }

# Implementing watch version in Shim
## Maintaining revisions
The shim layer will introduce a counter to assign the ``mod_revision`` for each update. The possible mechanics for the
counter updates are (the applicability of each mechanic is the subject of further investigation):
 * Shim implements counter as an Ignite key which is updated on each KV service operation. This implementation provides
 strict correspondence to the original ETCD implementation, but induces contention on the counter which limits the 
 overall throughput of updates to KV store. We consider this implementation to be sufficient for the PoC stage.
 * Shim implements counter as an in-memory counter which upper-bound value is persisted to an Ignite key, similarly to
 the Google Percolator timestamp oracle [2]. This implementation provides a significantly higher throughput than a 
 per-update version increment, but introduces differences from the original ETCD behavior. Namely, after a timestamp
 oracle crash, the next update timestamp will be reloaded from the stored upper-bound, leaving a 'gap' in the versions
 stored in the KV service: there will be a range of versions for which there are no corresponding updates stored in the
 KV service.
 * Use a hybrid approach combining local timestamps, node identifiers and local counters. In this case, a 3-tuple is 
 encoded in the 64-bit value having (from MSB to LSB) local timestamp, local update counter and node identifier. 
 
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
counter update mechanics. According to the ETCD semantics, each individual update receives a unique ``mod_revision``. 
At the same time, a multi-key transaction is considered to be a single update thus multiple key-value pairs will receive
the same ``mod_revision`` (the ``mod_revision`` remains to be unique across other update operations). We can exploit
this property combined with the multi-versioning to ensure transactional watch updates. A `SELECT` of all updates
ordered by ``mod_version`` form a stream of transactional updates history where each transaction is a set of keys 
sharing the same ``mod_version``. The implementation with Ignite SQL is quite straightforward.

The ongoing updates can be delivered to the watch agents both via Ignite Continuous Queries and accumulated in-memory
(in case when the watch client keeps up with the update rate), or obtained in batches via SQL queries based on version
ranges.
# Implementing Multi-Versioning in Shim
Ignite does not yet support MVCC natively, and ETCD exposes multiple versions of the same key via an API. To facilitate 
this behavior, shim layer implements multi-version ETCD-like key-value store in Ignite on application level by embedding
the modification version in Ignite key. For better performance, shim maintains both the storage of the latest (actual) 
key-value pairs, and historical storage allowing shim to read in the past.

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

# Preliminary Test Results and Possible Issues
* Current implementation of the range queries uses SQL, which is not transactional in Ignite. This will require 
additional synchronization/filtering to coordinate Shim transaction request and range queries.
* The strategy of scalable ``mod_revision`` assignment is still unclear. A single ``mod_revision`` oracle does not look
like a viable solution since it becomes a bottleneck for the whole system. The most promising soltion is the 
compound timestamp-based ``mod_revision``.
* Authentication and cluster services are left unimplemented. We assume no technical risks in implementing the 
authentication service and assume no need to implement the management service.
* Some integration tests fail. The root cause breakdown can be found here: [Kubernetes Integration Test Report](integration-test-report.md)

# Links
[1] https://github.com/etcd-io/etcd/blob/release-3.4/etcdserver/etcdserverpb/rpc.proto

[2] http://notes.stephenholiday.com/Percolator.pdf
