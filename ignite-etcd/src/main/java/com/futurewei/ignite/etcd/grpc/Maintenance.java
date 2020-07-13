package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.MaintenanceGrpc;
import etcdserverpb.Rpc;
import io.grpc.stub.StreamObserver;

public class Maintenance extends MaintenanceGrpc.MaintenanceImplBase {
    private final com.futurewei.ignite.etcd.Maintenance impl = new com.futurewei.ignite.etcd.Maintenance();

    @Override
    public void alarm(Rpc.AlarmRequest req, StreamObserver<Rpc.AlarmResponse> res) {
        res.onNext(impl.alarm(req));
        res.onCompleted();
    }

    @Override
    public void status(Rpc.StatusRequest req, StreamObserver<Rpc.StatusResponse> res) {
        res.onNext(impl.status(req));
        res.onCompleted();
    }

    @Override
    public void defragment(Rpc.DefragmentRequest req, StreamObserver<Rpc.DefragmentResponse> res) {
        res.onNext(impl.defragment(req));
        res.onCompleted();
    }

    @Override
    public void hash(Rpc.HashRequest req, StreamObserver<Rpc.HashResponse> res) {
        res.onNext(impl.hash(req));
        res.onCompleted();
    }

    @Override
    public void snapshot(Rpc.SnapshotRequest req, StreamObserver<Rpc.SnapshotResponse> res) {
        res.onNext(impl.snapshot(req));
        res.onCompleted();
    }

    @Override
    public void moveLeader(Rpc.MoveLeaderRequest req, StreamObserver<Rpc.MoveLeaderResponse> res) {
        res.onNext(impl.moveLeader(req));
        res.onCompleted();
    }
}
