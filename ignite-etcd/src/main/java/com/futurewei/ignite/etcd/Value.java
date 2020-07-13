package com.futurewei.ignite.etcd;

import com.google.protobuf.ByteString;

import java.io.Serializable;

// TODO: protobuf-agnostic
final class Value implements Serializable {
    private static final long serialVersionUID = 0L;

    private final ByteString data;
    private final long crtRev;
    private final long modRev;

    Value(ByteString data, long crtRev, long modRev) {
        this.data = data;
        this.crtRev = crtRev;
        this.modRev = modRev;
    }

    ByteString data() {
        return data;
    }

    long createRevision() {
        return crtRev;
    }

    long modifyRevision() {
        return modRev;
    }
}
