/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.ld;

import com.vts.jplceditor.compiler.pic18.Pic18Compiler;
import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import com.vts.jplceditor.compiler.pic18.Pic18Prog;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.ld.LadderDiagram;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class LDCompiler {

    private LadderDiagram ld;
    private final LDVisitor visitor;
    private List<PLCOperand> tags;
    private final Pic18Compiler picCompiler;

    public LDCompiler() {
        visitor = new LDVisitor();
        picCompiler = Pic18Compiler.forPic18f2550();
    }

    public boolean compile(LadderDiagram ladderDiagram, List<PLCOperand> tagList) {
        boolean success;
        ld = ladderDiagram;
        tags = tagList;
        if(!visitor.visitLD(ld)){
            success=false;
        }else{
          picCompiler.compileInstructionList(visitor.getRungList(), tagList);  
          success=true;
        } 
        return success;
    }

    public String getLDStatement(){
       return visitor.generateResult();
    }
    
    public String getPicAsm(){
        return picCompiler.getAsmOutput().toString();
    }
    
    public StringBuffer getPicProgramMap(){
        return picCompiler.getPicProgram().getProgramMap();
    }
    
    public String getPicHex(){
        return picCompiler.getHexOutput().toString();
    }
    public Pic18Prog getPicProgram(){
        return picCompiler.getPicProgram();
    }
}
