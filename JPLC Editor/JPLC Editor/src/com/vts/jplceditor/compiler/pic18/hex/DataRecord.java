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
public class DataRecord extends HexRecord{
    
    public DataRecord(int length, int address, byte[] bytes) {
        super(length, address, bytes, RecordType.DATA);        
    }
    
}
