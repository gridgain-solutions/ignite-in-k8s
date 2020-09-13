package com.futurewei.ignite.etcd;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;

public class Value extends HistoricalValue {
    private static final long serialVersionUID = 1L;

    private long modRev;

    Value(byte[] val, long crtRev, long modRev, long ver, long lease) {
        super(val, crtRev, ver, lease);
        this.modRev = modRev;
    }

    Value(HistoricalValue histVal, long modRev) {
        super(histVal.value(), histVal.createRevision(), histVal.version(), histVal.lease());
        this.modRev = modRev;
    }

    /**
     * @return the revision of last modification on this key.
     */
    long modifyRevision() {
        return modRev;
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
}
