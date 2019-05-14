/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.mem;

/**
 *
 * @author Vusivus
 */
public class MemoryRange {
    private final int startAddress;
    private final int endAddress;

    public MemoryRange(int startAddress, int endAddress) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public int getEndAddress() {
        return endAddress;
    }
    
    public boolean contains(final int address) {
            return startAddress <= address && address <= endAddress;
        }
       
}
