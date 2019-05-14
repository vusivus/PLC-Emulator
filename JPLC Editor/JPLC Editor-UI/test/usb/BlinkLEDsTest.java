/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import com.vts.jplceditor.comms.usb.SessionManager;
import com.vts.jplceditor.compiler.pic18.ByteInstruction;
import com.vts.jplceditor.compiler.pic18.ControlInstruction;
import com.vts.jplceditor.compiler.pic18.LiteralInstruction;
import com.vts.jplceditor.compiler.pic18.Opcode;
import com.vts.jplceditor.compiler.pic18.Pic18Instruction;
import com.vts.jplceditor.compiler.pic18.Pic18Mem;
import com.vts.jplceditor.compiler.pic18.Pic18Prog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusumuzi
 */
public class BlinkLEDsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Pic18Prog program;
        List <Pic18Instruction> asm = new ArrayList<>();
        asm.add(new LiteralInstruction(Opcode.MOVLB,0x0));
        asm.add(new LiteralInstruction(Opcode.MOVLW,0x02));
        asm.add(new ByteInstruction(Opcode.MOVWF,0x73,true,true));
        asm.add(new ControlInstruction(Opcode.RETURN,false));
        for(int i=0;i<asm.size();i++){
            asm.get(i).Assemble();
        }
        program = new Pic18Prog((int) Pic18Mem.PROGRAM_ORG,asm);
        SessionManager session = new SessionManager();
        System.out.println(session.writeToPlc(program.getCodeHex().array()));
    }
}
