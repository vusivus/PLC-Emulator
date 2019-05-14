/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.ld;

import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.ld.component.LDInstruction;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class LDStatement {
    StringBuffer instructions;

    public void generateStatement(List<LDInstruction> ldList){
        instructions = new StringBuffer();
        String line;
        for(int i=0;i<ldList.size();i++){
            instructions.append(generateInstruction(ldList.get(i))).append("\n");
        }
    }
    
    private String generateInstruction(LDInstruction instruction){
        String instr = instruction.getInstruction().getPLCInstruction().getOperator().name();
        instr+=": ";
        for (PLCOperand parameter : instruction.getInstruction().getPLCInstruction().getOperands()) {
           if(parameter.getDataType()==PLCDataType.LITERAL){
               instr+=parameter.getIdentity()+", ";
           }else{
             instr+=parameter.getIdentity()+"("+parameter.getAddress().toString()+")"+", ";  
           }           
        }
        return instr;
    }
    
    public StringBuffer getStatement(){
        return instructions;
    }
    
    @Override
    public String toString(){
        return instructions.toString();
    }
}
