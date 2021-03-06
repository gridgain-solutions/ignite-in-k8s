package com.futurewei.ignite.etcd.grpc;

import etcdserverpb.KVGrpc;
import etcdserverpb.Rpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.apache.ignite.Ignite;

public final class KV extends KVGrpc.KVImplBase {
    private final com.futurewei.ignite.etcd.KV impl;

    public KV(Ignite ignite, String cacheName, String histCacheName, String leaseCacheName) {
        impl = new com.futurewei.ignite.etcd.KV(ignite, cacheName, histCacheName, leaseCacheName);
    }

    @Override
    public void range(Rpc.RangeRequest req, StreamObserver<Rpc.RangeResponse> res) {
        try {
            res.onNext(impl.range(req));
            res.onCompleted();
        } catch (IndexOutOfBoundsException ex) {
            res.onError(new StatusException(Status.OUT_OF_RANGE.withDescription(ex.getMessage())));
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void put(Rpc.PutRequest req, StreamObserver<Rpc.PutResponse> res) {
        try {
            res.onNext(impl.put(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void deleteRange(Rpc.DeleteRangeRequest req, StreamObserver<Rpc.DeleteRangeResponse> res) {
        try {
            res.onNext(impl.deleteRange(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void txn(Rpc.TxnRequest req, StreamObserver<Rpc.TxnResponse> res) {
        try {
            res.onNext(impl.txn(req));
            res.onCompleted();
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }

    @Override
    public void compact(Rpc.CompactionRequest req, StreamObserver<Rpc.CompactionResponse> res) {
        try {
            res.onNext(impl.compact(req));
            res.onCompleted();
        } catch (IndexOutOfBoundsException ex) {
            res.onError(new StatusException(Status.OUT_OF_RANGE.withDescription(ex.getMessage())));
        } catch (Throwable t) {
            res.onError(new StatusException(Status.UNKNOWN.withDescription(t.getCause().getMessage())));
        }
    }
}
