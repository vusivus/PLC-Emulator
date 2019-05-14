package de.holger_oehm.pic.progmem.hexfile;

public class LinearAddressRecord extends HexFileRecord {
    private final int upperAddress;

    public LinearAddressRecord(final int length, final int address, final byte[] bytes) {
        if (length != 2) {
            throw new IllegalArgumentException("length of a segment record must be two, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        upperAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16);
    }

    @Override
    public RecordType getType() {
        return RecordType.LIN_ADDR;
    }

    public int getUpperAddress() {
        return upperAddress;
    }
}