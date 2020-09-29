package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;

/**
 * Etcd Cluster simulator: global revision, cluster and member IDs, etc.
 */
final class EtcdCluster {
    EtcdCluster(Ignite ignite) {
        rev = ignite.atomicLong("etcd_rev", 1, true);
    }

    static Rpc.ResponseHeader getHeader(long rev) {
        return Rpc.ResponseHeader.newBuilder()
            .setClusterId(111L)
            .setMemberId(222L)
            .setRevision(rev)
            .setRaftTerm(4)
            .build();
    }

    /**
     * @return The key space maintains multiple revisions. When the store is created, the initial revision is 1.
     * Each atomic mutative operation (e.g., a transaction operation may contain multiple operations) creates a new
     * revision on the key space. All data held by previous revisions remains unchanged. Old versions of key can
     * still be accessed through previous revisions. Likewise, revisions are indexed as well; ranging over revisions
     * with watchers is efficient. If the store is compacted to save space, revisions before the compact revision
     * will be removed. Revisions are monotonically increasing over the lifetime of a cluster.
     * For watch progress responses, the revision indicates progress. All future events received in the stream are
     * guaranteed to have a higher revision number than the header.revision number.
     */
    long revision() {
        return rev.get();
    }

    long incrementRevision() {
        return rev.incrementAndGet();
    }

    private final IgniteAtomicLong rev;
}
