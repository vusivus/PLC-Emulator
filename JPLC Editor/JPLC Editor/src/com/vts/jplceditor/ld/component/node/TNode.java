/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component.node;

import com.vts.jplceditor.ld.component.LDNode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Vusivus
 */
public class TNode extends Vertex implements LDNode{

    private int inDegree,outDegree;
    public TNode(Point2D location) {
        super(location);
        setNodeType(NodeType.Termination);
        setFill(Color.RED);
    }
    
    @Override
    public void setInDegree(int inDegree) {
        this.inDegree = inDegree;
    }

    @Override
    public void setOutDegree(int outDegree) {
        this.outDegree = outDegree;
    }

    @Override
    public int getInDegree() {
        return inDegree;
    }

    @Override
    public int getOutDegree() {
        return outDegree;
    }

    @Override
    public NodeType getNodeType(){
        return NodeType.Termination;
    }

}
