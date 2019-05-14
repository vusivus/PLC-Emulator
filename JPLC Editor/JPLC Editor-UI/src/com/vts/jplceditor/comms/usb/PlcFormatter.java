/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.comms.usb;

import java.nio.ByteBuffer;

/**
 *
 * @author Electronix
 */
public abstract class PlcFormatter {

    //DF1 Protocol bytes
    static final short DLE = 0x10;
    static final short STX = 0x02;
    private static final short ENQ = 0x05;
    private static final short ACK = 0x06;
    private static final short NAK = 0x0F;
    static final short ETX = 0x03;

    /**
     *
     * @param msg
     * @param transmitBuf
     * @return
     */
    public static int formatMessage(PlcMessage msg, ByteBuffer transmitBuf) {
        int length = 0;
        short cmd = msg.getCmd();
        transmitBuf.rewind();
        transmitBuf.put((byte) DLE);
        transmitBuf.put((byte) STX);
        switch (cmd) {
            case PlcMessage.cmdCONNECT:
                transmitBuf.put((byte) cmd);
                transmitBuf.put((byte) DLE);
                transmitBuf.put((byte) ETX);
                length = 5;
                break;
            case PlcMessage.cmdERASE:
                transmitBuf.put((byte) cmd);
                transmitBuf.putShort((short) msg.getAddress());
                transmitBuf.put((byte) msg.getLength());
                transmitBuf.put((byte) DLE);
                transmitBuf.put((byte) ETX);
                length = 8;
                break;
            case PlcMessage.cmdWRITE_FLASH:
                transmitBuf.put((byte) cmd);
                transmitBuf.putShort((short) msg.getAddress());
                transmitBuf.put((byte) msg.getLength());
                transmitBuf.put(msg.getData());
                transmitBuf.put((byte) DLE);
                transmitBuf.put((byte) ETX);

                length = 40;
                break;
            case PlcMessage.cmdREAD_FLASH:
                transmitBuf.put((byte) cmd);
                transmitBuf.putShort((short) msg.getAddress());
                transmitBuf.put((byte) msg.getLength());
                transmitBuf.put((byte) DLE);
                transmitBuf.put((byte) ETX);

                length = 8;
                break;

        }
        return length;
    }
}
