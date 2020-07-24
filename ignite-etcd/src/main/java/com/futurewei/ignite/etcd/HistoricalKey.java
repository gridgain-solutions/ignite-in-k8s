package com.futurewei.ignite.etcd;

final class HistoricalKey {
    private final byte[] key;
    private final long modRev;

    HistoricalKey(byte[] key, long modRev) {
        this.key = key;
        this.modRev = modRev;
    }

    /**
     * @return key in bytes. An empty key is not allowed.
     */
    byte[] key() {
        return key;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
