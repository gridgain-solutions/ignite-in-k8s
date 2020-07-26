package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import mvccpb.Kv;
import org.apache.ignite.Ignite;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class Watch {
    private final Cache<Key, Value> cache;
    private final Context ctx;
    private final Map<Long, Watcher> watchers = new ConcurrentHashMap<>();

    public Watch(Ignite ignite, String cacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.KV(cacheName));
        ctx = new Context(ignite);
    }

    public Rpc.WatchResponse watch(Rpc.WatchRequest req) throws InterruptedException {
        Rpc.WatchResponse.Builder res = Rpc.WatchResponse.newBuilder().setHeader(Context.getHeader(ctx.revision()));

        if (req.hasCreateRequest()) {
            Rpc.WatchCreateRequest startReq = req.getCreateRequest();
            res.addAllEvents(await(startReq)).setWatchId(startReq.getWatchId()).setCreated(true);
        } else if (req.hasCancelRequest()) {
            Rpc.WatchCancelRequest stopReq = req.getCancelRequest();
            cancel(stopReq);
            res.setWatchId(stopReq.getWatchId()).setCanceled(true);
        }

        return res.build();
    }

    private Collection<Kv.Event> await(Rpc.WatchCreateRequest req) throws InterruptedException {
        Watcher w = new Watcher(cache, req);
        watchers.put(req.getWatchId(), w);
        return w.await();
    }

    private void cancel(Rpc.WatchCancelRequest req) {
        Watcher w = watchers.remove(req.getWatchId());
        if (w != null)
            w.cancel();
    }

    private static final class Watcher {
        private final Cache<Key, Value> cache;
        private final Rpc.WatchCreateRequest req;
        private final CountDownLatch done = new CountDownLatch(1);

        public Watcher(Cache<Key, Value> cache, Rpc.WatchCreateRequest req) {
            this.cache = cache;
            this.req = req;
        }

        public Collection<Kv.Event> await() throws InterruptedException {
            // TODO: proper Watch implementation
            final ByteString bsk = req.getKey();
            final Key k = new Key (bsk.toByteArray());
            final AtomicReference<InterruptedException> err = new AtomicReference<>();
            final Collection<Kv.Event> evtList = new ArrayList<>();

            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    while (!done.await(1000, TimeUnit.MILLISECONDS)) {
                        Value v = cache.get(k);
                        if (v != null) {
                            Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
                                .setKey(bsk)
                                .setVersion(v.version())
                                .setValue(ByteString.copyFrom(v.value()))
                                .setCreateRevision(v.createRevision())
                                .setModRevision(v.modifyRevision());
                            evtList.add(Kv.Event.newBuilder().setType(Kv.Event.EventType.PUT).setKv(kv).build());

                            done.countDown();
                        }
                    }
                } catch (InterruptedException e) {
                    err.set(e);
                }
            });

            done.await();

            var e = err.get();
            if (e != null)
                throw e;

            return evtList;
        }

        public void cancel() {
            done.countDown();
        }
    }
}
