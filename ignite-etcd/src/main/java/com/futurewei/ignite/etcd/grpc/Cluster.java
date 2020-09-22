package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.ClusterGrpc;
import etcdserverpb.Rpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

public final class Cluster extends ClusterGrpc.ClusterImplBase {
    private final com.futurewei.ignite.etcd.Cluster impl;

    public Cluster(Ignite ignite) {
        impl = new com.futurewei.ignite.etcd.Cluster(ignite);
    }

    @Override
    public void memberAdd(Rpc.MemberAddRequest req, StreamObserver<Rpc.MemberAddResponse> res) {
        try {
            res.onNext(impl.memberAdd(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void memberRemove(Rpc.MemberRemoveRequest req, StreamObserver<Rpc.MemberRemoveResponse> res) {
        try {
            res.onNext(impl.memberRemove(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void memberUpdate(Rpc.MemberUpdateRequest req, StreamObserver<Rpc.MemberUpdateResponse> res) {
        try {
            res.onNext(impl.memberUpdate(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void memberList(Rpc.MemberListRequest req, StreamObserver<Rpc.MemberListResponse> res) {
        try {
            res.onNext(impl.memberList(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void memberPromote(Rpc.MemberPromoteRequest req, StreamObserver<Rpc.MemberPromoteResponse> res) {
        try {
            res.onNext(impl.memberPromote(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }
}
