package com.futurewei.ignite.etcd;

final class Value extends HistoricalValue {
    private final long modRev;

    Value(byte[] val, long crtRev, long modRev, long ver) {
        super(val, crtRev, ver);
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
