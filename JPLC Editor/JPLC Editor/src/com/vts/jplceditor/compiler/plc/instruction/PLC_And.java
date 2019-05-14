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
public class PLC_And extends Pic18Mem implements PLCInstr {

    private final List<Pic18Instruction> asm;
    private final int bitAdress;
    private int org;
    private final byte EN = 0;
    private final byte ENO = 1;
    private int wordCount;
    private final PLCOperand prevENO, dest;

    public PLC_And(int org, List<PLCOperand> parameters, PLCOperand prevENO) {

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
        int clrEN = org+12;
        //First set the EN depending on the prev ENO
        if (prevENO == null) { //If no prev ENO, this is rung start
            banksel(bitAdress);
            asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            wordCount = 2;
        } else {   // Else set this EN depending on the prev ENO
            code_start = org+16;
            wordCount = 8;
            int enFile = prevENO.getAddress().getFileAddr();
            /*0*/ banksel(enFile);
            /*2*/ asm.add(new BitInstruction(Opcode.BTFSS, enFile, ENO, bsr)); // If ENO set, set EN
            /*4*/ branch(org+4,clrEN);
            /*6*/ banksel(bitAdress);
            /*8*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            /*10*/ branch(org+10,code_start);

            //clrEN address
            /*12*/ banksel(bitAdress);
            /*14*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, EN, bsr));
        }
        org += (wordCount*2);
    }

    private void byteAndByte(PLCOperand in1, PLCOperand in2) {
        int input1, input2, des;
        int endif = org+10;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1&IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BTFSS, bitAdress, EN, bsr)); // If EN set EN,execute code
        /*2*/ branch(org+2,endif);
        /*4*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*6*/ banksel(input1);
        /*8*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*10*/ banksel(input2);
        /*12*/ asm.add(new ByteInstruction(Opcode.ANDWF, input2, bsr, w));
        /*14*/ banksel(des);
        /*16*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));
        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*18*/ asm.add(new LiteralInstruction(Opcode.MOVLW, 0));
            /*20*/ asm.add(new ByteInstruction(Opcode.ADDWFC, des + 1, bsr, w));
            wordCount += 2;
        }
        wordCount = 10;
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
        /*2*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*6*/ banksel(input2);
        /*8*/ asm.add(new ByteInstruction(Opcode.ANDWF, input2, bsr, w));
        /*10*/ banksel(des);
        /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));

        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*14*/ banksel(des+1);
            /*16*/ asm.add(new ByteInstruction(Opcode.CLRF, des+1, bsr, false));
            wordCount += 4;
        }
        wordCount = 11;
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
        /*4*/ asm.add(new ByteInstruction(Opcode.ANDWF, input2, bsr, w));
        /*5*/ banksel(des);
        /*6*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));

        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*7*/ banksel(input1);
            /*8*/ asm.add(new ByteInstruction(Opcode.MOVF, input1+1, bsr, w));
            /*9*/ banksel(input2);
            /*10*/ asm.add(new ByteInstruction(Opcode.ANDWF, input2 + 1, bsr, w));
            /*11*/ banksel(des);
            /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des+1, bsr, false));
            wordCount += 6;
        }
        /*17*/ asm.add(new ControlInstruction(Opcode.NOP));    //endif address
        wordCount = 17;
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
    
    private void branch(int currentAddress, int targetAddress) {
        //address = PC + 2 + 2n
        int branchAddress = targetAddress - currentAddress - 2;
        branchAddress /= 2;
        asm.add(new ControlInstruction(Opcode.BRA, branchAddress));
    }
}
