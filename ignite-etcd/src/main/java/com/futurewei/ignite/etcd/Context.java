package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;

final class Context {
    static Rpc.ResponseHeader getHeader(long rev) {
        return Rpc.ResponseHeader.newBuilder()
                .setClusterId(111L)
                .setMemberId(222L)
                .setRevision(rev)
                .setRaftTerm(4)
                .build();
    }

    static long revision() {
        return rev;
    }

    static long incrementRevision() {
        return ++rev;
    }

    // TODO: atomic revision
    private static long rev = 1;
}
