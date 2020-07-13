package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;

import java.io.Serializable;

// TODO: protobuf-agnostic
final class Key implements Serializable {
    private static final long serialVersionUID = 0L;

    private final ByteString data;
    private final long ver;

    Key(ByteString data, long ver) {
        this.data = data;
        this.ver = ver;
    }

    ByteString data() {
        return data;
    }

    long version() {
        return ver;
    }
}
