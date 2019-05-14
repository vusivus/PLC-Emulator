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
public enum PLCDataType {
   BIT(0),
   BYTE(1),
   INT(2),
   FLOAT(4),
   LITERAL(2);   
   
   private final int byteCount;

    private PLCDataType(int byteCount) {
        this.byteCount = byteCount;
    }

    public int getByteCount() {
        return byteCount;
    }
     
}
