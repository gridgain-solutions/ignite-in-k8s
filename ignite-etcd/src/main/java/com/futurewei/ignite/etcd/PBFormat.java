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
    static Kv.KeyValue kv(Map.Entry<? extends Key, ? extends HistoricalValue> entry) {
        return kv(entry, false);
    }

    static Kv.KeyValue kv(Map.Entry<? extends Key, ? extends HistoricalValue> entry, boolean noPayload) {
        HistoricalValue v = entry.getValue();
        Kv.KeyValue.Builder kv = Kv.KeyValue.newBuilder()
            .setKey(ByteString.copyFrom(entry.getKey().key()))
            .setVersion(v.version())
            .setCreateRevision(v.createRevision());

        if (v instanceof Value)
            kv.setModRevision(((Value)v).modifyRevision());
        else if (entry.getKey() instanceof HistoricalKey)
            kv.setModRevision(((HistoricalKey)entry.getKey()).modifyRevision());

        if (!noPayload && v.value() != null)
            kv.setValue(ByteString.copyFrom(v.value()));

        return kv.build();
    }

    static Kv.Event event(
        CacheEntryEvent<? extends Key, ? extends HistoricalValue> evt,
        boolean noPayload,
        boolean prevKv
    ) {
        EventType t = evt.getEventType();
        Kv.Event.EventType kvt = t == EventType.REMOVED || t == EventType.EXPIRED
            ? Kv.Event.EventType.DELETE
            : Kv.Event.EventType.PUT;

        Kv.Event.Builder kve = Kv.Event.newBuilder()
            .setType(kvt)
            .setKv(kv(new AbstractMap.SimpleImmutableEntry<>(evt.getKey(), evt.getValue()), noPayload));

        if (prevKv) {
            HistoricalValue oldVal = evt.getOldValue();

            if (oldVal != null)
                kve.setPrevKv(kv(new AbstractMap.SimpleImmutableEntry<>(evt.getKey(), oldVal), noPayload));
        }

        return kve.build();
    }
}
