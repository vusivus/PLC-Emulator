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
public class LiteralInstruction implements Pic18Instruction {

    private final Opcode opcode;
    private short literal;
    private short bitCode, bitCode2;
    private int wordCount;
    private byte f;

    public LiteralInstruction(Opcode opcode, int literal) {
        this.opcode = opcode;
        this.literal = (short) literal;
    }

    public LiteralInstruction(Opcode opcode, byte f, int literal) {
        this.opcode = opcode;
        this.literal = (short) (literal & 0xFFF);
        this.f = (byte) (f & 0x03);
    }

    @Override
    public void Assemble() {
        bitCode = opcode.getHexCode();
        switch (opcode.getBits()) {
            case 8:
                bitCode <<= 8;
                bitCode += literal & 0xFF;
                wordCount = 1;
                break;
            case 12:
                bitCode <<= 4;
                bitCode |= literal & 0xF;
                wordCount = 1;
                break;
            case 10:
                bitCode |= f;
                bitCode <<= 4;
                bitCode |= (literal & 0xF00) >> 8;
                bitCode2 = (short) 0xF000;
                bitCode2 |= literal & 0xFF;
                wordCount = 2;
                break;
            default:
                break;
        }
    }

    @Override
    public short get16BitCode() {
        return bitCode;
    }

    @Override
    public short getSecond16BitCode() {
        return bitCode2;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public void setFile2(int file) {
        literal = (short) file;
    }

    @Override
    public void setFile(int file) {

    }

    public void setF(byte f) {
        this.f = (byte) (f * 0x03);
    }

    @Override
    public String toString() {
        if (wordCount == 1) {
            return instrStr();
        } else {
            return instrStr();
        }
    }

    private String instrStr() {
        if (opcode.getBits() == 8) {
            return opcode.name() + " " + String.format("0x%02X", (literal ));
        }
        if (opcode.getBits() == 12) {
            return opcode.name() + " " + String.format("0x%01X", (literal ));
        } else {
            return opcode.name() + " " + String.format("0x%01X", (f )) + ", " + String.format("0x%03X", (literal ));
        }
    }
}
