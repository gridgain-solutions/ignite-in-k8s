package com.futurewei.ignite.etcd;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;

import java.util.Objects;

/**
 * {@link Key} with {@link #modifyRevision()}.
 */
public class HistoricalKey extends Key {
    private static final long serialVersionUID = 1L;

    private long modRev;

    HistoricalKey(Key key, long modRev) {
        super(key.key());
        this.modRev = modRev;
    }

    @Override
    public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        super.writeBinary(writer);
        writer.writeLong("modRev", modRev);
    }

    @Override
    public void readBinary(BinaryReader reader) throws BinaryObjectException {
        super.readBinary(reader);
        modRev = reader.readLong("modRev");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HistoricalKey that = (HistoricalKey) o;
        return modRev == that.modRev;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), modRev);
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
    }
}
