package com.futurewei.ignite.etcd;

public final class Value extends HistoricalValue {
    private static final long serialVersionUID = 1L;

    private final long modRev;

    Value(byte[] val, long crtRev, long modRev, long ver, long lease) {
        super(val, crtRev, ver, lease);
        this.modRev = modRev;
    }

    Value(HistoricalValue histVal, long modRev) {
        super(histVal.value(), histVal.createRevision(), histVal.version(), histVal.lease());
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
