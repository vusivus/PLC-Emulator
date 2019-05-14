/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class LDJoin{
    private Line sLine, hLine;
    private Point2D startPoint, endPoint;

    public LDJoin(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        buildHLine();
        buildSLine();
    }

    private void buildHLine() {
        hLine = new Line();
        hLine.setStrokeWidth(2);
        hLine.setStartX(startPoint.getX());
        hLine.setEndX(endPoint.getX());
        hLine.setStartY(endPoint.getY());
        hLine.setEndY(endPoint.getY());
    }

    private void buildSLine() {
        sLine = new Line();
        sLine.setStrokeWidth(2);
        sLine.setStartX(startPoint.getX());
        sLine.setEndX(startPoint.getX());
        sLine.setStartY(startPoint.getY());
        sLine.setEndY(endPoint.getY());
    }

    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
        sLine.setStartX(startPoint.getX());
        sLine.setEndX(startPoint.getX());
        sLine.setStartY(startPoint.getY());
        hLine.setStartX(startPoint.getX());
    }

    public void setEndPoint(Point2D endPoint) {
        this.endPoint = endPoint;
        sLine.setEndY(endPoint.getY());
        hLine.setEndX(endPoint.getX());
        hLine.setStartY(endPoint.getY());
        hLine.setEndY(endPoint.getY());
    }

    public Line getsLine() {
        return sLine;
    }

    public Line gethLine() {
        return hLine;
    }
    
}
