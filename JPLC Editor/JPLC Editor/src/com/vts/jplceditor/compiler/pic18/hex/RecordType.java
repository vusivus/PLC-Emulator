/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.hex;

/**
 *
 * @author Vusivus
 */
public enum RecordType {
    DATA(0),
    EOF(1),
    EXT_SEG_ADDR(2),
    START_SEG_ADDR(3),
    EXT_LINEAR_ADDR(4),
    START_LINEAR_ADDR(5);

    private final int code;

    private RecordType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
