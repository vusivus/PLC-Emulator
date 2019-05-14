/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic;

import com.vts.jplceditor.compiler.pic18.BitInstruction;
import com.vts.jplceditor.compiler.pic18.ByteInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;

/**
 *
 * @author Vusivus
 */
public class BitInstrTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Opcode op = Opcode.BSF;
        BitInstruction instr = new BitInstruction(op,0x6662,(byte)6,false);
        instr.Assemble();
        System.out.println(instr.toString());
    }
    
}
