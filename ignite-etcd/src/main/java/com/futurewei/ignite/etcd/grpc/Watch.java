package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.Rpc;
import etcdserverpb.WatchGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class Watch extends WatchGrpc.WatchImplBase {
    private final com.futurewei.ignite.etcd.Watch impl;
    private final AtomicLong streamId = new AtomicLong(1);

    public Watch(Ignite ignite, String cacheName, String histCacheName) {
        impl = new com.futurewei.ignite.etcd.Watch(ignite, cacheName, histCacheName);
    }

    @Override
    public StreamObserver<Rpc.WatchRequest> watch(final StreamObserver<Rpc.WatchResponse> res) {
        return new StreamObserver<Rpc.WatchRequest>() {
            private final Set<Runnable> cancellations = new HashSet<>();
            private final long id = streamId.getAndIncrement();

            @Override
            public void onNext(Rpc.WatchRequest req) {
                cancellations.add(impl.watch(id, req, r -> {
                    synchronized (res) {
                        res.onNext(r);
                    }
                }));
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
