package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.ClusterGrpc;
import etcdserverpb.Rpc;
import io.grpc.stub.StreamObserver;

public final class Cluster extends ClusterGrpc.ClusterImplBase {
    private final com.futurewei.ignite.etcd.Cluster impl = new com.futurewei.ignite.etcd.Cluster();

    @Override
    public void memberAdd(Rpc.MemberAddRequest req, StreamObserver<Rpc.MemberAddResponse> res) {
        res.onNext(impl.memberAdd(req));
        res.onCompleted();
    }

    @Override
    public void memberRemove(Rpc.MemberRemoveRequest req, StreamObserver<Rpc.MemberRemoveResponse> res) {
        res.onNext(impl.memberRemove(req));
        res.onCompleted();
    }

    @Override
    public void memberUpdate(Rpc.MemberUpdateRequest req, StreamObserver<Rpc.MemberUpdateResponse> res) {
        res.onNext(impl.memberUpdate(req));
        res.onCompleted();
    }

    @Override
    public void memberList(Rpc.MemberListRequest req, StreamObserver<Rpc.MemberListResponse> res) {
        res.onNext(impl.memberList(req));
        res.onCompleted();
    }

    @Override
    public void memberPromote(Rpc.MemberPromoteRequest req, StreamObserver<Rpc.MemberPromoteResponse> res) {
        res.onNext(impl.memberPromote(req));
        res.onCompleted();
    }
}
