package com.futurewei.ignite.etcd;

/**
 * {@link Key} with {@link #modifyRevision()}.
 */
public final class HistoricalKey extends Key {
    private final long modRev;

    HistoricalKey(byte[] key, long modRev) {
        super(key);
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
