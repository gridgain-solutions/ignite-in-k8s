package com.futurewei.ignite.etcd;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

import java.io.Serializable;

/**
 * {@link Value} without {@link Value#modifyRevision()} since {@link HistoricalValue} is always paired with
 * {@link HistoricalKey} that already has {@link HistoricalKey#modifyRevision()}.
 */
public class HistoricalValue implements Binarylizable, Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] val;
    private long crtRev;
    private long ver;
    private long lease;

    static final HistoricalValue TOMBSTONE = new HistoricalValue(null, 0, 0, 0);

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
     * @return A key’s life spans a generation, from creation to deletion. Each key may have one or multiple
     * generations. Creating a key increments the version of that key, starting at 1 if the key does not exist at
     * the current revision. Deleting a key generates a key tombstone, concluding the key’s current generation by
     * resetting its version to 0. Each modification of a key increments its version; so, versions are monotonically
     * increasing within a key’s generation. Once a compaction happens, any generation ended before the compaction
     * revision will be removed, and values set before the compaction revision except the latest one will be removed.
     */
    long version() {
        return ver;
    }

    /**
     * @return the ID of the lease that attached to key. When the attached lease expires, the key will be deleted.
     * If lease is 0, then no lease is attached to the key.
     */
    long lease() {
        return lease;
    }

    /**
     * @return {@code true} if this "entry deleted" marker (tombstone).
     */
    boolean isTombstone() {
        return version() == 0;
    }

    @Override
    public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        writer.writeByteArray("val", val);
        writer.writeLong("crtRev", crtRev);
        writer.writeLong("ver", ver);
        writer.writeLong("lease", lease);

        if (TextKV.isEnabled)
            TextKV.writeBinary(this, writer);
    }

    @Override
    public void readBinary(BinaryReader reader) throws BinaryObjectException {
        val = reader.readByteArray("val");
        crtRev = reader.readLong("crtRev");
        ver = reader.readLong("ver");
        lease = reader.readLong("lease");
    }
}
