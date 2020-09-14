package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import javax.cache.Cache;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Applications can grant leases for keys from an etcd cluster. When a key is attached to a lease, its lifetime is
 * bound to the lease’s lifetime which in turn is governed by a time-to-live (TTL). Each lease has a minimum
 * time-to-live (TTL) value specified by the application at grant time. The lease’s actual TTL value is at least the
 * minimum TTL and is chosen by the etcd cluster. Once a lease’s TTL elapses, the lease expires and all attached
 * keys are deleted.
 */
public final class Lease {
    private final IgniteCache<Long, Long> cache;
    private final IgniteLogger log;
    private final IgniteCache<Key, Value> kvCache;
    private final IgniteCache<HistoricalKey, HistoricalValue> histCache;
    private final EtcdCluster ctx;

    public Lease(Ignite ignite, String cacheName, String kvCacheName, String histCacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.Lease(cacheName));
        log = ignite.log();
        kvCache = ignite.getOrCreateCache(CacheConfig.KV(kvCacheName));
        histCache = ignite.getOrCreateCache(CacheConfig.KVHistory(histCacheName));
        ctx = new EtcdCluster(ignite);
    }

    /**
     * Creates a lease which expires if the server does not receive a keepAlive within a given time to live period.
     * All keys attached to the lease will be expired and deleted if the lease expires. Each expired key generates
     * a delete event in the event history.
     */
    public Rpc.LeaseGrantResponse leaseGrant(Rpc.LeaseGrantRequest req) {
        long ttl = req.getTTL();
        long id = req.getID();

        if (id == 0)
            id = Math.abs(Random.instance.nextLong());

        Rpc.LeaseGrantResponse.Builder res = Rpc.LeaseGrantResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(ctx.revision()))
            .setID(id)
            .setTTL(ttl);

        try {
            cache.put(id, ttl);
        } catch (Exception ex) {
            res.setError(ex.getMessage());
        }

        return res.build();
    }

    /** Revokes a lease. All keys attached to the lease will expire and be deleted. */
    public Rpc.LeaseRevokeResponse leaseRevoke(Rpc.LeaseRevokeRequest req) {
        long id = req.getID();

        Consumer<IgniteCache<?, ?>> kvClean = c -> {
            Long delCnt = (Long) c.query(
                new SqlFieldsQuery(new SqlFieldsQuery("DELETE FROM " + c.getName() + " WHERE LEASE = ?").setArgs(id))
            ).getAll().iterator().next().iterator().next();

            if (log.isTraceEnabled())
                log.trace(
                    "Revoking lease " + String.format("%16x", id) + " removed " + delCnt + " entries from " +
                        c.getName()
                );
        };

        kvClean.accept(kvCache);
        kvClean.accept(histCache);
        cache.remove(id);

        return Rpc.LeaseRevokeResponse.newBuilder().setHeader(EtcdCluster.getHeader(ctx.revision())).build();
    }

    /**
     * Keeps the lease alive by streaming keep alive requests from the client to the server and streaming keep alive
     * responses from the server to the client.
     */
    public void leaseKeepAlive(Rpc.LeaseKeepAliveRequest req, Consumer<Rpc.LeaseKeepAliveResponse> resConsumer) {
        long id = req.getID();

        Rpc.LeaseKeepAliveResponse.Builder res = Rpc.LeaseKeepAliveResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(ctx.revision()));

        Long ttl = cache.get(id);

        if (ttl == null)
            throw new IllegalArgumentException("Lease does not exist: " + String.format("%16x", id));

        resetTtl(kvCache, id, ttl);
        resetTtl(histCache, id, ttl);

        if (log.isTraceEnabled())
            log.trace("Kept lease " + String.format("%16x", id) + " alive");

        resConsumer.accept(res.setID(id).setTTL(ttl).build());
    }

    /** Retrieves lease information. */
    public Rpc.LeaseTimeToLiveResponse leaseTimeToLive(Rpc.LeaseTimeToLiveRequest req) {
        long id = req.getID();
        boolean listKeys = req.getKeys();

        Rpc.LeaseTimeToLiveResponse.Builder res = Rpc.LeaseTimeToLiveResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(ctx.revision()));

        Long grantedTtl = cache.get(id);

        if (grantedTtl != null) {
            long ttl = grantedTtl; // TODO: KV lease management

            res.setID(id).setTTL(ttl).setGrantedTTL(grantedTtl);

            if (listKeys) {
                SqlFieldsQuery q = new SqlFieldsQuery("SELECT KEY FROM ETCD_KV WHERE LEASE = ?").setArgs(id);

                for (List<?> row : cache.query(q))
                    res.addKeys(ByteString.copyFrom((byte[]) row.get(0)));
            }
        }

        return res.build();
    }

    /** Lists all existing leases. */
    public Rpc.LeaseLeasesResponse leaseLeases(Rpc.LeaseLeasesRequest ignored) {
        Rpc.LeaseLeasesResponse.Builder res = Rpc.LeaseLeasesResponse.newBuilder()
            .setHeader(EtcdCluster.getHeader(ctx.revision()));

        for (Cache.Entry<Long, Long> i : cache.query(new ScanQuery<Long, Long>()))
            res.addLeases(Rpc.LeaseStatus.newBuilder().setID(i.getKey()).build());

        return res.build();
    }

    private static <K> void resetTtl(IgniteCache<K, ?> cache, long lease, long ttl) {
        // In Ignite invoking a GET for all leased keys with an access expiry policy resets the TTL
        @SuppressWarnings("unchecked") Set<K> keys = cache.query(
            new SqlFieldsQuery("SELECT _KEY FROM " + cache.getName() + " WHERE LEASE = ?").setArgs(lease)
        ).getAll()
            .stream()
            .map(i -> (K) i.iterator().next())
            .collect(Collectors.toSet());

        if (!keys.isEmpty()) {
            ExpiryPolicy expPlc = new AccessedExpiryPolicy(new Duration(TimeUnit.SECONDS, ttl));

            cache.withExpiryPolicy(expPlc).invokeAll(keys, (e, o) -> null);
        }
    }

    private static class Random {
        static final SecureRandom instance = new SecureRandom();
    }
}
