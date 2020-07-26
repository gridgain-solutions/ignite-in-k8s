package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.EventType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class Watch {
    private static final Runnable nop = () -> {};
    private final IgniteCache<Key, Value> cache;
    private final EtcdCluster ctx;
    private final Map<Long, Watcher> watchers = new ConcurrentHashMap<>();

    public Watch(Ignite ignite, String cacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        ctx = new EtcdCluster(ignite);
    }

    /**
     * Starts asynchronously watching for key events.
     *
     * @param req         Watch parameters
     * @param resConsumer Accepts the event notifications
     * @return Cancellation routine
     */
    public Runnable watch(Rpc.WatchRequest req, Consumer<Rpc.WatchResponse> resConsumer) {
        if (req.hasCreateRequest()) {
            Rpc.WatchCreateRequest startReq = req.getCreateRequest();
            long watchId = startReq.getWatchId();

            watchers.put(watchId, new Watcher(ctx, cache, startReq, resConsumer));

            resConsumer.accept(
                Rpc.WatchResponse.newBuilder()
                    .setHeader(EtcdCluster.getHeader(ctx.revision()))
                    .setWatchId(watchId)
                    .setCreated(true)
                    .build()
            );

            return () -> {
                Watcher w = watchers.remove(watchId);
                if (w != null)
                    w.cancel();
            };
        } else if (req.hasCancelRequest()) {
            long watchId = req.getCancelRequest().getWatchId();

            Watcher w = watchers.remove(watchId);
            if (w != null)
                w.cancel();

            resConsumer.accept(
                Rpc.WatchResponse.newBuilder()
                    .setHeader(EtcdCluster.getHeader(ctx.revision()))
                    .setWatchId(watchId)
                    .setCanceled(true)
                    .build()
            );
        }

        return nop;
    }

    private static final class Watcher {
        private final EtcdCluster ctx;
        private final IgniteCache<Key, Value> cache;
        private final Rpc.WatchCreateRequest req;
        private final Consumer<Rpc.WatchResponse> resConsumer;
        private final CountDownLatch done;

        public Watcher(
            EtcdCluster ctx,
            IgniteCache<Key, Value> cache,
            Rpc.WatchCreateRequest req,
            Consumer<Rpc.WatchResponse> resConsumer
        ) {
            this.ctx = ctx;
            this.cache = cache;
            this.req = req;
            this.resConsumer = resConsumer;
            done = new CountDownLatch(1);

            Executors.newSingleThreadExecutor().submit(this::run);
        }

        public void cancel() {
            done.countDown();
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

            q.setLocalListener(this::report);

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

            try (QueryCursor<Cache.Entry<Key, Value>> ignored = cache.query(q)) {
                done.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void report(Iterable<CacheEntryEvent<? extends Key, ? extends Value>> evts) {
            Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder()
                .setHeader(EtcdCluster.getHeader(ctx.revision()))
                .setWatchId(req.getWatchId());

            for (CacheEntryEvent<? extends Key, ? extends Value> evt : evts) {
                Key k = evt.getKey();
                Value v = evt.getValue();
                EventType t = evt.getEventType();

                Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                    .setKey(ByteString.copyFrom(k.key()))
                    .setVersion(v.version())
                    .setValue(ByteString.copyFrom(v.value()))
                    .setCreateRevision(v.createRevision())
                    .setModRevision(v.modifyRevision());

                Kv.Event.EventType kvt = t == EventType.REMOVED || t == EventType.EXPIRED
                    ? Kv.Event.EventType.DELETE
                    : Kv.Event.EventType.PUT;

                res.addEvents(Kv.Event.newBuilder().setType(kvt).setKv(kv).build());
            }

            resConsumer.accept(res.build());
        }
    }
}
