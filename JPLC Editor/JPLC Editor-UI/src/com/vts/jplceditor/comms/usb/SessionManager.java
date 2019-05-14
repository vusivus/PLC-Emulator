/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.comms.usb;

import com.vts.jplceditor.compiler.pic18.Pic18Mem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusman
 */
public class SessionManager {

    private int transactionHandle;
    private short status;
    private short sessionCmd;
    private byte sessionNumber;
    private HidManager usb;
    private int replySize;
    private boolean readingRam;

    /**
     *
     */
    public SessionManager() {
        init();
    }

    private void init() {
        usb = new HidManager();
    }

    /**
     *
     * @param packet
     * @return
     */
    public StringBuilder writeToPlc(byte[] packet) {
        StringBuilder feedback = new StringBuilder();

        if (usb.locatePlc(feedback)) {//See if PLC Device is connected.
            if (usb.connectPlc(feedback)) {//Request connection from device               
                feedback.append("Writing to PLC.");
                List<PlcMessage> messages = new ArrayList<>();
                createFlashWritePackets(packet, messages);
                if (usb.handleBullkTransfer(messages, feedback)) {
                    List<PlcMessage> readMessages = new ArrayList<>();
                    createFlashReadPackets(packet, readMessages);
                    usb.handleBullkRead(readMessages, feedback);
                    verifyProgram(usb.getReadData(), packet, feedback);
                }
            }
        } else {
            feedback.append("Could not locate PLC");
        }
        return feedback;
    }

    public StringBuilder readFromPlcRam(ByteBuffer io, ByteBuffer bank1, ByteBuffer bank2) {
        StringBuilder feedback = new StringBuilder();

        if (usb.locatePlc(feedback)) {//See if PLC Device is connected.
            if (usb.connectPlc(feedback)) {//Request connection from device               
                feedback.append("\nReading from PLC RAM.");
                List<PlcMessage> messages = createRamReadPackets(io, bank1, bank2);
                readingRam = true;
                while (readingRam) {
                    if(usb.handleBullkRead(messages, feedback)){
                       List<ByteBuffer> readData = usb.getReadData();
                       io.put(readData.get(0));
                      // bank1.put(readData.get(1), replySize, replySize);
                    }                   
                }

            }
        } else {
            feedback.append("Could not locate PLC");
        }
        return feedback;
    }

    /**
     *
     * @return
     */
    public short getStatus() {
        return status;
    }

    /**
     *
     * @param packet
     * @param messages
     */
    public void createFlashReadPackets(byte[] packet, List<PlcMessage> messages) {
        int size = packet.length;    //size of the program
        int left = size;             //bytes left to transmit
        int sessions = size / 64;    // number of 64 bit sessions required
        //if times is not multiple of 64 add an extra session for the fraction
        if (size % 64 > 0) {
            sessions += 1;
        }
        int ptr = 0;
        int address = (int) Pic18Mem.PROGRAM_ORG;
        for (int i = 0; i < sessions; i++) {
            PlcMessage msg = new PlcMessage(PlcMessage.cmdREAD_FLASH);
            msg.setAddress(address);
            msg.setLength((short) 64);
            messages.add(msg);
            address += 64;
        }
    }

