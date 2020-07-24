package com.futurewei.ignite.etcd;

public class Key {
    private final byte[] key;

    Key(byte[] key) {
        this.key = key;
    }

    /**
     * @return key in bytes. An empty key is not allowed.
     */
    byte[] key() {
        return key;
    }

    boolean isZero() {
        return key[0] == 0;
    }
}
