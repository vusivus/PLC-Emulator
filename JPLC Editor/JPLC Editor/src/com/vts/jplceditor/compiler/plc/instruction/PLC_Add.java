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
public class PLC_Add extends Pic18Mem implements PLCInstr {

    private final List<Pic18Instruction> asm;
    private final int bitAdress;
    private int org;
    private final byte EN = 0;
    private final byte ENO = 1;
    private int wordCount;
    private int count;
    private final PLCOperand prevENO, dest;
    private final int byteAndByteSize = 14;
    private final int byteAndIntSize = 14;
    private final int intAndIntSize = 14;

    public PLC_Add(int org, List<PLCOperand> parameters, PLCOperand prevENO) {

        this.bitAdress = parameters.get(0).getAddress().getFileAddr();
        asm = new ArrayList<>();
        this.org = org;
        PLCOperand in1, in2;
        in1 = parameters.get(2);
        in2 = parameters.get(3);
        dest = parameters.get(4);
        this.prevENO = prevENO;

        if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.BYTE) {
            if (dest.getDataType() == PLCDataType.INT) {
                checkENO(byteAndByteSize + 4);
            } else {
                checkENO(byteAndByteSize);
            }
            byteAndByte(in1, in2);
        } else if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.INT) {
            if (dest.getDataType() == PLCDataType.INT) {
                checkENO(byteAndByteSize + 10);
            } else {
                checkENO(byteAndByteSize);
            }
            byteAndInt(in1, in2);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.BYTE) {
            if (dest.getDataType() == PLCDataType.INT) {
                checkENO(byteAndByteSize + 10);
            } else {
                checkENO(byteAndByteSize);
            }
            byteAndInt(in2, in1);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.INT) {
            if (dest.getDataType() == PLCDataType.INT) {
                checkENO(byteAndByteSize + 10);
            } else {
                checkENO(byteAndByteSize);
            }
            intAndInt(in1, in2);
        }
    }

    private void checkENO(int endif) {
        int code_start, clearENO;

        //First set the EN depending on the prev ENO
        if (prevENO == null) { //If no prev ENO, this is rung start
            banksel(bitAdress);
            asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            wordCount = 2;
        } else {   // Else set this EN depending on the prev ENO
            code_start = org + 18;
            clearENO = code_start + endif;  //clear_ENO in instruction code
            int clrEN = org + 12;
            int enFile = prevENO.getAddress().getFileAddr();
            /*0*/ banksel(enFile);
            /*2*/ asm.add(new BitInstruction(Opcode.BTFSS, enFile, ENO, bsr)); // If ENO set, set EN
            /*4*/ branch(org + 4, clrEN);
            /*6*/ banksel(bitAdress);
            /*8*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            /*10*/ branch(org + 10, code_start);


            //clrEN address
            /*12*/ banksel(bitAdress);
            /*14*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, EN, bsr));
            /*16*/ branch(org + 16, clearENO);
            wordCount = 9;
        }
        org += (wordCount * 2);
    }

    private void byteAndByte(PLCOperand in1, PLCOperand in2) {
        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1+IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*2*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*6*/ banksel(input2);
        /*8*/ asm.add(new ByteInstruction(Opcode.ADDWF, input2, bsr, w));
        /*10*/ banksel(des);
        /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));
        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*14*/ asm.add(new LiteralInstruction(Opcode.MOVLW, 0));
            /*16*/ asm.add(new ByteInstruction(Opcode.ADDWFC, des + 1, bsr, w));
            wordCount += 2;
        }
    }

    private void byteAndInt(PLCOperand in1, PLCOperand in2) {

        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1+IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*2*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*6*/ banksel(input2);
        /*8*/ asm.add(new ByteInstruction(Opcode.ADDWF, input2, bsr, w));
        /*10*/ banksel(des);
        /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));

        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*14*/ banksel(WREG);
            /*16*/ asm.add(new ByteInstruction(Opcode.CLRF, WREG, bsr, false));
            /*18*/ banksel(input2);
            /*20*/ asm.add(new ByteInstruction(Opcode.ADDWFC, input2 + 1, bsr, w));
            /*22*/ asm.add(new ByteInstruction(Opcode.MOVWF, des + 1, bsr, false));
            wordCount += 5;
        }
    }

    private void intAndInt(PLCOperand in1, PLCOperand in2) {
        int input1, input2, des;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();
        des = dest.getAddress().getFileAddr();


        //IF EN
        //DEST = IN1+IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*2*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*6*/ banksel(input2);
        /*8*/ asm.add(new ByteInstruction(Opcode.ADDWF, input2, bsr, w));
        /*10*/ banksel(des);
        /*12*/ asm.add(new ByteInstruction(Opcode.MOVWF, des, bsr, false));
        wordCount = 7;
        if (dest.getDataType() == PLCDataType.INT) {
            /*14*/ banksel(input1);
            /*16*/ asm.add(new ByteInstruction(Opcode.MOVF, input1 + 1, bsr, w));
            /*18*/ banksel(input2);
            /*20*/ asm.add(new ByteInstruction(Opcode.ADDWFC, input2 + 1, bsr, w));
            /*22*/ asm.add(new ByteInstruction(Opcode.MOVWF, des + 1, bsr, false));
            wordCount += 5;
        }
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
