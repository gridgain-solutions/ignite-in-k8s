package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import io.grpc.StatusRuntimeException;
import mvccpb.Kv;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.EventType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Watch {
    private static final Runnable nop = () -> {};
    private final IgniteCache<Key, Value> cache;
    private final IgniteLogger log;
    private final EtcdCluster ctx;
    private final Map<WatcherId, Watcher> watchers = new ConcurrentHashMap<>();

    public Watch(Ignite ignite, String cacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        log = ignite.log();
        ctx = new EtcdCluster(ignite);
    }

    /**
     * Starts asynchronously watching for key events.
     *
     * @param req         Watch parameters
     * @param resConsumer Accepts the event notifications
     * @return Cancellation routine
     */
    public Runnable watch(long streamId, Rpc.WatchRequest req, Consumer<Rpc.WatchResponse> resConsumer) {
        Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision()));

        if (req.hasCreateRequest()) {
            Rpc.WatchCreateRequest startReq = req.getCreateRequest();

            // If watch_id is provided and non-zero, it will be assigned to this watcher.
            // Since creating a watcher in etcd is not a synchronous operation,
            // this can be used ensure that ordering is correct when creating multiple
            // watchers on the same stream. Creating a watcher with an ID already in
            // use on the stream will cause an error to be returned.
            long watchId = startReq.getWatchId();

            WatcherId id = new WatcherId(streamId, watchId);

            if (watchers.putIfAbsent(id, new Watcher(ctx, cache, log, startReq, resConsumer)) != null) {
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
        private final IgniteLogger log;
        private final Rpc.WatchCreateRequest req;
        private final Consumer<Rpc.WatchResponse> resConsumer;
        private final CountDownLatch done = new CountDownLatch(1);
        private final CompletableFuture<?> watchFut;
        private final List<CompletableFuture<?>> reportFuts = new ArrayList<>();

        public Watcher(
            EtcdCluster ctx,
            IgniteCache<Key, Value> cache,
            IgniteLogger log,
            Rpc.WatchCreateRequest req,
            Consumer<Rpc.WatchResponse> resConsumer
        ) {
            this.ctx = ctx;
            this.cache = cache;
            this.log = log;
            this.req = req;
            this.resConsumer = resConsumer;

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

        private void run() {
            // key is the key to register for watching
            ByteString key = req.getKey();

            // range_end is the end of the range [key, range_end) to watch. If range_end is not given,
            // only the key argument is watched. If range_end is equal to '\0', all keys greater than
            // or equal to the key argument are watched.
            // TODO: If the range_end is one bit larger than the given key,
            // then all keys with the prefix (the given key) will be watched.
            ByteString rangeEnd = req.getRangeEnd();

            // start_revision is an optional revision to watch from (inclusive). No start_revision is "now".
            long startRev = req.getStartRevision();

            // filters filter the events at server side before it sends back to the watcher.
            List<Rpc.WatchCreateRequest.FilterType> filters = req.getFiltersList();

            Key start = new Key(key.toByteArray());
            Key end = rangeEnd.isEmpty() ? null : new Key(rangeEnd.toByteArray());
            boolean noPut = filters.contains(Rpc.WatchCreateRequest.FilterType.NOPUT);
            boolean noDel = filters.contains(Rpc.WatchCreateRequest.FilterType.NODELETE);

            ContinuousQuery<Key, Value> q = new ContinuousQuery<>();

            q.setLocalListener(evts -> {
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
            });

            q.setRemoteFilterFactory((Factory<CacheEntryEventFilter<Key, Value>>) () ->
                (CacheEntryEventFilter<Key, Value>) e -> {
                    if (startRev > 0 && e.getValue().modifyRevision() < startRev)
                        return false;

                    if (noPut && (e.getEventType() == EventType.CREATED || e.getEventType() == EventType.UPDATED))
                        return false;

                    if (noDel && (e.getEventType() == EventType.REMOVED || e.getEventType() == EventType.EXPIRED))
                        return false;

                    Key k = e.getKey();

                    if (end == null)
                        return start.equals(k);

                    Comparator<ByteString> cmp = ByteString.unsignedLexicographicalComparator();
                    ByteString bsk = ByteString.copyFrom(k.key());

                    boolean notLess = cmp.compare(bsk, key) >= 0;

                    if (end.isZero())
                        return notLess;

                    return notLess && cmp.compare(bsk, rangeEnd) < 0;
                }
            );

            if (done.getCount() > 0) {
                Supplier<String> watcherInfo = () -> {
                    StringBuilder s = new StringBuilder("Watcher {")
                        .append("key: ").append(key.toStringUtf8());
                    if (end != null)
                        s.append(", rangeEnd: ").append(rangeEnd.toStringUtf8());
                    if (startRev > 0)
                        s.append(", startRev: ").append(startRev);
                    if (!filters.isEmpty())
                        s.append(", filters: [").append(
                            filters.stream()
                                .map(Rpc.WatchCreateRequest.FilterType::toString)
                                .collect(Collectors.joining(", "))
                        ).append("]");
                    s.append("}");
                    return s.toString();
                };

                if (log.isTraceEnabled())
                    log.trace(watcherInfo.get() + " started");

                try (QueryCursor<Cache.Entry<Key, Value>> ignored = cache.query(q)) {
                    done.await();
                } catch (InterruptedException e) {
                    log.error(watcherInfo.get() + " completed ungracefully", e);
                }

                if (log.isTraceEnabled())
                    log.trace(watcherInfo.get() + " completed");
            }
        }

        private void report(Iterable<CacheEntryEvent<? extends Key, ? extends Value>> evts) {
            // If prev_kv is set, created watcher gets the previous KV before the event happens.
            // If the previous KV is already compacted, nothing will be returned.
            boolean prevKv = req.getPrevKv();

            Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder()
                .setHeader(EtcdCluster.getHeader(ctx.revision()))
                .setWatchId(req.getWatchId());

            for (CacheEntryEvent<? extends Key, ? extends Value> evt : evts) {
                EventType t = evt.getEventType();
                Kv.Event.EventType kvt = t == EventType.REMOVED || t == EventType.EXPIRED
                    ? Kv.Event.EventType.DELETE
                    : Kv.Event.EventType.PUT;

                Kv.Event.Builder kve = Kv.Event.newBuilder()
                    .setType(kvt)
                    .setKv(PBFormat.kv(new AbstractMap.SimpleImmutableEntry<>(evt.getKey(), evt.getValue())));

                if (prevKv) {
                    Value oldVal = evt.getOldValue();

                    if (oldVal != null)
                        kve.setPrevKv(PBFormat.kv(new AbstractMap.SimpleImmutableEntry<>(evt.getKey(), oldVal)));
                }

                res.addEvents(kve.build());
            }

            if (done.getCount() > 0) {
                try {
                    resConsumer.accept(res.build());
                } catch (StatusRuntimeException | IllegalStateException ignored) {
                    // Client closed connection: ignore
                } catch (Exception e) {
                    log.error("Failed to notify watcher", e);
                }
            }
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

        private static class NamedThreadFactory implements ThreadFactory {
            private final ThreadGroup group;
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String namePrefix;

            NamedThreadFactory(String prefix) {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                namePrefix = prefix;
            }

            public Thread newThread(Runnable r) {
                Thread t = new Thread(group, r, namePrefix  + "-" + threadNumber.getAndIncrement(), 0);

                if (t.isDaemon())
                    t.setDaemon(false);

                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);

                return t;
            }
        }
    }
}
