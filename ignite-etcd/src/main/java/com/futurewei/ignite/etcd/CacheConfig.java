package com.futurewei.ignite.etcd;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Collections;
import java.util.Set;

public final class CacheConfig {
    private CacheConfig() {
    }

    /**
     * Users may define a KV cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     */
    public static String KVSpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL +
        "\nSQL: TABLE ETCD_KV (KEY BINARY, LEASE BIGINT)" +
        "\nPRIMARY KEY(KEY)" +
        "\nINDEX(LEASE)";

    /**
     * Users may define a KV History cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     */
    public static String KVHistorySpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL;

    /**
     * Users may define a Lease cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     */
    public static String LeaseSpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL;

    /**
     * Default KV cache configuration is used if the user did not specify external configuration according
     * to {@link #KVSpec}.
     */
    static CacheConfiguration<Key, Value> KV(String cacheName) {
        return new CacheConfiguration<Key, Value>(cacheName)
            .setCacheMode(CacheMode.PARTITIONED)
            .setBackups(1)
            .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
            .setSqlSchema("PUBLIC")
            .setQueryEntities(Collections.singleton(
                new QueryEntity(Key.class, Value.class)
                    .setTableName("ETCD_KV")
                    .addQueryField("key", byte[].class.getName(), null)
                    .addQueryField("val", byte[].class.getName(), "VALUE")
                    .addQueryField("modRev", long.class.getName(), "MODIFY_REVISION")
                    .addQueryField("crtRev", long.class.getName(), "CREATE_REVISION")
                    .addQueryField("ver", long.class.getName(), "VERSION")
                    .addQueryField("lease", long.class.getName(), null)
                    .setKeyFields(Set.of("key"))
                    .setIndexes(Collections.singletonList(new QueryIndex("lease")))
            ));
    }

    /**
     * Default KV History cache configuration is used if the user did not specify external configuration according
     * to {@link #KVHistorySpec}.
     */
    static CacheConfiguration<HistoricalKey, HistoricalValue> KVHistory(String cacheName) {
        return new CacheConfiguration<HistoricalKey, HistoricalValue>(cacheName)
            .setCacheMode(CacheMode.PARTITIONED)
            .setBackups(1)
            .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
            .setSqlSchema("PUBLIC")
            .setQueryEntities(Collections.singleton(
                new QueryEntity(HistoricalKey.class, HistoricalValue.class)
                    .setTableName("ETCD_KV_HISTORY")
                    .addQueryField("key", byte[].class.getName(), null)
                    .addQueryField("val", byte[].class.getName(), "VALUE")
                    .addQueryField("modRev", long.class.getName(), "MODIFY_REVISION")
                    .addQueryField("crtRev", long.class.getName(), "CREATE_REVISION")
                    .addQueryField("ver", long.class.getName(), "VERSION")
                    .addQueryField("lease", long.class.getName(), null)
                    .setKeyFields(Set.of("key", "modRev"))
            ));
    }

    /**
     * Default Lease cache configuration is used if the user did not specify external configuration according
     * to {@link #LeaseSpec}.
     */
    static CacheConfiguration<Long, Long> Lease(String cacheName) {
        return new CacheConfiguration<Long, Long>(cacheName)
            .setCacheMode(CacheMode.REPLICATED)
            .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
            .setSqlSchema("PUBLIC")
            .setQueryEntities(Collections.singleton(
                new QueryEntity(Long.class, Long.class)
                    .setTableName("ETCD_LEASE")
                    .addQueryField("lease", Long.class.getName(), null)
                    .addQueryField("ttl", Long.class.getName(), null)
                    .setKeyFieldName("lease")
                    .setValueFieldName("ttl")
            ));
    }
}
