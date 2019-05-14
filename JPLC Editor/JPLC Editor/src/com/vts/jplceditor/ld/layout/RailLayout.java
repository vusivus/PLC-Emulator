/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.layout;

import com.vts.jplceditor.ld.component.node.SNode;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class RailLayout {
    private List <SNode> sourceNodes;
    private double width;
    private double height;
    private double railHeight,rungWidth;
    private Point2D railStart, railEnd;
    public static final int RUNG_SIZE = 150;
    

    public RailLayout(double width, double height) {
        this.width = width;
        this.height = height;
        createRails();
        createSourceNodes();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private void createRails() {
       double x = 0.05*width;
       double y1 = 0.05*height;
       double y2 = 0.95*height;
       railStart = new Point2D(x,y1);
       railEnd = new Point2D(x,y2);
       railHeight = y2-y1;
       rungWidth = 0.1*width;
    }

    public Point2D getRailStart() {
        return railStart;
    }

    public Point2D getRailEnd() {
        return railEnd;
    }

    private void createSourceNodes() {
        sourceNodes = new ArrayList<>();
        addNewSourceNode();
    }

    public List<SNode> getSourceNodes() {
        return sourceNodes;
    }
    
   public void addNewSourceNode(){
       int count = sourceNodes.size()+1;
       double y = railStart.getY() + (RUNG_SIZE*count);
       Point2D loc = new Point2D(railStart.getX(),y);
       SNode con = new SNode(loc);
       sourceNodes.add(con);
   } 
}
