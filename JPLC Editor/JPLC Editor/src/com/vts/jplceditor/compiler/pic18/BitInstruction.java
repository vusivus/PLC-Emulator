/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

/**
 * Represents PIC 18 Bit Instruction
 *
 * @author Vusivus
 */
public class BitInstruction implements Pic18Instruction {

    private final Opcode opcode;
    private final byte b;
    private final boolean a;
    private short file;
    private short bitCode;

    /**
     * Instantiates the instruction
     *
     * @param opcode<p>
     * The Bit Oriented Opcode</p>
     * @param file
     * <p>
     * The 8 bit file address</p>
     * @param b
     * <p>
     * The bit number (from 0 to 7)</p>
     * @param a
     * <p>
     * a=0(false) to force Access Bank</p>
     * <p>
     * a=1(true) for BSR o select Bank</p>
     */
    public BitInstruction(Opcode opcode, int file, byte b, boolean a) {
        this.opcode = opcode;
        this.b = b;
        this.a = a;
        this.file = (short) file;
    }

    /**
     * Change the file address
     *
     * @param file the new 8 it file address
     */
    @Override
    public void setFile(int file) {
        this.file = (short) file;
    }

    /**
     * Change the file address
     *
     * @param file the new 8 it file address
     */
    @Override
    public void setFile2(int file) {
    }

    /**
     * Assembles the instruction into a 16 bit code
     */
    @Override
    public void Assemble() {
        if (opcode.getBits() == 4) {
            bitCode = opcode.getHexCode();
            bitCode |= (b & 0x07) << 1;
            if (a == true) {
                bitCode += 0x01;
            }
            bitCode <<= 8;
            bitCode += file & 0xFF;
        }
    }

    /**
     *
     * @return the 16 Bit code
     */
    @Override
    public short get16BitCode() {
        return bitCode;
    }

    /**
     *
     * @return return 0
     */
    @Override
    public short getSecond16BitCode() {
        return 0;
    }

    /**
     *
     * @return returns 1
     */
    @Override
    public int getWordCount() {
        return 1;
    }

    @Override
    public String toString() {
        return instrStr();
    }

    private String instrStr() {
        int a = 0;
        if (this.a == true) {
            a = 1;
        }
        return opcode.name() + " " + String.format("0x%02X", (file )) + ", " + b + ", " + a;
    }
}
