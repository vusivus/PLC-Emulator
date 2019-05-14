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
public enum Opcode {
    ADDWF((short) 6, (short) 0x24),
    ADDWFC((short) 6, (short) 0x20),
    ANDWF((short) 6, (short) 0x14),
    CLRF((short) 7, (short) 0x6A),
    COMF((short) 6, (short) 0x1C),
    CPFSEQ((short) 7, (short) 0x62),
    CPFSGT((short) 7, (short) 0x64),
    CPFSLT((short) 7, (short) 0x60),
    DECF((short) 6, (short) 0x04),
    DECFSZ((short) 6, (short) 0x2C),
    DCFSNZ((short) 6, (short) 0x4C),
    INCF((short) 6, (short) 0x28),
    INCFSZ((short) 6, (short) 0x3C),
    INFSNZ((short) 6, (short) 0x48),
    IORWF((short) 6, (short) 0x10),
    MOVF((short) 6, (short) 0x50),
    MOVFF((short) 8, (short) 0xCF),
    MOVWF((short) 7, (short) 0x6E),
    MULWF((short) 7, (short) 0x02),
    NEGF((short) 7, (short) 0x6C),
    RLCF((short) 6, (short) 0x34),
    RLNCF((short) 6, (short) 0x44),
    RRCF((short) 6, (short) 0x30),
    RRNCF((short) 6, (short) 0x40),
    SETF((short) 7, (short) 0x68),
    SUBFWB((short) 6, (short) 0x54),
    SUBWF((short) 6, (short) 0x5C),
    SUBWFB((short) 6, (short) 0x58),
    SWAPF((short) 6, (short) 0x38),
    TSTFSZ((short) 6, (short) 0x66),
    XORWF((short) 6, (short) 0x18),
    
    BCF((short) 4, (short) 0x90),
    BSF((short) 4, (short) 0x80),
    BTFSC((short) 4, (short) 0xB0),
    BTFSS((short) 4, (short) 0xA0),
    BTG((short) 4, (short) 0x70),
    
    CALL((short) 11, (short) 0xECF),
    BRA((short) 5, (short) 0xD0),
    GOTO((short) 12, (short) 0xEFF),
    NOP((short) 16, (short) 0x0),
    RETURN((short) 15, (short) 0x12),
    
    ADDLW((short) 8, (short) 0x0F),
    ANDLW((short) 8, (short) 0x0B),
    IORLW((short) 8, (short) 0x09),
    LFSR((short) 10, (short) 0x0EE0),
    MOVLB((short) 12, (short) 0x010),
    MOVLW((short) 8, (short) 0x0E),
    MULLW((short) 8, (short) 0xD0),
    SUBLW((short) 8, (short) 0x08),
    XORLW((short) 8, (short) 0x0A);

    private final short bits;
    private final short hexCode;

    private Opcode(short bits, short hexCode) {
        this.bits = bits;
        this.hexCode = hexCode;
    }

    public short getBits() {
        return bits;
    }

    public short getHexCode() {
        return hexCode;
    }

}
