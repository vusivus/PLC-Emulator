package de.holger_oehm.pic.progmem.hexfile;

public class DataRecord extends HexFileRecord {
    private final int length;
    private final int address;
    private final byte[] bytes;

    public DataRecord(final int length, final int address, final byte[] bytes) {
        this.length = length;
        this.address = address;
        this.bytes = bytes;
    }

    @Override
    public RecordType getType() {
        return RecordType.DATA;
    }

    public int getLength() {
        return length;
    }

    public int getAddress() {
        return address;
    }

    public byte[] getBytes() {
        return bytes;
    }
}