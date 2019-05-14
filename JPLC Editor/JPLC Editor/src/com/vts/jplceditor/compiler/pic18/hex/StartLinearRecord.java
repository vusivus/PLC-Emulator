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
public class StartLinearRecord extends HexRecord{
    
    int linearAddress;
    
    public StartLinearRecord(byte[] bytes) {
        super(4, 0, bytes, RecordType.START_LINEAR_ADDR);

        linearAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16) + ((bytes[2] & 0x00ff) << 8)
                + (bytes[3] & 0x00ff);
    }

    public StartLinearRecord(int length, int address, byte[] bytes) {
        super(length, address, bytes, RecordType.START_LINEAR_ADDR);
        if (length != 4) {
            throw new IllegalArgumentException("length of a segment start record must be four, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        linearAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16) + ((bytes[2] & 0x00ff) << 8)
                + (bytes[3] & 0x00ff);
    }

    public int getLinearAddress() {
        return linearAddress;
    }
    
}
