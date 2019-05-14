/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

/**
 * Represents a PIC18 Byte Oriented Instruction
 *
 * @author Vusivus
 */
public class ByteInstruction implements Pic18Instruction {

    private final boolean d;
    private final boolean a;
    private short file, file2;
    private final Opcode op;
    private short bitCode, bitCode2;
    private int wordCount;

    /**
     * Instantiates a Byte Oriented Instruction
     *
     * @param opcode
     * <p>
     * The Byte oriented Opcode</p>
     *
     * @param file 8 bit file register address
     * @param a
     * <p>
     * a=0(false) to force Access Bank</p>
     * <p>
     * a=1(true) for BSR o select Bank</p>
     * <p>
     * If the opcode doesn't use a set it to false</p>
     * @param d<p>
     * d=0(false) for result destination to be WREG</p>
     * <p>
     * d=1(true) for result destination to be file</p>
     * <p>
     * If the opcode doesn't use d set it to false</p>
     */
    public ByteInstruction(Opcode opcode, int file, boolean a, boolean d) {
        this.d = d;
        this.a = a;
        this.file = (short) file;
        this.op = opcode;
    }

    /**
     * Assembles the instruction into a 16 bit code
     */
    @Override
    public void Assemble() {
        bitCode = op.getHexCode();

        if (op.getBits() == 6 || op.getBits() == 7) {
            if (d == true) {
                bitCode |= 0x02;
            }
            if (a == true) {
                bitCode |= 0x01;
            }
            bitCode <<= 8;
            bitCode |= (file & 0xFF);
            wordCount = 1;
        } else if (op.getBits() == 8) {
            bitCode = (short) (op.getHexCode() & 0xF0);
            bitCode <<= 8;
            bitCode |= (file & 0xFFF);
            bitCode2 = (short) 0xF000;
            bitCode2 |= (file2 & 0xFFF);
            wordCount = 2;
        }
    }

    /**
     * change the file address
     *
     * @param file the new 8 bit address
     */
    public void setFile(int file) {
        this.file = (short) file;
    }

    /**
     * Set the file address for two word Byte Oriented Instructions like MOVFF
     *
     * @param file2 8 bit address of destination file
     */
    @Override
    public void setFile2(int file2) {
        this.file2 = (short) file2;
    }

    /**
     *
     * @return the assembled 16 bit PIC code for the Instruction
     */
    @Override
    public short get16BitCode() {
        return bitCode;
    }

    /**
     *
     * @return the second word of the assembled 16 bit PIC code for the
     * Instruction
     */
    @Override
    public short getSecond16BitCode() {
        return bitCode2;
    }

    /**
     *
     * @return the number of 16 bit words in the instruction
     */
    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public String toString() {
        if (wordCount == 1) {
            return instrStr() ;
        } else {
            return instrStr();
        }
    }

    private String instrStr() {
        int d = 0, a = 0;
        if (this.d == true) {
            d = 1;
        }
        if (this.a == true) {
            a = 1;
        }
        if (op.getBits() == 6) {
            return op.name() + " " + String.format("0x%02X", (file)) + ", " + d + ", " + a;
        }
        if (op.getBits() == 7) {
            return op.name() + " " + String.format("0x%02X", (file)) + ", " + a;
        } else {
            return op.name() + " " + String.format("0x%02X", (file)) + ", " + String.format("0x%02X", (file2));
        }
    }
}
