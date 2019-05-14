/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18;

import com.vts.jplceditor.compiler.pic18.hex.HexWriter;
import com.vts.jplceditor.compiler.pic18.mem.MemoryRange;
import com.vts.jplceditor.compiler.pic18.mem.Pic18f2550Ram;
import com.vts.jplceditor.compiler.plc.PLCInstruction;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.compiler.plc.instruction.PLCInstr;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Add;
import com.vts.jplceditor.compiler.plc.instruction.PLC_And;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Equ;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Grt;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Les;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Mov;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Mvm;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Or;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Ote;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Otl;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Otu;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Rung;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Sub;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Xic;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Xio;
import com.vts.jplceditor.compiler.plc.instruction.PLC_Xor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class Pic18Compiler extends Pic18 {

    private List<PLCInstr> plcInstructions;
    private List<Pic18Instruction> picInstructions;
    private int org = (int) Pic18Mem.PROGRAM_ORG;  // where user program starts
    private StringBuffer asmOutput, hexOutput;
    private Pic18Prog picProgram;
    private int address = org;
    private String adr;

    public Pic18Compiler(List<MemoryRange> gpRegisters, List<MemoryRange> sfRegisters) {
        super(gpRegisters, sfRegisters);
    }

    public static Pic18Compiler forPic18f2550() {
        Pic18f2550Ram ram = Pic18f2550Ram.create();
        return new Pic18Compiler(ram.getGpRegisters(), ram.getSfRegisters());
    }

    public void compileInstructionList(List<PLC_Rung> list, List<PLCOperand> tagList) {
        plcInstructions = new ArrayList<>();
        picInstructions = new ArrayList<>();
        verifyTypes(list, tagList);   //assign data types to instruction operands
        assignAddresses(list, tagList);  //assign addresses to operands and tags
        //PLC_Rung
        compilePLC(list);    //generate a PLC instruction list
        compilePICAsm();     //generate a Pic instruction list
        compilePICHex();     // generate hex code
    }

    private void compilePICAsm() {
        asmOutput = new StringBuffer();
        asmOutput.append("org 0x2000\n");
        plcInstructions.stream().forEach((plcI) -> {
            plcI.getPicAsm().stream().map((picI) -> {
                picI.Assemble();
                adr = String.format("0x%04X", address);
                asmOutput.append(adr + " : " + picI.toString() + "\n");
                address += (picI.getWordCount()) * 2;
                return picI;
            }).forEach((picI) -> {
                picInstructions.add(picI);
            });
        });
        ControlInstruction ret = new ControlInstruction(Opcode.RETURN, false);
        ret.Assemble();
        picInstructions.add(ret);
        asmOutput.append("RET");
    }

    private void compilePICHex() {
        picProgram = new Pic18Prog((int) Pic18Mem.PROGRAM_ORG, picInstructions);
        HexWriter writer = new HexWriter(picProgram);
        writer.writeOS();
        writer.writeCode();
        writer.writeConfig();
        hexOutput = writer.getHexContent();
    }

    private void compilePLC(List<PLC_Rung> rungs) {
        rungs.stream().forEach((rung) -> { //Add each instruction to the asm instruction list
            int size = rung.getInstructions().size(); //number of instructions in rung
            PLCInstruction crrI;
            for (int i = 0; i < size; i++) {
                PLCOperand prevENO;
                crrI = rung.getInstructions().get(i);
                if (i != 0) {
                    prevENO = rung.getInstructions().get(i - 1).getOperands().get(1);
                } else {
                    prevENO = null;
                }
                addToAsmList(prevENO, crrI);
            }
        }
        );
    }

    public void addToAsmList(PLCOperand prevENO, PLCInstruction crrI) {
        switch (crrI.getOperator()) {
            case EQU:
                PLC_Equ equ = new PLC_Equ(org, crrI.getOperands(), prevENO);
                plcInstructions.add(equ);
                org += equ.getwordCount() * 2;
                break;
            case LES:
                PLC_Les les = new PLC_Les(org, crrI.getOperands(), prevENO);
                plcInstructions.add(les);
                org += les.getwordCount() * 2;
                break;
            case GRT:
                PLC_Grt grt = new PLC_Grt(org, crrI.getOperands(), prevENO);
                plcInstructions.add(grt);
                org += grt.getwordCount() * 2;
                break;
            case ADD:
                PLC_Add add = new PLC_Add(org, crrI.getOperands(), prevENO);
                plcInstructions.add(add);
                org += add.getwordCount() * 2;
                break;
            case SUB:
                PLC_Sub sub = new PLC_Sub(org, crrI.getOperands(), prevENO);
                plcInstructions.add(sub);
                org += sub.getwordCount() * 2;
                break;
            case XIC:
                PLC_Xic xic = new PLC_Xic(org, crrI.getOperands(), prevENO);
                plcInstructions.add(xic);
                org += xic.getwordCount() * 2;
                break;
            case XIO:
                PLC_Xio xio = new PLC_Xio(org, crrI.getOperands(), prevENO);
                plcInstructions.add(xio);
                org += xio.getwordCount() * 2;
                break;
            case OTE:
                PLC_Ote ote = new PLC_Ote(org, crrI.getOperands(), prevENO);
                plcInstructions.add(ote);
                org += ote.getwordCount() * 2;
                break;
            case OTL:
                PLC_Otl otl = new PLC_Otl(org, crrI.getOperands(), prevENO);
                plcInstructions.add(otl);
                org += otl.getwordCount() * 2;
                break;
            case OTU:
                PLC_Otu otu = new PLC_Otu(org, crrI.getOperands(), prevENO);
                plcInstructions.add(otu);
                org += otu.getwordCount() * 2;
                break;
            case AND:
                PLC_And and = new PLC_And(org, crrI.getOperands(), prevENO);
                plcInstructions.add(and);
                org += and.getwordCount() * 2;
                break;
            case OR:
                PLC_Or or = new PLC_Or(org, crrI.getOperands(), prevENO);
                plcInstructions.add(or);
                org += or.getwordCount() * 2;
                break;
            case XOR:
                PLC_Xor xor = new PLC_Xor(org, crrI.getOperands(), prevENO);
                plcInstructions.add(xor);
                org += xor.getwordCount() * 2;
                break;
            case MOV:
                PLC_Mov mov = new PLC_Mov(org, crrI.getOperands(), prevENO);
                plcInstructions.add(mov);
                org += mov.getwordCount() * 2;
                break;
            case MVM:
                PLC_Mvm mvm = new PLC_Mvm(org, crrI.getOperands(), prevENO);
                plcInstructions.add(mvm);
                org += mvm.getwordCount() * 2;
                break;
        }
    }

    private void verifyTypes(List<PLC_Rung> list, List<PLCOperand> tagList) {
        list.stream().forEach((rung) -> {
            rung.getInstructions().stream().forEach((inst) -> {
                for (PLCOperand op : inst.getOperands()) {
                    for (PLCOperand tag : tagList) {
                        if (op.getIdentity().equals(tag.getIdentity())) {
                            op.setDataType(tag.getDataType());
                            break;
                        }
                    }
                }
            });
        });
    }

    private void assignAddresses(List<PLC_Rung> list, List<PLCOperand> tagList) {
        int ramAddress = 0x100;  //available RAM starts from 0x75 on PLC to 0x3FF
        byte bitNo = 0;
        int nextAdr;
        PicAddress adr;
        //First assign addresses to the user tags
        for (PLCOperand op : tagList) {
            if (null != op.getDataType()) {
                switch (op.getDataType()) {
                    case BIT:
                        adr = new PicAddress(ramAddress, bitNo);
                        op.setAddress(adr);
                        ++bitNo;
                        if (bitNo > 7) {
                            bitNo = 0;
                            ++ramAddress;
                        }
                        break;
                    case BYTE:
                        ++ramAddress;
                        adr = new PicAddress(ramAddress);
                        op.setAddress(adr);
                        break;
                    case INT:
                        ++ramAddress;
                        adr = new PicAddress(ramAddress);
                        op.setAddress(adr);
                        ramAddress += 2;
                        break;
                    case FLOAT:
                        ++ramAddress;
                        adr = new PicAddress(ramAddress);
                        op.setAddress(adr);
                        ramAddress += 4;
                        break;
                    default:
                        break;
                }
            }
        }
        //Next assign addresses to PLC Instructions
        ++ramAddress;
        for (PLC_Rung rung : list) {
            for (PLCInstruction i : rung.getInstructions()) {
                //First set addresses on any instruction using tags
                for (PLCOperand tagOp : tagList) { //Tag list
                    for (PLCOperand op : i.getOperands()) { //get the operands of each instruction
                        //if the operand name is equal to the tag name, give it the same address.
                        if (op.getIdentity().equals(tagOp.getIdentity())) {
                            op.setAddress(tagOp.getAddress());
                        }
                    }
                }
                //next assign addresses to the EN and ENO for each instruction
                i.getOperands().get(0).setAddress(new PicAddress(ramAddress, (byte) 0)); //EN bit
                i.getOperands().get(1).setAddress(new PicAddress(ramAddress, (byte) 1)); //ENO bit
                //Next assign addresses to any I/O operands if any
                i.getOperands().stream().filter((op) -> (isIO(op.getIdentity()))).forEach((op) -> {
                    setIOAdr(op);  //if I/O operand found, assign its default address
                });
                //next assign the addresses to the timer instructions if they exist
                if (i.getOperator() == PLCOperator.CTD || i.getOperator() == PLCOperator.CTU
                        || i.getOperator() == PLCOperator.TON) {
                    //assign addresses to the bit flags
                    i.getOperands().get(2).setAddress(new PicAddress(ramAddress, (byte) 2));
                    i.getOperands().get(3).setAddress(new PicAddress(ramAddress, (byte) 3));

                    ++ramAddress;
                    i.getOperands().get(4).setAddress(new PicAddress(ramAddress));
                    ++ramAddress;
                    i.getOperands().get(5).setAddress(new PicAddress(ramAddress));
                    ramAddress += 2;
                    i.getOperands().get(6).setAddress(new PicAddress(ramAddress));
                }
                ++ramAddress;
            }
        }
    }

    public StringBuffer getAsmOutput() {
        return asmOutput;
    }

    public StringBuffer getHexOutput() {
        return hexOutput;
    }

    private boolean isIO(String identity) {
        switch (identity) {
            case "DI0":
                return true;
            case "DI1":
                return true;
            case "DI2":
                return true;
            case "DI3":
                return true;
            case "DI4":
                return true;
            case "DI5":
                return true;
            case "DI6":
                return true;
            case "DI7":
                return true;
            case "DO0":
                return true;
            case "DO1":
                return true;
            case "DO2":
                return true;
            case "DO3":
                return true;
            case "DO4":
                return true;
            case "DO5":
                return true;
            case "DO6":
                return true;
            case "DO7":
                return true;
            case "AI0":
                return true;
            case "AI1":
                return true;
            case "AI2":
                return true;
            case "AI3":
                return true;
            case "AI4":
                return true;
            case "AI5":
                return true;
            case "AI6":
                return true;
            case "AI7":
                return true;
            case "AO0":
                return true;
            default:
                return false;
        }
    }

    private void setIOAdr(PLCOperand op) {
        String identity = op.getIdentity();
        switch (identity) {
            case "DI0":
                op.setAddress(new PicAddress(0x70, (byte) 0));
                break;
            case "DI1":
                op.setAddress(new PicAddress(0x70, (byte) 1));
                break;
            case "DI2":
                op.setAddress(new PicAddress(0x70, (byte) 2));
                break;
            case "DI3":
                op.setAddress(new PicAddress(0x70, (byte) 3));
                break;
            case "DI4":
                op.setAddress(new PicAddress(0x70, (byte) 4));
                break;
            case "DI5":
                op.setAddress(new PicAddress(0x70, (byte) 5));
                break;
            case "DI6":
                op.setAddress(new PicAddress(0x70, (byte) 6));
                break;
            case "DI7":
                op.setAddress(new PicAddress(0x70, (byte) 7));
                break;
            case "DO0":
                op.setAddress(new PicAddress(0x73, (byte) 0));
                break;
            case "DO1":
                op.setAddress(new PicAddress(0x73, (byte) 1));
                break;
            case "DO2":
                op.setAddress(new PicAddress(0x73, (byte) 2));
                break;
            case "DO3":
                op.setAddress(new PicAddress(0x73, (byte) 3));
                break;
            case "DO4":
                op.setAddress(new PicAddress(0x73, (byte) 4));
                break;
            case "DO5":
                op.setAddress(new PicAddress(0x73, (byte) 5));
                break;
            case "DO6":
                op.setAddress(new PicAddress(0x73, (byte) 6));
                break;
            case "DO7":
                op.setAddress(new PicAddress(0x73, (byte) 7));
                break;
            case "AI0":
                op.setAddress(new PicAddress(0x60));
                break;
            case "AI1":
                op.setAddress(new PicAddress(0x62));
                break;
            case "AI2":
                op.setAddress(new PicAddress(0x64));
                break;
            case "AI3":
                op.setAddress(new PicAddress(0x66));
                break;
            case "AI4":
                op.setAddress(new PicAddress(0x68));
                break;
            case "AI5":
                op.setAddress(new PicAddress(0x6A));
                break;
            case "AI6":
                op.setAddress(new PicAddress(0x6C));
                break;
            case "AI7":
                op.setAddress(new PicAddress(0x6E));
                break;
            case "AO0":
                op.setAddress(new PicAddress(0x71));
                break;
        }
    }

    public Pic18Prog getPicProgram() {
        return picProgram;
    }

}
