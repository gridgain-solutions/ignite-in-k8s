package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import io.grpc.StatusRuntimeException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.EventType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Watch {
    private static final Runnable nop = () -> {
    };
    private final IgniteCache<Key, Value> cache;
    private final IgniteCache<HistoricalKey, HistoricalValue> histCache;
    private final IgniteLogger log;
    private final EtcdCluster ctx;
    private final Map<WatcherId, Watcher> watchers = new ConcurrentHashMap<>();

    public Watch(Ignite ignite, String cacheName, String histCacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        histCache = ignite.getOrCreateCache(CacheConfig.KVHistory(histCacheName));
        log = ignite.log();
        ctx = new EtcdCluster(ignite);
    }

    /**
     * Starts asynchronously watching for events happening or that have happened. Both input and output are streams;
     * the input stream is for creating and canceling watchers and the output stream sends events. One watch RPC can
     * watch on multiple key ranges, streaming events for several watches at once. The entire event history can be
     * watched starting from the last compaction revision.
     *
     * @param req         Watch parameters
     * @param resConsumer Accepts the event notifications
     * @return Cancellation routine
     */
    public Runnable watch(long streamId, Rpc.WatchRequest req, Consumer<Rpc.WatchResponse> resConsumer) {
        // The key-value store revision when the request was applied. For watch progress responses, the header.revision
        // indicates progress. All future events received in this stream are guaranteed to have a higher revision
        // number than the header.revision number.
        final long watchRev = ctx.revision();

        Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder().setHeader(EtcdCluster.getHeader(watchRev));

        if (req.hasCreateRequest()) {
            Rpc.WatchCreateRequest startReq = req.getCreateRequest();

            // If watch_id is provided and non-zero, it will be assigned to this watcher.
            // Since creating a watcher in etcd is not a synchronous operation,
            // this can be used ensure that ordering is correct when creating multiple
            // watchers on the same stream. Creating a watcher with an ID already in
            // use on the stream will cause an error to be returned.
            long watchId = startReq.getWatchId();

            WatcherId id = new WatcherId(streamId, watchId);
            Watcher watcher = new Watcher(ctx, cache, histCache, log, startReq, resConsumer, watchRev);

            if (watchers.putIfAbsent(id, watcher) != null) {
                throw new IllegalStateException("Watcher " + watchId + " already exists for stream " + streamId);
            }

            resConsumer.accept(res.setWatchId(watchId).setCreated(true).build());

            return () -> {
                Watcher w = watchers.remove(id);
                if (w != null)
                    w.cancel();
            };
        } else if (req.hasCancelRequest()) {
            long watchId = req.getCancelRequest().getWatchId();
            WatcherId id = new WatcherId(streamId, watchId);

            Watcher w = watchers.remove(id);
            if (w != null)
                w.cancel();

            resConsumer.accept(res.setWatchId(watchId).setCanceled(true).build());
        } else
            resConsumer.accept(res.build());

        return nop;
    }

    private static final class WatcherId {
        private final long streamId;
        private final long watchId;

        public WatcherId(long streamId, long watchId) {
            this.streamId = streamId;
            this.watchId = watchId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WatcherId watcherId = (WatcherId) o;
            return streamId == watcherId.streamId &&
                watchId == watcherId.watchId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(streamId, watchId);
        }

        @Override
        public String toString() {
            return "WatcherId {" + streamId + ", " + watchId + "}";
        }
    }

    private static final class Watcher {
        /** Limit number of simultaneous watchers to 1000 */
        private static final ExecutorService watchExec =
            Executors.newCachedThreadPool(new NamedThreadFactory("etcd-watch"));

        /** Threads reporting events to the clients */
        private static final ExecutorService reportExec = Executors.newFixedThreadPool(
            8,
            new NamedThreadFactory("etcd-watch-report")
        );

        private final EtcdCluster ctx;
        private final IgniteCache<Key, Value> cache;
        private final IgniteCache<HistoricalKey, HistoricalValue> histCache;
        private final IgniteLogger log;
        private final Rpc.WatchCreateRequest req;
        private final Consumer<Rpc.WatchResponse> resConsumer;
        private final CountDownLatch done = new CountDownLatch(1);
        private final CompletableFuture<?> watchFut;
        private final List<CompletableFuture<?>> reportFuts = new ArrayList<>();
        private final long watchRev;

        public Watcher(
            EtcdCluster ctx,
            IgniteCache<Key, Value> cache,
            IgniteCache<HistoricalKey, HistoricalValue> histCache,
            IgniteLogger log,
            Rpc.WatchCreateRequest req,
            Consumer<Rpc.WatchResponse> resConsumer,
            long watchRev
        ) {
            this.ctx = ctx;
            this.cache = cache;
            this.histCache = histCache;
            this.log = log;
            this.req = req;
            this.resConsumer = resConsumer;
            this.watchRev = watchRev;

            watchFut = CompletableFuture.runAsync(this::run, watchExec);
        }

        public void cancel() {
            CompletableFuture<?>[] futArr;
            synchronized (reportFuts) {
                done.countDown();
                futArr = reportFuts.toArray(new CompletableFuture[0]);
            }

            await(CompletableFuture.allOf(futArr), 20_000);

            // Number of simultaneous watchers is limited so the watch timeout can be significant. Set the watcher
            // timeout to 3 minutes for now and consider making it configurable.
            await(watchFut, 180_000);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder("WatchRequest {")
                .append("key: ").append(req.getKey().toStringUtf8());
            if (req.getRangeEnd() != null && !req.getRangeEnd().isEmpty())
                s.append(", rangeEnd: ").append(req.getRangeEnd().toStringUtf8());
            if (req.getStartRevision() > 0)
                s.append(", startRev: ").append(req.getStartRevision());
            if (!req.getFiltersList().isEmpty())
                s.append(", filters: [").append(
                    req.getFiltersList().stream()
                        .map(Rpc.WatchCreateRequest.FilterType::toString)
                        .collect(Collectors.joining(", "))
                ).append("]");
            s.append("}");
            return s.toString();
        }

        private void run() {
            // key is the key to register for watching
            ByteString key = req.getKey();

            // range_end is the end of the range [key, range_end) to watch. If range_end is not given,
            // only the key argument is watched. If range_end is equal to '\0', all keys greater than
            // or equal to the key argument are watched.
            // If the range_end is one bit larger than the given key, then all keys with the prefix (the given key)
            // will be watched.
            ByteString rangeEnd = req.getRangeEnd();

            // start_revision is an optional revision to watch from (inclusive). No start_revision is "now".
            long startRev = req.getStartRevision();

            // filters filter the events at server side before it sends back to the watcher.
            List<Rpc.WatchCreateRequest.FilterType> filters = req.getFiltersList();

            final Key start = new Key(key.toByteArray());
            final Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());
            boolean noPut = filters.contains(Rpc.WatchCreateRequest.FilterType.NOPUT);
            boolean noDel = filters.contains(Rpc.WatchCreateRequest.FilterType.NODELETE);

            // We monitor the "latest" KV cache for updates and the "historical" KV cache for removals.
            //
            // We cannot monitor the latest KV cache for removals since the removed entry's modification revision
            // will be the last update revision but ETCD requires it to be the revision at the time of the removal.
            // Removing from the historical KV cache means putting special "tombstone" entry and such event gives us
            // the required modification revision.
            //
            // On the other side monitoring the historical cache for updates is less efficient: the historical cache
            // is append-only and the CREATED events do not include "old" values that ETCD has to report.
            // The old value can be retrieved with additional requests and this reduces efficiency.
            if (done.getCount() > 0) {
                QueryCursor<Cache.Entry<Key, Value>> cur = null;
                QueryCursor<Cache.Entry<HistoricalKey, HistoricalValue>> hCur = null;

                try {
                    if (!noPut) {
                        ContinuousQuery<Key, Value> q = new ContinuousQuery<>();
                        q.setLocalListener(this::reportAsync);
                        q.setRemoteFilterFactory(new KVFilter(key, rangeEnd, startRev, watchRev));
                        cur = cache.query(q);
                    }

                    if (!noDel) {
                        ContinuousQuery<HistoricalKey, HistoricalValue> hq = new ContinuousQuery<>();
                        hq.setLocalListener(this::reportHistoryAsync);
                        hq.setRemoteFilterFactory(new KVHistoryFilter(key, rangeEnd, startRev, watchRev));
                        hCur = histCache.query(hq);
                    }

                    if (log.isTraceEnabled())
                        log.trace(this.toString() + " started");

                    // Get history after the query started. This may lead to duplicates and out-of-order
                    // reporting but we will not loose data. We do not use Continuous Query's initial query since
                    // initial query does not provide "exactly once" guarantee anyway and will likely be deprecated.
                    getHistory(startRev, start, end, noPut, noDel);

                    done.await();
                } catch (InterruptedException e) {
                    if (cur != null)
                        cur.close();

                    if (hCur != null)
                        hCur.close();

                    log.error(this.toString() + " completed ungracefully", e);
                }

                if (log.isTraceEnabled())
                    log.trace(this.toString() + " completed");
            }
        }

        private void getHistory(long startRev, Key start, Key end, boolean noPut, boolean noDel) {
            if (startRev <= 0)
                return;

            Collection<String> filterList = new ArrayList<>();
            Collection<Object> qArgs = new ArrayList<>();

            filterList.add("MODIFY_REVISION >= ?");
            qArgs.add(startRev);

            if (!start.isZero()) {
                filterList.add("KEY " + (end == null ? "=" : ">=") + " ?");
                qArgs.add(start.key());
            }

            if (end != null && !end.isZero()) {
                filterList.add("KEY < ?");
                qArgs.add(end.key());
            }

            SqlFieldsQuery q = new SqlFieldsQuery(
                "SELECT _KEY, _VAL FROM ETCD_KV_HISTORY WHERE " +
                    String.join(" AND ", filterList) + " ORDER BY KEY, MODIFY_REVISION"
            ).setArgs(qArgs.toArray());

            // Convert historical KVs to non-historical KV events with valid modification revisions of the "old" values
            Map<Key, Value> prevKvs = new HashMap<>(); // index by Key to store only previous values
            Collection<CacheEntryEvent<? extends Key, ? extends Value>> evts = new ArrayList<>();

            for (List<?> row : histCache.query(q)) {
                Iterator<?> it = row.iterator();
                final HistoricalKey histKey = (HistoricalKey) it.next();
                final HistoricalValue histVal = (HistoricalValue) it.next();
                final EventType type = histVal.isTombstone() ? EventType.REMOVED : EventType.UPDATED;
                Key k = new Key(histKey.key()); // re-create to avoid HistoricalKey's hashCode() in a Map
                Value v = new Value(histVal, histKey.modifyRevision());

                if ((type == EventType.UPDATED && !noPut) || (type == EventType.REMOVED && !noDel)) {
                    Value prevVal = prevKvs.get(k);

                    if (prevVal != null && prevVal.isTombstone())
                        prevVal = null;

                    evts.add(new KVEvent(cache, type, k, v, prevVal));
                }

                prevKvs.put(k, v);
            }

            if (evts.size() > 0)
                reportAsync(evts);
        }

        private void reportAsync(Iterable<CacheEntryEvent<? extends Key, ? extends Value>> evts) {
            synchronized (reportFuts) {
                if (done.getCount() > 0) {
                    CompletableFuture<?> fut = CompletableFuture.runAsync(() -> report(evts), reportExec);
                    reportFuts.add(fut);

                    fut.thenRun(() -> {
                        synchronized (reportFuts) {
                            if (done.getCount() > 0)
                                reportFuts.remove(fut);
                        }
                    });
                }
            }
        }

        private void reportHistoryAsync(
            Iterable<CacheEntryEvent<? extends HistoricalKey, ? extends HistoricalValue>> evts
        ) {
            synchronized (reportFuts) {
                if (done.getCount() > 0) {
                    CompletableFuture<?> fut = CompletableFuture.runAsync(() -> reportHistory(evts), reportExec);
                    reportFuts.add(fut);

                    fut.thenRun(() -> {
                        synchronized (reportFuts) {
                            if (done.getCount() > 0)
                                reportFuts.remove(fut);
                        }
                    });
                }
            }
        }

        private void report(Iterable<CacheEntryEvent<? extends Key, ? extends Value>> evts) {
            // If prev_kv is set, created watcher gets the previous KV before the event happens.
            // If the previous KV is already compacted, nothing will be returned.
            boolean prevKv = req.getPrevKv();

            Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder()
                .setHeader(EtcdCluster.getHeader(ctx.revision()))
                .setWatchId(req.getWatchId());

            for (CacheEntryEvent<? extends Key, ? extends Value> evt : evts)
                res.addEvents(PBFormat.event(evt, prevKv));

            if (done.getCount() > 0) {
                Rpc.WatchResponse r = res.build();

                if (log.isTraceEnabled())
                    trace(r);

                try {
                    resConsumer.accept(r);
                } catch (StatusRuntimeException | IllegalStateException ignored) {
                    // Client closed connection: ignore
                } catch (Exception e) {
                    log.error("Failed to notify watcher", e);
                }
            }
        }

        private void reportHistory(
            Iterable<CacheEntryEvent<? extends HistoricalKey, ? extends HistoricalValue>> evts
        ) {
            // If prev_kv is set, created watcher gets the previous KV before the event happens.
            // If the previous KV is already compacted, nothing will be returned.
            boolean prevKv = req.getPrevKv();

            Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder()
                .setHeader(EtcdCluster.getHeader(ctx.revision()))
                .setWatchId(req.getWatchId());

            for (CacheEntryEvent<? extends HistoricalKey, ? extends HistoricalValue> histEvt : evts) {
                HistoricalKey histKey = histEvt.getKey();
                HistoricalValue histVal = histEvt.getValue();
                EventType t = histVal.isTombstone() ? EventType.REMOVED : histEvt.getEventType();
                Key k = new Key(histKey.key());
                Value v = new Value(histVal, histKey.modifyRevision());
                Value prevVal = null;

                if (prevKv) {
                    // Historical cache is append-only and sends only CREATED events that do not have previous values.
                    // Get previous value, which has largest revision before the event's revision.
                    SqlFieldsQuery q = new SqlFieldsQuery(
                        "SELECT _KEY, _VAL FROM ETCD_KV_HISTORY " +
                            "WHERE KEY = ? AND MODIFY_REVISION < ? " +
                            "ORDER BY MODIFY_REVISION DESC " +
                            "LIMIT 1"
                    ).setArgs(histKey.key(), histKey.modifyRevision());

                    Iterator<List<?>> rows = histCache.query(q).getAll().iterator();

                    if (rows.hasNext()) {
                        Iterator<?> r = rows.next().iterator();
                        HistoricalKey prevKey = (HistoricalKey) r.next();
                        HistoricalValue prevHistVal = (HistoricalValue) r.next();
                        prevVal = new Value(prevHistVal, prevKey.modifyRevision());
                    }
                }

                res.addEvents(PBFormat.event(new KVEvent(cache, t, k, v, prevVal), prevKv));
            }

            if (done.getCount() > 0) {
                Rpc.WatchResponse r = res.build();

                if (log.isTraceEnabled())
                    trace(r);

                try {
                    resConsumer.accept(r);
                } catch (StatusRuntimeException | IllegalStateException ignored) {
                    // Client closed connection: ignore
                } catch (Exception e) {
                    log.error("Failed to notify watcher", e);
                }
            }
        }

        private void trace(Rpc.WatchResponse res) {
            log.trace(
                "WatchResponse {rev: " + res.getHeader().getRevision() + ", watchId: " + res.getWatchId() +
                    ", events: [" +
                    String.join(
                        ", ",
                        res.getEventsList().stream()
                            .map(evt -> "{type: " + evt.getType() +
                                ", key: " + evt.getKv().getKey().toStringUtf8() +
                                ", modRev: " + evt.getKv().getModRevision() + "}")
                            .toArray(String[]::new)
                    ) + "]}"
            );
        }

        private void await(CompletableFuture<?> fut, long timeout) {
            try {
                fut.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                log.error("Operation did not complete in " + timeout + " ms", e);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Operation completed ungracefully", e);
            }
        }

        /** Remote filter that passes only PUT events. */
        private static final class KVFilter implements Factory<CacheEntryEventFilter<Key, Value>> {
            private final ByteString key;
            private final ByteString rangeEnd;
            private final long startRev;
            private final long watchRev;
            private final Key start;
            private final Key end;

            KVFilter(ByteString key, ByteString rangeEnd, long startRev, long watchRev) {
                this.key = key;
                this.rangeEnd = rangeEnd;
                this.startRev = startRev;
                this.watchRev = watchRev;
                start = new Key(key.toByteArray());
                end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());
            }

            @Override
            public CacheEntryEventFilter<Key, Value> create() {
                return e -> {
                    Key k = e.getKey();
                    Value v = e.getValue();
                    EventType t = e.getEventType();

                    if (startRev > 0 && v.modifyRevision() < startRev)
                        return false;

                    // Future events have higher revision than the header.revision number
                    if (v.modifyRevision() <= watchRev)
                        return false;

                    // KVFilter passes PUT events, KVHistoryFilter passes PUT TOMBSTONE events
                    if (t == EventType.REMOVED || t == EventType.EXPIRED)
                        return false;

                    if (end == null)
                        return start.equals(k);

                    Comparator<ByteString> cmp = ByteString.unsignedLexicographicalComparator();
                    ByteString bsk = ByteString.copyFrom(k.key());

                    boolean notLess = cmp.compare(bsk, key) >= 0;

                    if (end.isZero())
                        return notLess;

                    return notLess && cmp.compare(bsk, rangeEnd) < 0;
                };
            }
        }

        /** Remote history filter that passes only DELETE events. */
        private static final class KVHistoryFilter
            implements Factory<CacheEntryEventFilter<HistoricalKey, HistoricalValue>> {
            private final ByteString key;
            private final ByteString rangeEnd;
            private final long startRev;
            private final long watchRev;
            private final Key start;
            private final Key end;

            KVHistoryFilter(ByteString key, ByteString rangeEnd, long startRev, long watchRev) {
                this.key = key;
                this.rangeEnd = rangeEnd;
                this.startRev = startRev;
                this.watchRev = watchRev;
                start = new Key(key.toByteArray());
                end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());
            }

            @Override
            public CacheEntryEventFilter<HistoricalKey, HistoricalValue> create() {
                return e -> {
                    HistoricalKey k = e.getKey();
                    EventType t = e.getEventType();

                    if (startRev > 0 && k.modifyRevision() < startRev)
                        return false;

                    // Future events have higher revision than the header.revision number
                    if (k.modifyRevision() <= watchRev)
                        return false;

                    // KVFilter passes PUT events, KVHistoryFilter passes PUT TOMBSTONE events
                    if (t == EventType.REMOVED || t == EventType.EXPIRED || !e.getValue().isTombstone())
                        return false;

                    if (end == null)
                        return Arrays.equals(start.key(), k.key());

                    Comparator<ByteString> cmp = ByteString.unsignedLexicographicalComparator();
                    ByteString bsk = ByteString.copyFrom(k.key());

                    boolean notLess = cmp.compare(bsk, key) >= 0;

                    if (end.isZero())
                        return notLess;

                    return notLess && cmp.compare(bsk, rangeEnd) < 0;
                };
            }
        }

        private static final class KVEvent extends CacheEntryEvent<Key, Value> {
            private final Key k;
            private final Value v;
            private final Value oldVal;

            KVEvent(Cache<Key, Value> src, EventType t, Key k, Value v, Value oldVal) {
                super(src, t);
                this.k = k;
                this.v = v;
                this.oldVal = oldVal;
            }

            @Override
            public Value getOldValue() {
                return oldVal;
            }

            @Override
            public boolean isOldValueAvailable() {
                return oldVal != null;
            }

            @Override
            public Key getKey() {
                return k;
            }

            @Override
            public Value getValue() {
                return v;
            }

            @Override
            public <T> T unwrap(Class<T> clazz) {
                return null;
            }
        }

        private static final class NamedThreadFactory implements ThreadFactory {
            private final ThreadGroup group;
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String namePrefix;

            NamedThreadFactory(String prefix) {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                namePrefix = prefix;
            }

            public Thread newThread(Runnable r) {
                Thread t = new Thread(group, r, namePrefix + "-" + threadNumber.getAndIncrement(), 0);

                if (t.isDaemon())
                    t.setDaemon(false);

                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);

                return t;
            }
        }
    }
}
