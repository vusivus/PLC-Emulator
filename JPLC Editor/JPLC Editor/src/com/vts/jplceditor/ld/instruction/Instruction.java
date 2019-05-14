/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction;

import com.vts.jplceditor.compiler.plc.PLCInstruction;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import java.util.List;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Vusivus
 */
public interface Instruction {
    public AnchorPane getInstance();
    public PLCInstruction getPLCInstruction();
    public void setParameters(List<PLCOperand>parameters);
}
