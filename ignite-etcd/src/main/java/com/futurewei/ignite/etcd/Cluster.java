package com.futurewei.ignite.etcd;

import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;

public final class Cluster {
    private final Context ctx;

    public Cluster(Ignite ignite) {
        ctx = new Context(ignite);
    }

    public Rpc.MemberAddResponse memberAdd(Rpc.MemberAddRequest req) {
        return Rpc.MemberAddResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    public Rpc.MemberRemoveResponse memberRemove(Rpc.MemberRemoveRequest req) {
        return Rpc.MemberRemoveResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    public Rpc.MemberUpdateResponse memberUpdate(Rpc.MemberUpdateRequest req) {
        return Rpc.MemberUpdateResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    public Rpc.MemberListResponse memberList(Rpc.MemberListRequest req) {
        return Rpc.MemberListResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    public Rpc.MemberPromoteResponse memberPromote(Rpc.MemberPromoteRequest req) {
        return Rpc.MemberPromoteResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }
}
