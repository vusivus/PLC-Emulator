/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.hex;

/**
 *
 * @author Vusivus
 */
public class StartSegmentAddressRecord extends HexRecord {

    private final int segment;
    private final int offset;

    public StartSegmentAddressRecord(byte[] bytes) {
        super(4, 0, bytes, RecordType.START_SEG_ADDR);

        segment = ((bytes[0] & 0x00ff) << 8) + (bytes[1] & 0x00ff);
        offset = ((bytes[2] & 0x00ff) << 8) + (bytes[3] & 0x00ff);
    }

    public StartSegmentAddressRecord(int length, int address, byte[] bytes) {
        super(length, address, bytes, RecordType.START_SEG_ADDR);
        if (length != 4) {
            throw new IllegalArgumentException("length of a segment start record must be four, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        segment = ((bytes[0] & 0x00ff) << 8) + (bytes[1] & 0x00ff);
        offset = ((bytes[2] & 0x00ff) << 8) + (bytes[3] & 0x00ff);
    }

    public int getSegment() {
        return segment;
    }

    public int getOffset() {
        return offset;
    }

}
