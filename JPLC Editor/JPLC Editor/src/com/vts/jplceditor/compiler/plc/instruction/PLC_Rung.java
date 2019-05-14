/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc.instruction;

import com.vts.jplceditor.compiler.plc.PLCInstruction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusman
 */
public class PLC_Rung {

    private List<PLCInstruction> instructions;

    public PLC_Rung() {
        instructions = new ArrayList<>();
    }

    public void addInstruction(PLCInstruction instruction) {
        instructions.add(instruction);
    }

    public List<PLCInstruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<PLCInstruction> instructions) {
        this.instructions = instructions;
    }

}
