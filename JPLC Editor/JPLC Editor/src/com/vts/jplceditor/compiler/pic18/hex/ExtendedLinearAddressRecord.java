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
public class ExtendedLinearAddressRecord extends HexRecord{
    
    private final int upperAddress;
    public ExtendedLinearAddressRecord(byte[] bytes) {
        super(2, 0, bytes, RecordType.EXT_LINEAR_ADDR);

        upperAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16);
    }

    public ExtendedLinearAddressRecord(int length, int address, byte[] bytes) {
        super(length, address, bytes, RecordType.EXT_LINEAR_ADDR);
        if (length != 2) {
            throw new IllegalArgumentException("length of a segment record must be two, but is " + length);
        }
        if (address != 0) {
            throw new IllegalArgumentException("address of a segment record must be zero, but is " + length);
        }
        upperAddress = ((bytes[0] & 0x00ff) << 24) + ((bytes[1] & 0x00ff) << 16);
    }
    
    

    public int getUpperAddress() {
        return upperAddress;
    }
       
}
