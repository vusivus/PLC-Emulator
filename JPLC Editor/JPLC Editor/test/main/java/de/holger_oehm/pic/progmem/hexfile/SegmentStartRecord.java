package de.holger_oehm.pic.progmem.hexfile;

public class SegmentStartRecord extends HexFileRecord {
    private final int segment;
    private final int offset;

    public SegmentStartRecord(final int length, final int address, final byte[] bytes) {
        if (length != 4) {
            throw new IllegalArgumentException("length of a segment start record must be four, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        segment = ((bytes[0] & 0x00ff) << 8) + (bytes[1] & 0x00ff);
        offset = ((bytes[2] & 0x00ff) << 8) + (bytes[3] & 0x00ff);
    }

    @Override
    public RecordType getType() {
        return RecordType.SEG_START;
    }

    public int getSegment() {
        return segment;
    }

    public int getOffset() {
        return offset;
    }
}