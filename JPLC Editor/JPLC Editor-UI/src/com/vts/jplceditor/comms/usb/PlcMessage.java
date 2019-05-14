/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.comms.usb;

/**
 *
 * @author Electronix
 */
public class PlcMessage {

    private final short cmd;
    private int address;
    private short length;
    private byte[] data;

    /**
     *
     */
    public final static short cmdCONNECT = 0x06;

    /**
     *
     */
    public final static short cmdREAD_FLASH = 0x02;

    /**
     *
     */
    public final static short cmdREAD_RAM = 0x03;

    /**
     *
     */
    public final static short cmdWRITE_FLASH = 0x01;

    /**
     *
     */
    public final static short cmdWRITE_RAM = 0x04;

    /**
     *
     */
    public final static short cmdERASE = 0x05;

    /**
     *
     * @param cmd
     */
    public PlcMessage(short cmd) {
        this.cmd = cmd;
    }

    /**
     *
     * @param address
     */
    public void setAddress(int address) {
        this.address = address;
    }

    /**
     *
     * @param length
     */
    public void setLength(short length) {
        this.length = length;
    }

    /**
     *
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     *
     * @return
     */
    public short getCmd() {
        return cmd;
    }

    /**
     *
     * @return
     */
    public long getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public short getLength() {
        return length;
    }

    /**
     *
     * @return
     */
    public byte[] getData() {
        return data;
    }

}
