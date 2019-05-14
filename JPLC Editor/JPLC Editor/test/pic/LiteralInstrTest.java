/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic;

import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;

/**
 *
 * @author Vusivus
 */
public class LiteralInstrTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Opcode op = Opcode.LFSR;
        LiteralInstruction instr = new LiteralInstruction(op,(byte)1,0x66E2);
        instr.Assemble();
        System.out.println(instr.toString());
    }
    
}
