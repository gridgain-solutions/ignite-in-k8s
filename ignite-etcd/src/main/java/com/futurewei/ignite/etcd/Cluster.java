package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;

// TODO: protobuf-agnostic
public final class Cluster {
    public Rpc.MemberAddResponse memberAdd(Rpc.MemberAddRequest req) {
        return Rpc.MemberAddResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.MemberRemoveResponse memberRemove(Rpc.MemberRemoveRequest req) {
        return Rpc.MemberRemoveResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.MemberUpdateResponse memberUpdate(Rpc.MemberUpdateRequest req) {
        return Rpc.MemberUpdateResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.MemberListResponse memberList(Rpc.MemberListRequest req) {
        return Rpc.MemberListResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.MemberPromoteResponse memberPromote(Rpc.MemberPromoteRequest req) {
        return Rpc.MemberPromoteResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}
