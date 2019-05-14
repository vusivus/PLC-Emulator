/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc;

/**
 *
 * @author Vusivus
 */
public class PLCOperatorDecoder {

    public static PLCOperator fromID(String instructionID) {
        PLCOperator instruction = null;
        switch (instructionID) {
            case "XIC":
                instruction = PLCOperator.XIC;
                break;
            case "XIO":
                instruction = PLCOperator.XIO;
                break;
            case "OTE":
                instruction = PLCOperator.OTE;
                break;
            case "OTL":
                instruction = PLCOperator.OTL;
                break;
            case "OTU":
                instruction = PLCOperator.OTU;
                break;
            case "TON":
                instruction = PLCOperator.TON;
                break;
            case "CTU":
                instruction = PLCOperator.CTU;
                break;
            case "CTD":
                instruction = PLCOperator.CTD;
                break;
            case "EQU":
                instruction = PLCOperator.EQU;
                break;
            case "GRT":
                instruction = PLCOperator.GRT;
                break;
            case "LES":
                instruction = PLCOperator.LES;
                break;
            case "LIM":
                instruction = PLCOperator.LIM;
                break;
            case "MOV":
                instruction = PLCOperator.MOV;
                break;
            case "MVM":
                instruction = PLCOperator.MVM;
                break;
            case "AND":
                instruction = PLCOperator.AND;
                break;
            case "NOT":
                instruction = PLCOperator.NOT;
                break;
            case "OR":
                instruction = PLCOperator.OR;
                break;
            case "XOR":
                instruction = PLCOperator.XOR;
                break;
            case "ADD":
                instruction = PLCOperator.ADD;
                break;
            case "DIV":
                instruction = PLCOperator.DIV;
                break;
            case "SUB":
                instruction = PLCOperator.SUB;
                break;
            case "MUL":
                instruction = PLCOperator.MUL;
                break;
        }
        return instruction;
    }
}
