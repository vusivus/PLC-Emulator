/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

import com.vts.jplceditor.compiler.pic18.mem.MemoryRange;
import com.vts.jplceditor.compiler.pic18.mem.Pic18f2550Ram;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class Pic18 extends Pic18f2550Ram {

    public static final int W16AL = 0xB5;
    public static final int W16BL = 0xB7;
    public static final int W16CL = 0xB9;
    public static final int W16DL = 0xBB;
    public static final int W16AH = 0x8B6;
    public static final int W16BH = 0xB8;
    public static final int W16CH = 0xBA;
    public static final int W16DH = 0xBC;

    public static final int WREGA = 0xBC;
    public static final int WREGB = 0xBC;
    public static final int WREGC = 0xBC;
    public static final int WREGD = 0xBC;
    public static final int WREGE = 0xBC;

    public static final int W32_0 = 0xBD;
    public static final int W32_1 = 0xBE;
    public static final int W32_2 = 0xBF;
    public static final int W32_3 = 0xC0;
    public final int freeRamSlot = 0xC1;

    public static int CONFIG1L = 0x03;
    public static int CONFIG1H = 0x0C;
    public static int CONFIG2L = 0x36;
    public static int CONFIG2H = 0x13;
    public static int CONFIG3L = 0;
    public static int CONFIG3H = 0x85;
    public static int CONFIG4L = 0xA5;
    public static int CONFIG4H = 0;
    public static int CONFIG5L = 0x3F;
    public static int CONFIG5H = 0xC0;
    public static int CONFIG6L = 0;
    public static int CONFIG6H = 0;
    public static int CONFIG7L = 0;
    public static int CONFIG7H = 0;
    public static int DEVID2 = 0X12;
    public static int DEVID1 = 0X40;

    public static boolean w = false;
    public static boolean f = true;
    public static boolean bsr = true;
    public static boolean acc = false;

    private final HashMap<Integer, Pic18Instruction> picProgram;
    private int currentAddress, nextAddress;

    public Pic18(List<MemoryRange> gpRegisters, List<MemoryRange> sfRegisters) {
        super(gpRegisters, sfRegisters);
        picProgram = new HashMap<>();
        currentAddress = 0;
        nextAddress = 1;
    }

    public void addInstruction(Pic18Instruction instruction) {
        picProgram.put(nextAddress, instruction);
        currentAddress += instruction.getWordCount();
        nextAddress += instruction.getWordCount();
    }

    public void write16Multiply() {
        //labels.add(new Subroutine("Multiply16", nextAddress));
        addInstruction(new LiteralInstruction(Opcode.MOVLW, 0));
        addInstruction(new ByteInstruction(Opcode.MOVWF, BSR, f, acc));

        addInstruction(new ByteInstruction(Opcode.MOVF, W16AL, w, bsr));
        addInstruction(new LiteralInstruction(Opcode.MULWF, W16BL));
        addInstruction(new ByteInstruction(Opcode.MOVFF, PRODH, f, acc));
        picProgram.get(currentAddress).setFile2(W32_1);
        addInstruction(new ByteInstruction(Opcode.MOVFF, PRODL, f, acc));
        picProgram.get(currentAddress).setFile2(W32_0);

        addInstruction(new ByteInstruction(Opcode.MOVF, W16AH, w, bsr));
        addInstruction(new LiteralInstruction(Opcode.MULWF, W16BH));
        addInstruction(new ByteInstruction(Opcode.MOVFF, PRODH, f, acc));
        picProgram.get(currentAddress).setFile2(W32_3);
        addInstruction(new ByteInstruction(Opcode.MOVFF, PRODL, f, acc));
        picProgram.get(currentAddress).setFile2(W32_2);

        addInstruction(new ByteInstruction(Opcode.MOVF, W16AL, w, bsr));
        addInstruction(new LiteralInstruction(Opcode.MULWF, W16BH));
        addInstruction(new ByteInstruction(Opcode.MOVF, PRODL, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWF, W32_1, f, bsr));
        addInstruction(new ByteInstruction(Opcode.MOVF, PRODH, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWFC, W32_2, f, bsr));
        addInstruction(new ByteInstruction(Opcode.CLRF, WREG, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWFC, W32_3, f, bsr));

        addInstruction(new ByteInstruction(Opcode.MOVF, W16AH, w, bsr));
        addInstruction(new LiteralInstruction(Opcode.MULWF, W16BL));
        addInstruction(new ByteInstruction(Opcode.MOVF, PRODL, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWF, W32_1, f, bsr));
        addInstruction(new ByteInstruction(Opcode.MOVF, PRODH, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWFC, W32_2, f, bsr));
        addInstruction(new ByteInstruction(Opcode.CLRF, WREG, w, acc));
        addInstruction(new ByteInstruction(Opcode.ADDWFC, W32_3, f, bsr));
        addInstruction(new ControlInstruction(Opcode.RETURN, true));
    }

    public void write16Divide() {
        //labels.add(new Subroutine("Divide16", nextAddress));

        int dloop;
        int nochk;
        int nogo;

        addInstruction(new LiteralInstruction(Opcode.MOVLW, 0));
        addInstruction(new ByteInstruction(Opcode.MOVWF, BSR, f, acc));
//First move the 16 bit register b to register d        
        addInstruction(new BitInstruction(Opcode.BCF, INTCON, (byte) 7, acc));
        addInstruction(new ByteInstruction(Opcode.MOVFF, W16BH, false, acc));
        picProgram.get(currentAddress).setFile2(W16DH);
        addInstruction(new ByteInstruction(Opcode.MOVFF, W16BL, false, acc));
        picProgram.get(currentAddress).setFile2(W16DL);
        addInstruction(new BitInstruction(Opcode.BSF, INTCON, (byte) 7, acc));
// Clear register b and c
        addInstruction(new ByteInstruction(Opcode.CLRF, W16BH, w, acc));
        addInstruction(new ByteInstruction(Opcode.CLRF, W16BL, w, acc));
        addInstruction(new ByteInstruction(Opcode.CLRF, W16CH, w, acc));
        addInstruction(new ByteInstruction(Opcode.CLRF, W16CL, w, acc));
        //Set up a loop counter
        addInstruction(new LiteralInstruction(Opcode.MOVLW, 0xF));
        addInstruction(new ByteInstruction(Opcode.MOVWF, WREGA, f, bsr));
        dloop = nextAddress;
        addInstruction(new BitInstruction(Opcode.BCF, STATUS, (byte) 0, acc));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16DL, f, bsr));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16DH, f, bsr));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16CL, f, bsr));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16CH, f, bsr));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16DL, f, bsr));
        addInstruction(new ByteInstruction(Opcode.MOVF, W16AH, w, bsr));
        addInstruction(new ByteInstruction(Opcode.SUBWF, W16CH, w, bsr));
        addInstruction(new BitInstruction(Opcode.BTFSS, STATUS, (byte) 2, acc));
        nochk = dloop + 0xB;
        nogo = 0xA + nochk;
        addInstruction(new ControlInstruction(Opcode.GOTO, nochk));
        addInstruction(new ByteInstruction(Opcode.MOVF, W16AL, w, bsr));
        addInstruction(new ByteInstruction(Opcode.SUBWF, W16CL, w, bsr));
        //nochk subroutine
        addInstruction(new BitInstruction(Opcode.BTFSS, STATUS, (byte) 0, acc));
        addInstruction(new ControlInstruction(Opcode.GOTO, nogo));
        addInstruction(new ByteInstruction(Opcode.MOVF, W16AL, w, bsr));
        addInstruction(new ByteInstruction(Opcode.SUBWF, W16CL, f, bsr));
        addInstruction(new BitInstruction(Opcode.BTFSS, STATUS, (byte) 0, acc));
        addInstruction(new ByteInstruction(Opcode.DECF, W16AH, f, bsr));
        addInstruction(new ByteInstruction(Opcode.MOVF, W16AH, w, bsr));
        addInstruction(new ByteInstruction(Opcode.SUBWF, W16CH, f, bsr));
        addInstruction(new ByteInstruction(Opcode.SUBWF, W16CH, f, bsr));
        addInstruction(new BitInstruction(Opcode.BSF, STATUS, (byte) 0, acc));
        //nogo subroutine
        addInstruction(new ByteInstruction(Opcode.RLCF, W16BL, f, bsr));
        addInstruction(new ByteInstruction(Opcode.RLCF, W16BH, f, bsr));
        addInstruction(new BitInstruction(Opcode.DECFSZ, WREGA, (byte) 0, acc));
        addInstruction(new ControlInstruction(Opcode.GOTO, dloop));
        addInstruction(new ControlInstruction(Opcode.RETURN, true));
    }

    public void write16Add(){
        
    }
    
    public void write16Subtract(){
        
    }
    
    public void writeFloatAdd(){
        
    }
    
    public void writeFloatSubtract(){
        
    }
    
    public void writeFloatMultiply(){
        
    }
    
    public void writeFloatDivide(){
        
    }
    
    public void write16Compare(){
        
    }
    
    public void writeFloatCompare(){
        
    }
}
