package com.futurewei.ignite.etcd;

public final class Value extends HistoricalValue {
    private final long modRev;

    Value(byte[] val, long crtRev, long modRev, long ver, long lease) {
        super(val, crtRev, ver, lease);
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
