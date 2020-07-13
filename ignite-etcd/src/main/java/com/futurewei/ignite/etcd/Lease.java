package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;

// TODO: protobuf-agnostic
public final class Lease {
    public Rpc.LeaseGrantResponse leaseGrant(Rpc.LeaseGrantRequest req) {
        return Rpc.LeaseGrantResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.LeaseRevokeResponse leaseRevoke(Rpc.LeaseRevokeRequest req) {
        return Rpc.LeaseRevokeResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.LeaseKeepAliveResponse leaseKeepAlive(Rpc.LeaseKeepAliveRequest req) {
        return Rpc.LeaseKeepAliveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.LeaseTimeToLiveResponse leaseTimeToLive(Rpc.LeaseTimeToLiveRequest req) {
        return Rpc.LeaseTimeToLiveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.LeaseLeasesResponse leaseLeases(Rpc.LeaseLeasesRequest req) {
        return Rpc.LeaseLeasesResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
