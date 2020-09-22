package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.MaintenanceGrpc;
import etcdserverpb.Rpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

public class Maintenance extends MaintenanceGrpc.MaintenanceImplBase {
    private final com.futurewei.ignite.etcd.Maintenance impl;

    public Maintenance(Ignite ignite) {
        impl = new com.futurewei.ignite.etcd.Maintenance(ignite);
    }

    @Override
    public void alarm(Rpc.AlarmRequest req, StreamObserver<Rpc.AlarmResponse> res) {
        try {
            res.onNext(impl.alarm(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void status(Rpc.StatusRequest req, StreamObserver<Rpc.StatusResponse> res) {
        try {
            res.onNext(impl.status(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void defragment(Rpc.DefragmentRequest req, StreamObserver<Rpc.DefragmentResponse> res) {
        try {
            res.onNext(impl.defragment(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void hash(Rpc.HashRequest req, StreamObserver<Rpc.HashResponse> res) {
        try {
            res.onNext(impl.hash(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void snapshot(Rpc.SnapshotRequest req, StreamObserver<Rpc.SnapshotResponse> res) {
        try {
            res.onNext(impl.snapshot(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void moveLeader(Rpc.MoveLeaderRequest req, StreamObserver<Rpc.MoveLeaderResponse> res) {
        try {
            res.onNext(impl.moveLeader(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }
}
