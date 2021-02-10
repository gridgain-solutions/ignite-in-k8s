package com.futurewei.ignite.etcd;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.QueryIndexType;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

public final class CacheConfig {
    private CacheConfig() {
    }

    /**
     * Users may define a KV cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     * TODO: analyze queries, add indexes
     */
    public static String KVSpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL +
        "\nSQL: TABLE ETCD_KV (KEY BINARY, VALUE BINARY, MODIFY_REVISION BIGINT, CREATE_REVISION BIGINT, " +
        "VERSION BIGINT, LEASE BIGINT)" +
        "\n     PRIMARY KEY(KEY)" +
        "\n     INDEX(LEASE)";

    /**
     * Users may define a KV History cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     * TODO: analyze queries, add indexes
     */
    public static String KVHistorySpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL +
        "\nSQL: TABLE ETCD_KV_HISTORY (KEY BINARY, VALUE BINARY, MODIFY_REVISION BIGINT, CREATE_REVISION BIGINT, " +
        "VERSION BIGINT, LEASE BIGINT)" +
        "\n     PRIMARY KEY(KEY, MODIFY_REVISION)" +
        "\n     INDEX(KEY)" +
        "\n     INDEX(LEASE)" +
        "\n     INDEX(MODIFY_REVISION)" +
        "\n     INDEX(CREATE_REVISION)";

    /**
     * Users may define a Lease cache in the external configuration and specify the cache name as a parameter for
     * ignite-etcd. The user-defined cache must conform to this specification.
     */
    public static String LeaseSpec = "atomicityMode: " + CacheAtomicityMode.TRANSACTIONAL +
        "\nSQL: TABLE LEASE (LEASE BIGINT, TTL BIGINT, ETL BIGINT" +
        "\nPRIMARY KEY(LEASE)";

    /**
     * Default KV cache configuration is used if the user did not specify external configuration according
     * to {@link #KVSpec}.
     */
    static CacheConfiguration<Key, Value> KV(String cacheName) {
        return TextKV.extend(new CacheConfiguration<Key, Value>(cacheName)
            .setCacheMode(CacheMode.REPLICATED)
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
                    .setKeyFields(new LinkedHashSet<>(Collections.singletonList("key"))) // use modifiable Set
                    .setIndexes(Arrays.asList(
                        new QueryIndex("key"),
                        new QueryIndex("lease")
                    ))
            )));
    }

    /**
     * Default KV History cache configuration is used if the user did not specify external configuration according
     * to {@link #KVHistorySpec}.
     */
    static CacheConfiguration<HistoricalKey, HistoricalValue> KVHistory(String cacheName) {
        return TextKV.extend(new CacheConfiguration<HistoricalKey, HistoricalValue>(cacheName)
            .setCacheMode(CacheMode.REPLICATED)
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
                    .setKeyFields(new LinkedHashSet<>(Arrays.asList("key", "modRev"))) // use modifiable Set
                    .setIndexes(Arrays.asList(
                        new QueryIndex(Arrays.asList("key", "modRev"), QueryIndexType.SORTED),
                        new QueryIndex("key"),
                        new QueryIndex("lease"),
                        new QueryIndex("modRev"),
                        new QueryIndex("crtRev")
                    ))
            )));
    }

    /**
     * Default Lease cache configuration is used if the user did not specify external configuration according
     * to {@link #LeaseSpec}.
     */
    static CacheConfiguration<Long, Ttl> Lease(String cacheName) {
        return new CacheConfiguration<Long, Ttl>(cacheName)
            .setCacheMode(CacheMode.REPLICATED)
            .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
            .setSqlSchema("PUBLIC")
            .setQueryEntities(Collections.singleton(
                new QueryEntity(Long.class, Ttl.class)
                    .setTableName("ETCD_LEASE")
                    .addQueryField("lease", Long.class.getName(), null)
                    .addQueryField("ttl", Long.class.getName(), null)
                    .addQueryField("etl", Long.class.getName(), null)
                    .setKeyFieldName("lease")
                    .setIndexes(Collections.singletonList(new QueryIndex("lease")))
            ));
    }
}
