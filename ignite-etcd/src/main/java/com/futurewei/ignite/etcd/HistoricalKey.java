package com.futurewei.ignite.etcd;

/**
 * {@link Key} with {@link #modifyRevision()}.
 */
public final class HistoricalKey extends Key {
    private final long modRev;

    HistoricalKey(Key key, long modRev) {
        super(key.key());
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
