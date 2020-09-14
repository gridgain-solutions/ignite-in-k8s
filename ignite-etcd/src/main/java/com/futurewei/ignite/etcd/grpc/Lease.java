package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.LeaseGrpc;
import etcdserverpb.Rpc;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class Lease extends LeaseGrpc.LeaseImplBase {
    private final com.futurewei.ignite.etcd.Lease impl;

    public Lease(Ignite ignite, String cacheName, String kvCacheName, String histCacheName) {
        impl = new com.futurewei.ignite.etcd.Lease(ignite, cacheName, kvCacheName, histCacheName);
    }

    @Override
    public void leaseGrant(Rpc.LeaseGrantRequest req, StreamObserver<Rpc.LeaseGrantResponse> res) {
        res.onNext(impl.leaseGrant(req));
        res.onCompleted();
    }

    @Override
    public void leaseRevoke(Rpc.LeaseRevokeRequest req, StreamObserver<Rpc.LeaseRevokeResponse> res) {
        res.onNext(impl.leaseRevoke(req));
        res.onCompleted();
    }

    @Override
    public StreamObserver<Rpc.LeaseKeepAliveRequest> leaseKeepAlive(StreamObserver<Rpc.LeaseKeepAliveResponse> res) {
        return new StreamObserver<>() {
            private CompletableFuture<?> keepAlive = null;

            @Override
            public void onNext(Rpc.LeaseKeepAliveRequest req) {
                keepAlive = impl.leaseKeepAlive(req, res::onNext);
            }

            @Override
            public void onError(Throwable ignored) {
                if (keepAlive != null)
                    keepAlive.cancel(true);
            }

            @Override
            public void onCompleted() {
                res.onCompleted();
            }
        };
    }

    @Override
    public void leaseTimeToLive(Rpc.LeaseTimeToLiveRequest req, StreamObserver<Rpc.LeaseTimeToLiveResponse> res) {
        res.onNext(impl.leaseTimeToLive(req));
        res.onCompleted();
    }

    @Override
    public void leaseLeases(Rpc.LeaseLeasesRequest req, StreamObserver<Rpc.LeaseLeasesResponse> res) {
        res.onNext(impl.leaseLeases(req));
        res.onCompleted();
    }
}
