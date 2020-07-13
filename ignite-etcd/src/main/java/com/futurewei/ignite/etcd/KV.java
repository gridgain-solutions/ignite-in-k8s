package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;

@RestController
@RequestMapping("/v3/kv")
public class KV {
    @Autowired
    private Cache<Key, Value> store;

    @PostMapping("/range")
    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(Context.getHeader(Context.revision()));
        ByteString reqKey = req.getKey();

        if (reqKey != null && reqKey.size() > 0) {
            // TODO: versioning
            Key k = new Key(req.getKey(), 1);
            Value v = store.get(k);

            if (v != null)
                res.setKvs(
                        0,
                        Kv.KeyValue.newBuilder()
                                .setKey(k.data())
                                .setVersion(k.version())
                                .setValue(v.data())
                                .setCreateRevision(v.createRevision())
                                .setModRevision(v.modifyRevision())
                ).setCount(1);
        }

        return res.build();
    }

    @PostMapping("/put")
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

    @PostMapping("/deleterange")
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

    @PostMapping("/txn")
    public Rpc.TxnResponse txn(Rpc.TxnRequest req) {
        return Rpc.TxnResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }

    @PostMapping("/compaction")
    public Rpc.CompactionResponse compact(Rpc.CompactionRequest req) {
        return Rpc.CompactionResponse.newBuilder().setHeader(Context.getHeader(Context.revision())).build();
    }
}