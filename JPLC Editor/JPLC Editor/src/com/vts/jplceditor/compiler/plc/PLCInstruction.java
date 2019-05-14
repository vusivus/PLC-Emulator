/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class PLCInstruction {
    private final PLCOperator operator;
    private List <PLCOperand> operands;

    public PLCInstruction(PLCOperator operator) {
        this.operator = operator;
        operands = new ArrayList<>();
    }

    public PLCOperator getOperator() {
        return operator;
    }

    public void setOperands(List<PLCOperand> operands) {
        this.operands = operands;
    }

    public void addOperand(PLCOperand operand) {
        operands.add(operand);
    }

    public List<PLCOperand> getOperands() {
        return operands;
    }
        
}
