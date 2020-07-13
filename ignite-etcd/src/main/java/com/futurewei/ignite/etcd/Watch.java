package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;

// TODO: protobuf-agnostic
public final class Watch {
    public Rpc.WatchResponse watch(Rpc.WatchRequest req) {
        return Rpc.WatchResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
