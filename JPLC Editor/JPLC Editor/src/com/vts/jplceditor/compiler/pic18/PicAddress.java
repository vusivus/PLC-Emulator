/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

/**
 *
 * @author Vusivus
 */
public class PicAddress {

    private int fileAddr;
    private byte bitNo;
    private boolean isBit;

    public PicAddress(int address) {
        this.fileAddr = address;
        isBit = false;
    }

    public PicAddress(int address, byte bitNo) {
        this.fileAddr = address;
        this.bitNo = bitNo;
        isBit = true;
    }

    public int getFileAddr() {
        return fileAddr;
    }

    public byte getBitNo() {
        return bitNo;
    }

    @Override
    public String toString() {
        if (isBit) {
            return String.format("0x%03X", fileAddr) + ", BitNumber:" + bitNo;
        } else {
            return String.format("0x%03X", fileAddr);
        }
    }
}
