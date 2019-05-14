package de.holger_oehm.pic.progmem.hexfile;

public class EOFRecord extends HexFileRecord {
    public EOFRecord() {
    }

    @Override
    public RecordType getType() {
        return RecordType.EOF;
    }
}