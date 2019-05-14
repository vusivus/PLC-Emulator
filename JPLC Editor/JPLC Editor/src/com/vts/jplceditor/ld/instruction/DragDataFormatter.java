/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction;

/**
 *
 * @author Vusivus
 */
public class DragDataFormatter {

    public static String TagType_BIT = "bit";
    public static String TagType_BYTE = "byte";
    public static String TagType_INT = "integer";
    public static String TagType_FLOAT = "float";

    public static String DragData_TAG = "tag";
    public static String DragData_INSTRUCTION = "instruction";

    private static String[] tagType = new String[]{
        TagType_BIT, TagType_BYTE, TagType_INT, TagType_FLOAT};
    private static String[] dragDataType = new String[]{
        DragData_TAG, DragData_INSTRUCTION
    };

    public static String getTagType(String data) {
        if (validTagData(data)) {
            String[] split = splitData(data);
            return split[2];
        } else {
            return null;
        }
    }

    public static String getTagName(String data) {
        if (validTagData(data)) {
            String[] split = splitData(data);
            return split[1];
        } else {
            return null;
        }
    }

    public static String getInstructionName(String data) {
        if (validInstructionData(data)) {
            String[] split = splitData(data);
            return split[1];
        } else {
            return null;
        }
    }

    private static boolean validTagData(String data) {
        boolean valid;
        String[] split = splitData(data);
        if (split.length != 3) {
            return false;
        } else {
            valid = validateDataType(split[0]);
            if (!valid) {
                return false;
            } else {
                valid = validateTagType(split[2]);
                return valid;
            }
        }
    }

    private static boolean validInstructionData(String data) {
        boolean valid;
        String[] split = splitData(data);
        if (split.length != 3) {
            return false;
        } else {
            return split[0] == DragData_INSTRUCTION;
        }
    }

    private static String[] splitData(String data) {
        return data.split("_");
    }

    private static boolean validateDataType(String get) {
        return get.equals(DragData_TAG) || get.equals(DragData_INSTRUCTION);
    }

    private static boolean validateTagType(String get) {
        for (String tagType1 : tagType) {
            if (get.equals(tagType1)) {
                return true;
            }
        }
        return false;
    }

    public static String formatTag(String tagName, String tagType) {
        return DragData_TAG + "_" + tagName + "_" + tagType;
    }

    public static String formatInstruction(String iName, String iType) {
        return DragData_INSTRUCTION + "_" + iName + "_" + iType;
    }
}
