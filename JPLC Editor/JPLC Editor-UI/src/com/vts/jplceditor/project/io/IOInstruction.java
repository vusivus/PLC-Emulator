/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project.io;

import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.ld.layout.Size;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class IOInstruction {
    private double locationX;
    private double locationY;
    private Size size;
    private PLCOperator instruction;
    private List<PLCOperand> parameters;

    /**
     *
     * @param locationX
     * @param locationY
     */
    public IOInstruction(double locationX, double locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }

    /**
     *
     * @return
     */
    public double getLocationX() {
        return locationX;
    }

    /**
     *
     * @param locationX
     */
    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    /**
     *
     * @return
     */
    public double getLocationY() {
        return locationY;
    }

    /**
     *
     * @param locationY
     */
    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    /**
     *
     * @return
     */
    public Size getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     *
     * @return
     */
    public PLCOperator getInstruction() {
        return instruction;
    }

    /**
     *
     * @param instruction
     */
    public void setInstruction(PLCOperator instruction) {
        this.instruction = instruction;
    }

    /**
     *
     * @return
     */
    public List<PLCOperand> getParameters() {
        return parameters;
    }

    /**
     *
     * @param parameters
     */
    public void setParameters(List<PLCOperand> parameters) {
        this.parameters = parameters;
    }
       
}
