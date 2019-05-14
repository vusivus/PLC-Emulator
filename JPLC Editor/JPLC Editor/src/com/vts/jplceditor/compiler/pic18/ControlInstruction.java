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
public class ControlInstruction implements Pic18Instruction {

    private Opcode opcode;
    private boolean s;
    private int n;
    private short bitCode, bitCode2;
    private int wordCount;

    public ControlInstruction(Opcode opcode) {
        this.opcode = opcode;
    }

    public ControlInstruction(Opcode opcode, int n) {
        this.opcode = opcode;
        this.n = n;
    }

    public ControlInstruction(Opcode opcode, boolean s) {
        this.opcode = opcode;
        this.s = s;
    }

    public ControlInstruction(Opcode opcode, boolean s, int n) {
        this.opcode = opcode;
        this.s = s;
        this.n = n;
    }

    @Override
    public void Assemble() {
        if (null != opcode) switch (opcode) {
            case GOTO:{
                bitCode = (short) 0xEF00;
                bitCode |= (byte) n & 0xFF;
                int temp = (short) (n & 0xFFF00);
                bitCode2 = (short) (temp >> 8);
                bitCode2 |= 0xF0000;
                wordCount = 2;
                    break;
                }
            case BRA:{
                bitCode = (short) 0xD000;
                bitCode |= (short) n & 0x7FF;
                wordCount = 1;
                    break;
                }
            case CALL:{
                bitCode = (short) 0xEC00;
                if (s) {
                    bitCode |= 0x0100;
                }       bitCode |= (byte) n & 0xFF;
                int temp = (short) (n & 0xFFF00);
                bitCode2 = (short) (temp >> 8);
                bitCode2 |= 0xF0000;
                wordCount = 2;
                    break;
                }
            case NOP:
                bitCode = 0;
                wordCount = 1;
                break;
            case RETURN:
                bitCode = 0x12;
                if (s) {
                    bitCode |= 0x1;
                }   wordCount=1;
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
    public void setFile(int file) {
        n = file;
    }

    @Override
    public void setFile2(int file) {

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
        int s1 = 0;
        if (s) {
            s1 = 1;
        }
        switch (opcode.getBits()) {
            case 8:
                return opcode.name() + " " + String.format("0x%02X", (n));
            case 12:
                return opcode.name() + " " + String.format("0x%05X", (n));
            case 11:
                return opcode.name() + " " + String.format("0x%05X", (n)) + " " + s1;
            case 5:
                return opcode.name() + " " + String.format("0x%03X", (n));
            case 15:
                return opcode.name() + " " + s1;
            default:
                return opcode.name() + " ";
        }
    }

    public byte reverseByte(byte x) {
        int intSize = 8;
        byte y = 0;
        for (int position = intSize - 1; position > 0; position--) {
            y += ((x & 1) << position);
            x >>= 1;
        }
        return y;
    }
}
