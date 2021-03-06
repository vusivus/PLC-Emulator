package com.vts.jplceditor.compiler.pic18.hex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexRecordParser {
    public static final Pattern HEXRECORD_PATTERN = Pattern
            .compile(":([a-fA-F0-9]{2})([a-fA-F0-9]{4})([a-fA-F0-9]{2})(([a-fA-F0-9]{2})*)([a-fA-F0-9]{2})");

    @SuppressWarnings("unchecked")
    public static <T extends HexRecord> T parse(final String line) {
        final Matcher recordMatcher = HexRecordParser.HEXRECORD_PATTERN.matcher(line);
        if (!recordMatcher.matches()) {
            throw new IllegalArgumentException("wrong format " + line);
        }
        HexRecordParser.validateChecksum(line);
        final int length = Integer.parseInt(recordMatcher.group(1), 16);
        final int address = Integer.parseInt(recordMatcher.group(2), 16);
        final RecordType recordType = RecordType.values()[Integer.parseInt(recordMatcher.group(3), 16)];
        final String data = recordMatcher.group(4);
        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(data.substring(2 * i, 2 * i + 2), 16);
        }
        switch (recordType) {
        case EOF:
            return (T) new EOFRecord();
        case DATA:
            return (T) new DataRecord(length, address, bytes);
        case EXT_SEG_ADDR:
            return (T) new ExtendedSegmentAddressRecord(length, address, bytes);
        case START_SEG_ADDR:
            return (T) new StartSegmentAddressRecord(length, address, bytes);
        case EXT_LINEAR_ADDR:
            return (T) new ExtendedLinearAddressRecord(length, address, bytes);
        case START_LINEAR_ADDR:
            return (T) new StartLinearRecord(length, address, bytes);
        default:
            throw new IllegalStateException("dont know record type " + recordType);
        }
    }

    private static void validateChecksum(final String line) {
        int checksum = 0;
        for (int i = 1; i + 1 < line.length(); i += 2) {
            final int byteValue = Integer.parseInt(line.substring(i, i + 2), 16);
            checksum += byteValue;
        }
        if ((checksum & 0x00ff) != 0) {
            throw new IllegalArgumentException("wrong checksum: " + (checksum & 0x0ff) + " in line " + line);
        }
    }
}
