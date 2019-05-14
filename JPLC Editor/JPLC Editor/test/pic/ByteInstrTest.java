/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic;

import com.vts.jplceditor.compiler.pic18.ByteInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;

/**
 *
 * @author Vusivus
 */
public class ByteInstrTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Opcode op = Opcode.INCF;
        ByteInstruction instr = new ByteInstruction(op,0x6662,true,true);
        instr.setFile2(0x112);
        instr.Assemble();
        System.out.println(instr.toString());
        System.out.println(String.format("0x%04X", instr.get16BitCode()));
    }
    
}
