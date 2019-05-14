/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project.io;

import com.vts.jplceditor.ld.component.node.NodeType;

/**
 *
 * @author Vusivus
 */
public class IONode {
    private NodeType nodeType;
    private double locationX;
    private double locationY;

    /**
     *
     * @param nodeType
     * @param locationX
     * @param locationY
     */
    public IONode(NodeType nodeType, double locationX, double locationY) {
        this.nodeType = nodeType;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    /**
     *
     * @return
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     *
     * @param nodeType
     */
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
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
       
}
