package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class KV {
    private final IgniteCache<Key, Value> cache;
    private final IgniteCache<HistoricalKey, HistoricalValue> histCache;
    private final Context ctx;

    public KV(Ignite ignite, String cacheName, String histCacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        histCache = ignite.getOrCreateCache(CacheConfig.KVHistory(histCacheName));
        ctx = new Context(ignite);
    }

    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(Context.getHeader(ctx.revision()));

        // the first key for the range. If range_end is not given, the request only looks up key.
        ByteString key = req.getKey();

        // the upper bound on the requested range [key, range_end).
        // If range_end is '\0', the range is all keys >= key.
        // If range_end is key plus one (e.g., "aa"+1 == "ab", "a\xff"+1 == "b"),
        // then the range request gets all keys prefixed with key.
        // If both key and range_end are '\0', then the range request returns all keys.
        ByteString rangeEnd = req.getRangeEnd();

        // when set returns only the count of the keys in the range.
        boolean cntOnly = req.getCountOnly();

        // when set returns only the keys and not the values.
        boolean keysOnly = req.getKeysOnly();

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

        // the lower bound for returned key mod revisions; all keys with lesser mod revisions will
        // be filtered away.
        long minModRev = req.getMinModRevision();

        // the upper bound for returned key mod revisions; all keys with greater mod revisions
        // will be filtered away.
        long maxModRev = req.getMaxModRevision();

        // the lower bound for returned key create revisions; all keys with lesser create revisions
        // will be filtered away.
        long minCrtRev = req.getMinCreateRevision();

        // the upper bound for returned key create revisions; all keys with greater create revisions
        // will be filtered away.
        long maxCrtRev = req.getMaxCreateRevision();

        Key start = key.isEmpty() ? null : new Key(key.toByteArray());
        Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());

        if (cntOnly) {
            long cnt = count(start, end, rev, minModRev, maxModRev, minCrtRev, maxCrtRev);
            res.setCount(cnt);
        } else {
            Collection<SimpleImmutableEntry<Key, Value>> kvs = range(
                start,
                end,
                limit,
                rev,
                minModRev,
                maxModRev,
                minCrtRev,
                maxCrtRev,
                keysOnly,
                sortOrder,
                sortTarget
            );

            for (SimpleImmutableEntry<Key, Value> entry : kvs) {
                Value v = entry.getValue();
                Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                    .setKey(ByteString.copyFrom(entry.getKey().key()))
                    .setVersion(v.version())
                    .setCreateRevision(v.createRevision())
                    .setModRevision(v.modifyRevision());

                if (!keysOnly)
                    kv.setValue(ByteString.copyFrom(v.value()));

                res.addKvs(kv);
            }

            res.setCount(kvs.size());
        }

        return res.build();
    }

    public Rpc.PutResponse put(Rpc.PutRequest req) {
        ByteString reqKey = req.getKey();
        ByteString reqVal = req.getValue();
        long lease = req.getLease();
        long rev;

        if (!reqKey.isEmpty() && !reqVal.isEmpty()) {
            // TODO: atomicity
            rev = ctx.incrementRevision();

            Key k = new Key(reqKey.toByteArray());
            Value val = getWithoutPayload(k);
            long ver = val == null ? 1 : val.version();
            long crtRev = val == null ? rev : val.createRevision();

            HistoricalValue hVal = new HistoricalValue(reqVal.toByteArray(), crtRev, ver, lease);

            cache.put(k, new Value(hVal, rev));
            histCache.put(new HistoricalKey(k, rev), hVal);
        } else
            rev = ctx.revision();

        return Rpc.PutResponse.newBuilder().setHeader(Context.getHeader(rev)).build();
    }

    public Rpc.DeleteRangeResponse deleteRange(Rpc.DeleteRangeRequest req) {
        // key is the first key to delete in the range.
        ByteString reqKey = req.getKey();

        // range_end is the key following the last key to delete for the range [key, range_end).
        // If range_end is not given, the range is defined to contain only the key argument.
        // If range_end is one bit larger than the given key, then the range is all the keys
        // with the prefix (the given key).
        // If range_end is '\0', the range is all keys greater than or equal to the key argument.
        ByteString rangeEnd = req.getRangeEnd();

        long rev;
        long cnt = 0;

        if (reqKey != null && reqKey.size() > 0) {
            // TODO: atomicity
            rev = ctx.incrementRevision();

            Key start = new Key(reqKey.toByteArray());
            Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());

            if (!start.isZero() && end == null) {
                // Optimization for single key removal
                if (cache.remove(start)) {
                    putTombstone(start, rev);
                    cnt++;
                }
            } else {
                Set<Key> keyList = keysInRange(start, end);

                cache.removeAll(keyList);
                putTombstone(keyList, rev);

                cnt = keyList.size();
            }
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

    private void putTombstone(Key start, long rev) {
        histCache.put(new HistoricalKey(start, rev), new HistoricalValue(null, 0, 0, 0));
    }

    private void putTombstone(Set<Key> keys, long rev) {
        histCache.putAll(keys.stream().collect(Collectors.toMap(
            k -> new HistoricalKey(k, rev),
            k -> new HistoricalValue(null, 0, 0, 0)
        )));
    }

    private Value getWithoutPayload(Key k) {
        // Use invoke to avoid transferring payload
        return cache.invoke(k, (kv, ignored) -> {
            Value v = kv.getValue();

            if (v == null)
                return null;

            return new Value(null, v.createRevision(), v.modifyRevision(), v.version(), v.lease());
        });
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

            Key k = new Key(key.toByteArray());

            switch (operation) {
                case Rpc.Compare.CompareResult.EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            Value v = cache.get(k);
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return Arrays.equals(lhs, rhs);
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs == rhs;
                    }
                case Rpc.Compare.CompareResult.GREATER_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            Value v = cache.get(k);
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return compare(lhs, rhs) > 0;
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs > rhs;
                    }
                case Rpc.Compare.CompareResult.LESS_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs < rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs < rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            Value v = cache.get(k);
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return compare(lhs, rhs) < 0;
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs < rhs;
                    }
                case Rpc.Compare.CompareResult.NOT_EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.modifyRevision();
                            long rhs = req.getModRevision();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.VALUE_VALUE: {
                            Value v = cache.get(k);
                            byte[] lhs = v == null ? null : v.value();
                            byte[] rhs = req.getValue().toByteArray();
                            return !Arrays.equals(lhs, rhs);
                        }
                        case Rpc.Compare.CompareTarget.LEASE_VALUE:
                            Value v = getWithoutPayload(k);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs != rhs;
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

    private Collection<SimpleImmutableEntry<Key, Value>> range(
        Key start,
        Key end,
        long limit,
        long rev,
        long minModRev,
        long maxModRev,
        long minCrtRev,
        long maxCrtRev,
        boolean noPayload,
        Rpc.RangeRequest.SortOrder sortOrder,
        Rpc.RangeRequest.SortTarget sortTarget
    ) {
        if (allZeroOrNegative(rev, minModRev, maxModRev, minCrtRev, maxCrtRev)) {
            if (!start.isZero() && end == null) {
                // Optimization for getting single entry without filtering
                Value v = noPayload ? getWithoutPayload(start) : cache.get(start);

                return v == null
                    ? Collections.emptyList()
                    : Collections.singletonList(new SimpleImmutableEntry<>(start, v));
            } else if (!noPayload && start.isZero() && end.isZero() && limit <= 0 &&
                sortOrder == Rpc.RangeRequest.SortOrder.NONE
            ) {
                // Optimization for getting all entries with payload without filtering, sorting and limit
                return cache.query(new ScanQuery<Key, Value>()).getAll().stream()
                    .map(e -> new SimpleImmutableEntry<>(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            }
        }

        Collection<SimpleImmutableEntry<Key, Value>> res = new ArrayList<>();
        Collection<Object> sqlArgs = new ArrayList<>();

        String tbl = rev <= 0 ? "ETCD_KV" : "ETCD_KV_HISTORY";

        Collection<String> flds = Arrays.asList("CREATE_REVISION", "MODIFY_REVISION", "VERSION", "LEASE", "KEY");

        if (!noPayload)
            flds.add("VALUE");

        String sqlFilter = sqlFilter(sqlArgs, start, end, minModRev, maxModRev, minCrtRev, maxCrtRev);
        String sqlSort = sqlSort(sortOrder, sortTarget);
        String sqlLimit = limit <= 0 ? "" : "LIMIT " + limit;

        StringBuilder sql = new StringBuilder("SELECT ").append(String.join(", ", flds)).append(" FROM ").append(tbl);

        if (!sqlFilter.isEmpty())
            sql.append("\n").append(sqlFilter);

        if (!sqlSort.isEmpty())
            sql.append("\n").append(sqlSort);

        if (!sqlLimit.isEmpty())
            sql.append("\n").append(sqlLimit);

        for (List<?> row : cache.query(new SqlFieldsQuery(sql.toString()).setArgs(sqlArgs))) {
            Iterator<?> it = row.iterator();

            long crtRev = (Long) it.next();
            long modRev = (Long) it.next();
            long ver = (Long) it.next();
            long lease = (Long) it.next();
            byte[] k = (byte[]) it.next();
            byte[] v = noPayload ? null : (byte[]) it.next();

            res.add(new SimpleImmutableEntry<>(new Key(k), new Value(v, crtRev, modRev, ver, lease)));
        }

        return res;
    }

    private long count(Key start, Key end, long rev, long minModRev, long maxModRev, long minCrtRev, long maxCrtRev) {
        if (allZeroOrNegative(rev, minModRev, maxModRev, minCrtRev, maxCrtRev)) {
            if (!start.isZero() && end == null)
                // Optimization for a popular case to check single entry without filtering
                return cache.containsKey(start) ? 1 : 0;
            else if (start.isZero() && end.isZero())
                // Optimization for counting all entries without filtering
                return cache.size();
        }

        Collection<Object> sqlArgs = new ArrayList<>();

        String tbl = rev <= 0 ? "ETCD_KV" : "ETCD_KV_HISTORY";

        String sqlFilter = sqlFilter(sqlArgs, start, end, minModRev, maxModRev, minCrtRev, maxCrtRev);

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM").append(tbl);

        if (!sqlFilter.isEmpty())
            sql.append("\n").append(sqlFilter);

        List<List<?>> res = cache.query(new SqlFieldsQuery(sql.toString()).setArgs(sqlArgs)).getAll();

        return (Long) res.iterator().next().iterator().next();
    }

    private Set<Key> keysInRange(Key start, Key end) {
        Collection<Object> sqlArgs = new ArrayList<>();
        Collection<String> filterList = new ArrayList<>();

        if (!start.isZero()) {
            filterList.add("KEY " + (end == null ? "=" : ">=") + " ?");
            sqlArgs.add(start);
        }

        if (end != null && !end.isZero()) {
            filterList.add("KEY < ?");
            sqlArgs.add(end);
        }

        StringBuilder sql = new StringBuilder("SELECT KEY FROM ETCD_KV");

        if (!filterList.isEmpty())
            sql.append("\n").append(String.join(" AND ", filterList));

        Set<Key> res = new HashSet<>();

        for (List<?> row : cache.query(new SqlFieldsQuery(sql.toString()).setArgs(sqlArgs)))
            res.add(new Key((byte[]) row.iterator().next()));

        return res;
    }

    private static boolean allZeroOrNegative(long n1, long n2, long n3, long n4, long n5) {
        return n1 <= 0 && n2 <= 0 && n3 <= 0 && n4 <= 0 && n5 <= 0;
    }

    private static String sqlFilter(
        Collection<Object> sqlArgs,
        Key start,
        Key end,
        long minModRev,
        long maxModRev,
        long minCrtRev,
        long maxCrtRev
    ) {
        Collection<String> filterList = new ArrayList<>();

        if (!start.isZero()) {
            filterList.add("KEY " + (end == null ? "=" : ">=") + " ?");
            sqlArgs.add(start);
        }

        if (end != null && !end.isZero()) {
            filterList.add("KEY < ?");
            sqlArgs.add(end);
        }

        if (minModRev > 0) {
            filterList.add("MODIFY_REVISION >= ?");
            sqlArgs.add(minModRev);
        }

        if (maxModRev > 0) {
            filterList.add("MODIFY_REVISION <= ?");
            sqlArgs.add(maxModRev);
        }

        if (minCrtRev > 0) {
            filterList.add("CREATE_REVISION >= ?");
            sqlArgs.add(minCrtRev);
        }

        if (maxCrtRev > 0) {
            filterList.add("CREATE_REVISION <= ?");
            sqlArgs.add(maxCrtRev);
        }

        return String.join(" AND ", filterList);
    }

    private String sqlSort(Rpc.RangeRequest.SortOrder sortOrder, Rpc.RangeRequest.SortTarget sortTarget) {
        if (sortOrder == Rpc.RangeRequest.SortOrder.NONE)
            return "";

        String target;
        switch (sortTarget) {
            case KEY:
                target = "KEY";
                break;
            case VERSION:
                target = "VERSION";
                break;
            case CREATE:
                target = "CREATE_REVISION";
                break;
            case MOD:
                target = "MODIFY_REVISION";
                break;
            default:
                target = "VALUE";
                break;
        }

        return sortOrder == Rpc.RangeRequest.SortOrder.ASCEND ? target : target + " DESC";
    }
}