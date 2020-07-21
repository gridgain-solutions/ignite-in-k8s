package com.futurewei.ignite.etcd;

final class Value {
    private final byte[] val;
    private final long crtRev;
    private final long modRev;
    private long lease;

    Value(byte[] val, long crtRev, long modRev) {
        this.val = val;
        this.crtRev = crtRev;
        this.modRev = modRev;
    }

    byte[] value() {
        return val;
    }

    long createRevision() {
        return crtRev;
    }

    long modifyRevision() {
        return modRev;
    }

    public long lease() {
        return lease;
    }

    public Value lease(long lease) {
        this.lease = lease;
        return this;
    }
}
