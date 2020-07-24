package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import etcdserverpb.Rpc;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.security.SecureRandom;
import java.util.List;

public final class Lease {
    private final IgniteCache<Long, Long> cache;
    private final Context ctx;

    public Lease(Ignite ignite, String cacheName, String kvCacheName) {
        cache = ignite.getOrCreateCache(CacheConfig.Lease(cacheName));

        ctx = new Context(ignite);

        CacheConfiguration<byte[], Value> kvCacheCfg = CacheConfig.KV(kvCacheName);

        ignite.getOrCreateCache(kvCacheCfg);
    }

    public Rpc.LeaseGrantResponse leaseGrant(Rpc.LeaseGrantRequest req) {
        long ttl = req.getTTL();
        long id = req.getID();

        if (id == 0)
            id = Math.abs(Random.instance.nextLong());

        // TODO: KV lease management

        Rpc.LeaseGrantResponse.Builder res = Rpc.LeaseGrantResponse.newBuilder()
            .setHeader(Context.getHeader(ctx.revision()))
            .setID(id)
            .setTTL(ttl);

        try {
            cache.put(id, ttl);
        } catch (Exception ex) {
            res.setError(ex.getMessage());
        }

        return res.build();
    }

    public Rpc.LeaseRevokeResponse leaseRevoke(Rpc.LeaseRevokeRequest req) {
        long id = req.getID();

        // TODO: KV lease management

        cache.remove(id);

        return Rpc.LeaseRevokeResponse.newBuilder().setHeader(Context.getHeader(ctx.revision())).build();
    }

    public Rpc.LeaseKeepAliveResponse leaseKeepAlive(Rpc.LeaseKeepAliveRequest req) {
        long id = req.getID();

        Rpc.LeaseKeepAliveResponse.Builder res = Rpc.LeaseKeepAliveResponse.newBuilder()
            .setHeader(Context.getHeader(ctx.revision()));

        Long grantedTtl = cache.get(id);

        if (grantedTtl != null) {
            // TODO: KV lease management

            res.setID(id).setTTL(grantedTtl);
        }

        return res.build();
    }

    public Rpc.LeaseTimeToLiveResponse leaseTimeToLive(Rpc.LeaseTimeToLiveRequest req) {
        long id = req.getID();
        boolean listKeys = req.getKeys();

        Rpc.LeaseTimeToLiveResponse.Builder res = Rpc.LeaseTimeToLiveResponse.newBuilder()
            .setHeader(Context.getHeader(ctx.revision()));

        Long grantedTtl = cache.get(id);

        if (grantedTtl != null) {
            long ttl = grantedTtl; // TODO: KV lease management

            res.setID(id).setGrantedTTL(grantedTtl).setTTL(ttl);

            if (listKeys) {
                SqlFieldsQuery q = new SqlFieldsQuery("SELECT KEY FROM ETCD_KV WHERE LEASE = ?").setArgs(id);

                for (List<?> row : cache.query(q))
                    res.addKeys(ByteString.copyFrom((byte[]) row.get(0)));
            }
        }

        return res.build();
    }

    public Rpc.LeaseLeasesResponse leaseLeases(Rpc.LeaseLeasesRequest ignored) {
        Rpc.LeaseLeasesResponse.Builder res = Rpc.LeaseLeasesResponse.newBuilder()
            .setHeader(Context.getHeader(ctx.revision()));

        for (Cache.Entry<Long, Long> i : cache.query(new ScanQuery<Long, Long>()))
            res.addLeases(Rpc.LeaseStatus.newBuilder().setID(i.getKey()).build());

        return res.build();
    }

    private static class Random {
        static final SecureRandom instance = new SecureRandom();
    }
}
