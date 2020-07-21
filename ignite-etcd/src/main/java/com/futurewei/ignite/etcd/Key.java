package com.futurewei.ignite.etcd;

final class Key {
    private final byte[] key;
    private final long ver;

    Key(byte[] key, long ver) {
        this.key = key;
        this.ver = ver;
    }

    byte[] key() {
        return key;
    }

    long version() {
        return ver;
    }
}
