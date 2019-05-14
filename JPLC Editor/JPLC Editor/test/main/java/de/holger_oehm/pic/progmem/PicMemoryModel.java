package de.holger_oehm.pic.progmem;

public class PicMemoryModel {
    public static final PicMemoryModel PIC18F13K50 = new PicMemoryModel( //
            new Range(0x0800, 0x1fff), // program memory without boot block
            new Range(0x200000, 0x200007), // id locations
            new Range(0x300000, 0x30000d), // configuration fuses
            new Range(0x3ffffe, 0x3fffff), // device id
            new Range(0xf00000, 0xf000ff) // EEPROM
    );

    public static final class Range {
        private final int start;
        private final int end;

        Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public boolean contains(final int address) {
            return start <= address && address <= end;
        }
    }

    private final Range code;
    private final Range idLocation;
    private final Range config;
    private final Range deviceId;
    private final Range eepromData;

    private PicMemoryModel(final Range code, final Range idLocation, final Range config, final Range deviceId,
            final Range eepromData) {
        this.code = code;
        this.idLocation = idLocation;
        this.config = config;
        this.deviceId = deviceId;
        this.eepromData = eepromData;
    }

    public Range getCode() {
        return code;
    }

    public Range getIdLocation() {
        return idLocation;
    }

    public Range getConfig() {
        return config;
    }

    public Range getDeviceId() {
        return deviceId;
    }

    public Range getEepromData() {
        return eepromData;
    }
}
