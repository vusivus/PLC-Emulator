package de.holger_oehm.pic.progmem;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class HexFileWriter implements Closeable {
    public static final int MAX_LINE_LEN = 0x10;

    private final PrintWriter writer;
    private final PicMemory memory;

    public HexFileWriter(final PicMemory memory, final Writer writer) {
        this.memory = memory;
        this.writer = new PrintWriter(writer);
    }

    public HexFileWriter(final PicMemory memory, final File file) throws IOException {
        this(memory, new FileWriter(file));
    }

    public void write() {
        int upperAddress = -1;
        for (final int chunkAddress : memory.getChunkAddresses()) {
            if (upperAddress != upper(chunkAddress)) {
                upperAddress = upper(chunkAddress);
                println(2, 0, 4, to4hex(upperAddress), (upperAddress >> 8) + upperAddress);
            }
            for (int address = chunkAddress; address < chunkAddress + PicMemory.CHUNK_SIZE; address += MAX_LINE_LEN) {
                final StringBuilder value = new StringBuilder();
                int checksum = 0;
                int length = 0;
                for (int idx = 0; idx < MAX_LINE_LEN - 1; idx += 2) {
                    if (memory.getByte(address + idx) != 0xff || memory.getByte(address + idx + 1) != 0xff) {
                        length = idx + 2;
                    }
                }
                for (int idx = 0; idx < length; idx++) {
                    final int b = memory.getByte(address + idx);
                    value.append(to2hex(b));
                    checksum += b;
                }
                if (length > 0) {
                    println(length, address, 0, value.toString(), checksum);
                }
            }
        }
        printEOF();
    }

    private int upper(final int address) {
        return (address >> 16) & 0xffff;
    }

    private void printEOF() {
        println(0, 0, 1, "", 0);
    }

    private void println(final int len, final int address, final int type, final String value, final int checksum) {
        writer.print(":" + to2hex(len) + to4hex(address) + to2hex(type) + value
                + to2hex(-len - (address >> 8) - address - type - checksum) + "\n");
    }

    private String to2hex(final int value) {
        return String.format("%02x", value & 0xff);
    }

    private String to4hex(final int value) {
        return String.format("%04x", value & 0xffff);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
