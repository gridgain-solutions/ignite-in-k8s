package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.Rpc;
import etcdserverpb.WatchGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

public final class Watch extends WatchGrpc.WatchImplBase {
    private final com.futurewei.ignite.etcd.Watch impl;

    public Watch(Ignite ignite, String cacheName) {
        impl = new com.futurewei.ignite.etcd.Watch(ignite, cacheName);
    }

    @Override
    public StreamObserver<Rpc.WatchRequest> watch(StreamObserver<Rpc.WatchResponse> res) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Rpc.WatchRequest req) {
                try {
                    res.onNext(impl.watch(req));
                } catch (InterruptedException e) {
                    // TODO: error handling
                }
            }

            @Override
            public void onError(Throwable t) {
                // TODO: error handling
            }

            @Override
            public void onCompleted() {
                res.onCompleted();
            }
        };
    }
}
