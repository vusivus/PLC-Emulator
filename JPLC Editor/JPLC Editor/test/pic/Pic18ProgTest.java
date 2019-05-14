/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic;

import com.vts.jplceditor.compiler.pic18.ByteInstruction;
import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;
import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import com.vts.jplceditor.compiler.pic18.Pic18Prog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Electronix
 */
public class Pic18ProgTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<Pic18Instruction> asm = new ArrayList<>();
        asm.add(new ByteInstruction(Opcode.ANDWF,0x23,false,false));
        asm.add(new ByteInstruction(Opcode.CLRF,0x33,false,false));
        asm.add(new LiteralInstruction(Opcode.ADDLW,0x22));
        
        for(int i=0;i<asm.size();i++){
            asm.get(i).Assemble();
        }
        
        Pic18Prog program = new Pic18Prog(0x2000,asm);
        System.out.println(program.getProgramMap());
    }
    
}
