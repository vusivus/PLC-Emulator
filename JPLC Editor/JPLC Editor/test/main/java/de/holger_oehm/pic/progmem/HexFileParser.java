package de.holger_oehm.pic.progmem;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import de.holger_oehm.pic.progmem.hexfile.DataRecord;
import de.holger_oehm.pic.progmem.hexfile.HexFileRecord;
import de.holger_oehm.pic.progmem.hexfile.HexRecordParser;
import de.holger_oehm.pic.progmem.hexfile.LinearAddressRecord;
import de.holger_oehm.pic.progmem.hexfile.SegmentRecord;

public class HexFileParser {
    private final List<String> warnings = new ArrayList<>();

    public PicMemory parse(final Reader content) throws IOException {
        warnings.clear();
        boolean gotEof = false;
        int currentOffset = 0;
        final PicMemory result = new PicMemory();
        final LineNumberReader reader = new LineNumberReader(content);
        do {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (gotEof) {
                warnings.add("content after EOF record: " + line);
                continue;
            }
            final HexFileRecord record = HexRecordParser.parse(line);
            switch (record.getType()) {
            case EOF:
                gotEof = true;
                break;
            case DATA:
                final DataRecord dataRecord = (DataRecord) record;
                final byte[] bytes = dataRecord.getBytes();
                final int address = currentOffset + dataRecord.getAddress();
                result.setBytes(address, bytes);
                break;
            case SEG_ADDR:
                final SegmentRecord segmentRecord = (SegmentRecord) record;
                currentOffset = segmentRecord.getUpperAddress();
                break;
            case LIN_ADDR:
                final LinearAddressRecord linearAddressRecord = (LinearAddressRecord) record;
                currentOffset = linearAddressRecord.getUpperAddress();
                break;
            }
        } while (true);
        if (!gotEof) {
            warnings.add("EOF record is missing");
        }
        return result;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public PicMemory parse(final File file) throws IOException {
        try (final Reader reader = new FileReader(file)) {
            return parse(reader);
        }
    }
}
