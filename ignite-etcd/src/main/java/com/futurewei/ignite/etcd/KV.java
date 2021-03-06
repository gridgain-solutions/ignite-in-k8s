package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.apache.ignite.transactions.TransactionOptimisticException;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class KV {
    /** Override transaction timeout (e.g. for debugging). */
    private static final int TX_TIMEOUT = Integer.parseInt(System.getProperty("TX_TIMEOUT", "0"));

    private final Ignite ignite;
    private final IgniteLogger log;
    private final IgniteCache<Key, Value> cache;
    private final IgniteCache<HistoricalKey, HistoricalValue> histCache;
    private final IgniteCache<Long, Ttl> leaseCache;
    private final EtcdCluster ctx;

    public KV(Ignite ignite, String cacheName, String histCacheName, String leaseCacheName) {
        this.ignite = ignite;
        log = ignite.log();
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        histCache = ignite.getOrCreateCache(CacheConfig.KVHistory(histCacheName));
        leaseCache = ignite.getOrCreateCache(CacheConfig.Lease(leaseCacheName));
        ctx = new EtcdCluster(ignite);
    }

    /**
     * Gets the keys in the range from the key-value store.
     *
     * @throws IndexOutOfBoundsException if the specified revision does not exist.
     */
    public Rpc.RangeResponse range(Rpc.RangeRequest req) {
        return transaction(60000, 0, () -> rangeNonTx(req, new TxnContext(ctx), false));
    }

    /**
     * Puts the given key into the key-value store. A put request increments the revision of the key-value store
     * and generates one event in the event history.
     */
    public Rpc.PutResponse put(Rpc.PutRequest req) {
        return transaction(3000, 1, () -> putNonTx(req, new TxnContext(ctx)));
    }

    /**
     * Deletes the given range from the key-value store. A delete request increments the revision of the key-value
     * store and generates a delete event in the event history for every deleted key.
     */
    public Rpc.DeleteRangeResponse deleteRange(Rpc.DeleteRangeRequest req) {
        return transaction(10000, 0, () -> deleteRangeNonTx(req, new TxnContext(ctx)));
    }

    /**
     * Processes multiple requests in a single transaction. A txn request increments the revision of the key-value store
     * and generates events with the same revision for every completed request. It is not allowed to modify the same
     * key several times within one txn.
     */
    public Rpc.TxnResponse txn(Rpc.TxnRequest req) {
        return transaction(60000, 0, () -> txnNonTx(req, new TxnContext(ctx)));
    }

    /**
     * Compacts the event history in the etcd key-value store. The key-value store should be periodically compacted
     * or the event history will continue to grow indefinitely.
     *
     * @throws IndexOutOfBoundsException if the specified revision does not exist.
     */
    public Rpc.CompactionResponse compact(Rpc.CompactionRequest req) {
        // the key-value store revision when the request was applied
        final long curRev = ctx.revision();

        // the key-value store revision for the compaction operation
        long compactRev = req.getRevision();

        if (compactRev == curRev)
            compactRev = curRev - 1;

        if (compactRev > curRev)
            throw new IndexOutOfBoundsException("Required revision is a future revision");
        else if (compactRev > 0) {
            Long delCnt = (Long) histCache.query(
                new SqlFieldsQuery(new SqlFieldsQuery("DELETE FROM ETCD_KV_HISTORY WHERE MODIFY_REVISION <= ?")
                    .setArgs(compactRev))
            ).getAll().iterator().next().iterator().next();

            if (delCnt == 0)
                throw new IndexOutOfBoundsException("Required revision has been compacted");

            if (log.isTraceEnabled())
                log.trace(
                    "Compacting revision " + compactRev + " removed " + delCnt + " entries from ETCD_KV_HISTORY"
                );
        }

        return Rpc.CompactionResponse.newBuilder().setHeader(EtcdCluster.getHeader(curRev)).build();
    }

    /**
     * Optimization to get Value without payload since payloads in Kubernetes can be large.
     *
     * @param k             Key
     * @param txModifiesKey {@code true} if this method is called in a transaction modifying the key.
     * @return Value associated with the key without {@link Value#value()}
     */
    private Value getWithoutPayload(Key k, boolean txModifiesKey) {
        // Do not use Cache.get(k) to avoid transferring Value's payload.
        // IgniteCache#invoke(k) causes subsequent Cache#put(k) to fail with IgniteCheckedException("Failed to enlist
        // write value for key (cannot have update value in transaction after EntryProcessor is applied)").
        // Thus, use IgniteCache#invoke(k) only if there is not Cache#put(k) in the same transaction. Otherwise use
        // IgniteCompute#affiniyCall(k).
        if (txModifiesKey) {
            String cacheName = cache.getName();

            return ignite.compute().affinityCall(cacheName, k, new IgniteCallable<Value>() {
                @IgniteInstanceResource
                private Ignite ignite;

                @Override
                public Value call() {
                    IgniteCache<Key, Value> cache = ignite.cache(cacheName);
                    Value v = cache.get(k);

                    return v == null
                        ? null
                        : new Value(null, v.createRevision(), v.modifyRevision(), v.version(), v.lease());
                }
            });
        } else {
            return cache.invoke(k, (kv, ignored) -> {
                Value v = kv.getValue();

                if (v == null)
                    return null;

                return new Value(null, v.createRevision(), v.modifyRevision(), v.version(), v.lease());
            });
        }
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

        if (key.size() > 0) {
            int operation = req.getResultValue();
            int target = req.getTargetValue();

            Key k = new Key(key.toByteArray());

            switch (operation) {
                case Rpc.Compare.CompareResult.EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs == rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k, true);
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
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs == rhs;
                    }
                case Rpc.Compare.CompareResult.GREATER_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k, true);
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
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs > rhs;
                    }
                case Rpc.Compare.CompareResult.LESS_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs < rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs > rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k, true);
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
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs < rhs;
                    }
                case Rpc.Compare.CompareResult.NOT_EQUAL_VALUE:
                    switch (target) {
                        case Rpc.Compare.CompareTarget.VERSION_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.version();
                            long rhs = req.getVersion();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.CREATE_VALUE: {
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.createRevision();
                            long rhs = req.getCreateRevision();
                            return lhs != rhs;
                        }
                        case Rpc.Compare.CompareTarget.MOD_VALUE: {
                            Value v = getWithoutPayload(k, true);
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
                            Value v = getWithoutPayload(k, true);
                            long lhs = v == null ? 0 : v.lease();
                            long rhs = req.getLease();
                            return lhs != rhs;
                    }
            }
        }

        return false;
    }

    private Rpc.ResponseOp exec(Rpc.RequestOp req, TxnContext txnCtx) {
        if (req.hasRequestRange())
            return Rpc.ResponseOp.newBuilder()
                .setResponseRange(rangeNonTx(req.getRequestRange(), txnCtx, true))
                .build();
        if (req.hasRequestPut())
            return Rpc.ResponseOp.newBuilder().setResponsePut(putNonTx(req.getRequestPut(), txnCtx)).build();
        if (req.hasRequestDeleteRange())
            return Rpc.ResponseOp.newBuilder()
                .setResponseDeleteRange(deleteRangeNonTx(req.getRequestDeleteRange(), txnCtx))
                .build();
        return Rpc.ResponseOp.newBuilder().setResponseTxn(txnNonTx(req.getRequestTxn(), txnCtx)).build();
    }

    /**
     * @param txModifiesKey See {@link #getWithoutPayload(Key, boolean)}.
     */
    private Collection<SimpleImmutableEntry<Key, Value>> range(
        boolean txModifiesKey,
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
        if (rev <= 0 && minModRev <= 0 && maxModRev <= 0 && minCrtRev <= 0 && maxCrtRev <= 0) {
            if (!start.isZero() && end == null) {
                // Optimization for getting single entry without filtering
                Value v = noPayload ? getWithoutPayload(start, txModifiesKey) : cache.get(start);

                return v == null
                    ? Collections.emptyList()
                    : Collections.singletonList(new SimpleImmutableEntry<>(start, v));
            } else if (!noPayload &&
                start.isZero() &&
                end.isZero() &&
                limit <= 0 &&
                sortOrder == Rpc.RangeRequest.SortOrder.NONE) {
                // Optimization for getting all entries with payload without filtering, sorting and limit
                return cache.query(new ScanQuery<Key, Value>()).getAll().stream()
                    .map(e -> new SimpleImmutableEntry<>(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            }
        }

        Collection<SimpleImmutableEntry<Key, Value>> res = new ArrayList<>();
        Collection<Object> sqlArgs = new ArrayList<>();

        String tbl = rev <= 0 ? "ETCD_KV" : "ETCD_KV_HISTORY";

        Collection<String> flds = noPayload
            ? Arrays.asList("CREATE_REVISION", "MODIFY_REVISION", "VERSION", "LEASE", "KEY")
            : Arrays.asList("CREATE_REVISION", "MODIFY_REVISION", "VERSION", "LEASE", "KEY", "VALUE");

        String sqlFilter = sqlFilter(sqlArgs, start, end, minModRev, maxModRev, minCrtRev, maxCrtRev);
        String sqlSort = sqlSort(sortOrder, sortTarget);

        StringBuilder sql = new StringBuilder("SELECT ").append(String.join(", ", flds)).append(" FROM ").append(tbl);

        if (!sqlFilter.isEmpty())
            sql.append("\nWHERE ").append(sqlFilter);

        if (rev > 0) {
            // Get latest from a historical table.
            // Normally, for a historical (versioned) table HIST like
            //     ID VAL VER
            //     a  a1  1
            //     b  b1  2
            //     a  a2  3
            //     b  b2  4
            // this SQL gives the latest {ID, VAL} records:
            //     SELECT ID, VAL FROM HIST AS O
            //       WHERE O.VER <= ? AND O.VER = (SELECT MAX(VER) FROM HIST AS I WHERE I.VER <= ? AND I.ID = O.ID)
            // NB! "WHERE Subquery" limitation:
            // That SQL does not work for H2-based Ignite SQL since the inner query is executed locally and there are
            // no Ignite Query properties to change that. The only way to make this SQL work in H2-based Ignite is
            // replicating the cache.
            // This is a known "WHERE Subquery" limitation: https://www.gridgain.com/docs/latest/developers-guide/SQL/sql-api#subqueries-in-where-clause
            // To work around this limitation we retrieve full history up to the requested revision and filter the
            // latest entries locally on this node. This approach has a scalability (memory/performance) issue: we
            // may need to develop a more scalable solution if this issue prevents meeting the requirements.
            // TODO: the "WHERE Subquery" limitation will be fixed in Calcite-based Ignite, remove the workaround.
            sql.append(sqlFilter.isEmpty() ? "\nWHERE " : " AND ").append("MODIFY_REVISION <= ?");
            sqlArgs.add(rev);
        }

        if (!sqlSort.isEmpty())
            sql.append("\nORDER BY ").append(sqlSort);
        else if (limit > 0)
            sql.append("\nORDER BY KEY").append(sqlSort);

        if (limit > 0) {
            // Cannot LIMIT historical queries since we retrieve full history due to the "WHERE Subquery" limitation
            // explained above.
            // TODO: the "WHERE Subquery" limitation will be fixed in Calcite-based Ignite, remove the workaround.
            if (rev <= 0) {
                sql.append("\nLIMIT ?");
                sqlArgs.add(limit);
            }
        }

        // Working around the "WHERE Subquery" limitation: store the latest (max) modification revision of each Key
        // in a Map. Build the latest revisions map when iterating over results. Keep only the latest entries in the
        // final result.
        // TODO: the "WHERE Subquery" limitation will be fixed in Calcite-based Ignite, remove the workaround.
        final Map<Key, Long> latestRevs = new HashMap<>();

        for (List<?> row : cache.query(new SqlFieldsQuery(sql.toString()).setArgs(sqlArgs.toArray()))) {
            Iterator<?> it = row.iterator();

            Long crtRev = (Long) it.next();
            Long modRev = (Long) it.next();
            Long ver = (Long) it.next();
            Long lease = (Long) it.next();

            Key k = new Key((byte[]) it.next());

            // "WHERE Subquery" limitation workaround: build and filter the latest versions
            if (rev > 0) {
                Long latestRev = latestRevs.get(k);
                if (latestRev != null && latestRev >= modRev)
                    continue; // skip: a newer entry was already added to result
                else {
                    latestRevs.put(k, modRev);
                }
            }

            if (crtRev > 0) { // do not add tombstone (deleted entry marker)
                Value v = new Value((noPayload ? null : (byte[]) it.next()), crtRev, modRev, ver, lease);
                res.add(new SimpleImmutableEntry<>(k, v));
            }
        }

        // "WHERE Subquery" limitation workaround: filter
        if (rev > 0)
            res.removeIf(e -> e.getValue().modifyRevision() < latestRevs.get(e.getKey()));

        // "WHERE Subquery" limitation workaround: limit
        if (rev > 0 && limit > 0)
            res = res.stream().limit(limit).collect(Collectors.toList());

        return res;
    }

    private long count(Key start, Key end, long rev, long minModRev, long maxModRev, long minCrtRev, long maxCrtRev) {
        if (rev <= 0 && minModRev <= 0 && maxModRev <= 0 && minCrtRev <= 0 && maxCrtRev <= 0) {
            if (!start.isZero() && end == null)
                // Optimization for a popular case to check single entry without filtering
                return cache.containsKey(start) ? 1 : 0;
            else if (start.isZero() && end.isZero())
                // Optimization for counting all entries without filtering
                return cache.size();
        }

        if (rev <= 0) {
            Collection<Object> sqlArgs = new ArrayList<>();

            String sqlFilter = sqlFilter(sqlArgs, start, end, minModRev, maxModRev, minCrtRev, maxCrtRev);

            String sql = "SELECT COUNT(*) FROM ETCD_KV" + (sqlFilter.isEmpty() ? "" : " WHERE " + sqlFilter);

            return (Long) cache.query(new SqlFieldsQuery(sql).setArgs(sqlArgs.toArray()))
                .getAll().iterator().next().iterator().next();
        } else {
            // Cannot write a complete historical query due to the "WHERE Subquery" limitation explained in method
            // range(). For historical count get number of elements in historical range.
            // TODO: the "WHERE Subquery" limitation will be fixed in Calcite-based Ignite, remove the workaround.
            Collection<SimpleImmutableEntry<Key, Value>> kvs = range(
                false,
                start,
                end,
                rev,
                0,
                minModRev,
                maxModRev,
                minCrtRev,
                maxCrtRev,
                true,
                Rpc.RangeRequest.SortOrder.NONE,
                Rpc.RangeRequest.SortTarget.UNRECOGNIZED
            );

            return kvs.size();
        }
    }

    private Set<Key> keysInRange(Key start, Key end) {
        Collection<Object> sqlArgs = new ArrayList<>();
        Collection<String> filterList = new ArrayList<>();

        if (!start.isZero()) {
            filterList.add("KEY " + (end == null ? "=" : ">=") + " ?");
            sqlArgs.add(start.key());
        }

        if (end != null && !end.isZero()) {
            filterList.add("KEY < ?");
            sqlArgs.add(end.key());
        }

        StringBuilder sql = new StringBuilder("SELECT KEY FROM ETCD_KV");

        if (!filterList.isEmpty())
            sql.append("\nWHERE ").append(String.join(" AND ", filterList));

        Set<Key> res = new HashSet<>();

        for (List<?> row : cache.query(new SqlFieldsQuery(sql.toString()).setArgs(sqlArgs.toArray())))
            res.add(new Key((byte[]) row.iterator().next()));

        return res;
    }

    /**
     * Non-transactional {@link #range(Rpc.RangeRequest)}.
     *
     * @param txModifiesKey See {@link #getWithoutPayload(Key, boolean)}.
     */
    private Rpc.RangeResponse rangeNonTx(Rpc.RangeRequest req, TxnContext txnCtx, boolean txModifiesKey) {
        final long curRev = txnCtx.currentRevision(false);

        Rpc.RangeResponse.Builder res = Rpc.RangeResponse.newBuilder().setHeader(EtcdCluster.getHeader(curRev));

        // the first key for the range. If range_end is not given, the request only looks up key.
        ByteString key = req.getKey();

        // the upper bound on the requested range [key, range_end).
        // If range_end is '\0', the range is all keys >= key.
        // If range_end is key plus one (e.g., "aa"+1 == "ab", "a\xff"+1 == "b"), then the range request gets all
        // keys prefixed with key.
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

        if (rev > curRev)
            throw new IndexOutOfBoundsException("Required revision is a future revision");
        else if (rev == curRev)
            rev = 0;

        if (log.isTraceEnabled()) {
            StringBuilder s = new StringBuilder("RangeRequest {")
                .append("key: ").append(key.toStringUtf8());
            if (!rangeEnd.isEmpty())
                s.append(", rangeEnd: ").append(rangeEnd.toStringUtf8());
            if (cntOnly)
                s.append(", countOnly: true");
            if (keysOnly)
                s.append(", keysOnly: true");
            if (rev > 0)
                s.append(", rev: ").append(rev);
            if (limit > 0)
                s.append(", limit: ").append(limit);
            if (sortOrder != Rpc.RangeRequest.SortOrder.NONE) {
                s.append(", sortOrder: ").append(sortOrder);
                s.append(", sortTarget: ").append(sortTarget);
            }
            s.append("}");
            log.trace(s.toString());
        }

        Key start = key.isEmpty() ? null : new Key(key.toByteArray());
        Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());

        if (cntOnly) {
            long cnt = count(start, end, rev, minModRev, maxModRev, minCrtRev, maxCrtRev);
            res.setCount(cnt);

            if (log.isTraceEnabled())
                log.trace("RangeResponse {rev: " + res.getHeader().getRevision() +  ", count: " + cnt + "}");
        } else {
            // If limit > 0 we have to limit entries and return total number of entries. We cannot have separate
            // range(limit) and count() since SQL is used for complex queries and SQL is not transactional (SQL "sees"
            // uncommitted changes). This may lead to range() would be inconsistent with count().
            // Thus we use single range() without limit to get ALL entries and limit them on this node. This is
            // inefficient and may need to be optimized.
            Collection<SimpleImmutableEntry<Key, Value>> kvs = range(
                txModifiesKey,
                start,
                end,
                0, // get all entries due to the problem described above
                rev,
                minModRev,
                maxModRev,
                minCrtRev,
                maxCrtRev,
                keysOnly,
                sortOrder,
                sortTarget
            );

            if (limit > 0) {
                long cnt = kvs.size();
                res.addAllKvs(kvs.stream().limit(limit).map(e -> PBFormat.kv(e, keysOnly)).collect(Collectors.toList()))
                    .setCount(cnt)
                    .setMore(cnt > limit);
            } else
                res.addAllKvs(kvs.stream().map(e -> PBFormat.kv(e, keysOnly)).collect(Collectors.toList()))
                    .setCount(kvs.size());

            if (log.isTraceEnabled())
                log.trace(
                    "RangeResponse {rev: " + res.getHeader().getRevision() + ", keys: [" +
                        String.join(
                            ", ",
                            kvs.stream()
                                .map(kv -> ByteString.copyFrom(kv.getKey().key()).toStringUtf8())
                                .toArray(String[]::new)
                        ) + "]" + (res.getMore() ? ", more: true" : "") + "}"
                );
        }

        return res.build();
    }

    /**
     * Non-transactional {@link #put(Rpc.PutRequest)}.
     */
    private Rpc.PutResponse putNonTx(Rpc.PutRequest req, TxnContext txnCtx) {
        ByteString reqKey = req.getKey();
        ByteString reqVal = req.getValue();
        long lease = req.getLease();

        if (log.isTraceEnabled())
            log.trace(
                "PutRequest {key: " + reqKey.toStringUtf8() +
                    ", value: " + reqVal.toStringUtf8() +
                    (lease > 0 ? ", lease: " + lease + " / " + String.format("%16x", lease) : "") +
                    "}"
            );

        long curRev;

        if (!reqKey.isEmpty() && !reqVal.isEmpty()) {
            curRev = txnCtx.currentRevision(true);

            Key k = new Key(reqKey.toByteArray());
            Value val = getWithoutPayload(k, true);
            long ver = val == null ? 1 : val.version() + 1;
            long crtRev = val == null ? curRev : val.createRevision();

            HistoricalValue hVal = new HistoricalValue(reqVal.toByteArray(), crtRev, ver, lease);

            IgniteCache<Key, Value> leasedCache;
            IgniteCache<HistoricalKey, HistoricalValue> leasedHistCache;

            if (lease == 0) {
                leasedCache = cache;
                leasedHistCache = histCache;
            } else {
                Ttl ttl = leaseCache.get(lease);

                if (ttl == null)
                    throw new IllegalArgumentException("Lease does not exist: " + String.format("%16x", lease));

                ExpiryPolicy expPlc = new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, ttl.remainingTtl()));

                leasedCache = cache.withExpiryPolicy(expPlc);
                leasedHistCache = histCache.withExpiryPolicy(expPlc);
            }

            leasedCache.put(k, new Value(hVal, curRev));
            leasedHistCache.put(new HistoricalKey(k, curRev), hVal);
        } else
            curRev = txnCtx.currentRevision(false);

        if (log.isTraceEnabled())
            log.trace("PutResponse {rev: " + curRev + "}");

        return Rpc.PutResponse.newBuilder().setHeader(EtcdCluster.getHeader(curRev)).build();
    }

    /**
     * Non-transactional {@link #deleteRange(Rpc.DeleteRangeRequest)}.
     */
    private Rpc.DeleteRangeResponse deleteRangeNonTx(Rpc.DeleteRangeRequest req, TxnContext txnCtx) {
        // the key-value store revision when the request was applied
        final long curRev = txnCtx.currentRevision(true);

        // key is the first key to delete in the range.
        ByteString key = req.getKey();

        // range_end is the key following the last key to delete for the range [key, range_end).
        // If range_end is not given, the range is defined to contain only the key argument.
        // If range_end is one bit larger than the given key, then the range is all the keys with the prefix
        // (the given key).
        // If range_end is '\0', the range is all keys greater than or equal to the key argument.
        ByteString rangeEnd = req.getRangeEnd();

        // If prev_kv is set, etcd gets the previous key-value pairs before deleting it.
        // The previous key-value pairs will be returned in the delete response.
        boolean prevKv = req.getPrevKv();

        long cnt = 0;
        Map<Key, Value> prevVals = null;

        Rpc.DeleteRangeResponse.Builder res = Rpc.DeleteRangeResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(curRev));

        Key start = new Key(key.toByteArray());
        Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());

        if (log.isTraceEnabled()) {
            StringBuilder s = new StringBuilder("DeleteRangeRequest {")
                .append("key: ").append(key.toStringUtf8());
            if (!rangeEnd.isEmpty())
                s.append(", rangeEnd: ").append(rangeEnd.toStringUtf8());
            if (prevKv)
                s.append(", prevKv: true");
            s.append("}");
            log.trace(s.toString());
        }

        if (!start.isZero() && end == null) {
            // Optimization for single key removal
            boolean removed = false;

            if (prevKv) {
                Value v = cache.getAndRemove(start);
                if (v != null) {
                    removed = true;
                    prevVals = Collections.singletonMap(start, v);
                }
            } else
                removed = cache.remove(start);

            if (removed) {
                histCache.put(new HistoricalKey(start, curRev), HistoricalValue.TOMBSTONE);
                cnt++;
            }
        } else {
            Set<Key> keys = keysInRange(start, end);

            if (!keys.isEmpty()) {
                if (prevKv)
                    prevVals = cache.getAll(keys);

                cache.removeAll(keys);
                histCache.putAll(keys.stream().collect(Collectors.toMap(
                    k -> new HistoricalKey(k, curRev),
                    k -> HistoricalValue.TOMBSTONE
                )));
            }

            cnt = keys.size();
        }

        if (log.isTraceEnabled())
            log.trace("DeleteRangeResponse {rev: " + curRev + ", deleted: " + cnt + "}");

        if (prevVals != null)
            res.addAllPrevKvs(prevVals.entrySet().stream().map(PBFormat::kv).collect(Collectors.toList()));

        return res.setDeleted(cnt).build();
    }

    /**
     * Non-transactional {@link #txn(Rpc.TxnRequest)}.
     */
    private Rpc.TxnResponse txnNonTx(Rpc.TxnRequest req, TxnContext txnCtx) {
        List<Rpc.Compare> cmpReqList = req.getCompareList();
        Collection<Rpc.ResponseOp> resList = null;
        boolean ok = false;

        if (cmpReqList.size() > 0) {
            ok = cmpReqList.stream().allMatch(this::check);

            List<Rpc.RequestOp> reqList = ok ? req.getSuccessList() : req.getFailureList();

            resList = reqList.stream().map(r -> exec(r, txnCtx)).collect(Collectors.toList());
        }

        Rpc.TxnResponse.Builder res = Rpc.TxnResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(txnCtx.currentRevision(false)))
            .setSucceeded(ok);

        if (resList != null)
            res.addAllResponses(resList);

        return res.build();
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
            sqlArgs.add(start.key());
        }

        if (end != null && !end.isZero()) {
            filterList.add("KEY < ?");
            sqlArgs.add(end.key());
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

    private <R> R transaction(long timeout, int size, Supplier<R> work) {
        while (true) {
            try (Transaction tx = ignite.transactions().txStart(
                TransactionConcurrency.OPTIMISTIC,
                TransactionIsolation.READ_COMMITTED,
                TX_TIMEOUT > 0 ? TX_TIMEOUT : timeout,
                size
            )) {
                R res = work.get();
                tx.commit();
                return res;
            } catch (TransactionOptimisticException ignored) {
                // Retry optimistic transaction
            }
        }
    }

    /**
     * Transaction context: use from single thread.
     */
    private static final class TxnContext {
        private final EtcdCluster clusterCtx;
        long curRev = 0;
        boolean isModified = false;

        private TxnContext(EtcdCluster clusterCtx) {
            this.clusterCtx = clusterCtx;
        }

        /**
         * @param isModification {@code true} if the revision is requested for a modification operation.
         * @return The key-value store revision when the request was applied. The revision is incremented if the
         * transaction has at least one modification operation. The revision is incremented once even if there are
         * multiple modification operations.
         */
        long currentRevision(boolean isModification) {
            if (!isModified) {
                if (isModification) {
                    curRev = clusterCtx.incrementRevision();
                    isModified = true;
                } else if (curRev == 0)
                    curRev = clusterCtx.revision();
            }

            return curRev;
        }
    }
}