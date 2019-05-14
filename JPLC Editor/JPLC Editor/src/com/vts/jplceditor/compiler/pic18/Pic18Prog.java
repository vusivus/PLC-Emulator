/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 *
 * @author Vusman
 */
public class Pic18Prog {

    private ByteBuffer codeHex;
    private ByteBuffer configHex;
    private ByteBuffer devIdHex;
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private int size = 0;
    private final int org;
    private List<Pic18Instruction> program;

    public Pic18Prog(int org, List<Pic18Instruction> prog) {
       this.org = org;
       program = prog;
       program.stream().forEach((i) -> {
            size += i.getWordCount();
        });
        System.out.println("Buffer Size is " + size);
        size *= 2;
        codeHex = ByteBuffer.allocate(size);
        codeHex.order(byteOrder);
        putProg(program);
        configHex = ByteBuffer.allocate(14);
        configHex.order(byteOrder);
        putConfig();
        devIdHex = ByteBuffer.allocate(2);
        putDevId();
    }

    private void putProg(List<Pic18Instruction> prog) {
        prog.stream().forEach((i) -> {
            if (i.getWordCount() == 2) {
                codeHex.putShort(i.get16BitCode());
                codeHex.putShort(i.getSecond16BitCode());
            } else {
                codeHex.putShort(i.get16BitCode());
            }
        });
        //fixBytes(codeHex);
        codeHex.rewind();
    }

    public byte readCodeByte(int index) {
        return codeHex.get(index);
    }

    public int codeBufferPosition() {
        return codeHex.position();
    }

    public ByteBuffer getCodeHex() {
        return codeHex;
    }

    public byte readConfigByte() {
        return configHex.get();
    }

    public ByteBuffer getConfigHex() {
        return configHex;
    }

    private void putConfig() {
        configHex.put((byte) Pic18Mem.CONFIG1H);
        configHex.put((byte) Pic18Mem.CONFIG1L);
        configHex.put((byte) Pic18Mem.CONFIG2H);
        configHex.put((byte) Pic18Mem.CONFIG2L);
        configHex.put((byte) Pic18Mem.CONFIG3H);
        configHex.put((byte) Pic18Mem.CONFIG3L);
        configHex.put((byte) Pic18Mem.CONFIG4H);
        configHex.put((byte) Pic18Mem.CONFIG4L);
        configHex.put((byte) Pic18Mem.CONFIG5H);
        configHex.put((byte) Pic18Mem.CONFIG5L);
        configHex.put((byte) Pic18Mem.CONFIG6H);
        configHex.put((byte) Pic18Mem.CONFIG6L);
        configHex.put((byte) Pic18Mem.CONFIG7H);
        configHex.put((byte) Pic18Mem.CONFIG7L);
        fixBytes(configHex);
        configHex.rewind();
    }

    private void putDevId() {
        devIdHex.put((byte) Pic18Mem.DEVID2);
        devIdHex.put((byte) Pic18Mem.DEVID1);
        fixBytes(devIdHex);
        devIdHex.rewind();
    }

    private void fixBytes(ByteBuffer buf) {
        for (int i = 0; i < buf.capacity(); i++) {
            byte b = swapNibbles(buf.get(i));
            buf.put(i, b);
        }
    }

    private byte swapNibbles(byte x) {
        return (byte) ((x & 0x0F) << 4 | (x & 0xF0) >> 4);
    }

    public ByteBuffer getDevIdHex() {
        return devIdHex;
    }

    public int getSize() {
        return size;
    }

    public int getOrg() {
        return org;
    }

    public StringBuffer getProgramMap() {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < codeHex.capacity(); i++) {
            buff.append(String.format("0x%04X", org + i)).append(" : ").append(String.format("0x%02X", codeHex.get(i)));
            buff.append("\n");
        }
        return buff;
    }

    public String getProgramInformation() {
        StringBuilder info = new StringBuilder();
        info.append("==========================================");
        info.append("\nProgram  Start : ").append(String.format("0x%04X", org));
        info.append("\n");
        info.append("Program Size : ").append(codeHex.capacity()).append(" bytes.");
        info.append("\n");

        return info.toString();
    }

}
