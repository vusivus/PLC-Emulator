package de.holger_oehm.pic.progmem.hexfile;

public class SegmentRecord extends HexFileRecord {
    private final int segment;

    public SegmentRecord(final int length, final int address, final byte[] bytes) {
        if (length != 2) {
            throw new IllegalArgumentException("length of a segment record must be two, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        segment = ((bytes[0] & 0x00ff) << 8) + (bytes[1] & 0x00ff);
    }

    @Override
    public RecordType getType() {
        return RecordType.SEG_ADDR;
    }

    public int getSegment() {
        return segment;
    }

    public int getUpperAddress() {
        return segment << 8;
    }
}