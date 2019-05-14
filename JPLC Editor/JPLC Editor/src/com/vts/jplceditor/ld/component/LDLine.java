/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import java.util.HashMap;
import java.util.UUID;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

/**
 *Represents a line on a ladder diagram
 * @author Vusumuzi_Tshabangu
 */
public class LDLine extends Line{

    /**
     *
     * @param startPoint
     * @param endPoint
     */
    public LDLine(Point2D startPoint, Point2D endPoint) {
        setStartX(startPoint.getX());
        setStartY(startPoint.getY());
        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
        setStrokeWidth(2);
        setId(UUID.randomUUID().toString());
    }

    /**
     *Relocates the starting point
     * @param startPoint
     */
    public void setStartPoint(Point2D startPoint){
        setStartX(startPoint.getX());
        setStartY(startPoint.getY());
    }
    
    /**
     *Relocates the end point
     * @param endPoint
     */
    public void setEndPoint(Point2D endPoint){
        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
    }

}
