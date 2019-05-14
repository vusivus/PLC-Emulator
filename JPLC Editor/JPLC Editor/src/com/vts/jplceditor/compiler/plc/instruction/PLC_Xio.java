/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc.instruction;

import com.vts.jplceditor.compiler.pic18.BitInstruction;
import com.vts.jplceditor.compiler.pic18.ControlInstruction;
import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;
import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import com.vts.jplceditor.compiler.pic18.Pic18Mem;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusman
 */
public class PLC_Xio extends Pic18Mem implements PLCInstr {

    private final List<Pic18Instruction> asm;
    private final int bitAdress;
    private int org;
    private final byte EN = 0;
    private final byte ENO = 1;
    private int wordCount;
    private int count;
    private final PLCOperand prevENO;

    public PLC_Xio(int org, List<PLCOperand> parameters, PLCOperand prevENO) {

        this.bitAdress = parameters.get(0).getAddress().getFileAddr();
        asm = new ArrayList<>();
        this.org = org;
        this.prevENO = prevENO;
        PLCOperand in = parameters.get(2);
        checkENO();
        xio(in);
    }

    private void checkENO() {
        int code_start, clearENO;

        //First set the EN depending on the prev ENO
        if (prevENO == null) { //If no prev ENO, this is rung start
            banksel(bitAdress);
            asm.add(new BitInstruction(Opcode.BSF, bitAdress, EN, bsr));
            wordCount = 2;
        } else {   // Else set this EN depending on the prev ENO
            code_start = org + 18;
            clearENO = code_start + 12;  //clear_ENO in instruction code
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

    private void xio(PLCOperand in) {
        int endif;
        int clear_ENO;
        byte bit = in.getAddress().getBitNo();
        int input = in.getAddress().getFileAddr();
        //IF EN
        //ENO = IN == TRUE
        //ENDIF
        clear_ENO = org + 12;
        endif = org + 16;
        /*0*/ banksel(input);


        //if bit=TRUE set ENO
        /*2*/ asm.add(new BitInstruction(Opcode.BTFSS, input, bit, bsr));
        /*4*/ branch(org + 4, clear_ENO);
        /*6*/ banksel(bitAdress);
        /*8*/ asm.add(new BitInstruction(Opcode.BSF, bitAdress, ENO, bsr));
        /*10*/ branch(org + 10, endif);


        //clear_ENO address
        /*12*/ banksel(bitAdress);
        /*14*/ asm.add(new BitInstruction(Opcode.BCF, bitAdress, ENO, bsr));
        //endif address. Size is 20 bytes
        wordCount += 8;
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
