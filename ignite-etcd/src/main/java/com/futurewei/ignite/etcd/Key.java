package com.futurewei.ignite.etcd;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

import java.io.Serializable;
import java.util.Arrays;

public class Key implements Binarylizable, Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] key;

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

    @Override
    public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        writer.writeByteArray("key", key);

        if (TextKV.isEnabled)
            TextKV.writeBinary(this, writer);
    }

    @Override
    public void readBinary(BinaryReader reader) throws BinaryObjectException {
        key = reader.readByteArray("key");
    }
}
