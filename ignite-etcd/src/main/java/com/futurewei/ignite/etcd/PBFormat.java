package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import mvccpb.Kv;

import java.util.Map;

/**
 * Protobuf serialization and deserialization.
 */
final class PBFormat {
    static Kv.KeyValue kv(Map.Entry<Key, Value> entry) {
        return kv(entry, false);
    }

    static Kv.KeyValue kv(Map.Entry<Key, Value> entry, boolean noPayload) {
        Value v = entry.getValue();
        Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
            .setKey(ByteString.copyFrom(entry.getKey().key()))
            .setVersion(v.version())
            .setCreateRevision(v.createRevision())
            .setModRevision(v.modifyRevision());

        if (!noPayload)
            kv.setValue(ByteString.copyFrom(v.value()));

        return kv.build();
    }
}
