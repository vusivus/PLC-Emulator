/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.ld;

import com.vts.jplceditor.compiler.plc.instruction.PLC_Rung;
import com.vts.jplceditor.ld.LadderDiagram;
import com.vts.jplceditor.ld.component.LDInstruction;
import com.vts.jplceditor.ld.component.LDRung;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class LDVisitor {
    
    private final LDRungVisitor rungV;
    private final List<PLC_Rung> rungList;
    
    public LDVisitor() {
        rungV = new LDRungVisitor();
        rungList = new ArrayList<>();
    }
    
    public boolean visitLD(LadderDiagram ld) {
        int count = ld.getRungList().size();
        for (int i = 0; i < count; i++) {
            LDRung r = ld.getRungList().get(i);
            r.cleanUp();
            PLC_Rung rung = new PLC_Rung();
            if(!rungV.visitRung(r)){
              return false;  
            }
            List<LDInstruction> iList = r.getInstructionList();
            for(LDInstruction inst : iList){
                if(inst.hasInstruction()){
                   rung.addInstruction(inst.getInstruction().getPLCInstruction()); 
                }              
            }
            rungList.add(rung);
        }
        return true;
    }
    
    public String generateResult() {
        rungV.generateStatement();
        return rungV.getStatement().toString();
    }
    
    public List<PLC_Rung> getRungList() {
        return rungList;
    }
    
}
