package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;
import org.apache.ignite.Ignite;

import javax.cache.Cache;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class KV {
    private final Cache<Key, Value> cache;
    private final Context ctx;

    public KV(Ignite ignite, String cacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        ctx = new Context(ignite);
    }

    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(Context.getHeader(ctx.revision()));
        ByteString reqKey = req.getKey();

        if (reqKey != null && reqKey.size() > 0) {
            // TODO: versioning
            ByteString bsk = req.getKey();
            Key k = new Key(bsk.toByteArray(), 1);
            Value v = cache.get(k);

            if (v != null) {
                Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                        .setKey(bsk)
                        .setVersion(k.version())
                        .setValue(ByteString.copyFrom(v.value()))
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
        long lease = req.getLease();
        long rev;

        if (reqKey != null && reqKey.size() > 0 && reqVal != null && reqVal.size() > 0) {
            rev = ctx.incrementRevision();

            // TODO: versioning
            // TODO: atomicity
            Key k = new Key(reqKey.toByteArray(), 1);
            Value curVal = cache.get(k);

            Value v = curVal == null
                    ? new Value(reqVal.toByteArray(), rev, rev)
                    : new Value(reqVal.toByteArray(), curVal.createRevision(), rev);

            if (lease != 0)
                v.lease(lease);

            cache.put(k, v);
        } else
            rev = ctx.revision();

        return Rpc.PutResponse.newBuilder().setHeader(Context.getHeader(rev)).build();
    }

    public Rpc.DeleteRangeResponse deleteRange(Rpc.DeleteRangeRequest req) {
        ByteString reqKey = req.getKey();
        long rev;
        long cnt = 0;

        if (reqKey != null && reqKey.size() > 0) {
            rev = ctx.incrementRevision();

            // TODO: versioning
            Key k = new Key(reqKey.toByteArray(), 1);

            if (cache.remove(k))
                cnt++;
        } else
            rev = ctx.revision();

        return Rpc.DeleteRangeResponse.newBuilder().setHeader(Context.getHeader(rev)).setDeleted(cnt).build();
    }

    public Rpc.TxnResponse txn(Rpc.TxnRequest req) {
        List<Rpc.Compare> cmpReqList = req.getCompareList();
        Collection<Rpc.ResponseOp> resList = null;
        boolean ok = false;

        if (cmpReqList.size() > 0) {
            // TODO: atomicity
            ok = cmpReqList.stream().allMatch(this::check);

            List<Rpc.RequestOp> reqList = ok ? req.getSuccessList() : req.getFailureList();

            resList = reqList.stream().map(this::exec).collect(Collectors.toList());
        }

        Rpc.TxnResponse.Builder res = Rpc.TxnResponse.newBuilder()
                .setHeader(Context.getHeader(ctx.revision()))
                .setSucceeded(ok);

        if (resList != null)
            res.addAllResponses(resList);

        return res.build();
    }

    public Rpc.CompactionResponse compact(Rpc.CompactionRequest req) {
        return Rpc.CompactionResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    private static int compare(byte[] lhs, byte[] rhs) {
        if (lhs == rhs)
            return 0;

        if (lhs.length != rhs.length)
            return lhs.length - rhs.length;

        for (int i = 0, j = 0; i < lhs.length && j < rhs.length; i++, j++) {
            int a = (lhs[i] & 0xff);
            int b = (rhs[j] & 0xff);
            if (a != b)
                return a - b;
        }

        return 0;
    }

    private boolean check(Rpc.Compare req) {
        ByteString key = req.getKey();

        if (key != null && key.size() > 0) {
            int operation = req.getResultValue();
            int target = req.getTargetValue();

            // TODO: versioning
            Key k = new Key(key.toByteArray(), 1);
            Value v = cache.get(k);

            switch (operation) {
                case Rpc.Compare.CompareResult.EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            // TODO: versioning
                            long lhs = v == null ? 0 : 1;
                            long rhs = req.getVersion();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return Arrays.equals(lhs, rhs);
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            // TODO: leasing
                            return false;
                    }
                case Rpc.Compare.CompareResult.GREATER_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            // TODO: versioning
                            long lhs = v == null ? 0 : 1;
                            long rhs = req.getVersion();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return compare(lhs, rhs) > 0;
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            // TODO: leasing
                            return false;
                    }
                case Rpc.Compare.CompareResult.LESS_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            // TODO: versioning
                            long lhs = v == null ? 0 : 1;
                            long rhs = req.getVersion();
                            return lhs < rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs < rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return compare(lhs, rhs) < 0;
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            // TODO: leasing
                            return false;
                    }
                case Rpc.Compare.CompareResult.NOT_EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            // TODO: versioning
                            long lhs = v == null ? 0 : 1;
                            long rhs = req.getVersion();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return !Arrays.equals(lhs, rhs);
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            // TODO: leasing
                            return false;
                    }
            }
        }

        return false;
    }

    private Rpc.ResponseOp exec(Rpc.RequestOp req) {
        if (req.hasRequestRange())
            return Rpc.ResponseOp.newBuilder().setResponseRange(range(req.getRequestRange())).build();
        if (req.hasRequestPut())
            return Rpc.ResponseOp.newBuilder().setResponsePut(put(req.getRequestPut())).build();
        if (req.hasRequestDeleteRange())
            return Rpc.ResponseOp.newBuilder().setResponseDeleteRange(deleteRange(req.getRequestDeleteRange())).build();
        return Rpc.ResponseOp.newBuilder().setResponseTxn(txn(req.getRequestTxn())).build();
    }
}