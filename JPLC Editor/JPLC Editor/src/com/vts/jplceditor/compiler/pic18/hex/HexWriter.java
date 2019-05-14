/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.pic18.hex;

import com.vts.jplceditor.compiler.pic18.Pic18Prog;

/**
 *
 * @author Vusivus
 */
public class HexWriter {

    public static final int MAX_LINE_LEN = 0x10;
    private final Pic18Prog program;
    private final StringBuffer hexContent;

    public HexWriter(Pic18Prog program) {
        this.program = program;
        hexContent = new StringBuffer();
    }

    public StringBuffer getHexContent() {
        return hexContent;
    }

    private int upper(final int address) {
        return (address >> 16) & 0xffff;
    }

    private void writeEOF() {
        writeLine(0, 0, 1, "", 0);
    }

    private void writeLine(final int len, final int address, final int type, final String value, final int checksum) {
        hexContent.append(":" + to2hex(len) + to4hex(address) + to2hex(type) + value
                + to2hex(-len - (address >> 8) - address - type - checksum) + "\n");
    }

    private String to2hex(final int value) {
        return String.format("%02x", value & 0xff);
    }

    private String to4hex(final int value) {
        return String.format("%04x", value & 0xffff);
    }

    public void writeOS() {
        hexContent.append(":101D16001AEC0FF0B886B06ACF0EAF6EAC8413DD46\n"
                + ":101D2600020E0B6E040E0C6EBA0E0D6E0D2EFED745\n"
                + ":101D36000C2EFCD70B2EFAD700000F0EC112070E81\n"
                + ":101D4600B4129280516A94909494816A936A8290B4\n"
                + ":101D56008294736B706B7F6B78DF010EE56F1DEC01\n"
                + ":101D660009F00C0EE56F1DEC09F080A002D0F3DE41\n"
                + ":081D760001D012DEFAD7FFD7FD\n"
                + ":101E340010EE29F0270E006E010E016EA70EF66E4D\n"
                + ":101E44001D0EF76E000EF86ED2EC0BF010EE80F063\n"
                + ":101E5400110E006E010E016E010EF66E1E0EF76E6F\n"
                + ":0A1E6400000EF86ED2EC0BF0120035\n"
                + ":101D7E00090229000101008032090400000203005B\n"
                + ":101D8E000000092101010001222100070581034005\n"
                + ":091D9E00000107050103400001EA\n"
                + ":091DA70052554E204D4F444500F9\n"
                + ":0D1DB00050524F4752414D204D4F444500C9\n"
                + ":101DBD0050524F4752414D4D494E472E2E2E2E2EED\n"
                + ":011DCD000015\n"
                + ":101DCE000600FF0901A10119012940150026FF0097\n"
                + ":101DDE0075089540810219012940750895409102B8\n"
                + ":011DEE00C034\n"
                + ":101DEF00120100020000000834120100010001027C\n"
                + ":021DFF000001E1\n"
                + ":101E0100434F4E4E454354454420544F2050432E9A\n"
                + ":011E110000D0\n"
                + ":101E120010035600540020004C0061006200730061\n"
                + ":0E1E22000E0356005400200050004C004300F8\n"
                + ":041E3000040309049A\n");
    }

    public void writeConfig() {
        hexContent.append(":020000040030CA\n"
                + ":0E000000210C391EFF8081FF0FC00FE00F4062\n"
                + ":00000001FF");
    }

    public void writeCode() {
        int address = program.getOrg();
        int size = program.getSize();
        int pointer = 0;
        while (size > 0) {
            final StringBuilder value = new StringBuilder();
            int checksum = 0;
            int length = 0;

            if (size >= MAX_LINE_LEN) {
                size -= MAX_LINE_LEN;
                length = MAX_LINE_LEN;
            } else {
                length = size;
                size = 0;
            }
            for (int idx = 0; idx < length; idx++) {
                final int b = program.readCodeByte(pointer);
                value.append(to2hex(b));
                checksum += b;
                ++pointer;
            }
            if (length > 0) {
                writeLine(length, address, 0, value.toString(), checksum);
            }
            address += length;
        }
    }

    private void writeConfigMem() {
        writeLine(2, 0, 4, "0030", 0xCA);
        final StringBuilder value = new StringBuilder();
        int checksum = 0;
        for (int idx = 0; idx < 14; idx++) {
            final int b = program.readConfigByte();
            value.append(to2hex(b));
            checksum += b;
        }
        writeLine(14, 0x300000, 0, value.toString(), checksum);
    }

    private void writeDevId() {

    }

}
