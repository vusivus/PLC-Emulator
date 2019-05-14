/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction;

import com.vts.jplceditor.ld.instruction.compare.Equ;
import com.vts.jplceditor.ld.instruction.compare.Grt;
import com.vts.jplceditor.ld.instruction.compare.Les;
import com.vts.jplceditor.ld.instruction.compare.Lim;
import com.vts.jplceditor.ld.instruction.file.Mov;
import com.vts.jplceditor.ld.instruction.file.Mvm;
import com.vts.jplceditor.ld.instruction.logical.And;
import com.vts.jplceditor.ld.instruction.logical.Or;
import com.vts.jplceditor.ld.instruction.logical.Xor;
import com.vts.jplceditor.ld.instruction.math.Add;
import com.vts.jplceditor.ld.instruction.math.Div;
import com.vts.jplceditor.ld.instruction.math.Mul;
import com.vts.jplceditor.ld.instruction.math.Sub;
import com.vts.jplceditor.ld.instruction.relay.Xic;
import com.vts.jplceditor.ld.instruction.relay.Xio;
import com.vts.jplceditor.ld.instruction.relay.Ote;
import com.vts.jplceditor.ld.instruction.relay.Otl;
import com.vts.jplceditor.ld.instruction.relay.Otu;
import com.vts.jplceditor.ld.instruction.timer.Ctd;
import com.vts.jplceditor.ld.instruction.timer.Ctu;
import com.vts.jplceditor.ld.instruction.timer.Ton;

/**
 *
 * @author Vusivus
 */
public class InstructionUtil {

    public static Instruction getInstruction(String name) {
        switch (name) {
            case "XIC":
                return new Xic();
            case "XIO":
                return new Xio();
            case "OTE":
                return new Ote();
            case "OTL":
                return new Otl();
            case "OTU":
                return new Otu();
            case "TON":
                return new Ton();
            case "CTU":
                return new Ctu();
            case "CTD":
                return new Ctd();
            case "EQU":
                return new Equ();
            case "GRT":
                return new Grt();
            case "LES":
                return new Les();
            case "LIM":
                return new Lim();
            case "AND":
                return new And();
            case "OR":
                return new Or();
            case "XOR":
                return new Xor();
            case "ADD":
                return new Add();
            case "DIV":
                return new Div();
            case "MUL":
                return new Mul();
            case "SUB":
                return new Sub();
            case "MOV":
                return new Mov();
            case "MVM":
                return new Mvm();
            default:
                return null;
        }
    }
    
    public static boolean isBranch(String name){
        return "BRN".equals(name);
    }
}
