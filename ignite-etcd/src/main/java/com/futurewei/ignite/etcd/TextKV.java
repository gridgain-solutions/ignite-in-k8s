package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.configuration.CacheConfiguration;

/**
 * The class is for troubleshooting only. Enables storing etcd keys and values as strings in fields "SKEY" and "SVALUE"
 * in addition to the binary fields "key" and "val". This allows using SQL against the "SKEY" and "SVALUE" to help with
 * troubleshooting the cache contents.
 * System property {@code TEXT_KV} must be set to {@code true} to enable this functionality.
 */
final class TextKV {
    private TextKV() {
    }

    static final boolean isEnabled = Boolean.getBoolean("TEXT_KV");

    /**
     * Extends {@link CacheConfiguration} to store binary keys and values as strings.
     *
     * @param cfg Etcd KV {@link CacheConfiguration} with initialized query entity.
     * @return Extended {@link CacheConfiguration}.
     */
    static <K, V> CacheConfiguration<K, V> extend(CacheConfiguration<K, V> cfg) {
        if (!isEnabled)
            return cfg;

        cfg.getQueryEntities().iterator().next()
            .addQueryField("skey", String.class.getName(), null)
            .addQueryField("sval", String.class.getName(), "SVALUE")
            .getKeyFields().add("skey");

        return cfg;
    }

    public static void writeBinary(Key k, BinaryWriter writer) {
        if (!isEnabled)
            return;

        writer.writeString("skey", ByteString.copyFrom(k.key()).toStringUtf8());
    }

    public static void writeBinary(HistoricalValue v, BinaryWriter writer) {
        if (!isEnabled)
            return;

        writer.writeString("sval", ByteString.copyFrom(v.value()).toStringUtf8());
    }
}
