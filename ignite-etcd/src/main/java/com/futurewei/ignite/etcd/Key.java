package com.futurewei.ignite.etcd;

import java.io.Serializable;
import java.util.Arrays;

public class Key implements Serializable {
    private static final long serialVersionUID = 1L;

    private final byte[] key;

    Key(byte[] key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key1 = (Key) o;
        return Arrays.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
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