    /**
     *
     * @param packet
     * @param messages
     */
    public void createFlashWritePackets(byte[] packet, List<PlcMessage> messages) {
        int size = packet.length;    //size of the program
        int left = size;             //bytes left to transmit
        int sessions = size / 32;    // number of 64 bit sessions required
        //if times is not multiple of 64 add an extra session for the fraction
        if (size % 32 > 0) {
            sessions += 1;
        }
        if (size < 32) {
            sessions = 1;
        }

        int ptr = 0;
        int address = (int) Pic18Mem.PROGRAM_ORG;
        PlcMessage erase = new PlcMessage(PlcMessage.cmdERASE);
        erase.setAddress(address);
        erase.setLength((short) sessions);
        messages.add(erase);
        for (int i = 0; i < sessions; i++) {
            PlcMessage msg = new PlcMessage(PlcMessage.cmdWRITE_FLASH);
            byte[] transmitData = new byte[32];
            msg.setAddress(address);
            msg.setLength((short) 32);
            if (left >= 32) {
                for (int k = 0; k < 32; k++) {
                    transmitData[k] = packet[ptr];
                    ++ptr;
                }
                left = left - 32;
            } else {
                for (int k = 0; k < left; k++) {
                    transmitData[k] = packet[ptr];
                    ++ptr;
                }
                for (int j = left - 1; j < 32; j++) {
                    transmitData[j] = 0;
                }
                left = 0;
            }
            msg.setData(transmitData);
            messages.add(msg);
            address += 32;
        }
    }

    public boolean isReadingRam() {
        return readingRam;
    }

    public void setReadingRam(boolean readingRam) {
        this.readingRam = readingRam;
    }

    /**
     *
     * @param readData
     * @param packet
     * @param feedback
     */
    public void verifyProgram(List<ByteBuffer> readData, byte[] packet, StringBuilder feedback) {
        int pointer = 0;
        int size = packet.length;
        int left = size;
        for (ByteBuffer buffer : readData) {
            if (left >= 64) {
                for (int i = 0; i < 64; i++) {
                    if (buffer.get(i) != packet[pointer]) {
                        feedback.append("\nByte number :").append(pointer).append(" is not the expected byte. Byte is :").append(String.format("0x%02X", buffer.get(i)));
                    }
                    ++pointer;
                }
                left = left - 64;
            } else if (left > 0 && left < 64) {
                for (int i = 0; i < left - 1; i++) {
                    if (buffer.get(i) != packet[pointer]) {
                        feedback.append("\nByte number :").append(pointer).append(" is not the expected byte. Byte is :").append(String.format("0x%02X", buffer.get(i)));
                    }
                    ++pointer;
                }
                left = 0;
            }
        }
        feedback.append("\nFinished verifying " + size + " bytes.");
    }

    private List<PlcMessage> createRamReadPackets(ByteBuffer io, ByteBuffer bank1, ByteBuffer bank2) {
        List<PlcMessage> messages = new ArrayList<>();
        PlcMessage ioRead = new PlcMessage(PlcMessage.cmdREAD_RAM);
        ioRead.setAddress(Pic18Mem.IO_START);
        ioRead.setLength((short) 20);
        messages.add(ioRead);
        if (bank1 != null) {
            int size = bank1.capacity();
            int sessions = size / 56;
            if (size % 56 > 0) {
                sessions += 1;
            }
            if (size < 56) {
                sessions = 1;
            }
            int left = size;
            int addr1 = Pic18Mem.BANK1;
            for (int k = 0; k < sessions; k++) {
                PlcMessage msg = new PlcMessage(PlcMessage.cmdWRITE_RAM);
                msg.setAddress(addr1);
                if (left > 64) {
                    msg.setLength((short) 64);
                    left -= 64;
                } else {
                    msg.setLength((short) left);
                    left = 0;
                }
                addr1 += 64;
                messages.add(msg);
            }
        }
        if (bank2 != null) {
            int size = bank2.capacity();
            int sessions = size / 64;
            if (size % 64 > 0) {
                sessions += 1;
            }
            if (size < 64) {
                sessions = 1;
            }
            int left = size;
            int addr1 = Pic18Mem.BANK2;
            for (int k = 0; k < sessions; k++) {
                PlcMessage msg = new PlcMessage(PlcMessage.cmdWRITE_RAM);
                msg.setAddress(addr1);
                if (left > 64) {
                    msg.setLength((short) 64);
                    left -= 64;
                } else {
                    msg.setLength((short) left);
                    left = 0;
                }
                addr1 += 64;
                messages.add(msg);
            }
        }
        return messages;
    }
}
