package de.holger_oehm.pic.progmem.hexfile;

public class LinearStartRecord extends HexFileRecord {
    private final int linearAddress;

    public LinearStartRecord(final int length, final int address, final byte[] bytes) {
        if (length != 4) {
            throw new IllegalArgumentException("length of a segment start record must be four, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        linearAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16) + ((bytes[2] & 0x00ff) << 8)
                + (bytes[3] & 0x00ff);
    }

    @Override
    public RecordType getType() {
        return RecordType.LIN_START;
    }

    public int getLinearAddress() {
        return linearAddress;
    }
}