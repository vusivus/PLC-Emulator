/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.ld;

import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.ld.component.LDRung;
import com.vts.jplceditor.ld.component.LDInstruction;
import com.vts.jplceditor.ld.instruction.Instruction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class LDRungVisitor {

    private final List<LDInstruction> instructionList;
    private LDStatement statement;

    public LDRungVisitor() {
        instructionList = new ArrayList<>();
    }

    public boolean visitRung(LDRung rung) {
        boolean success = false;
        List<LDInstruction> list = rung.getInstructionList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hasInstruction()) {
                Instruction in = list.get(i).getInstruction();
                if(verifyParameters(in.getPLCInstruction().getOperands())==-1){
                  instructionList.add(list.get(i)); 
                  success=true;
                }else{
                  list.get(i).select();
                  success=false;
                  break;
                }              
            }
        }
        return success;
    }

    public void generateStatement() {
        statement = new LDStatement();
        statement.generateStatement(instructionList);
    }

    public LDStatement getStatement() {
        return statement;
    }

    public List<LDInstruction> getInstructionList() {
        return instructionList;
    }

    private int verifyParameters(List<PLCOperand> operands) {
       int index=-1;
        for(int i=0;i<operands.size();i++){
           if(blank(operands.get(i))){
              index=i;
              break;
           }
       }
        return index;
    }

    private boolean blank(PLCOperand op) {
       return "".equals(op.getIdentity()) || op.getIdentity()=="Tag_Name";
    }

}
