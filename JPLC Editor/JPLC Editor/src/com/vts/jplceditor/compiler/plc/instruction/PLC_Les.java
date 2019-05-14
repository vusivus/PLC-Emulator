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
public class PLC_Les extends Pic18Mem implements PLCInstr {

    private final List<Pic18Instruction> asm;
    private final int bitAdress;
    private int org;
    private final byte EN = 0;
    private final byte ENO = 1;
    private int wordCount;
    private int count;
    private final PLCOperand prevENO;

    public PLC_Les(int org, List<PLCOperand> parameters, PLCOperand prevENO) {

        this.bitAdress = parameters.get(0).getAddress().getFileAddr();
        asm = new ArrayList<>();
        this.org = org;
        PLCOperand in1, in2;
        in1 = parameters.get(2);
        in2 = parameters.get(3);
        this.prevENO = prevENO;

        if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.BYTE) {
            checkENO();
            byteToByte(in1, in2);
        } else if (in1.getDataType() == PLCDataType.BYTE && in2.getDataType() == PLCDataType.INT) {
            checkENO();
            byteToInt(in1, in2);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.BYTE) {
            checkENO();
            intToByte(in1, in2);
        } else if (in1.getDataType() == PLCDataType.INT && in2.getDataType() == PLCDataType.INT) {
            checkENO();
            intToInt(in1, in2);
        } 
    }

    private void checkENO() {
        int code_start;
        int clrEN = (org + 8)*2;
        //First set the EN depending on the prev ENO
        if (prevENO == null) { //If no prev ENO, this is rung start
            banksel(bitAdress);
            asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            count = 2;
        } else {   // Else set this EN depending on the prev ENO
            code_start = (9 + org)*2;
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
        org += (count*2);
    }
 
    private void byteToByte(PLCOperand in1, PLCOperand in2) {
        int endif = (org + 17)*2;
        int not_less = (org + 15)*2;
        int input1, input2;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();

        //IF EN
        //ENO = IN1<IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BTFSS, bitAdress, EN, bsr));
        /*1*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));      // Goto ENDIF if EN=0
        /*3*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1, bsr, w));
        /*5*/ banksel(input2);
        /*6*/ asm.add(new ByteInstruction(Opcode.SUBWF, input2, bsr, w));
        /*7*/ banksel(STATUS);
        /*8*/ asm.add(new BitInstruction(Opcode.BTFSS, STATUS, C, bsr));     //if in2<in1 C=1;
        /*9*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));
        /*11*/ banksel(bitAdress);
        /*12*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*13*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));

        //not_less address
        /*15*/ banksel(bitAdress);
        /*16*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, ENO, bsr));
        /*17*/ asm.add(new ControlInstruction(Opcode.NOP));    //endif address
        wordCount = (18+count);
    }

    private void byteToInt(PLCOperand in1, PLCOperand in2) {

        int less = (org + 18)*2;
        int not_less = (org + 22)*2;
        int endif = (not_less + 2)*2;
        int input1, input2;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();

        //IF EN
        //ENO = IN1<IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BTFSS, bitAdress, EN, bsr));
        /*1*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));          // Goto ENDIF if EN=0 
        /*3*/ banksel(input2);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input2 + 1, bsr, w)); //check if in2H>0
        /*5*/ asm.add(new LiteralInstruction(Opcode.SUBLW, 0));
        /*6*/ banksel(STATUS);
        /*7*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, bsr));    //Check if 0-in2H produces negative                
        /*8*/ asm.add(new ControlInstruction(Opcode.GOTO, less));


        //now check lower byte of int with than of byte
        /*10*/ banksel(input2);
        /*11*/ asm.add(new ByteInstruction(Opcode.MOVF, input2, bsr, w));
        /*12*/ banksel(input1);
        /*13*/ asm.add(new ByteInstruction(Opcode.SUBWF, input1, bsr, w));  //in1-in2L
        /*14*/ banksel(STATUS);
        /*15*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, bsr)); //if negative,in1 less than in2
        /*16*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));


        //here is the less address
        /*18*/ banksel(bitAdress);
        /*19*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));//if in1<in2 set ENO
        /*20*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));


        //here is the not_less address
        /*22*/ banksel(bitAdress);
        /*23*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, ENO, bsr));//if greater than zero set ENO
        /*24*/ asm.add(new ControlInstruction(Opcode.NOP));   //endif address
        wordCount = (25+count);
    }

    private void intToByte(PLCOperand in1, PLCOperand in2) {
       int less = (org + 18)*2;
        int not_less = (org + 22)*2;
        int endif = (not_less + 2)*2;
        int input1, input2;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();

        //IF EN
        //ENO = IN1<IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BTFSS, bitAdress, EN, bsr));
        /*1*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));          // Goto ENDIF if EN=0
        /*3*/ banksel(input1);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input1 + 1, bsr, w)); //check if in1H>0
        /*5*/ asm.add(new LiteralInstruction(Opcode.SUBLW, 0));
        /*6*/ banksel(STATUS);
        /*7*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, bsr));    //Check if 0-in1H produces negative                
        /*8*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));


        //now check lower byte of int with than of byte
        /*10*/ banksel(input2);
        /*11*/ asm.add(new ByteInstruction(Opcode.MOVF, input2, bsr, w));
        /*12*/ banksel(input1);
        /*13*/ asm.add(new ByteInstruction(Opcode.SUBWF, input1, bsr, w));  //in1-in2L
        /*14*/ banksel(STATUS);
        /*15*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, bsr));    //if negative, not less
        /*16*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));


        //here is the less address
        /*18*/ banksel(bitAdress);
        /*19*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));//if in1<in2 set ENO
        /*20*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));


        //here is the not_less address
        /*22*/ banksel(bitAdress);
        /*23*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, ENO, bsr));//if greater than zero set ENO
        /*24*/ asm.add(new ControlInstruction(Opcode.NOP));   //endif address
        wordCount = (25+count);
    }

    private void intToInt(PLCOperand in1, PLCOperand in2) {
        int less = (org + 19)*2;
        int not_less = (org + 23)*2;
        int endif = (not_less + 2)*2;
        int input1, input2;
        input1 = in1.getAddress().getFileAddr();
        input2 = in2.getAddress().getFileAddr();

        //IF EN
        //ENO = IN1<IN2
        //ENDIF
        /*0*/ asm.add(new BitInstruction(Opcode.BTFSS, bitAdress, EN, a));
        /*1*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));          // Goto ENDIF if EN=0 
        /*3*/ banksel(input2);
        /*4*/ asm.add(new ByteInstruction(Opcode.MOVF, input2 + 1, a, w)); //check if in1H>in2H
        /*5*/ banksel(input1);
        /*6*/ asm.add(new ByteInstruction(Opcode.SUBWF, input1+1, a, w));  //in1H-in2H
        /*7*/ banksel(STATUS);
        /*8*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, a));    //Check if in1H-in2H produces negative                
        /*9*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));


        //now check lower bytes
        /*11*/ banksel(input2);
        /*12*/ asm.add(new ByteInstruction(Opcode.MOVF, input2, a, w));
        /*13*/ banksel(input1);
        /*14*/ asm.add(new ByteInstruction(Opcode.SUBWF, input1, a, w));  //in1L-in2L
        /*15*/ banksel(STATUS);
        /*16*/ asm.add(new BitInstruction(Opcode.BTFSC, STATUS, N, a));    //if negative, not less
        /*17*/ asm.add(new ControlInstruction(Opcode.GOTO, not_less));


        //here is the less address
        /*19*/ banksel(bitAdress);
        /*20*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, a));//if in1<in2 set ENO
        /*21*/ asm.add(new ControlInstruction(Opcode.GOTO, endif));


        //here is the not_less address
        /*23*/ banksel(bitAdress);
        /*24*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, ENO, a));//if greater than zero set ENO
        /*25*/ asm.add(new ControlInstruction(Opcode.NOP));   //endif address
        wordCount = 26+count;
    }

    @Override
    public List<Pic18Instruction> getPicAsm() {
        return asm;
    }

    @Override
    public int getwordCount() {
        return wordCount;
    }
    
    private void banksel(int file){
        byte bank = (byte) (file>>8);
        asm.add(new LiteralInstruction(Opcode.MOVLB,bank));
    }
}
