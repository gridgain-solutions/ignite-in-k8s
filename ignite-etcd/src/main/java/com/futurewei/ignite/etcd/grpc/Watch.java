package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.Rpc;
import etcdserverpb.WatchGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

import java.util.HashSet;
import java.util.Set;

public final class Watch extends WatchGrpc.WatchImplBase {
    private final com.futurewei.ignite.etcd.Watch impl;

    public Watch(Ignite ignite, String cacheName) {
        impl = new com.futurewei.ignite.etcd.Watch(ignite, cacheName);
    }

    @Override
    public StreamObserver<Rpc.WatchRequest> watch(final StreamObserver<Rpc.WatchResponse> res) {
        return new StreamObserver<>() {
            private final Set<Runnable> cancellations = new HashSet<>();

            @Override
            public void onNext(Rpc.WatchRequest req) {
                cancellations.add(impl.watch(req, res::onNext));
            }

            @Override
            public void onError(Throwable ignored) {
                for (Runnable cancel : cancellations)
                    cancel.run();

                cancellations.clear();
            }

            @Override
            public void onCompleted() {
                res.onCompleted();
            }
        };
    }
}
