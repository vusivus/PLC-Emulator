/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic;

import com.vts.jplceditor.compiler.pic18.ControlInstruction;
import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;

/**
 *
 * @author Vusivus
 */
public class ControlInstrTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Opcode op = Opcode.RETURN;
        ControlInstruction instr = new ControlInstruction(op,false);
        instr.Assemble();
        System.out.println(instr.toString());
        System.out.println(String.format("0x%04X", instr.get16BitCode()));
        System.out.println(String.format("0x%04X", instr.getSecond16BitCode())); 
    }
    
}
