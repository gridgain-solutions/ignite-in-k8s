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
    private final Cache<byte[], Value> cache;
    private final Cache<HistoricalKey, HistoricalValue> histCache;
    private final Context ctx;

    public KV(Ignite ignite, String cacheName, String histCacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        histCache = ignite.getOrCreateCache(CacheConfig.KVHistory(histCacheName));
        ctx = new Context(ignite);
    }

    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(Context.getHeader(ctx.revision()));

        // the first key for the range. If range_end is not given, the request only looks up key.
        ByteString reqKey = req.getKey();

        if (reqKey == null || reqKey.isEmpty())
            return res.build();

        // the upper bound on the requested range [key, range_end).
        // If range_end is '\0', the range is all keys >= key.
        // If range_end is key plus one (e.g., "aa"+1 == "ab", "a\xff"+1 == "b"),
        // then the range request gets all keys prefixed with key.
        // If both key and range_end are '\0', then the range request returns all keys.
        ByteString rangeEnd = req.getRangeEnd();

        // a limit on the number of keys returned for the request. When limit is set to 0, it is treated as no limit.
        long limit = req.getLimit();

        // revision is the point-in-time of the key-value store to use for the range.
        // If revision is less or equal to zero, the range is over the newest key-value store.
        // If the revision has been compacted, ErrCompacted is returned as a response.
        long rev = req.getRevision();

        // sort_order is the order for returned sorted results.
        Rpc.RangeRequest.SortOrder sortOrder = req.getSortOrder();

        // sort_target is the key-value field to use for sorting.
        Rpc.RangeRequest.SortTarget sortTarget = req.getSortTarget();

        // when set returns only the keys and not the values.
        boolean keysOnly = req.getKeysOnly();

        // when set returns only the count of the keys in the range.
        boolean cntOnly = req.getCountOnly();

        // the lower bound for returned key mod revisions; all keys with lesser mod revisions will be filtered away.
        long minModRev = req.getMinModRevision();

        // the upper bound for returned key mod revisions; all keys with greater mod revisions will be filtered away.
        long maxModRev = req.getMaxModRevision();

        // the lower bound for returned key create revisions; all keys with lesser create revisions will be filtered
        // away.
        long minCrtRev = req.getMinCreateRevision();

        // the upper bound for returned key create revisions; all keys with greater create revisions will be filtered
        // away.
        long maxCrtRev = req.getMaxCreateRevision();

        // TODO: versioning
        ByteString bsk = req.getKey();
        byte[] k = bsk.toByteArray();
        Value v = cache.get(k);

        if (v != null) {
            Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                .setKey(bsk)
                .setVersion(v.version())
                .setValue(ByteString.copyFrom(v.value()))
                .setCreateRevision(v.createRevision())
                .setModRevision(v.modifyRevision());

            res.setCount(1).addKvs(kv);
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
            byte[] k = reqKey.toByteArray();
            Value curVal = cache.get(k);

            Value v = curVal == null
                ? new Value(reqVal.toByteArray(), rev, rev, 1)
                : new Value(reqVal.toByteArray(), curVal.createRevision(), rev, 1);

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
            byte[] k = reqKey.toByteArray();

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
            byte[] k = key.toByteArray();
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