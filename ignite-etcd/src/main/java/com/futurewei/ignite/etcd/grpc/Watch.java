package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.Rpc;
import etcdserverpb.WatchGrpc;
import io.grpc.stub.StreamObserver;

public final class Watch extends WatchGrpc.WatchImplBase {
    private final com.futurewei.ignite.etcd.Watch impl = new com.futurewei.ignite.etcd.Watch();

    @Override
    public StreamObserver<Rpc.WatchRequest> watch(StreamObserver<Rpc.WatchResponse> res) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Rpc.WatchRequest req) {
                impl.watch(req);
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
