/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.compiler.plc;

import com.vts.jplceditor.compiler.pic18.PicAddress;

/**
 *
 * @author Vusivus
 */
public class PLCOperand {

    private PLCDataType dataType;
    private String identity;
    private PicAddress address;

    public PLCOperand(PLCDataType dataType, String identity) {
        this.dataType = dataType;
        this.identity = identity;
    }

    public PLCDataType getDataType() {
        return dataType;
    }

    public void setDataType(PLCDataType dataType) {
        this.dataType = dataType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
        if (isInteger(identity)) {
            dataType = PLCDataType.LITERAL;
        }
    }

    public PicAddress getAddress() {
        return address;
    }

    public void setAddress(PicAddress address) {
        this.address = address;
    }

    public static boolean isInteger(String s) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }
}
