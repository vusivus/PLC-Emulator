/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc.instruction;

import com.vts.jplceditor.compiler.pic18.BitInstruction;
import com.vts.jplceditor.compiler.pic18.ByteInstruction;
import com.vts.jplceditor.compiler.pic18.ControlInstruction;
import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;
import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import com.vts.jplceditor.compiler.pic18.Pic18Mem;
import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusman
 */
public class PLC_Xor extends Pic18Mem implements PLCInstr {

    private final List<Pic18Instruction> asm;
    private final int bitAdress;
    private int org;
    private final byte EN = 0;
    private final byte ENO = 1;
    private int wordCount;
    private int count;
    private final PLCOperand prevENO, dest;

    public PLC_Xor(int org, List<PLCOperand> parameters, PLCOperand prevENO) {

        this.bitAdress = parameters.get(0).getAddress().getFileAddr();
        asm = new ArrayList<>();
        this.org = org;
        PLCOperand in1, in2;
        in1 = parameters.get(2);
        in2 = parameters.get(3);
        dest = parameters.get(4);
        this.prevENO = prevENO;

        if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.BYTE) {
            checkENO();
            byteAndByte(in1, in2);
        } else if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.INT) {
            checkENO();
            byteAndInt(in1, in2);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.BYTE) {
            checkENO();
            byteAndInt(in2, in1);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.INT) {
            checkENO();
            intAndInt(in1, in2);
        }
    }

    private void checkENO() {
        int code_start;
        int clrEN = org + 8;
        //First set the EN depending on the prev ENO
        if (prevENO == null) { //If no prev ENO, this is rung start
            banksel(bitAdress);
            asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            count = 2;
        } else {   // Else set this EN depending on the prev ENO
            code_start = 9 + org;
            count = 9;
            int enFile = prevENO.getAddress().getFileAddr();
            /*0*/ banksel(enFile);
            /*1*/ asm.add(new BitInstruction(Opcode.BTFSS, enFile, ENO, bsr)); // If ENO set, set EN
            /*2*/ asm.add(new ControlInstruction(Opcode.GOTO, clrEN));
            /*4*/ banksel(bitAdress);
            /*5*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            /*6*/ asm.add(new ControlInstruction(Opcode.GOTO, code_start));


            //clrEN address
            /*8*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, EN, bsr));
        }
        org += count;
    }

    private void byteAndByte(PLCOperand in1, PLCOperand in2) {
        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1&IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*1*/ banksel(input1);
        /*2*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*3*/ banksel(input2);
        /*4*/ asm.add(new ByteInstruction(Opcode.XORWF, input2, bsr, w));
        /*5*/ banksel(des);
        /*6*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));
        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*7*/ asm.add(new LiteralInstruction(Opcode.MOVLW, 0));
            /*8*/ asm.add(new ByteInstruction(Opcode.ADDWFC, des + 1, bsr, w));
            wordCount += 2;
        }
        /*17*/ asm.add(new ControlInstruction(Opcode.NOP));    //endif address
        wordCount = 1 + count;
    }

    private void byteAndInt(PLCOperand in1, PLCOperand in2) {

        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1&IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*1*/ banksel(input1);
        /*2*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*3*/ banksel(input2);
        /*4*/ asm.add(new ByteInstruction(Opcode.XORWF, input2, bsr, w));
        /*5*/ banksel(des);
        /*6*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));

        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*8*/ banksel(input2+1);
            /*9*/ asm.add(new ByteInstruction(Opcode.MOVF, input2 + 1, bsr, w));
            /*10*/ banksel(des+1);
            /*11*/ asm.add(new ByteInstruction(Opcode.MOVWF, des+1, bsr, false));
            wordCount += 4;
        }
        /*17*/ asm.add(new ControlInstruction(Opcode.NOP));    //endif address
        wordCount = 1 + count;
    }

    private void intAndInt(PLCOperand in1, PLCOperand in2) {
        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1&IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*1*/ banksel(input1);
        /*2*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*3*/ banksel(input2);
        /*4*/ asm.add(new ByteInstruction(Opcode.XORWF, input2, bsr, w));
        /*5*/ banksel(des);
        /*6*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));

        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*7*/ banksel(input1);
            /*8*/ asm.add(new ByteInstruction(Opcode.MOVF, input1+1, bsr, w));
            /*9*/ banksel(input2);
            /*10*/ asm.add(new ByteInstruction(Opcode.XORWF, input2 + 1, bsr, w));
            /*11*/ banksel(des);
            /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des+1, bsr, false));
            wordCount += 6;
        }
        /*17*/ asm.add(new ControlInstruction(Opcode.NOP));    //endif address
        wordCount = 1 + count;
    }

    @Override
    public List<Pic18Instruction> getPicAsm() {
        return asm;
    }

    @Override
    public int getwordCount() {
        return wordCount;
    }

    private void banksel(int file) {
        byte bank = (byte) (file >> 8);
        asm.add(new LiteralInstruction(Opcode.MOVLB, bank));
    }
}
