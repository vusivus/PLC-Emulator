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
public class HexRecord {
    private int length;
    private int address;
    private byte[] dataBytes;
    private RecordType recordType;

    public HexRecord(int length, int address, byte[] bytes, RecordType recordType) {
        this.length = length;
        this.address = address;
        this.dataBytes = bytes;
        this.recordType = recordType;
    }

    public HexRecord(RecordType recordType) {
        this.recordType = recordType;
    }
    
    public int getLength() {
        return length;
    }

    public int getAddress() {
        return address;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public RecordType getRecordType() {
        return recordType;
    }
        
}
