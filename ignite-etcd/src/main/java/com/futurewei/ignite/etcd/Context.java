package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;

final class Context {
    Context(Ignite ignite) {
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

    long revision() {
        return rev.get();
    }

    long incrementRevision() {
        return rev.incrementAndGet();
    }

    private final IgniteAtomicLong rev;
}
