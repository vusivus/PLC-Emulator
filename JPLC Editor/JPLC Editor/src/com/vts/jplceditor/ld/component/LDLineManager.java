/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import javafx.geometry.Point2D;
import javafx.scene.Group;

/**
 *
 * @author Vusivus
 */
public class LDLineManager extends Group{

    public LDLineManager() {
        
    }
    
    public void addLine(Point2D sPoint, Point2D tPoint){
        getChildren().add(new LDLine(sPoint,tPoint));
    }
    
    public void relocateLine(String id,Point2D p1,Point2D p2){
        LDLine temp;
        for(int i=0;i<= getChildren().size();i++){
            if(getChildren().get(i).getId() == null ? id == null : getChildren().get(i).getId().equals(id)){
                temp = (LDLine) getChildren().get(i);
                temp.setStartPoint(p1);
                temp.setEndPoint(p2);
            }
            
        }
    }
    
    public void clearAll(){
        getChildren().clear();
    }
}
