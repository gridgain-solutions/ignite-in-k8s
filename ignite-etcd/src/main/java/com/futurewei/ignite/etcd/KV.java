package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

// TODO: protobuf-agnostic
public final class KV {
    private Cache<Key, Value> store = Caching
            .getCachingProvider()
            .getCacheManager()
            .createCache("etcd", new MutableConfiguration<>());

    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(Context.getHeader(Context.revision()));
        ByteString reqKey = req.getKey();

        if (reqKey != null && reqKey.size() > 0) {
            // TODO: versioning
            Key k = new Key(req.getKey(), 1);
            Value v = store.get(k);

            if (v != null) {
                Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                        .setKey(k.data())
                        .setVersion(k.version())
                        .setValue(v.data())
                        .setCreateRevision(v.createRevision())
                        .setModRevision(v.modifyRevision());

                res.setCount(1).addKvs(kv);
            }
        }

        return res.build();
    }

    public Rpc.PutResponse put(Rpc.PutRequest req) {
        ByteString reqKey = req.getKey();
        ByteString reqVal = req.getValue();
        long rev;

        if (reqKey != null && reqKey.size() > 0 && reqVal != null && reqVal.size() > 0) {
            rev = Context.incrementRevision();

            // TODO: versioning, atomicity
            Key k = new Key(reqKey, 1);
            Value curVal = store.get(k);

            Value v = curVal == null
                    ? new Value(reqVal, rev, rev)
                    : new Value(reqVal, curVal.createRevision(), rev);

            store.put(k, v);
        }
        else
            rev = Context.revision();

        return Rpc.PutResponse.newBuilder().setHeader(Context.getHeader(rev)).build();
    }

    public Rpc.DeleteRangeResponse deleteRange(Rpc.DeleteRangeRequest req) {
        ByteString reqKey = req.getKey();
        long rev;
        long cnt = 0;

        if (reqKey != null && reqKey.size() > 0) {
            rev = Context.incrementRevision();

            // TODO: versioning
            Key k = new Key(reqKey, 1);

            if (store.remove(k))
                cnt++;
        }
        else
            rev = Context.revision();

        return Rpc.DeleteRangeResponse.newBuilder().setHeader(Context.getHeader(rev)).setDeleted(cnt).build();
    }

    public Rpc.TxnResponse txn(Rpc.TxnRequest req) {
        return Rpc.TxnResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    public Rpc.CompactionResponse compact(Rpc.CompactionRequest req) {
        return Rpc.CompactionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}