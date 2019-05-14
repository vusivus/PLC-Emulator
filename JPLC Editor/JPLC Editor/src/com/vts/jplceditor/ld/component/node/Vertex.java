/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component.node;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

/**
 *
 * @author Vusivus
 */
public class Vertex extends Circle{
    Point2D location;
    NodeType nodeType;

    public Vertex(Point2D location) {
        super(location.getX(),location.getY(),6);
        this.location = location;       
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
        setCenterX(location.getX());
        setCenterY(location.getY());
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }
    
    
}
