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
public class ProgramMemoryModel {

    private final MemoryRange programMemory;
    private final MemoryRange idLocations;
    private final MemoryRange configFuses;
    private final MemoryRange deviceId;
    private final MemoryRange eepromData;

    public ProgramMemoryModel(MemoryRange programMemory, MemoryRange idLocations, MemoryRange configFuses, MemoryRange deviceId, MemoryRange eepromData) {
        this.programMemory = programMemory;
        this.idLocations = idLocations;
        this.configFuses = configFuses;
        this.deviceId = deviceId;
        this.eepromData = eepromData;
    }

    public static ProgramMemoryModel forPic18f2550() {
        ProgramMemoryModel memory;
        MemoryRange code = new MemoryRange(0x20, 0x7FFF);
        MemoryRange idlocs = new MemoryRange(0x200000, 0x200007);
        MemoryRange conf = new MemoryRange(0x300000, 0x30000d);
        MemoryRange id = new MemoryRange(0x3ffffe, 0x3fffff);
        MemoryRange eeprom = new MemoryRange(0xf00000, 0xf000ff);
        memory = new ProgramMemoryModel(code, idlocs, conf, id, eeprom);
        return memory;
    }

    public MemoryRange getProgramMemory() {
        return programMemory;
    }

    public MemoryRange getIdLocations() {
        return idLocations;
    }

    public MemoryRange getConfigFuses() {
        return configFuses;
    }

    public MemoryRange getDeviceId() {
        return deviceId;
    }

    public MemoryRange getEepromData() {
        return eepromData;
    }

}
