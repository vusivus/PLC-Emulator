/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc.instruction;

import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import java.util.List;

/**
 *
 * @author Vusman
 */
public interface PLCInstr{
    public List<Pic18Instruction> getPicAsm();
    public int getwordCount();
}
