package com.futurewei.ignite.etcd;

import java.io.Serializable;

/**
 * {@link Value} without {@link Value#modifyRevision()} since {@link HistoricalValue} is always paired with
 * {@link HistoricalKey} that already has {@link HistoricalKey#modifyRevision()}.
 */
public class HistoricalValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private final byte[] val;
    private final long crtRev;
    private final long ver;
    private final long lease;

    HistoricalValue(byte[] val, long crtRev, long ver, long lease) {
        this.val = val;
        this.crtRev = crtRev;
        this.ver = ver;
        this.lease = lease;
    }

    /**
     * @return value held by the key, in bytes.
     */
    byte[] value() {
        return val;
    }

    /**
     * @return the revision of last creation on this key.
     */
    long createRevision() {
        return crtRev;
    }

    /**
     * @return version of the key. A deletion resets the version to zero and any modification of the key increases
     * its version.
     */
    long version() {
        return ver;
    }

    /**
     * @return the ID of the lease that attached to key. When the attached lease expires, the key will be deleted.
     * If lease is 0, then no lease is attached to the key.
     */
    public long lease() {
        return lease;
    }
}
