package com.futurewei.ignite.etcd;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

import java.time.Instant;

/** Cache entries Time-To-Live (TTL) info. */
public class Ttl implements Binarylizable {
    private long ttl;
    private long etl;

    public Ttl(long ttl, long etl) {
        this.ttl = ttl;
        this.etl = etl;
    }

    /**
     * @return Original Time-To-Live: the time interval a cache entry exists after it was created, updated or accessed
     * (depends on the cache expiry policy).
     */
    public long ttl() {
        return ttl;
    }

    /**
     * @return Epoch-To-Live: the Unix Epoch (in seconds) until a cache entry exists after it was created, updated
     * or accessed (depends on the cache expiry policy).
     */
    public long etl() {
        return etl;
    }

    /** {@inheritDoc} */
    @Override
    public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        writer.writeLong("ttl", ttl);
        writer.writeLong("etl", etl);
    }

    /** {@inheritDoc} */
    @Override
    public void readBinary(BinaryReader reader) throws BinaryObjectException {
        ttl = reader.readLong("ttl");
        etl = reader.readLong("etl");
    }

    /** @return New {@link Ttl} with the {@link #etl()} increased by {@link #ttl()}. */
    public Ttl keepAlive() {
        return new Ttl(ttl, Instant.now().getEpochSecond() + ttl);
    }

    /** @return Remaining Time-To-Live: the remaining time a cache entry exists until {@link #etl()}. */
    public long remainingTtl() {
        long t = etl - Instant.now().getEpochSecond();
        return t > 0 ? t : 0;
    }
}
