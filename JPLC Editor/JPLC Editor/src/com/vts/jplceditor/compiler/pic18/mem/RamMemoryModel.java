/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.mem;

import java.util.List;

/**
 *
 * @author Vusivus
 */
public class RamMemoryModel {   

    final List<MemoryRange> gpRegisters;
    final List<MemoryRange> sfRegisters;

    public RamMemoryModel(List<MemoryRange> gpRegisters, List<MemoryRange> sfRegisters) {
        this.gpRegisters = gpRegisters;
        this.sfRegisters = sfRegisters;
    }

    public List<MemoryRange> getGpRegisters() {
        return gpRegisters;
    }

    public List<MemoryRange> getSfRegisters() {
        return sfRegisters;
    }
   
}
