package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;

final class Key {
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
