package com.futurewei.ignite.etcd;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Collections;
import java.util.Set;

final class CacheConfig {
    private CacheConfig() {}

    public static final String KV_TBL_NAME = "ETCD_KV";

    public static CacheConfiguration<Key, Value> KV(String name) {
        return new CacheConfiguration<Key, Value>(name)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1)
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setSqlSchema("PUBLIC")
                .setQueryEntities(Collections.singleton(
                        new QueryEntity(Key.class, Value.class)
                                .setTableName(KV_TBL_NAME)
                                .addQueryField("key", byte[].class.getName(), null)
                                .addQueryField("ver", long.class.getName(), "VERSION")
                                .addQueryField("val", byte[].class.getName(), "VALUE")
                                .addQueryField("crtRev", long.class.getName(), "CREATE_REVISION")
                                .addQueryField("modRev", long.class.getName(), "MODIFY_REVISION")
                                .addQueryField("lease", long.class.getName(), null)
                                .setKeyFields(Set.of("key", "ver"))
                ));
    }
}
