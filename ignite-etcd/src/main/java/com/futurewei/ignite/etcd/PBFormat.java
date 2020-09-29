package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;
import mvccpb.Kv;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.EventType;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Protobuf serialization and deserialization.
 */
final class PBFormat {
    static Kv.KeyValue kv(Map.Entry<? extends Key, ? extends Value> entry) {
        return kv(entry, false);
    }

    static Kv.KeyValue kv(Map.Entry<? extends Key, ? extends Value> entry, boolean keyOnly) {
        Value v = entry.getValue();
        Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
            .setKey(ByteString.copyFrom(entry.getKey().key()))
            .setVersion(v.version())
            .setCreateRevision(v.createRevision())
            .setModRevision(v.modifyRevision());

        if (!keyOnly && v.value() != null)
            kv.setValue(ByteString.copyFrom(v.value()));

        return kv.build();
    }

    static Kv.Event event(CacheEntryEvent<? extends Key, ? extends Value> evt, boolean prevKv) {
        Key k = evt.getKey();
        Value v = evt.getValue();
        EventType t = evt.getEventType();
        Kv.Event.EventType kvt = t == EventType.REMOVED || t == EventType.EXPIRED
            ? Kv.Event.EventType.DELETE
            : Kv.Event.EventType.PUT;

        Kv.Event.Builder kve = Kv.Event.newBuilder()
            .setType(kvt)
            .setKv(kv(new AbstractMap.SimpleImmutableEntry<>(k, v), false));

        if (prevKv) {
            Value oldVal = evt.getOldValue();

            if (oldVal != null)
                kve.setPrevKv(kv(new AbstractMap.SimpleImmutableEntry<>(k, oldVal), false));
        }

        return kve.build();
    }
}
