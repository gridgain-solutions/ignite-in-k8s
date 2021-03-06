package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;

public class Maintenance {
    private final EtcdCluster ctx;

    public Maintenance(Ignite ignite) {
        ctx = new EtcdCluster(ignite);
    }

    public Rpc.AlarmResponse alarm(Rpc.AlarmRequest req) {
        return Rpc.AlarmResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.StatusResponse status(Rpc.StatusRequest req) {
        return Rpc.StatusResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.DefragmentResponse defragment(Rpc.DefragmentRequest req) {
        return Rpc.DefragmentResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.HashResponse hash(Rpc.HashRequest req) {
        return Rpc.HashResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.SnapshotResponse snapshot(Rpc.SnapshotRequest req) {
        return Rpc.SnapshotResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    public Rpc.MoveLeaderResponse moveLeader(Rpc.MoveLeaderRequest req) {
        return Rpc.MoveLeaderResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }
}
