/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component.node;

import com.vts.jplceditor.ld.component.LDNode;
import com.vts.jplceditor.ld.component.LDParent;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Vusivus
 */
public class BNode extends Vertex implements LDNode,LDParent{

    private final List<LDNode> children;
    private int inDegree,outDegree;
    
    public BNode(Point2D location) {
        super(location);
        setNodeType(NodeType.Branch);
        setFill(Color.BLUE);
        children = new ArrayList<LDNode>();
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
        return NodeType.Branch;
    }
    
    @Override
    public void addChild(LDNode child) {
        children.add(child);
    }

    @Override
    public void atChildAt(int index, LDNode child) {
        children.add(index, child);
    }

    @Override
    public List<LDNode> getChildren() {
        return children;
    }

}
